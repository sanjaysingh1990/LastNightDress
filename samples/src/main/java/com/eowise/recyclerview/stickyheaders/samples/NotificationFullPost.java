package com.eowise.recyclerview.stickyheaders.samples;

import android.app.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import android.widget.RelativeLayout;
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
import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.TagSelectingTextview;
import com.eowise.recyclerview.stickyheaders.samples.LndUserProfile.LndProfile;
import com.eowise.recyclerview.stickyheaders.samples.NewMessage.SendMessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.PostDataShop.DressEditPost;
import com.eowise.recyclerview.stickyheaders.samples.PostDataShop.HandBagsEditPost;
import com.eowise.recyclerview.stickyheaders.samples.PostDataShop.JewelleryEditPost;
import com.eowise.recyclerview.stickyheaders.samples.PostDataShop.ShoesEditPost;
import com.eowise.recyclerview.stickyheaders.samples.PurchaseAndSwap.ShippingAddressActivity;
import com.eowise.recyclerview.stickyheaders.samples.SQLDB.FavoriteData;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.CommentBean;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.HomeImageSliderLayout;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.Home_List_Data;
import com.eowise.recyclerview.stickyheaders.samples.UserProfile.OtherUserProfileActivity;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ConstantValues;
import com.eowise.recyclerview.stickyheaders.samples.Utils.RelativeTimeTextView;
import com.eowise.recyclerview.stickyheaders.samples.adapters.SendToAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.Chat_Banner_Data;
import com.eowise.recyclerview.stickyheaders.samples.data.FollowersFollowingData;
import com.eowise.recyclerview.stickyheaders.samples.interfaces.TagClick;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import github.ankushsachdeva.emojicon.EmojiconTextView;

public class NotificationFullPost extends AppCompatActivity implements View.OnClickListener, TagClick {

    @Bind(R.id.switcher)
    HomeImageSliderLayout hfl;
    @Bind(R.id.desc)
    EmojiconTextView description;
    @Bind(R.id.pricewas)
    TextView price_was;
    @Bind(R.id.pricenow)
    TextView price_now;
    @Bind(R.id.messagetouser)
    ImageButton msgtouser;
    @Bind(R.id.favorate)
    ImageButton favorates;
    @Bind(R.id.sendto)
    ImageButton sendto;
    @Bind(R.id.buy)
    TextView buy;
    @Bind(R.id.likescount)
    TextView likescount;
    @Bind(R.id.condition)
    TextView condition;
    @Bind(R.id.comment)
    TextView comment;
    @Bind(R.id.forward)
    ImageButton forward;
    @Bind(R.id.backward)
    ImageButton backward;
    @Bind(R.id.size)
    TextView size;
    @Bind(R.id.color)
    TextView color;
    @Bind(R.id.uname)
    TextView uname;
    @Bind(R.id.brandname)
    TextView brandname;
    @Bind(R.id.likesclick)
    ImageButton likebutton;
    @Bind(R.id.profilepic)
    ImageView profilepic;
    @Bind(R.id.time)
    RelativeTimeTextView time;
    @Bind(R.id.shoppoastcontrols)
    RelativeLayout shoppostcontrols;

    @Bind(R.id.privatepoastcontrols)
    LinearLayout privatepostcontrols;

    @Bind(R.id.ownpoastcontrols)
    LinearLayout ownpostcontrols;


    @Bind(R.id.edit)
    TextView edit;
    @Bind(R.id.delete)
    TextView delete;
    @Bind(R.id.commenttextview)
    TextView commenttext;
    @Bind(R.id.viewallcomments)
    TextView viewallcomments;

    @Bind(R.id.islocked)
    ImageView islocked;

    Home_List_Data hld;

