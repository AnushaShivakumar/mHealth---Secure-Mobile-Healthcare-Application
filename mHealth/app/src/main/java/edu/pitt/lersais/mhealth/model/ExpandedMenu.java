package edu.pitt.lersais.mhealth.model;

/**
 * The menu item model in the left menu.
 *
 * @author Haobing Huang and Runhua Xu.
 */

public class ExpandedMenu {
    int menuNameId;
    int menuIconId;

    public ExpandedMenu(int nameId, int iconId) {
        this.menuNameId = nameId;
        this.menuIconId = iconId;
    }

    public int getMenuName() {
        return menuNameId;
    }

    public int getMenuIconId() {
        return menuIconId;
    }

    public void setMenuName(int _menuNameId) {
        this.menuNameId = _menuNameId;
    }

    public void setMenuIconId(int _menuIconId) {
        this.menuIconId = _menuIconId;
    }

}
