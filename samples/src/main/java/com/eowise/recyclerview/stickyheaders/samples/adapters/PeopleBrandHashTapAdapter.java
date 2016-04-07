package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.HashTagsFullView.LndBrandHashTagGridViewActivity;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.LndUserProfile.LndProfile;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.UserProfile.OtherUserProfileActivity;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.data.PeopleData;

import java.util.List;

/**
 * Created by aurel on 22/09/14.
 */
public class PeopleBrandHashTapAdapter extends RecyclerView.Adapter {
    private List<PeopleData> items;
    static Context mContext;
    static int count = 0;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final int VIEW_NORESULT = 2;
    private final int VIEW_BRAND = 3;
    private final int VIEW_HASHTAG = 4;

    public PeopleBrandHashTapAdapter(Context context, List<PeopleData> data) {
        this.mContext = context;
        this.items = data;

    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getType() == 0)
            return VIEW_PROG;
        else if (items.get(position).getType() == 1)
            return VIEW_ITEM;
        else if (items.get(position).getType() == 3)
            return VIEW_BRAND;
        else if (items.get(position).getType() == 4)
            return VIEW_HASHTAG;
        else
            return VIEW_NORESULT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.people_item_row, parent, false);

            vh = new ViewHolder(v);
        } else if (viewType == VIEW_PROG) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_find, parent, false);

            vh = new ProgressViewHolder(v);
        } else if (viewType == VIEW_BRAND) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.brand_hashtag_item_row, parent, false);

            vh = new BrandViewHolder(v);
        } else if (viewType == VIEW_HASHTAG) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.brand_hashtag_item_row, parent, false);

            vh = new HashTagViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_find, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;

    }

    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        PeopleData pd = items.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_ITEM:

                ((ViewHolder) holder).uname.setText(Capitalize.capitalize(pd.getUname()));
                SingleTon.imageLoader.displayImage(pd.getImageurl(), ((ViewHolder) holder).profilepic, SingleTon.options3);

                break;
            case VIEW_BRAND:

                ((BrandViewHolder) holder).brandhashtag.setText(pd.getBrandname());
                ((BrandViewHolder) holder).total.setText(pd.getTotal());


                break;
            case VIEW_HASHTAG:

                ((HashTagViewHolder) holder).brandhashtag.setText("#" + pd.getHasttag());
                ((HashTagViewHolder) holder).total.setText(pd.getTotal());

                break;
            case VIEW_PROG:
                ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);

                break;
            case VIEW_NORESULT:
                ((ProgressViewHolder) holder).progressBar.setVisibility(View.GONE);
                ((ProgressViewHolder) holder).showingtext.setText("no result found");


                break;

        }
    }


    @Override
    public int getItemCount() {


        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView uname;
        ImageView profilepic;

        public ViewHolder(View itemView) {
            super(itemView);
            uname = (TextView) itemView.findViewById(R.id.uname);
            uname.setTypeface(SingleTon.unamefont);
            profilepic = (ImageView) itemView.findViewById(R.id.profilepic);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Add user to database
            PeopleData peopleData = items.get(getAdapterPosition());
            SingleTon.db.addPoeple(peopleData);
            Intent profile;
            if(SingleTon.pref.getString("user_id", "").compareToIgnoreCase(items.get(getAdapterPosition()).getUserid())==0)
            {
                profile = new Intent(mContext, LndProfile.class);
             }
            else
            {
                profile = new Intent(mContext, OtherUserProfileActivity.class);
                profile.putExtra("uname", items.get(getAdapterPosition()).getUname());
                profile.putExtra("user_id", items.get(getAdapterPosition()).getUserid());

            }



            mContext.startActivity(profile);
        }
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public TextView showingtext;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progress);
            showingtext = (TextView) v.findViewById(R.id.showingtext);
        }
    }

    public class HashTagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView brandhashtag, total;

        public HashTagViewHolder(View itemView) {
            super(itemView);
            brandhashtag = (TextView) itemView.findViewById(R.id.hashtagbrand);
            total = (TextView) itemView.findViewById(R.id.total);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


            Intent hashtag = new Intent(mContext,LndBrandHashTagGridViewActivity.class);
            hashtag.putExtra("hashtag", items.get(getAdapterPosition()).getHasttag());
            hashtag.putExtra("type", 1);
            mContext.startActivity(hashtag);
        }
    }

    public class BrandViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView brandhashtag, total;

        public BrandViewHolder(View itemView) {
            super(itemView);
            brandhashtag = (TextView) itemView.findViewById(R.id.hashtagbrand);
            total = (TextView) itemView.findViewById(R.id.total);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


            Intent hashtag = new Intent(mContext, LndBrandHashTagGridViewActivity.class);
            hashtag.putExtra("hashtag", items.get(getAdapterPosition()).getBrandname());
            hashtag.putExtra("type", 2);
            mContext.startActivity(hashtag);
        }
    }
}
