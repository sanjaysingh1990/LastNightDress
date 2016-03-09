package com.eowise.recyclerview.stickyheaders.samples.data;

/**
 * Created by sanjay on 12/30/2015.
 */
public class ShippingLabelData
{
private String packageweight;

    public String getPackageweight() {
        return packageweight;
    }

    public void setPackageweight(String packageweight) {
        this.packageweight = packageweight;
    }

    public String getPackageprice() {
        return packageprice;
    }

    public void setPackageprice(String packageprice) {
        this.packageprice = packageprice;
    }

    private String packageprice;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private boolean selected;
}
