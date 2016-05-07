package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.NewMessage.SendMessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.UserProfile.OtherUserProfileActivity;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.data.MessageData;
import com.eowise.recyclerview.stickyheaders.samples.data.UserType;

import java.text.SimpleDateFormat;
import java.util.List;

import github.ankushsachdeva.emojicon.EmojiconTextView;


/**
 * Created by madhur on 17/01/15.
 */
public class SendMsgListAdapter extends BaseAdapter {

    private List<MessageData> chatMessages;
    private Context context;

    public SendMsgListAdapter(List<MessageData> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;

    }


    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        MessageData message = chatMessages.get(position);

        ViewHolder1 holder1;
        ViewHolder2 holder2;
        ViewHolder3 holder3;
        if (message.getUserType() == UserType.SELF) {
            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_user2_item, null, false);
                holder1 = new ViewHolder1();

                holder1.profpic = (ImageView) v.findViewById(R.id.chatpicsender);
                holder1.messageTextView = (EmojiconTextView) v.findViewById(R.id.message_text);
                holder1.timeTextView = (TextView) v.findViewById(R.id.time_text);

                v.setTag(holder1);
            } else {
                v = convertView;
                holder1 = (ViewHolder1) v.getTag();

            }
            holder1.timeTextView.setText(message.getTime());
            holder1.messageTextView.setText(message.getMessage());
            SingleTon.imageLoader.displayImage(message.getProfilepic(), holder1.profpic, SingleTon.options3);

            //holder1.timeTextView.setText(message.getTime());

        } else if (message.getUserType() == UserType.OTHER) {

            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_user1_item, null, false);

                holder2 = new ViewHolder2();


                holder2.messageTextView = (EmojiconTextView) v.findViewById(R.id.message_text);
                holder2.timeTextView = (TextView) v.findViewById(R.id.time_text);
                holder2.profpic = (ImageView) v.findViewById(R.id.chatpic);
                v.setTag(holder2);

            } else {
                v = convertView;
                holder2 = (ViewHolder2) v.getTag();

            }
            holder2.timeTextView.setText(message.getTime());

            holder2.messageTextView.setText(message.getMessage());
            SingleTon.imageLoader.displayImage(message.getProfilepic(), holder2.profpic, SingleTon.options3);


        } else if (message.getUserType() == UserType.BANNER) {
            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.banner_layout, null, false);
                holder3 = new ViewHolder3();


                holder3.brandname = (TextView) v.findViewById(R.id.brandname);
                holder3.sellername = (TextView) v.findViewById(R.id.sellername);
                holder3.sellername.setTypeface(SingleTon.robotomedium);
                holder3.size = (TextView) v.findViewById(R.id.size);
                holder3.price = (TextView) v.findViewById(R.id.listprice);
                holder3.bannerimage = (ImageView) v.findViewById(R.id.bannerimg);

                v.setTag(holder3);
            } else {
                v = convertView;
                holder3 = (ViewHolder3) v.getTag();

            }

            holder3.brandname.setText(Capitalize.capitalizeFirstLetter(SendMessageActivity.chatbanner.getBrand()));
            holder3.sellername.setText(Capitalize.capitalizeFirstLetter(SendMessageActivity.chatbanner.getSellername()));
            holder3.size.setText(SendMessageActivity.chatbanner.getSize().split(",").length + "");
            holder3.price.setText("$"+SendMessageActivity.chatbanner.getPricenow());
            SingleTon.imageLoader2.displayImage(SendMessageActivity.chatbanner.getImage_url(), holder3.bannerimage, SingleTon.options);

            //banner username
            holder3.sellername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent userprofile = new Intent(context, OtherUserProfileActivity.class);
                    userprofile.putExtra("uname", SendMessageActivity.chatbanner.getSellername());
                    userprofile.putExtra("user_id", SendMessageActivity.chatbanner.getSellerid());
                    context.startActivity(userprofile);
                }
            });

        }


        return v;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        MessageData message = chatMessages.get(position);
        return message.getUserType().ordinal();
    }

    private class ViewHolder1 {
        public ImageView profpic;
        public EmojiconTextView messageTextView;
        public TextView timeTextView;


    }

    private class ViewHolder2 {
        public ImageView profpic;
        public EmojiconTextView messageTextView;
        public TextView timeTextView;

    }

    private class ViewHolder3 {
        public ImageView bannerimage;
        public TextView brandname;
        public TextView sellername;
        public TextView size;
        public TextView price;

    }
}
