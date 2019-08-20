@Grab(group='nl.practicom', module='c4w-txa', version='0.1')
@Grab(group='nl.practicom', module='multidll', version='0.1')

import nl.practicom.c4w.multidll.DllTxaGenerator

def buildProcedureLists(File proceduresFile){
    def publicProcedures = []
    def privateProcedures = []
    def withinPrivateSection = false

    println "Reading procedures from ${proceduresFile}"

    proceduresFile.withReader { reader ->
        def line
        while (line = reader.readLine()?.trim()){
            if ( line.equalsIgnoreCase('[private]')) {
                withinPrivateSection = true
                continue
            }
            if ( withinPrivateSection && !publicProcedures.contains(line)){
                privateProcedures.add(line)
            } else {
                publicProcedures.addAll(line)
            }
        }
    }

    println "Read ${publicProcedures.size()} public and ${privateProcedures.size()} private procedures"
    return new Tuple(publicProcedures,privateProcedures)
}

def txaFile = new File("/Volumes/Projects/Clients/Udea/tmp/historie10.txa")
def proceduresFile = "/Volumes/Projects/Clients/Udea/tmp/stamgegevens_procedures_selectie.txt"
def dllTxaFile = "/Volumes/Projects/Clients/Udea/tmp/stamgegevens_dll.txa"
def dllExternalTxaFile = new File("/Volumes/Projects/Clients/Udea/tmp/stamgegevens_dll_ext.txa")

(publicProcedures, privateProcedures) = buildProcedureLists(proceduresFile)
generateDllTxa(txaFile, publicProcedures, privateProcedures, dllTxaFile, dllExternalTxaFile)
