package nl.intreq.c4w.multidll.transforms.procedure

import nl.intreq.c4w.multidll.transforms.procedure.ProcedureTransform
import nl.intreq.c4w.txa.transform.SectionMark
import nl.intreq.c4w.txa.transform.TxaContext

import static nl.intreq.c4w.txa.transform.SectionMark.END

/**
 * Removes the NOEXPORT flag from a procedure to make it a public procedure
 **/
class ConvertToPublicProcedure implements ProcedureTransform {

  @Override
  String transformSectionStart(TxaContext context, SectionMark section) {
    if (context.isProcedureDeclaration(section) || context.within(SectionMark.PROCEDURE)){
      return section.toString()
    }
  }

  @Override
  String transformSectionContent(TxaContext context, SectionMark section, String content) {
    if ( content.equalsIgnoreCase("NOEXPORT")){
      return null
    } else {
      return content
    }
  }

  @Override
  String transformSectionEnd(TxaContext context, SectionMark section) {
    return section.requiresExplicitEnd() && context.currentLine.isSectionEnd() ? END : null
  }
}
