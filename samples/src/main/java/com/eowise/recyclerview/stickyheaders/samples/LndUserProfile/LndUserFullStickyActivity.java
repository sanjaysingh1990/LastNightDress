package com.eowise.recyclerview.stickyheaders.samples.LndUserProfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.Home_List_Data;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.LndHomeAdapter;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ConstantValues;
import com.init.superslim.LayoutManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class LndUserFullStickyActivity extends AppCompatActivity {

    private static final String TAG_COUNTRIES_FRAGMENT = "tag_countries_fragment";


    private LndHomeAdapter mAdapter;

    static int mHeaderDisplay;

    private boolean mAreMarginsFixed;
    static RecyclerView mRecyclerView;
    private static final String KEY_HEADER_POSITIONING = "key_header_mode";

    private static final String KEY_MARGINS_FIXED = "key_margins_fixed";

    private TextView heading;

    private int post_loc = 0;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String data="";
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sticky_header_layout);

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


        mAdapter = new LndHomeAdapter(this, mHeaderDisplay,LndProfile.mItems);
        //   mAdapter.setMarginsFixed(mAreMarginsFixed);
        mAdapter.setHeaderDisplay(mHeaderDisplay);
        mRecyclerView.setAdapter(mAdapter);


        //header reference and custom font
        heading = (TextView) findViewById(R.id.heading);
        heading.setText("MY POST");
        heading.setTextSize(18);
        heading.setTypeface(SingleTon.robotobold);
        //get intent
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            post_loc = extra.getInt("post_location", 0);

        }



        //pull
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*skipdata = 0;
                shopdata.clear();
                try {
                    // Toast.makeText(getActivity(), LndShopActivity.selectedcategory + "", Toast.LENGTH_SHORT).show();
                    pullrefresh = true;
                    getData(skipdata, LndShopActivity.selectedcategory);
                } catch (Exception ex) {

                }*/
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

        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.scrollToPosition((post_loc + 1) * 2 - 1);
            }
        }, 0);
        //notifying adapter
        mAdapter.notifyDataSetChanged();

    }








    @Override
    public void onBackPressed() {

        if(LndHomeAdapter.deleteditemssposition.size()>0)
        {
            Intent output = new Intent();
            output.putExtra("posarray", LndHomeAdapter.deleteditemssposition);
            setResult(5, output);
        }
        finish();

    }

}
