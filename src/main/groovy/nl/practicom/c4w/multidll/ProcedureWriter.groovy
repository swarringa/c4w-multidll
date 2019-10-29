package nl.practicom.c4w.multidll

/**
 * We may want to handle the export of procedures from a txa in different ways.
 * The most basic is to write a single TXA file. Another option is to write a
 * each procedure to a seperate txa file. Or maybe generate seperate module files
 * holding multile procedures.
 * This interface allows for providing different implementations to handle each scenario.
 */
interface ProcedureWriter {
  def write(Procedure procedure)
}
