package nl.intreq.c4w.multidll.dto

import java.nio.file.Path

class Procedure extends ProcedureInfo {

  // Text content of procedure including [PROCEDURE]
  StringBuilder body = new StringBuilder()

  @Override
  String toString() {
    return "${this.name} (${this.template})@${this.lineNumber}"
  }

  def save(Path filePath){
    filePath.toFile().write(this.body.toString(),"ISO-8859-1")
  }
}