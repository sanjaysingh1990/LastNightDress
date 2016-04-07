package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.HashTagsFullView.HashTagStickyActivity;
import com.eowise.recyclerview.stickyheaders.samples.HashTagsFullView.LndBrandHashTagGridViewActivity;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.LndPostFullView.LndFullStickyActivity;
import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.LndUserProfile.LndUserFullStickyActivity;
import com.eowise.recyclerview.stickyheaders.samples.data.ShopData;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class NumberedAdapter extends RecyclerView.Adapter {

    List<ShopData> shopdata;
    Activity con;
    private int forwhich = 0;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int type=0;

    public NumberedAdapter(ArrayList<ShopData> sd, Activity con, int value, RecyclerView recyclerView) {

        shopdata = sd;
        this.con = con;
        this.forwhich = value;

    }

    public NumberedAdapter(ArrayList<ShopData> sd, Activity con, int value,int cattype) {

        shopdata = sd;
        this.con = con;
        this.forwhich = value;
        this.type=cattype;
    }

    @Override
    public int getItemViewType(int position) {
        return shopdata.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            Display display = con.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;


            ViewGroup.LayoutParams params = view.getLayoutParams();

            params.height = (width / 3);
            view.requestLayout();

            vh = new ItemViewHolder(view);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        double price_now = 0.0;

        String country = SingleTon.pref.getString("country", "united states");

        if (holder instanceof ItemViewHolder) {

            if ((position + 1) % 3 == 0)
                ((ItemViewHolder) holder).separator.setVisibility(View.GONE);
            else
                ((ItemViewHolder) holder).separator.setVisibility(View.VISIBLE);


            ShopData sd = shopdata.get(position);
            //convert value
            try {

                price_now = Double.parseDouble(sd.getPrice());
            } catch (Exception ex) {

            }
            //settting price
            SingleTon.showValue(country, ((ItemViewHolder) holder).price, price_now);
             Log.e("url", sd.getImageurl() + "'");
            if (sd.getImageurl().length() == 0)
                ((ItemViewHolder) holder).img.setImageResource(R.drawable.loading_icon);
            else
                ImageLoader.getInstance()
                        .displayImage(sd.getImageurl(), ((ItemViewHolder) holder).img, SingleTon.options4, new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                                ((ItemViewHolder) holder).loader.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                ((ItemViewHolder) holder).loader.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                ((ItemViewHolder) holder).loader.setVisibility(View.GONE);
                            }
                        }, new ImageLoadingProgressListener() {
                            @Override
                            public void onProgressUpdate(String imageUri, View view, int current, int total) {

                            }
                        });
            ((ItemViewHolder) holder).img.setOnClickListener(new MyEvent(position));


        } else {
            // ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }


    }

    @Override
    public int getItemCount() {
        return shopdata.size();
    }

    private class MyEvent implements View.OnClickListener {
        int pos;

        public MyEvent(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            if (forwhich == 100) {
                Intent i = new Intent(con, LndUserFullStickyActivity.class);
                // i.putExtra("post_id",shopdata.get(pos).getPostid());
                i.putExtra("post_location", pos);

                //  con.startActivity(i);
            } else if (forwhich == 200) {
                Intent i = new Intent(con, LndFullStickyActivity.class);
                i.putExtra("post_location", pos);

                con.startActivity(i);
            } else if (forwhich == 300) {
                Intent i = new Intent(con, HashTagStickyActivity.class);
                i.putExtra("post_location", pos);
                i.putExtra("hashtag", LndBrandHashTagGridViewActivity.hashtagorbrand);
                i.putExtra("type", type);
                con.startActivity(i);
            }
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public AVLoadingIndicatorView progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (AVLoadingIndicatorView) v.findViewById(R.id.progress);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView price;
        public ImageView img;
        private View separator;
        public ProgressBar loader;

        public ItemViewHolder(View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.price);
            img = (ImageView) itemView.findViewById(R.id.image);
            separator = itemView.findViewById(R.id.separator);
            price = (TextView) itemView.findViewById(R.id.price);
            img = (ImageView) itemView.findViewById(R.id.image);
            loader = (ProgressBar) itemView.findViewById(R.id.progressBar2);
        }


    }
}

