package nl.practicom.c4w.multidll

import nl.practicom.c4w.txa.transform.*

import static nl.practicom.c4w.txa.transform.SectionMark.APPLICATION

class ProcedureInfoScanner implements TxaContentHandler, TxaSectionHandler, TxaLogicalContentHandler {

  /* Regular expression patterns used to inspect content */
  final private static MAIN_PROCEDURE_DECLARATION = ~/^\s*PROCEDURE\s+(\w+)\s*$/
  final private static TEMPLATE_DECLARATION = ~/^FROM\s+(\w[\w\s]+)\s*$/
  final private static NOEXPORT_FLAG = ~/^\s*NOEXPORT\s*$/

  List<ProcedureInfo> procedures = []

  private ProcedureInfo currentProcedure = null

  private String mainProcedureName

  def getMainProcedure(){
      procedures.find { it.isMainProcedure }
  }

  def getPrivateProcedures() {
    procedures.findAll { !it.isExported }
  }

  def getPublicProcedures() {
    procedures.findAll { it.isExported }
  }

  @Override
  void onProcessingStart(TxaContext context) {
    // Nothing to do here
  }

  @Override
  void onProcessingFinished(TxaContext context) {
    // Nothing to do here
  }

  @Override
  void onSectionStart(TxaContext ctx, SectionMark section) {
    if (isProcedureDeclaration(ctx, section)) {
      currentProcedure = new ProcedureInfo()
      currentProcedure.lineNumber = ctx.currentLineNumber
    }
  }

  @Override
  void onSectionContent(TxaContext context, SectionMark section, Long lineNo, String content) {
    if ( section == APPLICATION && content ==~ MAIN_PROCEDURE_DECLARATION ){
      (content =~ MAIN_PROCEDURE_DECLARATION).each { _, name ->
        mainProcedureName = name
      }
    }

    if ( currentProcedure ){
      if ( currentProcedure.name == null && context.currentProcedureName){
        currentProcedure.name = context.currentProcedureName
        currentProcedure.isMainProcedure = ( currentProcedure.name == mainProcedureName )
      }

      if (!currentProcedure.template) {
        (content =~ TEMPLATE_DECLARATION).each { _, templateName ->
          currentProcedure.template = templateName
        }
      }

      if ( content ==~ NOEXPORT_FLAG ){
        currentProcedure.isExported = false
      }
    }
  }

  @Override
  void onSectionEnd(TxaContext context, SectionMark section) {
    if ( currentProcedure && isProcedureDeclaration(context, section)){
      this.procedures << currentProcedure
      currentProcedure = null
    }
  }

  def static isProcedureDeclaration(TxaContext context,SectionMark section) {
    section == SectionMark.PROCEDURE && !context.within(SectionMark.DEFINITION)
  }

}
