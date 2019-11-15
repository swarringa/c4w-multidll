package nl.practicom.c4w.multidll


import nl.practicom.c4w.txa.transform.SectionMark
import nl.practicom.c4w.txa.transform.StreamingTxaTransform
import nl.practicom.c4w.txa.transform.TxaContext

import java.util.regex.Matcher

/**
 * Transforms an application (txa) to create another application (txa)
 * Supported transforms:
 * A: Main Application (exe) -> Multi-DLL Main application
 * B: Main Application -> Data DLL
 * C: Main Aplication -> Procedure DLL
 * D: Procedure DLL -> Procedure DLL
 *
 * The resulting application (txa) will not contain any procedures. These will have
 * to be manually imported from the source application or a txa.
 *
 * The following transformations are applied:
 * - Changing the build model from exe to dll (B,C)
 * - Externalize global data (A,C)
 * - Externalize files (A,C)
 * - Removing procedures and their parent modules
 * - Export globals and template globals (B)
 * - Export file declarations (B)
 * - Update the project section:
 *   - update file type of the #system command (B,C)
 *   - remove #compile and #link commands
 *   - add #compile command for main module
 *   - add #compile command for BC module
 *   - add link command for application
 */

import static nl.practicom.c4w.multidll.TxaTransformOptions.ApplicationType.*
import static nl.practicom.c4w.txa.transform.SectionMark.*
/**
 * Generates a new application TXA file from source TXA.
 */
class TxaApplicationTransform extends StreamingTxaTransform {
    TxaTransformOptions options

    // Global state
    String currentPrompt = null
    Boolean insideQueue = false
    String[] referencedProcedures

    /**
     * Use for testing
     * @param txaout
     * @param transformOptions
     */
    TxaApplicationTransform(StringBuffer txaout, TxaTransformOptions transformOptions){
        super(txaout)
        this.options = transformOptions
    }

    /**
     * Generate transformed TXA file
     * @param targetTxaFile - absolute path where txa should be generated
     * @param transformOptions - instructions on required transformations
     */
    TxaApplicationTransform(File targetTxaFile, TxaTransformOptions transformOptions) {
        /* ToDo: path resolution, existence and access checks */
        super(targetTxaFile.newWriter())
        this.options = transformOptions
    }

    /**
     * Write transformed TXA to outputstream
     * @param txaout
     * @param transformOptions
     */
    TxaApplicationTransform(OutputStream txaout,TxaTransformOptions transformOptions) {
        super(txaout.newWriter())
        this.options = transformOptions
    }

    /**
     * Write tranformed TXA to writer
     * @param txaout
     * @param transformOptions
     */
    TxaApplicationTransform(Writer txaout, TxaTransformOptions transformOptions) {
        super(txaout)
        this.options = transformOptions
    }

    @Override
    protected String transformInitialize(TxaContext context) {
        return super.transformInitialize(context)
    }

    @Override
    protected String transformSectionStart(TxaContext context, SectionMark section) {
        def output = '' << section

        if ( section == MODULE || context.within(MODULE)) {
            return null
        }

        if ( section == PROCEDURE || context.within(PROCEDURE)) {
            return null
        }

        if (section == DATA && context.within(PROGRAM)){
            if ( options.targetType == DataDLL) {
                def resourceURL = TxaApplicationTransform.classLoader.getResource("missing_globals.txa")
                if (resourceURL) {
                    output << EOL << resourceURL.text
                }
            }
        }

        return output.toString()
    }

    @Override
    protected String transformSectionContent(TxaContext context, SectionMark section, String content) {
        if ( section == MODULE || context.within(MODULE)) {
            return null
        }

        if ( section == PROCEDURE || context.within(PROCEDURE)) {
            return null
        }

        if ( section == APPLICATION && context.currentSection == APPLICATION){
            return processApplicationContent(context, content)
        }

        if ( context.within(APPLICATION,COMMON,ADDITION)) {
            return processCapeSoftGlobalExtensions(context, content)
        }

        if ( context.within(APPLICATION,COMMON,PROMPTS)){
          return processGlobalPromptContent(context, content)
        }

        if ( section == PROJECT || context.within(PROJECT)){
            return processProjectContent(context, content)
        }

        if ( context.within(PROGRAM,COMMON,DATA,REPORTCONTROLS)){
            return processGlobalData(context, content)
        }

        return super.transformSectionContent(context, section, content)
    }

