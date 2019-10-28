package nl.practicom.c4w.multidll

class Procedure {
  // Name of procedure extracted from NAME
  String name = null

  // Template used to generate procedure extracted from FROM
  String template = null

  // Line number where procedure definition starts in txa
  long lineNumber = 0

  // Text content of procedure including [PROCEDURE]
  StringBuilder body = new StringBuilder()

  @Override
  String toString() {
    return "${this.name} (${this.template})@${this.lineNumber}"
  }

  def save(String filePath){
    new File(filePath).write(this.body.toString())
  }
}