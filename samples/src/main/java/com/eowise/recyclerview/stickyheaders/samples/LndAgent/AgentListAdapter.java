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
    public static final int USERHEADER = 0, HEADER = 1, NORMALUSER = 2, SHOPUSER = 3, SURPASSEDYOU = 4;
    ArrayList<LndAgentBean> items;

    public AgentListAdapter(Context con, ArrayList<LndAgentBean> data) {
        items = data;
    }

    public class Header extends RecyclerView.ViewHolder {

        public TextView total;

        public Header(View v) {
            super(v);

            total = (TextView) v.findViewById(R.id.total);


        }


    }

    public class NormalUser extends RecyclerView.ViewHolder {
        public TextView uname, totalpost, totalsales, totalrefuser;
        public ImageView profileimg;

        public NormalUser(View v) {
            super(v);
            uname = (TextView) v.findViewById(R.id.uname);
            totalpost = (TextView) v.findViewById(R.id.totalpost);
            totalsales = (TextView) v.findViewById(R.id.totalsales);
            totalrefuser = (TextView) v.findViewById(R.id.totalrefuser);
            profileimg = (ImageView) v.findViewById(R.id.mainprofilepic);

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

    public class UserHeader extends RecyclerView.ViewHolder {
        public TextView uname, totalpost, totalsales, totalrefuser;
        public ImageView profileimg;

        public UserHeader(View v) {
            super(v);
            uname = (TextView) v.findViewById(R.id.uname);
            totalpost = (TextView) v.findViewById(R.id.totalpost);
            totalsales = (TextView) v.findViewById(R.id.totalsales);
            totalrefuser = (TextView) v.findViewById(R.id.totalrefuser);
            profileimg = (ImageView) v.findViewById(R.id.mainprofilepic);

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

        } else if (items.get(position).getType() == SURPASSEDYOU) {
            return SURPASSEDYOU;

        } else if (items.get(position).getType() == USERHEADER) {
            return USERHEADER;

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
            case USERHEADER:
                final View userheader = inflater.inflate(R.layout.agent_user_header, parent, false);
                viewHolder = new UserHeader(userheader);
                break;
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LndAgentBean item = items.get(position);
        switch (holder.getItemViewType()) {


            case HEADER:
                Header header = (Header) holder;
                if (item.getHeaderType() == 1)
                    header.total.setText("Basic Users (" + item.getTotal() + "/5)");
                else if (item.getHeaderType() == 2)
                    header.total.setText("Agents (" + item.getTotal() + "/5)");
                break;

            case NORMALUSER:
                NormalUser normalUser = (NormalUser) holder;
                normalUser.uname.setText(Capitalize.capitalize(item.getUname()));
                normalUser.totalpost.setText(item.getTotalpost());
                normalUser.totalsales.setText(item.getTotalsales());
                normalUser.totalrefuser.setText(item.getTotalrefuser() + "/5");
                SingleTon.imageLoader.displayImage(item.getProfilepic(), normalUser.profileimg, SingleTon.options2);
                break;
            case SHOPUSER:

                break;
            case USERHEADER:

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
