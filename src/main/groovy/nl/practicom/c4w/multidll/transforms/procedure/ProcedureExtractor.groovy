package nl.intreq.c4w.multidll.transforms.procedure

import nl.intreq.c4w.multidll.dto.Procedure
import nl.intreq.c4w.multidll.io.ProcedureWriter
import nl.intreq.c4w.multidll.transforms.procedure.ProcedureTransform
import nl.intreq.c4w.multidll.transforms.procedure.ProcedureTransformFactory
import nl.intreq.c4w.txa.transform.SectionMark
import nl.intreq.c4w.txa.transform.StreamingTxaTransform
import nl.intreq.c4w.txa.transform.TxaContext

import static nl.intreq.c4w.txa.transform.SectionMark.*

/**
 * The ProcedureExtractor extracts specific procedures from a
 * source txa and writes them out.
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
        if (ctx.isProcedureDeclaration(section)) {
            if (currentProcedure){
                writeProcedure()
            }
            currentProcedure = new Procedure()
            currentProcedure.lineNumber = ctx.currentLineNumber
            currentTransform = null
            return section.toString()
        } else {
            if (ctx.within(PROCEDURE)) {
                if (currentProcedure != null && currentTransform != null) {
                    return currentTransform.transformSectionStart(ctx, section)
                } else {
                    return null
                }
            }
        }
    }

    @Override
    protected String transformSectionContent(TxaContext ctx, SectionMark section, String content) {
        final TEMPLATE_DECL = ~/^FROM\s+(\w[\w\s]+)\s*$/

        if ( currentProcedure ) {
            if ( currentProcedure.name == null && ctx.currentProcedureName != null ){
                currentProcedure.name = ctx.currentProcedureName
                def t = transformFactory.getTransform(currentProcedure)
                if ( t ){
                    currentTransform = t
                } else {
                    currentTransform = null
                    currentProcedure = null
                    super.clear() // clear [PROCEDURE] section marker
                }
            }
        }

        if ( ctx.within(PROCEDURE) ) {
            // Extract template for current procedure
            if (currentProcedure && !currentProcedure.template) {
                (content =~ TEMPLATE_DECL).each {
                    _, templateName -> currentProcedure.template = templateName
                }
            }

            if (currentTransform && currentProcedure) {
                return currentTransform.transformSectionContent(ctx, section, content)
            } else {
                return null
            }

        }
    }

    @Override
    protected String transformSectionEnd(TxaContext ctx, SectionMark section) {
        if (ctx.within(PROCEDURE)) { // only process sections within the procedure
            if (currentProcedure && currentTransform) {
                currentTransform.transformSectionEnd(ctx, section)
            }
        }
    }

    @Override
    protected String transformInitialize(TxaContext context) {
        destination.open()
    }

    @Override
    protected String transformFinalize(TxaContext context) {
        if (currentProcedure) {
            writeProcedure()
        }
        currentProcedure = null
        destination.close()
    }

    // Copy collected content to procedure body and clear the contents
    def writeProcedure() {
        if (currentProcedure) {
            currentProcedure.body.append(super.getContent())
            destination.write(currentProcedure)
            destination.flush()
            super.clear()
        }
    }
}
