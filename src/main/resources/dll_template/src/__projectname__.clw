   PROGRAM


StringTheory:TemplateVersion equate('3.07')
xFiles:TemplateVersion equate('3.11')
PC:DebugLevel        LONG

MemoryAddr           ULONG
StringRef            &CSTRING

WTSCurrServer        LONG(0h)
WTSCurrSession       LONG(-1h)
WTSClientAddress     LONG(14)
WTSClientName        LONG(10)
PC:Result            ULONG
PC:ClientName        CSTRING(255)
PC:Local             ULONG(0)
PC:Length            ULONG(255)
PC:INIfile           CSTRING(1024)
PC:WinDir            CSTRING(1024)
PC:TraceTime         LONG

WinEvent:TemplateVersion      equate('5.26')

   INCLUDE('ABERROR.INC'),ONCE
   INCLUDE('ABFILE.INC'),ONCE
   INCLUDE('ABUTIL.INC'),ONCE
   INCLUDE('ERRORS.CLW'),ONCE
   INCLUDE('KEYCODES.CLW'),ONCE
   INCLUDE('ABFUZZY.INC'),ONCE
   INCLUDE('BARCODE.INC'),ONCE
  include('StringTheory.Inc'),ONCE
   include('xfiles.inc'),ONCE
   INCLUDE('pdfrpt41.inc'),ONCE
    Include('WinEvent.Inc'),Once
 INCLUDE('EXLCLASS.INC'),ONCE

   MAP
!--- Application Global and Exported Procedure Definitions --------------------------------------------
         include('PC_map.clw')
    ! Declare functions defined in this DLL
__projectname__:Init     PROCEDURE(<ErrorClass curGlobalErrors>, <INIClass curINIMgr>)
__projectname__:Kill     PROCEDURE
    ! Declare init functions defined in a different dll
     MODULE('UDEADCT.DLL')
UDEADCT:Init           PROCEDURE(<ErrorClass curGlobalErrors>, <INIClass curINIMgr>)
UDEADCT:Kill           PROCEDURE
     END
   END

  include('StringTheory.Inc'),ONCE
GloFlag              STRING(1),EXTERNAL,DLL(dll_mode)
GLO:PosSchuif1       LONG,EXTERNAL,DLL(dll_mode)
GloWijzigenVerkoper  SHORT,EXTERNAL,DLL(dll_mode)
GloStatus            STRING(30),EXTERNAL,DLL(dll_mode)
GloSoortActie        STRING(20),EXTERNAL,DLL(dll_mode)
GloSoortDatum        STRING(20),EXTERNAL,DLL(dll_mode)
GloKeuze             STRING(3),EXTERNAL,DLL(dll_mode)
GloOrdernummer       DECIMAL(6,2),EXTERNAL,DLL(dll_mode)
GloGroepNummer       DECIMAL(17,2),EXTERNAL,DLL(dll_mode)
GloPakbonKey         STRING(20),EXTERNAL,DLL(dll_mode)
GloOpmerking         STRING(255),EXTERNAL,DLL(dll_mode)
GloFactuurnummer     DECIMAL(7,2),EXTERNAL,DLL(dll_mode)
GloLaatsteFactuurnummer DECIMAL(7,2),EXTERNAL,DLL(dll_mode)
GloFactuurdatum      DATE,EXTERNAL,DLL(dll_mode)
GloFilterSoort       STRING(40),EXTERNAL,DLL(dll_mode)
GloPrintDebiteuren   STRING(50),EXTERNAL,DLL(dll_mode)
GloPrintCrediteuren  STRING(50),EXTERNAL,DLL(dll_mode)
VanDebiteurnummer    DECIMAL(8,2),EXTERNAL,DLL(dll_mode)
TotDebiteurnummer    DECIMAL(8,2),EXTERNAL,DLL(dll_mode)
VanCrediteurnummer   DECIMAL(6,2),EXTERNAL,DLL(dll_mode)
TotCrediteurnummer   DECIMAL(6,2),EXTERNAL,DLL(dll_mode)
VanZoekcode          STRING(8),EXTERNAL,DLL(dll_mode)
TotZoekcode          STRING(8),EXTERNAL,DLL(dll_mode)
VanPostcode          STRING(10),EXTERNAL,DLL(dll_mode)
TotPostcode          STRING(10),EXTERNAL,DLL(dll_mode)
VanPlaats            STRING(40),EXTERNAL,DLL(dll_mode)
TotPlaats            STRING(40),EXTERNAL,DLL(dll_mode)
VanHoofdroute        DECIMAL(4,2),EXTERNAL,DLL(dll_mode)
TotHoofdroute        DECIMAL(4,2),EXTERNAL,DLL(dll_mode)
DatumLeveren         DATE,EXTERNAL,DLL(dll_mode)
GloUitleverdatum     DATE,EXTERNAL,DLL(dll_mode)
GloCrediteurnummer   DECIMAL(6,2),EXTERNAL,DLL(dll_mode)
GloDebiteurnummer    DECIMAL(8,2),EXTERNAL,DLL(dll_mode)
GloInkoper           STRING(5),EXTERNAL,DLL(dll_mode)
GloDatumLeveren      DATE,EXTERNAL,DLL(dll_mode)
GloDatumInkoop       DATE,EXTERNAL,DLL(dll_mode)
GloProductgroep      DECIMAL(8,2),EXTERNAL,DLL(dll_mode)
GloProductnummer     DECIMAL(8,2),EXTERNAL,DLL(dll_mode)
GloOrigineelProductgroep DECIMAL(8,2),EXTERNAL,DLL(dll_mode)
GloOrigineelProductnummer DECIMAL(8,2),EXTERNAL,DLL(dll_mode)
GloNieuwProductgroep DECIMAL(8,2),EXTERNAL,DLL(dll_mode)
GloNieuwProductnummer DECIMAL(8,2),EXTERNAL,DLL(dll_mode)
GloAfdelingscode     DECIMAL(4,2),EXTERNAL,DLL(dll_mode)
GloKopie             STRING(8),EXTERNAL,DLL(dll_mode)
GloVanProductnummer  DECIMAL(8,2),EXTERNAL,DLL(dll_mode)
GloTotProductnummer  DECIMAL(8,2),EXTERNAL,DLL(dll_mode)
GloVanProductgroep   DECIMAL(8,2),EXTERNAL,DLL(dll_mode)
GloTotProductgroep   DECIMAL(8,2),EXTERNAL,DLL(dll_mode)
GloVerwerkDatum      STRING(8),EXTERNAL,DLL(dll_mode)
GloPeriode           STRING(2),EXTERNAL,DLL(dll_mode)
GloBoekjaar          STRING(4),EXTERNAL,DLL(dll_mode)
GloVerwDatumBank     STRING(6),EXTERNAL,DLL(dll_mode)
GloVerwDatumGiro     STRING(6),EXTERNAL,DLL(dll_mode)
GloVerwerkDatum0Dagen STRING(6),EXTERNAL,DLL(dll_mode)
GloVerwerkDatum7Dagen STRING(6),EXTERNAL,DLL(dll_mode)
GloVerwerkDatum14Dagen STRING(6),EXTERNAL,DLL(dll_mode)
GloVerwerkDatum21Dagen STRING(6),EXTERNAL,DLL(dll_mode)
GloVerwerkDatum0DagenDate DATE,EXTERNAL,DLL(dll_mode)
GloVerwerkDatum7DagenDate DATE,EXTERNAL,DLL(dll_mode)
GloVerwerkDatum14DagenDate DATE,EXTERNAL,DLL(dll_mode)
GloVerwerkDatum21DagenDate STRING(20),EXTERNAL,DLL(dll_mode)
GloStaffel           LONG,EXTERNAL,DLL(dll_mode)
GloStaffelKortingSoort STRING(20),EXTERNAL,DLL(dll_mode)
GloStaffelKortingPercentage DECIMAL(6,2),EXTERNAL,DLL(dll_mode)
GloStaffelKortingBedrag DECIMAL(6,2),EXTERNAL,DLL(dll_mode)
GloHoofdgroep        DECIMAL(4,2),EXTERNAL,DLL(dll_mode)
GloSoortKlacht       STRING(40),EXTERNAL,DLL(dll_mode)
GloVanDatum          DATE,EXTERNAL,DLL(dll_mode)
GloTotDatum          DATE,EXTERNAL,DLL(dll_mode)
GloMedewerkersCode   STRING(5),EXTERNAL,DLL(dll_mode)
GloVanMedewerkerscode STRING(5),EXTERNAL,DLL(dll_mode)
GloTotMedewerkerscode STRING(5),EXTERNAL,DLL(dll_mode)
GloDag               STRING(15),EXTERNAL,DLL(dll_mode)
GloWeek              SHORT,EXTERNAL,DLL(dll_mode)
GloLandcode          STRING(3),EXTERNAL,DLL(dll_mode)
GloWijzigen          STRING(1),EXTERNAL,DLL(dll_mode)
GloVanWeek           SHORT(1),EXTERNAL,DLL(dll_mode)
GloTotWeek           SHORT,EXTERNAL,DLL(dll_mode)
GloHoofdroute        DECIMAL(4,2),EXTERNAL,DLL(dll_mode)
GloVanMagazijnroute  LONG,EXTERNAL,DLL(dll_mode)
GloTotMagazijnroute  LONG,EXTERNAL,DLL(dll_mode)
GloLabelType         STRING(25),EXTERNAL,DLL(dll_mode)
GloTransporteur      DECIMAL(6,2),EXTERNAL,DLL(dll_mode)
GloDatumOphalen      DATE,EXTERNAL,DLL(dll_mode)
GloTijdOphalen       TIME,EXTERNAL,DLL(dll_mode)
GloInkoopOpmerking1  STRING(500),EXTERNAL,DLL(dll_mode)
GloInkoopOpmerking2  STRING(500),EXTERNAL,DLL(dll_mode)
GloMedewerkerscodeAanmelden STRING(5),EXTERNAL,DLL(dll_mode)
GloWachtwoord        STRING(10),EXTERNAL,DLL(dll_mode)
GloAanmelden         STRING(20),EXTERNAL,DLL(dll_mode)
GloMagazijnroute     LONG,EXTERNAL,DLL(dll_mode)
GloConsumentenStaffel STRING(20),EXTERNAL,DLL(dll_mode)
GloAantal            DECIMAL(7,2,1),EXTERNAL,DLL(dll_mode)
GloOpslagAanbod      DECIMAL(5,2),EXTERNAL,DLL(dll_mode)
GloModemNummer       DECIMAL(4,2),EXTERNAL,DLL(dll_mode)
GloProcedure         STRING(50),EXTERNAL,DLL(dll_mode)
GloCradlenummer      LONG,EXTERNAL,DLL(dll_mode)
GLO:VanProductcategorie DECIMAL(4,2),EXTERNAL,DLL(dll_mode)
GLO:TmProductcategorie DECIMAL(4,2),EXTERNAL,DLL(dll_mode)
GLO:MailSoort        STRING(50),EXTERNAL,DLL(dll_mode)
GLO:NoEmail          BYTE,EXTERNAL,DLL(dll_mode)
GLO:NoPreview        BYTE,EXTERNAL,DLL(dll_mode)
GLO:RandNo           BYTE,EXTERNAL,DLL(dll_mode)
GLO:RTFname          STRING('Report {44}'),EXTERNAL,DLL(dll_mode)
GLO:RTFpath          STRING(200),EXTERNAL,DLL(dll_mode)
GLO:MailTo           STRING(255),EXTERNAL,DLL(dll_mode)
GLO:MailCC           STRING(255),EXTERNAL,DLL(dll_mode)
GLO:MailBCC          STRING(255),EXTERNAL,DLL(dll_mode)
GLO:Subject          STRING(255),EXTERNAL,DLL(dll_mode)
GLO:RTFleft          LONG(500),EXTERNAL,DLL(dll_mode)
GLO:RTFtop           LONG(500),EXTERNAL,DLL(dll_mode)
Glo:Thread           LONG,EXTERNAL,DLL(dll_mode)
GloMagazijnpicker    STRING(5),EXTERNAL,DLL(dll_mode)
GloMagazijnpickerBatch STRING(5),EXTERNAL,DLL(dll_mode)
GloMagazijnpickerAparteOrder STRING(5),EXTERNAL,DLL(dll_mode)
GloVastePrinter      CSTRING(256),EXTERNAL,DLL(dll_mode)
GloSoortPrint        SHORT,EXTERNAL,DLL(dll_mode)
GloCBLafdeling       LONG,EXTERNAL,DLL(dll_mode)
GloCBLhoofdgroep     LONG,EXTERNAL,DLL(dll_mode)
GLO:Bestandsnaam     CSTRING(256),EXTERNAL,DLL(dll_mode)
GLO:BestandsCopy     CSTRING(256),EXTERNAL,DLL(dll_mode)
GLO:GebruikersValutaProduct STRING(3),EXTERNAL,DLL(dll_mode)
GLO:GebruikersDebOmzetScherm STRING(3),EXTERNAL,DLL(dll_mode)
GLO:VBAF::ViewMode   BYTE,EXTERNAL,DLL(dll_mode),THREAD
GLO:RetourView       BYTE,EXTERNAL,DLL(dll_mode)
GLO:StatusPrintMedewerker STRING(15),EXTERNAL,DLL(dll_mode)
GLO:SortPrintMedewerker STRING(20),EXTERNAL,DLL(dll_mode)
GLO:AGFMedewerkerscode STRING(5),EXTERNAL,DLL(dll_mode)
GLO:TempBestandDiepvrieslijst CSTRING(256),EXTERNAL,DLL(dll_mode)
GLO:TempBestandChauffeurslijst CSTRING(256),EXTERNAL,DLL(dll_mode)
GLO:TempBestandChLijstOphaal CSTRING(256),EXTERNAL,DLL(dll_mode)
GLO:TempBestandKlantenDKW STRING(256),EXTERNAL,DLL(dll_mode)
GLO:TempBestandAfbeeldingen CSTRING(256),EXTERNAL,DLL(dll_mode)
GLO:CalculatieBestelnummer DECIMAL(8,2),EXTERNAL,DLL(dll_mode)
GLO:CalculatieProductnummer DECIMAL(8,2),EXTERNAL,DLL(dll_mode)
GLO:CalculatieStatus BYTE,EXTERNAL,DLL(dll_mode)
GLO:CalculatiePressOK BYTE,EXTERNAL,DLL(dll_mode)
GLO:ComputerName     STRING(255),EXTERNAL,DLL(dll_mode)
GLO:TempBestandGBVoorraad CSTRING(256),EXTERNAL,DLL(dll_mode)
GLO:ToegestaanOrderVerwijderen BYTE,EXTERNAL,DLL(dll_mode)
GLO:ToegestaanVoorraadWijzigen BYTE,EXTERNAL,DLL(dll_mode)
GLO:AfdelingMagazijnKlaarzetten DECIMAL(4,2),EXTERNAL,DLL(dll_mode)
GLO:KeuzeMagazijnpicker STRING(5),EXTERNAL,DLL(dll_mode)
GLO:KeuzeProductgroep DECIMAL(8,2),EXTERNAL,DLL(dll_mode)
GLO:FilterDebiteurnummer DECIMAL(8,2),EXTERNAL,DLL(dll_mode)
GLO:InvervoINI       STRING(255),EXTERNAL,DLL(dll_mode)
GloMagazijnAfdelingscode DECIMAL(4,2),EXTERNAL,DLL(dll_mode)
GloORPBestand        STRING(255),EXTERNAL,DLL(dll_mode)
GLO:InkoopTypeControle STRING(10),EXTERNAL,DLL(dll_mode)
GLO:MagTeller        LONG,EXTERNAL,DLL(dll_mode)
GLO:NoDuplexSkip     BYTE,EXTERNAL,DLL(dll_mode)
GLO:AutoSluitenApp   BYTE,EXTERNAL,DLL(dll_mode)
GLO:ActieAfdeling    DECIMAL(4,2),EXTERNAL,DLL(dll_mode)
GLO:Bedrijfsafdelingscode DECIMAL(4,2),EXTERNAL,DLL(dll_mode)
GLO:dbOwnerS2        CSTRING(256),EXTERNAL,DLL(dll_mode)
GLO:dbOwnerS3        CSTRING(256),EXTERNAL,DLL(dll_mode)
GLO:dbOwnerWMS       CSTRING(256),EXTERNAL,DLL(dll_mode)
GLO:dbOwner          CSTRING(256),EXTERNAL,DLL(dll_mode)
GLO:Versie           STRING(20),EXTERNAL,DLL(dll_mode)
GLO:AfdelingMagazijnConsument DECIMAL(4,2),EXTERNAL,DLL(dll_mode)
GLO:AfdelingMagazijn DECIMAL(4,2),EXTERNAL,DLL(dll_mode)
GLO:IPServer         STRING(255),EXTERNAL,DLL(dll_mode)
GLO:HyperactiveMap   STRING(255),EXTERNAL,DLL(dll_mode)
GLO:PrijslijnId      LONG,EXTERNAL,DLL(dll_mode)
GLO:DatumVan         DATE,EXTERNAL,DLL(dll_mode)
GLO:DatumTot         DATE,EXTERNAL,DLL(dll_mode)
GLO:Reset            BYTE,EXTERNAL,DLL(dll_mode)
GLO:VoorraadGelijk   CSTRING(4),EXTERNAL,DLL(dll_mode)
GLO:WMSHerkomst      CSTRING(11),EXTERNAL,DLL(dll_mode)
GLO:c2dwe            STRING(255),EXTERNAL,DLL(dll_mode)
GLO:c2ExportSlimDagafzet STRING(255),EXTERNAL,DLL(dll_mode)
GLO:c2ExportSlimPromotie STRING(255),EXTERNAL,DLL(dll_mode)
GLO:c2ExportSlimPurchase STRING(255),EXTERNAL,DLL(dll_mode)
GLO:c2ExportSlimStock STRING(255),EXTERNAL,DLL(dll_mode)
GLO:c2Bnl500         STRING(255),EXTERNAL,DLL(dll_mode)
GLO:c2intertr        STRING(255),EXTERNAL,DLL(dll_mode)
GLO:c2ORDERS         STRING(255),EXTERNAL,DLL(dll_mode)
GLO:QueueFacturen    QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
qfp_Factuurnummer      DECIMAL(7,2)
qfp_Ordernummer        DECIMAL(7,2)
qfp_Debiteurnummer     DECIMAL(8,2)
qfp_DatumOrder         DATE
qfp_Uitleverdatum      DATE
qfp_Referentie         CSTRING(51)
qfp_ReedsBetaald       DECIMAL(7,2)
qfp_BetaalMethode      STRING(15)
qfp_Bijzonderheden     CSTRING(1001)
                     END
