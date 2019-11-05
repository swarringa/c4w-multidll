package nl.practicom.c4w.multidll

import nl.practicom.c4w.txa.meta.ClarionDateMixins
import nl.practicom.c4w.txa.meta.ClarionStringMixins
import nl.practicom.c4w.txa.transform.StreamingTxaReader

import static nl.practicom.c4w.multidll.TxaTransformOptions.ApplicationType.*

class TxaApplicationTransformTest extends GroovyTestCase implements MultiDllTestSupport {

  void setUp() {
    super.setUp()
    ClarionDateMixins.initialize()
    ClarionStringMixins.initialize()
  }

  private static InputStream openResource(String filename){
    return TxaApplicationTransformTest.classLoader.getResourceAsStream(filename)
  }

  void testMainProcedureIsRemovedForDlls(){
    def sourceTXA = '' << ''''
        [APPLICATION]
        VERSION 34
        TODO ABC ToDo
        DICTIONARY 'udea10.dct'
        PROCEDURE Hoofdmenu
        [COMMON]
        FROM ABC
        MODIFIED '2019/05/28' '14:50:06'
        [PROMPTS]
    '''.trimLines(EOL)

    def targetTXA = '' << ''''
        [APPLICATION]
        VERSION 34
        TODO ABC ToDo
        DICTIONARY 'udea10.dct'
        [COMMON]
        FROM ABC
        MODIFIED '2019/05/28' '14:50:06'
        [PROMPTS]
    '''.trimLines(EOL)

    def reader = new StreamingTxaReader()
    def output = '' << ''
    def h = new TxaApplicationTransform(output,new TxaTransformOptions(targetType: MainApplication))
    reader.registerHandler(h)
    reader.parse(sourceTXA)
    assertContentEquals(output,sourceTXA)
    // no changes made

    reader = new StreamingTxaReader()
    output = '' << ''
    h = new TxaApplicationTransform(output,new TxaTransformOptions(targetType: ProcedureDLL))
    reader.registerHandler(h)
    reader.parse(sourceTXA)
    assertContentEquals(output, targetTXA)

    reader = new StreamingTxaReader()
    output = '' << ''
    h = new TxaApplicationTransform(output,new TxaTransformOptions(targetType: DataDLL))
    reader.registerHandler(h)
    reader.parse(sourceTXA)
    assertContentEquals(output, targetTXA)
  }

  void testGlobalOptionsSetToExternalGlobalsAndFiles(){
    def sourceTXA = '' << '''
    [APPLICATION]
    [COMMON]
    [PROMPTS]
    %GlobalExternal LONG  (0)
    %DefaultExternal DEFAULT  ('None External')
    %DefaultExternalAPP LONG  (0)
    '''.trimLines(EOL)

    def targetTXA = '' << '''
    [APPLICATION]
    [COMMON]
    [PROMPTS]
    %GlobalExternal LONG  (1)
    %DefaultExternal DEFAULT  ('All External')
    %DefaultExternalAPP LONG  (1)
    '''.trimLines(EOL)

    def reader = new StreamingTxaReader()
    def output = '' << ''
    def h = new TxaApplicationTransform(
      output,
      new TxaTransformOptions(targetType: MainApplication, applicationName: 'stamgegevens')
    )
    reader.registerHandler(h)
    reader.parse(sourceTXA)
    assertContentEquals(output, targetTXA)
  }

  void testStandardExternalModuleIsUpdated(){
    def sourceTXA = '' << '''
    [APPLICATION]
    [COMMON]
    [PROMPTS]
     %StandardExternalModule DEPEND %Module LONG TIMES 3
     WHEN  ('') (1)
     WHEN  ('invervo.clw') (1)
     WHEN  ('invervo001.clw') (1) 
    '''.trimLines(EOL)

    def targetTXA = '' << '''
    [APPLICATION]
    [COMMON]
    [PROMPTS]
     %StandardExternalModule DEPEND %Module LONG TIMES 3
     WHEN  ('') (1)
     WHEN  ('stamgegevens.clw') (1)
     WHEN  ('stamgegevens001.clw') (1)
    '''.trimLines(EOL)

    def reader = new StreamingTxaReader()
    def output = '' << ''
    def h = new TxaApplicationTransform(
      output,
      new TxaTransformOptions(targetType: MainApplication, applicationName: 'stamgegevens')
    )
    reader.registerHandler(h)
    reader.parse(sourceTXA)
    assertContentEquals(output, targetTXA)
  }

