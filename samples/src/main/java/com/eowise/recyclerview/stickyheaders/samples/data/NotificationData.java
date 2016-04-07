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



    private long time;
    private String notification_id;
    private String senderid;
    private String message;
    private String notitype;
    private String swapstatus;


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }


    public String getSwappostids() {
        return swappostids;
    }

    public void setSwappostids(String swappostids) {
        this.swappostids = swappostids;
    }

    private String swappostids;

    public String getSwapstatus() {
        return swapstatus;
    }

    public void setSwapstatus(String swapstatus) {
        this.swapstatus = swapstatus;
    }


    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotitype() {
        return notitype;
    }

    public void setNotitype(String notitype) {
        this.notitype = notitype;
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


}
