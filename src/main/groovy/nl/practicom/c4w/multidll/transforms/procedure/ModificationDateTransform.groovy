package nl.practicom.c4w.multidll.transforms.procedure

import nl.practicom.c4w.txa.transform.SectionMark
import nl.practicom.c4w.txa.transform.TxaContext

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
