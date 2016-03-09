package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.UserProfile.OtherUserProfileActivity;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.data.PeopleData;
import com.eowise.recyclerview.stickyheaders.samples.data.PersonDataProvider;
import com.eowise.recyclerview.stickyheaders.samples.listeners.OnLoadMoreListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aurel on 22/09/14.
 */
public class PeopleBrandHashTapAdapter extends RecyclerView.Adapter {
    private  List<PeopleData> items;
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
        if(items.get(position).getType()==0)
        return  VIEW_PROG;
        else if(items.get(position).getType()==1)
            return VIEW_ITEM;
        else if(items.get(position).getType()==3)
            return VIEW_BRAND;
        else if(items.get(position).getType()==4)
            return VIEW_HASHTAG;

        else
         return  VIEW_NORESULT;
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
        }
        else if (viewType == VIEW_BRAND) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.brand_hashtag_item_row, parent, false);

            vh = new BrandHashTagViewHolder(v);
        }
        else if (viewType == VIEW_HASHTAG) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.brand_hashtag_item_row, parent, false);

            vh = new BrandHashTagViewHolder(v);
        }
        else  {
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
                ImageLoaderImage.imageLoader.displayImage(pd.getImageurl(), ((ViewHolder) holder).profilepic, ImageLoaderImage.options3);

                break;
            case VIEW_BRAND:

                ((BrandHashTagViewHolder) holder).brandhashtag.setText(pd.getBrandname());
                ((BrandHashTagViewHolder) holder).total.setText(pd.getTotal());


            break;
            case VIEW_HASHTAG:

                ((BrandHashTagViewHolder) holder).brandhashtag.setText(pd.getHasttag());
                ((BrandHashTagViewHolder) holder).total.setText(pd.getTotal());

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


    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView uname;
        ImageView profilepic;

        public ViewHolder(View itemView) {
            super(itemView);
            uname = (TextView) itemView.findViewById(R.id.uname);
            uname.setTypeface(ImageLoaderImage.unamefont);
            profilepic = (ImageView) itemView.findViewById(R.id.profilepic);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Add user to database
            PeopleData peopleData = items.get(getAdapterPosition());
            ImageLoaderImage.db.addPoeple(peopleData);

            Intent otherlnduser = new Intent(mContext, OtherUserProfileActivity.class);
            otherlnduser.putExtra("uname", items.get(getAdapterPosition()).getUname());
            otherlnduser.putExtra("user_id", items.get(getAdapterPosition()).getUserid());

            mContext.startActivity(otherlnduser);
        }
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public TextView showingtext;
        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progress);
             showingtext= (TextView) v.findViewById(R.id.showingtext);
        }
    }
    public static class BrandHashTagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView brandhashtag,total;
        public BrandHashTagViewHolder(View itemView) {
            super(itemView);
            brandhashtag= (TextView) itemView.findViewById(R.id.hashtagbrand);
            total    = (TextView) itemView.findViewById(R.id.total);

        }

        @Override
        public void onClick(View v) {

        }
    }

}
