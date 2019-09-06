@GrabResolver(name='practicom_dev', root='http://localhost:8080/grapes/practicom/')
@Grab("nl.practicom:c4w-txa:0.1")
@Grab("nl.practicom:c4w-multidll:0.1")

import nl.practicom.c4w.multidll.ProcedureDependencyScanner
import nl.practicom.c4w.txa.transform.StreamingTxaReader

final EOL = System.lineSeparator()

interface TxaGraphTransform {
    def writeHeader(writer)
    def writeGraphStart(writer)
    def writeNodesHeader(writer)
    def writeNode(writer, nodeId, nodeLabel)
    def writeNodesFooter(writer)
    def writeEdgesHeader(writer)
    def writeEdge(writer, edgeId, sourceId, targetId)
    def writeEdgesFooter(writer)
    def writeGraphEnd(writer)
    def writeFooter(writer)     
}

class GraphMLTransform implements TxaGraphTransform {

    def nodeShape(name){
        def shape  
        switch(name.toUpperCase().take(5)){
          case "BROWSE":
           shape = "rectangle"
           break
          case "SELPR":
           shape = "roundedrectangle"
           break
          case"PRINT":
           shape ="circle"
           break
          default:
           shape = "circle"
         }
         shape
    } 

    def writeHeader(writer){
        writer << '''\
<?xml version="1.0" encoding="UTF-8"?>
<graphml xmlns="http://graphml.graphdrawing.org/xmlns"  
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:y="http://www.yworks.com/xml/graphml"
    xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns
    http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd
    http://www.yworks.com/xml/schema/graphml/1.0/ygraphml.xsd">
''' 
    }

    def writeGraphStart(writer){
        writer << """\
    <graph id='historie' edgedefault='directed'>
      <key id='d0' for='node' yfiles.type='nodegraphics'/>
      <key id='d1' for='node' attr.name='name' attr.type='string'/>
    """
    }

    def writeNodesHeader(writer){}

    def writeNode(writer, nodeId, nodeLabel){
      writer << """
      <node id='${nodeId}'>
        <data key='d0'>
            <y:ShapeNode>
                <y:NodeLabel>${nodeLabel}</y:NodeLabel>
                <y:Shape>${nodeShape(nodeLabel)}</y:Shape>
            </y:ShapeNode>
        </data>
        <data key='d1'>${nodeLabel}</data>
      </node>
      """
    }

    def writeNodesFooter(writer){}

    def writeEdgesHeader(writer){}

    def writeEdge(writer, edgeId, sourceId, targetId){
        writer << """
           <edge id='${edgeId++}' directed='true' source='${sourceId}' target='${targetId}' />
         """
    }

    def writeEdgesFooter(writer){}

    def writeGraphEnd(writer){
        writer.write("</graph>")
    }

    def writeFooter(writer){
        writer << "</graphml>"
    }
}

def runTransform(txaFile,outputFile, TxaGraphTransform t) {

    def scanner = new ProcedureDependencyScanner()
    
    new StreamingTxaReader()
            .withHandler(scanner)
            .parse(txaFile)
    
    def nodesWritten = 0
    def edgesWritten = 0
    
    outputFile.withWriter('UTF-8') { writer ->
        t.writeHeader(writer)
        t.writeGraphStart(writer)
        
        t.writeNodesHeader(writer)
        scanner.dependencies.eachWithIndex {  procedureName, _, idx ->
            t.writeNode(writer, idx, procedureName)
            nodesWritten++
        }    
        t.writeNodesFooter(writer)
    
        t.writeEdgesHeader(writer)
        
        def edgeId = 0
    
        scanner.dependencies.eachWithIndex { _, dependentProcedures, sourceIdx ->
            dependentProcedures.each { dependentProcedure ->
               def targetIdx = scanner.dependencies.findIndexOf{it.key == dependentProcedure}
               t.writeEdge(writer, edgeId++, sourceIdx, targetIdx)
               edgesWritten++
            }
        }
    
       t.writeEdgesFooter(writer)
    
       t.writeGraphEnd(writer)
       t.writeFooter(writer)
       
       return new Tuple(nodesWritten, edgesWritten)
    }
}

def txaFile = new File("/Volumes/SSD2/Workspaces/clients/Practicom/data/invervo10.txa")
def graphmlFile =  new File("/Volumes/SSD2/Workspaces/clients/Practicom/data/invervo10.graphml")
(nodes, edges) = runTransform(txaFile, graphmlFile, new GraphMLTransform())
println("Wrote ${nodes} nodes and ${edges} edges to ${graphmlFile.toString()}")