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

import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
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
public class SendToAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static Map<Integer, Parcelable> scrollStatePositionsMap = new HashMap<>();
    private static List<FollowersFollowingData> items;
    private PersonDataProvider personDataProvider;
    static Context mContext;
    static int count = 0;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    public static HashMap<String, Boolean> usersselected = new HashMap<>();

    public SendToAdapter(Context context, List<FollowersFollowingData> data) {
        this.mContext = context;
        this.personDataProvider = personDataProvider;
        this.items = data;
        //setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_to_row, parent, false);
            vh = new ItemViewHolder(itemView);

        }
        else
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);
            ViewGroup.LayoutParams params = v.getLayoutParams();
            vh = new ProgressViewHolder(v);
        }

        //Perhaps the first most crucial part. The ViewPager loses its width information when it is put
        //inside a RecyclerView. It needs to be explicitly resized, in this case to the width of the
        //screen. The height must be provided as a fixed value.
        /*DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        v.getLayoutParams().width = displayMetrics.widthPixels;
        v.requestLayout();*/

        return vh;

    }

    @Override
    public long getItemId(int position) {
//        return items.get(position).hashCode();
        return 1;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ItemViewHolder) {
            {

                ItemViewHolder holder = (ItemViewHolder) viewHolder;
                FollowersFollowingData fd = items.get(position);
                SingleTon.imageLoader.displayImage(fd.getUserpic(), holder.profilepic, SingleTon.options3);
                holder.uname.setText(Capitalize.capitalize(fd.getUname()));
                holder.profilepic.setOnClickListener(new MyEvent(holder.userselected, fd));
                if (fd.isSelected()) {
                    holder.userselected.setBackgroundResource(R.drawable.user_selected);

                } else
                    holder.userselected.setBackground(null);
            }
            }

    }

    public class MyEvent implements View.OnClickListener {

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
        try {
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
        catch(Exception ex)
        {
           /* if (usersselected.size() > 0) {
                LndFullActivityAdapter.usermessage.setVisibility(View.VISIBLE);
                LndFullActivityAdapter.sendcancel.setText("SEND");
                LndFullActivityAdapter.sendcancel.setTextColor(Color.parseColor("#be4d66"));
            } else {
                LndFullActivityAdapter.usermessage.setVisibility(View.GONE);
                LndFullActivityAdapter.sendcancel.setText("CANCEL");
                LndFullActivityAdapter.sendcancel.setTextColor(Color.parseColor("#be4d66"));

            }*/
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.uname)
        TextView uname;
        @Bind(R.id.profilepic)
        ImageView profilepic;
        @Bind(R.id.roundeduser)
        RelativeLayout userselected;


        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
    @Override
    public int getItemViewType(int position) {
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public AVLoadingIndicatorView progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (AVLoadingIndicatorView) v.findViewById(R.id.progress);
        }
    }
}
