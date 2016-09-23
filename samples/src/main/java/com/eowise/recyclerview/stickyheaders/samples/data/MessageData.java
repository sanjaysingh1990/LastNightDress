package com.eowise.recyclerview.stickyheaders.samples.data;

/**
 * Created by sanjay on 12/8/2015.
 */
public class MessageData {
    private String uname;
    private String message;
    private int type;
    private String brandname;
    private String Sellername;
    private String size;
    private String color;
    private String price;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    private int status;
    private int msg_id;
    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    private String postid;

    public UserMessageType getUserMessageType() {
        return userMessageType;
    }

    public void setUserMessageType(UserMessageType userMessageType) {
        this.userMessageType = userMessageType;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public String getSellername() {
        return Sellername;
    }

    public void setSellername(String sellername) {
        Sellername = sellername;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public int getMsgid() {
        return msgid;
    }

    public void setMsgid(int msgid) {
        this.msgid = msgid;
    }

    public String getShared_imgage_url() {
        return shared_imgage_url;
    }

    public void setShared_imgage_url(String shared_imgage_url) {
        this.shared_imgage_url = shared_imgage_url;
    }

    public long getTimeago() {
        return timeago;
    }

    public void setTimeago(long timeago) {
        this.timeago = timeago;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMsgindicator() {
        return msgindicator;
    }

    public void setMsgindicator(int msgindicator) {
        this.msgindicator = msgindicator;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    private String imageurl;
    private int msgid;
    private String shared_imgage_url;
    private long timeago;
    private UserMessageType userMessageType;
    private String profilepic;
    private String time;
    private int msgindicator;
    private String sender_id;
    private String datetime;
}
