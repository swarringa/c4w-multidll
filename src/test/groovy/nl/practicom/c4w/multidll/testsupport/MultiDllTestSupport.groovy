package nl.intreq.c4w.multidll.testsupport

import nl.intreq.c4w.multidll.dto.Procedure
import nl.intreq.c4w.txa.test.TxaTestSupport

trait MultiDllTestSupport implements TxaTestSupport {

  def assertStructuresAtLine(Procedure p, int lineno, List<String>... structures){
    assertStructuresAtLine(p.body.toString(), lineno, structures)
  }

  def assertStructureAtLine(Procedure p, int lineno, structure){
    assertStructureAtLine(p.body.toString(), lineno, structure)
  }

  def assertStructureAtLine(Procedure p, int lineno, String[] structure){
  }

  def assertSectionsClosedCorrectly(Procedure p){
    assertSectionsClosedCorrectly(p.body.toString())
  }
}