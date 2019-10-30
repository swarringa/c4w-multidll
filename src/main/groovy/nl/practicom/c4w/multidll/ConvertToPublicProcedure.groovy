package nl.practicom.c4w.multidll

import nl.practicom.c4w.txa.transform.SectionMark
import nl.practicom.c4w.txa.transform.TxaContext

import static nl.practicom.c4w.txa.transform.SectionMark.END


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
