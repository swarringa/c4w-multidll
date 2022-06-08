package nl.intreq.c4w.multidll.transforms.procedure

import nl.intreq.c4w.txa.transform.SectionMark
import nl.intreq.c4w.txa.transform.TxaContext

/**
 * Set the MODIFIED atrribute of a procedure to the current date and time
 **/
class ModificationDateTransform extends ChainableTransform {
    final modifiedDatePattern = /^\s*MODIFIED\s+[0-9\/:\s']+$/

    @Override
    String transformSectionContent(TxaContext context, SectionMark section, String content) {
        /* Set modification date to this moment */
        if (content ==~ modifiedDatePattern){
            return "MODIFIED ${LocalDateTime.now().toClarionDateTimeString()}"
        } else {
            return super.transformSectionContent(context, section, content)
        }
    }
}
