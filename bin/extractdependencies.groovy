@Grab(group='nl.practicom', module='c4w-txa', version='0.1')
@Grab(group='nl.practicom', module='multidll', version='0.1')

import nl.practicom.c4w.multidll.ProcedureDependencyScanner
import nl.practicom.c4w.txa.transform.StreamingTxaReader

def txaFile = new File("/Volumes/Projects/Clients/Udea/tmp/historie10.txa")
def scanner = new ProcedureDependencyScanner()

new StreamingTxaReader()
    .withHandler(scanner)
    .parse(txaFile)

scanner.dependencies.Hoofdmenu.sort().each {
    println it
}

assert scanner.dependencies.Hoofdmenu.size() == 71