  void testSystemCommandSetToExeForMainApplication(){
    def sourceTXA = '' << ''''
        [PROJECT]
        #system win32 abc
        #model clarion dll
        #set RELEASE = on 
    '''.trimLines(EOL)

    def targetTXA = '' << ''''
        [PROJECT]
        #system win32 exe
        #model clarion dll
        #set RELEASE = on 
    '''.trimLines(EOL)

    def reader = new StreamingTxaReader()
    def output = '' << ''
    def h = new TxaApplicationTransform(output,new TxaTransformOptions(targetType: MainApplication))
    reader.registerHandler(h)
    reader.parse(sourceTXA)
    assertContentEquals(output, targetTXA)
  }

  void testSystemCommandSetToDllForProcedureAndDataDLL(){
    def sourceTXA = '' << ''''
        [PROJECT]
        #system win32 abc
        #model clarion dll
        #set RELEASE = on 
    '''.trimLines(EOL)

    def targetTXA = '' << ''''
        [PROJECT]
        #system win32 dll
        #model clarion dll
        #set RELEASE = on 
    '''.trimLines(EOL)

    def output = '' << ''
    def t1 = new TxaApplicationTransform(output,new TxaTransformOptions(targetType: ProcedureDLL))
    def reader = new StreamingTxaReader()
    reader.registerHandler(t1)
    reader.parse(sourceTXA)
    assertContentEquals(output, targetTXA)

    output = '' << ''
    def t2 = new TxaApplicationTransform(output,new TxaTransformOptions(targetType: DataDLL))
    reader.registerHandler(t2)
    reader.parse(sourceTXA)
    assertContentEquals(output, targetTXA)
  }

  void testProjectPragmasArePassedOn(){
    def sourceTXA = '' << '''
        [PROJECT]
        #pragma link_option(icon=>practicom.ico) -- GENERATED
        #pragma define(_ODDJOB_=>0) -- GENERATED
        #pragma define(_CRYPTONITE_=>0) -- GENERATED
        #pragma define(_DRAW_=>0) -- GENERATED
    '''.trimLines(EOL)

    def targetTXA = '' << '''
        [PROJECT]
        #pragma link_option(icon=>practicom.ico) -- GENERATED
        #pragma define(_ODDJOB_=>0) -- GENERATED
        #pragma define(_CRYPTONITE_=>0) -- GENERATED
        #pragma define(_DRAW_=>0) -- GENERATED
    '''.trimLines(EOL)

    StringBuffer output = '' << ''
    def t = new TxaApplicationTransform(output,new TxaTransformOptions())
    def reader = new StreamingTxaReader()
    reader.registerHandler(t)
    reader.parse(sourceTXA)
    assertContentEquals(output, targetTXA)
  }

  void testProjectLinkCommandsAreReplaced(){
    def sourceTxa = '' << '''
        [PROJECT]
        #compile "source_application.clw" -- GENERATED
        #compile "source_applications001.clw" -- GENERATED
        #compile "source_applications002.clw" -- GENERATED
        #compile "source_applications003.clw" -- GENERATED
        #link "source_application.EXE"
    '''.trimLines(EOL)

    def targetTxaMainApplication = '' << '''
        [PROJECT]
        #compile "target_application.clw" -- GENERATED
        #compile "target_application001.clw" -- GENERATED
        #link "target_application.EXE"
    '''.trimLines(EOL)

    def targetTxaDll = '' << '''
        [PROJECT]
        #compile "target_application.clw" -- GENERATED
        #compile "target_application001.clw" -- GENERATED
        #link "target_application.DLL"
    '''.trimLines(EOL)

    def options = new TxaTransformOptions(applicationName: 'target_application')
    options.targetType = MainApplication

    StringBuffer output = '' << ''
    def t1 = new TxaApplicationTransform(output, options)
    def reader = new StreamingTxaReader()
    reader.registerHandler(t1)
    reader.parse(sourceTxa)
    assertContentEquals(output, targetTxaMainApplication)

    output = '' << ''
    options.targetType = ProcedureDLL
    def t2 = new TxaApplicationTransform(output, options)
    reader = new StreamingTxaReader()
    reader.registerHandler(t2)
    reader.parse(sourceTxa)
    assertContentEquals(output, targetTxaDll)
  }

  void testProceduresAreRemoved(){
    def sourceTxa = '' << '''
        [APPLICATION]
        VERSION 34
        TODO ABC ToDo
        DICTIONARY 'udea10.dct'
        [COMMON]
        [PROCEDURE]
        NAME Procedure1
        [COMMON]
        [PROCEDURE]
        NAME Procedure1
        [COMMON]
        [MODULE]
        [COMMON]
    '''.trimLines(EOL)

    def targetTxa = '' << '''
        [APPLICATION]
        VERSION 34
        TODO ABC ToDo
        DICTIONARY 'udea10.dct'
        [COMMON]
        [MODULE]
        [COMMON]
    '''.trimLines(EOL)

    def output = '' << ''
    def t = new TxaApplicationTransform(output, new TxaTransformOptions())
    def reader = new StreamingTxaReader()
    reader.registerHandler(t)
    reader.parse(sourceTxa)
    assertContentEquals(output, targetTxa)
  }

