package nl.practicom.c4w.multidll

/**
 * Writes procedures to a single txa file
 */
class SingleTxaProcedureWriter extends FileWriter implements ProcedureWriter {

  private static EOL = System.lineSeparator()

  def numProceduresPerModule = 20
  def numProceduresWritten = 0

  SingleTxaProcedureWriter(String txaFile, boolean append = false, int numProceduresPerModule = 20) {
    super(txaFile,append)
    this.numProceduresPerModule = numProceduresPerModule
  }

  @Override
  def write(Procedure procedure) {
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
