package com.eowise.recyclerview.stickyheaders.samples.adapters;


import android.content.Context;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;


import com.eowise.recyclerview.stickyheaders.samples.R;

import com.eowise.recyclerview.stickyheaders.samples.data.PersonDataProvider;


import java.util.List;

/**
 * Created by aurel on 22/09/14.
 */
public class PersonAdapter2 extends RecyclerView.Adapter<PersonAdapter2.ViewHolder>  {

    private static List<String> items;
    private PersonDataProvider personDataProvider;
    static Context mContext;
    static int count=0;
    public PersonAdapter2(Context context, List<String> data) {
        this.mContext = context;

        this.items = data;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fullimage_layout, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
     //   ImageLoaderImage.imageLoader.displayImage(items.get(position).getImgurl(), viewHolder.img, ImageLoaderImage.options);
/*        viewHolder.img.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewHolder.mDetector.onTouchEvent(event);
                return true;
            }
        });*/
        Toast.makeText(mContext,"position"+position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ViewHolder(View itemView, PersonAdapter2 personAdapter) {
            super(itemView);


        }


        @Override
        public void onClick(View v) {


        }

    }


}
