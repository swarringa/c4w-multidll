package nl.practicom.c4w.multidll

class ProcedureTestWriter implements ProcedureWriter {
  List<Procedure> procedures = []

  @Override
  def write(Procedure procedure) {
    procedures << procedure
  }
}
