package nl.intreq.c4w.multidll.transforms.procedure

import nl.intreq.c4w.multidll.transforms.procedure.ProcedureTransform
import nl.intreq.c4w.txa.transform.SectionMark
import nl.intreq.c4w.txa.transform.TxaContext

import static nl.intreq.c4w.txa.transform.SectionMark.END

/**
 * Identity transformation for a procedure
 **/
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