GLO:QueueFacturenPdfEnMailen QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
QFP:Factuurnummer      DECIMAL(7,2)
QFP:PdfBestand         STRING(500)
QFP:EmailAdres         STRING(500)
QFP:Debiteurnummer     DECIMAL(8,2)
QFP:FTP                BYTE
QFP:FTPBestand         STRING(255)
QFP:FTPPad             STRING(255)
                     END
QueueAfdelingActieTonen QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
qa_Afdeling            DECIMAL(4,2)
                     END
QueueOrderApartePicker QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
qop_Ordernummer        DECIMAL(6,2)
qop_ApartePicker       STRING(5)
qop_AfwijkId           LONG
                     END
Glo:iQErrorMessageText STRING(128),EXTERNAL,DLL(dll_mode)
QueueMeldingen       QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
qmld_Type              STRING(40)
qmld_Productgroep      DECIMAL(8,2)
qmld_Productnummer     DECIMAL(8,2)
qmld_Omschrijving      STRING(255)
                     END
QueueMeldingModembestelling QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
qmod_Debiteurnummer    STRING(20)
                     END
QueueNietKlaargezet  QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
qnk_Debiteurnummer     DECIMAL(8,2)
qnk_Naam               STRING(50)
qnk_Ordernummer        DECIMAL(6,2)
qnk_Afdeling           DECIMAL(4,2)
qnk_Omschrijving       STRING(50)
                     END
QueueMailOrder       QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
qmo_Ordernummer        DECIMAL(8,2)
qmo_Debiteurnummer     DECIMAL(8,2)
qmo_DatumLeveren       DATE
qmo_Email              STRING(255)
qmo_EmailVerkoper      STRING(255)
qmo_Afzender           STRING(255)
                     END
GLO:QueueLocatieVolume QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
qlvg_Locatie           LONG
qlvg_Omschrijving      STRING(50)
qlvg_Volume            DECIMAL(12,2)
qlvg_Wachten           BYTE
qlvg_Bundelnummer      ULONG
qlvg_AantalKratten     SHORT
                     END
GLO:QueueDigLeveringsbon QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
qdlb_Verkooporder      DECIMAL(6,2)
                     END
