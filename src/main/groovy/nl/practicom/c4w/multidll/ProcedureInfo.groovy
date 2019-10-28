package nl.practicom.c4w.multidll

class ProcedureInfo {
  // Name of procedure extracted from NAME
  String name = null

  // Template used to generate procedure extracted from FROM
  String template = null

  // Flag indicating if this is the main application procedure
  boolean isMainProcedure = false

  // Flag indicating if procedure is exported
  boolean isExported = true

  // Line number where procedure definition starts in txa
  long lineNumber = 0

  @Override
  String toString() {
    return "${this.name} (${this.template})@${this.lineNumber}"
  }
}
