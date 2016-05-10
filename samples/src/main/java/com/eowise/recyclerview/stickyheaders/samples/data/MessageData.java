package com.eowise.recyclerview.stickyheaders.samples.data;

/**
 * Created by sanjay on 12/8/2015.
 */
public class MessageData
{
private String uname;
    private String message;
    private int type;

    public String getCurrenttimestamp() {
        return currenttimestamp;
    }

    public void setCurrenttimestamp(String currenttimestamp) {
        this.currenttimestamp = currenttimestamp;
    }

    private String currenttimestamp;
    public String getBase64_imgage_url() {
        return base64_imgage_url;
    }

    public void setBase64_imgage_url(String base64_imgage_url) {
        this.base64_imgage_url = base64_imgage_url;
    }

    private String base64_imgage_url;
    public long getTimeago() {
        return timeago;
    }

    public void setTimeago(long timeago) {
        this.timeago = timeago;
    }

    private long timeago;
    public int getMsgid() {
        return msgid;
    }

    public void setMsgid(int msgid) {
        this.msgid = msgid;
    }

    private int msgid;

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    private UserType userType;

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
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

    private String profilepic;
    private String time;
    private int msgindicator;

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

    private String sender_id;
    private String datetime;
}
