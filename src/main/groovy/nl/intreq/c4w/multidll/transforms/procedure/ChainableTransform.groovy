package nl.intreq.c4w.multidll.transforms.procedure

import nl.intreq.c4w.multidll.transforms.procedure.ProcedureTransform
import nl.intreq.c4w.txa.transform.SectionMark
import nl.intreq.c4w.txa.transform.TxaContext

/**
 * A transformation within a transformation chain should follow
 * a different contract than a stand-alone transformation.
 *
 * Inside a transformation chain the transformation should only
 * return a non-null value when the content needs to be replaced
 * and null otherwise. A stand-alone transform returns null when the
 * content should be discarded.
 *
 * ToDo: transformations should be usable as both stand-alone and chainable transforms
 */
class ChainableTransform implements ProcedureTransform {
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
