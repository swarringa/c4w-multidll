package nl.practicom.c4w.multidll

import groovy.xml.MarkupBuilder
import groovy.xml.StreamingMarkupBuilder
import org.codehaus.groovy.runtime.StringBufferWriter

class ClarionProjectTransformerTest extends GroovyTestCase {

    void testProjectGuidIsReplaced(){
        def cwproj = new StreamingMarkupBuilder().bind {
            Project(DefaultTargets: "Build", 'xmlns' : "http://schemas.microsoft.com/developer/msbuild/2003") {
                PropertyGroup() {
                    ProjectGuid("{AE22DA0B-1921-4863-878F-D425ABC8B621}")
                }
            }
        }

        def writer = new StringBufferWriter(''<<'')
        def options = new ProjectTransformOptions(
                assemblyName: 'mymainapp',
                outputName: 'mymainapp1',
                applicationType: ApplicationType.MainApplication
        )

        new ClarionProjectTransformer(options).convert(cwproj.toString(), writer)
        def xmlout = new XmlSlurper(false,false).parse(new StringReader(writer.toString()))
        def guid = xmlout.PropertyGroup.first().ProjectGuid.text()
        assert guid ==~ /\{[A-Z0-9]{8}-([A-Z0-9]{4}-){3}[A-Z0-9]{12}\}/
        assert guid != "{AE22DA0B-1921-4863-878F-D425ABC8B621}"
    }

    void testCompilationSymbolsSetToDllMode(){
        def compileSymbols = [
            IPDRV : 1,
            _CCLSDllMode_ : 0,
            _CCLSLinkMode_ : 1,
            _IGDLL_ : 1,
            FM2 : 1,
            HyperActiveLinkMode : 0,
            HyperActiveDllMode : 1
        ]

        def compileSymbolsExpected = [
            IPDRV : 1,
            _CCLSDllMode_ : 1,
            _CCLSLinkMode_ : 0,
            _IGDLL_ : 1,
            FM2 : 1,
            HyperActiveLinkMode : 0,
            HyperActiveDllMode : 1
        ]

        def compileSymbolsText = compileSymbols.inject(
                [],
                {accu, symbol ->
                    accu << "${symbol.key}=>${symbol.value}"
                }
            ).join(',')

        def cwproj = new StreamingMarkupBuilder().bind {
            Project(DefaultTargets: "Build", 'xmlns' : "http://schemas.microsoft.com/developer/msbuild/2003") {
                PropertyGroup() {
                    DefineConstants(compileSymbolsText)
                }
            }
        }

        def writer = new StringBufferWriter(''<<'')
        def options = new ProjectTransformOptions(
                assemblyName: 'mymainapp',
                outputName: 'mymainapp1',
                applicationType: ApplicationType.MainApplication
        )

        new ClarionProjectTransformer(options).convert(cwproj.toString(), writer)
        def xmlout = new XmlSlurper(false,false).parse(new StringReader(writer.toString()))

        def updatedSymbolsText = xmlout.PropertyGroup.DefineConstants.first().text()
        def updatedSymbols = updatedSymbolsText.split(',').inject(
                [:],
                {symbols, symbol ->
                    def (key, value) = symbol.split('=>')
                    symbols.putAll([(key): value as Integer])
                    return symbols
                })

        assert updatedSymbols == compileSymbolsExpected
    }

    void testGeneratedResourcesReplacedBySingleSource(){
        def cwproj = new StreamingMarkupBuilder().bind {
            Project(DefaultTargets: "Build", 'xmlns' : "http://schemas.microsoft.com/developer/msbuild/2003") {
                ItemGroup() {
                    Compile(Include: "historie10.clw"){
                        Generated("true")
                    }
                }
            }
        }
        def writer = new StringBufferWriter(''<<'')
        def options = new ProjectTransformOptions(
                assemblyName: 'mymainapp',
                outputName: 'mymainapp1',
                applicationType: ApplicationType.MainApplication
        )
        new ClarionProjectTransformer(options).convert(cwproj.toString(), writer)
        def xmlout = new XmlSlurper(false,false).parse(new StringReader(writer.toString()))
        assert xmlout.ItemGroup.size() == 1
        def generatedSources = xmlout.ItemGroup.Compile
        assert generatedSources.size() == 1
    }
}
