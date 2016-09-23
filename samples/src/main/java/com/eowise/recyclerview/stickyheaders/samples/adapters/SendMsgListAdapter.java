package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.TagSelectingTextview;
import com.eowise.recyclerview.stickyheaders.samples.LndUserProfile.LndProfile;
import com.eowise.recyclerview.stickyheaders.samples.NewMessage.FullImageViewDialog;
import com.eowise.recyclerview.stickyheaders.samples.NewMessage.MyCallBack;
import com.eowise.recyclerview.stickyheaders.samples.NewMessage.SendMessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.NewMessage.UploadFileToServer;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.UserProfile.OtherUserProfileActivity;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.data.MessageData;
import com.eowise.recyclerview.stickyheaders.samples.data.PersonDataProvider;
import com.eowise.recyclerview.stickyheaders.samples.data.UserMessageType;
import com.eowise.recyclerview.stickyheaders.samples.interfaces.TagClick;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import github.ankushsachdeva.emojicon.EmojiconTextView;

/**
 * Created by aurel on 22/09/14.
 */
public class SendMsgListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements TagClick, MyCallBack {
    private static List<MessageData> items;
    private PersonDataProvider personDataProvider;
    static Activity activity;
    static int count = 0;
    TagSelectingTextview mTagSelectingTextview;
    public static int hashTagHyperLinkDisabled = 0;
    String hastTagColorBlue = "#be4d66";
    private final int VIEW_ITEM_SELF = 1;
    private final int VIEW_ITEM_OTHER = 2;
    private final int VIEW_ITEM_BANNER = 3;
    private final int VIEW_ITEM_IMAGE_SELF_SERVER = 4;
    private final int VIEW_ITEM_IMAGE_OTHER_SERVER = 5;
    private final int VIEW_ITEM_SELF_LOCAL_IMAGE_UPLOAD = 6;
    private final int VIEW_ITEM_SELF_LOCAL_IMAGE_UPLOADED = 7;
    private SingleTon singleTon;

