import java.nio.file.Paths
import java.nio.file.Path
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

def bind(line, bindings) {
    bindings.inject(line){ s, placeholder, replacement -> 
          s = s.replaceAll(placeholder, replacement)
    }
}

def currentClarionDate() {
    def clarionDateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    def clarionTimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss")
    def timestamp = LocalDateTime.now()
    return "'${timestamp.format(clarionDateFormat)}' ' ${timestamp.format(clarionTimeFormat)}'"
}
    
def generateDllProject(projectName, projectTitle, Path targetFolder){ 
    def excluded = ['.DS_Store']
    def currentDir = Paths.get('.')
    def templateDir= currentDir.resolve('../src/main/resources/dll_template').normalize()
    def templateSources = templateDir.resolve('src')
    def templateLibs = templateDir.resolve('lib')
    def templateData = templateDir.resolve('data')
    def templateBin = templateDir.resolve('bin')
    
    if ( !new File(targetFolder.toString()).mkdirs() ){
        targetFolder.deleteDir()
        new File(targetFolder.toString()).mkdirs()
    }
    
    def bindings = [
      __projectname__: projectName,
      __PROJECTNAME__: projectName.toUpperCase(),
      __projecttitle__: projectTitle,
      __datemodified__: currentClarionDate()
    ]
    
    templateSources.eachFile { sourceFile ->
      generateFile(sourceFile, targetFolder, bindings)
    }
    
    copyFiles(templateLibs, targetFolder, bindings)
    copyFiles(templateData, targetFolder, bindings)
    copyFiles(templateBin, targetFolder, bindings)
}

def copyFiles(Path sourceFolder, Path destinationFolder, bindings){
    sourceFolder.eachFile { sourceFile ->
        def targetFileName = bind(sourceFile.getFileName().toString(), bindings)
        def targetFile = destinationFolder.resolve(targetFileName)
        println targetFile
        Files.copy(sourceFile,targetFile, StandardCopyOption.REPLACE_EXISTING)
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

generateDllProject('histdll01', 'historie omzet', Paths.get('E:/Develop/Clarion10.app/Ontwikkelaars/Stefan/MultiDLL/dllgen/histdll01'))