  void testGlobalDataIsDeclaredExternalForMainApplicationAndProcedureDll(){
    def sourceTxa = '' << '''
        [PROGRAM]
        [COMMON]
        [DATA]
        [SCREENCONTROLS]
        ! PROMPT('Glo Flag:'),USE(?GloFlag:Prompt)
        ! ENTRY(@s1),USE(GloFlag)
        [REPORTCONTROLS]
        ! STRING(@s1),USE(GloFlag)
        GloFlag                  STRING(1)  !Global flag
        !!> GUID('9a57571a-c705-4c5e-bd13-2a7fc7f34da0'),PROMPT('Glo Flag:'),HEADER('Glo Flag'),PICTURE(@s1)
        [SCREENCONTROLS]
        ! PROMPT('GLO : Pos Schuif 1:'),USE(?GLO:PosSchuif1:Prompt)
        ! ENTRY(@n-14),USE(GLO:PosSchuif1),RIGHT(1)
        [REPORTCONTROLS]
        ! STRING(@n-14),USE(GLO:PosSchuif1),RIGHT(1)
        GLO:PosSchuif1           LONG,EXTERNAL
        !!> GUID('4321bd7d-cec4-4204-94c4-0e0ebc4d7396'),PROMPT('GLO : Pos Schuif 1:'),HEADER('GLO : Pos Schuif 1'),PICTURE(@n-14),JUSTIFY(RIGHT,1)
        [SCREENCONTROLS]
        ! CHECK('Wijzigen Verkoper'),USE(GloWijzigenVerkoper),RIGHT
        [REPORTCONTROLS]
        ! CHECK('Wijzigen Verkoper'),USE(GloWijzigenVerkoper),RIGHT
        GloWijzigenVerkoper      SHORT,EXTERNAL,DLL
        !!> GUID('aee622ca-57f2-4be8-977c-aa51a32f8ca4'),VALID(BOOLEAN),PROMPT('Wijzigen Verkoper:'),HEADER('Wijzigen Verkoper'),PICTURE(@n-7),JUSTIFY(RIGHT,1)
    '''.trimLines(EOL)

    def targetTxaExternal = '' << '''
        [PROGRAM]
        [COMMON]
        [DATA]
        [SCREENCONTROLS]
        ! PROMPT('Glo Flag:'),USE(?GloFlag:Prompt)
        ! ENTRY(@s1),USE(GloFlag)
        [REPORTCONTROLS]
        ! STRING(@s1),USE(GloFlag)
        GloFlag                  STRING(1),EXTERNAL,DLL !Global flag
        !!> GUID('9a57571a-c705-4c5e-bd13-2a7fc7f34da0'),PROMPT('Glo Flag:'),HEADER('Glo Flag'),PICTURE(@s1)
        [SCREENCONTROLS]
        ! PROMPT('GLO : Pos Schuif 1:'),USE(?GLO:PosSchuif1:Prompt)
        ! ENTRY(@n-14),USE(GLO:PosSchuif1),RIGHT(1)
        [REPORTCONTROLS]
        ! STRING(@n-14),USE(GLO:PosSchuif1),RIGHT(1)
        GLO:PosSchuif1           LONG,EXTERNAL,DLL
        !!> GUID('4321bd7d-cec4-4204-94c4-0e0ebc4d7396'),PROMPT('GLO : Pos Schuif 1:'),HEADER('GLO : Pos Schuif 1'),PICTURE(@n-14),JUSTIFY(RIGHT,1)
        [SCREENCONTROLS]
        ! CHECK('Wijzigen Verkoper'),USE(GloWijzigenVerkoper),RIGHT
        [REPORTCONTROLS]
        ! CHECK('Wijzigen Verkoper'),USE(GloWijzigenVerkoper),RIGHT
        GloWijzigenVerkoper      SHORT,EXTERNAL,DLL
        !!> GUID('aee622ca-57f2-4be8-977c-aa51a32f8ca4'),VALID(BOOLEAN),PROMPT('Wijzigen Verkoper:'),HEADER('Wijzigen Verkoper'),PICTURE(@n-7),JUSTIFY(RIGHT,1)
    '''.trimLines(EOL)

    StringBuffer output = '' << ''
    def t1 = new TxaApplicationTransform(output, new TxaTransformOptions(targetType: MainApplication))
    def reader = new StreamingTxaReader()
    reader.registerHandler(t1)
    reader.parse(sourceTxa)
    assertContentEquals(output, targetTxaExternal)

    output = '' << ''
    def t2 = new TxaApplicationTransform(output, new TxaTransformOptions(targetType: ProcedureDLL))
    def reader2 = new StreamingTxaReader()
    reader2.registerHandler(t2)
    reader2.parse(sourceTxa)
    assertContentEquals(output, targetTxaExternal)
  }

