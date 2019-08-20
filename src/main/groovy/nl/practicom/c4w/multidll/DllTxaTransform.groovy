package nl.practicom.c4w.multidll

import nl.practicom.c4w.txa.transform.SectionMark
import nl.practicom.c4w.txa.transform.StreamingTxaTransform
import nl.practicom.c4w.txa.transform.TxaContext

import static nl.practicom.c4w.txa.transform.SectionMark.*

/**
 * Generates a TXA file for a DLL by copying procedures from the source TXA.
 * The generated TXA will contain publicly visible (exported) procedures as
 * well as internal (non-exported) procedures organized in modules.
 */
class DllTxaTransform extends StreamingTxaTransform {
    private publicProcedures = []
    private privateProcedures = []
    private File targetTxaFile
    int numProceduresPerModule = 20

    protected Writer out = null

    /**
     * @param targetTxaFile - absolute path where DLL txa should be generated
     * @param publicProcedures - list of procedures names to be exported from the DLL
     * @param privateProcedures - list of procedure names to be included in the DLL but not exported
     * @param numProceduresPerModule - number of procedures per module
     */
    DllTxaTransform(
            String targetTxaFile,
            List<String> publicProcedures,
            List<String> privateProcedures = [],
            int numProceduresPerModule = 20) {

        /* ToDo: path resolution, existence and access checks */
        this.targetTxaFile = new File(targetTxaFile)
        this.publicProcedures = publicProcedures
        this.privateProcedures = privateProcedures
        this.numProceduresPerModule = numProceduresPerModule
    }

    @Override
    protected String transformInitialize(TxaContext context) {
        this.out = targetTxaFile.newWriter()

        return super.transformInitialize(context)
    }

    @Override
    protected String transformSectionStart(TxaContext context, SectionMark section) {
        if ( context.currentSection == PROCEDURE && section == COMMON ){
            if ( isPrivateProcedure(context.currentProcedureName)){
                return 'NOEXPORT' + EOL + section
            } else {
                return super.transformSectionStart(context, section)
            }
        }
        return super.transformSectionStart(context, section)
    }

    @Override
    protected String transformSectionContent(TxaContext context, SectionMark section, String content) {
        return super.transformSectionContent(context, section, content)
    }

    @Override
    protected String transformSectionEnd(TxaContext context, SectionMark section) {
        if ( isProcedureDeclaration(section) && isDllProcedure(context.currentProcedureName)){
            out << super.content
            super.clear()
        }
        return super.transformSectionEnd(context, section)
    }

    @Override
    protected String transformFinalize(TxaContext context) {
        if ( out ){
            out.flush()
            out.close()
        }

        return super.transformFinalize(context)
    }


    def isProcedureDeclaration(TxaContext context,SectionMark section) {
        section == PROCEDURE && !context.within(DEFINITION)
    }

    def isDllProcedure(String procedureName){
        privateProcedures.contains(procedureName) || publicProcedures.contains(procedureName)
    }

    def isPrivateProcedure(String procedureName){
        // public overrides private!
        !publicProcedures.contains(procedureName) && privateProcedures.contains(procedureName)
    }
}
