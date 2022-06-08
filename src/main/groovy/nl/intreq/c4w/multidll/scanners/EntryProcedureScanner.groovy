/**
 * Extracts the procedures attached to a menu (sub)tree
 * from a txa file.
 */
package nl.intreq.c4w.multidll.scanners

import nl.intreq.c4w.txa.transform.SectionMark
import nl.intreq.c4w.txa.transform.TxaContentHandler
import nl.intreq.c4w.txa.transform.TxaContext
import nl.intreq.c4w.txa.transform.TxaLogicalContentHandler
import nl.intreq.c4w.txa.transform.TxaSectionHandler

import static nl.intreq.c4w.txa.transform.SectionMark.*

class EntryProcedureScanner implements TxaContentHandler, TxaSectionHandler, TxaLogicalContentHandler {

    final private static MAIN_PROCEDURE_DECLARATION = ~/^\s*PROCEDURE\s+(\w+)\s*$/

    //Eg: %ButtonProcedure DEPEND %Control PROCEDURE TIMES 3
    final private static buttonProcedurePattern =
      ~/^%ButtonProcedure\s+DEPEND\s+%Control\s+PROCEDURE\s+TIMES\s+([0-9]+)\s*$/

    //Eg: WHEN  ('?ITEM1') (SelPrtHistorieProductgroepPeriodeTotDebiteur)
    final private static controlActionPattern =
      ~/^\s*WHEN\s+\('(\?[\w]+)'\)\s+\(([\w]+)\)\s*$/

    //Eg: MENUBAR,USE(?MENUBAR1),#ORDINAL(1)
    final menubarPattern = /^\s*MENUBAR.*USE\((\?\w+)\).*#ORDINAL\(([0-9]+)\)\s*$/

    //Eg: MENU('Menu 1'),USE(?MENU1),#ORDINAL(1)
    final menuPattern = ~/^\s*MENU\('(.*)'\).*USE\((\?[\w]+)\).*#ORDINAL\(([0-9])+\)\s*$/

    //Eg: ITEM('item_1_1'),USE(?ITEM1),#ORDINAL(1)
    final menuItemPattern = ~/^\s*ITEM\('(.*)'\).*USE\((\?[\w]+)\).*#ORDINAL\(([0-9]+)\)\s*$/

    //Eg: END
    final menuEndPattern = ~/^\s*END\s*$/

    private menuActions = [:]

    /**
     * Map containing structure (parent/child relations) of the menu
     * Keys can be only MENU control identifiers (the control USE)
     * Values are both MENU and ITEM id's. A special key 'MENUBAR' is
     * used for top-level menu items (MENU and ITEM controls).
     * Eg:
     *  [
     *    'MENUBAR': [MENU1, ITEM1, ITEM2],
     *    MENU1: [ITEM3, ITEM4, MENU2, ITEM5],
     *    MENU2: [ITEM6]
     *  ]
     */
    public menuTree = [:]

    public menuRoot = null

    /* The procedure containing the MENUBAR */
    def procedureName = null

    /* Flags to enable/disable processing */
    boolean withinButtonProcedures = false

    /* Maintains the current level in the menu structure */
    def menuParents = ['MENUBAR']

    /**
     * Collect the entry procedures for the main application procedure
     */
    EntryProcedureScanner() {}

    /**
     * Collect the entry procedures attached the menu of a procedure
     * @param procedureName - name of the procedure to scan. The procedure
     * should contain a window with a MENUBAR control
     */
    EntryProcedureScanner(procedureName) {
        this.procedureName = procedureName
    }

    /**
     * Get the procedures attached to a specific menu or menu item entry
     * @param menuControl - use name (like ?MENU1 or ?ITEM1) of the root control entry
     * @param depth - integer specifying how deep the menu tree should be traversed. A
     *   value of 0 only returns the immediate children, -1 returns an exhaustive tree.
     *   Only applicable when the menuControl is a MENU control. For a menu ITEM only 0 or
     *   1 procedure is returned.
     * @return List of procedures names
     */
    List<String> entryProceduresFor(String menuControl, int depth=-1){
        // Use a set to ignore duplicates
        def menuItems = [] as Set<String>

        if ( menuTree.containsKey(menuControl)) {
            menuItems.addAll(collectMenuItems(menuControl, depth))
        } else if (!menuForItem(menuControl).isEmpty()) {
            menuItems << menuControl
        }

        menuItems.inject([]) {
            actions, itemId -> menuActions.containsKey(itemId) ? actions << menuActions[itemId] : actions
        } as List<String>
    }

