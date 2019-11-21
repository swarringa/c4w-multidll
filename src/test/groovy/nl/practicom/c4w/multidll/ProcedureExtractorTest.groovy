package nl.practicom.c4w.multidll

import nl.practicom.c4w.multidll.testsupport.MultiDllTestSupport
import nl.practicom.c4w.multidll.testsupport.ProcedureTestWriter
import nl.practicom.c4w.multidll.testsupport.ProcedureTransformTestFactory
import nl.practicom.c4w.multidll.transforms.procedure.ProcedureExtractor
import nl.practicom.c4w.txa.meta.ClarionDateMixins
import nl.practicom.c4w.txa.meta.ClarionStringMixins
import nl.practicom.c4w.txa.transform.StreamingTxaReader

class ProcedureExtractorTest extends GroovyTestCase implements MultiDllTestSupport {

    void setUp() {
        super.setUp()
        ClarionDateMixins.initialize()
        ClarionStringMixins.initialize()
    }

    void testExtraction(){
        def contents = txaContent("""\
            [APPLICATION]
               [MODULE]
                  [PROCEDURE]
                     NAME P1
                     FROM ABC Browse
                  [PROCEDURE]
                     NAME P2
                     FROM ABC Window
               [END]
               [MODULE]
                 [PROCEDURE]
                    NAME P3
                    FROM ABC Report
               [END]
        """)

        assertSectionsClosedCorrectly(contents)

        def reader = new StreamingTxaReader()
        def wr = new ProcedureTestWriter()
        def xt = new ProcedureExtractor(new ProcedureTransformTestFactory(), wr)
        reader.registerHandler(xt)
        reader.parse('' << contents)

        wr.with {
            assert procedures.size() == 3
            assertSectionsClosedCorrectly(procedures[0])
            assertSectionsClosedCorrectly(procedures[1])
            assertSectionsClosedCorrectly(procedures[2])

            assert procedures[0].name == 'P1'
            assert procedures[0].template == 'ABC Browse'
            assertStructureAtLine(procedures[0], 0, [
              "[PROCEDURE]",
              "NAME P1",
              "FROM ABC Browse"
            ])
            assert procedures[1].name == 'P2'
            assertStructureAtLine(procedures[1], 0, [
              "[PROCEDURE]",
              "NAME P2",
              "FROM ABC Window"
            ])
            assert procedures[1].template == 'ABC Window'
            assert procedures[2].name == 'P3'
            assert procedures[2].template == 'ABC Report'
            assertStructureAtLine(procedures[2], 0, [
              "[PROCEDURE]",
              "NAME P3",
              "FROM ABC Report"
            ])
        }
    }

    void testEmbeddedProcedureIsIgnored(){
        def contents = txaContent("""\
           [MODULE]
             [PROCEDURE]
             NAME P1
             FROM ABC Browse
             [COMMON]
                 [EMBED]
                   EMBED %Embedpoint1
                   [DEFINITION]
                     [PROCEDURE]
                       P2()
                   [END] 
                 [END]
             [PROCEDURE]
             NAME P2
             FROM ABC Window
             [COMMON]
                 [EMBED]
                   EMBED %EmbedPoint2
                   [INSTANCES]
                     WHEN 'Init'
                     [DEFINITION]
                        [PROCEDURE]
                           P1()
                     [END]
                   [END]
                 [END]
           [END]          
        """)

        assertSectionsClosedCorrectly(contents)

        def reader = new StreamingTxaReader()
        def wr = new ProcedureTestWriter()
        def xt = new ProcedureExtractor(new ProcedureTransformTestFactory(), wr)
        reader.registerHandler(xt)
        reader.parse('' << contents)

        wr.with {
            assert procedures.size() == 2
            assertSectionsClosedCorrectly(procedures[0])
            assertSectionsClosedCorrectly(procedures[1])
        }
    }

    void testFlatProcedureListIsExtractedCorrectly(){
        def contents = txaContent("""\
         [PROCEDURE]
         NAME P1
         FROM ABC Browse
         [PROCEDURE]
         NAME P2
         FROM ABC Window
         [PROCEDURE]
         NAME P3
         FROM ABC Report          
        """)

        assertSectionsClosedCorrectly(contents)

        def reader = new StreamingTxaReader()
        def wr = new ProcedureTestWriter()
        def xt = new ProcedureExtractor(new ProcedureTransformTestFactory(), wr)
        reader.registerHandler(xt)
        reader.parse('' << contents)

        wr.with {
            assert procedures.size() == 3
            assert procedures[0].name == 'P1'
            assert procedures[0].template == 'ABC Browse'
            assertStructureAtLine(procedures[0], 0, [
              "[PROCEDURE]",
              "NAME P1",
              "FROM ABC Browse"
            ])
            assert procedures[1].name == 'P2'
            assertStructureAtLine(procedures[1], 0, [
              "[PROCEDURE]",
              "NAME P2",
              "FROM ABC Window"
            ])
            assert procedures[1].template == 'ABC Window'
            assert procedures[2].name == 'P3'
            assert procedures[2].template == 'ABC Report'
            assertStructureAtLine(procedures[2], 0, [
              "[PROCEDURE]",
              "NAME P3",
              "FROM ABC Report"
            ])
        }
    }

