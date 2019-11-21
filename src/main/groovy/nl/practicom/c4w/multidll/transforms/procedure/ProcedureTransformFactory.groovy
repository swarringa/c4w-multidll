package nl.practicom.c4w.multidll.transforms.procedure

import nl.practicom.c4w.multidll.transforms.procedure.ProcedureTransform

interface ProcedureTransformFactory {
    ProcedureTransform getTransform(String procedureName)
}