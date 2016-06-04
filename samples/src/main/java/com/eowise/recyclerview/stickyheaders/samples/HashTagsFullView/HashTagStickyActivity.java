package com.eowise.recyclerview.stickyheaders.samples.HashTagsFullView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.Home_List_Data;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.LndHomeAdapter;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.Utils.TimeAgo;
import com.init.superslim.LayoutManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class HashTagStickyActivity extends AppCompatActivity {


    private LndHomeAdapter mAdapter;

    private int mHeaderDisplay;

    private boolean mAreMarginsFixed;
    private RecyclerView mRecyclerView;
    private static final String KEY_HEADER_POSITIONING = "key_header_mode";

    private static final String KEY_MARGINS_FIXED = "key_margins_fixed";
    private ArrayList<Home_List_Data> mItems = new ArrayList<>();

    private TextView heading;

    private SwipeRefreshLayout swipeRefreshLayout;
    private int post_loc = 0;
    @Bind(R.id.loader)
    AVLoadingIndicatorView loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sticky_header_layout);

        ButterKnife.bind(this);
        //hiding progress bar

        loader.setVisibility(View.GONE);


//for header
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

        mRecyclerView.setLayoutManager(new LayoutManager(this));


        mAdapter = new LndHomeAdapter(this, mHeaderDisplay, setData());
        //   mAdapter.setMarginsFixed(mAreMarginsFixed);
        mAdapter.setHeaderDisplay(mHeaderDisplay);
        mRecyclerView.setAdapter(mAdapter);


        //header reference and custom font
        heading = (TextView) findViewById(R.id.heading);

//pull to refresh
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

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String hashtag = extra.getString("hashtag");
            post_loc = extra.getInt("post_location", 0);

            int type = extra.getInt("type");
            if (type == 1) {
                heading.setText("#" + hashtag);

            } else {
                heading.setText(hashtag);

            }
            heading.setTextSize(18);
            getData();


        }

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


    public void getData() {


        // Log.e("dataresponse", response.toString());
        try {

            boolean isprivate = false;
            String lastHeader = "";
            int sectionManager = -1;
            int headerCount = 0;
            int sectionFirstPosition = 0;

            JSONObject jobj = new JSONObject(LndBrandHashTagGridViewActivity.data);
            JSONArray jarray = jobj.getJSONArray("data");
            for (int i = 0; i < jarray.length(); i++) {


                JSONObject jo = jarray.getJSONObject(i);
                ArrayList<String> imgurls = new ArrayList<String>();
                String imgurl=jo.getString("imageurl1");

                imgurls.add(imgurl);

                imgurl=jo.getString("imageurl2");
                if(imgurl.length()>0)
                    imgurls.add(imgurl);

                imgurl=jo.getString("imageurl3");
                if(imgurl.length()>0)
                    imgurls.add(imgurl);
                imgurl=jo.getString("imageurl4");
                if(imgurl.length()>0)
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
                hld.setTotalcomments(jo.getInt("post_total_comment"));
                hld.setIssold(jo.getInt("issold"));
                hld.setTime(TimeAgo.getMilliseconds(jo.getString("date_time")));
                JSONArray commnets=jo.getJSONArray("postcoments");
                if(commnets.length()>0)
                {

                    ArrayList<SpannableString> post_cont = new ArrayList<>();
                    for (int j = 0; j < commnets.length(); j++) {
                        JSONObject jsonObject=commnets.getJSONObject(j);
                        String uname = jsonObject.getString("uname");
                        String comment = jsonObject.getString("comment");

                        SpannableString word = new SpannableString(Capitalize.capitalizeFirstLetter(uname+" "+comment));

                        word.setSpan(new ForegroundColorSpan(Color.parseColor("#be4d66")), 0, uname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        word.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, uname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        post_cont.add(word);
                    }
                    hld.setPostcomments(post_cont);

                } else {
                    ArrayList<SpannableString> post_cont = new ArrayList<>();
                    hld.setPostcomments(post_cont);

                }

                //for header
                hld2.setProfilepicurl(jo.getString("profile_pic"));
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
                hld2.setBrandname(jo.getString("brand_name"));
                mItems.add(hld);
            }

            //scrolling adapter to position
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.scrollToPosition((post_loc + 1) * 2 - 1);
                }
            }, 0);

            //notifying adapter
            mAdapter.notifyDataSetChanged();


        } catch (Exception ex) {
            Log.e("json parsing error", ex.getMessage());
        }
    }


}
