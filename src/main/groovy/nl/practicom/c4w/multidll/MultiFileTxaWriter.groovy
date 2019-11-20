package nl.practicom.c4w.multidll

import java.nio.file.Path
import java.nio.file.Paths

class MultiFileTxaWriter implements ProcedureWriter {

    List<Writer> writers = []

    int numSplits
    int currentSplit

    MultiFileTxaWriter(Path destinationPath, String filePrefix, int numSplits) {
        this.numSplits = numSplits

        for ( split in 0..numSplits-1) {
            def postfix = String.format("%03d", split)
            def outFilePath = destinationPath.resolve(Paths.get("${filePrefix}_${postfix}.txa"))
            writers << new FileWriter(outFilePath.toFile())
        }

        this.currentSplit = 0
    }

    @Override
    void open() {}

    @Override
    void write(Procedure procedure) {
        writers[currentSplit].write(procedure.body.toString())
        this.currentSplit = (this.currentSplit+1) % numSplits
    }

    @Override
    void flush() {
        writers.each { w -> w.flush() }
    }

    @Override
    void close() {
        writers.each { w -> w.close() }
    }
}
