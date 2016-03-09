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
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.samples.Purchase.ShippingAddressActivity;
import com.eowise.recyclerview.stickyheaders.samples.Purchase.SwapCheckOutActivity;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.data.RecyclerHeaderViewHolder;
import com.eowise.recyclerview.stickyheaders.samples.interfaces.TagClick;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;


class NotificationSwipeableItemAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements SwipeableItemAdapter<RecyclerView.ViewHolder>, TagClick {
    private static final String TAG = "MySwipeableItemAdapter";
    private static EventListener mEventListener;
    static Activity activity;
    // NOTE: Make accessible with short name
    public static final int FOLLOWER = 1, SWAPREQUEST = 3, CHECKOUT = 4, DECLINED = 6, USERMETION = 7, PURCHASEDITEM = 8, POSTSHARED = 9, BLANK = 10;
    TagSelectingTextview mTagSelectingTextview;
    public static int hashTagHyperLinkEnabled = 1;
    public static int hashTagHyperLinkDisabled = 0;
    String hastTagColorBlue = "#be4d66";

    @Override
    public void clickedTag(CharSequence tag) {
        Toast.makeText(activity, "Clicked on " + tag, 1).show();
    }


    private interface Swipeable extends SwipeableItemConstants {
    }

    public interface EventListener {
        void onItemRemovedNotification(int position);


    }

    private AbstractDataProvider mProvider;
    private boolean mCanSwipeLeft;

    public static class MyViewHolder extends AbstractSwipeableItemViewHolder {
        public PagerSwipeItemFrameLayout mContainer;
        public TextView notiTextView;

