package nl.practicom.c4w.multidll

import nl.practicom.c4w.txa.transform.SectionMark
import nl.practicom.c4w.txa.transform.StreamingTxaTransform
import nl.practicom.c4w.txa.transform.TxaContext

class ProcedureExtractor extends StreamingTxaTransform {

    List<Procedure> procedures = []

    Procedure getProcedureByName(String name){
        procedures.find{ p -> p.name.equalsIgnoreCase(name)}
    }

    private Procedure currentProcedure = null

    @Override
    protected String transformSectionStart(TxaContext ctx, SectionMark section) {
        if (isProcedureDeclaration(ctx, section)) {
            currentProcedure = new Procedure()
            currentProcedure.lineNumber = ctx.currentLineNumber
        }

        // Suppress content outside procedures
        return currentProcedure ? super.transformSectionStart(ctx, section) : null
    }

    @Override
    protected String transformSectionContent(TxaContext ctx, SectionMark section, String content) {
        final TEMPLATE_DECL = ~/^FROM\s+(\w[\w\s]+)\s*$/

        if ( currentProcedure ) {
            currentProcedure.name = currentProcedure.name ?: ctx.currentProcedureName

            if (!currentProcedure.template) {
                (content =~ TEMPLATE_DECL).each {
                    _, templateName -> currentProcedure.template = templateName
                }
            }
        }

        // Suppress content outside procedures
        return currentProcedure ? super.transformSectionContent(ctx,section,content) : null
    }

    @Override
    protected String transformSectionEnd(TxaContext ctx, SectionMark section) {
        // End of the current procedure: store it and set it to null until
        // we receive the next [PROCEDURE] start mark
        if ( currentProcedure && isProcedureDeclaration(ctx, section)){
            appendProcedure()
            currentProcedure = null
        }

        return currentProcedure ? super.transformSectionEnd(ctx, section) : null
    }

    @Override
    protected String transformFinalize(TxaContext context) {
        appendProcedure()
        currentProcedure = null
    }

    // Copy collected content to procedure body and clear the contents
    def appendProcedure() {
        if (currentProcedure) {
            currentProcedure.body.append(super.getContent())
            this.procedures << currentProcedure
            super.clear()
        }
    }

    def static isProcedureDeclaration(TxaContext context,SectionMark section) {
        section == SectionMark.PROCEDURE && !context.within(SectionMark.DEFINITION)
    }
}
