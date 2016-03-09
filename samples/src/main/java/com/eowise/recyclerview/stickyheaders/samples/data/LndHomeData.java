package com.eowise.recyclerview.stickyheaders.samples.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanjay on 11/29/2015.
 */
public class LndHomeData implements Serializable
{

    private List<String> images=new ArrayList<String>();
    private String imgurl;

    public boolean isFavorate() {
        return favorate;
    }

    public void setFavorate(boolean favorate) {
        this.favorate = favorate;
    }

    private boolean favorate;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    private String uname;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String desc;


    public String getCommentstotal() {
        return commentstotal;
    }

    public void setCommentstotal(String commentstotal) {
        this.commentstotal = commentstotal;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    private String commentstotal,likes;

    public String getLen() {
        return len;
    }

    public void setLen(String len) {
        this.len = len;
    }

    private String len;
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

    private String pricewas,pricenow;

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    private String postid;

    public List<CommentData> getComments() {
        return comments;
    }

    public void setComments(List<CommentData> comments) {
        this.comments = comments;
    }

    private List<CommentData> comments=new ArrayList<CommentData>();

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }




    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

}
