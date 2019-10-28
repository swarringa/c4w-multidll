package nl.practicom.c4w.multidll

class TxaTransformOptions {

  enum ApplicationType {
    MainApplication, ProcedureDLL, DataDLL
  }

  // Name of the application
  String applicationName

  ApplicationType targetType

  // list of procedures names to be included and exported. Only applicable for DLL
  List<String> publicProcedures = []

  //List of procedure to be included but not exported. Only applicable for DLL
  List<String> privateProcedures = []

  // List of procedures to be declared as external
  List<String> externalProcedures = []

  // Number of procedures per module. Set to 0 to generate a single module
  int numProceduresPerModule = 20
}
