package nl.intreq.c4w.multidll.transforms.procedure

import nl.intreq.c4w.multidll.transforms.procedure.ProcedureTransform
import nl.intreq.c4w.txa.transform.SectionMark
import nl.intreq.c4w.txa.transform.TxaContext

import static nl.intreq.c4w.txa.transform.SectionMark.*

/**
 * Marks a procedure as private by adding a NOEXPORT flag
 **/
class ConvertToPrivateProcedure implements ProcedureTransform {

  private final static EOL = System.lineSeparator()

  private hasNoExportFlag = false
  private noExportFlagWritten = true

  @Override
  String transformSectionStart(TxaContext context, SectionMark section) {
    if ( context.currentSection == PROCEDURE && section == COMMON && !hasNoExportFlag){
      noExportFlagWritten = false
      return 'NOEXPORT' + EOL + section
    } else {
      return section.toString()
    }
  }

  @Override
  String transformSectionEnd(TxaContext context, SectionMark section) {
    if ( context.isProcedureDeclaration(section)){
      if (!hasNoExportFlag && noExportFlagWritten){
        noExportFlagWritten = false
        return 'NOEXPORT'
      }
    } else {
      return section.requiresExplicitEnd() && context.currentLine.isSectionEnd() ? END : null
    }
  }

  @Override
  String transformSectionContent(TxaContext context, SectionMark section, String content) {
    if ( content == 'NOEXPORT') {
      hasNoExportFlag = true
    }
    return content
  }

}
