package nl.practicom.c4w.multidll

import nl.practicom.c4w.txa.meta.ClarionDateMixins
import nl.practicom.c4w.txa.meta.ClarionStringMixins
import nl.practicom.c4w.txa.transform.StreamingTxaReader

class ConvertToPrivateProcedureTest extends GroovyTestCase implements MultiDllTestSupport {

  void setUp() {
    super.setUp()
    ClarionDateMixins.initialize()
    ClarionStringMixins.initialize()
  }

  void testNoExportFlagInsertedBeforeExistingCommonSection() {
    def contents = """\
            [APPLICATION]
               [MODULE]
                  [PROCEDURE]
                     NAME P1
                     [COMMON]
                       FROM ABC Browse
                [END]
        """.trimLines()

    assertSectionsClosedCorrectly(contents)

    def reader = new StreamingTxaReader()
    def wr = new ProcedureTestWriter()
    def ptf = new ProcedureTransformTestFactory().withDefaultTransform(new ConvertToPrivateProcedure())
    def xt = new ProcedureExtractor(ptf, wr)
    reader.registerHandler(xt)
    reader.parse('' << contents)

    wr.with {
      assert procedures.size() == 1
      assertSectionsClosedCorrectly(procedures[0])
      assertStructureAtLine(procedures[0], 0, [
        "[PROCEDURE]",
        "NAME P1",
        "NOEXPORT",
        "[COMMON]",
        "FROM ABC Browse"
      ])
    }
  }

  void testNoExportFlagInsertedAtEndWhenNoCommonSection() {
    def contents = """\
            [APPLICATION]
               [MODULE]
                  [PROCEDURE]
                     NAME P1
                [END]
        """.trimLines()

    assertSectionsClosedCorrectly(contents)

    def reader = new StreamingTxaReader()
    def wr = new ProcedureTestWriter()
    def ptf = new ProcedureTransformTestFactory().withDefaultTransform(new ConvertToPrivateProcedure())
    def xt = new ProcedureExtractor(ptf, wr)
    reader.registerHandler(xt)
    reader.parse('' << contents)

    wr.with {
      assert procedures.size() == 1
      assertSectionsClosedCorrectly(procedures[0])
      assertStructureAtLine(procedures[0], 0, [
        "[PROCEDURE]",
        "NAME P1",
        "NOEXPORT"
      ])
    }
  }
}
