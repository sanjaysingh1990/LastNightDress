package com.eowise.recyclerview.stickyheaders.samples.data;

/**
 * Created by sanjay on 11/29/2015.
 */
public class ShopData {
    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }


    public boolean isItemchecked() {
        return itemchecked;
    }

    public void setItemchecked(boolean itemchecked) {
        this.itemchecked = itemchecked;
    }
    private String imageurl;
    private String price;
    private String postid;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    private String uname;

    private boolean itemchecked;
}
