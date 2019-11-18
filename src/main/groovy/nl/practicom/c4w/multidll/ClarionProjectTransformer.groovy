package nl.practicom.c4w.multidll

import groovy.util.slurpersupport.GPathResult
import groovy.xml.XmlUtil

class ClarionProjectTransformer {
    Writer out
    ProjectTransformOptions options

    ClarionProjectTransformer(ProjectTransformOptions options) {
        this.out = out
        this.options = options
    }

    def convert(InputStream projectFile, Writer out){
        convert(new InputStreamReader(projectFile),out)
    }

    def convert(String input, Writer out){
        convert(new StringReader(input),out)
    }

    def convert(Reader input, Writer writer){
        def root = new XmlSlurper(false,false).parse(input).declareNamespace('':"http://schemas.microsoft.com/developer/msbuild/2003")

        if ( !root.PropertyGroup?.isEmpty()) {
            generateProjectGuid(root)
            transformCompilationSymbols(root)
            transformApplicationName(root)
            transformApplicationModel(root)
        }

        transformSourceGeneration(root)

        XmlUtil.serialize(root, writer)
    }

    def generateProjectGuid(GPathResult root) {
        root.PropertyGroup.ProjectGuid = '{' + UUID.randomUUID().toString().toUpperCase() + '}'
    }

    def transformApplicationName(GPathResult root) {
        root.PropertyGroup.AssemblyName = options.assemblyName
        root.PropertyGroup.OutputName = options.outputName
    }

    def transformApplicationModel(GPathResult root) {
        if (options.applicationType == ApplicationType.MainApplication) {
            root.PropertyGroup.Model = "Exe"
        } else {
            root.PropertyGroup.Model = "Dll"
        }
    }

    def transformCompilationSymbols(GPathResult root) {
        if (!root.PropertyGroup.DefineConstants.isEmpty()) {
            def compileSymbols = root.PropertyGroup.DefineConstants.toString().split(",")
            def updatedCompileSymbols = compileSymbols.inject(
                    [],
                    { accu, symbolExpr ->
                        def (key, value) = symbolExpr.split('=>')
                        if (key.toLowerCase().contains('linkmode')) {
                            value = 0
                        }
                        if (key.toLowerCase().contains('dllmode')) {
                            value = 1
                        }
                        accu << "${key}=>${value}"
                        return accu
                    }
            ).join(',')

            root.PropertyGroup.DefineConstants = updatedCompileSymbols
        }
    }

    def transformSourceGeneration(GPathResult root) {
        if (!root.ItemGroup.isEmpty()) {
            root.ItemGroup.Compile.replaceNode {}
            root.ItemGroup.first().appendNode {
                Compile(Include: "${options.assemblyName}.clw") {
                    Generated(true)
                }
            }
        }
    }

}
