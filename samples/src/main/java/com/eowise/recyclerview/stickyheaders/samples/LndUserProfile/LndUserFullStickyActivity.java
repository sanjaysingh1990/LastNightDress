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
import com.init.superslim.LayoutManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class LndUserFullStickyActivity extends AppCompatActivity {

    private static final String TAG_COUNTRIES_FRAGMENT = "tag_countries_fragment";


    private LndUserFullPostAdapter mAdapter;

    static int mHeaderDisplay;

    private boolean mAreMarginsFixed;
    static RecyclerView mRecyclerView;
    private static final String KEY_HEADER_POSITIONING = "key_header_mode";

    private static final String KEY_MARGINS_FIXED = "key_margins_fixed";
    private ArrayList<Home_List_Data> mItems = new ArrayList<>();
    private ArrayList<String> deleteditemssposition=new ArrayList<>();

    private TextView heading;
    private AVLoadingIndicatorView dialog;
    private int post_loc = 0;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String data="";
    private ArrayList<String> deleteditems=new ArrayList<>();
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


        mAdapter = new LndUserFullPostAdapter(this, mHeaderDisplay, setData());
        //   mAdapter.setMarginsFixed(mAreMarginsFixed);
        mAdapter.setHeaderDisplay(mHeaderDisplay);
        mRecyclerView.setAdapter(mAdapter);


        //header reference and custom font
        heading = (TextView) findViewById(R.id.heading);
        heading.setText("MY POST");
        heading.setTextSize(18);
        heading.setTypeface(SingleTon.robotobold);
//loading spiiner
        dialog = (AVLoadingIndicatorView) findViewById(R.id.loader);
        //get intent
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            post_loc = extra.getInt("post_location", 0);

        }

        getData();

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


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/postdata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.setVisibility(View.GONE);

                Log.e("json", response.toString());
                data=response;
                try {

                    boolean isprivate = false;

                    int sectionManager = -1;
                    int headerCount = 0;
                    int sectionFirstPosition = 0;

                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");

                    for (int i = 0; i < jarray.length(); i++) {


                        JSONObject jo = jarray.getJSONObject(i);
                        ArrayList<String> imgurls = new ArrayList<String>();
                        imgurls.add(jo.getString("imageurl1"));
                        imgurls.add(jo.getString("imageurl2"));
                        imgurls.add(jo.getString("imageurl3"));
                        imgurls.add(jo.getString("imageurl4"));
                        //adding for headers
                        String header = jo.getString("uname") + "";

                        sectionManager = (sectionManager + 1) % 1;
                        sectionFirstPosition = i + headerCount;

                        headerCount += 1;
                        Home_List_Data hld2 = new Home_List_Data(header, "header", sectionManager, sectionFirstPosition);
                        mItems.add(hld2);

                        if (jo.getString("utype").compareTo("private") == 0)
                            isprivate = true;
                        else
                            isprivate = false;

                        //content
                        Home_List_Data hld = new Home_List_Data(jo.getString("uname") + "", isprivate, "content", sectionManager, sectionFirstPosition);
                        hld.setProfilepicurl(jo.getString("profile_pic"));
                        hld.setPricenow(jo.getString("price_now"));
                        hld.setPricewas(jo.getString("price_was"));
                        hld.setSize(jo.getString("size"));
                        hld.setLikestotal(jo.getInt("likes"));
                        hld.setImageurls(imgurls);
                        hld.setPost_id(jo.getString("post_id"));
                        hld.setDescription(jo.getString("description"));
                        hld.setUname(jo.getString("uname"));
                        hld.setLikedvalue(jo.getString("isliked"));
                        hld.setColors(jo.getString("color"));
                        hld.setConditon(jo.getString("condition"));
                        hld.setCategory(jo.getInt("category_type"));
                        hld.setBrandname(jo.getString("brand_name"));
                        hld.setUserid(jo.getString("user_id"));
                        hld.setProdtype(jo.getString("prod_type"));

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
                        hld2.setLikedvalue(jo.getString("isliked"));
                        hld2.setColors(jo.getString("color"));
                        hld2.setConditon(jo.getString("condition"));
                        hld2.setCategory(jo.getInt("category_type"));
                        hld2.setBrandname(jo.getString("brand_name"));
                        hld2.setProdtype(jo.getString("prod_type"));
                        hld2.setUserid(jo.getString("user_id"));
                        mItems.add(hld);
                    }

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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.setVisibility(View.GONE);
   /* try {
                    new com.eowise.recyclerview.stickyheaders.samples.AlertDialog().showAlertDialog(MainActivity.this);
                }
                catch(Exception ex)
                {

                }*/
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "11");
                params.put("user_id", SingleTon.pref.getString("user_id", ""));
                params.put("skip", "0");
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