    void testCorrectRollupInnerSection(){
        def contents = txaContent("""
        [PROCEDURE]
        NAME PrintHistorieProductPeriodeTotProduct
        [COMMON]
        FROM ABC Report
            [DATA]
                [SCREENCONTROLS]
                [REPORTCONTROLS]
        [WINDOW]
        Window  MyWindow
        """)

        assertSectionsClosedCorrectly(contents)

        def reader = new StreamingTxaReader()
        def wr = new ProcedureTestWriter()
        def xt = new ProcedureExtractor(new ProcedureTransformTestFactory(), wr)
        reader.registerHandler(xt)
        reader.parse('' << contents)

        wr.with {
            assert procedures.size() == 1
            assert procedures[0].body.contains('MyWindow')
        }
    }

    void testFullProcedureExtractedCorrectly(){
        def contents = txaContent("""\
        [PROCEDURE]
        NAME PrintHistorieProductPeriodeTotProduct
        [COMMON]
        DESCRIPTION 'Printen Historie v-t product / per periode totaal per product'
        FROM ABC Report
        MODIFIED '2018/02/06' ' 9:04:54'
        [DATA]
        [SCREENCONTROLS]
        ! PROMPT('Bedrag:'),USE(?Bedrag:Prompt)
        ! ENTRY(@n-10`2),USE(Bedrag),DECIMAL(12)
        [REPORTCONTROLS]
        ! STRING(@n-10`2),USE(Bedrag),DECIMAL(12)
        [FILES]
        [PRIMARY]
        HistVerkopen
        [INSTANCE]
        0
        [KEY]
        HVER:KeyGroepNummerLeveren
        [SECONDARY]
        Debiteuren HistVerkopen
        [PROMPTS]
        %RangeLimitType DEFAULT  ('Range of Values')
        [EMBED]
        EMBED %ProcessManagerMethodCodeSection
        [INSTANCES]
        WHEN 'TakeRecord'
        [INSTANCES]
        WHEN '(),BYTE'
        [DEFINITION]
        [SOURCE]
        PROPERTY:BEGIN
        PRIORITY 5500
        PROPERTY:END
        !source code
        [END]
        [END]
        [END]
        [END]
        [ADDITION]
        NAME WinEvent WinEvent
        [INSTANCE]
        INSTANCE 1
        OWNER 6
        [PROMPTS]
        %DisableWinEvent LONG  (0)
        [ADDITION]
        NAME ABC ReportPageNumber
        [INSTANCE]
        INSTANCE 2
        [WINDOW]
        ProgressWindow WINDOW('Voortgang...'),AT(,,142,59),DOUBLE,CENTER,GRAY,TIMER(1)
                  PROGRESS,AT(15,15,111,12),USE(Progress:Thermometer),RANGE(0,100),#ORIG(Progress:Thermometer)
                  STRING(''),AT(0,3,141,10),USE(?Progress:UserString),CENTER,#ORIG(?Progress:UserString)
                  STRING(''),AT(0,30,141,10),USE(?Progress:PctText),CENTER,#ORIG(?Progress:PctText)
                  BUTTON('Annuleren'),AT(45,42,50,15),USE(?Progress:Cancel),#ORIG(?Progress:Cancel)
                END
        
        [REPORT] 
        Report  REPORT('InVerVo'),AT(10,46,185,226),PRE(RPT),PAPER(PAPER:A4),FONT('Myriad Pro Light'       
        [FORMULA]
        DEFINE Datum
        ASSIGN ReportDate
        CLASS Procedure Setup
        INSTANCE 0
        DESCRIPTION 
        = TODAY()
        DEFINE 
        ASSIGN TotaalAantal
        CLASS Before Print Detail
        INSTANCE 0
        DESCRIPTION 
        = HVER:SVE * HVER:Aantal
        """)

        assertSectionsClosedCorrectly(contents)

        def reader = new StreamingTxaReader()
        def wr = new ProcedureTestWriter()
        def xt = new ProcedureExtractor(new ProcedureTransformTestFactory(), wr)
        reader.registerHandler(xt)
        reader.parse('' << contents)

        wr.with {
            assert procedures.size() == 1
            def body = procedures[0].body.toString()
            assert body.lineCount(EOL) == contents.lineCount(EOL)
            assertSectionsClosedCorrectly(body)
            assertStructureAtLine(procedures[0], 0, contents.toLineArray(EOL))
        }
    }
}


