package nl.practicom.c4w.multidll

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Writes procedures to a single txa file
 */
class SingleTxaProcedureWriter extends OutputStreamWriter implements ProcedureWriter {

  private static EOL = System.lineSeparator()

  def numProceduresPerModule = 20
  def numProceduresWritten = 0

  SingleTxaProcedureWriter(String txaFilePath, int numProceduresPerModule = 20) {
    this(Paths.get(txaFilePath), numProceduresPerModule)
  }

  SingleTxaProcedureWriter(Path txaFile, int numProceduresPerModule = 20) {
    this(txaFile.toFile(), numProceduresPerModule)
  }

  SingleTxaProcedureWriter(File txaFile, int numProceduresPerModule = 20) {
    this(txaFile.newOutputStream(), numProceduresPerModule)
  }

  SingleTxaProcedureWriter(OutputStream os, int numProceduresPerModule = 20) {
    super(os)
    this.numProceduresPerModule = numProceduresPerModule
  }

  @Override
  void open() {}

  @Override
  void close() {
    if (numProceduresWritten > 0) {
      writeModuleSectionEnd()
    }
    super.close()
  }

  @Override
  void write(Procedure procedure) {
    if ( numProceduresPerModule > 0) {
      if (numProceduresWritten > 0 && numProceduresWritten % numProceduresPerModule == 0) {
        writeModuleSectionEnd()
      }
      if (numProceduresWritten == 0 || numProceduresWritten % numProceduresPerModule == 0) {
        writeModuleSectionStart()
      }
    }
    super.write(procedure.body.toString())
    numProceduresWritten++
  }

  def writeModuleSectionStart() {
    super.write("[MODULE]" + EOL)
    super.write("[COMMON]" + EOL)
    super.write("FROM ABC GENERATED" + EOL)
  }

  def writeModuleSectionEnd() {
    super.write("[END]" + EOL)
    super.flush()
  }
}
