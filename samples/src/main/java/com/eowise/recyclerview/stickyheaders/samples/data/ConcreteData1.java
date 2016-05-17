package com.eowise.recyclerview.stickyheaders.samples.data;

import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.AbstractDataProvider2;

public class ConcreteData1 extends AbstractDataProvider2.Data {

    private final long mId;


    private final int mViewType;
    private boolean mPinned;
    private int msgtype;
   private MessageData md;
    private String datetime;
    public ConcreteData1(long id, int viewType,MessageData md,String datetime) {
        mId = id;
        mViewType = viewType;
        this.msgtype =1;
        this.md=md;
        this.datetime=datetime;

    }

   public String getDatetime()
   {
       return datetime;
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
    public long getId() {
        return mId;
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
    public MessageData getMessage() {
        return md;
    }


    @Override
    public void setPinned(boolean pinned) {
        mPinned = pinned;
    }
}