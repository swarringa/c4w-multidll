package nl.intreq.c4w.multidll

import nl.intreq.c4w.multidll.testsupport.MultiDllTestSupport
import nl.intreq.c4w.multidll.transforms.application.TxaApplicationTransform
import nl.intreq.c4w.multidll.transforms.application.TxaApplicationTransformOptions
import nl.intreq.c4w.txa.meta.ClarionDateMixins
import nl.intreq.c4w.txa.meta.ClarionStringMixins
import nl.intreq.c4w.txa.transform.StreamingTxaReader

import static nl.intreq.c4w.multidll.transforms.application.ApplicationType.*
import static nl.intreq.c4w.txa.transform.SectionMark.*

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
    def sourceTXA = '' << txaContent(''''
        [APPLICATION]
        VERSION 34
        TODO ABC ToDo
        DICTIONARY 'udea10.dct'
        PROCEDURE Hoofdmenu
        [COMMON]
        FROM ABC
        MODIFIED '2019/05/28' '14:50:06'
        [PROMPTS]
    ''')

    def targetTXA = '' << txaContent(''''
        [APPLICATION]
        VERSION 34
        TODO ABC ToDo
        DICTIONARY 'udea10.dct'
        [COMMON]
        FROM ABC
        MODIFIED '2019/05/28' '14:50:06'
        [PROMPTS]
    ''')

    def reader = new StreamingTxaReader()
    def output = '' << ''
    def h = new TxaApplicationTransform(output,new TxaApplicationTransformOptions(targetType: MainApplication))
    reader.registerHandler(h)
    reader.parse(sourceTXA)
    assertContentEquals(output,sourceTXA)
    // no changes made

    reader = new StreamingTxaReader()
    output = '' << ''
    h = new TxaApplicationTransform(output,new TxaApplicationTransformOptions(targetType: ProcedureDLL))
    reader.registerHandler(h)
    reader.parse(sourceTXA)
    assertContentEquals(output, targetTXA)

    reader = new StreamingTxaReader()
    output = '' << ''
    h = new TxaApplicationTransform(output,new TxaApplicationTransformOptions(targetType: DataDLL))
    reader.registerHandler(h)
    reader.parse(sourceTXA)
    assertContentEquals(output, targetTXA)
  }

  void testGlobalOptionsSetToExternalGlobalsAndFiles(){
    def sourceTXA = '' << txaContent('''
    [APPLICATION]
    [COMMON]
    [PROMPTS]
    %GlobalExternal LONG  (0)
    %DefaultExternal DEFAULT  ('None External')
    %DefaultExternalAPP LONG  (0)
    ''')

    def targetTXA = '' << txaContent('''
    [APPLICATION]
    [COMMON]
    [PROMPTS]
    %GlobalExternal LONG  (1)
    %DefaultExternal DEFAULT  ('All External')
    %DefaultExternalAPP LONG  (1)
    ''')

    def reader = new StreamingTxaReader()
    def output = '' << ''
    def h = new TxaApplicationTransform(
      output,
      new TxaApplicationTransformOptions(targetType: MainApplication, applicationName: 'stamgegevens')
    )
    reader.registerHandler(h)
    reader.parse(sourceTXA)
    assertContentEquals(output, targetTXA)
  }

  void testStandardExternalModuleIsUpdated(){
    def sourceTXA = '' << txaContent('''
    [APPLICATION]
    [COMMON]
    [PROMPTS]
     %StandardExternalModule DEPEND %Module LONG TIMES 3
     WHEN  ('') (1)
     WHEN  ('invervo.clw') (1)
     WHEN  ('invervo001.clw') (1) 
    ''')

    def targetTXA = '' << txaContent('''
    [APPLICATION]
    [COMMON]
    [PROMPTS]
     %StandardExternalModule DEPEND %Module LONG TIMES 3
     WHEN  ('') (1)
     WHEN  ('stamgegevens.clw') (1)
     WHEN  ('stamgegevens001.clw') (1)
    ''')

    def reader = new StreamingTxaReader()
    def output = '' << ''
    def h = new TxaApplicationTransform(
      output,
      new TxaApplicationTransformOptions(targetType: MainApplication, applicationName: 'stamgegevens')
    )
    reader.registerHandler(h)
    reader.parse(sourceTXA)
    assertContentEquals(output, targetTXA)
  }

  void testSystemCommandSetToExeForMainApplication(){
    def sourceTXA = '' << txaContent('''
        [PROJECT]
        #system win32 abc
        #model clarion dll
        #set RELEASE = on 
    ''')

    def targetTXA = '' << txaContent('''
        [PROJECT]
        #system win32 exe
        #model clarion dll
        #set RELEASE = on 
    ''')

    def reader = new StreamingTxaReader()
    def output = '' << ''
    def h = new TxaApplicationTransform(output,new TxaApplicationTransformOptions(targetType: MainApplication))
    reader.registerHandler(h)
    reader.parse(sourceTXA)
    assertContentEquals(output, targetTXA)
  }

  void testSystemCommandSetToDllForProcedureAndDataDLL(){
    def sourceTXA = '' << txaContent('''
        [PROJECT]
        #system win32 abc
        #model clarion dll
        #set RELEASE = on 
    ''')

    def targetTXA = '' << txaContent('''
        [PROJECT]
        #system win32 dll
        #model clarion dll
        #set RELEASE = on 
    ''')

    def output = '' << ''
    def t1 = new TxaApplicationTransform(output,new TxaApplicationTransformOptions(targetType: ProcedureDLL))
    def reader = new StreamingTxaReader()
    reader.registerHandler(t1)
    reader.parse(sourceTXA)
    assertContentEquals(output, targetTXA)

    output = '' << ''
    def t2 = new TxaApplicationTransform(output,new TxaApplicationTransformOptions(targetType: DataDLL))
    reader.registerHandler(t2)
    reader.parse(sourceTXA)
    assertContentEquals(output, targetTXA)
  }


  void testLinkModeSetCorrectly(){
    def dllModeTXA = '' << txaContent('''
        [PROJECT]
        #pragma define(_CCLSDllMode_=>1) -- GENERATED
        #pragma define(_CCLSLinkMode_=>0) -- GENERATED
        #pragma define(HyperActiveLinkMode=>0) -- GENERATED
        #pragma define(HyperActiveDllMode=>1) -- GENERATED
    ''')

    def linkModeTXA = '' << txaContent('''
        [PROJECT]
        #pragma define(_CCLSDllMode_=>0) -- GENERATED
        #pragma define(_CCLSLinkMode_=>1) -- GENERATED
        #pragma define(HyperActiveLinkMode=>1) -- GENERATED
        #pragma define(HyperActiveDllMode=>0) -- GENERATED
    ''')

    def output = '' << ''
    def t1 = new TxaApplicationTransform(output,new TxaApplicationTransformOptions(targetType: ProcedureDLL))
    def reader = new StreamingTxaReader()
    reader.registerHandler(t1)
    reader.parse(linkModeTXA)
    assertContentEquals(output, dllModeTXA)

    output = '' << ''
    t1 = new TxaApplicationTransform(output,new TxaApplicationTransformOptions(targetType: MainApplication))
    reader.registerHandler(t1)
    reader.parse(linkModeTXA)
    assertContentEquals(output, dllModeTXA)
  }

  void testProjectPragmasArePassedOn(){
    def sourceTXA = '' << txaContent('''
        [PROJECT]
        #pragma link_option(icon=>intreq.ico) -- GENERATED
        #pragma define(_ODDJOB_=>0) -- GENERATED
        #pragma define(_CRYPTONITE_=>0) -- GENERATED
        #pragma define(_DRAW_=>0) -- GENERATED
    ''')

    def targetTXA = '' << txaContent('''
        [PROJECT]
        #pragma link_option(icon=>intreq.ico) -- GENERATED
        #pragma define(_ODDJOB_=>0) -- GENERATED
        #pragma define(_CRYPTONITE_=>0) -- GENERATED
        #pragma define(_DRAW_=>0) -- GENERATED
    ''')

    StringBuffer output = '' << ''
    def t = new TxaApplicationTransform(output,new TxaApplicationTransformOptions())
    def reader = new StreamingTxaReader()
    reader.registerHandler(t)
    reader.parse(sourceTXA)
    assertContentEquals(output, targetTXA)
  }

  void testProjectLinkCommandsAreReplaced(){
    def sourceTxa = '' << txaContent('''
        [PROJECT]
        #compile "source_application.clw" -- GENERATED
        #compile "source_applications001.clw" -- GENERATED
        #compile "source_applications002.clw" -- GENERATED
        #compile "source_applications003.clw" -- GENERATED
        #link "source_application.EXE"
    ''')

    def targetTxaMainApplication = '' << txaContent('''
        [PROJECT]
        #compile "target_application.clw" -- GENERATED
        #compile "target_application001.clw" -- GENERATED
        #link "target_application.EXE"
    ''')

    def targetTxaDll = '' << txaContent('''
        [PROJECT]
        #compile "target_application.clw" -- GENERATED
        #compile "target_application001.clw" -- GENERATED
        #link "target_application.DLL"
    ''')

    def options = new TxaApplicationTransformOptions(applicationName: 'target_application')
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

  void testProceduresAndModulesAreRemoved(){
    def sourceTxa = '' << txaContent('''
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
    ''')

    def targetTxa = '' << txaContent('''
        [APPLICATION]
        VERSION 34
        TODO ABC ToDo
        DICTIONARY 'udea10.dct'
        [COMMON]
    ''')

    def output = '' << ''
    def t = new TxaApplicationTransform(output, new TxaApplicationTransformOptions())
    def reader = new StreamingTxaReader()
    reader.registerHandler(t)
    reader.parse(sourceTxa)
    assertContentEquals(output, targetTxa)
  }

  void testtModulesWithContentAreRemoved() {
    def sourceTxa = '' << txaContent('''
        [APPLICATION]
        VERSION 34
        TODO ABC ToDo
        DICTIONARY 'udea10.dct'
        [COMMON]
        [MODULE]
          [PROCEDURE]
          NAME Procedure1
            [COMMON]
        [END]
        [MODULE]
          [PROCEDURE]
          NAME Procedure1
            [COMMON]
              [EMBED]
                EMBED %WindowManagerMethodCodeSection
                [INSTANCES]
                  WHEN 'Init\'
                  [INSTANCES]
                    WHEN '(),BYTE\'
                    [DEFINITION]
                        [SOURCE]
                        Source code here
                    [END]
                  [END]
                [END]
              [END]
         [END]
         [PROCEDURE]     
    ''')

    def targetTxa = '' << txaContent('''
        [APPLICATION]
        VERSION 34
        TODO ABC ToDo
        DICTIONARY 'udea10.dct'
        [COMMON]
    ''')

    assertSectionsClosedCorrectly(sourceTxa.toString())

    def output = '' << ''
    def t = new TxaApplicationTransform(output, new TxaApplicationTransformOptions())
    def reader = new StreamingTxaReader()
    reader.registerHandler(t)
    reader.parse(sourceTxa)
    assertContentEquals(output, targetTxa)
  }

  void testReferencedProceduresAreMaintained() {
    def sourceTxa = '' << txaContent('''
        [APPLICATION]
        VERSION 34
        TODO ABC ToDo
        DICTIONARY 'udea10.dct'
        PROCEDURE Hoofdmenu
        [COMMON]
        [MODULE]
            [PROCEDURE]
            NAME Hoofdmenu
            [COMMON]
            DESCRIPTION 'Hoofdmenu Historie *** Udea ***\'
            FROM ABC Frame
        [END]
        [MODULE]
          [PROCEDURE]
          NAME Procedure1
            [COMMON]
              [EMBED]
                EMBED %WindowManagerMethodCodeSection
                [INSTANCES]
                  WHEN 'Init\'
                  [INSTANCES]
                    WHEN '(),BYTE\'
                    [DEFINITION]
                        [SOURCE]
                        Source code here
                    [END]
                  [END]
                [END]
              [END]
         [END]
         [PROCEDURE]     
    ''')

    def targetTxa = '' << txaContent('''
        [APPLICATION]
        VERSION 34
        TODO ABC ToDo
        DICTIONARY 'udea10.dct'
        [COMMON]
    ''')

    assertSectionsClosedCorrectly(sourceTxa.toString())

    def output = '' << ''
    def t = new TxaApplicationTransform(output, new TxaApplicationTransformOptions())
    def reader = new StreamingTxaReader()
    reader.registerHandler(t)
    reader.parse(sourceTxa)
    assertContentEquals(output, targetTxa)
  }

  void testGlobalDataIsDeclaredExternalForMainApplicationAndProcedureDll(){
    def sourceTxa = '' << txaContent('''
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
        GloWijzigenVerkoper      SHORT,EXTERNAL,DLL(dll_mode)
        !!> GUID('aee622ca-57f2-4be8-977c-aa51a32f8ca4'),VALID(BOOLEAN),PROMPT('Wijzigen Verkoper:'),HEADER('Wijzigen Verkoper'),PICTURE(@n-7),JUSTIFY(RIGHT,1)
    ''')

    /* We need to take another route here since missing globals are
      inserted into the content which we don't want to test for.
      Therefore we test if the field declarations contain the proper
      attributes
    */
    def validateExternalDll = { line ->
      if (line.trim()[0] == '!' || line.isSectionMark()) {
        return true
      } else {
        return line.contains('EXTERNAL,DLL(dll_mode)')
      }
    }

    StringBuffer output = '' << ''
    def t1 = new TxaApplicationTransform(output, new TxaApplicationTransformOptions(targetType: MainApplication))
    def reader = new StreamingTxaReader()
    reader.registerHandler(t1)
    reader.parse(sourceTxa)

    assertAllGlobals(output, validateExternalDll)

    output = '' << ''
    def t2 = new TxaApplicationTransform(output, new TxaApplicationTransformOptions(targetType: ProcedureDLL))
    def reader2 = new StreamingTxaReader()
    reader2.registerHandler(t2)
    reader2.parse(sourceTxa)
    assertAllGlobals(output, validateExternalDll)
  }

  def assertAllGlobals(StringBuffer content, validator){
    def withinDataSection = false

    content.toString().eachLine { line ->
      if ( line.contains('[DATA]')) {
        withinDataSection = true
      } else if (line.isSectionMark()){
          withinDataSection = line.asSectionMark() in [REPORTCONTROLS, SCREENCONTROLS]
      }

      withinDataSection && assertTrue(validator(line))
    }
  }

  void testGlobalDataIsDeclaredInternalForDataDll() {
    def sourceTxa = '' << txaContent('''
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
        GloWijzigenVerkoper      SHORT,EXTERNAL,DLL(dll_mode)
        !!> GUID('aee622ca-57f2-4be8-977c-aa51a32f8ca4'),VALID(BOOLEAN),PROMPT('Wijzigen Verkoper:'),HEADER('Wijzigen Verkoper'),PICTURE(@n-7),JUSTIFY(RIGHT,1)
    ''')

    def validateNoExternalDll = { line ->
      if (line.trim()[0] == '!' || line.isSectionMark()) {
        return true
      } else {
        return !line.contains('EXTERNAL,DLL')
      }
    }

    StringBuffer output = '' << ''
    def t = new TxaApplicationTransform(output, new TxaApplicationTransformOptions(targetType: DataDLL))
    def reader = new StreamingTxaReader()
    reader.registerHandler(t)
    reader.parse(sourceTxa)
    assertAllGlobals(output, validateNoExternalDll)
  }

  void testQueueFieldsAreDeclaredInternal(){
    def sourceTxa = '' << txaContent('''
          [APPLICATION]
          [PROGRAM]
          [COMMON]
          [DATA]
          [SCREENCONTROLS]
          [REPORTCONTROLS]
          GLO:QueueFustenRegel     QUEUE,PRE()
          [SCREENCONTROLS]
          [REPORTCONTROLS]
          qfr_Fust                   DECIMAL(7),EXTERNAL,DLL(dll_mode) !Verpakkingsnummer
          [SCREENCONTROLS]
          [REPORTCONTROLS]
          qfr_Prijs                  DECIMAL(7,2) !Inkoopprijs NL
                                   END
     ''')

    def targetTxa = '' << txaContent('''
          [APPLICATION]
          [PROGRAM]
          [COMMON]
          [DATA]
          [SCREENCONTROLS]
          [REPORTCONTROLS]
          GLO:QueueFustenRegel     QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
          [SCREENCONTROLS]
          [REPORTCONTROLS]
          qfr_Fust                   DECIMAL(7) !Verpakkingsnummer
          [SCREENCONTROLS]
          [REPORTCONTROLS]
          qfr_Prijs                  DECIMAL(7,2) !Inkoopprijs NL
                                   END
     ''')

    StringBuffer output = '' << ''
    def t = new TxaApplicationTransform(output, new TxaApplicationTransformOptions(targetType: MainApplication))
    def reader = new StreamingTxaReader()
    reader.registerHandler(t)
    reader.parse(sourceTxa)
    assertContentEquals(output,targetTxa)
  }

  void testCapeSoftExtensionsMultiDllEnabled(){
    def sourceTxa = '' << txaContent('''
      [APPLICATION]
        [COMMON]
          [ADDITION]
            NAME The name of the addition
            [INSTANCE]
            INSTANCE 5
            [PROMPTS]
            %MultiDLL LONG  (0)
            %RootDLL LONG  (0)
            %DynamicDLL LONG  (0)
            %FMDataDLL LONG  (0)
            %FMPartOfMultiDLLPrj LONG  (0)
    ''')

    def targetTxaEnabledMain = '' << txaContent('''
      [APPLICATION]
        [COMMON]
          [ADDITION]
            NAME The name of the addition
            [INSTANCE]
              INSTANCE 5
            [PROMPTS]
              %MultiDLL LONG  (1)
              %RootDLL LONG  (0)
              %DynamicDLL LONG  (1)
              %FMDataDLL LONG  (0)
              %FMPartOfMultiDLLPrj LONG  (1)                           
    ''')

    def targetTxaProcedureDll = '' << txaContent('''
      [APPLICATION]
        [COMMON]
          [ADDITION]
            NAME The name of the addition
            [INSTANCE]
              INSTANCE 5
            [PROMPTS]
              %MultiDLL LONG  (1)
              %RootDLL LONG  (0)
              %DynamicDLL LONG  (1)
              %FMDataDLL LONG  (0)
              %FMPartOfMultiDLLPrj LONG  (1)                        
    ''')

    def targetTxaDataDll = '' << txaContent('''
      [APPLICATION]
        [COMMON]
          [ADDITION]
            NAME The name of the addition
            [INSTANCE]
              INSTANCE 5
            [PROMPTS]
              %MultiDLL LONG  (1)
              %RootDLL LONG  (1)
              %DynamicDLL LONG  (1)
              %FMDataDLL LONG  (1)
              %FMPartOfMultiDLLPrj LONG  (1)                        
    ''')

    assertSectionsClosedCorrectly(sourceTxa.toString())

    StringBuffer output = '' << ''
    def t = new TxaApplicationTransform(output, new TxaApplicationTransformOptions(targetType: MainApplication))
    def reader = new StreamingTxaReader()
    reader.registerHandler(t)
    reader.parse(sourceTxa)
    assertContentEquals(output, targetTxaEnabledMain)

    output = '' << ''
    t = new TxaApplicationTransform(output, new TxaApplicationTransformOptions(targetType: ProcedureDLL))
    reader = new StreamingTxaReader()
    reader.registerHandler(t)
    reader.parse(sourceTxa)
    assertContentEquals(output, targetTxaProcedureDll)

    output = '' << ''
    t = new TxaApplicationTransform(output, new TxaApplicationTransformOptions(targetType: DataDLL))
    reader = new StreamingTxaReader()
    reader.registerHandler(t)
    reader.parse(sourceTxa)
    assertContentEquals(output, targetTxaDataDll)
  }

  void testPCVersionDisabledForDLL(){
    def sourceTxa = '' << txaContent('''
      [APPLICATION]
        [COMMON]
          [ADDITION]
            NAME intreqTemplates PC_VersionControl
            [INSTANCE]
            INSTANCE 5
            [PROMPTS]
            %intreqGenerate LONG (1)
            %pcVersieOverslaan LONG (0)
            %pcSubversieOverslaan LONG (0)
    ''')

    def targetTxaDll = '' << txaContent('''
      [APPLICATION]
      [COMMON]
      [ADDITION]
      NAME intreqTemplates PC_VersionControl
      [INSTANCE]
      INSTANCE 5
      [PROMPTS]
      %intreqGenerate LONG (0)
      %pcVersieOverslaan LONG (1)
      %pcSubversieOverslaan LONG (0)
    ''')

    assertSectionsClosedCorrectly(sourceTxa.toString())

    StringBuffer output = '' << ''
    def t = new TxaApplicationTransform(output, new TxaApplicationTransformOptions(targetType: MainApplication))
    def reader = new StreamingTxaReader()
    reader.registerHandler(t)
    reader.parse(sourceTxa)
    assertContentEquals(output, sourceTxa)

    output = '' << ''
    t = new TxaApplicationTransform(output, new TxaApplicationTransformOptions(targetType: ProcedureDLL))
    reader = new StreamingTxaReader()
    reader.registerHandler(t)
    reader.parse(sourceTxa)
    assertContentEquals(output, targetTxaDll)
  }

  void testOnceAttributeAddedToIncludes(){
    def sourceTxa = '' << txaContent('''
        [APPLICATION]
          [COMMON]
          [PROGRAM]
            [COMMON]
            FROM ABC ABC
            [DATA]
            [EMBED]
              EMBED %AfterGlobalIncludes
              [DEFINITION]
                [SOURCE]
                PROPERTY:BEGIN
                PRIORITY 4000
                PROPERTY:END
                include('prnprop.clw')
              [END]
              EMBED %BeforeGlobalIncludes
              [DEFINITION]
                [SOURCE]
                PROPERTY:BEGIN
                PRIORITY 4000
                PROPERTY:END
                INCLUDE('LibXL.clw','Equates'),ONCE
              [END]
              EMBED %GlobalMap
              [DEFINITION]
                [SOURCE]
                PROPERTY:BEGIN
                FIRST 
                PROPERTY:END
                INCLUDE('LibXL.clw','Prototypes')
              [END]
            [END]
          [END]
    ''')

    def targetTxa = '' << txaContent('''
        [APPLICATION]
          [COMMON]
          [PROGRAM]
            [COMMON]
            FROM ABC ABC
            [DATA]
            [EMBED]
              EMBED %AfterGlobalIncludes
              [DEFINITION]
                [SOURCE]
                PROPERTY:BEGIN
                PRIORITY 4000
                PROPERTY:END
                include('prnprop.clw'), ONCE
              [END]
              EMBED %BeforeGlobalIncludes
              [DEFINITION]
                [SOURCE]
                PROPERTY:BEGIN
                PRIORITY 4000
                PROPERTY:END
                INCLUDE('LibXL.clw','Equates'),ONCE
              [END]
              EMBED %GlobalMap
              [DEFINITION]
                [SOURCE]
                PROPERTY:BEGIN
                FIRST 
                PROPERTY:END
                INCLUDE('LibXL.clw','Prototypes'), ONCE
              [END]
            [END]
          [END]            
    ''')

    assertSectionsClosedCorrectly(sourceTxa.toString())
    StringBuffer output = '' << ''
    def t = new TxaApplicationTransform(output, new TxaApplicationTransformOptions(targetType: ProcedureDLL))
    def reader = new StreamingTxaReader()
    reader.registerHandler(t)
    reader.parse(sourceTxa)
    assertContentEquals(output, targetTxa)
  }
}
