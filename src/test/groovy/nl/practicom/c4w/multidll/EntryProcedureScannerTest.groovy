package nl.practicom.c4w.multidll

import nl.practicom.c4w.txa.meta.ClarionDateMixins
import nl.practicom.c4w.txa.meta.ClarionStringMixins
import nl.practicom.c4w.txa.transform.StreamingTxaReader

class EntryProcedureScannerTest extends GroovyTestCase implements  MultiDllTestSupport {
    void setUp() {
        super.setUp()
        ClarionStringMixins.initialize()
        ClarionDateMixins.initialize()
    }

    void testTopLevelItems(){
        def content = """\
            [PROCEDURE]
            NAME Hoofdmenu
            [COMMON]
            DESCRIPTION 'Hoofdmenu Historie *** Udea ***'
            FROM ABC Frame
                [PROMPTS]
                %ButtonProcedure DEPEND %Control PROCEDURE TIMES 5
                WHEN  ('?ITEM1') (SelPrtHistorieProductgroepPeriodeTotDebiteur)
                WHEN () ('')
                WHEN  ('?ITEM2') (SelPrtHistorieMargeDebiteur)
                WHEN ('') ('')
                WHEN  ('?ITEM3') ()
            [WINDOW]
            AppFrame  APPLICATION('Historie')
                      MENUBAR,USE(?MENUBAR1),#ORDINAL(1)
                        ITEM('item1'),USE(?ITEM1),#ORDINAL(1)
                        ITEM('item2'),USE(?ITEM2),#ORDINAL(2)
                        ITEM('item3'),USE(?ITEM3),#ORDINAL(3)
                        ITEM('item4'),USE(?ITEM3),#ORDINAL(4)
                      END
        """.trimLines(EOL)

        def scanner = new EntryProcedureScanner('Hoofdmenu')

        new StreamingTxaReader()
                .withHandler(scanner)
                .parse('' << content)

        assert scanner.entryProceduresFor('?ITEM1') == ['SelPrtHistorieProductgroepPeriodeTotDebiteur']
        assert scanner.entryProceduresFor('?ITEM2') == ['SelPrtHistorieMargeDebiteur']
        assert scanner.entryProceduresFor('?ITEM3') == []
        assert scanner.entryProceduresFor('?ITEM4') == []

    }

    void testFlatMenu(){
        def content = """\
            [PROCEDURE]
            NAME Hoofdmenu
            [COMMON]
            DESCRIPTION 'Hoofdmenu Historie *** Udea ***\'
            FROM ABC Frame
                [PROMPTS]
                %ButtonProcedure DEPEND %Control PROCEDURE TIMES 5
                WHEN  ('?ITEM1') (SelPrtHistorieProductgroepPeriodeTotDebiteur)
                WHEN () ('')
                WHEN  ('?ITEM2') (SelPrtHistorieMargeDebiteur)
                WHEN ('') ('')
                WHEN  ('?ITEM3') ()
                WHEN  ('?ITEM4') (SelPrtHistorieProductgroepPeriodeTotDebiteur)
                WHEN () ('')
                WHEN  ('?ITEM5') (SelPrtHistorieMargeDebiteur)
                WHEN  ('?ITEM6') (SelPrintistorieOverzichtHistorie)
                WHEN  ('?ITEM7') (SelPrintHistorieOverzichtMagazijnscanning)
                WHEN  ('?ITEM8') ()
                WHEN  ('?ITEM9') (SelPrtHistorieProductgroepPeriodeTotDebiteur)
            [WINDOW]
            AppFrame  APPLICATION('Historie')
                      MENUBAR,USE(?MENUBAR1),#ORDINAL(1)
                        MENU('Menu 1'),USE(?MENU1),#ORDINAL(1)
                            ITEM('item_1_1'),USE(?ITEM1),#ORDINAL(1)
                            ITEM('item_1_2'),USE(?ITEM2),#ORDINAL(2)
                            ITEM('item_1_3'),USE(?ITEM3),#ORDINAL(3)
                            ITEM('item_1_4'),USE(?ITEM4),#ORDINAL(4)
                        END
                        MENU('Menu 2'),USE(?MENU2),#ORDINAL(1)
                            ITEM('item_2_1'),USE(?ITEM5),#ORDINAL(1)
                            ITEM('item_2_2'),USE(?ITEM6),#ORDINAL(2)
                            ITEM('item_2_3'),USE(?ITEM7),#ORDINAL(3)
                            ITEM('item_2_4'),USE(?ITEM8),#ORDINAL(4)
                        END
                      END
        """.trimLines(EOL)

        def scanner = new EntryProcedureScanner('Hoofdmenu')

        new StreamingTxaReader()
                .withHandler(scanner)
                .parse('' << content)

        // Indivual ietms always return 0 or 1 entries
        assert scanner.entryProceduresFor('?ITEM1') == ['SelPrtHistorieProductgroepPeriodeTotDebiteur']
        assert scanner.entryProceduresFor('?ITEM2') == ['SelPrtHistorieMargeDebiteur']
        assert scanner.entryProceduresFor('?ITEM3') == []
        assert scanner.entryProceduresFor('?ITEM4') == ['SelPrtHistorieProductgroepPeriodeTotDebiteur']

        assert scanner.entryProceduresFor('?ITEM5') == ['SelPrtHistorieMargeDebiteur']
        assert scanner.entryProceduresFor('?ITEM6') == ['SelPrintistorieOverzichtHistorie']
        assert scanner.entryProceduresFor('?ITEM7') == ['SelPrintHistorieOverzichtMagazijnscanning']
        assert scanner.entryProceduresFor('?ITEM8') == []

        // ?ITEM9 not part of the menu structure!
        assert scanner.entryProceduresFor('?ITEM9') == []

        // MENU subtrees will return all entries of containing items

        // duplicate entry removed
        assert scanner.entryProceduresFor('?MENU1').containsAll([
                'SelPrtHistorieProductgroepPeriodeTotDebiteur',
                'SelPrtHistorieMargeDebiteur'
        ])

        assert scanner.entryProceduresFor('?MENU2').containsAll([
                'SelPrtHistorieMargeDebiteur',
                'SelPrintistorieOverzichtHistorie',
                'SelPrintHistorieOverzichtMagazijnscanning'
        ])
    }

