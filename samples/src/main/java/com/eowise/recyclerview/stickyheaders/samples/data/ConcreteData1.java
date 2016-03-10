package com.eowise.recyclerview.stickyheaders.samples.data;

import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.AbstractDataProvider2;

public class ConcreteData1 extends AbstractDataProvider2.Data {

    private final long mId;
    private final String uname;
    private final String message;
    private  int status;
    private final int mViewType;
    private boolean mPinned;
    private int msgtype;
    private String url;
    private String datetime;
    private String senderid;
    public int msgid;
    public ConcreteData1(long id, int viewType, String uname, String message, String url, String datetime, String senderid,int msgid, int status) {
        mId = id;
        mViewType = viewType;
        this.uname = uname;
        this.message = message;
        this.url = url;
        this.datetime = datetime;
        this.status = status;
        this.msgtype =1;
        this.senderid = senderid;
        this.msgid=msgid;

    }


    @Override
    public boolean isSectionHeader() {
        return false;
    }

    @Override
    public int getViewType() {
        return mViewType;
    }

    @Override
    public String username() {
        return uname;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public String profilePic() {
        return url;
    }

    @Override
    public String dateTime() {
        return datetime;
    }

    @Override
    public int msgid() {
        return msgid;
    }

    @Override
    public void changestatus() {
        status=1;
    }

    @Override
    public String senderid() {
      return  senderid;
    }

    @Override
    public int msgstatus() {
        return status;
    }

    @Override
    public long getId() {
        return mId;
    }


    @Override
    public String toString() {
        return uname;
    }


    @Override
    public boolean isPinned() {
        return mPinned;
    }

    @Override
    public int getMessageType() {
        return msgtype;
    }


    @Override
    public void setPinned(boolean pinned) {
        mPinned = pinned;
    }
}