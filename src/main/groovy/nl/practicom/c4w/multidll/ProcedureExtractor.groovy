package nl.practicom.c4w.multidll

import nl.practicom.c4w.txa.transform.SectionMark
import nl.practicom.c4w.txa.transform.StreamingTxaTransform
import nl.practicom.c4w.txa.transform.TxaContext
/**
 * The ProcedureExtractor extracts specific procedures from a
 * source txa and writes them either collectively to a new txa
 * file or individually to a separate txa file per procedure.
 *
 */
class ProcedureExtractor extends StreamingTxaTransform {

    private ProcedureTransformFactory transformFactory
    private ProcedureWriter destination

    private Procedure currentProcedure = null
    private ProcedureTransform currentTransform = null

    /**
     * Apply transformation to procedures and write the transformed procedures
     * @param transformFactory - provides the transformation to apply
     * @param destination - ProcedureWriter instance handling the actual writing
     */
    ProcedureExtractor(ProcedureTransformFactory transformFactory, ProcedureWriter destination){
        super(new StringBuffer(4000)) // Collect content in buffer
        this.transformFactory = transformFactory
        this.destination = destination
    }

    @Override
    protected String transformSectionStart(TxaContext ctx, SectionMark section) {
        if (isProcedureDeclaration(ctx, section)) {
            currentProcedure = new Procedure()
            currentProcedure.lineNumber = ctx.currentLineNumber
            currentTransform = null
            return section.toString()
        } else {
            if (currentProcedure != null && currentTransform != null) {
                currentTransform.transformSectionStart(ctx, section)
            } else {
                null
            }
        }
    }

    @Override
    protected String transformSectionContent(TxaContext ctx, SectionMark section, String content) {
        final TEMPLATE_DECL = ~/^FROM\s+(\w[\w\s]+)\s*$/

        if ( currentProcedure ) {
            if ( currentProcedure.name == null && ctx.currentProcedureName != null ){
                currentProcedure.name = ctx.currentProcedureName
                def t = transformFactory.getTransform(currentProcedure.name)
                if ( t != null){
                    currentTransform = t
                } else {
                    currentProcedure = null
                    super.clear() // clear [PROCEDURE] section marker
                }
            }

            if (!currentProcedure.template) {
                (content =~ TEMPLATE_DECL).each {
                    _, templateName -> currentProcedure.template = templateName
                }
            }
        }

        if (currentProcedure != null && currentTransform != null) {
            currentTransform.transformSectionContent(ctx, section, content)
        } else {
            null
        }
    }

    @Override
    protected String transformSectionEnd(TxaContext ctx, SectionMark section) {
        // End of the current procedure: store it and set it to null until
        // we receive the next [PROCEDURE] start mark
        if ( isProcedureDeclaration(ctx, section)){
            if ( currentProcedure && currentTransform) {
                writeProcedure()
                currentProcedure = null
                currentTransform = null
                null
            }
        } else {
            if ( currentProcedure && currentTransform){
                currentTransform.transformSectionEnd(ctx, section)
            }
        }
    }

    @Override
    protected String transformFinalize(TxaContext context) {
        if ( currentProcedure) {
            writeProcedure()
        }
        currentProcedure = null
    }

    // Copy collected content to procedure body and clear the contents
    def writeProcedure() {
        if (currentProcedure) {
            currentProcedure.body.append(super.getContent())
            destination.write(currentProcedure)
            super.clear()
        }
    }

    /* Support methods */

    def static isProcedureDeclaration(TxaContext context,SectionMark section) {
        section == SectionMark.PROCEDURE && !context.within(SectionMark.DEFINITION)
    }
}
