package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.LndMessage.SendSwapRequestActivity;
import com.eowise.recyclerview.stickyheaders.samples.LndMessage.SwapRequestActivity;
import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.data.ShopData;
import com.eowise.recyclerview.stickyheaders.samples.listeners.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class SwapRequestAcceptdAdapter extends RecyclerView.Adapter {

    static List<ShopData> shopdata;
    Activity con;

    private OnLoadMoreListener onLoadMoreListener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private Context context;


    public SwapRequestAcceptdAdapter(ArrayList<ShopData> sd, Activity con, RecyclerView recyclerView, Context context) {

        shopdata = sd;
        this.con = con;
        this.context = context;

    }



    @Override
    public int getItemViewType(int position) {
        return shopdata.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myitem_row, parent, false);
            Display display = con.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;


            ViewGroup.LayoutParams params = view.getLayoutParams();

            params.height = (width / 3);
            view.requestLayout();

            vh = new StudentViewHolder(view);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof StudentViewHolder) {

            if ((position + 1) % 3 == 0)
                ((StudentViewHolder) holder).separator.setVisibility(View.GONE);
            else
                ((StudentViewHolder) holder).separator.setVisibility(View.VISIBLE);


            ShopData sd = shopdata.get(position);

            ((StudentViewHolder) holder).price.setText("$" + sd.getPrice());
            if (sd.isItemchecked())
                ((StudentViewHolder) holder).layer.setVisibility(View.VISIBLE);
            else
                ((StudentViewHolder) holder).layer.setVisibility(View.GONE);

            Log.e("status", sd.isItemchecked() + "");

            SingleTon.imageLoader.displayImage(sd.getImageurl(), ((StudentViewHolder) holder).img, SingleTon.options);
            ((StudentViewHolder) holder).img.setOnClickListener(new mySelection(((StudentViewHolder) holder).layer, position));


        } else {
            //((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }


    }



    @Override
    public int getItemCount() {
        return shopdata.size();
    }

    private class MyEvent implements View.OnClickListener {
        int value;

        public MyEvent(int value) {
            this.value = value;
        }

        @Override
        public void onClick(View v) {

        }
    }



    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public AVLoadingIndicatorView progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (AVLoadingIndicatorView) v.findViewById(R.id.progress);
        }
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView price;
        public ImageView img;
        private Context con;
        public View separator;
        public FrameLayout layer;

        public StudentViewHolder(View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.price);
            img = (ImageView) itemView.findViewById(R.id.image);
            separator = itemView.findViewById(R.id.separator);
            layer = (FrameLayout) itemView.findViewById(R.id.layer);

        }


    }

    private class mySelection implements View.OnClickListener {
        FrameLayout fl;
        int pos;

        public mySelection(FrameLayout fl, int pos) {
            this.fl = fl;
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {


            ShopData sd = shopdata.get(pos);
            SwapRequestActivity.swapingpostid=sd.getPostid();

            for(ShopData sd2:shopdata)
            {
                sd2.setItemchecked(false);
            }
            notifyDataSetChanged();
            if (sd.isItemchecked()) {
                SendSwapRequestActivity.selectedpost.remove(pos + "");

                fl.setVisibility(View.GONE);
                sd.setItemchecked(false);
                notifyItemChanged(pos);
                check();
            } else {
                SendSwapRequestActivity.selectedpost.put(pos + "", sd);

                fl.setVisibility(View.VISIBLE);
                sd.setItemchecked(true);
                notifyItemChanged(pos);
                check();
            }

        }

        private void check() {
            try {
                SwapRequestActivity ssr = (SwapRequestActivity) context;
                if(SwapRequestActivity.swapingpostid.length()>0)
                ssr.changeColor(1);
                else
                    ssr.changeColor(0);

            } catch (Exception ex) {
                SendSwapRequestActivity ssr = (SendSwapRequestActivity) context;
                if (SendSwapRequestActivity.selectedpost.size() > 0)
                    ssr.changeColor(1);
                else
                    ssr.changeColor(0);

            }
        }
    }


}
