package nl.practicom.c4w.multidll.transforms.procedure


import nl.practicom.c4w.txa.transform.SectionMark
import nl.practicom.c4w.txa.transform.TxaContext

/**
 * This procedure transformation chains together multiple transforms.
 *
 * The current implementation provides the most basic 'first one wins'
 * algorithm: the first transformation that returns some content will determine
 * the results outputted.
 *
 * The transformations used with this transformation chain should therefore always
 * output 'null' when there's nothing to transform!
 * 
 * Also the order in which the transformations are added is therefore important
 * since this is the order in which transforms are called.
 */
class ProcedureTransformChain implements ProcedureTransform {
    List<ProcedureTransform> transformChain = []

    ProcedureTransformChain append(ProcedureTransform... transforms){
        this.transformChain.addAll(transforms)
        return this
    }

    @Override
    String transformSectionStart(TxaContext context, SectionMark section) {
        def transformedContent = null
        def ti = this.transformChain.iterator()
        while ( transformedContent == null && ti.hasNext()){
            transformedContent = ti.next().transformSectionStart(context, section)
        }
        return transformedContent
    }

    @Override
    String transformSectionContent(TxaContext context, SectionMark section, String content) {
        def transformedContent = null
        def ti = this.transformChain.iterator()
        while ( transformedContent == null && ti.hasNext()){
            transformedContent = ti.next().transformSectionContent(context, section, content)
        }
        return transformedContent
    }

    @Override
    String transformSectionEnd(TxaContext context, SectionMark section) {
        def transformedContent = null
        def ti = this.transformChain.iterator()
        while ( transformedContent == null && ti.hasNext()){
            transformedContent = ti.next().transformSectionEnd(context, section)
        }
        return transformedContent
    }
}
