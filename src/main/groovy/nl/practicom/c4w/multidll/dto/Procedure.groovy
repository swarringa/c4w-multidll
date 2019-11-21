package nl.practicom.c4w.multidll.dto

class Procedure extends ProcedureInfo {

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