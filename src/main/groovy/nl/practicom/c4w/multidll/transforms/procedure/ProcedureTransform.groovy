package nl.intreq.c4w.multidll.transforms.procedure

import nl.intreq.c4w.txa.transform.SectionMark
import nl.intreq.c4w.txa.transform.TxaContext

/**
 * Central interface for [PROCEDURE] section transformations
 */
interface ProcedureTransform {
  
  /**
   * Called when a current line starts a new section
   * @param context   the current parser context containing
   *                  information on content preceding this section
   * @param section   the section mark encountered
   * @return          Content to insert at start of the section or null
   */
  String transformSectionStart(TxaContext context, SectionMark section)

  /**
   * Called for each line in the current section
   * @param context   the current parser context containing
   *                  information on content preceding this line
   * @param section   the section that is currently parsed
   * @return          transformed content to replace this line
   **/
  String transformSectionContent(TxaContext context, SectionMark section, String content)

 /**
   * Called when end of section is reached
   * @param context   the current parser context containing
   *                  information on content preceding this section
   * @param section   the section mark being closed
   * @return          Content to insert at end of the section or null
   */
  String transformSectionEnd(TxaContext context, SectionMark section)
}