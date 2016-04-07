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

import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.NewMessage.SendMessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.data.MessageToFriendsData;
import com.eowise.recyclerview.stickyheaders.samples.data.PersonDataProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aurel on 22/09/14.
 */
public class NewMessageAdapter extends RecyclerView.Adapter<NewMessageAdapter.ViewHolder> {
    private static Map<Integer, Parcelable> scrollStatePositionsMap = new HashMap<>();
    private static List<MessageToFriendsData> items;
    private PersonDataProvider personDataProvider;
    static Context mContext;
    static int count=0;

    public NewMessageAdapter(Context context, List<MessageToFriendsData> data) {
        this.mContext = context;
        this.personDataProvider = personDataProvider;
        this.items = data;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_message_row, viewGroup, false);

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
          MessageToFriendsData msgfrnd=items.get(position);
           SingleTon.imageLoader.displayImage(msgfrnd.getProfilepic(), viewHolder.img, SingleTon.options3);

           viewHolder.uname.setText(capitalize(msgfrnd.getUname()));

    }
    private String capitalize(final String line) {
        String[] split=line.split(" ");
        String output="";
        for(String str:split)
        {

            output+=Character.toUpperCase(str.charAt(0)) + str.substring(1)+" ";
        }
        return output;
    }
    @Override
    public int getItemCount() {
        return items.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView uname;
        ImageView img;
        public ViewHolder(View itemView) {
        super(itemView);
        uname= (TextView) itemView.findViewById(R.id.uname);
        img= (ImageView) itemView.findViewById(R.id.profilepic);
        uname.setTypeface(SingleTon.unamefont);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {

            Intent msgtofrnd=new Intent(mContext, SendMessageActivity.class);
            msgtofrnd.putExtra("uname", items.get(getAdapterPosition()).getUname());
            msgtofrnd.putExtra("user_id", items.get(getAdapterPosition()).getUserid());

            mContext.startActivity(msgtofrnd);
        }
    }



}
