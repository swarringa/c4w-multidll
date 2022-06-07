package nl.intreq.c4w.multidll.transforms.procedure

import nl.intreq.c4w.multidll.dto.ProcedureInfo
import nl.intreq.c4w.multidll.transforms.procedure.ConvertToExternalProcedure
import nl.intreq.c4w.multidll.transforms.procedure.ConvertToPrivateProcedure
import nl.intreq.c4w.multidll.transforms.procedure.ConvertToPublicProcedure
import nl.intreq.c4w.multidll.transforms.procedure.ProcedureTransform
import nl.intreq.c4w.multidll.transforms.procedure.ProcedureTransformFactory

class ProcedureListTransformFactory implements ProcedureTransformFactory {

  def ProcedureTransform publicTransform = new ConvertToPublicProcedure()
  def ProcedureTransform privateTransform = new ConvertToPrivateProcedure()
  def ProcedureTransform externalTransform = new ConvertToExternalProcedure()

  // list of procedures names to be included and exported. Only applicable for DLL
  List<String> publicProcedures = []

  //List of procedure to be included but not exported. Only applicable for DLL
  List<String> privateProcedures = []

  // List of procedures to be declared as external
  List<String> externalProcedures = []

    ProcedureTransform getTransform(ProcedureInfo procedureInfo){
    if (publicProcedures.contains(procedureInfo.name)){
      return this.publicTransform
    } else if ( privateProcedures.contains(procedureInfo.name)){
      return this.privateTransform
    } else if ( externalProcedures.contains(procedureInfo.name)){
      return this.externalTransform
    } else {
      return null
    }
  }
}
