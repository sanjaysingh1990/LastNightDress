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
import android.text.method.LinkMovementMethod;
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
import com.eowise.recyclerview.stickyheaders.samples.PurchaseAndSwap.ShippingAddressActivity;
import com.eowise.recyclerview.stickyheaders.samples.SQLDB.FavoriteData;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.HomeImageSliderLayout;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.Home_List_Data;
import com.eowise.recyclerview.stickyheaders.samples.UserProfile.OtherUserProfileActivity;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ConstantValues;
import com.eowise.recyclerview.stickyheaders.samples.Utils.RelativeTimeTextView;
import com.eowise.recyclerview.stickyheaders.samples.adapters.SentToAdapter;
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

public class NotificationFullPost extends AppCompatActivity implements View.OnClickListener,TagClick {

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
    public String post_id="";
    public String isliked="";
    private String likestotal="";
    AlertDialog alert=null;

    List<FollowersFollowingData> users = new ArrayList<>();
    public static EditText usermessage;
    public static TextView sendcancel;
    private SentToAdapter mAdapter;
    private ProgressBar prog;
    private TextView showtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_full_post);
        ButterKnife.bind(this);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String post_id = extra.getString("post_id", "");
            getData(post_id);
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.height = (height * 70) / 100;
        params.width = width;


        hfl.setLayoutParams(params);


        //bind with listeners
        this.buy.setOnClickListener(this);

        this.sendto.setOnClickListener(this);
        this.msgtouser.setOnClickListener(this);
        this.favorates.setOnClickListener(this);
        this.comment.setOnClickListener(this);
        this.likebutton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
      switch(view.getId())
      {
          case R.id.likesclick:
              postnotiLiked();
              break;
      }
    }

    public void getData(final String postid) {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/postdata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.e("json", response + "");
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jsonArray = jobj.getJSONArray("data");
                    JSONObject data = jsonArray.getJSONObject(0);

                    //read data and show to views
                   /* hld2.setProfilepicurl(jo.getString("profile_pic"));
                    hld2.setPricenow(jo.getString("price_now"));
                    hld2.setPricewas(jo.getString("price_was"));
                    hld2.setSize(jo.getString("size"));
                    hld2.setLikestotal(jo.getInt("likes"));
                    hld2.setImageurls(imgurls);
                    hld2.setPost_id(jo.getString("post_id"));
                    hld2.setDescription(jo.getString("description"));
                    hld2.setUname(jo.getString("uname"));
                    hld2.setLikedvalue(jo.getString("like"));
                    hld2.setColors(jo.getString("color"));
                    hld2.setConditon(jo.getString("condition"));
                    hld2.setCategory(jo.getInt("category_type"));
                    hld2.setUserid(jo.getString("user_id"));
                    hld2.setBrandname(jo.getString("brand_name"));*/
                    //post id
                    post_id=data.getString("post_id");

                    price_was.setText("$" + data.getString("price_was"));
                    price_now.setText("$" + data.getString("price_now"));

                    likestotal=data.getString("likes");
                    likescount.setText(likestotal+" likes");
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
                    String uname = data.getString("uname");
                    String desc = data.getString("description");
                    TagSelectingTextview mTagSelectingTextview = new TagSelectingTextview();
                    int hashTagHyperLinkDisabled = 0;
                    String hastTagColorBlue = "#be4d66";

                    uname = Capitalize.capitalizeFirstLetter(uname);
                    //checking description for hashtag and usermention
                    description.setMovementMethod(LinkMovementMethod.getInstance());
                    description.setText(mTagSelectingTextview.addClickablePart(uname + " " + desc,
                                    NotificationFullPost.this, hashTagHyperLinkDisabled, hastTagColorBlue, uname.length()),
                            TextView.BufferType.SPANNABLE);


                    //for images
                    ArrayList<String> imgurls = new ArrayList<String>();

                    String imgurl=data.getString("imageurl1");

                    imgurls.add(imgurl);

                    imgurl=data.getString("imageurl2");
                    if(imgurl.length()>0)
                        imgurls.add(imgurl);

                    imgurl=data.getString("imageurl3");
                    if(imgurl.length()>0)
                        imgurls.add(imgurl);
                    imgurl=data.getString("imageurl4");
                    if(imgurl.length()>0)
                        imgurls.add(imgurl);

                    hfl.setFeatureItems(forward, backward, imgurls,NotificationFullPost.this);
               //setting profile pic

                    String url=data.getString("profile_pic");
                    if(url.length()>0)
                    {
                        SingleTon.imageLoader.displayImage(url, profilepic, SingleTon.options3);

                    }

                    //username and brandname
                    uname=data.getString("uname");
                    String brandname=data.getString("brand_name");
                    NotificationFullPost.this.uname.setText(uname+"");
                    NotificationFullPost.this.brandname.setText(brandname + "");

                  //checking already liked or not
                    isliked=data.getString("like");
                    if (isliked.compareTo("1") == 0)
                        likebutton.setImageResource(R.drawable.liked_icon);
                    else
                        likebutton.setImageResource(R.drawable.like_icon);



                    //checking user type

                   if(data.getString("utype").compareTo("private") == 0)
                   {
                       if (SingleTon.pref.getString("user_id", "").compareToIgnoreCase(data.getString("user_id")) == 0)
                       {
                           ownpostcontrols.setVisibility(View.VISIBLE);

                       }
                       else
                       {
                           privatepostcontrols.setVisibility(View.VISIBLE);
                          }
                   }
                    else
                   {
                       if (SingleTon.pref.getString("user_id", "").compareToIgnoreCase(data.getString("user_id")) == 0)
                       {
                           ownpostcontrols.setVisibility(View.VISIBLE);

                       }
                       else
                       {
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
                            likers.putExtra("postid",post_id);
                            startActivity(likers);
                        }
                    });

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

    public void likedUnliked()
    {
        if (isliked.compareTo("2") == 0) {
            likebutton.setImageResource(R.drawable.liked_icon);
         int lt=Integer.parseInt(likestotal);

             likescount.setText((lt+1)+" likes");
             likestotal=(lt+1)+"";
        }
            else {
            likebutton.setImageResource(R.drawable.like_icon);
            int lt=Integer.parseInt(likestotal);
            if(lt!=0)

            {
                likescount.setText((lt - 1) + " likes");
                likestotal=(lt-1)+"";
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
                            isliked="1";
                            likedUnliked();
                        } else {
                            isliked="2";
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
                params.put("post_id", post_id);
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

    private void show(View v) {
        View view = null;
        Home_List_Data hld = null;
        android.app.AlertDialog.Builder dialog = null;

        switch (v.getId()) {


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
                dialog = new android.app.AlertDialog.Builder(this);
                view = LayoutInflater.from(this).inflate(R.layout.agent_comment_popup, null);

                dialog.setView(view);
                alert = dialog.create();
                alert.show();
                view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });
                    /*view2.findViewById(R.id.swapcontinue).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert2.dismiss();
                            Intent sendswaprequest = new Intent(mContext, SendSwapRequestActivity.class);
                            sendswaprequest.putExtra("data", mItems.get(getAdapterPosition()));
                            Main_TabHost.activity.startActivityForResult(sendswaprequest, 4);
                            // Log.e("uname", mItems.get(pos).getUname());
                        }
                    });*/
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
                mAdapter = new SentToAdapter(this, users);
                recyclerView.setAdapter(mAdapter);
                String userid = SingleTon.pref.getString("user_id", "");
                //to get all followers
                //getFollowers(userid);
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
                        if (SentToAdapter.usersselected.size() == 0) {
                            sendtodialog.dismiss();
                            return;
                        }
                        try {
                            JSONObject jobj = new JSONObject();
                            JSONArray dressArray = new JSONArray(SentToAdapter.usersselected.keySet());
                            jobj.put("userids", dressArray);
                            jobj.put("message", usermessage.getText() + "");
                            jobj.put("postid", post_id);
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
               /* FavoriteData fav = ImageLoaderImage.db.getContact(mItems.get(pos).getPost_id());
                if (fav == null) {
                    FavoriteData favdata = new FavoriteData();
                    Home_List_Data lndhome = mItems.get(pos);
                    favdata.setPostid(lndhome.getPost_id());
                    favdata.setCost(lndhome.getPricenow());
                    favdata.setImageurl(lndhome.getImageurls().get(0));
                    ImageLoaderImage.db.addContact(favdata);
                    ((ImageButton) v).setImageResource(R.drawable.filled_favorate_icon);
                } else {
                    Log.e("postid", fav.getPostid() + "");
                    ImageLoaderImage.db.deleteContact(fav.getPostid());
                    ((ImageButton) v).setImageResource(R.drawable.favorite_icon);

                }*/
                break;


            case R.id.edit:
                /*hld = mItems.get(pos);
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

                }*/
                break;

            case R.id.delete:
               /* dialog = new android.app.AlertDialog.Builder(this);
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
                        Home_List_Data lhd = mItems.get(pos);
                        List<String> imageurls = lhd.getImageurls();
                        JSONObject delobj = new JSONObject();
                        try {
                            delobj.put("image1", fileName(imageurls.get(0)));
                            delobj.put("image2", fileName(imageurls.get(1)));
                            delobj.put("image3", fileName(imageurls.get(2)));
                            delobj.put("image4", fileName(imageurls.get(3)));
                            delobj.put("postid", lhd.getPost_id());

                            //deletePost(delobj.toString(), pos);

                        } catch (Exception ex) {
                            Log.e("delete error", ex.getMessage() + "");
                        }

                    }
                });*/
                break;

        }
    }
    static long getMilliseconds(String datetime)
    {
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
}

