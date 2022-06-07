package nl.intreq.c4w.multidll.transforms.procedure


import nl.intreq.c4w.txa.transform.SectionMark
import nl.intreq.c4w.txa.transform.TxaContext

/**
 * This procedure transformation chains together multiple procedure transforms.
 * It implements the 'chain of responsibilty' pattern where each handler
 * (procedure transform) decides if it wants to process the input. 
 *
 * If a handler does not want to process the input it will should return a null. 
 * Anything else will be considered transformed content.
 *
 * Once a handler transforms the input the chain will emit the transformed input. 
 * Any remaining handlers will be ignored.
 *
 * Therefore order in which the transformations are added is important!
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
