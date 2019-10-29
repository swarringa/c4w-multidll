package nl.practicom.c4w.multidll

import nl.practicom.c4w.txa.transform.SectionMark
import nl.practicom.c4w.txa.transform.TxaContext

import static nl.practicom.c4w.txa.transform.SectionMark.END

class ProcedureIdentityTransform implements ProcedureTransform {

  @Override
  String transformSectionStart(TxaContext context, SectionMark section) {
    return section.toString()
  }

  @Override
  String transformSectionContent(TxaContext context, SectionMark section, String content) {
    return content
  }

  @Override
  String transformSectionEnd(TxaContext context, SectionMark section) {
    return section.requiresExplicitEnd() && context.currentLine.isSectionEnd()? END : null
  }
}