    void testSubmenus(){
        def content = """\
            [PROCEDURE]
            NAME Hoofdmenu
            [COMMON]
            DESCRIPTION 'Hoofdmenu Historie *** Udea ***\'
            FROM ABC Frame
                [PROMPTS]
                %ButtonProcedure DEPEND %Control PROCEDURE TIMES 5
                WHEN  ('?ITEM1') (SelPrtHistorieProductgroepPeriodeTotDebiteur)
                WHEN () ('')
                WHEN  ('?ITEM2') (SelPrtHistorieMargeDebiteur)
                WHEN ('') ('')
                WHEN  ('?ITEM3') ()
                WHEN  ('?ITEM4') (SelPrtHistorieProductgroepPeriodeTotDebiteur)
                WHEN () ('')
                WHEN  ('?ITEM5') (SelPrtHistorieMargeDebiteur)
                WHEN  ('?ITEM6') (SelPrintistorieOverzichtHistorie)
                WHEN  ('?ITEM7') (SelPrintHistorieOverzichtMagazijnscanning)
                WHEN  ('?ITEM8') ()
                WHEN  ('?ITEM9') (SelPrtHistorieProductgroepPeriodeTotDebiteur)
            [WINDOW]
            AppFrame  APPLICATION('Historie')
                      MENUBAR,USE(?MENUBAR1),#ORDINAL(1)
                        MENU('Menu 1'),USE(?MENU1),#ORDINAL(1)
                            ITEM('item_1_1'),USE(?ITEM1),#ORDINAL(1)
                            ITEM('item_1_2'),USE(?ITEM2),#ORDINAL(2)
                            ITEM('item_1_3'),USE(?ITEM3),#ORDINAL(3)
                            ITEM('item_1_4'),USE(?ITEM4),#ORDINAL(4)
                            MENU('Menu 2'),USE(?MENU2),#ORDINAL(1)
                                ITEM('item_2_1'),USE(?ITEM5),#ORDINAL(1)
                                ITEM('item_2_2'),USE(?ITEM6),#ORDINAL(2)
                                ITEM('item_2_3'),USE(?ITEM7),#ORDINAL(3)
                                ITEM('item_2_4'),USE(?ITEM8),#ORDINAL(4)
                            END
                        END
                      END
        """.trimLines(EOL)

        def scanner = new EntryProcedureScanner('Hoofdmenu')

        new StreamingTxaReader()
                .withHandler(scanner)
                .parse('' << content)

        // Indivual items always return 0 or 1 entries
        assert scanner.entryProceduresFor('?ITEM1') == ['SelPrtHistorieProductgroepPeriodeTotDebiteur']
        assert scanner.entryProceduresFor('?ITEM2') == ['SelPrtHistorieMargeDebiteur']
        assert scanner.entryProceduresFor('?ITEM3') == []
        assert scanner.entryProceduresFor('?ITEM4') == ['SelPrtHistorieProductgroepPeriodeTotDebiteur']

        assert scanner.entryProceduresFor('?ITEM5') == ['SelPrtHistorieMargeDebiteur']
        assert scanner.entryProceduresFor('?ITEM6') == ['SelPrintistorieOverzichtHistorie']
        assert scanner.entryProceduresFor('?ITEM7') == ['SelPrintHistorieOverzichtMagazijnscanning']
        assert scanner.entryProceduresFor('?ITEM8') == []

        // ?ITEM9 not part of the menu structure!
        assert scanner.entryProceduresFor('?ITEM9') == []

        // MENU subtrees will return all entries of containing items

        // duplicate entry removed
        assert scanner.entryProceduresFor('?MENU1', 0).containsAll([
                'SelPrtHistorieProductgroepPeriodeTotDebiteur',
                'SelPrtHistorieMargeDebiteur'
        ])

        assert scanner.entryProceduresFor('?MENU2').containsAll([
                'SelPrtHistorieMargeDebiteur',
                'SelPrintistorieOverzichtHistorie',
                'SelPrintHistorieOverzichtMagazijnscanning'
        ])

        assert scanner.entryProceduresFor('?MENU1').containsAll([
                'SelPrtHistorieProductgroepPeriodeTotDebiteur',
                'SelPrtHistorieMargeDebiteur',
                'SelPrtHistorieProductgroepPeriodeTotDebiteur',
                'SelPrintistorieOverzichtHistorie',
                'SelPrintHistorieOverzichtMagazijnscanning'
        ])
    }

