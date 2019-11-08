package nl.practicom.c4w.multidll

import java.nio.file.AccessDeniedException

class ProcedureListReader {
    private final static ACCESS_MODIFIERS = ['+','-','@']

    def publicProcedures = []
    def privateProcedures = []
    def externalProcedures = []

    def read(String s) {
        readList(new StringReader(s))
    }

    def read(File listFile){
        if (!listFile.exists()){
            throw new Exception("File not found: ${listFile.path}")
        }
        if (!listFile.isFile()){
            throw new Exception("File is not a file: ${listFile.path}")
        }
        if (!listFile.canRead()){
            throw new AccessDeniedException(listFile)
        }
        readList(new FileReader(listFile))
    }

    private readList(Reader r){
        r.eachLine { line ->
            // Remove all spacing
            def procedureName = line.trim().replace(' ','')

            if (procedureName.length() > 0) {

                def accessModifier = '+'
                if (procedureName[0] in ACCESS_MODIFIERS) {
                    accessModifier = procedureName[0]
                    procedureName = procedureName.replaceFirst(~/[${ACCESS_MODIFIERS.join('')}]/,'')
                }

                if (procedureName.length() > 0) {
                    switch (accessModifier) {
                        case '+':
                            publicProcedures << procedureName
                            break
                        case '-':
                            privateProcedures << procedureName
                            break
                        case '@':
                            externalProcedures << procedureName
                            break
                        default:
                            publicProcedures << procedureName
                    }
                }
            }
        }
    }
}
