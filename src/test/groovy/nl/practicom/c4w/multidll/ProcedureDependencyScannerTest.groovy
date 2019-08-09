package nl.practicom.c4w.multidll

import nl.practicom.c4w.txa.meta.ClarionDateMixins
import nl.practicom.c4w.txa.meta.ClarionStringMixins
import nl.practicom.c4w.txa.transform.StreamingTxaReader

class ProcedureDependencyScannerTest extends GroovyTestCase {
    void setUp() throws java . lang . Exception {
        super.setUp()
        ClarionStringMixins.initialize()
        ClarionDateMixins.initialize()
    }

    void testCollectCalls() {
        def content = '''\
        [PROCEDURE]
        NAME P1
        [COMMON]
        [CALLS]
        P2
        P3
        [PROCEDURE]
        NAME P2
        [COMMON]
        [CALLS]
        P3
        [PROCEDURE]
        NAME P3
        [COMMON]
        '''.trimLines()


        StreamingTxaReader reader = new StreamingTxaReader()
        def scanner = new ProcedureDependencyScanner()
        reader.registerHandler(scanner)
        reader.parse('' << content)
        assert scanner.dependencies['P1'] == ['P2','P3']
        assert scanner.dependencies['P2'] == ['P3']
        assert scanner.dependencies['P3'] == []
    }

    void testCollectPrintButtonProcedure(){
        def content = '''\
            [PROCEDURE]
            NAME BrowseCbsAangifte
            [COMMON]
                [ADDITION]
                NAME ABC BrowsePrintButton
                [INSTANCE]
                INSTANCE 2
                PARENT 1
                [PROMPTS]
                %PrintProcedure PROCEDURE  (PrintCBSAangifte)
        '''.trimLines()

        StreamingTxaReader reader = new StreamingTxaReader()
        def scanner = new ProcedureDependencyScanner()
        reader.registerHandler(scanner)
        reader.parse('' << content)
        assert scanner.dependencies['BrowseCbsAangifte'] == ['PrintCBSAangifte']
    }

    void testCollectProcedurePromptProcedures(){
        def content ="""\
            [PROCEDURE]
            NAME BrowseFustSaldo
            [COMMON]
                [PROMPTS]
                %PostLookupProcedure DEPEND %Control PROCEDURE TIMES 4
                WHEN  ('?LOC:Aantal') ()
                WHEN  ('?LOC:DebNr') (BrowseDebiteuren)
                WHEN  ('?LOC:FustNr') (BrowseDebiteuren)
                WHEN  ('?LOC:TotaalSaldo') (BrowseSaldo)
        """.trimLines()

        StreamingTxaReader reader = new StreamingTxaReader()
        def scanner = new ProcedureDependencyScanner()
        reader.registerHandler(scanner)
        reader.parse('' << content)
        assert scanner.dependencies['BrowseFustSaldo'] == [
            'BrowseDebiteuren','BrowseSaldo'
        ]
    }

    void testCollectSplash(){
        def content = """
            [PROCEDURE]
            NAME Hoofdmenu
            [COMMON]
                [PROMPTS]
                %SplashProcedure PROCEDURE  (Splash)
        """.trimLines()

        StreamingTxaReader reader = new StreamingTxaReader()
        def scanner = new ProcedureDependencyScanner()
        reader.registerHandler(scanner)
        reader.parse('' << content)
        assert scanner.dependencies['Hoofdmenu'] == ['Splash']
    }
}
