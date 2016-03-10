package com.eowise.recyclerview.stickyheaders.samples.data;

import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.AbstractDataProvider;
import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.NotificationType;

public  class ConcreteData2 extends AbstractDataProvider.Data {

    private final long mId;
    private final String mText;
    private final int mViewType;
    private boolean mPinned;
    private NotificationType notitype;

 public  ConcreteData2(long id, int viewType, String text, int swipeReaction,NotificationType notitype)
    {
        mId = id;
        mViewType = viewType;
        mText = makeText(id, text, swipeReaction);
        this.notitype=notitype;
    }

    private static String makeText(long id, String text, int swipeReaction)
    {
        final StringBuilder sb = new StringBuilder();

        sb.append(id);
        sb.append(" - ");
        sb.append(text);

        return sb.toString();
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
        return mText;
    }

    @Override
    public String getText() {
        return mText;
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