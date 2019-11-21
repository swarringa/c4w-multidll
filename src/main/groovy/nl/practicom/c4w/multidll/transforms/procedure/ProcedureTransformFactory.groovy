package nl.practicom.c4w.multidll.transforms.procedure

import nl.practicom.c4w.multidll.dto.ProcedureInfo
import nl.practicom.c4w.multidll.transforms.procedure.ProcedureTransform

/**
 * In many cases we need to apply different transform to
 * different procedures. This can be solved by writing
 * a single transform that can handle all cases but this
 * may result in complex code. Also, the transform to apply
 * may not depend on just the procedure attributes or content
 * but is determined in another way.
 * The ProcedureTransformFactor provides an interface to
 * implement dynamic transformation construction to handle
 * these cases.
 */
interface ProcedureTransformFactory {
    ProcedureTransform getTransform(ProcedureInfo procedureInfo)
}