    private menuForItem(menuControl){
        menuTree.findResults { menu, items -> items?.contains(menuControl) ? menu : null }
    }

    private Set<String> collectMenuItems(String menuControl, int depth=-1){
        Set<String> result = []

        result << menuTree[menuControl]?.findResults { menuTree.containsKey(it) ? null : it}
        def submenus = menuTree[menuControl]?.findResults { menuTree.containsKey(it) ? it : null}
        def subMenuItems = submenus.inject([]) {
            c, subMenu -> c << collectMenuItems(subMenu, depth-1)
        }
        result.addAll(subMenuItems)
        result.flatten()
    }

    @Override
    void onProcessingStart(TxaContext txaContext) {}

    @Override
    void onSectionStart(TxaContext txaContext, SectionMark sectionMark) {}

    @Override
    void onSectionContent(TxaContext ctx, SectionMark sectionMark, Long lineNumber, String content) {
        // If no procedure was set use the main procedure
        if ( this.procedureName == null && sectionMark == APPLICATION && content ==~ MAIN_PROCEDURE_DECLARATION ){
            (content =~ MAIN_PROCEDURE_DECLARATION).each { _, name ->
                this.procedureName = name
            }
        }

        // While inside the procedure scan it's contents
        if (this.procedureName && ctx.currentProcedureName == this.procedureName){
            processPrompts(ctx, content)
            processMenu(ctx, content)
        }
    }

    void processPrompts(TxaContext ctx, content){
        if (ctx.within(PROMPTS)){
            if (withinButtonProcedures){
                if ( content ==~ controlActionPattern){
                    (content =~ controlActionPattern).each {
                        _, controlUse, proc -> addOrUpdateMenuActions(controlUse,proc)
                    }
                } else if ( !content.trim().startsWith('WHEN')){
                    withinButtonProcedures = false
                }
            } else if ( content ==~ buttonProcedurePattern) {
                withinButtonProcedures = true
            }
        }
    }

    void processMenu(TxaContext ctx, String content){
        // Restrict processing to the [WINDOW] section where the menu should live
        if (ctx.within(WINDOW)){

            // Menubar control is always the top item of the menu tree
            // since items can be added to it directly
            if (content ==~ menubarPattern) {
                (content =~ menubarPattern).each {
                    _, id, ordinal ->
                    menuRoot = id
                    menuTree[id as String] = []
                    menuParents = [id]
                }
            }

            //... and needs to be present for subsequent processing
            if (!menuParents.isEmpty()){
                if (content ==~ menuPattern) {
                    (content =~ menuPattern).each {
                        _, label, id, ordinal ->
                            addOrUpdateMenuTree(menuParents?.last(), id)
                            menuParents << id
                    }
                } else if ( content ==~ menuItemPattern ) {
                    (content =~ menuItemPattern).each {
                        _,label,id,ordinal -> addOrUpdateMenuTree(menuParents?.last(),id)
                    }
                } else if ( content ==~ menuEndPattern ) {
                    if (!menuParents.isEmpty()) {
                        menuParents.removeLast()
                    }
                }
            }
        }
    }

    void addOrUpdateMenuActions(String controlUse, String procedure) {
        if (controlUse == null || controlUse.trim().isEmpty()) return
        if (procedure == null || procedure.trim().isEmpty()) return
        menuActions[controlUse] = procedure
    }

    void addOrUpdateMenuTree(String parent, String child){
        if ( child == null || child.trim().isEmpty()) return
        if ( parent == null || parent.trim().isEmpty()) return
        if ( !menuTree.containsKey(parent)){
            menuTree[parent] = [child]
        } else {
            menuTree[parent] << child
        }
    }

    @Override
    void onSectionEnd(TxaContext txaContext, SectionMark sectionMark) {}

    @Override
    void onProcessingFinished(TxaContext txaContext) {}

}
