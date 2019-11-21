package nl.practicom.c4w.multidll.testsupport

import nl.practicom.c4w.multidll.dto.ProcedureInfo
import nl.practicom.c4w.multidll.transforms.procedure.ProcedureIdentityTransform
import nl.practicom.c4w.multidll.transforms.procedure.ProcedureTransform
import nl.practicom.c4w.multidll.transforms.procedure.ProcedureTransformFactory

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
