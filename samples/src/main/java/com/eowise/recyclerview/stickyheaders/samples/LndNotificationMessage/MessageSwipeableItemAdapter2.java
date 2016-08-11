/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.NewMessage.SendMessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.Home_List_Data;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.Utils.RelativeTimeTextView;
import com.eowise.recyclerview.stickyheaders.samples.data.MessageData;
import com.eowise.recyclerview.stickyheaders.samples.data.RecyclerHeaderViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;

import github.ankushsachdeva.emojicon.EmojiconTextView;

class MessageSwipeableItemAdapter2
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements SwipeableItemAdapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MySwipeableItemAdapter";
    private static EventListener mEventListener;
    Activity con;
    public static final int MESSAGE = 1, BLANK = 2;

    // NOTE: Make accessible with short name
    private interface Swipeable extends SwipeableItemConstants {
    }

    public static AbstractDataProvider2 mProvider;
    private boolean mCanSwipeLeft;

    public static class Blank extends RecyclerHeaderViewHolder {


        public Blank(View v) {
            super(v);
        }


    }

    public class MyViewHolder extends AbstractSwipeableItemViewHolder implements View.OnClickListener {
        public PagerSwipeItemFrameLayout mContainer;
        public TextView uname;
        public EmojiconTextView message;
        LinearLayout fullmsg;
        public ImageView profilepic, messindicator;
        public RelativeTimeTextView time;

        public MyViewHolder(View v) {
            super(v);
            mContainer = (PagerSwipeItemFrameLayout) v.findViewById(R.id.container);
            uname = (TextView) v.findViewById(R.id.uname);
            message = (EmojiconTextView) v.findViewById(R.id.msg);
            profilepic = (ImageView) v.findViewById(R.id.profilepic);
            messindicator = (ImageView) v.findViewById(R.id.messidicator);
            time = (RelativeTimeTextView) v.findViewById(R.id.time);
            fullmsg = (LinearLayout) v.findViewById(R.id.fullmsg);
            fullmsg.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final AbstractDataProvider2.Data item = mProvider.getItem(getAdapterPosition());
            MessageData md = item.getMessage();
            Intent sendmsg = new Intent(con, SendMessageActivity.class);
            Home_List_Data hld=new Home_List_Data();
            hld.setUname(md.getUname());
            hld.setUserid(md.getSender_id());
            sendmsg.putExtra("msgstatus", md.getMsgindicator());
            sendmsg.putExtra("msgid", md.getMsgid());
            sendmsg.putExtra("bannerdata",hld);
            sendmsg.putExtra("pos", getAdapterPosition());
            md.setMsgindicator(1);
            MessageFragment.myItemAdapter.notifyDataSetChanged();
            Main_TabHost.activity.startActivityForResult(sendmsg, 200);

        }


        @Override
        public View getSwipeableContainerView() {
            return mContainer;
        }

        @Override
        public void onSlideAmountUpdated(float horizontalAmount, float verticalAmount, boolean isSwiping) {
            float alpha = 1.0f - Math.min(Math.max(Math.abs(horizontalAmount), 0.0f), 1.0f);
            ViewCompat.setAlpha(mContainer, alpha);
        }
    }

    public MessageSwipeableItemAdapter2(AbstractDataProvider2 dataProvider, boolean canSwipeLeft, Activity context) {
        mProvider = dataProvider;
        mCanSwipeLeft = canSwipeLeft;
        con = context;
        // SwipeableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case MESSAGE:
                final View v1 = inflater.inflate(R.layout.list_item_view_message, parent, false);
                viewHolder = new MyViewHolder(v1);
                break;
            case BLANK:
                final View blank = inflater.inflate(R.layout.blank_view_bottom, parent, false);
                viewHolder = new Blank(blank);


        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final AbstractDataProvider2.Data item = mProvider.getItem(position);


        switch (holder.getItemViewType()) {
            case MESSAGE:
                MessageData md = item.getMessage();
                ((MyViewHolder) holder).mContainer.setCanSwipeLeft(mCanSwipeLeft);
                ((MyViewHolder) holder).mContainer.setCanSwipeRight(!mCanSwipeLeft);
                ((MyViewHolder) holder).uname.setText(Capitalize.capitalizeFirstLetter(md.getUname()));
                ((MyViewHolder) holder).message.setText(md.getMessage());
                ((MyViewHolder) holder).time.setReferenceTime(md.getTimeago());
                SingleTon.imageLoader.displayImage(md.getProfilepic(), ((MyViewHolder) holder).profilepic, SingleTon.options3);
                if (md.getMsgindicator() == 0) {
                    ((MyViewHolder) holder).messindicator.setVisibility(View.VISIBLE);

                    ((MyViewHolder) holder).messindicator.setImageResource(R.drawable.color_icon);

                }
                    else
                    ((MyViewHolder) holder).messindicator.setVisibility(View.GONE);


                break;

        }

    }

    public interface EventListener {
        void onItemRemoved(int position);

        void onItemPinned(int position);

        void onItemViewClicked(View v, boolean pinned);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof MyViewHolder)
            ViewCompat.setAlpha(((MyViewHolder) holder).mContainer, 1.0f);
    }

    @Override
    public int getItemCount() {
        return mProvider.getCount();
    }

    @Override
    public int onGetSwipeReactionType(RecyclerView.ViewHolder holder, int position, int x, int y) {
        // NOTE: Need to specify REACTION_MASK_START_xxx flags to make ViewPager can handle touch event.
        if (mCanSwipeLeft) {
            return Swipeable.REACTION_CAN_SWIPE_LEFT |
                    Swipeable.REACTION_CAN_NOT_SWIPE_RIGHT_WITH_RUBBER_BAND_EFFECT |
                    Swipeable.REACTION_MASK_START_SWIPE_RIGHT;
        } else {
            return Swipeable.REACTION_CAN_SWIPE_RIGHT |
                    Swipeable.REACTION_CAN_NOT_SWIPE_LEFT_WITH_RUBBER_BAND_EFFECT |
                    Swipeable.REACTION_MASK_START_SWIPE_LEFT;
        }
    }

    @Override
    public long getItemId(int position) {
        return mProvider.getItem(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        if (mProvider.getItem(position).getMessageType() == MESSAGE) {
            return MESSAGE;
        } else if (mProvider.getItem(position).getMessageType() == BLANK) {
            return BLANK;
        }
        return -1;

    }

    @Override
    public void onSetSwipeBackground(RecyclerView.ViewHolder holder, int position, int type) {
    }

    @Override
    public SwipeResultAction onSwipeItem(RecyclerView.ViewHolder holder, final int position, int result) {
        //  Log.d(TAG, "onSwipeItem(position = " + position + ", result = " + result + ")");

        if (position == RecyclerView.NO_POSITION) {
            return null;
        }

        if ((mCanSwipeLeft && result == Swipeable.RESULT_SWIPED_LEFT) ||
                (!mCanSwipeLeft && result == Swipeable.RESULT_SWIPED_RIGHT)) {
            return new DismissResultAction(this, position);
        } else {
            return new DefaultResultAction(this, position);
        }
    }

    public static class DismissResultAction extends SwipeResultActionRemoveItem {
        public static MessageSwipeableItemAdapter2 mAdapter;
        private final int mPosition;

        DismissResultAction(MessageSwipeableItemAdapter2 adapter, int position) {
            mAdapter = adapter;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            mAdapter.mProvider.removeItem(mPosition);
            mAdapter.notifyItemRemoved(mPosition);

            if (mEventListener != null) {
                mEventListener.onItemRemoved(mPosition);
            }


        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    private static class DefaultResultAction extends SwipeResultActionDefault {

        private MessageSwipeableItemAdapter2 mAdapter;
        private final int mPosition;

        DefaultResultAction(MessageSwipeableItemAdapter2 adapter, int position) {
            mAdapter = adapter;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            AbstractDataProvider2.Data item = mAdapter.mProvider.getItem(mPosition);
            if (item.isPinned()) {
                item.setPinned(false);
                mAdapter.notifyItemChanged(mPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

}