        public MyViewHolder(View v) {
            super(v);
            mContainer = (PagerSwipeItemFrameLayout) v.findViewById(R.id.container);
            notiTextView = (TextView) v.findViewById(R.id.notiinfotext);
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

    public static class Follower extends AbstractSwipeableItemViewHolder {
        public PagerSwipeItemFrameLayout mContainer;
        public TextView notiTextView;

        public Follower(View v) {
            super(v);
            mContainer = (PagerSwipeItemFrameLayout) v.findViewById(R.id.container);
            notiTextView = (TextView) v.findViewById(R.id.notiinfotext);
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

    public static class SwapRequest extends AbstractSwipeableItemViewHolder {
        public PagerSwipeItemFrameLayout mContainer;
        public TextView notiTextView;

        public SwapRequest(View v) {
            super(v);
            mContainer = (PagerSwipeItemFrameLayout) v.findViewById(R.id.container);
            notiTextView = (TextView) v.findViewById(R.id.notiinfotext);
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

    public static class Blank extends RecyclerHeaderViewHolder {


        public Blank(View v) {
            super(v);
        }


    }


    public static class CheckOut extends AbstractSwipeableItemViewHolder implements View.OnClickListener {
        public PagerSwipeItemFrameLayout mContainer;
        public TextView notiTextView;
        public TextView swapcheckout;

        public CheckOut(View v) {
            super(v);
            mContainer = (PagerSwipeItemFrameLayout) v.findViewById(R.id.container);
            notiTextView = (TextView) v.findViewById(R.id.notiinfotext);
            swapcheckout = (TextView) v.findViewById(R.id.swapcheckout);
            swapcheckout.setOnClickListener(this);
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

        @Override
        public void onClick(View view) {
            Intent checkout = new Intent(activity, ShippingAddressActivity.class);
            checkout.putExtra("data","buy");
            activity.startActivity(checkout);
        }
    }


    public NotificationSwipeableItemAdapter(AbstractDataProvider dataProvider, boolean canSwipeLeft, Activity act) {
        mProvider = dataProvider;
        mCanSwipeLeft = canSwipeLeft;
        activity = act;
        // SwipeableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
        mTagSelectingTextview = new TagSelectingTextview();
    }

    @Override
    public long getItemId(int position) {
        return mProvider.getItem(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        if (mProvider.getItem(position).getnotiType() == NotificationType.FOLLOWING) {
            return FOLLOWER;
        } else if (mProvider.getItem(position).getnotiType() == NotificationType.SWAPREQUEST) {
            return SWAPREQUEST;
        } else if (mProvider.getItem(position).getnotiType() == NotificationType.CHECKOUT) {
            return CHECKOUT;

        }  else if (mProvider.getItem(position).getnotiType() == NotificationType.DECNIED) {
            return DECLINED;
        } else if (mProvider.getItem(position).getnotiType() == NotificationType.USERMENTION) {
            return USERMETION;
        } else if (mProvider.getItem(position).getnotiType() == NotificationType.PURCHASEDITEM) {
            return PURCHASEDITEM;
        } else if (mProvider.getItem(position).getnotiType() == NotificationType.POSTSHARED) {
            return POSTSHARED;
        } else if (mProvider.getItem(position).getnotiType() == NotificationType.BLANK) {
            return BLANK;
        }
        return -1;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {

            case DECLINED:
                final View declined = inflater.inflate(R.layout.list_item_common_view, parent, false);
                viewHolder = new MyViewHolder(declined);
                break;
            case USERMETION:
                final View usermetion = inflater.inflate(R.layout.list_item_common_view, parent, false);
                viewHolder = new MyViewHolder(usermetion);
                break;
            case PURCHASEDITEM:
                final View purchaseditem = inflater.inflate(R.layout.list_item_common_view, parent, false);
                viewHolder = new MyViewHolder(purchaseditem);
                break;
            case POSTSHARED:
                final View postshared = inflater.inflate(R.layout.list_item_common_view, parent, false);
                viewHolder = new MyViewHolder(postshared);
                break;

            case FOLLOWER:
                final View v2 = inflater.inflate(R.layout.list_item_following, parent, false);
                viewHolder = new Follower(v2);
                break;
            case SWAPREQUEST:
                final View swaprequest = inflater.inflate(R.layout.list_item_swaprequest, parent, false);
                viewHolder = new SwapRequest(swaprequest);
                break;
            case CHECKOUT:
                final View checkout = inflater.inflate(R.layout.list_item_swapcheckout, parent, false);
                viewHolder = new CheckOut(checkout);
                break;
            case BLANK:
                final View blank = inflater.inflate(R.layout.blank_view_bottom, parent, false);
                viewHolder = new Blank(blank);


        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final AbstractDataProvider.Data item = mProvider.getItem(position);
        switch (holder.getItemViewType()) {


            case DECLINED:
                setSpannableText("Cindy Lowe ", "declined your swap request. ", " 2m", ((MyViewHolder) holder).notiTextView);

                ((MyViewHolder) holder).mContainer.setCanSwipeLeft(mCanSwipeLeft);
                ((MyViewHolder) holder).mContainer.setCanSwipeRight(!mCanSwipeLeft);
                break;

            case USERMETION:
                //setSpannableText("Cindy Lowe ", "mentioned you in a post. @Jakie @Oliva i know you always wanted this #beautiful and #nice #dress.  ", " 2m", ((MyViewHolder) holder).notiTextView);
                String uname = "Cindy Lowe ";


                ((MyViewHolder) holder).notiTextView.setMovementMethod(LinkMovementMethod.getInstance());
                ((MyViewHolder) holder).notiTextView.setTextColor(Color.parseColor("#000000"));
                ((MyViewHolder) holder).notiTextView.setText(mTagSelectingTextview.addClickablePart(uname + " mentioned you in a post. @Jakie @Oliva i know you always wanted this #beautiful and #nice #dress",
                                this, hashTagHyperLinkDisabled, hastTagColorBlue, uname.length()),
                        TextView.BufferType.SPANNABLE);

                ((MyViewHolder) holder).mContainer.setCanSwipeLeft(mCanSwipeLeft);
                ((MyViewHolder) holder).mContainer.setCanSwipeRight(!mCanSwipeLeft);
                break;
            case PURCHASEDITEM:
                setSpannableText("Cindy Lowe ", "shared this post with you: Hey, check this out i think this is perfect for you.  ", " 1w", ((MyViewHolder) holder).notiTextView);

                ((MyViewHolder) holder).mContainer.setCanSwipeLeft(mCanSwipeLeft);
                ((MyViewHolder) holder).mContainer.setCanSwipeRight(!mCanSwipeLeft);
                break;

            case POSTSHARED:
                setSpannableText("Cindy Lowe ", "purchased your item, mail it now.  ", " 1w", ((MyViewHolder) holder).notiTextView);

                ((MyViewHolder) holder).mContainer.setCanSwipeLeft(mCanSwipeLeft);
                ((MyViewHolder) holder).mContainer.setCanSwipeRight(!mCanSwipeLeft);
                break;
            case FOLLOWER:
                setSpannableText("Cindy Lowe ", "started following you. ", " 2m", ((Follower) holder).notiTextView);
                ((Follower) holder).mContainer.setCanSwipeLeft(mCanSwipeLeft);
                ((Follower) holder).mContainer.setCanSwipeRight(!mCanSwipeLeft);
                break;
            case SWAPREQUEST:
                setSpannableText2("Cindy Lowe ", "requested a   ", " 2m", ((SwapRequest) holder).notiTextView);

                ((SwapRequest) holder).mContainer.setCanSwipeLeft(mCanSwipeLeft);
                ((SwapRequest) holder).mContainer.setCanSwipeRight(!mCanSwipeLeft);
                break;

            case CHECKOUT:

                setSpannableText("Cindy Lowe ", "accepted swap request.  ", " 2m", ((CheckOut) holder).notiTextView);
                ((CheckOut) holder).mContainer.setCanSwipeLeft(mCanSwipeLeft);
                ((CheckOut) holder).mContainer.setCanSwipeRight(!mCanSwipeLeft);

        }
        //  holder.mTextView.setText(item.getText());
    }

    public class LoremIpsumSpan extends ClickableSpan {
        String uname;

        public LoremIpsumSpan(String str) {
            uname = str;
        }

        @Override
        public void onClick(View widget) {
            Toast.makeText(activity, "clicked" + uname, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

    private void setSpannableText(String uname, String msg, String time, TextView txt) {

        //username spannable
        Spannable un = new SpannableString(uname);

        un.setSpan(new LoremIpsumSpan(uname), 0, uname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        un.setSpan(new ForegroundColorSpan(Color.parseColor("#be4d66")), 0, uname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        un.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, uname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txt.setText(un);
        txt.setMovementMethod(LinkMovementMethod.getInstance());


        //message spannable
        Spannable notimsg = new SpannableString(msg);
        notimsg.setSpan(new ForegroundColorSpan(Color.BLACK), 0, notimsg.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt.append(notimsg);
        //spannable timing
        Spannable tme = new SpannableString(time);
        tme.setSpan(new ForegroundColorSpan(Color.parseColor("#dadada")), 0, time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tme.setSpan(new RelativeSizeSpan(0.8f), 0, time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt.append(tme);

    }

    private void setSpannableText2(String uname, String msg, String time, TextView txt) {
        //username spannable
        Spannable un = new SpannableString(uname);
        un.setSpan(new LoremIpsumSpan(uname), 0, uname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        un.setSpan(new ForegroundColorSpan(Color.parseColor("#be4d66")), 0, uname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        un.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, uname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txt.setText(un);
        txt.setMovementMethod(LinkMovementMethod.getInstance());

        //message spannable
        Spannable notimsg = new SpannableString(msg);
        notimsg.setSpan(new ForegroundColorSpan(Color.BLACK), 0, notimsg.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt.append(notimsg);

        //swap request
        Spannable swaptext = new SpannableString(" SWAP  ");
        swaptext.setSpan(new ForegroundColorSpan(Color.parseColor("#be4d66")), 0, swaptext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        swaptext.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, swaptext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt.append(swaptext);

        //spannable timing
        Spannable tme = new SpannableString(time);
        tme.setSpan(new ForegroundColorSpan(Color.parseColor("#dadada")), 0, time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tme.setSpan(new RelativeSizeSpan(0.8f), 0, time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt.append(tme);

    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof MyViewHolder)
            ViewCompat.setAlpha(((MyViewHolder) holder).mContainer, 1.0f);
        else if (holder instanceof Follower)
            ViewCompat.setAlpha(((Follower) holder).mContainer, 1.0f);
        else if (holder instanceof SwapRequest)
            ViewCompat.setAlpha(((SwapRequest) holder).mContainer, 1.0f);
        else if (holder instanceof CheckOut)
            ViewCompat.setAlpha(((CheckOut) holder).mContainer, 1.0f);
        //else
          //  ViewCompat.setAlpha(((Bl) holder).mContainer, 1.0f);


    }

    @Override
    public int getItemCount() {
        return mProvider.getCount();
    }

    @Override
    public int onGetSwipeReactionType(RecyclerView.ViewHolder holder, int position, int x, int y) {
        // NOTE: Need to specify REACTION_MASK_START_xxx flags to make ViewPager can handle touch event.
        if (holder instanceof SwapRequest)
            return 0;
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
    public void onSetSwipeBackground(RecyclerView.ViewHolder holder, int position, int type) {
    }

    @Override
    public SwipeResultAction onSwipeItem(RecyclerView.ViewHolder holder, final int position, int result) {
        Log.d(TAG, "onSwipeItem(position = " + position + ", result = " + result + ")");

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

    private static class DismissResultAction extends SwipeResultActionRemoveItem {
        private NotificationSwipeableItemAdapter mAdapter;
        private final int mPosition;

        DismissResultAction(NotificationSwipeableItemAdapter adapter, int position) {
            mAdapter = adapter;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            mAdapter.mProvider.removeItem(mPosition);
            mAdapter.notifyItemRemoved(mPosition);
            if (mEventListener != null) {
                mEventListener.onItemRemovedNotification(mPosition);
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

        private NotificationSwipeableItemAdapter mAdapter;
        private final int mPosition;

        DefaultResultAction(NotificationSwipeableItemAdapter adapter, int position) {
            mAdapter = adapter;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            AbstractDataProvider.Data item = mAdapter.mProvider.getItem(mPosition);
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
