package com.eowise.recyclerview.stickyheaders.samples.StickyHeader;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.LndCustomCameraPost.CameraPreviewFragment;
import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.LndNotificationMessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SQLDB.FavoriteData;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ConstantValues;
import com.eowise.recyclerview.stickyheaders.samples.contacts.ContactsActivity;
import com.facebook.FacebookSdk;
import com.init.superslim.LayoutManager;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import io.fabric.sdk.android.Fabric;


public class StickyActivity extends AppCompatActivity {

    private static final String TAG_COUNTRIES_FRAGMENT = "tag_countries_fragment";


    private LndHomeAdapter mAdapter;

    private int mHeaderDisplay;

    private boolean mAreMarginsFixed;
    private RecyclerView mRecyclerView;
    private static final String KEY_HEADER_POSITIONING = "key_header_mode";

    private static final String KEY_MARGINS_FIXED = "key_margins_fixed";
    private ArrayList<Home_List_Data> mItems = new ArrayList<>();

    private TextView heading;
    private AVLoadingIndicatorView dialog;
    private TextView instructiontextview;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout indicator;

    private LinearLayout instructionview;
    boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int skipdata = 0;

    boolean isprivate = false;
    String lastHeader = "";
    int sectionManager = -1;
    int headerCount = 0;
    int sectionFirstPosition = 0;
    int i = 0;
    private boolean firsttime = true;
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.

    private Button twitter, facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sticky_header_layout);

        instructiontextview = (TextView) findViewById(R.id.instructiontextview);
        indicator = (LinearLayout) findViewById(R.id.indicator);
        instructionview = (LinearLayout) findViewById(R.id.instructionview);

        //for twitter
        twitter = (Button) findViewById(R.id.twitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Main_TabHost parentActivity;
                parentActivity = (Main_TabHost) StickyActivity.this.getParent();
                parentActivity.twitter();
            }
        });
        //for facebook
        facebook = (Button) findViewById(R.id.facebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Main_TabHost parentActivity;
                parentActivity = (Main_TabHost) StickyActivity.this.getParent();
                parentActivity.inviteFriends();
            }
        });
//
        ;//for header
        if (savedInstanceState != null) {
            mHeaderDisplay = savedInstanceState
                    .getInt(KEY_HEADER_POSITIONING,
                            getResources().getInteger(R.integer.default_header_display));
            mAreMarginsFixed = savedInstanceState
                    .getBoolean(KEY_MARGINS_FIXED,
                            getResources().getBoolean(R.bool.default_margins_fixed));
        } else {
            mHeaderDisplay = getResources().getInteger(R.integer.default_header_display);
            mAreMarginsFixed = getResources().getBoolean(R.bool.default_margins_fixed);
        }
        mHeaderDisplay = LayoutManager.LayoutParams.HEADER_INLINE | (mHeaderDisplay & LayoutManager.LayoutParams.HEADER_OVERLAY) | (
                mHeaderDisplay & LayoutManager.LayoutParams.HEADER_STICKY);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final LayoutManager manager = new LayoutManager(this);
        mRecyclerView.setLayoutManager(manager);


        mAdapter = new LndHomeAdapter(this, mHeaderDisplay, setData());
        //   mAdapter.setMarginsFixed(mAreMarginsFixed);
        mAdapter.setHeaderDisplay(mHeaderDisplay);
        mRecyclerView.setAdapter(mAdapter);


        //header reference and custom font
        heading = (TextView) findViewById(R.id.heading);
        Typeface tf = Typeface.createFromAsset(getAssets(), "Mural_Script.ttf");
        heading.setTypeface(tf);
//loading spiiner
        dialog = (AVLoadingIndicatorView) findViewById(R.id.loader);
        dialog.setVisibility(View.VISIBLE);