    AlertDialog alert = null;
    String data = "";
    List<FollowersFollowingData> users = new ArrayList<>();
    public static EditText usermessage;
    public static TextView sendcancel;
    private SendToAdapter mAdapter;
    private ProgressBar prog;
    private TextView showtext;
    String post_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_full_post);
        ButterKnife.bind(this);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            post_id = extra.getString("post_id", "");
            getData(post_id);
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.height = (height * 70) / 100;
        params.width = width;


        hfl.setLayoutParams(params);


        //applying font
        uname.setTypeface(SingleTon.robotomedium);
        brandname.setTypeface(SingleTon.robotoregular);


    }

    @Override
    public void onClick(View v) {
        View view = null;
        android.app.AlertDialog.Builder dialog = null;

        switch (v.getId()) {

            case R.id.likesclick:
                postnotiLiked();
                break;
            case R.id.buy:
                Intent buy = new Intent(this, ShippingAddressActivity.class);

                startActivity(buy);
                break;
            case R.id.swap:
                dialog = new android.app.AlertDialog.Builder(this);
                view = LayoutInflater.from(this).inflate(R.layout.swap_dialog_layout, null);

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
                        //  Intent sendswaprequest = new Intent(this, SendSwapRequestActivity.class);
                        // sendswaprequest.putExtra("data", mItems.get(pos));
                        // Main_TabHost.activity.startActivityForResult(sendswaprequest, 4);
                        // Log.e("uname", mItems.get(pos).getUname());
                    }
                });
                break;
            case R.id.comment:
                Intent comment = new Intent(this, LndComments.class);
                comment.putExtra("post_id", post_id);
                comment.putExtra("from", 100);
                startActivity(comment);
                break;
            case R.id.viewallcomments:
                comment = new Intent(this, LndComments.class);
                comment.putExtra("post_id", post_id);
                comment.putExtra("from", 100);
                startActivity(comment);
                break;
            case R.id.sendto:

                users.clear();

                final Dialog sendtodialog = new Dialog(this, R.style.DialogSlideAnim3);
                sendtodialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                sendtodialog.setTitle("Comments");

                // dialog.getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c6290")));
                sendtodialog.setContentView(R.layout.sendtouser_dialog_layout);
                prog = (ProgressBar) sendtodialog.findViewById(R.id.progressBar);
                usermessage = (EditText) sendtodialog.findViewById(R.id.messagetext);
                sendcancel = (TextView) sendtodialog.findViewById(R.id.cancel);
                showtext = (TextView) sendtodialog.findViewById(R.id.showtext);
                showtext.setVisibility(View.GONE);

                RecyclerView recyclerView = (RecyclerView) sendtodialog.findViewById(R.id.recyclerView);
                LinearLayoutManager layoutManager
                        = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

                // mLayoutManager.
                recyclerView.setLayoutManager(layoutManager);
                mAdapter = new SendToAdapter(this, users);
                recyclerView.setAdapter(mAdapter);
                //to get all followers
                getFollowers(SingleTon.pref.getString("user_id", ""));
                sendtodialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                sendtodialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                sendtodialog.show();
                //cancel event
                sendtodialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (sendcancel.getText().toString().compareToIgnoreCase("send") == 0 && usermessage.getText().length() == 0)
                            return;
                        if (SendToAdapter.usersselected.size() == 0) {
                            sendtodialog.dismiss();
                            return;
                        }
                        try {
                            JSONObject jobj = new JSONObject();
                            JSONArray dressArray = new JSONArray(SendToAdapter.usersselected.keySet());
                            jobj.put("userids", dressArray);
                            jobj.put("message", usermessage.getText() + "");
                            jobj.put("postid", hld.getPost_id());
                            jobj.put("senderid", SingleTon.pref.getString("user_id", ""));
                            jobj.put("datetime", SingleTon.getCurrentTimeStamp());

                            //Log.e("userid", jobj.toString());

                            //sharepost(jobj.toString());
                        } catch (Exception ex) {

                        }
                        sendtodialog.dismiss();

                    }
                });

                break;
            case R.id.messagetouser:

                Intent msgtofrnd = new Intent(this, SendMessageActivity.class);
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
                startActivity(msgtofrnd);

                break;
            case R.id.favorate:
                FavoriteData fav = SingleTon.db.getContact(hld.getPost_id());
                if (fav == null) {
                    FavoriteData favdata = new FavoriteData();
                    favdata.setPostid(hld.getPost_id());
                    favdata.setCost(hld.getPricenow());
                    favdata.setImageurl(hld.getImageurls().get(0));
                    SingleTon.db.addContact(favdata);
                    ((ImageButton) v).setImageResource(R.drawable.filled_favorate_icon);
                } else {
                    //Log.e("postid", fav.getPostid() + "");
                    SingleTon.db.deleteContact(fav.getPostid());
                    ((ImageButton) v).setImageResource(R.drawable.favorite_icon);

                }
                break;


            case R.id.edit:
                if (hld.getCategory() == 1) {
                    Intent i = new Intent(this, DressEditPost.class);
                    // i.putExtra("data", mItems.get(pos));
                    startActivity(i);
                } else if (hld.getCategory() == 2) {
                    Intent i = new Intent(this, HandBagsEditPost.class);
                    // i.putExtra("data", mItems.get(pos));
                    startActivity(i);

                } else if (hld.getCategory() == 3) {
                    Intent i = new Intent(this, ShoesEditPost.class);
                    //i.putExtra("data", mItems.get(pos));
                    startActivity(i);

                } else if (hld.getCategory() == 4) {
                    Intent i = new Intent(this, JewelleryEditPost.class);
                    //   i.putExtra("data", mItems.get(pos));
                    startActivity(i);

                }
                break;

            case R.id.delete:
                dialog = new android.app.AlertDialog.Builder(this);
                view = LayoutInflater.from(this).inflate(R.layout.deletepost_dialog_layout, null);

                dialog.setView(view);
                final android.app.AlertDialog alert = dialog.create();
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

                        // List<String> imageurls = lhd.getImageurls();
                        JSONObject delobj = new JSONObject();
                        try {
                           /* delobj.put("image1", fileName(imageurls.get(0)));
                            delobj.put("image2", fileName(imageurls.get(1)));
                            delobj.put("image3", fileName(imageurls.get(2)));
                            delobj.put("image4", fileName(imageurls.get(3)));
                            delobj.put("postid", lhd.getPost_id());*/

                            //deletePost(delobj.toString(), pos);

                        } catch (Exception ex) {
                            Log.e("delete error", ex.getMessage() + "");
                        }

                    }
                });
                break;

        }
    }
