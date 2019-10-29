package nl.practicom.c4w.multidll

class TxaTransformOptions {

  enum ApplicationType {
    MainApplication, ProcedureDLL, DataDLL
  }

  // Name of the application
  String applicationName

  // The type of multi-DLL app to generate
  ApplicationType targetType

}
