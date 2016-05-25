package com.eowise.recyclerview.stickyheaders.samples.StickyHeader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.HashTagsFullView.LndBrandHashTagGridViewActivity;
import com.eowise.recyclerview.stickyheaders.samples.Likers.LikersActivity;
import com.eowise.recyclerview.stickyheaders.samples.LndComments;
import com.eowise.recyclerview.stickyheaders.samples.LndMessage.SendSwapRequestActivity;
import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.TagSelectingTextview;
import com.eowise.recyclerview.stickyheaders.samples.LndUserProfile.LndProfile;
import com.eowise.recyclerview.stickyheaders.samples.LndUserProfile.LndUserFullStickyActivity;
import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.NewMessage.SendMessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.PostDataShop.DressEditPost;
import com.eowise.recyclerview.stickyheaders.samples.PostDataShop.HandBagsEditPost;
import com.eowise.recyclerview.stickyheaders.samples.PostDataShop.JewelleryEditPost;
import com.eowise.recyclerview.stickyheaders.samples.PostDataShop.ShoesEditPost;
import com.eowise.recyclerview.stickyheaders.samples.PurchaseAndSwap.RegularCheckoutActivity;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SQLDB.FavoriteData;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.UserProfile.OtherUserProfileActivity;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ConstantValues;
import com.eowise.recyclerview.stickyheaders.samples.Utils.RelativeTimeTextView;
import com.eowise.recyclerview.stickyheaders.samples.adapters.SentToAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.Chat_Banner_Data;
import com.eowise.recyclerview.stickyheaders.samples.data.FollowersFollowingData;
import com.eowise.recyclerview.stickyheaders.samples.interfaces.TagClick;
import com.init.superslim.GridSLM;
import com.init.superslim.LinearSLM;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import github.ankushsachdeva.emojicon.EmojiconTextView;

/**
 *
 */
public class LndHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements TagClick {

    private static final int VIEW_TYPE_HEADER = 0x01;

    private static final int VIEW_TYPE_CONTENT_PRIVATE_OTHER = 0x00;
    private static final int VIEW_TYPE_CONTENT_SHOP_OTHER = 0x02;
    private static final int VIEW_TYPE_CONTENT_PRIVATE_USER = 0x03;
    private static final int VIEW_TYPE_CONTENT_SHOP_USER = 0x04;
    private static final int VIEW_TYPE_CONTENT_SHOP_ITEM_SOLD = 0x05;
    private static final int VIEW_TYPE_CONTENT_PRIVATE_USER_ITEM_SOLD = 0x06;
    private static final int VIEW_TYPE_CONTENT_SHOP_ITEM_UNLOKED = 0x07;
    private static final int VIEW_TYPE_CONTENT_PRIVATE_USER_ITEM_LOCKED = 0x08;

    private static final int LINEAR = 0;

    public final ArrayList<Home_List_Data> mItems;

    private int mHeaderDisplay;

    private boolean mMarginsFixed;

    private final Activity mContext;
    List<FollowersFollowingData> users = new ArrayList<>();
    public static EditText usermessage;
    public static TextView sendcancel;
    private SentToAdapter mAdapter;
    private ProgressBar prog;
    private TextView showtext;


    TagSelectingTextview mTagSelectingTextview;
    public static int hashTagHyperLinkEnabled = 1;
    public static int hashTagHyperLinkDisabled = 0;
    String hastTagColorBlue = "#be4d66";
    public static ArrayList<String> deleteditemssposition = new ArrayList<>();

    public LndHomeAdapter(Activity context, int headerMode, ArrayList<Home_List_Data> homeitems) {
        mContext = context;

        mHeaderDisplay = headerMode;

        mItems = homeitems;
        mTagSelectingTextview = new TagSelectingTextview();

    }

    public boolean isItemHeader(int position) {
        if (mItems.get(position).sectiontype.compareTo("header") == 0)
            return true;
        else
            return false;
    }

    public String itemToString(int position) {
        return mItems.get(position).text;
    }

    @Override
    public void clickedTag(CharSequence tag) {
        if (tag.toString().startsWith("#")) {
            Intent hashtag = new Intent(mContext, LndBrandHashTagGridViewActivity.class);
            hashtag.putExtra("hashtag", tag.toString().substring(1));
            hashtag.putExtra("type", 1);

            mContext.startActivity(hashtag);
        } else if (tag.toString().startsWith("@")) {
            Intent profile;
            if (SingleTon.pref.getString("uname", "").compareToIgnoreCase(tag.toString().substring(1)) == 0) {
                //  profile = new Intent(activity, LndProfile.class);
                profile = new Intent(mContext, LndProfile.class);
            } else {
                profile = new Intent(mContext, OtherUserProfileActivity.class);
                profile.putExtra("uname", tag.toString().substring(1));
                profile.putExtra("user_id", "-1");

            }
            mContext.startActivity(profile);

        } else if (tag.toString().contains("more")) {
            String[] id = tag.toString().split(",");
            // Toast.makeText(mContext,id[0],Toast.LENGTH_SHORT).show();
            Intent likers = new Intent(mContext, LikersActivity.class);
            likers.putExtra("postid", id[0]);
            likers.putExtra("type", 2);
            mContext.startActivity(likers);
        } else {

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
    }


    private void postLiked(final int pos) {

        Home_List_Data hld = mItems.get(pos);
        final String postid = hld.getPost_id();
        final String whospost = hld.getUserid();
        if (hld != null)
            if (hld.getLikedvalue().compareTo("1") == 0)
                hld.setLikedvalue(0 + "");
            else
                hld.setLikedvalue(1 + "");
        notifyDataSetChanged();
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/lndlikeunlike.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("json", response.toString());
                try {

                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status")) {
                        int val = jobj.getInt("value");
                        if (val == 1) {
                            //  Home_List_Data hld1 = mItems.get(pos);
                            Home_List_Data hld2 = mItems.get(pos + 1);
                            if (hld2.getLikestotal() != 0) {
                                //  hld1.setLikestotal(hld1.getLikestotal() - 1);
                                hld2.setLikestotal(hld2.getLikestotal() - 1);
                                Toast.makeText(mContext, response + "", Toast.LENGTH_SHORT).show();

                            }
                            notifyDataSetChanged();
                            //notifyItemChanged(pos);
                            //notifyItemChanged(pos + 1);

                        } else {
                            // Home_List_Data hld1 = mItems.get(pos);
                            Home_List_Data hld2 = mItems.get(pos + 1);

                            // hld1.setLikestotal(hld1.getLikestotal() + 1);
                            hld2.setLikestotal(hld2.getLikestotal() + 1);
                            notifyDataSetChanged();
                            //notifyItemChanged(pos);
                            // notifyItemChanged(pos + 1);
                        }
                    }

                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage() + "");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //showing snakebar here
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", SingleTon.pref.getString("user_id", ""));
                params.put("post_id", postid);
                params.put("date_time", SingleTon.getCurrentTimeStamp());
                params.put("whos_post", whospost);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_item, parent, false);

