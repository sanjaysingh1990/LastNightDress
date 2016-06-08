package com.eowise.recyclerview.stickyheaders.samples.StickyHeader;

import java.io.Serializable;

/**
 * Created by INIT on 6/8/2016.
 */
public class CommentBean implements Serializable {
    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private String uname;
private String comment;
}
