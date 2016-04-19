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

package com.eowise.recyclerview.stickyheaders.samples.LndAgent;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.AbstractDataProvider;
import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.NotificationType;
import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.PagerSwipeItemFrameLayout;
import com.eowise.recyclerview.stickyheaders.samples.LndUserProfile.LndProfile;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.UserProfile.OtherUserProfileActivity;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.Utils.TimeAgo;
import com.eowise.recyclerview.stickyheaders.samples.data.LndAgentBean;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;

import java.util.ArrayList;


class AgentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEADER = 1, NORMALUSER = 2, SHOPUSER = 3,SURPASSEDYOU=4;
    ArrayList<LndAgentBean> items;
public AgentListAdapter(Context con, ArrayList<LndAgentBean> data)
{
    items=data;
}
    public class Header extends RecyclerView.ViewHolder {
        public PagerSwipeItemFrameLayout mContainer;
        public TextView notiTextView;
        public ImageView notiprofile, notiimage;

        public Header(View v) {
            super(v);
            mContainer = (PagerSwipeItemFrameLayout) v.findViewById(R.id.container);
            notiTextView = (TextView) v.findViewById(R.id.notiinfotext);
            notiprofile = (ImageView) v.findViewById(R.id.notipropic);
            notiimage = (ImageView) v.findViewById(R.id.notiimage);

        }


    }

    public class NormalUser extends RecyclerView.ViewHolder {
        public PagerSwipeItemFrameLayout mContainer;
        public TextView notiTextView;
        public ImageView notiprofile, notiimage;

        public NormalUser(View v) {
            super(v);
            mContainer = (PagerSwipeItemFrameLayout) v.findViewById(R.id.container);
            notiTextView = (TextView) v.findViewById(R.id.notiinfotext);
            notiprofile = (ImageView) v.findViewById(R.id.notipropic);
            notiimage = (ImageView) v.findViewById(R.id.notiimage);

        }


    }

    public class ShopUser extends RecyclerView.ViewHolder {
        public PagerSwipeItemFrameLayout mContainer;
        public TextView notiTextView;
        public ImageView notiprofile, notiimage;

        public ShopUser(View v) {
            super(v);
            mContainer = (PagerSwipeItemFrameLayout) v.findViewById(R.id.container);
            notiTextView = (TextView) v.findViewById(R.id.notiinfotext);
            notiprofile = (ImageView) v.findViewById(R.id.notipropic);
            notiimage = (ImageView) v.findViewById(R.id.notiimage);

        }


    }

    public class SurpassedUser extends RecyclerView.ViewHolder {
        public PagerSwipeItemFrameLayout mContainer;
        public TextView notiTextView;
        public ImageView notiprofile, notiimage;

        public SurpassedUser(View v) {
            super(v);
            mContainer = (PagerSwipeItemFrameLayout) v.findViewById(R.id.container);
            notiTextView = (TextView) v.findViewById(R.id.notiinfotext);
            notiprofile = (ImageView) v.findViewById(R.id.notipropic);
            notiimage = (ImageView) v.findViewById(R.id.notiimage);

        }

    }
    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getType() == HEADER) {
            return HEADER;
        } else if (items.get(position).getType() == NORMALUSER) {
            return NORMALUSER;
        } else if (items.get(position).getType() == SHOPUSER) {
            return SHOPUSER;

        }
        else if (items.get(position).getType() == SURPASSEDYOU) {
            return SURPASSEDYOU;

        }
        return -1;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {

            case HEADER:
                final View header = inflater.inflate(R.layout.agent_total_header, parent, false);
                viewHolder = new Header(header);
                break;
            case NORMALUSER:
                final View normaluser = inflater.inflate(R.layout.agent_normal_user_row_layout, parent, false);
                viewHolder = new NormalUser(normaluser);
                break;
            case SHOPUSER:
                final View shopuser = inflater.inflate(R.layout.agent_shop_user_row_layout, parent, false);
                viewHolder = new ShopUser(shopuser);
                break;
            case SURPASSEDYOU:
                final View surpasseduser = inflater.inflate(R.layout.agent_user_surpassed_row_layout, parent, false);
                viewHolder = new SurpassedUser(surpasseduser);
                break;
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LndAgentBean item = items.get(position);
        switch (holder.getItemViewType()) {


            case HEADER:

                break;

            case NORMALUSER:
                //setSpannableText("Cindy Lowe ", "mentioned you in a post. @Jakie @Oliva i know you always wanted this #beautiful and #nice #dress.  ", " 2m", ((MyViewHolder) holder).notiTextView);

                break;
            case SHOPUSER:

                break;


        }
        //  holder.mTextView.setText(item.getText());
    }


    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
       /* if (holder instanceof Header)
            ViewCompat.setAlpha(((Header) holder).mContainer, 1.0f);
        else if (holder instanceof NormalUser)
            ViewCompat.setAlpha(((NormalUser) holder).mContainer, 1.0f);
        else if (holder instanceof ShopUser)
            ViewCompat.setAlpha(((ShopUser) holder).mContainer, 1.0f);*/


    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
