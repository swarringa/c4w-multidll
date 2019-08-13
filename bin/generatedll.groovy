import java.nio.file.Paths
import java.nio.file.Path
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.io.File

def projectName = "histomzet"

def bind(line, bindings) {

    bindings.inject(line){ s, placeholder, replacement -> 
          s = s.replaceAll(placeholder, replacement)
    }
}

def generateDllProject(projectName, projectTitle, Path targetFolder){ 
    def excluded = ['.DS_Store']
    def currentDir = Paths.get('.')
    def templateDir= currentDir.resolve('../src/main/resources/dll_template').normalize()
    def templateSources = templateDir.resolve('src')
    def templateLibs = templateDir.resolve('lib')
    def templateData = templateDir.resolve('data')
    
    if ( !new File(targetFolder.toString()).mkdirs() ){
        targetFolder.deleteDir()
        new File(targetFolder.toString()).mkdirs()
    }
     
    def bindings = [
      __projectname__: projectName,
      __PROJECTNAME__: projectName.toUpperCase(),
      __projecttitle__: projectTitle
    ]
    
    templateSources.eachFile { sourceFile ->
      generateFile(sourceFile, targetFolder, bindings)
    }
    
    templateLibs.eachFile { sourceFile ->
        def targetFile = targetFolder.resolve(sourceFile.getFileName())
        println targetFile
        Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING)
    }
    
    templateData.eachFile { sourceFile ->
        def targetFile = targetFolder.resolve(sourceFile.getFileName())
        println targetFile
        Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING)
    }
}

def generateFile(Path inputFile, Path targetFolder, Map<String,String> bindings){
    def outputFile = bind(targetFolder.resolve(inputFile.getFileName()).toString(), bindings)
   
    def reader = new FileReader(inputFile.toString())
    def writer = new FileWriter(outputFile)
    println outputFile
    reader.transformLine(writer){ line ->
        bind(line, bindings)
    }
}

generateDllProject('histomzet', 'historie omzet', Paths.get('/Volumes/Projects/Clients/Udea/tmp/histomzet'))
