package com.eowise.recyclerview.stickyheaders.samples.data;

import java.io.Serializable;

/**
 * Created by sanjay on 12/9/2015.
 */
public class NotificationData implements Serializable

{
private String uname;
    private String postid;
    private String profilepicimg;
    private String imgurl;
    private String time;
    private String notification_id;
    private String receiverid;

    public String getSwapstatus() {
        return swapstatus;
    }

    public void setSwapstatus(String swapstatus) {
        this.swapstatus = swapstatus;
    }

    private String swapstatus;
    public String getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }



    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNotitype() {
        return notitype;
    }

    public void setNotitype(String notitype) {
        this.notitype = notitype;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getProfilepicimg() {
        return profilepicimg;
    }

    public void setProfilepicimg(String profilepicimg) {
        this.profilepicimg = profilepicimg;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    private String comment;
    private String notitype;

}