    @Override
    protected String transformSectionEnd(TxaContext context, SectionMark section) {
        switch(section){
            case PROJECT:
                return processProjectEnd(context)
                break
            case MODULE:
                return null
                break
            default:
                if (context.within(MODULE)) {
                    return null
                } else {
                    return super.transformSectionEnd(context, section)
                }
        }
    }

    @Override
    protected String transformFinalize(TxaContext context) {
        return super.transformFinalize(context)
    }

    /* Transformation functions */

    String processApplicationContent(TxaContext context, String content) {
      def output = content

      // Remove main procedure for DLL's
      if ( options.targetType != MainApplication )  {
        if ( content.startsWith('PROCEDURE') ) {
          output = null
        }
      }

      return output
    }

    String processGlobalPromptContent(TxaContext context, String content) {
      final def PROMPT_PATTERN = ~/^(%\w+)\s.*$/

      def output = content

      Matcher matcher = (content =~ PROMPT_PATTERN)

      if ( matcher.matches()){
        currentPrompt = matcher[0][1] as String

        switch(currentPrompt) {
          case '%StandardExternalModule':
            if ( currentPrompt == '%StandardExternalModule' && options.applicationName != null){
              output = '' << '%StandardExternalModule DEPEND %Module LONG TIMES 3' << EOL
              output << "WHEN  ('') (1)" << EOL
              output << "WHEN  ('${options.applicationName}.clw') (1)" << EOL
              output << "WHEN  ('${options.applicationName}001.clw') (1)"
            }
            break
          case "%GlobalExternal":
            if ( options.targetType == DataDLL){
              output = "%GlobalExternal LONG  (0)"
            } else {
              output = "%GlobalExternal LONG  (1)"
            }
            break
          case "%DefaultExternal":
            if (options.targetType == DataDLL){
              output = "%DefaultExternal DEFAULT  ('None External')"
            } else {
              output = "%DefaultExternal DEFAULT  ('All External')"
            }
            break
          case "%DefaultExternalAPP":
            if (options.targetType == DataDLL){
              output = "%DefaultExternalAPP LONG  (0)"
            } else {
              output = "%DefaultExternalAPP LONG  (1)"
            }
            break
          case "%DefaultGenerate":
            if (options.targetType == DataDLL){
              output = "%DefaultGenerate LONG  (1)"
            } else {
              output = "%DefaultGenerate LONG  (0)"
            }
            break
        case "%DefaultExport":
            if (options.targetType == DataDLL){
                output = "%DefaultExport LONG  (1)"
            } else {
                output = "%DefaultExport LONG  (0)"
            }
            break
        }
      } else if ( content.startsWith('WHEN')) {
        if ( currentPrompt == '%StandardExternalModule' && options.applicationName != null){
          output = null // suppress existing prompt conditions from source
        }

      } else if ( content.trim().size() == 0) {
        currentPrompt = null
      }

      return output
    }

