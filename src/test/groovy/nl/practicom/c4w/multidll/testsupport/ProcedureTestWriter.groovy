package nl.intreq.c4w.multidll.testsupport

import nl.intreq.c4w.multidll.dto.Procedure
import nl.intreq.c4w.multidll.io.ProcedureWriter

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
