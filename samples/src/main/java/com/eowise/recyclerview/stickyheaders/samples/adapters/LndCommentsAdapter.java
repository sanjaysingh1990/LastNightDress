package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.AbstractDataProvider2;
import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.MessageFragment;
import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.PagerSwipeItemFrameLayout;
import com.eowise.recyclerview.stickyheaders.samples.NewMessage.SendMessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.Utils.RelativeTimeTextView;
import com.eowise.recyclerview.stickyheaders.samples.data.CommentData;
import com.eowise.recyclerview.stickyheaders.samples.data.MessageData;
import com.eowise.recyclerview.stickyheaders.samples.data.MessageToFriendsData;
import com.eowise.recyclerview.stickyheaders.samples.data.PersonDataProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aurel on 22/09/14.
 */
public class LndCommentsAdapter extends RecyclerView.Adapter<LndCommentsAdapter.ViewHolder> {
    private static List<CommentData> items;
    private PersonDataProvider personDataProvider;
    static Context mContext;
    static int count = 0;

    public LndCommentsAdapter(Context context, List<CommentData> data) {
        this.mContext = context;
       this.items = data;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item_row, viewGroup, false);

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
        CommentData cd = items.get(position);
        SingleTon.imageLoader.displayImage(cd.getProfilepic(), viewHolder.profilepic, SingleTon.options3);

        viewHolder.uname.setText(capitalize(cd.getUname()));
        viewHolder.message.setText(cd.getCommenttxxt());
        viewHolder.time.setReferenceTime(cd.getTime());

    }

    private String capitalize(final String line) {
        String[] split = line.split(" ");
        String output = "";
        for (String str : split) {

            output += Character.toUpperCase(str.charAt(0)) + str.substring(1) + " ";
        }
        return output;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public PagerSwipeItemFrameLayout mContainer;
        public TextView uname, message;
        public ImageView profilepic, messindicator;
        public RelativeTimeTextView time;

        public ViewHolder(View v) {
            super(v);
            uname = (TextView) v.findViewById(R.id.uname);
            message = (TextView) v.findViewById(R.id.msg);
            profilepic = (ImageView) v.findViewById(R.id.profilepic);
            messindicator = (ImageView) v.findViewById(R.id.messidicator);
            time = (RelativeTimeTextView) v.findViewById(R.id.time);

            messindicator.setVisibility(View.GONE);

            //apply font
            uname.setTypeface(SingleTon.robotomedium);
        }


    }


}
