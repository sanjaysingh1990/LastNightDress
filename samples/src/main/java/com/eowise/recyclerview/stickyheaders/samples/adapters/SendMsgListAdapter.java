package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.AbstractDataProvider2;
import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.MessageFragment;
import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.PagerSwipeItemFrameLayout;
import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.TagSelectingTextview;
import com.eowise.recyclerview.stickyheaders.samples.LndUserProfile.LndProfile;
import com.eowise.recyclerview.stickyheaders.samples.NewMessage.SendMessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.UserProfile.OtherUserProfileActivity;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.Utils.RelativeTimeTextView;
import com.eowise.recyclerview.stickyheaders.samples.data.CommentData;
import com.eowise.recyclerview.stickyheaders.samples.data.MessageData;
import com.eowise.recyclerview.stickyheaders.samples.data.MessageToFriendsData;
import com.eowise.recyclerview.stickyheaders.samples.data.PersonDataProvider;
import com.eowise.recyclerview.stickyheaders.samples.data.UserType;
import com.eowise.recyclerview.stickyheaders.samples.interfaces.TagClick;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import github.ankushsachdeva.emojicon.EmojiconTextView;

/**
 * Created by aurel on 22/09/14.
 */
public class SendMsgListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements TagClick {
    private static List<MessageData> items;
    private PersonDataProvider personDataProvider;
    static Context mContext;
    static int count = 0;
    TagSelectingTextview mTagSelectingTextview;
    public static int hashTagHyperLinkDisabled = 0;
    String hastTagColorBlue = "#be4d66";
    private final int VIEW_ITEM_SELF = 1;
    private final int VIEW_ITEM_OTHER = 2;
    private final int VIEW_ITEM_BANNER = 3;
    private final int VIEW_ITEM_IMAGE = 4;

    public SendMsgListAdapter(Context context, List<MessageData> data) {
        this.mContext = context;
        this.items = data;
        mTagSelectingTextview = new TagSelectingTextview();

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
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.chat_user2_item_image, parent, false);

            vh = new ImageMsg(v);
        }
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
            case VIEW_ITEM_IMAGE:
                ImageMsg holder4 = (ImageMsg) holder;

                try {
                    byte[] decodedString = Base64.decode(cd.getBase64_imgage_url(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    holder4.shareimg.setImageBitmap(decodedByte);
                } catch (Exception ex) {
                    Log.e("error", ex.getMessage());
                }
                holder4.timeTextView.setText(cd.getTime());
                SingleTon.imageLoader.displayImage(cd.getProfilepic(), holder4.profpic, SingleTon.options3);

                break;

        }
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();

    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getUserType() == UserType.SELF) {
            return VIEW_ITEM_SELF;
        } else if (items.get(position).getUserType() == UserType.OTHER) {
            return VIEW_ITEM_OTHER;
        } else if (items.get(position).getUserType() == UserType.BANNER) {
            return VIEW_ITEM_BANNER;
        } else if (items.get(position).getUserType() == UserType.SELF_IMAGE) {
            return VIEW_ITEM_IMAGE;
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
            profile = new Intent(mContext, LndProfile.class);
        } else {
            profile = new Intent(mContext, OtherUserProfileActivity.class);
            profile.putExtra("uname", tag.toString().trim());
            profile.putExtra("user_id", "-1");

        }

        mContext.startActivity(profile);
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

    public static class ImageMsg extends RecyclerView.ViewHolder {
        public ImageView shareimg;
        public TextView timeTextView;
        public ImageView profpic;

        public ImageMsg(View v) {
            super(v);
            timeTextView = (TextView) v.findViewById(R.id.time_text);
            profpic = (ImageView) v.findViewById(R.id.chatpicsender);
            shareimg = (ImageView) v.findViewById(R.id.shareimg);
        }


    }


}