    void testMainProcedureIsScannedByIfNoSpecificProcedureProvided(){
        def content = """\
            [APPLICATION]
            PROCEDURE P1
            [PROCEDURE]
            NAME P1
            [COMMON]
            DESCRIPTION 'Procedure 1'
            FROM ABC Frame
                [PROMPTS]
                %ButtonProcedure DEPEND %Control PROCEDURE TIMES 1
                WHEN  ('?ITEM1') (SelPrtHistorieProductgroepPeriodeTotDebiteur)
            [WINDOW]
            window1  WINDOW('Window 1')
                      MENUBAR,USE(?MENUBAR1),#ORDINAL(1)
                        ITEM('item1'),USE(?ITEM1),#ORDINAL(1)
                      END
            [PROCEDURE]
            NAME P2
            [COMMON]
            DESCRIPTION 'Procedure 2'
            FROM ABC Frame
                [PROMPTS]
                %ButtonProcedure DEPEND %Control PROCEDURE TIMES 1
            [WINDOW]
            AppFrame  WINDOW('Window 2')
                      MENUBAR,USE(?MENUBAR1),#ORDINAL(1)
                        ITEM('item1'),USE(?ITEM1),#ORDINAL(1)
                      END
        """.trimLines(EOL)

        def scanner = new EntryProcedureScanner()

        new StreamingTxaReader()
          .withHandler(scanner)
          .parse('' << content)

        assert scanner.entryProceduresFor('?ITEM1') == ['SelPrtHistorieProductgroepPeriodeTotDebiteur']
    }

    /**
     * Menu entries can have dependencies on procedures via source code
     * embeds on their accept event. The embedded source code may call
     * a procedure directly or may do so via routine(s).
     * All routines called via source code should be specified in the
     * procedures' [CALL] section.
    @Test @Ignore("Not implemented yet")
    void testIndirectSourceDependencies(){
        def content ="""
            [PROCEDURE]
            NAME Hoofdmenu
            [COMMON]
                DESCRIPTION 'Hoofdmenu Historie *** Udea ***'
                FROM ABC Frame
                [EMBED]
                    EMBED %ControlEventHandling
                    [INSTANCES]
                      WHEN '?Aanmelden'
                      [INSTANCES]
                      WHEN 'Accepted'
                            [DEFINITION]
                                [SOURCE]
                                DO Aanmelding
                            [END]
                      [END]
                      WHEN '?OpdrachtbevestigingenAanmaken'
                        [INSTANCES]
                          WHEN 'Accepted'
                            [DEFINITION]
                                [SOURCE]
                                    PROPERTY:BEGIN
                                    PRIORITY 5000
                                    PROPERTY:END
                                    !Aanroep printen opdrachtbevestiging
                                    
                                    GloOrdernummer = 0
                                    
                                    START(SelPrintOpdrachtbevestiging, 25000)
                            [END]
                        [END]
                    [END]
                    EMBED %ProcedureRoutines
                    [DEFINITION]
                        [SOURCE]
                            PROPERTY:BEGIN
                            PRIORITY 4000
                            PROPERTY:END
                            Aanmelding ROUTINE
                             !Global aanmelden standaard op nee
                             GloAanmelden = 'Nee'
                            
                             !Aanmelding procedure
                             Aanmelden()
                    [END]
                [END]
            [CALLS]
            Aanmelden
            SelPrintOpdrachtbevestiging
            [WINDOW]
            AppFrame APPLICATION('INkoop VERkoop VOorraad')
                      MENUBAR,USE(?MENUBAR1),#ORDINAL(1)
                        ITEM('Aanmelden'),USE(?Aanmelden),#ORDINAL(1)
                        MENU('Groothandel bestellingen'),USE(?VerkopenModembestellingen),MSG('Inlezen, p' & |
                            'rinten en verwerking van modembestellingen'),#ORDINAL(22)
                            ITEM('Opdrachtbevestigingen aanmaken'),USE(?OpdrachtbevestigingenAanmaken), |
                              #ORDINAL(2)
                        END
                      END
        """.trimLines(EOL)

        def scanner = new EntryProcedureScanner('Hoofdmenu')

        new StreamingTxaReader()
                .withHandler(scanner)
                .parse('' << content)

        assert scanner.entryProceduresFor('?Aanmelden') == ['Aanmelden']
        assert scanner.entryProceduresFor('?VerkopenModembestellingen') == ['SelPrintOpdrachtbevestiging']
    }
     */

}
