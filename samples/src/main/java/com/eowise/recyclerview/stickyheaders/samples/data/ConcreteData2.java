package com.eowise.recyclerview.stickyheaders.samples.data;

import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.AbstractDataProvider;
import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.NotificationType;

public  class ConcreteData2 extends AbstractDataProvider.Data {

    private final long mId;
    private final String uname;
    private  String imageurl;
    private  String profilepic;

    private final int mViewType;
    private boolean mPinned;
    private NotificationType notitype;
    private NotificationData nd;

 public  ConcreteData2(long id, int viewType, NotificationData nd, int swipeReaction,NotificationType notitype)
    {
        mId = id;
        mViewType = viewType;
        this.uname=nd.getUname();
        this.imageurl=nd.getImgurl();
        this.profilepic=nd.getProfilepicimg();
        this.notitype=notitype;
        this.nd=nd;
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
    public String toString() {
        return uname;
    }

    @Override
    public String getUname() {
        return uname;
    }

    @Override
    public String getProfilepic() {
        return profilepic;
    }

    @Override
    public String getImageurl() {
        return imageurl;
    }

    @Override
    public NotificationData getNotificationdata() {
        return nd;
    }

    @Override
    public boolean isPinned() {
        return mPinned;
    }



    @Override
    public NotificationType getnotiType() {
        return notitype;
    }


    @Override
    public void setPinned(boolean pinned) {
        mPinned = pinned;
    }
}