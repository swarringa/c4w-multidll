/**
 * Extracts the procedures attached to a menu (sub)tree
 * from a txa file.
 */
package nl.practicom.c4w.multidll

import nl.practicom.c4w.txa.transform.SectionMark
import nl.practicom.c4w.txa.transform.TxaContentHandler
import nl.practicom.c4w.txa.transform.TxaContext

import static nl.practicom.c4w.txa.transform.SectionMark.*

class EntryProcedureScanner implements TxaContentHandler {

    private menuActions = [:]

    /**
     * Map containing structure (parent/child relations) of the menu
     * Keys can be only MENU control identifiers (the control USE)
     * Values are both MENU and ITEM id's. A special key 'window' is
     * used for top-level menu items (MENU and ITEM controls).
     * Eg:
     *  [
     *    window: [MENU1, ITEM1, ITEM2],
     *    MENU1: [ITEM3, ITEM4, MENU2, ITEM5],
     *    MENU2: [ITEM6]
     *  ]
     */
    private menuTree = [:]

    /* The procedure containing the MENUBAR */
    def procedureName

    /* Flags to enable/disable processing */
    boolean withinButtonProcedures = false

    /* Maintains the current level in the menu structure */
    def menuParents = ['MENUBAR']

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
    void onSectionContent(TxaContext ctx, SectionMark sectionMark, String s) {
        if (ctx.currentProcedureName == this.procedureName){
            processPrompts(ctx)
            processMenu(ctx)
        }
    }

    void processPrompts(TxaContext ctx){

        //Eg: %ButtonProcedure DEPEND %Control PROCEDURE TIMES 3
        final buttonProcedurePattern =
                ~/^%ButtonProcedure\s+DEPEND\s+%Control\s+PROCEDURE\s+TIMES\s+([0-9]+)\s*$/

        //Eg: WHEN  ('?ITEM1') (SelPrtHistorieProductgroepPeriodeTotDebiteur)
        final controlActionPattern =
                ~/^\s*WHEN\s+\('(\?[\w]+)'\)\s+\(([\w]+)\)\s*$/

        if (ctx.within(PROMPTS)){
            if (withinButtonProcedures){
                if ( ctx.currentLine ==~ controlActionPattern){
                    (ctx.currentLine =~ controlActionPattern).each {
                        _, controlUse, proc -> addOrUpdateMenuActions(controlUse,proc)
                    }
                }
            } else if (ctx.currentLine.trim().size() == 0) {
                withinButtonProcedures = false
            } else if ( ctx.currentLine ==~ buttonProcedurePattern) {
                withinButtonProcedures = true
            }
        }
    }

    void processMenu(TxaContext ctx){

        //Eg: MENUBAR,USE(?MENUBAR1),#ORDINAL(1)
        final menubarPattern = /^\s*MENUBAR.*USE\((\?\w+)\).*#ORDINAL\(([0-9]+)\)\s*$/

        //Eg: MENU('Menu 1'),USE(?MENU1),#ORDINAL(1)
        final menuPattern = ~/^\s*MENU\('(.*)'\).*USE\((\?[\w]+)\).*#ORDINAL\(([0-9])+\)\s*$/

        //Eg: ITEM('item_1_1'),USE(?ITEM1),#ORDINAL(1)
        final menuItemPattern = ~/^\s*ITEM\('(.*)'\).*USE\((\?[\w]+)\).*#ORDINAL\(([0-9]+)\)\s*$/

        //Eg: END
        final menuEndPattern = ~/^\s*END\s*$/

        // Restrict processing to the [WINDOW] section where the menu should live
        if (ctx.within(WINDOW)){

            // Menubar control is always the top item of the menu tree
            // since items can be added to it directly
            if (ctx.currentLine ==~ menubarPattern) {
                (ctx.currentLine =~ menubarPattern).each {
                    _, id, ordinal ->
                    menuTree[id as String] = []
                    menuParents = [id]
                }
            }

            //... and needs to be present for subsequent processing
            if (!menuParents.isEmpty()){
                if (ctx.currentLine ==~ menuPattern) {
                    (ctx.currentLine =~ menuPattern).each {
                        _, label, id, ordinal ->
                            addOrUpdateMenuTree(menuParents?.last(), id)
                            menuParents << id
                    }
                } else if ( ctx.currentLine ==~ menuItemPattern ) {
                    (ctx.currentLine =~ menuItemPattern).each {
                        _,label,id,ordinal -> addOrUpdateMenuTree(menuParents?.last(),id)
                    }
                } else if ( ctx.currentLine ==~ menuEndPattern ) {
                    if (!menuParents.isEmpty()) {
                        menuParents.pop()
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
