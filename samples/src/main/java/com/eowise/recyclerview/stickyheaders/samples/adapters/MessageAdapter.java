package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.NewMessage.SendMessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.data.MessageData;
import com.eowise.recyclerview.stickyheaders.samples.data.PersonDataProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aurel on 22/09/14.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private static Map<Integer, Parcelable> scrollStatePositionsMap = new HashMap<>();
    private static List<MessageData> items;
    private PersonDataProvider personDataProvider;
    static Context mContext;
    static int count=0;
    public MessageAdapter(Context context, List<MessageData> data) {
        this.mContext = context;
        this.personDataProvider = personDataProvider;
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

         MessageData md=items.get(position);
        ImageLoaderImage.imageLoader.displayImage(md.getProfilepic(), viewHolder.profileimg, ImageLoaderImage.options3);
         String uname=md.getUname();
        try {
            uname=Character.toUpperCase(uname.charAt(0)) + uname.substring(1);
        }
        catch (Exception ex)
        {
        }
            viewHolder.uname.setText(uname);
        viewHolder.message.setText(md.getMessage());
        if(md.getMsgindicator().compareTo("1")==0)
            viewHolder.messindicator.setImageResource(R.drawable.color_icon);
    else
            viewHolder.messindicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView profileimg,messindicator;
        TextView uname,message,time;
        public ViewHolder(View itemView) {
            super(itemView);
          profileimg= (ImageView) itemView.findViewById(R.id.profilepic);
            messindicator= (ImageView) itemView.findViewById(R.id.messidicator);
            uname= (TextView) itemView.findViewById(R.id.uname);
            message= (TextView) itemView.findViewById(R.id.msg);
            time= (TextView) itemView.findViewById(R.id.time);
         itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
Intent sendmsg=new Intent(mContext, SendMessageActivity.class);
            sendmsg.putExtra("uname", items.get(getAdapterPosition()).getUname());
            sendmsg.putExtra("user_id", items.get(getAdapterPosition()).getSender_id());

            mContext.startActivity(sendmsg);
        }
    }


}
