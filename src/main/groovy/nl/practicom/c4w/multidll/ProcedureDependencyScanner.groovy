package nl.practicom.c4w.multidll

import nl.practicom.c4w.txa.parser.SectionMark

import java.util.regex.Matcher

import static nl.practicom.c4w.txa.parser.SectionMark.*
import nl.practicom.c4w.txa.transform.TxaContentHandler
import nl.practicom.c4w.txa.transform.TxaContext

class ProcedureDependencyScanner implements TxaContentHandler {

    def dependencies = [:]

    boolean withinProcedurePrompt = false

    @Override
    void onProcessingStart(TxaContext context) {

    }

    @Override
    void onSectionStart(TxaContext context, SectionMark section) {

    }

    @Override
    void onSectionContent(TxaContext context, SectionMark section, String content) {
        final additionProcedurePattern = ~/^%\w+\s+PROCEDURE\s+\((\w+)\)/
        final simpleProcedurePromptPattern = ~/^%\w+\s+PROCEDURE\s+\((\w+)\)\s*$/
        final dependentProcedurePromptPattern = ~/^%\w+\sDEPEND.*PROCEDURE\s.*TIMES\s([0-9]+)\s*$/
        final promptWhenPattern = /^WHEN\s+\((.*)\)\s+\((\w*)\)\s*$/

        if ( context.currentProcedureName) {
            try {
                if (context.within(CALLS)) {
                    addOrUpdateDependency(context.currentProcedureName, content)
                }

                if (context.within(ADDITION) && content ==~ additionProcedurePattern) {
                    (content =~ additionProcedurePattern).each {
                        _, proc -> addOrUpdateDependency(context.currentProcedureName, proc)
                    }
                }

                if (context.within(PROMPTS)) {
                    if ( content ==~ simpleProcedurePromptPattern){
                        (content =~ simpleProcedurePromptPattern).each {
                            _, proc -> addOrUpdateDependency(context.currentProcedureName, proc)
                        }
                    } else if (content ==~ dependentProcedurePromptPattern) {
                        (content =~ dependentProcedurePromptPattern).each {
                             _, times -> withinProcedurePrompt = (times as int) > 0 ? true : false
                        }
                    } else if (content.trim().isEmpty()) {
                        withinProcedurePrompt = false
                    } else if (withinProcedurePrompt && content ==~ promptWhenPattern) {
                        (content =~ promptWhenPattern).each {
                            _, key, val -> addOrUpdateDependency(context.currentProcedureName, val)
                        }
                    }
                }
            } catch ( Exception x){
                println "Exception ${x.getMessage()} processing line ${context.getCurrentLineNumber()}:"
                println context.getCurrentLine()
            }
        }
    }

    @Override
    void onSectionEnd(TxaContext context, SectionMark section) {
        if ( section == PROCEDURE && !context.within(DEFINITION) && context.currentProcedureName){
            if (!dependencies.containsKey(context.currentProcedureName)){
                dependencies.put(context.currentProcedureName,[])
            }
        }
    }

    void onProcessingFinished(TxaContext context) {

    }

    def addOrUpdateDependency(String parent, String child) {
        if ( isProcedureName(parent) && isProcedureName(child)) {
            if (!dependencies.containsKey(parent)) {
                dependencies.put(parent, [])
            }
            if (!dependencies[parent].contains(child)) {
                dependencies[parent] << child
            }
        }
    }

    def isProcedureName = {s -> s != null && s.trim().size() > 0}

}
