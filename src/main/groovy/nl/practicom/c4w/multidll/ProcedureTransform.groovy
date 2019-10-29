package nl.practicom.c4w.multidll

import nl.practicom.c4w.txa.transform.SectionMark
import nl.practicom.c4w.txa.transform.TxaContext

interface ProcedureTransform {
  String transformSectionStart(TxaContext context, SectionMark section)
  String transformSectionContent(TxaContext context, SectionMark section, String content)
  String transformSectionEnd(TxaContext context, SectionMark section)
}