package nl.practicom.c4w.multidll

class ProcedureTestWriter implements ProcedureWriter {
  List<Procedure> procedures = []

  @Override
  void open() {}

  @Override
  void write(Procedure procedure) {
    procedures << procedure
  }

  @Override
  void flush() {}

  @Override
  void close() {}
}