public void delete(final int pos)
{
    deleteditems.add(mItems.get(pos).getPost_id());

    mItems.clear();
    deleteditemssposition.clear();
    try {

        int count=0;
        boolean isprivate = false;

        int sectionManager = -1;
        int headerCount = 0;
        int sectionFirstPosition = 0;

        JSONObject jobj = new JSONObject(data);
        JSONArray jarray = jobj.getJSONArray("data");

        for (int i = 0; i < jarray.length(); i++) {

            JSONObject jo = jarray.getJSONObject(i);

            if(deleteditems.contains(jo.getString("post_id"))) {
              //  Toast.makeText(this,"contains",Toast.LENGTH_SHORT).show();
                deleteditemssposition.add(i+"");
                continue;

            }
          //  Log.e("position",i+"");

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
            sectionFirstPosition = count + headerCount;

            headerCount += 1;
            Home_List_Data hld2 = new Home_List_Data(header, "header", sectionManager, sectionFirstPosition);
            mItems.add(hld2);

            if (jo.getString("utype").compareTo("private") == 0)
                isprivate = true;
            else
                isprivate = false;

            //content
            Home_List_Data hld = new Home_List_Data(jo.getString("uname") + "", isprivate, "content", sectionManager, sectionFirstPosition);
            hld.setProfilepicurl(jo.getString("profile_pic"));
            hld.setPricenow(jo.getString("price_now"));
            hld.setPricewas(jo.getString("price_was"));
            hld.setSize(jo.getString("size"));
            hld.setLikestotal(jo.getInt("ikes"));
            hld.setImageurls(imgurls);
            hld.setPost_id(jo.getString("post_id"));
            hld.setDescription(jo.getString("description"));
            hld.setUname(jo.getString("uname"));
            hld.setLikedvalue(jo.getString("isliked"));
            hld.setColors(jo.getString("color"));
            hld.setConditon(jo.getString("condition"));
            hld.setCategory(jo.getInt("category_type"));
            hld.setBrandname(jo.getString("brand_name"));
            hld.setProdtype(jo.getString("prod_type"));
            hld.setUserid(jo.getString("user_id"));
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
            hld2.setLikedvalue(jo.getString("isliked"));
            hld2.setColors(jo.getString("color"));
            hld2.setConditon(jo.getString("condition"));
            hld2.setCategory(jo.getInt("category_type"));
            hld2.setBrandname(jo.getString("brand_name"));
            hld2.setProdtype(jo.getString("prod_type"));
            hld2.setUserid(jo.getString("user_id"));
            mItems.add(hld);
            count++;
        }

        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.scrollToPosition((pos-1 + 1) * 2 - 1);
            }
        }, 0);
        //notifying adapter
        mAdapter.notifyDataSetChanged();


    } catch (Exception ex) {
        Log.e("json parsing error", ex.getMessage());
    }

}

    @Override
    public void onBackPressed() {

        if(deleteditems.size()>0)
        {
            Intent output = new Intent();
            output.putExtra("posarray", deleteditemssposition);
            setResult(5, output);
        }
        finish();

    }
}