//for global reference

    public void getData(final String postid) {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_POST_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    // Log.e("json", response + "");
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jsonArray = jobj.getJSONArray("data");
                    if (jsonArray.length() == 0) {

                        hfl.setVisibility(View.INVISIBLE);
                        Toast.makeText(NotificationFullPost.this, "Post not available", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        //bind with listeners
                        buy.setOnClickListener(NotificationFullPost.this);
                        hfl.setVisibility(View.VISIBLE);
                        sendto.setOnClickListener(NotificationFullPost.this);
                        msgtouser.setOnClickListener(NotificationFullPost.this);
                        favorates.setOnClickListener(NotificationFullPost.this);
                        comment.setOnClickListener(NotificationFullPost.this);
                        likebutton.setOnClickListener(NotificationFullPost.this);
                        edit.setOnClickListener(NotificationFullPost.this);
                        delete.setOnClickListener(NotificationFullPost.this);
                        viewallcomments.setOnClickListener(NotificationFullPost.this);


                    }
                    JSONObject data = jsonArray.getJSONObject(0);
                    hld = new Home_List_Data();
                    //read data and show to views
                    hld.setPost_id(data.getString("post_id"));
                    hld.setUname(data.getString("uname"));
                    hld.setUserid(data.getString("user_id"));
                    hld.setBrandname(data.getString("brand_name"));
                    hld.setLikedvalue(data.getString("like"));
                    hld.setSize(data.getString("size"));
                    hld.setPricenow(data.getString("price_now"));
                    hld.setLikestotal(Integer.parseInt(data.getString("likes")));
                    hld.setCategory(data.getInt("category_type"));
                    //post id

                    price_was.setText("$" + data.getString("price_was"));
                    price_now.setText("$" + data.getString("price_now"));


                    likescount.setText(data.getInt("post_total_likes") + " likes");
                    viewallcomments.setText("View all " + data.getInt("post_total_comments") + " comments");

                    //for condition
                    String con = data.getString("condition");
                    if (con != null && con.length() > 0) {
                        int pos = Integer.parseInt(con);
                        condition.setText(ConstantValues.condition[pos]);
                    }

                    final String lndsize = data.getString("size");
                    if (lndsize != null) {
                        if (lndsize.split(",").length == 1) {
                            size.setText(Capitalize.capitalize(lndsize));
                            size.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            size.setClickable(false);
                        } else {
                            size.setClickable(true);
                            size.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                            size.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    popupWindow(lndsize).showAsDropDown(view, -5, 0);


                                }
                            });
                        }

                    }

                    //for single or multiple colors
                    final String lndcolors = data.getString("color");
                    final int category = data.getInt("category_type");
                    try {
                        if (lndcolors.split(",").length == 1) {
                            color.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                            if (category == 4) {
                                int pos = Integer.parseInt(lndcolors);
                                color.setText(Capitalize.capitalizeFirstLetter(ConstantValues.metaltype[pos]));
                            } else
                                color.setText(Capitalize.capitalizeFirstLetter(lndcolors));

                            color.setClickable(false);
                        } else {
                            color.setClickable(true);
                            color.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                            color.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    popupWindow2(lndcolors, category).showAsDropDown(view, -5, 0);


                                }
                            });
                        }
                    } catch (Exception ex) {

                    }

                    // for description

                    String desc = data.getString("description");
                    TagSelectingTextview mTagSelectingTextview = new TagSelectingTextview();
                    int hashTagHyperLinkDisabled = 0;
                    String username = Capitalize.capitalizeFirstLetter(data.getString("uname"));


                    //checking description for hashtag and usermention
                    description.setMovementMethod(LinkMovementMethod.getInstance());
                    description.setText(mTagSelectingTextview.addClickablePart(username + " " + desc,
                            NotificationFullPost.this, hashTagHyperLinkDisabled, ConstantValues.hastTagColorBlue, username.length()),
                            TextView.BufferType.SPANNABLE);


                    //for images
                    ArrayList<String> imgurls = new ArrayList<String>();

                    String imgurl = data.getString("imageurl1");

                    imgurls.add(imgurl);

                    imgurl = data.getString("imageurl2");
                    if (imgurl.length() > 0)
                        imgurls.add(imgurl);

                    imgurl = data.getString("imageurl3");
                    if (imgurl.length() > 0)
                        imgurls.add(imgurl);
                    imgurl = data.getString("imageurl4");
                    if (imgurl.length() > 0)
                        imgurls.add(imgurl);
                    hld.setImageurls(imgurls);
                    hfl.setFeatureItems(forward, backward, imgurls, NotificationFullPost.this);
                    //setting profile pic

                    String url = data.getString("profile_pic");
                    if (url.length() > 0) {
                        SingleTon.imageLoader.displayImage(url, profilepic, SingleTon.options3);

                    }

                    //username and brandname
                    String brandname = data.getString("brand_name");
                    NotificationFullPost.this.uname.setText(Capitalize.capitalizeFirstLetter(data.getString("uname")) + "");
                    NotificationFullPost.this.brandname.setText(brandname + "");

                    //checking already liked or not

                    if (hld.getLikedvalue().compareTo("1") == 0)
                        likebutton.setImageResource(R.drawable.liked_icon);
                    else
                        likebutton.setImageResource(R.drawable.like_icon);


                    //checking user type

                    if (data.getString("utype").compareTo("private") == 0) {
                        if (SingleTon.pref.getString("user_id", "").compareToIgnoreCase(data.getString("user_id")) == 0) {
                            ownpostcontrols.setVisibility(View.VISIBLE);
                            favorates.setVisibility(View.GONE);
                            msgtouser.setVisibility(View.GONE);
                        } else {
                            privatepostcontrols.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (SingleTon.pref.getString("user_id", "").compareToIgnoreCase(data.getString("user_id")) == 0) {
                            ownpostcontrols.setVisibility(View.VISIBLE);
                            favorates.setVisibility(View.GONE);
                            msgtouser.setVisibility(View.GONE);


                        } else {
                            shoppostcontrols.setVisibility(View.VISIBLE);
                        }
                    }

                    //for time
                    time.setReferenceTime(getMilliseconds(data.getString("date_time")));

                    //total likers
                    likescount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent likers = new Intent(NotificationFullPost.this, LikersActivity.class);
                            likers.putExtra("postid", hld.getPost_id());
                            likers.putExtra("type", 1);
                            startActivity(likers);
                        }
                    });

                    //to read comments
                    JSONArray comments = data.getJSONArray("postcoments");


                    if (comments.length() > 0) {

                        for (int i = comments.length() - 1; i >= 0; i--) {
                            JSONObject jsonObject = comments.getJSONObject(i);
                            String uname = jsonObject.getString("uname");
                            String comment = jsonObject.getString("comment");

                            SpannableString word = new SpannableString(Capitalize.capitalizeFirstLetter(uname + " " + comment));

                            word.setSpan(new ForegroundColorSpan(Color.parseColor("#be4d66")), 0, uname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            word.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, uname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            commenttext.append(word);
                            if (i > 0)
                                commenttext.append("\n");
                        }


                    } else {
                        viewallcomments.setVisibility(View.GONE);
                        commenttext.setVisibility(View.GONE);
                    }
                    if (data.getInt("post_total_comments") <= 5)
                        viewallcomments.setVisibility(View.GONE);


                    if (SingleTon.pref.getInt("user_position", -1) > 1)
                        islocked.setVisibility(View.GONE);
                    else
                        islocked.setVisibility(View.VISIBLE);


                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "17");
                params.put("user_id", SingleTon.pref.getString("user_id", ""));
                params.put("post_id", postid);


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

    private void checkFavorate(Home_List_Data hld) {
        FavoriteData fd = SingleTon.db.getContact(hld.getPost_id());
        if (fd != null) {
            hld.setIsfavorate(true);
        }
    }

    public PopupWindow popupWindow(String sizes) {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(this);

        // the drop down list is a list view
        ListView lndsizes = new ListView(this);

        // set our adapter and pass our pop up window contents
        lndsizes.setAdapter(popupAdapter(sizes.split(",")));

        // set the item click listener
        lndsizes.setOnItemClickListener(new DropdownOnItemClickListener(popupWindow, sizes));

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(100);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        popupWindow.setContentView(lndsizes);

        return popupWindow;
    }

    public PopupWindow popupWindow2(String colors, int category) {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(this);

        // the drop down list is a list view
        ListView lndcolors = new ListView(this);

        // set our adapter and pass our pop up window contents
        lndcolors.setAdapter(popupAdapter(colors.split(",")));

        // set the item click listener
        lndcolors.setOnItemClickListener(new DropdownOnItemClickListener2(popupWindow, colors, category));

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
        popupWindow.setContentView(lndcolors);

        return popupWindow;
    }

    @Override
    public void clickedTag(CharSequence tag) {
        if (tag.toString().startsWith("#")) {
            Intent hashtag = new Intent(this, LndBrandHashTagGridViewActivity.class);
            hashtag.putExtra("hashtag", tag.toString().substring(1));
            hashtag.putExtra("type", 1);

            startActivity(hashtag);
        } else if (tag.toString().startsWith("@")) {
            Intent profile;
            if (SingleTon.pref.getString("uname", "").compareToIgnoreCase(tag.toString().substring(1)) == 0) {
                //  profile = new Intent(activity, LndProfile.class);
                profile = new Intent(this, LndProfile.class);
            } else {
                profile = new Intent(this, OtherUserProfileActivity.class);
                profile.putExtra("uname", tag.toString().substring(1));
                profile.putExtra("user_id", SingleTon.lnduserid.get(tag.toString().substring(1)));

            }
            startActivity(profile);

        } else {

            Intent profile;
            if (SingleTon.pref.getString("uname", "").compareToIgnoreCase(tag.toString()) == 0) {
                profile = new Intent(this, LndProfile.class);
            } else {
                profile = new Intent(this, OtherUserProfileActivity.class);
                profile.putExtra("uname", tag.toString());
                profile.putExtra("user_id", SingleTon.lnduserid.get(tag.toString().toLowerCase()));

            }
            startActivity(profile);

        }
    }

    public class DropdownOnItemClickListener implements AdapterView.OnItemClickListener {

        PopupWindow popupwindow;
        String sizecolorvalues[];

        public DropdownOnItemClickListener(PopupWindow popupWindow, String sizescolors) {
            this.popupwindow = popupWindow;
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

            try {
                size.setText(Capitalize.capitalizeFirstLetter(sizecolorvalues[pos]));

            } catch (Exception ex) {

            }


        }

    }

    public class DropdownOnItemClickListener2 implements AdapterView.OnItemClickListener {

        PopupWindow popupwindow;
        String metalcolorvalues[];
        int category;

        public DropdownOnItemClickListener2(PopupWindow popupWindow, String sizescolors, int category) {
            this.popupwindow = popupWindow;
            this.metalcolorvalues = sizescolors.split(",");
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

            try {
                if (category == 4)
                    color.setText(Capitalize.capitalizeFirstLetter(ConstantValues.metaltype[Integer.parseInt(metalcolorvalues[pos])]));
                else
                    color.setText(Capitalize.capitalizeFirstLetter(metalcolorvalues[pos]));

            } catch (Exception ex) {


            }


        }

    }

    private ArrayAdapter<String> popupAdapter(String popArray[]) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NotificationFullPost.this, android.R.layout.simple_list_item_1, popArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                // setting the ID and text for every items in the list

                String text = "";
                // setting the ID and text for every items in the list
                try {
                    text = ConstantValues.metaltype[Integer.parseInt(getItem(position))];
                } catch (Exception ex) {
                    text = Capitalize.capitalizeFirstLetter(getItem(position));

                }


                // visual settings for the list item
                TextView listItem = new TextView(NotificationFullPost.this);

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

    public void likedUnliked() {
        if (hld.getLikedvalue().compareTo("2") == 0) {
            likebutton.setImageResource(R.drawable.liked_icon);
            ;

            likescount.setText((hld.getLikestotal() + 1) + " likes");
        } else {
            likebutton.setImageResource(R.drawable.like_icon);

            if (hld.getLikestotal() != 0)

            {
                likescount.setText((hld.getLikestotal() - 1) + " likes");
                hld.setLikestotal(hld.getLikestotal() - 1);
            }
        }
    }

    public void postnotiLiked() {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/lndlikeunlike.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Log.e("json", response.toString());
                try {

                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status")) {
                        int val = jobj.getInt("value");
                        if (val == 1) {
                            hld.setLikedvalue("1");
                            likedUnliked();
                        } else {
                            hld.setLikedvalue("2");
                            likedUnliked();
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
                params.put("post_id", hld.getPost_id());
                params.put("date_time", SingleTon.getCurrentTimeStamp());
                params.put("noti_type", 1 + "");


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


    static long getMilliseconds(String datetime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            Date date = formatter.parse(datetime);
            // Log.e("date",date.toString());
            // Log.e("date2",formatter.format(date));

            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void getFollowers(final String userid) {
        prog.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
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

}

