package nl.practicom.c4w.multidll.io

import nl.practicom.c4w.multidll.dto.Procedure

import java.nio.file.Path

class SingleProcedureTxaWriter implements ProcedureWriter {
    Path destinationFolder

    SingleProcedureTxaWriter(Path destinationFolder) {
        this.destinationFolder = destinationFolder
    }

    @Override
    void open() {}

    @Override
    void write(Procedure procedure) {
        procedure.save(destinationFolder.resolve(procedure.name + ".txa"))
    }

    @Override
    void flush() {}

    @Override
    void close() {}
}
