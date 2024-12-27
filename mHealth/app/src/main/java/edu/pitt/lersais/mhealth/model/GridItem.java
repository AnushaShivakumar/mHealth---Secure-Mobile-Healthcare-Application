package edu.pitt.lersais.mhealth.model;


/**
 * The grid item model in the main window.
 *
 * @author Haobing Huang and Runhua Xu.
 */
public class GridItem {
    private int iId;
    private String iName;

    public GridItem() {
    }

    public GridItem(int iId, String iName) {
        this.iId = iId;
        this.iName = iName;
    }

    public int getiId() {
        return iId;
    }

    public String getiName() {

        return iName;
    }

    public void setiId(int iId) {

        this.iId = iId;
    }

    public void setiName(String iName) {

        this.iName = iName;
    }
}
