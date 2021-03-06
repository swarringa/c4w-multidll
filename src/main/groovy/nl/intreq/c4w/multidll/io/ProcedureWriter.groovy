package nl.intreq.c4w.multidll.io

import nl.intreq.c4w.multidll.dto.Procedure

/**
 * We may want to handle the export of procedures from a txa in different ways.
 * The most basic is to write a single TXA file. Another option is to write a
 * each procedure to a seperate txa file. Or maybe generate seperate module files
 * holding multile procedures.
 * This interface allows for providing different implementations to handle each scenario.
 */
interface ProcedureWriter {
  void open()
  void write(Procedure procedure)
  void flush()
  void close()
}