    public SendMsgListAdapter(Activity activity, List<MessageData> data) {
        this.activity = activity;
        this.items = data;
        mTagSelectingTextview = new TagSelectingTextview();
        singleTon = (SingleTon) activity.getApplication();
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_ITEM_SELF) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user2_item, parent, false);
            ViewGroup.LayoutParams params = view.getLayoutParams();

            vh = new MsgSelfOther(view);
        } else if (viewType == VIEW_ITEM_OTHER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.chat_user1_item, parent, false);

            vh = new MsgSelfOther(v);
        } else if (viewType == VIEW_ITEM_BANNER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.banner_layout, parent, false);

            vh = new Banner(v);
        } else if (viewType == VIEW_ITEM_IMAGE_OTHER_SERVER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.chat_other_user_image, parent, false);

            vh = new ChatImage(v);
        } else if (viewType == VIEW_ITEM_IMAGE_SELF_SERVER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.chat_self_user_image, parent, false);

            vh = new ChatImage(v);
        } else if (viewType == VIEW_ITEM_SELF_LOCAL_IMAGE_UPLOAD) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.chat_self_user_image, parent, false);

            vh = new ChatImage(v);
        } else if (viewType == VIEW_ITEM_SELF_LOCAL_IMAGE_UPLOADED) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.chat_self_user_image, parent, false);

            vh = new ChatImage(v);
        } else

            return null;
        return vh;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageData cd = items.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_ITEM_SELF:
                MsgSelfOther holder1 = (MsgSelfOther) holder;
                holder1.timeTextView.setText(cd.getTime());
                holder1.messageTextView.setText(cd.getMessage() + "");
                SingleTon.imageLoader.displayImage(cd.getProfilepic(), holder1.profpic, SingleTon.options3);
                break;

            case VIEW_ITEM_OTHER:
                holder1 = (MsgSelfOther) holder;
                holder1.timeTextView.setText(cd.getTime());
                holder1.messageTextView.setText(cd.getMessage() + "");
                SingleTon.imageLoader.displayImage(cd.getProfilepic(), holder1.profpic, SingleTon.options3);

                break;
            case VIEW_ITEM_BANNER:
                Banner holder3 = (Banner) holder;

                holder3.brandname.setText(Capitalize.capitalizeFirstLetter(cd.getBrandname()));
                holder3.sellername.setText(Capitalize.capitalizeFirstLetter(cd.getSellername()));
                holder3.size.setText(cd.getSize() + "");

                holder3.price.setText("$" + cd.getPrice());
                SingleTon.imageLoader2.displayImage(cd.getImageurl(), holder3.bannerimage, SingleTon.options);


                break;
            case VIEW_ITEM_SELF_LOCAL_IMAGE_UPLOAD:
                ChatImage localUpload = (ChatImage) holder;
                new UploadFileToServer(localUpload.donutProgress, cd.getShared_imgage_url(), this, position).execute();
                SingleTon.imageLoader.displayImage(ImageDownloader.Scheme.FILE.wrap(cd.getShared_imgage_url()), localUpload.shareimg, SingleTon.options3);
                localUpload.timeTextView.setText(cd.getTime());
                SingleTon.imageLoader2.displayImage(cd.getProfilepic(), localUpload.profpic, SingleTon.options);

                break;
            case VIEW_ITEM_SELF_LOCAL_IMAGE_UPLOADED:
                ChatImage local_uploaded = (ChatImage) holder;
                local_uploaded.timeTextView.setText(cd.getTime());
                SingleTon.imageLoader.displayImage(ImageDownloader.Scheme.FILE.wrap(cd.getShared_imgage_url()), local_uploaded.shareimg, SingleTon.options);
                SingleTon.imageLoader2.displayImage(cd.getProfilepic(), local_uploaded.profpic, SingleTon.options);
                break;

            case VIEW_ITEM_IMAGE_SELF_SERVER:
                ChatImage chat_self_image = (ChatImage) holder;
                chat_self_image.timeTextView.setText(cd.getTime());
                SingleTon.imageLoader.displayImage(cd.getShared_imgage_url(), chat_self_image.shareimg, SingleTon.options);
                SingleTon.imageLoader2.displayImage(cd.getProfilepic(), chat_self_image.profpic, SingleTon.options);

                break;
            case VIEW_ITEM_IMAGE_OTHER_SERVER:
                ChatImage chat_other_image = (ChatImage) holder;
                chat_other_image.timeTextView.setText(cd.getTime());
                SingleTon.imageLoader.displayImage(cd.getShared_imgage_url(), chat_other_image.shareimg, SingleTon.options);
                SingleTon.imageLoader2.displayImage(cd.getProfilepic(), chat_other_image.profpic, SingleTon.options);

                break;


        }
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();

    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getUserMessageType() == UserMessageType.SELF) {
            return VIEW_ITEM_SELF;
        } else if (items.get(position).getUserMessageType() == UserMessageType.OTHER) {
            return VIEW_ITEM_OTHER;
        } else if (items.get(position).getUserMessageType() == UserMessageType.BANNER) {
            return VIEW_ITEM_BANNER;
        } else if (items.get(position).getUserMessageType() == UserMessageType.SELF_IMAGE_LOCAL_UPLOAD) {
            return VIEW_ITEM_SELF_LOCAL_IMAGE_UPLOAD;
        } else if (items.get(position).getUserMessageType() == UserMessageType.SELF_IMAGE_LOCAL_UPLOADED) {
            return VIEW_ITEM_SELF_LOCAL_IMAGE_UPLOADED;
        } else if (items.get(position).getUserMessageType() == UserMessageType.SELF_IMAGE_SERVER) {
            return VIEW_ITEM_IMAGE_SELF_SERVER;
        } else if (items.get(position).getUserMessageType() == UserMessageType.OTHER_IMAGE_SERVER) {
            return VIEW_ITEM_IMAGE_OTHER_SERVER;
        }

        return 0;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void clickedTag(CharSequence tag) {
        Intent profile;
        if (SingleTon.pref.getString("uname", "").compareToIgnoreCase(tag.toString()) == 0) {
            profile = new Intent(activity, LndProfile.class);
        } else {
            profile = new Intent(activity, OtherUserProfileActivity.class);
            profile.putExtra("uname", tag.toString().trim());
            profile.putExtra("user_id", "-1");

        }

        activity.startActivity(profile);
    }

    @Override
    public void callback(int pos, String response) {
        if (response != null) {
            SendMessageActivity chatActivity = (SendMessageActivity) activity;

            try {


                DateFormat format = new SimpleDateFormat("hh:mm a");
                String curr_date_time_format = format.format(new Date());


                String msg_id = "lnd" + System.currentTimeMillis();

                JSONObject jobj = new JSONObject(response);
                chatActivity.sendImageMessage(jobj.getString("image_url"), msg_id);
                MessageData chatres = items.get(pos);
                chatres.setUserMessageType(UserMessageType.SELF_IMAGE_LOCAL_UPLOADED);
                chatres.setTime(curr_date_time_format);
                chatres.setDatetime(SingleTon.getCurrentTimeStamp());
                chatres.setMessage("image");

                notifyItemChanged(pos);
                //Log.e("imageurl", jobj.getString("image_url"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }


    public static class MsgSelfOther extends RecyclerView.ViewHolder {
        public ImageView profpic;
        public EmojiconTextView messageTextView;
        public TextView timeTextView;

        public MsgSelfOther(View v) {
            super(v);
            profpic = (ImageView) v.findViewById(R.id.chatpic);
            messageTextView = (EmojiconTextView) v.findViewById(R.id.message_text);
            timeTextView = (TextView) v.findViewById(R.id.time_text);
        }


    }


    public static class Banner extends RecyclerView.ViewHolder {
        public ImageView bannerimage;
        public TextView brandname;
        public TextView sellername;
        public TextView size;
        public TextView price;

        public Banner(View v) {
            super(v);
            brandname = (TextView) v.findViewById(R.id.brandname);
            sellername = (TextView) v.findViewById(R.id.sellername);
            sellername.setTypeface(SingleTon.robotomedium);
            size = (TextView) v.findViewById(R.id.size);
            price = (TextView) v.findViewById(R.id.listprice);
            bannerimage = (ImageView) v.findViewById(R.id.bannerimg);
        }


    }

    public class ChatImage extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView shareimg;
        public TextView timeTextView;
        public ImageView profpic;
        public DonutProgress donutProgress;

        public ChatImage(View v) {
            super(v);
            timeTextView = (TextView) v.findViewById(R.id.time_text);
            profpic = (ImageView) v.findViewById(R.id.profilepic);
            shareimg = (ImageView) v.findViewById(R.id.shareimg);
            donutProgress = (DonutProgress) v.findViewById(R.id.donut_progress);
            donutProgress.setVisibility(View.GONE);
            android.view.ViewGroup.LayoutParams layoutParams = shareimg.getLayoutParams();
            layoutParams.width = (singleTon.width * 60) / 100;
            layoutParams.height = (singleTon.width * 63) / 100;
            shareimg.setLayoutParams(layoutParams);
            shareimg.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            new FullImageViewDialog().show(items.get(getAdapterPosition()).getShared_imgage_url(), activity);
        }
    }


}
