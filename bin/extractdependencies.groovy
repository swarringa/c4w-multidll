@Grab(group='nl.practicom', module='c4w-txa', version='0.1')
@Grab(group='nl.practicom', module='multidll', version='0.1')

import nl.practicom.c4w.multidll.ProcedureDependencyScanner
import nl.practicom.c4w.txa.transform.StreamingTxaReader
import nl.practicom.c4w.multidll.EntryProcedureScanner

def generateProcedureList(txaFile, targetFile, procedureName, menuControl) {
    def entryScanner = new EntryProcedureScanner(procedureName)
    def depsScanner = new ProcedureDependencyScanner()

    println "Scanning ${txaFile}"
    new StreamingTxaReader()
            .withHandler(entryScanner, depsScanner)
            .parse(txaFile)

    println "Collecting entry procedures"
    def entryProcedures = entryScanner.entryProceduresFor("?Stamgegevens")
    println "Collecting dependent procedures"
    def calledProcedures = depsScanner.getTransitiveDependencies(entryProcedures)

    def EOL = System.lineSeparator()
    targetFile.withWriter { writer ->
        entryProcedures.each { writer << it << EOL }
        if (calledProcedures.size() > 0) {
            writer << "[private]"
            calledProcedures.each { writer << EOL << it }
        }
    }

    return new Tuple(entryProcedures.size() , calledProcedures.size())
}

def txaFile = new File("/Volumes/Projects/Clients/Udea/tmp/invervo10.txa")
def targetFile = new File("/Volumes/Projects/Clients/Udea/tmp/stamgegevens_procedures.txt")
(nrofentries, nrofdeps) = generateProcedureList(txaFile,targetFile,"Hoofdmenu","?Stamgegevens")

println("Wrote ${nrofentries} public procedures and ${nrofdeps} private procedures to ${targetFile}")
