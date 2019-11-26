package nl.practicom.c4w.multidll.io

import nl.practicom.c4w.multidll.dto.Procedure
import nl.practicom.c4w.multidll.transforms.procedure.ChainableTransform
import nl.practicom.c4w.txa.transform.SectionMark
import nl.practicom.c4w.txa.transform.TxaContext

import java.nio.file.Path
import java.nio.file.Paths

class MultiFileTxaProcedureWriter extends ChainableTransform implements ProcedureWriter {

    List<ProcedureWriter> writers = []

    int numSplits
    int currentSplit
    String filePrefix

    Boolean categoryWritten = false

    MultiFileTxaProcedureWriter(Path destinationPath, String filePrefix, int numSplits, int numProceduresPerModule) {
        this.numSplits = numSplits
        this.filePrefix = filePrefix

        for ( split in 0..numSplits-1) {
            def postfix = String.format("%03d", split)
            def outFilePath = destinationPath.resolve(Paths.get("${filePrefix}_${postfix}.txa"))
            writers << new SingleTxaProcedureWriter(outFilePath.toFile(),numProceduresPerModule)
        }

        this.currentSplit = 0
    }

    @Override
    void open() {}

    @Override
    void write(Procedure procedure) {
        writers[currentSplit].write(procedure)
        this.currentSplit = (this.currentSplit+1) % numSplits
        categoryWritten = false
    }

    @Override
    void flush() {
        writers.each { w -> w.flush() }
    }

    @Override
    void close() {
        writers.each { w -> w.close() }
    }

    /*
    * This is a bit of a hack to set the category of the transformed
    * to the name of the output file. This allows us to check the
    * procedures imported from a specific file using Clarions' category
    * tree view.
    * Since the procedure extractor is agnostic to the type of writer
    * used we cannot put this logic there. Also a separate transform
    * has no access to the output file data. Therefore, the only logical
    * solution is to make the information export (ie. this class) a
    * procedure transformer itself.
    * The assumption here is that the procedure that is being transformed
    * will be the next one written!
    */

    @Override
    String transformSectionContent(TxaContext context, SectionMark section, String content) {
        if ( content.trim().toUpperCase().startsWith('CATEGORY') ){
            categoryWritten = true
            return "CATEGORY '${getCategory()}'"
        }
    }

    @Override
    String transformSectionStart(TxaContext context, SectionMark section) {
        if (section == SectionMark.DATA){
            if (!categoryWritten){
                categoryWritten = true
                return "CATEGORY '${getCategory()}'" << System.lineSeparator() << section
            }
        }
    }

    @Override
    String transformSectionEnd(TxaContext context, SectionMark section) {
        if (section == SectionMark.DATA){
            categoryWritten = false
        }
    }

    def getCategory(){
        def postfix = String.format("%03d", currentSplit)
        return "${filePrefix}_${postfix}"
    }
}
