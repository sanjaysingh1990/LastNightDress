package com.eowise.recyclerview.stickyheaders.samples.data;

import java.io.Serializable;

/**
 * Created by sanjay on 12/6/2015.
 */
public class CommentData implements Serializable {
private String uname;


    public String getCommenttxxt() {
        return commenttxxt;
    }

    public void setCommenttxxt(String commenttxxt) {
        this.commenttxxt = commenttxxt;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    private String commenttxxt;
    private String profilepic;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    private long time;
}
