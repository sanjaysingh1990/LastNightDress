package com.eowise.recyclerview.stickyheaders.samples.data;

import java.io.Serializable;

/**
 * Created by sanju on 1/3/16.
 */
public class Chat_Banner_Data implements Serializable {
    private String image_url;
    private String brand;
    private String sellername;
    private String size;
    private String pricenow;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPricenow() {
        return pricenow;
    }

    public void setPricenow(String pricenow) {
        this.pricenow = pricenow;
    }

}
