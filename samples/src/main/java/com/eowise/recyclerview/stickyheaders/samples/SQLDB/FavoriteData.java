package com.eowise.recyclerview.stickyheaders.samples.SQLDB;

public class FavoriteData {
private String postid;
    private String imageurl;
    private String cost;

    public FavoriteData()
    {

    }
    public FavoriteData(String postid, String imageurl, String cost)
    {
        this.postid=postid;
        this.imageurl=imageurl;
        this.cost=cost;
    }
    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }


}