//pull
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = manager.getChildCount();
                    totalItemCount = manager.getItemCount();
                    pastVisiblesItems = manager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;

                            getData();
                        }
                    }
                }
            }
        });
        applySpannable();
        getData();
    }

    private void applySpannable() {

        Spannable word = new SpannableString("Tap on the camera ");

        word.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, word.length(), 0);
        word.setSpan(new ForegroundColorSpan(Color.parseColor("#222427")), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        instructiontextview.setText(word);
        Spannable wordTwo = new SpannableString("to post your first item");

        wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#757575")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        instructiontextview.append(wordTwo);
    }

    private ArrayList<Home_List_Data> setData() {
        boolean isprivate = false;
        final String[] countryNames = getResources().getStringArray(R.array.country_names);
        //Insert headers into list of items.
        String lastHeader = "";
        int sectionManager = -1;
        int headerCount = 0;
        int sectionFirstPosition = 0;
        for (int i = 0; i < countryNames.length; i++) {
            String header = countryNames[i];
            sectionManager = (sectionManager + 1) % 1;
            sectionFirstPosition = i + headerCount;
            lastHeader = header;
            headerCount += 1;
            mItems.add(new Home_List_Data(header, "header", sectionManager, sectionFirstPosition));
            if (i % 2 == 0)
                isprivate = true;
            else
                isprivate = false;
            Home_List_Data hld = new Home_List_Data(countryNames[i], isprivate, "content", sectionManager, sectionFirstPosition);
            mItems.add(hld);

        }
        return mItems;
    }

    private void showInstruction() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);  // or however you need to do it for your code
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(0);
        swipeRefreshLayout.setEnabled(false);
        //Load animation
        final Animation slide_up = AnimationUtils.loadAnimation(this,
                R.anim.slide_up);

        instructionview.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        indicator.setVisibility(View.VISIBLE);
                        indicator.startAnimation(slide_up);
                    }
                });
            }
        }).start();
    }

    public void getData() {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/postdata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.setVisibility(View.GONE);
                SingleTon.lnduserid.clear();
                try {


                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");
                    //to show instruction page
                    if (jarray.length() == 0) {
                        if (firsttime) {
                            firsttime = false;
                            showInstruction();
                        }
                        return;

                    }
                    for (int j = 0; j < jarray.length(); j++) {


                        JSONObject jo = jarray.getJSONObject(j);
                        ArrayList<String> imgurls = new ArrayList<String>();
                        String imgurl = jo.getString("imageurl1");

                        imgurls.add(imgurl);

                        imgurl = jo.getString("imageurl2");
                        if (imgurl.length() > 0)
                            imgurls.add(imgurl);

                        imgurl = jo.getString("imageurl3");
                        if (imgurl.length() > 0)
                            imgurls.add(imgurl);
                        imgurl = jo.getString("imageurl4");
                        if (imgurl.length() > 0)
                            imgurls.add(imgurl);

                        //adding for headers
                        String header = jo.getString("uname") + "";
                        sectionManager = (sectionManager + 1) % 1;
                        sectionFirstPosition = i + headerCount;
                        lastHeader = header;
                        headerCount += 1;
                        Home_List_Data hld2 = new Home_List_Data(header, "header", sectionManager, sectionFirstPosition);
                        mItems.add(hld2);

                        if (jo.getString("utype").compareTo("private") == 0)
                            isprivate = true;
                        else
                            isprivate = false;

                        //storing userid with uname

                        SingleTon.lnduserid.put(jo.getString("uname"), jo.getString("user_id"));

                        //content
                        Home_List_Data hld = null;
                        if (SingleTon.pref.getString("user_id", "").compareToIgnoreCase(jo.getString("user_id")) == 0)
                            hld = new Home_List_Data(jo.getString("uname") + "", isprivate, "contentuser", sectionManager, sectionFirstPosition);
                        else
                            hld = new Home_List_Data(jo.getString("uname") + "", isprivate, "contentotheruser", sectionManager, sectionFirstPosition);
                        hld.setProfilepicurl(jo.getString("profile_pic"));
                        hld.setPricenow(jo.getString("price_now"));
                        hld.setPricewas(jo.getString("price_was"));
                        hld.setSize(jo.getString("size"));
                        hld.setLikestotal(jo.getInt("likes"));
                        hld.setImageurls(imgurls);
                        hld.setPost_id(jo.getString("post_id"));
                        hld.setDescription(jo.getString("description"));
                        hld.setUname(jo.getString("uname"));
                        hld.setLikedvalue(jo.getString("like"));
                        hld.setColors(jo.getString("color"));
                        hld.setConditon(jo.getString("condition"));
                        hld.setCategory(jo.getInt("category_type"));
                        hld.setUserid(jo.getString("user_id"));
                        hld.setBrandname(jo.getString("brand_name"));
                        hld.setProdtype(jo.getString("prod_type"));
                        hld.setTime(getMilliseconds(jo.getString("date_time")));
                        hld.setIssold(jo.getInt("issold"));
                        hld.setSwapstatus(jo.getInt("swap_status"));


                       /* if (hld.getCategory() == 1) {
                            String size = "";

                            try {
                                String[] lndbagsize = hld.getSize().split(",");
                                if (lndbagsize.length > 1) {
                                    for (int i = 0; i < lndbagsize.length; i++) {
                                        size = size + ConstantValues.bagsize[Integer.parseInt(lndbagsize[i])] + ",";
                                    }
                                    hld.setSize(size);
                                } else
                                    hld.setSize(ConstantValues.bagsize[Integer.parseInt(hld.getSize())]);


                            } catch (Exception ex) {
                                Log.e("error", ex.getMessage());
                            }
                        } else*/
                        if (hld.getCategory() == 2) {
                            String size = "";

                            try {
                                String[] lndbagsize = hld.getSize().split(",");
                                if (lndbagsize.length > 1) {
                                    for (int i = 0; i < lndbagsize.length; i++) {
                                        size = size + ConstantValues.bagsize[Integer.parseInt(lndbagsize[i])] + ",";
                                    }
                                    hld.setSize(size);
                                } else
                                    hld.setSize(ConstantValues.bagsize[Integer.parseInt(hld.getSize())]);


                            } catch (Exception ex) {
                                Log.e("error", ex.getMessage());
                            }
                        } else if (hld.getCategory() == 4) {
                            String color = "";

                            try {
                                String[] lndcolormetaltype = hld.getColors().split(",");
                                if (lndcolormetaltype.length > 1) {
                                    for (int i = 0; i < lndcolormetaltype.length; i++) {
                                        color = color + ConstantValues.metaltype[Integer.parseInt(lndcolormetaltype[i])] + ",";
                                    }
                                    hld.setColors(color);
                                } else
                                    hld.setColors(ConstantValues.metaltype[Integer.parseInt(hld.getColors())]);


                            } catch (Exception ex) {
                                Log.e("error", ex.getMessage());
                            }
                        }


                        //for header
                        if (jo.getInt("noti_type_home") == 1) {
                            hld2.setHeadertype(1);
                            hld2.setNotitotallikers(jo.getInt("noti_total"));
                            hld2.setNotilikedby(jo.getString("noti_users"));

                        } else if (jo.getInt("noti_type_home") == 2) {
                            hld2.setHeadertype(2);
                            hld2.setNotitotallikers(jo.getInt("noti_total"));
                            hld2.setNotilikedby(jo.getString("noti_users"));

                        } else if (jo.getInt("noti_type_home") == 3) {
                            hld2.setHeadertype(3);
                            hld2.setNotitotallikers(jo.getInt("noti_total"));
                            hld2.setNotilikedby(jo.getString("noti_users"));

                        } else
                            hld2.setHeadertype(0);

                        hld2.setProfilepicurl(jo.getString("profile_pic"));
                        hld2.setIssold(jo.getInt("issold"));

                        hld2.setPost_id(jo.getString("post_id"));

                        hld2.setUname(jo.getString("uname"));
                        hld2.setLikedvalue(jo.getString("like"));
                        //  hld2.setLikestotal(jo.getInt("likes"));

                        hld2.setUserid(jo.getString("user_id"));
                        hld2.setBrandname(jo.getString("brand_name"));
                        hld2.setSwapstatus(jo.getInt("swap_status"));


                        checkFavorate(hld);
                        mItems.add(hld);
                        i++;
                    }

                    skipdata = skipdata + jarray.length();
                    if (jarray.length() == 0)
                        loading = false;
                    else
                        loading = true;
                    //notifying adapter
                    mAdapter.notifyDataSetChanged();
                    firsttime = false;

                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage() + "", Toast.LENGTH_SHORT).show();
                    Log.e("json parsing error", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading = true;
                try {
                    dialog.setVisibility(View.GONE);
                    new com.eowise.recyclerview.stickyheaders.samples.AlertDialog().showAlertDialog(StickyActivity.this);
                } catch (Exception ex) {
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "4");
                params.put("skipdata", skipdata + "");
                params.put("user_id", SingleTon.pref.getString("user_id", ""));

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


}
