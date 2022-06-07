package nl.intreq.c4w.multidll.testsupport

import nl.intreq.c4w.multidll.dto.ProcedureInfo
import nl.intreq.c4w.multidll.transforms.procedure.ProcedureIdentityTransform
import nl.intreq.c4w.multidll.transforms.procedure.ProcedureTransform
import nl.intreq.c4w.multidll.transforms.procedure.ProcedureTransformFactory

class ProcedureTransformTestFactory implements ProcedureTransformFactory {
  private transform

  ProcedureTransformTestFactory() {
    this.transform = new ProcedureIdentityTransform()
  }

  ProcedureTransformTestFactory withDefaultTransform(ProcedureTransform defaultTransform) {
    this.transform = defaultTransform
    return this
  }

  @Override
  ProcedureTransform getTransform(ProcedureInfo procedureInfo) {
    return this.transform
  }
}