GLO:QueueMailConsument QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
qmc_Type               BYTE
qmc_Winkel             DECIMAL(8,2)
qmc_Consument          LONG
qmc_Ordernummer        DECIMAL(7,2)
qmc_Email              STRING(255)
qmc_Afzender           STRING(255)
qmc_PdfNaam            STRING(255)
qmc_Printen            BYTE
qmc_Mailen             BYTE
qmc_Tekst              STRING(2500)
                     END
GLO:QueueMeldingVolume QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
qmv_Ordernummer        DECIMAL(7,2)
qmv_Productgroep       DECIMAL(7,2)
qmv_Omschrijving       STRING(100)
qmv_Volume             DECIMAL(7,2)
                     END
GLO:QueueConsumentPakbon QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
qcp_Type               STRING(2)
qcp_Id                 LONG
qcp_Soort              CSTRING(31)
qcp_Productgroep       DECIMAL(8,2)
qcp_Productnummer      DECIMAL(8,2)
                     END
GLO:QueueExportExcel QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
qexc_Afdeling          DECIMAL(7,2)
qexc_Bestelnummer      DECIMAL(7,2)
qexc_Productnummer     DECIMAL(7,2)
                     END
GLO:QueueExtraDebFactuurnummering QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
qedf_Debiteurnummer    DECIMAL(8,2)
qedf_Factuurnummer     DECIMAL(7,2)
                     END
GLO:QueueVrrdProductgroep QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
qvp_Productgroep       DECIMAL(8,2)
qvp_BesteldNaarVrrd    DECIMAL(10,2)
qvp_OnderwegNaarVrrd   DECIMAL(10,2)
qvp_AantalBesteld      DECIMAL(10,2)
qvp_AantalOnderweg     DECIMAL(10,2)
                     END
GLO:QueueAparteOrder QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
qao_Id                 LONG
qao_Omschrijving       STRING(100)
qao_Picker             STRING(5)
                     END
GLO:QueueDigFactuur  QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
qdf_Factuurnummer      DECIMAL(7,2)
qdf_Debiteurnummer     DECIMAL(8,2)
                     END
GLO:MemVerpakkingen  QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
mvp_Verpakkingsnummer  DECIMAL(8,2)
mvp_Statiegeld         STRING(3)
                     END
GLO:MemDebiteuren    QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
mdb_Debiteurnummer     DECIMAL(8,2)
mdb_Naam               STRING(50)
mdb_Postadres          STRING(80)
mdb_Postcode2          STRING(10)
mdb_Plaats2            STRING(40)
mdb_BtwNummer          STRING(50)
mdb_Betalingsvoorwaarde STRING(100)
mdb_Consumentenprijzen STRING(3)
mdb_EmailFactuur       STRING(254)
mdb_FactuurPdfFtp      BYTE
mdb_BetalingskortingCode STRING(3)
mdb_Landcode           STRING(3)
mdb_PrintenFactuur     BYTE(1)
mdb_DigitaleFactuur    BYTE
mdb_DagFacturen        CSTRING(4)
mdb_Taalcode           STRING(3)
mdb_OpslagOpFactuur    BYTE
mdb_OpslagOmschrijving STRING(30)
mdb_OpslagPercentage   DECIMAL(6,2)
mdb_FactuurGecomprimeerd STRING(3)
mdb_TwigEnForestLayout BYTE
                     END
GLO:MemBetalingkortingen QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
mbc_BetalingskortingCode STRING(3)
mbc_Korting            DECIMAL(5,2)
mbc_Omschrijving       STRING(100)
                     END
GLO:MemLanden        QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
mlc_Landcode           STRING(3)
mlc_Omschrijving       STRING(25)
                     END
GLO:MemProducten     QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
mpr_ProductGroep       DECIMAL(8,2)
mpr_Productnummer      DECIMAL(8,2)
mpr_Klassering         STRING(25)
mpr_KeurmerkBio        STRING(25)
mpr_KeurmerkOverig     STRING(25)
mpr_Kwaliteit          STRING(20)
                     END
GLO:MemBtwTaal       QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
mbt_BtwCode            STRING(20)
mbt_Taalcode           STRING(3)
mbt_Omschrijving       STRING(30)
                     END
GLO:MemBioKwaliteiten QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
mbk_Kwaliteit          STRING(20)
                     END
GLO:MemEenhedenTaal  QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
met_Eenheid            STRING(10)
met_Taalcode           STRING(3)
met_Omschrijving       STRING(20)
                     END
GLO:MemGrootboekrekeningen1 QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
mg1_GrootboekRekening  STRING(6)
mg1_LeveringRekening   STRING(6)
mg1_Omschrijving       STRING(25)
mg1_BioOpFactuur       BYTE
mg1_SpecificatieOpFactuur BYTE
                     END
GLO:MemGrootboekrekeningen2 QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
mg2_Grootboekrekening  STRING(6)
mg2_Omschrijving       STRING(25)
mg2_BioOpFactuur       BYTE
mg2_SpecificatieOpFactuur BYTE
                     END
GLO:MemConsumenten   QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
mco_ConsumentNummer    LONG
mco_Voornaam           STRING(255)
mco_Achternaam         STRING(255)
                     END
GLO:MemHerkomst      QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
mhe_Herkomst           STRING(20)
mhe_ISOLandcode        STRING(3)
                     END
GLO:MemDebOmzettenGrootboek QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
mdo_Debiteurnummer     DECIMAL(8,2)
mdo_Grootboekrekening  STRING(6)
mdo_Jaar               LONG
mdo_Verkoop            DECIMAL(10,2),DIM(53)
mdo_VerkoopBioKwaliteit DECIMAL(10,2),DIM(53)
                     END
GLO:QueueTripPlanningResult QUEUE,PRE(),EXTERNAL,DLL(dll_mode)
qtp_Verkooporder       DECIMAL(6,2)
                     END
PC:NewPage           LONG,EXTERNAL,DLL(dll_mode)
SilentRunning        BYTE(0)                               ! Set true when application is running in 'silent mode'

!region File Declaration
!endregion


GlobalRequest        BYTE,EXTERNAL,THREAD                  ! Exported from a library, set when a browse calls a form, to let it know action to perform
GlobalResponse       BYTE,EXTERNAL,THREAD                  ! Exported from a library, set to the response from the form
VCRRequest           LONG,EXTERNAL,THREAD                  ! Exported from a library, set to the request from the VCR buttons
FuzzyMatcher         FuzzyClass                            ! Global fuzzy matcher
LocalErrorStatus     ErrorStatusClass,THREAD
LocalErrors          ErrorClass
LocalINIMgr          INIClass
GlobalErrors         &ErrorClass
INIMgr               &INIClass
DLLInitializer       CLASS                                 ! An object of this type is used to initialize the dll, it is created in the generated bc module
Construct              PROCEDURE
Destruct               PROCEDURE
                     END

  CODE
DLLInitializer.Construct PROCEDURE


  CODE
  LocalErrors.Init(LocalErrorStatus)
  LocalINIMgr.Init('.\__projectname__.INI', NVD_INI)         ! Initialize the local INI manager to use windows INI file
  INIMgr &= LocalINIMgr
  IF GlobalErrors &= NULL
    GlobalErrors &= LocalErrors                            ! Assign local managers to global managers
  END
  FuzzyMatcher.Init                                        ! Initilaize the browse 'fuzzy matcher'
  FuzzyMatcher.SetOption(MatchOption:NoCase, 1)            ! Configure case matching
  FuzzyMatcher.SetOption(MatchOption:WordOnly, 0)          ! Configure 'word only' matching
  
!These procedures are used to initialize the DLL. It must be called by the main executable when it starts up
__projectname__:Init PROCEDURE(<ErrorClass curGlobalErrors>, <INIClass curINIMgr>)
__projectname__:Init_Called    BYTE,STATIC

  CODE
  IF __projectname__:Init_Called
     RETURN
  ELSE
     __projectname__:Init_Called = True
  END
  IF ~curGlobalErrors &= NULL
    GlobalErrors &= curGlobalErrors
  END
  IF ~curINIMgr &= NULL
    INIMgr &= curINIMgr
  END
  UDEADCT:Init(curGlobalErrors, curINIMgr)                 ! Initialise dll - (ABC) -

!This procedure is used to shutdown the DLL. It must be called by the main executable before it closes down

__projectname__:Kill PROCEDURE
__projectname__:Kill_Called    BYTE,STATIC

  CODE
  IF __projectname__:Kill_Called
     RETURN
  ELSE
     __projectname__:Kill_Called = True
  END
  UDEADCT:Kill()                                           ! Kill dll - (ABC) -
TraceLog             PROCEDURE  (STRING pMsg,<STRING pForce>) 

