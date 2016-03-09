package com.eowise.recyclerview.stickyheaders.samples.LndUserProfile;

import android.os.Bundle;
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
import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.LndUserProfile.LndUserFullPostAdapter;
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

    private int mHeaderDisplay;

    private boolean mAreMarginsFixed;
    private RecyclerView mRecyclerView;
    private static final String KEY_HEADER_POSITIONING = "key_header_mode";

    private static final String KEY_MARGINS_FIXED = "key_margins_fixed";
    private ArrayList<Home_List_Data> mItems = new ArrayList<>();

    private TextView heading;
    private AVLoadingIndicatorView dialog;
    private int post_loc=0;


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
        heading= (TextView) findViewById(R.id.heading);
        heading.setText("MY POST");
        heading.setTextSize(18);
        heading.setTypeface(ImageLoaderImage.robotobold);
//loading spiiner
        dialog= (AVLoadingIndicatorView) findViewById(R.id.loader);
        //get intent
        Bundle extra= getIntent().getExtras();
        if(extra!=null) {
            post_loc = extra.getInt("post_location", 0);

        }

            getData();
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

               Log.e("response", response.toString());
                try {

                    boolean isprivate = false;
                    String lastHeader = "";
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
                        lastHeader = header;
                        headerCount += 1;
                        Home_List_Data hld2 = new Home_List_Data(header, "header", sectionManager, sectionFirstPosition);
                        mItems.add(hld2);

                        if (jo.getString("utype").compareTo("private")==0)
                            isprivate = true;
                        else
                            isprivate = false;

                        //content
                        Home_List_Data hld = new Home_List_Data(jo.getString("uname") + "", isprivate, "content", sectionManager, sectionFirstPosition);
                        hld.setProfilepicurl(jo.getString("profile_pic"));
                        hld.setPricenow(jo.getString("price_now"));
                        hld.setPricewas(jo.getString("price_was"));
                        hld.setSize(jo.getString("size"));
                        hld.setLikestotal(jo.getString("likes"));
                        hld.setImageurls(imgurls);
                        hld.setPost_id(jo.getString("post_id"));
                        hld.setDescription(jo.getString("description"));
                        hld.setUname(jo.getString("uname"));
                        hld.setLikedvalue(jo.getString("isliked"));
                        hld.setColors(jo.getString("color"));
                        hld.setConditon(jo.getString("condition"));
                        hld.setCategory(jo.getString("category_type"));
                        hld.setBrandname(jo.getString("brand_name"));
                        hld.setProdtype(jo.getString("prod_type"));

                        //for header
                        hld2.setProfilepicurl(jo.getString("profile_pic"));
                        hld2.setPricenow(jo.getString("price_now"));
                        hld2.setPricewas(jo.getString("price_was"));
                        hld2.setSize(jo.getString("size"));
                        hld2.setLikestotal(jo.getString("likes"));
                        hld2.setImageurls(imgurls);
                        hld2.setPost_id(jo.getString("post_id"));
                        hld2.setDescription(jo.getString("description"));
                        hld2.setUname(jo.getString("uname"));
                        hld2.setLikedvalue(jo.getString("isliked"));
                        hld2.setColors(jo.getString("color"));
                        hld2.setConditon(jo.getString("condition"));
                        hld2.setCategory(jo.getString("category_type"));
                        hld2.setBrandname(jo.getString("brand_name"));
                        hld2.setProdtype(jo.getString("prod_type"));

                        mItems.add(hld);
                    }


                    //notifying adapter
                    mAdapter.notifyDataSetChanged();

                    mRecyclerView.smoothScrollToPosition((post_loc+1)*2-1);
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
                params.put("rqid","11");
                params.put("user_id",ImageLoaderImage.pref.getString("user_id",""));
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


}