            return new HeaderHolder(view);
        } else if (viewType == VIEW_TYPE_CONTENT_PRIVATE_OTHER) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_private_item, parent, false);
            return new LndProductPrivateHolder(view, mContext);
        }
        else if (viewType == VIEW_TYPE_CONTENT_PRIVATE_USER_ITEM_LOCKED) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_private_item_locked, parent, false);
            return new LndProductPrivateHolder(view, mContext);
        }
        else if (viewType == VIEW_TYPE_CONTENT_SHOP_OTHER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_shop_item, parent, false);
            return new LndProductShopHolder(view, mContext);

        }
        else if (viewType == VIEW_TYPE_CONTENT_SHOP_ITEM_UNLOKED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_shop_item_unlocked, parent, false);
            return new LndProductShopHolder(view, mContext);

        }
        else if (viewType == VIEW_TYPE_CONTENT_PRIVATE_USER) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_user_private_post, parent, false);
            return new LndProductPrivateUserHolder(view, mContext);
        } else if (viewType == VIEW_TYPE_CONTENT_PRIVATE_USER_ITEM_SOLD) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_private_item_sold, parent, false);
            return new LndProductPrivateHolderSold(view, mContext);
        } else if (viewType == VIEW_TYPE_CONTENT_SHOP_ITEM_SOLD) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_shop_item_sold, parent, false);
            return new LndProductShopHolderSold(view, mContext);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_user_shop_post, parent, false);
            return new LndProductShopUserHolder(view, mContext);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final Home_List_Data item = mItems.get(position);

        final View itemView = holder.itemView;
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_HEADER:

                HeaderHolder vh1 = (HeaderHolder) holder;


                vh1.uname.setText(Capitalize.capitalize(item.getUname()));
                vh1.brandname.setText(Capitalize.capitalize(item.getBrandname()));

                if (item.getLikedvalue().compareTo("1") == 0)
                    vh1.likebtn.setImageResource(R.drawable.liked_icon);
                else
                    vh1.likebtn.setImageResource(R.drawable.like_icon);
                if (item.getHeadertype() == 1) {
                    vh1.headertop.setVisibility(View.VISIBLE);
                    vh1.activitydoneby.setMovementMethod(LinkMovementMethod.getInstance());

                    if (item.getNotitotallikers() > 2)

                    {
                        String[] users = item.getNotilikedby().split(",");
                        vh1.activitydoneby.setText(mTagSelectingTextview.addClickablePart(users[0] + " and " + (item.getNotitotallikers() - 1) + " more liked this",
                                this, hashTagHyperLinkDisabled, hastTagColorBlue, users[0].length(), ((item.getNotitotallikers() - 1) + " more").length(), item.getPost_id()),
                                TextView.BufferType.SPANNABLE);


                    } else if (item.getNotitotallikers() == 1) {
                        String[] users = item.getNotilikedby().split(",");

                        vh1.activitydoneby.setText(mTagSelectingTextview.addClickablePart(Capitalize.capitalizeFirstLetter(users[0] + " liked this."),
                                this, hashTagHyperLinkDisabled, hastTagColorBlue, users[0].length()),
                                TextView.BufferType.SPANNABLE);

                    } else {
                        String[] users = item.getNotilikedby().split(",");
                        vh1.activitydoneby.setText(mTagSelectingTextview.addClickablePart(Capitalize.capitalizeFirstLetter(users[0]) + " and " + users[1] + " liked this.",
                                this, hashTagHyperLinkDisabled, hastTagColorBlue, users[0].length(), users[1].length(), ""),
                                TextView.BufferType.SPANNABLE);

                    }
                } else if (item.getHeadertype() == 2) {
                    vh1.headertop.setVisibility(View.VISIBLE);

                    //  vh1.activitytype.setText("commented on this.");
                } else if (item.getHeadertype() == 3) {
                    vh1.headertop.setVisibility(View.VISIBLE);
                    vh1.activitydoneby.setText("Omid Fatahi");
                    // vh1.activitytype.setText("was mentioned in this post.");
                } else

                    vh1.headertop.setVisibility(View.GONE);
                //setting profile pic
                SingleTon.imageLoader.displayImage(item.getProfilepicurl(), vh1.profilepic, SingleTon.options3);
                break;
            case VIEW_TYPE_CONTENT_PRIVATE_OTHER:
                LndProductPrivateHolder vh2 = (LndProductPrivateHolder) holder;


                String uname = Capitalize.capitalizeFirstLetter(item.getUname());
                //end here
                vh2.description.setMovementMethod(LinkMovementMethod.getInstance());
                vh2.description.setText(mTagSelectingTextview.addClickablePart(uname + " " + item.getDescription(),
                        this, hashTagHyperLinkDisabled, hastTagColorBlue, uname.length()),
                        TextView.BufferType.SPANNABLE);
                vh2.price_was.setText("$" + item.getPricewas());
                vh2.price_now.setText("$" + item.getPricenow());
                vh2.time.setReferenceTime(item.getTime());
                //bag size
                try {
                    int pos = Integer.parseInt(item.getSize());
                    vh2.len.setText(ConstantValues.bagsize[pos]);

                } catch (Exception ex) {

                    vh2.len.setText((item.getSize().toUpperCase()));

                }
                vh2.likescount.setText(item.getLikestotal() + " likes");
                //start

                try {
                    vh2.condition.setText(ConstantValues.condition[Integer.parseInt(item.getConditon())]);

                } catch (Exception ex) {

                }

                //for private dress color and private metal type

                vh2.color.setText(Capitalize.capitalizeFirstLetter(item.getColors()));


                try {
                    vh2.hfl.setFeatureItems(vh2.forward, vh2.backward, position, this, vh2.progress);
                    if (position == mItems.size() - 1) {
                        vh2.spaceview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
                        notifyDataSetChanged();
                    }
                    int pos = Integer.parseInt(item.getConditon());
                    vh2.condition.setText(ConstantValues.condition[pos]);
                } catch (Exception ex) {

                }
                //likers view
                vh2.likescount.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent likers = new Intent(mContext, LikersActivity.class);
                        likers.putExtra("postid", item.getPost_id());
                        likers.putExtra("type", 1);
                        mContext.startActivity(likers);
                    }
                });
                //check favorate
                if (item.isfavorate())
                    vh2.favorates.setImageResource(R.drawable.filled_favorate_icon);
                else
                    vh2.favorates.setImageResource(R.drawable.favorite_icon);


                break;
            case VIEW_TYPE_CONTENT_SHOP_OTHER:
                // Log.e("colors",item.getColors());
                 LndProductShopHolder vh3 = (LndProductShopHolder) holder;


                uname = Capitalize.capitalizeFirstLetter(item.getUname());
                //end here
                vh3.description.setMovementMethod(LinkMovementMethod.getInstance());
                vh3.description.setText(mTagSelectingTextview.addClickablePart(uname + " " + item.getDescription(),
                        this, hashTagHyperLinkDisabled, hastTagColorBlue, uname.length()),
                        TextView.BufferType.SPANNABLE);
                vh3.price_was.setText("$" + item.getPricewas());
                vh3.price_now.setText("$" + item.getPricenow());
                vh3.likescount.setText(item.getLikestotal() + " likes");
                vh3.time.setReferenceTime(item.getTime());


                try {
                    vh3.hfl.setFeatureItems(vh3.forward, vh3.backward, position, this, vh3.progress);
                    if (position == mItems.size() - 1) {
                        vh3.spaceview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
                    }
                    int pos = Integer.parseInt(item.getConditon());
                    vh3.condition.setText(ConstantValues.condition[pos]);

                } catch (Exception ex) {

                }
                //checking for single or multiple sizes
                try {
                    if (item.getSize().split(",").length == 1) {
                        vh3.size.setText(Capitalize.capitalize(item.getSize()));
                        vh3.size.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        vh3.size.setClickable(false);
                    } else {
                        vh3.size.setClickable(true);
                        vh3.size.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                        vh3.size.setText("Size");
                        vh3.size.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow(item.getSize(), (TextView) view, item.getCategory()).showAsDropDown(view, -5, 0);


                            }
                        });

                    }
                } catch (Exception ex) {

                }
                //likers view
                vh3.likescount.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent likers = new Intent(mContext, LikersActivity.class);
                        likers.putExtra("username", SingleTon.pref.getString("uname", ""));
                        likers.putExtra("postid", item.getPost_id());
                        likers.putExtra("type", 1);

                        mContext.startActivity(likers);
                    }
                });

                //for single or multiple colors
                try {
                    if (item.getColors().split(",").length == 1) {
                        vh3.color.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        //for private dress color and private metal type

                        vh3.color.setText(Capitalize.capitalizeFirstLetter(item.getColors()));

                        vh3.color.setClickable(false);
                    } else {
                        vh3.color.setClickable(true);
                        vh3.color.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                        vh3.color.setText("Color  ");
                        vh3.color.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow2(item.getColors(), (TextView) view, item.getCategory()).showAsDropDown(view, -5, 0);


                            }
                        });
                    }
                } catch (Exception ex) {

                }

                //check favorate
                if (item.isfavorate())
                    vh3.favorates.setImageResource(R.drawable.filled_favorate_icon);
                else
                    vh3.favorates.setImageResource(R.drawable.favorite_icon);


                break;

            case VIEW_TYPE_CONTENT_SHOP_ITEM_UNLOKED:
                // Log.e("colors",item.getColors());
                vh3 = (LndProductShopHolder) holder;


                uname = Capitalize.capitalizeFirstLetter(item.getUname());
                //end here
                vh3.description.setMovementMethod(LinkMovementMethod.getInstance());
                vh3.description.setText(mTagSelectingTextview.addClickablePart(uname + " " + item.getDescription(),
                        this, hashTagHyperLinkDisabled, hastTagColorBlue, uname.length()),
                        TextView.BufferType.SPANNABLE);
                vh3.price_was.setText("$" + item.getPricewas());
                vh3.price_now.setText("$" + item.getPricenow());
                vh3.likescount.setText(item.getLikestotal() + " likes");
                vh3.time.setReferenceTime(item.getTime());


                try {
                    vh3.hfl.setFeatureItems(vh3.forward, vh3.backward, position, this, vh3.progress);
                    if (position == mItems.size() - 1) {
                        vh3.spaceview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
                    }
                    int pos = Integer.parseInt(item.getConditon());
                    vh3.condition.setText(ConstantValues.condition[pos]);

                } catch (Exception ex) {

                }
                //checking for single or multiple sizes
                try {
                    if (item.getSize().split(",").length == 1) {
                        vh3.size.setText(Capitalize.capitalize(item.getSize()));
                        vh3.size.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        vh3.size.setClickable(false);
                    } else {
                        vh3.size.setClickable(true);
                        vh3.size.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                        vh3.size.setText("Size");
                        vh3.size.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow(item.getSize(), (TextView) view, item.getCategory()).showAsDropDown(view, -5, 0);


                            }
                        });

                    }
                } catch (Exception ex) {

                }
                //likers view
                vh3.likescount.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent likers = new Intent(mContext, LikersActivity.class);
                        likers.putExtra("username", SingleTon.pref.getString("uname", ""));
                        likers.putExtra("postid", item.getPost_id());
                        likers.putExtra("type", 1);

                        mContext.startActivity(likers);
                    }
                });

                //for single or multiple colors
                try {
                    if (item.getColors().split(",").length == 1) {
                        vh3.color.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        //for private dress color and private metal type

                        vh3.color.setText(Capitalize.capitalizeFirstLetter(item.getColors()));

                        vh3.color.setClickable(false);
                    } else {
                        vh3.color.setClickable(true);
                        vh3.color.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                        vh3.color.setText("Color  ");
                        vh3.color.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow2(item.getColors(), (TextView) view, item.getCategory()).showAsDropDown(view, -5, 0);


                            }
                        });
                    }
                } catch (Exception ex) {

                }

                //check favorate
                if (item.isfavorate())
                    vh3.favorates.setImageResource(R.drawable.filled_favorate_icon);
                else
                    vh3.favorates.setImageResource(R.drawable.favorite_icon);


                break;
            case VIEW_TYPE_CONTENT_PRIVATE_USER_ITEM_LOCKED:
                vh2 = (LndProductPrivateHolder) holder;


                 uname = Capitalize.capitalizeFirstLetter(item.getUname());
                //end here
                vh2.description.setMovementMethod(LinkMovementMethod.getInstance());
                vh2.description.setText(mTagSelectingTextview.addClickablePart(uname + " " + item.getDescription(),
                        this, hashTagHyperLinkDisabled, hastTagColorBlue, uname.length()),
                        TextView.BufferType.SPANNABLE);
                vh2.price_was.setText("$" + item.getPricewas());
                vh2.price_now.setText("$" + item.getPricenow());
                vh2.time.setReferenceTime(item.getTime());
                //bag size
                try {
                    int pos = Integer.parseInt(item.getSize());
                    vh2.len.setText(ConstantValues.bagsize[pos]);

                } catch (Exception ex) {

                    vh2.len.setText((item.getSize().toUpperCase()));

                }
                vh2.likescount.setText(item.getLikestotal() + " likes");
                //start

                try {
                    vh2.condition.setText(ConstantValues.condition[Integer.parseInt(item.getConditon())]);

                } catch (Exception ex) {

                }

                //for private dress color and private metal type

                vh2.color.setText(Capitalize.capitalizeFirstLetter(item.getColors()));


                try {
                    vh2.hfl.setFeatureItems(vh2.forward, vh2.backward, position, this, vh2.progress);
                    if (position == mItems.size() - 1) {
                        vh2.spaceview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
                        notifyDataSetChanged();
                    }
                    int pos = Integer.parseInt(item.getConditon());
                    vh2.condition.setText(ConstantValues.condition[pos]);
                } catch (Exception ex) {

                }
                //likers view
                vh2.likescount.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent likers = new Intent(mContext, LikersActivity.class);
                        likers.putExtra("postid", item.getPost_id());
                        likers.putExtra("type", 1);
                        mContext.startActivity(likers);
                    }
                });
                //check favorate
                if (item.isfavorate())
                    vh2.favorates.setImageResource(R.drawable.filled_favorate_icon);
                else
                    vh2.favorates.setImageResource(R.drawable.favorite_icon);


                break;
            case VIEW_TYPE_CONTENT_PRIVATE_USER:
                LndProductPrivateUserHolder vh4 = (LndProductPrivateUserHolder) holder;
                uname = Capitalize.capitalizeFirstLetter(item.getUname());

                //checking description for hashtag and usermention
                vh4.description.setMovementMethod(LinkMovementMethod.getInstance());
                vh4.description.setText(mTagSelectingTextview.addClickablePart(uname + " " + item.getDescription(),
                        this, hashTagHyperLinkDisabled, hastTagColorBlue, uname.length()),
                        TextView.BufferType.SPANNABLE);
                //price was and price now
                vh4.price_was.setText("$" + item.getPricewas());
                vh4.price_now.setText("$" + item.getPricenow());
                //bag size
                try {
                    int pos = Integer.parseInt(item.getSize());
                    vh4.len.setText(ConstantValues.bagsize[pos]);

                } catch (Exception ex) {

                    vh4.len.setText((item.getSize().toUpperCase()));

                }
                try {
                    vh4.condition.setText(ConstantValues.condition[Integer.parseInt(item.getConditon())]);

                } catch (Exception ex) {

                }
                vh4.likescount.setText(item.getLikestotal() + " likes");
                vh4.color.setText(item.getColors());
                vh4.time.setReferenceTime(item.getTime());

                //for private dress color and private metal type


                vh4.color.setText(Capitalize.capitalizeFirstLetter(item.getColors()));


                try {
                    vh4.hfl.setFeatureItems(vh4.forward, vh4.backward, position, this, vh4.progress);
                    if (position == mItems.size() - 1) {
                        vh4.spaceview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
                        notifyDataSetChanged();
                    }
                    int pos = Integer.parseInt(item.getConditon());
                    vh4.condition.setText(ConstantValues.condition[pos]);
                } catch (Exception ex) {

                }

                //likers view
                vh4.likescount.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent likers = new Intent(mContext, LikersActivity.class);
                        likers.putExtra("user_id", SingleTon.pref.getString("user_id", ""));
                        likers.putExtra("postid", item.getPost_id());
                        likers.putExtra("type", 1);

                        mContext.startActivity(likers);
                    }
                });


                break;
            case VIEW_TYPE_CONTENT_SHOP_USER:
                final LndProductShopUserHolder vh5 = (LndProductShopUserHolder) holder;
                uname = Capitalize.capitalizeFirstLetter(item.getUname());
                //checking description for hashtag and usermention
                vh5.description.setMovementMethod(LinkMovementMethod.getInstance());
                vh5.description.setText(mTagSelectingTextview.addClickablePart(uname + " " + item.getDescription(),
                        this, hashTagHyperLinkDisabled, hastTagColorBlue, uname.length()),
                        TextView.BufferType.SPANNABLE);

                //price was and price now
                vh5.price_was.setText("$" + item.getPricewas());
                vh5.price_now.setText("$" + item.getPricenow());
                vh5.likescount.setText(item.getLikestotal() + " likes");
                vh5.time.setReferenceTime(item.getTime());

                try {
                    vh5.hfl.setFeatureItems(vh5.forward, vh5.backward, position, this, vh5.progress);
                    if (position == mItems.size() - 1) {

                        vh5.spaceview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80));
                    }
                    int pos = Integer.parseInt(item.getConditon());
                    vh5.condition.setText(ConstantValues.condition[pos]);

                } catch (Exception ex) {

                }
                ///checking for single or multiple sizes
                try {
                    if (item.getSize().split(",").length == 1) {
                        vh5.size.setText((item.getSize()).toUpperCase());
                        vh5.size.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        vh5.size.setClickable(false);
                    } else {
                        vh5.size.setClickable(true);
                        vh5.size.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);

                        vh5.size.setText("Size");
                        vh5.size.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow(item.getSize(), (TextView) view, item.getCategory()).showAsDropDown(view, -5, 0);


                            }
                        });

                    }
                } catch (Exception ex) {
                    Log.e("error", ex.getMessage() + "");
                }
                //likers view
                vh5.likescount.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent likers = new Intent(mContext, LikersActivity.class);
                        likers.putExtra("username", SingleTon.pref.getString("uname", ""));
                        likers.putExtra("postid", item.getPost_id());
                        likers.putExtra("type", 1);

                        mContext.startActivity(likers);
                    }
                });


                //for single or multiple colors
                try {
                    if (item.getColors().split(",").length == 1) {
                        vh5.color.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        //for private dress color and private metal type


                        if (item.getCategory() == 4) {
                            int pos = Integer.parseInt(item.getColors());
                            vh5.color.setText(Capitalize.capitalizeFirstLetter(ConstantValues.metaltype[pos]));
                        } else
                            vh5.color.setText(Capitalize.capitalizeFirstLetter(item.getColors()));

                        vh5.color.setClickable(false);
                    } else {
                        vh5.color.setClickable(true);
                        vh5.color.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                        if (item.getCategory() == 4)
                            vh5.color.setText("Metal Type");
                        else
                            vh5.color.setText("Color  ");
                        vh5.color.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow2(item.getColors(), (TextView) view, item.getCategory()).showAsDropDown(view, -5, 0);


                            }
                        });
                    }
                } catch (Exception ex) {

                }


                break;

            case VIEW_TYPE_CONTENT_PRIVATE_USER_ITEM_SOLD:
                LndProductPrivateHolderSold vh6 = (LndProductPrivateHolderSold) holder;


                uname = Capitalize.capitalizeFirstLetter(item.getUname());
                //end here
                vh6.description.setMovementMethod(LinkMovementMethod.getInstance());
                vh6.description.setText(mTagSelectingTextview.addClickablePart(uname + " " + item.getDescription(),
                        this, hashTagHyperLinkDisabled, hastTagColorBlue, uname.length()),
                        TextView.BufferType.SPANNABLE);
                vh6.price_was.setText("$" + item.getPricewas());
                vh6.price_now.setText("$" + item.getPricenow());
                vh6.time.setReferenceTime(item.getTime());
                //to check sold or not

                Log.e("itemsold private other", item.getIssold() + "");
                //bag size
                try {
                    int pos = Integer.parseInt(item.getSize());
                    vh6.len.setText(ConstantValues.bagsize[pos]);

                } catch (Exception ex) {

                    vh6.len.setText((item.getSize().toUpperCase()));

                }
                vh6.likescount.setText(item.getLikestotal() + " likes");
                //start

                try {
                    vh6.condition.setText(ConstantValues.condition[Integer.parseInt(item.getConditon())]);

                } catch (Exception ex) {

                }

                //for private dress color and private metal type

                vh6.color.setText(Capitalize.capitalizeFirstLetter(item.getColors()));


                try {
                    vh6.hfl.setFeatureItems(vh6.forward, vh6.backward, position, this, vh6.progress);
                    if (position == mItems.size() - 1) {
                        vh6.spaceview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
                        notifyDataSetChanged();
                    }
                    int pos = Integer.parseInt(item.getConditon());
                    vh6.condition.setText(ConstantValues.condition[pos]);
                } catch (Exception ex) {

                }
                //likers view
                vh6.likescount.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent likers = new Intent(mContext, LikersActivity.class);
                        likers.putExtra("postid", item.getPost_id());
                        likers.putExtra("type", 1);

                        mContext.startActivity(likers);
                    }
                });
                //check favorate
                if (item.isfavorate())
                    vh6.favorates.setImageResource(R.drawable.filled_favorate_icon);
                else
                    vh6.favorates.setImageResource(R.drawable.favorite_icon);


                break;

            case VIEW_TYPE_CONTENT_SHOP_ITEM_SOLD:
                // Log.e("colors",item.getColors());
                final LndProductShopHolderSold vh7 = (LndProductShopHolderSold) holder;


                uname = Capitalize.capitalizeFirstLetter(item.getUname());
                //end here
                vh7.description.setMovementMethod(LinkMovementMethod.getInstance());
                vh7.description.setText(mTagSelectingTextview.addClickablePart(uname + " " + item.getDescription(),
                        this, hashTagHyperLinkDisabled, hastTagColorBlue, uname.length()),
                        TextView.BufferType.SPANNABLE);
                vh7.price_was.setText("$" + item.getPricewas());
                vh7.price_now.setText("$" + item.getPricenow());
                vh7.likescount.setText(item.getLikestotal() + " likes");
                vh7.time.setReferenceTime(item.getTime());
                //to check sold or not

                Log.e("itemsold shop other", item.getIssold() + "");


                try {
                    vh7.hfl.setFeatureItems(vh7.forward, vh7.backward, position, this, vh7.progress);
                    if (position == mItems.size() - 1) {
                        vh7.spaceview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
                    }
                    int pos = Integer.parseInt(item.getConditon());
                    vh7.condition.setText(ConstantValues.condition[pos]);

                } catch (Exception ex) {

                }
                //checking for single or multiple sizes
                try {
                    if (item.getSize().split(",").length == 1) {
                        vh7.size.setText(Capitalize.capitalize(item.getSize()));
                        vh7.size.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        vh7.size.setClickable(false);
                    } else {
                        vh7.size.setClickable(true);
                        vh7.size.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                        vh7.size.setText("Size");
                        vh7.size.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow(item.getSize(), (TextView) view, item.getCategory()).showAsDropDown(view, -5, 0);


                            }
                        });

                    }
                } catch (Exception ex) {

                }
                //likers view
                vh7.likescount.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent likers = new Intent(mContext, LikersActivity.class);
                        likers.putExtra("username", SingleTon.pref.getString("uname", ""));
                        likers.putExtra("postid", item.getPost_id());
                        likers.putExtra("type", 1);

                        mContext.startActivity(likers);
                    }
                });

                //for single or multiple colors
                try {
                    if (item.getColors().split(",").length == 1) {
                        vh7.color.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        //for private dress color and private metal type

                        vh7.color.setText(Capitalize.capitalizeFirstLetter(item.getColors()));

                        vh7.color.setClickable(false);
                    } else {
                        vh7.color.setClickable(true);
                        vh7.color.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                        vh7.color.setText("Color  ");
                        vh7.color.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow2(item.getColors(), (TextView) view, item.getCategory()).showAsDropDown(view, -5, 0);


                            }
                        });
                    }
                } catch (Exception ex) {

                }

                //check favorate
                if (item.isfavorate())
                    vh7.favorates.setImageResource(R.drawable.filled_favorate_icon);
                else
                    vh7.favorates.setImageResource(R.drawable.favorite_icon);


                break;
            default:
                break;
        }


        final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());
        // Overrides xml attrs, could use different layouts too.
        if (item.sectiontype.compareTo("header") == 0) {
            lp.headerDisplay = mHeaderDisplay;
            if (lp.isHeaderInline() || (mMarginsFixed && !lp.isHeaderOverlay())) {
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            lp.headerEndMarginIsAuto = !mMarginsFixed;
            lp.headerStartMarginIsAuto = !mMarginsFixed;
        }
        lp.setSlm(item.sectionManager == LINEAR ? LinearSLM.ID : GridSLM.ID);
        //  lp.setColumnWidth(mContext.getResources().getDimensionPixelSize(R.dimen.grid_column_width));
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.setFirstPosition(item.sectionFirstPosition);
        itemView.setLayoutParams(lp);
    }


    public PopupWindow popupWindow(String sizes, TextView size, int category) {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(mContext);

        // the drop down list is a list view
        ListView listViewDogs = new ListView(mContext);

        // set our adapter and pass our pop up window contents
        listViewDogs.setAdapter(popupAdapter(sizes.split(",")));
        // set the item click listener
        listViewDogs.setOnItemClickListener(new DropdownOnItemClickListenerSize(popupWindow, size, sizes));

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(100);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        popupWindow.setContentView(listViewDogs);

        return popupWindow;
    }

    public PopupWindow popupWindow2(String sizes, TextView size, int category) {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(mContext);

        // the drop down list is a list view
        ListView listViewDogs = new ListView(mContext);

        // set our adapter and pass our pop up window contents
        listViewDogs.setAdapter(popupAdapter(sizes.split(",")));

        // set the item click listener
        listViewDogs.setOnItemClickListener(new DropdownOnItemClickListenerColorandMetaltype(popupWindow, size, sizes));

        // some other visual settings
        popupWindow.setFocusable(true);
        // some other visual settings
        popupWindow.setFocusable(true);
        if (category == 4)
            popupWindow.setWidth(250);
        else
            popupWindow.setWidth(170);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        popupWindow.setContentView(listViewDogs);

        return popupWindow;
    }

    public class DropdownOnItemClickListenerSize implements AdapterView.OnItemClickListener {

        PopupWindow popupwindow;
        TextView size;
        String sizevalues[];


        public DropdownOnItemClickListenerSize(PopupWindow popupWindow, TextView sizecolor, String sizevalues) {
            this.popupwindow = popupWindow;
            this.size = sizecolor;
            this.sizevalues = sizevalues.split(",");

        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {

            // get the context and main activity to access variables
            Context mContext = v.getContext();

            // add some animation when a list item was clicked
            Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
            fadeInAnimation.setDuration(10);
            v.startAnimation(fadeInAnimation);
            popupwindow.dismiss();


            size.setText(Capitalize.capitalizeFirstLetter(sizevalues[pos]));


            //notifyDataSetChanged();

        }

    }

    public class DropdownOnItemClickListenerColorandMetaltype implements AdapterView.OnItemClickListener {

        PopupWindow popupwindow;
        TextView sizecolor;
        String sizecolorvalues[];

        public DropdownOnItemClickListenerColorandMetaltype(PopupWindow popupWindow, TextView sizecolor, String sizescolors) {
            this.popupwindow = popupWindow;
            this.sizecolor = sizecolor;
            this.sizecolorvalues = sizescolors.split(",");
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {

            // get the context and main activity to access variables
            Context mContext = v.getContext();

            // add some animation when a list item was clicked
            Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
            fadeInAnimation.setDuration(10);
            v.startAnimation(fadeInAnimation);
            popupwindow.dismiss();


            sizecolor.setText(Capitalize.capitalizeFirstLetter(sizecolorvalues[pos]));


            //notifyDataSetChanged();

        }

    }

    private ArrayAdapter<String> popupAdapter(String popArray[]) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, popArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                // setting the ID and text for every items in the list

                String text = "";
                // setting the ID and text for every items in the list
                try {
                    text = Capitalize.capitalizeFirstLetter(getItem(position));


                } catch (Exception ex) {

                }


                // visual settings for the list item
                TextView listItem = new TextView(mContext);

                listItem.setText(text);

                listItem.setTag(position);
                listItem.setTextSize(16);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor(Color.WHITE);

                return listItem;
            }
        };

        return adapter;
    }


    @Override
    public int getItemViewType(int position) {
        Home_List_Data lineItem = mItems.get(position);

        if (lineItem.sectiontype.compareTo("header") == 0)
            return VIEW_TYPE_HEADER;
        else if (lineItem.sectiontype.compareTo("contentotheruser") == 0) {

            if (lineItem.isprivate) {
                //to check availability
                if (lineItem.getIssold() == 1) {
                    if (lineItem.getSwapstatus() == 0)
                        return VIEW_TYPE_CONTENT_PRIVATE_OTHER;
                    else
                        return VIEW_TYPE_CONTENT_PRIVATE_USER_ITEM_LOCKED;//layout to show off
                } else
                    return VIEW_TYPE_CONTENT_PRIVATE_USER_ITEM_SOLD;

            } else {
                if (lineItem.getIssold() == 1) {
                    if(lineItem.getSwapstatus()==0)
                    return VIEW_TYPE_CONTENT_SHOP_OTHER;
                    else
                        return VIEW_TYPE_CONTENT_SHOP_ITEM_UNLOKED;

                }
                    else
                    return VIEW_TYPE_CONTENT_SHOP_ITEM_SOLD;

            }
        } else {

            if (lineItem.isprivate)
                return VIEW_TYPE_CONTENT_PRIVATE_USER;
            else
                return VIEW_TYPE_CONTENT_SHOP_USER;
        }


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setHeaderDisplay(int headerDisplay) {
        mHeaderDisplay = headerDisplay;
        notifyHeaderChanges();
    }

    public void setMarginsFixed(boolean marginsFixed) {
        mMarginsFixed = marginsFixed;
        notifyHeaderChanges();
    }

    private void notifyHeaderChanges() {
        for (int i = 0; i < mItems.size(); i++) {
            Home_List_Data item = mItems.get(i);
            if (item.sectiontype.compareTo("header") == 0) {
                notifyItemChanged(i);
            }
        }
    }


    class HeaderHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public TextView uname;
        public TextView brandname;
        public ImageView profilepic;
        public ImageButton likebtn;
        public LinearLayout headertop;
        public TextView activitydoneby;


        HeaderHolder(View view) {
            super(view);
            uname = (TextView) view.findViewById(R.id.uname);
            brandname = (TextView) view.findViewById(R.id.brandname);
            profilepic = (ImageView) view.findViewById(R.id.profilepic);
            likebtn = (ImageButton) view.findViewById(R.id.likesclick);
            //setting fonts
            uname.setTypeface(SingleTon.robotomedium);
            brandname.setTypeface(SingleTon.robotoregular);
            headertop = (LinearLayout) view.findViewById(R.id.headertop);
            activitydoneby = (TextView) view.findViewById(R.id.activitydoneby);

            uname.setOnClickListener(this);
            likebtn.setOnClickListener(this);

        }


        @Override

        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.uname:
                    Intent profile;
                    if (SingleTon.pref.getString("user_id", "").compareToIgnoreCase(mItems.get(getAdapterPosition()).getUserid()) == 0) {
                        profile = new Intent(mContext, LndProfile.class);
                    } else {
                        profile = new Intent(mContext, OtherUserProfileActivity.class);
                        profile.putExtra("uname", mItems.get(getAdapterPosition()).getUname());
                        profile.putExtra("user_id", mItems.get(getAdapterPosition()).getUserid());
                    }
                    mContext.startActivity(profile);
                    break;

                case R.id.likesclick:
                    postLiked(getAdapterPosition());
                    break;
               /* case R.id.activitydoneby:
                    profile = new Intent(mContext, OtherUserProfileActivity.class);
                    profile.putExtra("uname", ((TextView) view).getText().toString().trim());
                    profile.putExtra("user_id", "-1");
                    mContext.startActivity(profile);
                    break;*/
            }

        }
    }

    class LndProductPrivateHolderSold extends RecyclerView.ViewHolder implements OnClickListener {

        private TextView color;
        public HomeImageSliderLayout hfl;
        public EmojiconTextView description;
        public TextView len;
        public TextView price_was, price_now;
        public ImageButton msgtouser, favorates, sendto;

        Context con;
        ImageButton forward, backward;
        public TextView likescount;
        public View spaceview;
        public TextView condition;
        public TextView comment;
        public RelativeTimeTextView time;
        public ProgressBar progress;

        LndProductPrivateHolderSold(View view, Context context) {
            super(view);
            con = context;
            //setting height
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

//setting margins around imageimageview
            params.height = (height * 70) / 100; //left, top, right, bottom
            params.width = width;
            description = (EmojiconTextView) view.findViewById(R.id.desc);
            price_was = (TextView) view.findViewById(R.id.pricewas);
            price_now = (TextView) view.findViewById(R.id.pricenow);
            len = (TextView) view.findViewById(R.id.len);
            msgtouser = (ImageButton) view.findViewById(R.id.messagetouser);
            favorates = (ImageButton) view.findViewById(R.id.favorate);
            sendto = (ImageButton) view.findViewById(R.id.sendto);
            likescount = (TextView) view.findViewById(R.id.likescount);
            condition = (TextView) view.findViewById(R.id.condition);
            color = (TextView) view.findViewById(R.id.color);
            hfl = (HomeImageSliderLayout) view.findViewById(R.id.switcher);
            hfl.setLayoutParams(params);
            forward = (ImageButton) view.findViewById(R.id.forward);
            backward = (ImageButton) view.findViewById(R.id.backward);
            spaceview = view.findViewById(R.id.space);
            comment = (TextView) itemView.findViewById(R.id.comment);
            time = (RelativeTimeTextView) view.findViewById(R.id.time);
            progress = (ProgressBar) view.findViewById(R.id.fullpostloading);
            //bind with listeners
          /*  this.sendto.setOnClickListener(this);
            this.msgtouser.setOnClickListener(this);
            this.favorates.setOnClickListener(this);
            this.comment.setOnClickListener(this);*/

        }


        @Override
        public void onClick(View v) {
            show(v, getAdapterPosition());
        }

    }

    class LndProductShopHolderSold extends RecyclerView.ViewHolder implements OnClickListener {

        public HomeImageSliderLayout hfl;
        public EmojiconTextView description;
        public TextView price_was, price_now;
        public ImageButton msgtouser, favorates, sendto;

        public TextView likescount;
        public TextView condition;
        public TextView comment;
        public RelativeTimeTextView time;
        Context con;
        ImageButton forward, backward;
        public TextView size, color;
        public View spaceview;
        public ProgressBar progress;

        LndProductShopHolderSold(View view, Context context) {
            super(view);
            con = context;
            //setting height
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

//setting margins around imageimageview
            params.height = (height * 70) / 100; //left, top, right, bottom
            params.width = width;
            description = (EmojiconTextView) view.findViewById(R.id.desc);
            price_was = (TextView) view.findViewById(R.id.pricewas);
            price_now = (TextView) view.findViewById(R.id.pricenow);
            msgtouser = (ImageButton) view.findViewById(R.id.messagetouser);
            favorates = (ImageButton) view.findViewById(R.id.favorate);
            sendto = (ImageButton) view.findViewById(R.id.sendto);

            comment = (TextView) itemView.findViewById(R.id.comment);
            time = (RelativeTimeTextView) view.findViewById(R.id.time);

            likescount = (TextView) view.findViewById(R.id.likescount);
            condition = (TextView) view.findViewById(R.id.condition);
            // pattern = (TextView) view.findViewById(R.id.pattern);
            hfl = (HomeImageSliderLayout) view.findViewById(R.id.switcher);
            hfl.setLayoutParams(params);
            forward = (ImageButton) view.findViewById(R.id.forward);
            backward = (ImageButton) view.findViewById(R.id.backward);
            spaceview = view.findViewById(R.id.space);
            progress = (ProgressBar) view.findViewById(R.id.fullpostloading);

            size = (TextView) view.findViewById(R.id.size);
            color = (TextView) view.findViewById(R.id.color);

            description = (EmojiconTextView) view.findViewById(R.id.desc);


            //bind with listeners
          /*  this.buy.setOnClickListener(this);

            this.sendto.setOnClickListener(this);
            this.msgtouser.setOnClickListener(this);
            this.favorates.setOnClickListener(this);
            this.comment.setOnClickListener(this);*/


        }


        @Override
        public void onClick(View view) {
            show(view, getAdapterPosition());
        }
    }

    class LndProductPrivateHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private TextView color;
        public HomeImageSliderLayout hfl;
        public EmojiconTextView description;
        public TextView len;
        public TextView price_was, price_now;
        public ImageButton msgtouser, favorates, sendto;
        private TextView buy, swap;
        Context con;
        ImageButton forward, backward;
        public TextView likescount;
        public View spaceview;
        public TextView condition;
        public TextView comment;
        public RelativeTimeTextView time;
        public ProgressBar progress;

        LndProductPrivateHolder(View view, Context context) {
            super(view);
            con = context;
            //setting height
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

//setting margins around imageimageview
            params.height = (height * 70) / 100; //left, top, right, bottom
            params.width = width;
            description = (EmojiconTextView) view.findViewById(R.id.desc);
            price_was = (TextView) view.findViewById(R.id.pricewas);
            price_now = (TextView) view.findViewById(R.id.pricenow);
            len = (TextView) view.findViewById(R.id.len);
            msgtouser = (ImageButton) view.findViewById(R.id.messagetouser);
            favorates = (ImageButton) view.findViewById(R.id.favorate);
            sendto = (ImageButton) view.findViewById(R.id.sendto);
            buy = (TextView) itemView.findViewById(R.id.buy);
            swap = (TextView) itemView.findViewById(R.id.swap);
            likescount = (TextView) view.findViewById(R.id.likescount);
            condition = (TextView) view.findViewById(R.id.condition);
            color = (TextView) view.findViewById(R.id.color);
            hfl = (HomeImageSliderLayout) view.findViewById(R.id.switcher);
            hfl.setLayoutParams(params);
            forward = (ImageButton) view.findViewById(R.id.forward);
            backward = (ImageButton) view.findViewById(R.id.backward);
            spaceview = view.findViewById(R.id.space);
            comment = (TextView) itemView.findViewById(R.id.comment);
            time = (RelativeTimeTextView) view.findViewById(R.id.time);
            progress = (ProgressBar) view.findViewById(R.id.fullpostloading);

            //bind with listeners
            this.buy.setOnClickListener(this);

            this.swap.setOnClickListener(this);
            this.sendto.setOnClickListener(this);
            this.msgtouser.setOnClickListener(this);
            this.favorates.setOnClickListener(this);
            this.comment.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            show(v, getAdapterPosition());
        }

    }

    class LndProductShopHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public HomeImageSliderLayout hfl;
        public EmojiconTextView description;
        public TextView price_was, price_now;
        public ImageButton msgtouser, favorates, sendto;
        public TextView buy;
        public TextView likescount;
        public TextView condition;
        public TextView comment;
        public RelativeTimeTextView time;
        Context con;
        ImageButton forward, backward;
        public TextView size, color;
        public View spaceview;
        public ProgressBar progress;

        LndProductShopHolder(View view, Context context) {
            super(view);
            con = context;
            //setting height
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

//setting margins around imageimageview
            params.height = (height * 70) / 100; //left, top, right, bottom
            params.width = width;
            description = (EmojiconTextView) view.findViewById(R.id.desc);
            price_was = (TextView) view.findViewById(R.id.pricewas);
            price_now = (TextView) view.findViewById(R.id.pricenow);
            msgtouser = (ImageButton) view.findViewById(R.id.messagetouser);
            favorates = (ImageButton) view.findViewById(R.id.favorate);
            sendto = (ImageButton) view.findViewById(R.id.sendto);
            buy = (TextView) itemView.findViewById(R.id.buy);
            comment = (TextView) itemView.findViewById(R.id.comment);
            time = (RelativeTimeTextView) view.findViewById(R.id.time);

            likescount = (TextView) view.findViewById(R.id.likescount);
            condition = (TextView) view.findViewById(R.id.condition);
            // pattern = (TextView) view.findViewById(R.id.pattern);
            hfl = (HomeImageSliderLayout) view.findViewById(R.id.switcher);
            hfl.setLayoutParams(params);
            forward = (ImageButton) view.findViewById(R.id.forward);
            backward = (ImageButton) view.findViewById(R.id.backward);
            spaceview = view.findViewById(R.id.space);
            progress = (ProgressBar) view.findViewById(R.id.fullpostloading);

            size = (TextView) view.findViewById(R.id.size);
            color = (TextView) view.findViewById(R.id.color);

            description = (EmojiconTextView) view.findViewById(R.id.desc);
            //bind with listeners
            // this.buy.setOnClickListener(this);
            this.buy.setOnClickListener(new BuyItem(size, color));
            this.sendto.setOnClickListener(this);
            this.msgtouser.setOnClickListener(this);
            this.favorates.setOnClickListener(this);
            this.comment.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {
            show(view, getAdapterPosition());
        }
    }

    class LndProductPrivateUserHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private TextView color;
        public HomeImageSliderLayout hfl;
        public EmojiconTextView description;
        public TextView len;
        public TextView price_was, price_now;
        public ImageButton msgtouser, favorates, sendto;
        private TextView edit, delete;
        Context con;
        ImageButton forward, backward;
        public TextView likescount;
        public View spaceview;
        public TextView condition;
        public LinearLayout ll;
        public RelativeTimeTextView time;
        public ProgressBar progress;
        public TextView comment;

        LndProductPrivateUserHolder(View view, Context context) {
            super(view);
            con = context;
            //setting height
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

//setting margins around imageimageview
            params.height = (height * 70) / 100; //left, top, right, bottom
            params.width = width;
            description = (EmojiconTextView) view.findViewById(R.id.desc);
            price_was = (TextView) view.findViewById(R.id.pricewas);
            price_now = (TextView) view.findViewById(R.id.pricenow);
            len = (TextView) view.findViewById(R.id.len);
            msgtouser = (ImageButton) view.findViewById(R.id.messagetouser);
            favorates = (ImageButton) view.findViewById(R.id.favorate);
            sendto = (ImageButton) view.findViewById(R.id.sendto);
            edit = (TextView) itemView.findViewById(R.id.edit);
            delete = (TextView) itemView.findViewById(R.id.delete);
            likescount = (TextView) view.findViewById(R.id.likescount);
            condition = (TextView) view.findViewById(R.id.condition);
            color = (TextView) view.findViewById(R.id.color);
            time = (RelativeTimeTextView) view.findViewById(R.id.time);
            progress = (ProgressBar) view.findViewById(R.id.fullpostloading);
            hfl = (HomeImageSliderLayout) view.findViewById(R.id.switcher);
            hfl.setLayoutParams(params);
            forward = (ImageButton) view.findViewById(R.id.forward);
            backward = (ImageButton) view.findViewById(R.id.backward);
            spaceview = view.findViewById(R.id.space);
            comment = (TextView) itemView.findViewById(R.id.comment);

            //hiding views for user
            msgtouser.setVisibility(View.GONE);
            favorates.setVisibility(View.GONE);

            //bind with listeners
            this.edit.setOnClickListener(this);
            this.delete.setOnClickListener(this);
            this.sendto.setOnClickListener(this);
            this.msgtouser.setOnClickListener(this);
            this.favorates.setOnClickListener(this);
            this.comment.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            show(v, getAdapterPosition());
        }

    }

    class LndProductShopUserHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private TextView sizetext, colortext;
        public HomeImageSliderLayout hfl;
        public EmojiconTextView description;
        public TextView price_was, price_now;
        public ImageButton msgtouser, favorates, sendto;
        public TextView edit, delete;
        public TextView likescount;
        public RelativeTimeTextView time;
        public ProgressBar progress;
        public TextView comment;

        Context con;
        ImageButton forward, backward;
        public TextView size, color;
        public View spaceview;
        public TextView condition;

        LndProductShopUserHolder(View view, Context context) {
            super(view);
            con = context;
            //setting height
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

//setting margins around imageimageview
            params.height = (height * 70) / 100; //left, top, right, bottom
            params.width = width;
            description = (EmojiconTextView) view.findViewById(R.id.desc);
            price_was = (TextView) view.findViewById(R.id.pricewas);
            price_now = (TextView) view.findViewById(R.id.pricenow);
            msgtouser = (ImageButton) view.findViewById(R.id.messagetouser);
            favorates = (ImageButton) view.findViewById(R.id.favorate);
            sendto = (ImageButton) view.findViewById(R.id.sendto);
            edit = (TextView) itemView.findViewById(R.id.edit);
            delete = (TextView) itemView.findViewById(R.id.delete);
            //size text and color text
            time = (RelativeTimeTextView) view.findViewById(R.id.time);
            likescount = (TextView) view.findViewById(R.id.likescount);
            // pattern = (TextView) view.findViewById(R.id.pattern);
            hfl = (HomeImageSliderLayout) view.findViewById(R.id.switcher);
            hfl.setLayoutParams(params);
            forward = (ImageButton) view.findViewById(R.id.forward);
            backward = (ImageButton) view.findViewById(R.id.backward);
            spaceview = view.findViewById(R.id.space);
            size = (TextView) view.findViewById(R.id.size);
            color = (TextView) view.findViewById(R.id.color);
            condition = (TextView) view.findViewById(R.id.condition);
            progress = (ProgressBar) view.findViewById(R.id.fullpostloading);
            comment = (TextView) itemView.findViewById(R.id.comment);

            //hiding views for user
            msgtouser.setVisibility(View.GONE);
            favorates.setVisibility(View.GONE);

            //bind with listeners
            this.edit.setOnClickListener(this);
            this.delete.setOnClickListener(this);
            this.sendto.setOnClickListener(this);
            this.msgtouser.setOnClickListener(this);
            this.favorates.setOnClickListener(this);
            this.comment.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {
            show(view, getAdapterPosition());
        }
    }

    class BuyItem implements OnClickListener {
        TextView size, color;

        public BuyItem(TextView size, TextView color) {
            this.size = size;
            this.color = color;
        }

        @Override
        public void onClick(View v) {
            if (size.getText().toString().trim().compareToIgnoreCase("size") == 0) {
                Main_TabHost.main.showPopup("select size", View.GONE);
                return;

            } else if (color.getText().toString().trim().compareToIgnoreCase("color") == 0) {
                Main_TabHost.main.showPopup("select color", View.GONE);
                return;
            }
            Intent buy = new Intent(mContext, RegularCheckoutActivity.class);
            mContext.startActivity(buy);
        }
    }

    AlertDialog alert = null;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean loading = true;

    private void show(View v, final int pos) {
        View view = null;
        Home_List_Data hld = null;
        AlertDialog.Builder dialog = null;

        switch (v.getId()) {


            case R.id.buy:
                Intent buy = new Intent(mContext, RegularCheckoutActivity.class);

                mContext.startActivity(buy);
                break;
            case R.id.swap:
                dialog = new AlertDialog.Builder(mContext);
                view = LayoutInflater.from(mContext).inflate(R.layout.swap_dialog_layout, null);

                dialog.setView(view);
                alert = dialog.create();
                alert.show();
                view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });
                view.findViewById(R.id.swapcontinue).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        Intent sendswaprequest = new Intent(mContext, SendSwapRequestActivity.class);
                        sendswaprequest.putExtra("data", mItems.get(pos));
                        Main_TabHost.activity.startActivityForResult(sendswaprequest, 4);
                        // Log.e("uname", mItems.get(pos).getUname());
                    }
                });
                break;
            case R.id.comment:
                dialog = new AlertDialog.Builder(mContext);
                view = LayoutInflater.from(mContext).inflate(R.layout.agent_comment_popup, null);
                dialog.setView(view);
                alert = dialog.create();
                alert.show();
                //cancel dialog
                view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });
                //agent read more

                view.findViewById(R.id.learnmore).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        Intent i = new Intent(mContext, LndComments.class);
                        i.putExtra("pos", 2);
                        mContext.startActivity(i);
                    }
                });
                break;

            case R.id.sendto:

                users.clear();

                final Dialog sendtodialog = new Dialog(mContext, R.style.DialogSlideAnim3);
                sendtodialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                sendtodialog.setTitle("Comments");

                // dialog.getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c6290")));
                sendtodialog.setContentView(R.layout.sendtouser_dialog_layout);
                prog = (ProgressBar) sendtodialog.findViewById(R.id.progressBar);
                usermessage = (EditText) sendtodialog.findViewById(R.id.messagetext);
                EditText sendto = (EditText) sendtodialog.findViewById(R.id.sendto);

                sendcancel = (TextView) sendtodialog.findViewById(R.id.cancel);
                showtext = (TextView) sendtodialog.findViewById(R.id.showtext);
                showtext.setVisibility(View.GONE);

                RecyclerView recyclerView = (RecyclerView) sendtodialog.findViewById(R.id.recyclerView);
                final LinearLayoutManager layoutManager
                        = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

                // mLayoutManager.
                recyclerView.setLayoutManager(layoutManager);
                mAdapter = new SentToAdapter(mContext, users);
                recyclerView.setAdapter(mAdapter);
                String userid = SingleTon.pref.getString("user_id", "");
                //to get all followers
                getFollowers(userid);
                sendtodialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                sendtodialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                sendtodialog.show();
                //cancel event
                sendtodialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!(sendcancel.getText().toString().compareToIgnoreCase("send") == 0)) {
                            sendtodialog.dismiss();
                            return;
                        }
                        if (SentToAdapter.usersselected.size() == 0) {
                            return;
                        }
                        try {
                            JSONObject jobj = new JSONObject();
                            JSONArray dressArray = new JSONArray(SentToAdapter.usersselected.keySet());
                            jobj.put("userids", dressArray);
                            jobj.put("message", usermessage.getText() + "");
                            jobj.put("postid", mItems.get(pos).getPost_id());
                            jobj.put("senderid", SingleTon.pref.getString("user_id", ""));
                            jobj.put("datetime", SingleTon.getCurrentTimeStamp());

                            //Log.e("userid", jobj.toString());

                            sharepost(jobj.toString());
                        } catch (Exception ex) {

                        }
                        sendtodialog.dismiss();

                    }
                });
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (dx > 0) //check for scroll down
                        {
                            visibleItemCount = layoutManager.getChildCount();
                            totalItemCount = layoutManager.getItemCount();
                            pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                            if (loading) {
                                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                    loading = false;
                                    //  Toast.makeText(mContext,"called",Toast.LENGTH_SHORT).show();
//                                    getData();
                                }
                            }
                        }
                    }
                });
                addTextListener(sendto, recyclerView);
                break;
            case R.id.messagetouser:
                hld = mItems.get(pos);
                Intent msgtofrnd = new Intent(mContext, SendMessageActivity.class);
                msgtofrnd.putExtra("fromhome", true);
                msgtofrnd.putExtra("uname", hld.getUname());
                msgtofrnd.putExtra("user_id", hld.getUserid());

                //chat banner data

                Chat_Banner_Data cbd = new Chat_Banner_Data();
                cbd.setImage_url(hld.getImageurls().get(0));
                cbd.setBrand(hld.getBrandname());
                cbd.setSize(hld.getSize());
                cbd.setPricenow(hld.getPricenow());
                cbd.setSellername(hld.getUname());
                msgtofrnd.putExtra("bannerdata", cbd);

                mContext.startActivity(msgtofrnd);

                break;
            case R.id.favorate:
                FavoriteData fav = SingleTon.db.getContact(mItems.get(pos).getPost_id());
                if (fav == null) {
                    FavoriteData favdata = new FavoriteData();
                    Home_List_Data lndhome = mItems.get(pos);
                    favdata.setPostid(lndhome.getPost_id());
                    favdata.setCost(lndhome.getPricenow());
                    favdata.setImageurl(lndhome.getImageurls().get(0));
                    SingleTon.db.addContact(favdata);
                    ((ImageButton) v).setImageResource(R.drawable.filled_favorate_icon);
                } else {
                    Log.e("postid", fav.getPostid() + "");
                    SingleTon.db.deleteContact(fav.getPostid());
                    ((ImageButton) v).setImageResource(R.drawable.favorite_icon);

                }
                break;


            case R.id.edit:
                hld = mItems.get(pos);
                if (hld.getCategory() == 1) {
                    Intent i = new Intent(mContext, DressEditPost.class);
                    i.putExtra("data", mItems.get(pos));
                    mContext.startActivity(i);
                } else if (hld.getCategory() == 2) {
                    Intent i = new Intent(mContext, HandBagsEditPost.class);
                    i.putExtra("data", mItems.get(pos));
                    mContext.startActivity(i);

                } else if (hld.getCategory() == 3) {
                    Intent i = new Intent(mContext, ShoesEditPost.class);
                    i.putExtra("data", mItems.get(pos));
                    mContext.startActivity(i);

                } else if (hld.getCategory() == 4) {
                    Intent i = new Intent(mContext, JewelleryEditPost.class);
                    i.putExtra("data", mItems.get(pos));
                    mContext.startActivity(i);

                }
                break;

            case R.id.delete:
                dialog = new AlertDialog.Builder(mContext);
                view = LayoutInflater.from(mContext).inflate(R.layout.deletepost_dialog_layout, null);

                dialog.setView(view);
                final AlertDialog alert = dialog.create();
                alert.show();
                view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });
                view.findViewById(R.id.postdelete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        //   Log.e("uname", items.get(getAdapterPosition()).getUname());
                        Home_List_Data lhd = mItems.get(pos);
                        List<String> imageurls = lhd.getImageurls();
                        JSONObject delobj = new JSONObject();
                        try {
                            for (int i = 0; i < imageurls.size(); i++) {
                                delobj.put("image" + (i + 1), fileName(imageurls.get(i)));

                            }
                            delobj.put("postid", lhd.getPost_id());
                            deletePost(delobj.toString(), pos);

                        } catch (Exception ex) {
                            Log.e("delete error", ex.getMessage() + "");
                        }

                    }
                });
                break;

        }
    }

    //to get file name from url of image
    private String fileName(String filename) {
        int index = filename.lastIndexOf("/");
        if (index > 0)
            return filename.substring(index + 1);
        else
            return "";
    }

    public void getFollowers(final String userid) {
        prog.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/followfollowing.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                prog.setVisibility(View.GONE);

                //   Log.e("follower", response.toString());
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jsonArray = jobj.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        FollowersFollowingData fd = new FollowersFollowingData();
                        fd.setUname(jsonObject.getString("uname"));
                        fd.setUserpic(jsonObject.getString("user_pic"));
                        fd.setUserid(jsonObject.getString("followerid"));
                        fd.setSelected(false);
                        users.add(fd);
                    }
                    if (jsonArray.length() == 0)
                        showtext.setVisibility(View.VISIBLE);
                } catch (Exception ex) {

                    Log.e("json parsing error", ex.getMessage());
                }
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Log.e("response",error.getMessage()+"");
                prog.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "9");
                params.put("user_id", userid);
                params.put("skipdata", 0 + "");


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

    public void sharepost(final String data) {
        prog.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/lndnotification.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                prog.setVisibility(View.GONE);

                //  Log.e("follower", response.toString());
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status")) {
                        SentToAdapter.usersselected.clear();
                        Main_TabHost.main.showPopup("Shared successfully", View.VISIBLE);
                    } else {
                        Toast.makeText(mContext, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ex) {

                    Log.e("json parsing error", ex.getMessage());
                }
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Log.e("response",error.getMessage()+"");
                prog.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "1");
                params.put("data", data);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

    public void deletePost(final String data, final int pos) {

        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("wait deleting post");
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/postdata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pDialog.dismiss();
                // Log.e("response", response.toString());
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status")) {

                        Toast.makeText(mContext, jobj.getString("message"), Toast.LENGTH_LONG).show();
                        LndUserFullStickyActivity lnd = (LndUserFullStickyActivity) mContext;
                        delete(pos);
                    } else {
                        Toast.makeText(mContext, jobj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                //  Log.e("response",error.getMessage()+"");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("data", data);
                params.put("rqid", "6");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);


    }

    private void delete(int pos) {
        int sectionManager = -1;
        int headerCount = 0;
        int sectionFirstPosition = 0;
        int count = 0;

        deleteditemssposition.add((pos / 2) + "");

        mItems.remove(pos);
        mItems.remove(pos - 1);

        for (int i = 0; i < mItems.size(); i++) {
            if (i % 2 == 0) {
                sectionManager = (sectionManager + 1) % 1;
                sectionFirstPosition = count + headerCount;
                headerCount += 1;
                count++;
            }
            Home_List_Data hld = mItems.get(i);

            hld.sectionManager = sectionManager;
            hld.sectionFirstPosition = sectionFirstPosition;


        }
        notifyHeaderChanges();

        notifyDataSetChanged();

    }

    public void addTextListener(EditText searchbox, final RecyclerView recyclerView) {

        searchbox.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final List<FollowersFollowingData> filteredList = new ArrayList<>();

                for (int i = 0; i < users.size(); i++) {

                    final String text = users.get(i).getUname().toLowerCase();
                    if (text.contains(query)) {

                        filteredList.add(users.get(i));
                    }
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                mAdapter = new SentToAdapter(mContext, filteredList);

                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }
}