szMsg                cString(len(clip(pMsg))+len('[__projectname__]]')+1+6)

  CODE
   
 ! Trace
 dotrace# = FALSE
 IF CLIP(pForce) <> ''
  dotrace# = TRUE
 ELSE
  IF COMMAND('/PCDEBUG')
   dotrace# = TRUE
  END
 END
 
 IF dotrace#
    IF PC:TraceTime <> 0
       d# = CLOCK() - PC:TraceTime
    ELSE
       d# = 0
      END
    PC:TraceTime = CLOCK()
    szMsg = '[__projectname__][' & FORMAT( d#, @N04 ) & ']' & CLIP(pMsg)
    stOutPutDebugString(szMsg)
 END


GetEnv                 FUNCTION (str)
cres CSTRING(201)
cstr CSTRING(201)
len LONG
  CODE
  cstr = clip(str)
  len = GetEnvironmentVariableA (cstr, cres, size(cres)-1)
  return clip(cres)
   
PChtmlencode           FUNCTION  ( textIn )                 ! Declare Procedure
   
ReturnValue CSTRING(65535), AUTO
   
     CODE
   
      ! replace ascii-codes with HTML names
      
      ReturnValue = str_replace( '<26h>', '&amp;', textIn )
      ReturnValue = str_replace( '<3Eh>', '&gt;', ReturnValue )
      ReturnValue = str_replace( '<3Ch>', '&lt;', ReturnValue )
   
      ReturnValue = str_replace( '�', '&euro;', ReturnValue )
      ReturnValue = str_replace( '<20ACh>', '&euro;', ReturnValue )
   
      ReturnValue = str_replace( '<22h>', '&quot;', ReturnValue )
   
      ReturnValue = str_replace( '<80h>', '&euro;', ReturnValue )
      ReturnValue = str_replace( '<82h>', '&sbquo;', ReturnValue )
      ReturnValue = str_replace( '<83h>', '&fnof;', ReturnValue )
      ReturnValue = str_replace( '<84h>', '&bdquo;', ReturnValue )
      ReturnValue = str_replace( '<85h>', '&hellip;', ReturnValue )
      ReturnValue = str_replace( '<86h>', '&dagger;', ReturnValue )
      ReturnValue = str_replace( '<87h>', '&Dagger;', ReturnValue )
      ReturnValue = str_replace( '<88h>', '&circ;', ReturnValue )
      ReturnValue = str_replace( '<89h>', '&permil;', ReturnValue )
      ReturnValue = str_replace( '<8Ah>', '&Scaron;', ReturnValue )
      ReturnValue = str_replace( '<8Bh>', '&lsaquo;', ReturnValue )
      ReturnValue = str_replace( '<8Ch>', '&OElig;', ReturnValue )
      ReturnValue = str_replace( '<91h>', '&lsquo;', ReturnValue )
      ReturnValue = str_replace( '<92h>', '&rsquo;', ReturnValue )
      ReturnValue = str_replace( '<93h>', '&ldquo;', ReturnValue )
      ReturnValue = str_replace( '<94h>', '&rdquo;', ReturnValue )
      ReturnValue = str_replace( '<95h>', '&bull;', ReturnValue )
      ReturnValue = str_replace( '<96h>', '&ndash;', ReturnValue )
      ReturnValue = str_replace( '<97h>', '&mdash;', ReturnValue )
      ReturnValue = str_replace( '<98h>', '&tilde;', ReturnValue )
      ReturnValue = str_replace( '<99h>', '&trade;', ReturnValue )
      ReturnValue = str_replace( '<9Ah>', '&scaron;', ReturnValue )
      ReturnValue = str_replace( '<9Bh>', '&rsaquo;', ReturnValue )
      ReturnValue = str_replace( '<9Ch>', '&oelig;', ReturnValue )
      ReturnValue = str_replace( '<9Fh>', '&yuml;', ReturnValue )
   
      ReturnValue = str_replace( '<A0h>', '&nbsp;', ReturnValue )
      ReturnValue = str_replace( '<A1h>', '&iexcl;', ReturnValue )
      ReturnValue = str_replace( '<A2h>', '&cent;', ReturnValue )
      ReturnValue = str_replace( '<A3h>', '&pound;', ReturnValue )
      ReturnValue = str_replace( '<A4h>', '&curren;', ReturnValue )
      ReturnValue = str_replace( '<A5h>', '&yen;', ReturnValue )
      ReturnValue = str_replace( '<A6h>', '&brvbar;', ReturnValue )
      ReturnValue = str_replace( '<A7h>', '&sect;', ReturnValue )
      ReturnValue = str_replace( '<A8h>', '&uml;', ReturnValue )
      ReturnValue = str_replace( '<A9h>', '&copy;', ReturnValue )
      ReturnValue = str_replace( '<AAh>', '&ordf;', ReturnValue )
      ReturnValue = str_replace( '<ABh>', '&laquo;', ReturnValue )
      ReturnValue = str_replace( '<ACh>', '&not;', ReturnValue )
      ReturnValue = str_replace( '<ADh>', '&shy;', ReturnValue )
      ReturnValue = str_replace( '<AEh>', '&reg;', ReturnValue )
      ReturnValue = str_replace( '<AFh>', '&macr;', ReturnValue )
      ReturnValue = str_replace( '<B0h>', '&deg;', ReturnValue )
      ReturnValue = str_replace( '<B1h>', '&plusmn;', ReturnValue )
      ReturnValue = str_replace( '<B2h>', '&sup2;', ReturnValue )
      ReturnValue = str_replace( '<B3h>', '&sup3;', ReturnValue )
      ReturnValue = str_replace( '<B4h>', '&acute;', ReturnValue )
      ReturnValue = str_replace( '<B5h>', '&micro;', ReturnValue )
      ReturnValue = str_replace( '<B6h>', '&para;', ReturnValue )
      ReturnValue = str_replace( '<B7h>', '&middot;', ReturnValue )
      ReturnValue = str_replace( '<B8h>', '&cedil;', ReturnValue )
      ReturnValue = str_replace( '<B9h>', '&sup1;', ReturnValue )
      ReturnValue = str_replace( '<BAh>', '&ordm;', ReturnValue )
      ReturnValue = str_replace( '<BBh>', '&raquo;', ReturnValue )
      ReturnValue = str_replace( '<BCh>', '&frac14;', ReturnValue )
      ReturnValue = str_replace( '<BDh>', '&frac12;', ReturnValue )
      ReturnValue = str_replace( '<BEh>', '&frac34;', ReturnValue )
      ReturnValue = str_replace( '<BFh>', '&iquest;', ReturnValue )
      ReturnValue = str_replace( '<C0h>', '&Agrave;', ReturnValue )
      ReturnValue = str_replace( '<C1h>', '&Aacute;', ReturnValue )
      ReturnValue = str_replace( '<C2h>', '&Acirc;', ReturnValue )
      ReturnValue = str_replace( '<C3h>', '&Atilde;', ReturnValue )
      ReturnValue = str_replace( '<C4h>', '&Auml;', ReturnValue )
      ReturnValue = str_replace( '<C5h>', '&Aring;', ReturnValue )
      ReturnValue = str_replace( '<C6h>', '&AElig;', ReturnValue )
      ReturnValue = str_replace( '<C7h>', '&Ccedil;', ReturnValue )
      ReturnValue = str_replace( '<C8h>', '&Egrave;', ReturnValue )
      ReturnValue = str_replace( '<C9h>', '&Eacute;', ReturnValue )
      ReturnValue = str_replace( '<CAh>', '&Ecirc;', ReturnValue )
      ReturnValue = str_replace( '<CBh>', '&Euml;', ReturnValue )
      ReturnValue = str_replace( '<CCh>', '&Igrave;', ReturnValue )
      ReturnValue = str_replace( '<CDh>', '&Iacute;', ReturnValue )
      ReturnValue = str_replace( '<CEh>', '&Icirc;', ReturnValue )
      ReturnValue = str_replace( '<CFh>', '&Iuml;', ReturnValue )
      ReturnValue = str_replace( '<D0h>', '&ETH;', ReturnValue )
      ReturnValue = str_replace( '<D1h>', '&Ntilde;', ReturnValue )
      ReturnValue = str_replace( '<D2h>', '&Ograve;', ReturnValue )
      ReturnValue = str_replace( '<D3h>', '&Oacute;', ReturnValue )
      ReturnValue = str_replace( '<D4h>', '&Ocirc;', ReturnValue )
      ReturnValue = str_replace( '<D5h>', '&Otilde;', ReturnValue )
      ReturnValue = str_replace( '<D6h>', '&Ouml;', ReturnValue )
      ReturnValue = str_replace( '<D7h>', '&times;', ReturnValue )
      ReturnValue = str_replace( '<D8h>', '&Oslash;', ReturnValue )
      ReturnValue = str_replace( '<D9h>', '&Ugrave;', ReturnValue )
      ReturnValue = str_replace( '<DAh>', '&Uacute;', ReturnValue )
      ReturnValue = str_replace( '<DBh>', '&Ucirc;', ReturnValue )
      ReturnValue = str_replace( '<DCh>', '&Uuml;', ReturnValue )
      ReturnValue = str_replace( '<DDh>', '&Yacute;', ReturnValue )
      ReturnValue = str_replace( '<DEh>', '&THORN;', ReturnValue )
      ReturnValue = str_replace( '<DFh>', '&szlig;', ReturnValue )
      ReturnValue = str_replace( '<E0h>', '&agrave;', ReturnValue )
      ReturnValue = str_replace( '<E1h>', '&aacute;', ReturnValue )
      ReturnValue = str_replace( '<E2h>', '&acirc;', ReturnValue )
      ReturnValue = str_replace( '<E3h>', '&atilde;', ReturnValue )
      ReturnValue = str_replace( '<E4h>', '&auml;', ReturnValue )
      ReturnValue = str_replace( '<E5h>', '&aring;', ReturnValue )
      ReturnValue = str_replace( '<E6h>', '&aelig;', ReturnValue )
      ReturnValue = str_replace( '<E7h>', '&ccedil;', ReturnValue )
      ReturnValue = str_replace( '<E8h>', '&egrave;', ReturnValue )
      ReturnValue = str_replace( '<E9h>', '&eacute;', ReturnValue )
      ReturnValue = str_replace( '<EAh>', '&ecirc;', ReturnValue )
      ReturnValue = str_replace( '<EBh>', '&euml;', ReturnValue )
      ReturnValue = str_replace( '<ECh>', '&igrave;', ReturnValue )
      ReturnValue = str_replace( '<EDh>', '&iacute;', ReturnValue )
      ReturnValue = str_replace( '<EEh>', '&icirc;', ReturnValue )
      ReturnValue = str_replace( '<EFh>', '&iuml;', ReturnValue )
      ReturnValue = str_replace( '<F0h>', '&eth;', ReturnValue )
      ReturnValue = str_replace( '<F1h>', '&ntilde;', ReturnValue )
      ReturnValue = str_replace( '<F2h>', '&ograve;', ReturnValue )
      ReturnValue = str_replace( '<F3h>', '&oacute;', ReturnValue )
      ReturnValue = str_replace( '<F4h>', '&ocirc;', ReturnValue )
      ReturnValue = str_replace( '<F5h>', '&otilde;', ReturnValue )
      ReturnValue = str_replace( '<F6h>', '&ouml;', ReturnValue )
      ReturnValue = str_replace( '<F7h>', '&divide;', ReturnValue )
      ReturnValue = str_replace( '<F8h>', '&oslash;', ReturnValue )
      ReturnValue = str_replace( '<F9h>', '&ugrave;', ReturnValue )
      ReturnValue = str_replace( '<FAh>', '&uacute;', ReturnValue )
      ReturnValue = str_replace( '<FBh>', '&ucirc;', ReturnValue )
      ReturnValue = str_replace( '<FCh>', '&uuml;', ReturnValue )
      ReturnValue = str_replace( '<FDh>', '&yacute;', ReturnValue )
      ReturnValue = str_replace( '<FEh>', '&thorn;', ReturnValue )
      ReturnValue = str_replace( '<FFh>', '&yuml;', ReturnValue )

      ReturnValue = str_replace( '<13,10>', '<br><13,10>', ReturnValue )

      Return ReturnValue


!!! <summary>
!!! str_replace - Special Formatting Documentation
!!! </summary>
!!! <param name="needle">The needle comment</param>
!!! <param name="replace">The replace comment</param>
!!! <param name="haystack">The haystack comment</param>
!!! <remarks>
!!! This is a remark.
!!! </remarks>
str_replace          FUNCTION  ( needle, replace, haystack ) ! Declare Procedure

st		StringTheory

     CODE
  st.SetValue( haystack )
  st.Replace( needle, replace )
  
  return st.GetValue()
  

Log                  PROCEDURE  ( STRING ToLog )

PClogFile              FILE, DRIVER( 'ASCII', '/CLIP=on' ), CREATE, PRE( PCLOG ), NAME( 'Log.txt' )
Record                  RECORD, PRE()
Row                        STRING(8000)
                        END
                     END
   
     CODE
   
      If NOT EXISTS( 'Log.txt' ) 
         Create( PClogFile )
      END
      Open( PClogFile )
      PCLOG:Row = Format(Today(),@D06-) & ' ' & Format(Clock(),@T04) & ' ' & ToLog      
      Add( PClogFile ) 
      Close( PClogFile )
      
   

ClarionDate          FUNCTION  ( PAR:DATUM )              ! Declare Procedure

     CODE
   
   
   RETURN ROUND( PAR:DATUM / ( 60 * 60 * 24 ) - 0.5, 1 ) + 61730
   
   
   
   
   
   

UNIXdate             FUNCTION  ( PAR:DATUM )              ! Declare Procedure

     CODE
     
   ! Datum
   ! UNIX the number of seconds since the Unix Epoch (January 1 1970 00:00:00 GMT).
   !
   ! Clarion start at December 28, 1800
   ! number of days that have elapsed since December 28, 1800.
   ! The range of accessible dates is from January 1, 1801 (standard date 4) to December 31, 9999 (standard date 2,994,626)
   !
   ! DATE(month,day,year)
   !
   ! Number of days since 1970 times the numbers of seconds...
   !
   !       Clarion Days  -  1-1-1970         *  Seconds per day
   
   
   RETURN ( PAR:DATUM - Date( 1, 1, 1970 ) ) * ( 60 * 60 * 24 )
   
   
   
WeekNummer           FUNCTION  (Datum)

  CODE

  RETURN BepalenWeek( Datum )
  
BepalenWeek          FUNCTION  (DATUM)

  CODE
  
  If DATUM = 0 or DATUM = '' Then RETURN 0.

  eenjan# = DATE(1,1,YEAR(DATUM))           ! 1 januari dit jaar
  derdec# = DATE(12,31,YEAR(DATUM))         ! 31 december dit jaar

  begin#  = ( ( (eenjan# % 7) + 6 ) % 7)    ! dag van de week ! +6 -> zo = 0 moet zijn ma = 0
  eind# = ( ( (derdec# % 7) + 6 ) % 7 )      !

  weekbegin# = eenjan# - begin#             ! eerste dag van de eerste week
  dagen#  = DATUM - weekbegin#              ! aantal dagen vanaf eerste dag van eerste week

  if begin# > 3                             ! als het jaar laat in de week begint is die week van vorig jaar
     wk#     = INT ( dagen# / 7 )
     if wk# = 0                             ! dag hoort bij
        wk# = WeekNummer(DATUM-7) + 1             ! week van vorig jaar
     end
  else                                      ! eerste week is van dit jaar
     wk# = INT (dagen#/7) + 1               !
  end

  If wk# > 52 and eind# <= 2
     wk# = 1
  End

  RETURN wk#
 
  
   
   
PC_Calendar            PROCEDURE  (pcPassedDate)               ! Declare Procedure
 ! Start of "Data Section"
 ! [Priority 1000]

 ! [Priority 4000]
 ! Extension Function � PractiCom/Fingertips 1996 __________________________________________________________
 ! DESCRIPTION: Displays popup calendar window to allow user to scroll through the months
 !              and return a date by clicking on it.
 !
 ! USAGE:       Calendar(LONG)    If starting date is omitted, defaults to current date.
 ! RETURNS:     LONG
 !________________________________________________________________________________________________

 ! [Priority 4000]
 !Define Variables
MonthNumber    SHORT
YearNumber     SHORT
PassedDate		LONG
 ! [Priority 4000]
 !Define Window
window WINDOW('Caption'),AT(,,178,105),FONT('MS Sans Serif',10,,FONT:regular),CENTER,ALRT(UpKey),ALRT(DownKey), |
         ALRT(PgUpKey),ALRT(PgDnKey),ALRT(EscKey),ALRT(RightKey),ALRT(LeftKey),ALRT(MouseRight),ALRT(EnterKey),GRAY, |
         DOUBLE
       STRING('Ma'),AT(13,1,10,10),USE(?StringMo),CENTER,FONT(,,COLOR:Navy,)
       STRING('Di'),AT(28,1,10,10),USE(?StringTu),CENTER,FONT(,,COLOR:Navy,)
       STRING('Wo'),AT(42,1,12,10),USE(?StringWe),CENTER,FONT(,,COLOR:Navy,)
       STRING('Do'),AT(58,1,10,10),USE(?StringTh),CENTER,FONT(,,COLOR:Navy,)
       STRING('Vr'),AT(73,1,10,10),USE(?StringFr),CENTER,FONT(,,COLOR:Navy,)
       STRING('Za'),AT(88,1,10,10),USE(?StringSa),CENTER,FONT(,,COLOR:Red,)
       STRING('Zo'),AT(103,1,10,10),USE(?StringSu),CENTER,FONT(,,COLOR:Red,)
       STRING('Maand'),AT(127,15),USE(?StringMonth),TRN,CENTER,FONT(,,COLOR:Maroon,)
       SPIN(@n_2),AT(125,24,40,12),USE(MonthNumber),IMM,CENTER,HSCROLL,TIP('Maand vooruit/terug'),STEP(1)
       PANEL,AT(121,14,52,26),USE(?Panel3),BEVEL(-1)
       STRING('Jaar'),AT(130,46),USE(?StringYear),TRN,CENTER,FONT(,,COLOR:Maroon,)
  SPIN(@n_4),AT(125,55,40,12),USE(YearNumber),IMM,CENTER,HSCROLL,TIP('Jaar vooruit/terug'),STEP(1)
       PANEL,AT(121,45,52,26),USE(?Panel2),BEVEL(-1)
   BUTTON('Vandaag'),AT(125,74,40,12),USE(?Button:Today)
   BUTTON('Sluiten'),AT(125,90,40,12),USE(?Button:Close)
       PANEL,AT(0,0,,12),USE(?Panel1),FULL,BEVEL(0,0,09H)
       REGION,AT(12,14,12,12),USE(?Region1),IMM
       REGION,AT(27,14,12,12),USE(?Region2),IMM
       REGION,AT(42,14,12,12),USE(?Region3),IMM
       REGION,AT(57,14,12,12),USE(?Region4),IMM
       REGION,AT(72,14,12,12),USE(?Region5),IMM
       REGION,AT(87,14,12,12),USE(?Region6),IMM
       REGION,AT(102,14,12,12),USE(?Region7),IMM
       REGION,AT(12,29,12,12),USE(?Region8),IMM
       REGION,AT(27,29,12,12),USE(?Region9),IMM
       REGION,AT(42,29,12,12),USE(?Region10),IMM
       REGION,AT(57,29,12,12),USE(?Region11),IMM
       REGION,AT(72,29,12,12),USE(?Region12),IMM
       REGION,AT(87,29,12,12),USE(?Region13),IMM
       REGION,AT(102,29,12,12),USE(?Region14),IMM
       REGION,AT(12,44,12,12),USE(?Region15),IMM
       REGION,AT(27,44,12,12),USE(?Region16),IMM
       REGION,AT(42,44,12,12),USE(?Region17),IMM
       REGION,AT(57,44,12,12),USE(?Region18),IMM
       REGION,AT(72,44,12,12),USE(?Region19),IMM
       REGION,AT(87,44,12,12),USE(?Region20),IMM
       REGION,AT(102,44,12,12),USE(?Region21),IMM
       REGION,AT(12,59,12,12),USE(?Region22),IMM
       REGION,AT(27,59,12,12),USE(?Region23),IMM
       REGION,AT(42,59,12,12),USE(?Region24),IMM
       REGION,AT(57,59,12,12),USE(?Region25),IMM
       REGION,AT(72,59,12,12),USE(?Region26),IMM
       REGION,AT(87,59,12,12),USE(?Region27),IMM
       REGION,AT(102,59,12,12),USE(?Region28),IMM
       REGION,AT(12,74,12,12),USE(?Region29),IMM
       REGION,AT(27,74,12,12),USE(?Region30),IMM
       REGION,AT(42,74,12,12),USE(?Region31),IMM
       REGION,AT(57,74,12,12),USE(?Region32),IMM
       REGION,AT(72,74,12,12),USE(?Region33),IMM
       REGION,AT(87,74,12,12),USE(?Region34),IMM
       REGION,AT(102,74,12,12),USE(?Region35),IMM
       REGION,AT(12,90,12,12),USE(?Region36),IMM
       REGION,AT(27,90,12,12),USE(?Region37),IMM
       STRING('1'),AT(0,18,12,12),USE(?wk1),TRN,CENTER,FONT('Arial',7,,,)
       STRING('2'),AT(0,33,12,12),USE(?wk2),TRN,CENTER,FONT('Arial',7,,,)
       STRING('3'),AT(0,48,12,12),USE(?wk3),TRN,CENTER,FONT('Arial',7,,,)
       STRING('4'),AT(0,63,12,12),USE(?wk4),TRN,CENTER,FONT('Arial',7,,,)
       STRING('5'),AT(0,78,12,12),USE(?wk5),TRN,CENTER,FONT('Arial',7,,,)
       STRING('6'),AT(0,94,12,12),USE(?wk6),TRN,CENTER,FONT('Arial',7,,,)
       STRING(''),AT(12,16,12,12),USE(?String1),TRN,CENTER
       STRING(''),AT(27,16,12,12),USE(?String2),TRN,CENTER
       STRING(''),AT(42,16,12,12),USE(?String3),TRN,CENTER
       STRING(''),AT(57,16,12,12),USE(?String4),TRN,CENTER
       STRING(''),AT(72,16,12,12),USE(?String5),TRN,CENTER
       STRING(''),AT(87,16,12,12),USE(?String6),TRN,CENTER,FONT(,,COLOR:Red,)
       STRING(''),AT(102,16,12,12),USE(?String7),TRN,CENTER,FONT(,,COLOR:Red,)
       STRING(''),AT(12,31,12,12),USE(?String8),TRN,CENTER
       STRING(''),AT(27,31,12,12),USE(?String9),TRN,CENTER
       STRING(''),AT(42,31,12,12),USE(?String10),TRN,CENTER
       STRING(''),AT(57,31,12,12),USE(?String11),TRN,CENTER
       STRING(''),AT(72,31,12,12),USE(?String12),TRN,CENTER
       STRING(''),AT(87,31,12,12),USE(?String13),TRN,CENTER,FONT(,,COLOR:Red,)
       STRING(''),AT(102,31,12,12),USE(?String14),TRN,CENTER,FONT(,,COLOR:Red,)
       STRING(''),AT(12,46,12,12),USE(?String15),TRN,CENTER
       STRING(''),AT(27,46,12,12),USE(?String16),TRN,CENTER
       STRING(''),AT(42,46,12,12),USE(?String17),TRN,CENTER
       STRING(''),AT(57,46,12,12),USE(?String18),TRN,CENTER
       STRING(''),AT(72,46,12,12),USE(?String19),TRN,CENTER
       STRING(''),AT(87,46,12,12),USE(?String20),TRN,CENTER,FONT(,,COLOR:Red,)
       STRING(''),AT(102,46,12,12),USE(?String21),TRN,CENTER,FONT(,,COLOR:Red,)
       STRING(''),AT(12,61,12,12),USE(?String22),TRN,CENTER
       STRING(''),AT(27,61,12,12),USE(?String23),TRN,CENTER
       STRING(''),AT(42,61,12,12),USE(?String24),TRN,CENTER
       STRING(''),AT(57,61,12,12),USE(?String25),TRN,CENTER
       STRING(''),AT(72,61,12,12),USE(?String26),TRN,CENTER
       STRING(''),AT(87,61,12,12),USE(?String27),TRN,CENTER,FONT(,,COLOR:Red,)
       STRING(''),AT(102,61,12,12),USE(?String28),TRN,CENTER,FONT(,,COLOR:Red,)
       STRING(''),AT(12,76,12,12),USE(?String29),TRN,CENTER
       STRING(''),AT(27,76,12,12),USE(?String30),TRN,CENTER
       STRING(''),AT(42,76,12,12),USE(?String31),TRN,CENTER
       STRING(''),AT(57,76,12,12),USE(?String32),TRN,CENTER
       STRING(''),AT(72,76,12,12),USE(?String33),TRN,CENTER
       STRING(''),AT(87,76,12,12),USE(?String34),TRN,CENTER,FONT(,,COLOR:Red,)
       STRING(''),AT(102,76,12,12),USE(?String35),TRN,CENTER,FONT(,,COLOR:Red,)
       STRING(''),AT(12,92,12,12),USE(?String36),TRN,CENTER
       STRING(''),AT(27,92,12,12),USE(?String37),TRN,CENTER
     END

 ! [Priority 7300]

 ! End of "Data Section"
 ! Start of "Local Data After Object Declarations"
 ! [Priority 5000]

 ! End of "Local Data After Object Declarations"

  CODE
 ! Start of "Processed Code"
 ! [Priority 4000]
 !Open Calendar Window
  PUSHBIND
  OPEN(Window)
 !Set up initial display
 PassedDate = pcPassedDate
  IF PassedDate = ''         !If no date was passed default to current date
     PassedDate=Today()
  END
  MonthNumber = MONTH(PassedDate)
  YearNumber = YEAR(PassedDate)
  DO SetWindowTitle
  DO AssignAllStrings
  DO SetBevels
  DO HighlightPassedDate
   ACCEPT
    CASE EVENT()
    OF EVENT:AlertKey
      CASE KEYCODE()
         OF DownKey OROF LeftKey
            MonthNumber -=1
         OF UpKey OROF RightKey
            MonthNumber +=1
         OF PgUpKey
            YearNumber -= 1
         OF PgDnKey
            YearNumber += 1
         OF EscKey                   !Close, returning passed date or default current date
            POPBIND
            RETURN(pcPassedDate)
         OF MouseRight               !Close, returning passed date or default current date
            POPBIND
            RETURN(pcPassedDate)
         OF EnterKey                 !Close, returning current date
            RETURN(PassedDate)
      END	!CASE KEYCODE
      DO CheckRange
      DO SetWindowTitle
      DO ClearHighlight
      DO AssignAllStrings
      DO SetBevels
    END		!CASE EVENT
  CASE FIELD()
  OF ?Button:Close
  CASE EVENT()
  OF EVENT:Accepted
   POPBIND
            RETURN(PassedDate)
  END
  OF ?Button:Today
  CASE EVENT()	
  OF EVENT:Accepted
     MonthNumber = MONTH(Today())
     YearNumber = YEAR(Today())
   DO CheckRange
   DO SetWindowTitle
   DO ClearHighlight
   DO AssignAllStrings
   DO SetBevels
   Display()
  END		
    
    OF ?MonthNumber
  !CASE EVENT()
  !OF EVENT:Accepted
      !OF EVENT:NewSelection
        DO CheckRange
        DO SetWindowTitle
        DO ClearHighlight
        DO AssignAllStrings
        DO SetBevels
      !END
    OF ?YearNumber
      !CASE EVENT()
  !OF EVENT:Accepted
      !OF EVENT:NewSelection
        DO CheckRange
        DO SetWindowTitle
        DO ClearHighlight
        DO AssignAllStrings
        DO SetBevels
      !END
    OF ?Region1
      CASE EVENT()
      OF EVENT:Accepted
        IF ?String1{PROP:TEXT} <> ''
           POPBIND
           RETURN(DEFORMAT(?String1{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
         END
      END
    OF ?Region2
      CASE EVENT()
      OF EVENT:Accepted
        IF ?String2{PROP:TEXT} <> ''
           POPBIND
           RETURN(DEFORMAT(?String2{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
        END
      END
    OF ?Region3
      CASE EVENT()
      OF EVENT:Accepted
        IF ?String3{PROP:TEXT} <> ''
           POPBIND
           RETURN(DEFORMAT(?String3{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
        END
      END
    OF ?Region4
      CASE EVENT()
      OF EVENT:Accepted
        IF ?String4{PROP:TEXT} <> ''
           POPBIND
           RETURN(DEFORMAT(?String4{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
        END
      END
    OF ?Region5
      CASE EVENT()
      OF EVENT:Accepted
        IF ?String5{PROP:TEXT} <> ''
           POPBIND
           RETURN(DEFORMAT(?String5{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
        END
      END
    OF ?Region6
      CASE EVENT()
      OF EVENT:Accepted
        IF ?String6{PROP:TEXT} <> ''
           POPBIND
           RETURN(DEFORMAT(?String6{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
        END
      END
    OF ?Region7
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String7{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region8
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String8{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region9
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String9{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region10
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String10{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region11
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String11{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region12
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String12{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region13
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String13{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region14
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String14{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region15
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String15{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region16
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String16{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region17
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String17{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region18
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String18{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region19
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String19{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region20
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String20{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region21
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String21{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region22
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String22{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region23
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String23{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region24
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String24{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region25
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String25{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region26
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String26{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region27
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String27{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region28
      CASE EVENT()
      OF EVENT:Accepted
        POPBIND
        RETURN(DEFORMAT(?String28{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
      END
    OF ?Region29
      CASE EVENT()
      OF EVENT:Accepted
        IF ?String29{PROP:TEXT} <> ''
           POPBIND
           RETURN(DEFORMAT(?String29{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
        END
      END
    OF ?Region30
      CASE EVENT()
      OF EVENT:Accepted
        IF ?String30{PROP:TEXT} <> ''
           POPBIND
           RETURN(DEFORMAT(?String30{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
        END
      END
    OF ?Region31
      CASE EVENT()
      OF EVENT:Accepted
        IF ?String31{PROP:TEXT} <> ''
           POPBIND
           RETURN(DEFORMAT(?String31{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
        END
      END
    OF ?Region32
      CASE EVENT()
      OF EVENT:Accepted
        IF ?String32{PROP:TEXT} <> ''
           POPBIND
           RETURN(DEFORMAT(?String32{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
        END
      END
    OF ?Region33
      CASE EVENT()
      OF EVENT:Accepted
        IF ?String33{PROP:TEXT} <> ''
           POPBIND
           RETURN(DEFORMAT(?String33{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
        END
      END
    OF ?Region34
      CASE EVENT()
      OF EVENT:Accepted
        IF ?String34{PROP:TEXT} <> ''
           POPBIND
           RETURN(DEFORMAT(?String34{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
        END
      END
    OF ?Region35
      CASE EVENT()
      OF EVENT:Accepted
        IF ?String35{PROP:TEXT} <> ''
           POPBIND
           RETURN(DEFORMAT(?String35{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
        END
      END
    OF ?Region36
      CASE EVENT()
      OF EVENT:Accepted
        IF ?String36{PROP:TEXT} <> ''
           POPBIND
           RETURN(DEFORMAT(?String36{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
        END
      END
    OF ?Region37
      CASE EVENT()
      OF EVENT:Accepted
        IF ?String37{PROP:TEXT} <> ''
           POPBIND
           RETURN(DEFORMAT(?String37{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber,@D6))
        END
      END
    END		!CASE FIELD
  END		!ACCEPT LOOP
  !------------------------------------------------------------------------------------------------

SetWindowTitle ROUTINE
 !|Set up the window title
    CASE MonthNumber
         OF  1
             Window{PROP:TEXT}='Januari ' &YearNumber
         OF  2
             Window{PROP:TEXT}='Februari ' &YearNumber
         OF  3
             Window{PROP:TEXT}='Maart ' &YearNumber
         OF  4
             Window{PROP:TEXT}='April ' &YearNumber
         OF  5
             Window{PROP:TEXT}='Mei ' &YearNumber
         OF  6
             Window{PROP:TEXT}='Juni ' &YearNumber
         OF  7
             Window{PROP:TEXT}='Juli ' &YearNumber
         OF  8
             Window{PROP:TEXT}='Augustus ' &YearNumber
         OF  9
             Window{PROP:TEXT}='September ' &YearNumber
         OF  10
             Window{PROP:TEXT}='Oktober ' &YearNumber
         OF  11
             Window{PROP:TEXT}='November ' &YearNumber
         OF  12
             Window{PROP:TEXT}='December ' &YearNumber
    END!CASE
 !|----------------------------------------------------------------------------------------------
AssignAllStrings ROUTINE
 !|Redraws the calendar for the current month
 !|
 !How many days are there in this month?
   CASE MonthNumber
     OF     9                           !Short months
     OROF   4
     OROF   6
     OROF   11
            MaxDays#=30
     OF     2                           !If the month is February
            IF YearNumber % 4 = 0       !If a leap year
               MaxDays# = 29
            ELSE
               MaxDays# = 28
            END!IF
     ELSE   MaxDays# = 31               !Standard months
   END!CASE
   !Find what day the first of this month falls on.
   ?Wk1{PROP:Text} = BepalenWeek( DEFORMAT(('1/' &MonthNumber &'/' &YearNumber),@D6) )
   ?Wk2{PROP:Text} = BepalenWeek( DEFORMAT(('8/' &MonthNumber &'/' &YearNumber),@D6) )
   ?Wk3{PROP:Text} = BepalenWeek( DEFORMAT(('15/' &MonthNumber &'/' &YearNumber),@D6) )
   ?Wk4{PROP:Text} = BepalenWeek( DEFORMAT(('22/' &MonthNumber &'/' &YearNumber),@D6) )
   CASE DEFORMAT(('1/' &MonthNumber &'/' &YearNumber),@D6) % 7
      OF 1                              !Month starts on a Monday
         ?String1{PROP:TEXT} = '1'
         ?String2{PROP:TEXT} = '2'
         ?String3{PROP:TEXT} = '3'
         ?String4{PROP:TEXT} = '4'
         ?String5{PROP:TEXT} = '5'
         ?String6{PROP:TEXT} = '6'
         ?String7{PROP:TEXT} = '7'
      OF 2                              !Month starts on a Tuesday
         ?String1{PROP:TEXT} = ''
         ?String2{PROP:TEXT} = '1'
         ?String3{PROP:TEXT} = '2'
         ?String4{PROP:TEXT} = '3'
         ?String5{PROP:TEXT} = '4'
         ?String6{PROP:TEXT} = '5'
         ?String7{PROP:TEXT} = '6'
      OF 3                              !Month starts on a Wednesday
         ?String1{PROP:TEXT} = ''
         ?String2{PROP:TEXT} = ''
         ?String3{PROP:TEXT} = '1'
         ?String4{PROP:TEXT} = '2'
         ?String5{PROP:TEXT} = '3'
         ?String6{PROP:TEXT} = '4'
         ?String7{PROP:TEXT} = '5'
      OF 4                              !Month starts on a Thursday
         ?String1{PROP:TEXT} = ''
         ?String2{PROP:TEXT} = ''
         ?String3{PROP:TEXT} = ''
         ?String4{PROP:TEXT} = '1'
         ?String5{PROP:TEXT} = '2'
         ?String6{PROP:TEXT} = '3'
         ?String7{PROP:TEXT} = '4'
      OF 5                              !Month starts on a Friday
         ?String1{PROP:TEXT} = ''
         ?String2{PROP:TEXT} = ''
         ?String3{PROP:TEXT} = ''
         ?String4{PROP:TEXT} = ''
         ?String5{PROP:TEXT} = '1'
         ?String6{PROP:TEXT} = '2'
         ?String7{PROP:TEXT} = '3'
      OF 6                              !Month starts on a Saturday
         ?String1{PROP:TEXT} = ''
         ?String2{PROP:TEXT} = ''
         ?String3{PROP:TEXT} = ''
         ?String4{PROP:TEXT} = ''
         ?String5{PROP:TEXT} = ''
         ?String6{PROP:TEXT} = '1'
         ?String7{PROP:TEXT} = '2'
      OF 0                              !Month starts on a Sunday
         ?String1{PROP:TEXT} = ''
         ?String2{PROP:TEXT} = ''
         ?String3{PROP:TEXT} = ''
         ?String4{PROP:TEXT} = ''
         ?String5{PROP:TEXT} = ''
         ?String6{PROP:TEXT} = ''
         ?String7{PROP:TEXT} = '1'
   END
   !Format the strings for the rest of the current month.
   !From day 8 to 28 just add 1 to the previous string, then
   !start checking for potential last day of month from string 29 onwards
  ?String8{PROP:TEXT} = DEFORMAT(?String7{PROP:TEXT},@n2) + 1
  ?String9{PROP:TEXT} = DEFORMAT(?String8{PROP:TEXT},@n2) + 1
  ?String10{PROP:TEXT} = DEFORMAT(?String9{PROP:TEXT},@n2) + 1
  ?String11{PROP:TEXT} = DEFORMAT(?String10{PROP:TEXT},@n2) + 1
  ?String12{PROP:TEXT} = DEFORMAT(?String11{PROP:TEXT},@n2) + 1
  ?String13{PROP:TEXT} = DEFORMAT(?String12{PROP:TEXT},@n2) + 1
  ?String14{PROP:TEXT} = DEFORMAT(?String13{PROP:TEXT},@n2) + 1
  ?String15{PROP:TEXT} = DEFORMAT(?String14{PROP:TEXT},@n2) + 1
  ?String16{PROP:TEXT} = DEFORMAT(?String15{PROP:TEXT},@n2) + 1
  ?String17{PROP:TEXT} = DEFORMAT(?String16{PROP:TEXT},@n2) + 1
  ?String18{PROP:TEXT} = DEFORMAT(?String17{PROP:TEXT},@n2) + 1
  ?String19{PROP:TEXT} = DEFORMAT(?String18{PROP:TEXT},@n2) + 1
  ?String20{PROP:TEXT} = DEFORMAT(?String19{PROP:TEXT},@n2) + 1
  ?String21{PROP:TEXT} = DEFORMAT(?String20{PROP:TEXT},@n2) + 1
  ?String22{PROP:TEXT} = DEFORMAT(?String21{PROP:TEXT},@n2) + 1
  ?String23{PROP:TEXT} = DEFORMAT(?String22{PROP:TEXT},@n2) + 1
  ?String24{PROP:TEXT} = DEFORMAT(?String23{PROP:TEXT},@n2) + 1
  ?String25{PROP:TEXT} = DEFORMAT(?String24{PROP:TEXT},@n2) + 1
  ?String26{PROP:TEXT} = DEFORMAT(?String25{PROP:TEXT},@n2) + 1
  ?String27{PROP:TEXT} = DEFORMAT(?String26{PROP:TEXT},@n2) + 1
  ?String28{PROP:TEXT} = DEFORMAT(?String27{PROP:TEXT},@n2) + 1
  IF (DEFORMAT(?String28{PROP:TEXT},@n2) + 1) NOT > Maxdays#
     ?String29{PROP:TEXT} = DEFORMAT(?String28{PROP:TEXT},@n2) + 1
  ?Wk5{PROP:Text} = BepalenWeek( DEFORMAT((?String29{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber),@D6) )
  ELSE
     ?String29{PROP:TEXT} = ''
  ?Wk5{PROP:Text} = ''
  END
  IF ?String29{PROP:TEXT} <> '' AND (DEFORMAT(?String29{PROP:TEXT},@n2) + 1) NOT > MaxDays#
     ?String30{PROP:TEXT} = DEFORMAT(?String29{PROP:TEXT},@n2) + 1
  ELSE
     ?String30{PROP:TEXT} = ''
  END
  IF ?String30{PROP:TEXT} <> '' AND (DEFORMAT(?String30{PROP:TEXT},@n2) + 1) NOT > MaxDays#
     ?String31{PROP:TEXT} = DEFORMAT(?String30{PROP:TEXT},@n2) + 1
  ELSE
     ?String31{PROP:TEXT} = ''
  END
  IF ?String31{PROP:TEXT} <> '' AND (DEFORMAT(?String31{PROP:TEXT},@n2) + 1) NOT > MaxDays#
     ?String32{PROP:TEXT} = DEFORMAT(?String31{PROP:TEXT},@n2) + 1
  ELSE
     ?String32{PROP:TEXT} = ''
  END
  IF ?String32{PROP:TEXT} <> '' AND (DEFORMAT(?String32{PROP:TEXT},@n2) + 1) NOT > MaxDays#
     ?String33{PROP:TEXT} = DEFORMAT(?String32{PROP:TEXT},@n2) + 1
  ELSE
     ?String33{PROP:TEXT} = ''
  END
  IF ?String33{PROP:TEXT} <> '' AND (DEFORMAT(?String33{PROP:TEXT},@n2) + 1) NOT > MaxDays#
     ?String34{PROP:TEXT} = DEFORMAT(?String33{PROP:TEXT},@n2) + 1
  ELSE
     ?String34{PROP:TEXT} = ''
  END
  IF ?String34{PROP:TEXT} <> '' AND (DEFORMAT(?String34{PROP:TEXT},@n2) + 1) NOT > MaxDays#
     ?String35{PROP:TEXT} = DEFORMAT(?String34{PROP:TEXT},@n2) + 1
  ELSE
     ?String35{PROP:TEXT} = ''
  END
  IF ?String35{PROP:TEXT} <> '' AND (DEFORMAT(?String35{PROP:TEXT},@n2) + 1) NOT > MaxDays#
     ?String36{PROP:TEXT} = DEFORMAT(?String35{PROP:TEXT},@n2) + 1
  ?Wk6{PROP:Text} = BepalenWeek( DEFORMAT((?String36{PROP:TEXT} &'/' &MonthNumber &'/' &YearNumber),@D6) )
  ELSE
     ?String36{PROP:TEXT} = ''
  ?Wk6{PROP:Text} = ''
  END
  IF ?String36{PROP:TEXT} <> '' AND (DEFORMAT(?String36{PROP:TEXT},@n2) + 1) NOT > MaxDays#
     ?String37{PROP:TEXT} = DEFORMAT(?String36{PROP:TEXT},@n2) + 1
  ELSE
     ?String37{PROP:TEXT} = ''
  END
 !|-------------------------------------------------------------------------------------------
HighlightPassedDate ROUTINE
 !| Bevel the region representing the passed date
 !|
   CASE DAY(PassedDate)
   OF ?String1{PROP:TEXT}
      ?Region1{PROP:BEVEL} = -1
   OF ?String2{PROP:TEXT}
      ?Region2{PROP:BEVEL} = -1
   OF ?String3{PROP:TEXT}
      ?Region3{PROP:BEVEL} = -1
   OF ?String4{PROP:TEXT}
      ?Region4{PROP:BEVEL} = -1
   OF ?String5{PROP:TEXT}
      ?Region5{PROP:BEVEL} = -1
   OF ?String6{PROP:TEXT}
      ?Region6{PROP:BEVEL} = -1
   OF ?String7{PROP:TEXT}
      ?Region7{PROP:BEVEL} = -1
   OF ?String8{PROP:TEXT}
      ?Region8{PROP:BEVEL} = -1
   OF ?String9{PROP:TEXT}
      ?Region9{PROP:BEVEL} = -1
   OF ?String10{PROP:TEXT}
      ?Region10{PROP:BEVEL} = -1
   OF ?String11{PROP:TEXT}
      ?Region11{PROP:BEVEL} = -1
   OF ?String12{PROP:TEXT}
      ?Region12{PROP:BEVEL} = -1
   OF ?String13{PROP:TEXT}
      ?Region13{PROP:BEVEL} = -1
   OF ?String14{PROP:TEXT}
      ?Region14{PROP:BEVEL} = -1
   OF ?String15{PROP:TEXT}
      ?Region15{PROP:BEVEL} = -1
   OF ?String16{PROP:TEXT}
      ?Region16{PROP:BEVEL} = -1
   OF ?String17{PROP:TEXT}
      ?Region17{PROP:BEVEL} = -1
   OF ?String18{PROP:TEXT}
      ?Region18{PROP:BEVEL} = -1
   OF ?String19{PROP:TEXT}
      ?Region19{PROP:BEVEL} = -1
   OF ?String20{PROP:TEXT}
      ?Region20{PROP:BEVEL} = -1
   OF ?String21{PROP:TEXT}
      ?Region21{PROP:BEVEL} = -1
   OF ?String22{PROP:TEXT}
      ?Region22{PROP:BEVEL} = -1
   OF ?String23{PROP:TEXT}
      ?Region23{PROP:BEVEL} = -1
   OF ?String24{PROP:TEXT}
      ?Region24{PROP:BEVEL} = -1
   OF ?String25{PROP:TEXT}
      ?Region25{PROP:BEVEL} = -1
   OF ?String26{PROP:TEXT}
      ?Region26{PROP:BEVEL} = -1
   OF ?String27{PROP:TEXT}
      ?Region27{PROP:BEVEL} = -1
   OF ?String28{PROP:TEXT}
      ?Region28{PROP:BEVEL} = -1
   OF ?String29{PROP:TEXT}
      ?Region29{PROP:BEVEL} = -1
   OF ?String30{PROP:TEXT}
      ?Region30{PROP:BEVEL} = -1
   OF ?String31{PROP:TEXT}
      ?Region31{PROP:BEVEL} = -1
   OF ?String32{PROP:TEXT}
      ?Region32{PROP:BEVEL} = -1
   OF ?String33{PROP:TEXT}
      ?Region33{PROP:BEVEL} = -1
   OF ?String34{PROP:TEXT}
      ?Region34{PROP:BEVEL} = -1
   OF ?String35{PROP:TEXT}
      ?Region35{PROP:BEVEL} = -1
   OF ?String36{PROP:TEXT}
      ?Region36{PROP:BEVEL} = -1
   OF ?String37{PROP:TEXT}
      ?Region37{PROP:BEVEL} = -1
   END!CASE
 !|--------------------------------------------------------------------------------------------
ClearHighlight ROUTINE
 !| Clear any Bevels
 !|
   IF ?Region1{PROP:BEVEL} <> +1 THEN ?Region1{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region2{PROP:BEVEL} <> +1 THEN ?Region2{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region3{PROP:BEVEL} <> +1 THEN ?Region3{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region4{PROP:BEVEL} <> +1 THEN ?Region4{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region5{PROP:BEVEL} <> +1 THEN ?Region5{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region6{PROP:BEVEL} <> +1 THEN ?Region6{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region7{PROP:BEVEL} <> +1 THEN ?Region7{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region8{PROP:BEVEL} <> +1 THEN ?Region8{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region9{PROP:BEVEL} <> +1 THEN ?Region9{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region10{PROP:BEVEL} <> +1 THEN ?Region10{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region11{PROP:BEVEL} <> +1 THEN ?Region11{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region12{PROP:BEVEL} <> +1 THEN ?Region12{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region13{PROP:BEVEL} <> +1 THEN ?Region13{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region14{PROP:BEVEL} <> +1 THEN ?Region14{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region15{PROP:BEVEL} <> +1 THEN ?Region15{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region16{PROP:BEVEL} <> +1 THEN ?Region16{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region17{PROP:BEVEL} <> +1 THEN ?Region17{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region18{PROP:BEVEL} <> +1 THEN ?Region18{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region19{PROP:BEVEL} <> +1 THEN ?Region19{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region20{PROP:BEVEL} <> +1 THEN ?Region20{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region21{PROP:BEVEL} <> +1 THEN ?Region21{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region22{PROP:BEVEL} <> +1 THEN ?Region22{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region23{PROP:BEVEL} <> +1 THEN ?Region23{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region24{PROP:BEVEL} <> +1 THEN ?Region24{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region25{PROP:BEVEL} <> +1 THEN ?Region25{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region26{PROP:BEVEL} <> +1 THEN ?Region26{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region27{PROP:BEVEL} <> +1 THEN ?Region27{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region28{PROP:BEVEL} <> +1 THEN ?Region28{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region29{PROP:BEVEL} <> +1 THEN ?Region29{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region30{PROP:BEVEL} <> +1 THEN ?Region30{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region31{PROP:BEVEL} <> +1 THEN ?Region31{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region32{PROP:BEVEL} <> +1 THEN ?Region32{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region33{PROP:BEVEL} <> +1 THEN ?Region33{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region34{PROP:BEVEL} <> +1 THEN ?Region34{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region35{PROP:BEVEL} <> +1 THEN ?Region35{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region36{PROP:BEVEL} <> +1 THEN ?Region36{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
   IF ?Region37{PROP:BEVEL} <> +1 THEN ?Region37{PROP:BEVEL} = +1
      EXIT  !** Check EXIT allowed here **
   END
 !|---------------------------------------------------------======------------------
CheckRange ROUTINE
 !|Gives error message if year is out of range
 !|
 !Change year if month goes past 12 or before 1
  IF MonthNumber > 12
    MonthNumber = 1
    YearNumber +=1
  ELSIF MonthNumber < 1
    MonthNumber = 12
    YearNumber -=1
 END
 Display()
  IF    YearNumber < 1801
        YearNumber = 1801
        MESSAGE('De kalender is functioneel vanaf <13,10>'&|
        'het jaar 1801          ', |
        'Datum buiten bereik', Icon:Asterisk, |
        Button:Ok, Button:Ok, 0)
  ELSIF YearNumber > 2099
        YearNumber = 2099
        MESSAGE('De kalender is functioneel tot <13,10>'&|
        'het jaar 2099         ', |
        'Datum buiten bereik', Icon:Asterisk, |
        Button:Ok, Button:Ok, 0)
  END
 !|--------------------------------------------------------------------------------
SetBevels           ROUTINE
 !| Zet alle Bevels
 !|
   IF ?String1{PROP:TEXT} = ''
      ?Region1{PROP:BEVEL} = 0
   ELSE
      ?Region1{PROP:BEVEL} = +1
   END
   IF ?String2{PROP:TEXT} = ''
      ?Region2{PROP:BEVEL} = 0
   ELSE
      ?Region2{PROP:BEVEL} = +1
   END
   IF ?String3{PROP:TEXT} = ''
      ?Region3{PROP:BEVEL} = 0
   ELSE
      ?Region3{PROP:BEVEL} = +1
   END
   IF ?String4{PROP:TEXT} = ''
      ?Region4{PROP:BEVEL} = 0
   ELSE
      ?Region4{PROP:BEVEL} = +1
   END
   IF ?String5{PROP:TEXT} = ''
      ?Region5{PROP:BEVEL} = 0
   ELSE
      ?Region5{PROP:BEVEL} = +1
   END
   IF ?String6{PROP:TEXT} = ''
      ?Region6{PROP:BEVEL} = 0
   ELSE
      ?Region6{PROP:BEVEL} = +1
   END
   IF ?String7{PROP:TEXT} = ''
      ?Region7{PROP:BEVEL} = 0
   ELSE
      ?Region7{PROP:BEVEL} = +1
   END
   ?Region8{PROP:BEVEL} = +1
   ?Region9{PROP:BEVEL} = +1
   ?Region10{PROP:BEVEL} = +1
   ?Region11{PROP:BEVEL} = +1
   ?Region12{PROP:BEVEL} = +1
   ?Region13{PROP:BEVEL} = +1
   ?Region14{PROP:BEVEL} = +1
   ?Region15{PROP:BEVEL} = +1
   ?Region16{PROP:BEVEL} = +1
   ?Region17{PROP:BEVEL} = +1
   ?Region18{PROP:BEVEL} = +1
   ?Region19{PROP:BEVEL} = +1
   ?Region20{PROP:BEVEL} = +1
   ?Region21{PROP:BEVEL} = +1
   ?Region22{PROP:BEVEL} = +1
   ?Region23{PROP:BEVEL} = +1
   ?Region24{PROP:BEVEL} = +1
   ?Region25{PROP:BEVEL} = +1
   ?Region26{PROP:BEVEL} = +1
   ?Region27{PROP:BEVEL} = +1
   ?Region28{PROP:BEVEL} = +1
   IF ?String29{PROP:TEXT} = ''
      ?Region29{PROP:BEVEL} = 0
   ELSE
      ?Region29{PROP:BEVEL} = +1
   END
   IF ?String30{PROP:TEXT} = ''
      ?Region30{PROP:BEVEL} = 0
   ELSE
      ?Region30{PROP:BEVEL} = +1
   END
   IF ?String31{PROP:TEXT} = ''
      ?Region31{PROP:BEVEL} = 0
   ELSE
      ?Region31{PROP:BEVEL} = +1
   END
   IF ?String32{PROP:TEXT} = ''
      ?Region32{PROP:BEVEL} = 0
   ELSE
      ?Region32{PROP:BEVEL} = +1
   END
   IF ?String33{PROP:TEXT} = ''
      ?Region33{PROP:BEVEL} = 0
   ELSE
      ?Region33{PROP:BEVEL} = +1
   END
   IF ?String34{PROP:TEXT} = ''
      ?Region34{PROP:BEVEL} = 0
   ELSE
      ?Region34{PROP:BEVEL} = +1
   END
   IF ?String35{PROP:TEXT} = ''
      ?Region35{PROP:BEVEL} = 0
   ELSE
      ?Region35{PROP:BEVEL} = +1
   END
   IF ?String36{PROP:TEXT} = ''
      ?Region36{PROP:BEVEL} = 0
   ELSE
      ?Region36{PROP:BEVEL} = +1
   END
   IF ?String37{PROP:TEXT} = ''
      ?Region37{PROP:BEVEL} = 0
   ELSE
      ?Region37{PROP:BEVEL} = +1
 END
 
 If MonthNumber = Month( PassedDate ) AND YearNumber = Year( PassedDate )
  DO HighlightPassedDate
 End
  

DLLInitializer.Destruct PROCEDURE

  CODE
  FuzzyMatcher.Kill                                        ! Destroy fuzzy matcher
  LocalINIMgr.Kill                                         ! Kill local managers and assign NULL to global refernces
  INIMgr &= NULL                                           ! It is an error to reference these object after this point
  GlobalErrors &= NULL


