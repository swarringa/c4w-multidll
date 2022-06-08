package nl.intreq.c4w.multidll.scanners

import nl.intreq.c4w.txa.transform.*

import static nl.intreq.c4w.txa.transform.SectionMark.*

class ProcedureDependencyScanner implements TxaContentHandler, TxaSectionHandler, TxaLogicalContentHandler {

    def dependencies = [:]

    boolean withinProcedurePrompt = false

    @Override
    void onProcessingStart(TxaContext context) {}

    @Override
    void onSectionStart(TxaContext context, SectionMark section) {}


    @Override
    void onSectionContent(TxaContext context, SectionMark section, Long lineNo, String content) {
        final additionProcedurePattern = ~/^%\w+\s+PROCEDURE\s+\((\w+)\)/
        final simpleProcedurePromptPattern = ~/^%\w+\s+PROCEDURE\s+\((\w+)\)\s*$/
        final dependentProcedurePromptPattern = ~/^%\w+\sDEPEND.*PROCEDURE\s.*TIMES\s([0-9]+)\s*$/
        final promptWhenPattern = /^WHEN\s+\((.*)\)\s+\((\w*)\)\s*$/
        final procedureCallPattern = /^\s*(\w+)\(\)\s*$/

        try {
            if (context.within(CALLS)) {
                addOrUpdateDependency(context.currentProcedureName, content)
            }

            if (context.within(ADDITION) && content ==~ additionProcedurePattern) {
                (content =~ additionProcedurePattern).each {
                    _, proc -> addOrUpdateDependency(context.currentProcedureName, proc)
                }
            }

            if ( context.within(DEFINITION, PROCEDURE) && content ==~ procedureCallPattern){
                (content =~ procedureCallPattern ).each {
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

    @Override
    void onSectionEnd(TxaContext context, SectionMark section) {
        if ( section == PROCEDURE && !context.within(DEFINITION) && context.currentProcedureName){
            if (!dependencies.containsKey(context.currentProcedureName)){
                dependencies.put(context.currentProcedureName,[])
            }
        }
    }

    void onProcessingFinished(TxaContext context) {}


    def addOrUpdateDependency(String parent, String child) {
        if ( parent == null) {
            parent = "APPLICATION"
        }

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

    /**
     * Collect transitive dependencies for a procedure
     * @param procedure name of the procedure
     * @param level maximum depth to descend into the dependency hierarchy
     * @return List of procedures names on which the procedure is dependent
     */
    List<String> getTransitiveDependencies(String procedure, int level = -1) {
        return getTransitiveDependencies([procedure],level)
    }

    /**
     * Collect transitive dependencies for a set of procedures
     * @param procedures list of procedures names
     * @param level maximum depth to descend into the dependency hierarchy
     * @return List of procedures names on which the procedures are dependent
     */
    List<String> getTransitiveDependencies(List<String> procedures, int level = -1) {
        Set<String> result = procedures.inject([] as Set) {
            collected, procedure ->
                collected.addAll(dependencies[procedure] ?: [])
                collected
        }
        Set<String> candidates = []
        candidates.addAll(result)
        candidates.each { p ->
            result.addAll(addTransitiveDependencies(result, p, level) ?: [])
        }
        return result.toList()
    }


    private Set<String> addTransitiveDependencies(final Set<String> collected, String procedure, int level){
        if ( level == 0) return []

        Set<String> result  = []
        result.addAll(collected ?: [])
        Set<String> candidates = ( dependencies[procedure] ?: [] ) as Set

        candidates.removeAll(collected)
        if ( !candidates.isEmpty() ){
            collected.addAll(candidates)
            candidates.each { p ->
                result.addAll(addTransitiveDependencies(collected, p, level-1) ?: [] )
            }
        }

        result
    }

}
