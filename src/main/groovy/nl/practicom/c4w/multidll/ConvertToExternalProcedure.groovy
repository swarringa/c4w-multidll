package nl.practicom.c4w.multidll

import nl.practicom.c4w.txa.transform.SectionMark
import nl.practicom.c4w.txa.transform.TxaContext

class ConvertToExternalProcedure implements ProcedureTransform {
  @Override
  String transformSectionStart(TxaContext context, SectionMark section) {
    return null
  }

  @Override
  String transformSectionContent(TxaContext context, SectionMark section, String content) {
    return null
  }

  @Override
  String transformSectionEnd(TxaContext context, SectionMark section) {
    return null
  }
}
