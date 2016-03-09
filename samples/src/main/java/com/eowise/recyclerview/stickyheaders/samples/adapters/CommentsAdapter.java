package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.data.CommentData;
import com.eowise.recyclerview.stickyheaders.samples.data.PersonDataProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aurel on 22/09/14.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private static Map<Integer, Parcelable> scrollStatePositionsMap = new HashMap<>();
    private static List<CommentData> items;
    private PersonDataProvider personDataProvider;
    static Context mContext;
    static int count=0;

    public CommentsAdapter(Context context, List<CommentData> data) {
        this.mContext = context;
        this.personDataProvider = personDataProvider;
        this.items = data;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_row2, viewGroup, false);

        //Perhaps the first most crucial part. The ViewPager loses its width information when it is put
        //inside a RecyclerView. It needs to be explicitly resized, in this case to the width of the
        //screen. The height must be provided as a fixed value.
        /*DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        v.getLayoutParams().width = displayMetrics.widthPixels;
        v.requestLayout();*/

        ViewHolder vh = new ViewHolder(itemView);
        return vh;

    }

    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        CommentData cd=items.get(position);
        ImageLoaderImage.imageLoader.displayImage(items.get(position).getProfilepic(), viewHolder.profilepic, ImageLoaderImage.options2);
        String s= capitalize(cd.getUname())+" "+cd.getCommenttxxt();
        SpannableString ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.1f), 0,cd.getUname().length(), 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#be4d66")), 0, cd.getUname().length(), 0);// set color

        viewHolder.unamecmnt.setText(ss1);

    }
    private String capitalize(final String line) {
       String[] split=line.split(" ");
        String output="";
        for(String str:split)
        {
            try
                {

                    output += Character.toUpperCase(str.charAt(0)) + str.substring(1)+" ";
                }
                catch(Exception ex)
                {
//                    output += Character.toUpperCase(str.charAt(0))+" ";
                    output=split[0]+" ";
                }
                }
        return output;
    }
    @Override
    public int getItemCount() {
        return items.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView unamecmnt;
        ImageView profilepic;
        public ViewHolder(View itemView) {
            super(itemView);
         unamecmnt= (TextView) itemView.findViewById(R.id.unamecmnt);
            profilepic= (ImageView) itemView.findViewById(R.id.profileimg);

        }

        @Override
        public void onClick(View v) {

        }
    }


}
