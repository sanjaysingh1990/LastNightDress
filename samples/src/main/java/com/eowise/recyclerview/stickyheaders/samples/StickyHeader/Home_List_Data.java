package com.eowise.recyclerview.stickyheaders.samples.StickyHeader;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sanjay on 2/11/2016.
 */
public class Home_List_Data implements Serializable {
    public int sectionManager;
    public int sectionFirstPosition;
    public String sectiontype;
    public String text;
    public boolean isprivate = false;
    private String uname;
    private String brandname;
    private String pricewas;
    private String pricenow;
    private String likestotal;
    private String conditon;

    public boolean isvisible() {
        return isvisible;
    }

    public void setIsvisible(boolean isvisible) {
        this.isvisible = isvisible;
    }

    private boolean isvisible;
    public String getProdtype() {
        return prodtype;
    }

    public void setProdtype(String prodtype) {
        this.prodtype = prodtype;
    }

    private String prodtype;
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    private String userid;
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;
    private String size;

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    private String colors;

    public String getLikedvalue() {
        return likedvalue;
    }

    public void setLikedvalue(String likedvalue) {
        this.likedvalue = likedvalue;
    }

    private String likedvalue;




    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;
    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    private String post_id;

    public ArrayList<String> getImageurls() {
        return imageurls;
    }

    public void setImageurls(ArrayList<String> imageurls) {
        this.imageurls = imageurls;
    }

    private ArrayList<String> imageurls;

    public String getProfilepicurl() {
        return profilepicurl;
    }

    public void setProfilepicurl(String profilepicurl) {
        this.profilepicurl = profilepicurl;
    }

    private String profilepicurl;

    public Home_List_Data(String text, boolean isprivate, String sectiontype, int sectionManager, int sectionFirstPosition) {
        this.sectiontype = sectiontype;
        this.isprivate = isprivate;
        this.text = text;
        this.sectionManager = sectionManager;
        this.sectionFirstPosition = sectionFirstPosition;
    }

    public Home_List_Data(String text, String sectiontype, int sectionManager,
                          int sectionFirstPosition) {
        this.sectiontype = sectiontype;

        this.text = text;
        this.sectionManager = sectionManager;
        this.sectionFirstPosition = sectionFirstPosition;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public String getPricewas() {
        return pricewas;
    }

    public void setPricewas(String pricewas) {
        this.pricewas = pricewas;
    }

    public String getPricenow() {
        return pricenow;
    }

    public void setPricenow(String pricenow) {
        this.pricenow = pricenow;
    }

    public String getLikestotal() {
        return likestotal;
    }

    public void setLikestotal(String likestotal) {
        this.likestotal = likestotal;
    }

    public String getConditon() {
        return conditon;
    }

    public void setConditon(String conditon) {
        this.conditon = conditon;
    }




}
