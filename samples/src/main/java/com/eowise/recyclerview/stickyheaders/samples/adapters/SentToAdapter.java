package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.LndHomeAdapter;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.data.FollowersFollowingData;
import com.eowise.recyclerview.stickyheaders.samples.data.PersonDataProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by aurel on 22/09/14.
 */
public class SentToAdapter extends RecyclerView.Adapter<SentToAdapter.ViewHolder> {
    private static Map<Integer, Parcelable> scrollStatePositionsMap = new HashMap<>();
    private static List<FollowersFollowingData> items;
    private PersonDataProvider personDataProvider;
    static Context mContext;
    static int count = 0;
    public static HashMap<String, Boolean> usersselected = new HashMap<>();

    public SentToAdapter(Context context, List<FollowersFollowingData> data) {
        this.mContext = context;
        this.personDataProvider = personDataProvider;
        this.items = data;
        //setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.send_to_row, viewGroup, false);

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
//        return items.get(position).hashCode();
        return 1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        FollowersFollowingData fd = items.get(position);
        ImageLoaderImage.imageLoader.displayImage(fd.getUserpic(), viewHolder.profilepic, ImageLoaderImage.options3);
        viewHolder.uname.setText(Capitalize.capitalize(fd.getUname()));
        viewHolder.profilepic.setOnClickListener(new MyEvent(viewHolder.userselected, fd));
        if (fd.isSelected()) {
            viewHolder.userselected.setBackgroundResource(R.drawable.user_selected);

        } else
            viewHolder.userselected.setBackground(null);

    }

    private class MyEvent implements View.OnClickListener {

        RelativeLayout relativelayout;
        FollowersFollowingData fd;

        public MyEvent(RelativeLayout rl, FollowersFollowingData followerdata) {
            this.relativelayout = rl;
            fd = followerdata;
        }

        @Override
        public void onClick(View v) {

            if (fd.isSelected()) {
                relativelayout.setBackground(null);
                fd.setSelected(false);

                usersselected.remove(fd.getUserid());
                check(fd.getUname());

            } else {
                relativelayout.setBackgroundResource(R.drawable.user_selected);
                fd.setSelected(true);

                usersselected.put(fd.getUserid(), true);
                check(fd.getUname());
            }
        }
    }

    private void check(String uname) {
        if (usersselected.size() > 0) {
            LndHomeAdapter.usermessage.setVisibility(View.VISIBLE);
            LndHomeAdapter.sendcancel.setText("SEND");
            LndHomeAdapter.sendcancel.setTextColor(Color.parseColor("#be4d66"));
        } else {
            LndHomeAdapter.usermessage.setVisibility(View.GONE);
            LndHomeAdapter.sendcancel.setText("CANCEL");
            LndHomeAdapter.sendcancel.setTextColor(Color.parseColor("#be4d66"));

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.uname)
        TextView uname;
        @Bind(R.id.profilepic)
        ImageView profilepic;
        @Bind(R.id.roundeduser)
        RelativeLayout userselected;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }


}
