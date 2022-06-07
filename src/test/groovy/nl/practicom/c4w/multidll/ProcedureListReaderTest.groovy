package nl.intreq.c4w.multidll

import nl.intreq.c4w.multidll.transforms.procedure.ProcedureListReader

class ProcedureListReaderTest extends GroovyTestCase {

    void testEmptyList(){
        def procedureList = ''
        def plr = new ProcedureListReader()
        plr.read(procedureList)
        assert plr.publicProcedures == []
        assert plr.privateProcedures == []
        assert plr.externalProcedures == []
    }

    void testCorrectVisibility(){
        def procedureList = '''
        ImplicitPublicProcedure
        +ExplicitPublicProcedure
        -PrivateProcedure
        @ExternalProcedure
        '''
        def plr = new ProcedureListReader()
        plr.read(procedureList)
        assert plr.publicProcedures == ['ImplicitPublicProcedure','ExplicitPublicProcedure']
        assert plr.privateProcedures == ['PrivateProcedure']
        assert plr.externalProcedures == ['ExternalProcedure']
    }

    void testEmptySpaceIsIgnored(){
        def procedureList = '''

    + ExplicitPublicProcedure
    
        -    PrivateProcedure

@ ExternalProcedure

        '''
        def plr = new ProcedureListReader()
        plr.read(procedureList)
        assert plr.publicProcedures == ['ExplicitPublicProcedure']
        assert plr.privateProcedures == ['PrivateProcedure']
        assert plr.externalProcedures == ['ExternalProcedure']
    }

    void testAccessModifierWithoutProcedureNameIsIgnored(){
        def procedureList = '''
         @
         +
         -
        '''
        def plr = new ProcedureListReader()
        plr.read(procedureList)
        assert plr.publicProcedures == []
        assert plr.privateProcedures == []
        assert plr.externalProcedures == []
    }
}
