package nl.practicom.c4w.multidll

class ProcedureListTransformFactory implements ProcedureTransformFactory {

  // list of procedures names to be included and exported. Only applicable for DLL
  List<String> publicProcedures = []

  //List of procedure to be included but not exported. Only applicable for DLL
  List<String> privateProcedures = []

  // List of procedures to be declared as external
  List<String> externalProcedures = []

  ProcedureTransform getTransform(String procedureName){
    if (publicProcedures.contains(procedureName)){
      return new ConvertToPublicProcedure()
    } else if ( privateProcedures.contains(procedureName)){
      return new ConvertToPrivateProcedure()
    } else if ( externalProcedures.contains(procedureName)){
      return new ConvertToExternalProcedure()
    } else {
      return null
    }
  }
}
