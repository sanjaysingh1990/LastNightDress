package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.NewMessage.SendMessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.data.MessageData;
import com.eowise.recyclerview.stickyheaders.samples.data.UserType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by madhur on 17/01/15.
 */
public class SendMsgListAdapter extends BaseAdapter {

    private List<MessageData> chatMessages;
    private Context context;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm");

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
                holder1.messageTextView = (TextView) v.findViewById(R.id.message_text);
                holder1.timeTextView = (TextView) v.findViewById(R.id.time_text);

                v.setTag(holder1);
            } else {
                v = convertView;
                holder1 = (ViewHolder1) v.getTag();

            }

           holder1.messageTextView.setText(message.getMessage());
            ImageLoaderImage.imageLoader.displayImage(message.getProfilepic(), holder1.profpic, ImageLoaderImage.options3);

            //holder1.timeTextView.setText(message.getTime());

        } else if (message.getUserType() == UserType.OTHER) {

            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_user1_item, null, false);

                holder2 = new ViewHolder2();


                holder2.messageTextView = (TextView) v.findViewById(R.id.message_text);
                holder2.timeTextView = (TextView) v.findViewById(R.id.time_text);
                holder2.profpic = (ImageView) v.findViewById(R.id.chatpic);
                v.setTag(holder2);

            } else {
                v = convertView;
                holder2 = (ViewHolder2) v.getTag();

            }

            holder2.messageTextView.setText(message.getMessage());
            ImageLoaderImage.imageLoader.displayImage(message.getProfilepic(),holder2.profpic,ImageLoaderImage.options3);
            //holder2.messageTextView.setText(message.getMessageText());
            //holder2.timeTextView.setText(message.getTime());



            }
        else if (message.getUserType() == UserType.BANNER) {
            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.banner_layout, null, false);
                holder3 = new ViewHolder3();


                holder3.brandname = (TextView) v.findViewById(R.id.brandname);
                holder3.sellername = (TextView) v.findViewById(R.id.sellername);
                holder3.size = (TextView) v.findViewById(R.id.size);
                holder3.price = (TextView) v.findViewById(R.id.listprice);
                holder3.bannerimage = (ImageView) v.findViewById(R.id.bannerimg);
                v.setTag(holder3);
            } else {
                v = convertView;
                holder3 = (ViewHolder3) v.getTag();

            }

             holder3.brandname.setText(SendMessageActivity.chatbanner.getBrand());
            holder3.sellername.setText(SendMessageActivity.chatbanner.getSellername());
            holder3.size.setText(SendMessageActivity.chatbanner.getSize().split(",").length+"");
            holder3.price.setText(SendMessageActivity.chatbanner.getPricenow());
            ImageLoaderImage.imageLoader2.displayImage(SendMessageActivity.chatbanner.getImage_url(),holder3.bannerimage,ImageLoaderImage.options);


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
        public TextView messageTextView;
        public TextView timeTextView;


    }

    private class ViewHolder2 {
        public ImageView profpic;
        public TextView messageTextView;
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
