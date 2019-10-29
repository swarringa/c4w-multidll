package nl.practicom.c4w.multidll

import nl.practicom.c4w.txa.transform.SectionMark
import nl.practicom.c4w.txa.transform.TxaContext

import static nl.practicom.c4w.txa.transform.SectionMark.COMMON
import static nl.practicom.c4w.txa.transform.SectionMark.PROCEDURE

class ConvertToPrivateProcedure implements ProcedureTransform {

  private final static EOL = System.lineSeparator()

  @Override
  String transformSectionStart(TxaContext context, SectionMark section) {
    return context.currentLine
  }

  @Override
  String transformSectionEnd(TxaContext context, SectionMark section) {
    return context.currentLine
  }

  @Override
  String transformSectionContent(TxaContext context, SectionMark section, String content) {
    if (context.currentSection == PROCEDURE && section == COMMON) {
        return 'NOEXPORT' + EOL + section
      } else {
        return content
      }
  }

}