  void testGlobalDataIsDeclaredInternalForDataDll() {
    def sourceTxa = '' << '''
        [PROGRAM]
        [COMMON]
        [DATA]
        [SCREENCONTROLS]
        ! PROMPT('Glo Flag:'),USE(?GloFlag:Prompt)
        ! ENTRY(@s1),USE(GloFlag)
        [REPORTCONTROLS]
        ! STRING(@s1),USE(GloFlag)
        GloFlag                  STRING(1)  !Global flag
        !!> GUID('9a57571a-c705-4c5e-bd13-2a7fc7f34da0'),PROMPT('Glo Flag:'),HEADER('Glo Flag'),PICTURE(@s1)
        [SCREENCONTROLS]
        ! PROMPT('GLO : Pos Schuif 1:'),USE(?GLO:PosSchuif1:Prompt)
        ! ENTRY(@n-14),USE(GLO:PosSchuif1),RIGHT(1)
        [REPORTCONTROLS]
        ! STRING(@n-14),USE(GLO:PosSchuif1),RIGHT(1)
        GLO:PosSchuif1           LONG,EXTERNAL
        !!> GUID('4321bd7d-cec4-4204-94c4-0e0ebc4d7396'),PROMPT('GLO : Pos Schuif 1:'),HEADER('GLO : Pos Schuif 1'),PICTURE(@n-14),JUSTIFY(RIGHT,1)
        [SCREENCONTROLS]
        ! CHECK('Wijzigen Verkoper'),USE(GloWijzigenVerkoper),RIGHT
        [REPORTCONTROLS]
        ! CHECK('Wijzigen Verkoper'),USE(GloWijzigenVerkoper),RIGHT
        GloWijzigenVerkoper      SHORT,EXTERNAL,DLL
        !!> GUID('aee622ca-57f2-4be8-977c-aa51a32f8ca4'),VALID(BOOLEAN),PROMPT('Wijzigen Verkoper:'),HEADER('Wijzigen Verkoper'),PICTURE(@n-7),JUSTIFY(RIGHT,1)
    '''.trimLines(EOL)

    def targetTxaInternal = '' << '''
        [PROGRAM]
        [COMMON]
        [DATA]
        [SCREENCONTROLS]
        ! PROMPT('Glo Flag:'),USE(?GloFlag:Prompt)
        ! ENTRY(@s1),USE(GloFlag)
        [REPORTCONTROLS]
        ! STRING(@s1),USE(GloFlag)
        GloFlag                  STRING(1) !Global flag
        !!> GUID('9a57571a-c705-4c5e-bd13-2a7fc7f34da0'),PROMPT('Glo Flag:'),HEADER('Glo Flag'),PICTURE(@s1)
        [SCREENCONTROLS]
        ! PROMPT('GLO : Pos Schuif 1:'),USE(?GLO:PosSchuif1:Prompt)
        ! ENTRY(@n-14),USE(GLO:PosSchuif1),RIGHT(1)
        [REPORTCONTROLS]
        ! STRING(@n-14),USE(GLO:PosSchuif1),RIGHT(1)
        GLO:PosSchuif1           LONG
        !!> GUID('4321bd7d-cec4-4204-94c4-0e0ebc4d7396'),PROMPT('GLO : Pos Schuif 1:'),HEADER('GLO : Pos Schuif 1'),PICTURE(@n-14),JUSTIFY(RIGHT,1)
        [SCREENCONTROLS]
        ! CHECK('Wijzigen Verkoper'),USE(GloWijzigenVerkoper),RIGHT
        [REPORTCONTROLS]
        ! CHECK('Wijzigen Verkoper'),USE(GloWijzigenVerkoper),RIGHT
        GloWijzigenVerkoper      SHORT
        !!> GUID('aee622ca-57f2-4be8-977c-aa51a32f8ca4'),VALID(BOOLEAN),PROMPT('Wijzigen Verkoper:'),HEADER('Wijzigen Verkoper'),PICTURE(@n-7),JUSTIFY(RIGHT,1)
    '''.trimLines(EOL)

    StringBuffer output = '' << ''
    def t = new TxaApplicationTransform(output, new TxaTransformOptions(targetType: DataDLL))
    def reader = new StreamingTxaReader()
    reader.registerHandler(t)
    reader.parse(sourceTxa)
    assertContentEquals(output, targetTxaInternal)
  }
  }
}
