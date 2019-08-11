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

    void testCollectTransitiveDependenciesOnSimpleTree() {
        def scanner = new ProcedureDependencyScanner()
        scanner.dependencies["P1"] = ["P2","P3"]
        scanner.dependencies["P2"] = ["P4"]
        scanner.dependencies["P3"] = []
        scanner.dependencies["P4"] = ["P5"]
        scanner.dependencies["P5"] = []

        // Collect only direct dependencies
        assert scanner.getTransitiveDependencies("P1",0) == ["P2","P3"]
        assert scanner.getTransitiveDependencies("P2",0) == ["P4"]
        assert scanner.getTransitiveDependencies("P3",0) == []
        assert scanner.getTransitiveDependencies("P4",0) == ["P5"]
        assert scanner.getTransitiveDependencies("P5",0) == []

        // Collect one level deeper
        assert scanner.getTransitiveDependencies("P1",1) == ["P2","P3","P4"]
        assert scanner.getTransitiveDependencies("P2",1) == ["P4","P5"]
        assert scanner.getTransitiveDependencies("P3",1) == []
        assert scanner.getTransitiveDependencies("P4",1) == ["P5"]
        assert scanner.getTransitiveDependencies("P5",1) == []

        // Collect all
        assert scanner.getTransitiveDependencies("P1") == ["P2","P3","P4","P5"]
        assert scanner.getTransitiveDependencies("P2") == ["P4","P5"]
        assert scanner.getTransitiveDependencies("P3") == []
        assert scanner.getTransitiveDependencies("P4") == ["P5"]
        assert scanner.getTransitiveDependencies("P5") == []
    }

    void testCollectTransitiveDependenciesWithDuplicates() {
        def scanner = new ProcedureDependencyScanner()
        scanner.dependencies["P1"] = ["P2","P3"]
        scanner.dependencies["P2"] = ["P4","P5"]
        scanner.dependencies["P3"] = ["P4"]
        scanner.dependencies["P4"] = ["P5"]
        scanner.dependencies["P5"] = []

        // Collect only direct dependencies
        assert scanner.getTransitiveDependencies("P1",0) == ["P2","P3"]
        assert scanner.getTransitiveDependencies("P2",0) == ["P4","P5"]
        assert scanner.getTransitiveDependencies("P3",0) == ["P4"]
        assert scanner.getTransitiveDependencies("P4",0) == ["P5"]
        assert scanner.getTransitiveDependencies("P5",0) == []

        // Collect one level deeper
        assert scanner.getTransitiveDependencies("P1",1) == ["P2","P3","P4","P5"]
        assert scanner.getTransitiveDependencies("P2",1) == ["P4","P5"]
        assert scanner.getTransitiveDependencies("P3",1) == ["P4","P5"]
        assert scanner.getTransitiveDependencies("P4",1) == ["P5"]
        assert scanner.getTransitiveDependencies("P5",1) == []

        // Collect all
        assert scanner.getTransitiveDependencies("P1") == ["P2","P3","P4","P5"]
        assert scanner.getTransitiveDependencies("P2") == ["P4","P5"]
        assert scanner.getTransitiveDependencies("P3") == ["P4","P5"]
        assert scanner.getTransitiveDependencies("P4") == ["P5"]
        assert scanner.getTransitiveDependencies("P5") == []
    }


    void testCollectTransitiveDependenciesWithCycles(){
        def scanner = new ProcedureDependencyScanner()
        scanner.dependencies["P1"] = ["P2","P3"]
        scanner.dependencies["P2"] = ["P4","P5"]
        scanner.dependencies["P3"] = ["P2","P5"]
        scanner.dependencies["P4"] = ["P1"]
        scanner.dependencies["P5"] = []

        assert scanner.getTransitiveDependencies("P1",0) == ["P2","P3"]
        assert scanner.getTransitiveDependencies("P2",0) == ["P4","P5"]
        assert scanner.getTransitiveDependencies("P3",0) == ["P2","P5"]
        assert scanner.getTransitiveDependencies("P4",0) == ["P1"]
        assert scanner.getTransitiveDependencies("P5",0) == []

        assert scanner.getTransitiveDependencies("P1",1).containsAll(["P2","P3","P4","P5"])
        assert scanner.getTransitiveDependencies("P2",1).containsAll(["P4","P5","P1"])
        assert scanner.getTransitiveDependencies("P3",1).containsAll(["P2","P5","P4"])
        assert scanner.getTransitiveDependencies("P4",1).containsAll(["P1","P2","P3"])
        assert scanner.getTransitiveDependencies("P5",1) == []

        assert scanner.getTransitiveDependencies("P1").containsAll(["P1", "P2","P3","P4","P5"])
        assert scanner.getTransitiveDependencies("P2").containsAll(["P1", "P2","P3","P4","P5"])
        assert scanner.getTransitiveDependencies("P3").containsAll(["P1", "P2","P3","P4","P5"])
        assert scanner.getTransitiveDependencies("P4").containsAll(["P1", "P2","P3","P4","P5"])
        assert scanner.getTransitiveDependencies("P5") == []
    }

    void testCollectTransitiveDependenciesForProcedureList(){
        def scanner = new ProcedureDependencyScanner()
        scanner.dependencies["P1"] = ["P2","P3"]
        scanner.dependencies["P2"] = ["P4","P5"]
        scanner.dependencies["P3"] = ["P2","P5"]
        scanner.dependencies["P4"] = ["P1"]
        scanner.dependencies["P5"] = []

        assert scanner.getTransitiveDependencies(["P1","P2"],0) == ["P2","P3","P4","P5"]
        assert scanner.getTransitiveDependencies(["P1","P2"]).containsAll(["P1","P2","P3","P4","P5"])
    }
}
