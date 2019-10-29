package nl.practicom.c4w.multidll

class ProcedureIdentityTransformFactory extends ProcedureTransformFactory {
  private final static identityTransform = new ProcedureIdentityTransform()

  @Override
  ProcedureTransform getTransform(String procedureName) {
    return identityTransform
  }
}