    String processGlobalData(TxaContext context, String content) {
        // This pattern breaks down the field declaration into 3 parts
        // 1. Name + Type (required)
        // 2. Field attributes (optional)
        // 3. A comment (optional)
        def final FIELD_DECLARATION = ~/^([^,!]*)([^!]*?)(\s+!.*)?$/

        // ! Marks a control declaration of keyword list which we pass on
        if ( content.startsWith('!')){
            return content
        }

        if ( content.trim().toUpperCase() == "END"){
            this.insideQueue = false // assumme maximum nesting is 1
            return content
        }

        if ( content.trim().size() == 0){
            return content
        }

        def output = content
        Matcher matcher = (content =~ FIELD_DECLARATION)

        if ( context.currentSection == REPORTCONTROLS && matcher.matches()){
            String fieldDeclaration = (matcher[0][1]).trim()
            def fieldAttributes = (matcher[0][2]).split(',') as List<String>
            def fieldComment = matcher[0][3] ?: ""
            def isExternalDeclaration = fieldAttributes?.contains('EXTERNAL')

            if (options.targetType == DataDLL || this.insideQueue){
                if (isExternalDeclaration){
                    fieldAttributes.removeAll(['EXTERNAL','DLL(dll_mode)'])
                }
            } else {
                // For main application and procedure dll global data needs to be
                // declared as EXTERNAL,DLL
                if (isExternalDeclaration) {
                    if ( !fieldAttributes.contains('DLL')){
                        fieldAttributes << 'DLL(dll_mode)'
                    }
                } else {
                    fieldAttributes << 'EXTERNAL' << 'DLL(dll_mode)'
                }
            }

            output = fieldDeclaration + fieldAttributes.join(',') + fieldComment

            if ( fieldDeclaration.trim().toUpperCase().contains('QUEUE')){
                this.insideQueue = true
            }
        }

        return output
    }

    def processProjectContent(TxaContext context, String content) {
        final SYSTEM_COMMAND = ~/^#system\s+(\w+)\s(\w+)\s*$/
        final LinkModePragma = ~/^#pragma define\(((.*)LinkMode(.*))=>[0,1]\)(.*)$/
        final DllModePragma = ~/^#pragma define\(((.*)DllMode(.*))=>[0,1]\)(.*)$/

        def output = "" << ""

        // Pass section start
        if ( content.asSectionMark() == PROJECT){
            return content
        }

        // Replace system command with appropriate target type
        if ( content ==~ SYSTEM_COMMAND ){
            if (options.targetType == MainApplication) {
                (content =~ SYSTEM_COMMAND).each {
                    _, platform, model -> output << "#system ${platform} exe"
                }
            } else {
                (content =~ SYSTEM_COMMAND).each {
                    _, platform, model -> output << "#system ${platform} dll"
                }
            }
            return output
        }

        // Remove compile and link commands for source application
        if ( content.startsWith("#compile")|| content.startsWith("#link")){
            return null
        }

        // Disable link-mode: Link
        if (content ==~ LinkModePragma){
            def matches =  (content =~ LinkModePragma)[0]
            def symbol = matches[1]
            def postamble = matches[4]
            output << "#pragma define(${symbol}=>0)$postamble"
            return output
        }

        // Enable link-mode DLL
        if (content ==~ DllModePragma){
            def matches =  (content =~ DllModePragma)[0]
            def symbol = matches[1]
            def postamble = matches[4]
            output << "#pragma define(${symbol}=>1)$postamble"
            return output
        }

        return content
    }

    /* Insert compile and link commands for target */
    def processProjectEnd(TxaContext context){
        def output  = null

        if ( options.applicationName != null && options.applicationName.trim().size() > 0) {
          output = '' << ''
          output << "#compile \"${options.applicationName}.clw\" -- GENERATED" << EOL
          output << "#compile \"${options.applicationName}001.clw\" -- GENERATED" << EOL

          if (options.targetType == MainApplication) {
            output << "#link \"${options.applicationName}.EXE\""
          } else {
            output << "#link \"${options.applicationName}.DLL\""
          }
        }

        return output
    }

    def processCapeSoftGlobalExtensions(TxaContext ctx,  String content){
        def output = content
        if ( ctx.currentSection == PROMPTS) {
            if (content.startsWith("%MultiDLL")) {
                // Generic capesoft
                output = "%MultiDLL LONG  (1)"
            } else if (content.startsWith("%RootDLL")) {
                // Generic capesoft
                output = options.targetType == DataDLL ? "%RootDLL LONG  (1)" : "%RootDLL LONG  (0)"
            } else if(content.startsWith("%DynamicDLL")){
                // Nettalk Dynamic DLL support
                output = options.targetType == DataDLL ? "%DynamicDLL LONG  (1)" : "%DynamicDLL LONG  (0)"
            }
        }
        return output
    }

    def processGlobalAdditionContent(TxaContext ctx, String content){
        def PROCEDURE_REFERENCE = ~/%(.*)\s+PROCEDURE\s+\(.*\)/

    }
}
