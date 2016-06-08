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
    private int likestotal;
    private String conditon;

    public ArrayList<CommentBean> getUserpostcomments() {
        return userpostcomments;
    }

    public void setUserpostcomments(ArrayList<CommentBean> userpostcomments) {
        this.userpostcomments = userpostcomments;
    }

    private ArrayList<CommentBean> userpostcomments;
    private static final long serialVersionUID = 1L;
    public int getTotalcomments() {
        return totalcomments;
    }

    public void setTotalcomments(int totalcomments) {
        this.totalcomments = totalcomments;
    }

    private int totalcomments;


    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    private String comments;

    public int getSwapstatus() {
        return swapstatus;
    }

    public void setSwapstatus(int swapstatus) {
        this.swapstatus = swapstatus;
    }

    private int swapstatus;
    public int getIssold() {
        return issold;
    }

    public void setIssold(int issold) {
        this.issold = issold;
    }

    private int issold;

    public String getNotilikedby() {
        return notilikedby;
    }

    public void setNotilikedby(String notilikedby) {
        this.notilikedby = notilikedby;
    }

    private String notilikedby;

    public int getNotitotallikers() {
        return notitotallikers;
    }

    public void setNotitotallikers(int notitotallikers) {
        this.notitotallikers = notitotallikers;
    }

    private int notitotallikers;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    private long time;

    public int getHeadertype() {
        return headertype;
    }

    public void setHeadertype(int headertype) {
        this.headertype = headertype;
    }

    private int headertype;

    public boolean isfavorate() {
        return isfavorate;
    }

    public void setIsfavorate(boolean isfavorate) {
        this.isfavorate = isfavorate;
    }

    private boolean isfavorate;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    private int category;


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
public Home_List_Data()
{

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

    public int getLikestotal() {
        return likestotal;
    }

    public void setLikestotal(int likestotal) {
        this.likestotal = likestotal;
    }

    public String getConditon() {
        return conditon;
    }

    public void setConditon(String conditon) {
        this.conditon = conditon;
    }


}
