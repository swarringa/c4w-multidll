package nl.practicom.c4w.multidll

class ProcedureTransformTestFactory extends ProcedureTransformFactory {
  private transform

  ProcedureTransformTestFactory() {
    this.transform = new ProcedureIdentityTransform()
  }

  ProcedureTransformTestFactory withDefaultTransform(ProcedureTransform defaultTransform) {
    this.transform = defaultTransform
    return this
  }

  @Override
  ProcedureTransform getTransform(String procedureName) {
    return this.transform
  }
}
