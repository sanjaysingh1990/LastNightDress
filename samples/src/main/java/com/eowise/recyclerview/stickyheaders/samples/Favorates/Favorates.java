package com.eowise.recyclerview.stickyheaders.samples.Favorates;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SQLDB.FavoriteData;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.CommentBean;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.Home_List_Data;
import com.eowise.recyclerview.stickyheaders.samples.TabDemo.LndShopActivity;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ConstantValues;
import com.eowise.recyclerview.stickyheaders.samples.Utils.TimeAgo;
import com.eowise.recyclerview.stickyheaders.samples.adapters.FavoratesAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.ShopData;


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

public class Favorates extends AppCompatActivity {
    private RecyclerView recyclerv;
    private FavoratesAdapter adapter;
    private List<FavoriteData> favitems = new ArrayList<FavoriteData>();
    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.showinfo)
    TextView showinfo;
    public static ArrayList<Home_List_Data> mItems = new ArrayList<>();
    //for sticky header
    boolean isprivate = false;
    String lastHeader = "";
    int sectionManager = -1;
    int headerCount = 0;
    int sectionFirstPosition = 0;
    private int count = 0;
    private int skipdata = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorates);
        //initizlizing butter knife library

        ButterKnife.bind(this);
        //Sqllite db object reference here

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerv = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerv.setLayoutManager(new GridLayoutManager(this, 3));

        adapter = new FavoratesAdapter(favitems, this);
        recyclerv.setAdapter(adapter);
        //custom fonts
        heading.setTypeface(SingleTon.hfont);
        mItems.clear();
        intialize();
    }

    private void intialize() {


        getFavorates();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void back(View v) {
        onBackPressed();
    }

    public void delFavorite(int pos) {
        favitems.remove(pos);
        adapter.notifyDataSetChanged();
    }

    public void getFavorates() {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/postdata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jo = jarray.getJSONObject(i);
                        FavoriteData fd = new FavoriteData();
                        fd.setCost(jo.getString("price_now"));
                        fd.setImageurl(jo.getString("imageurl1"));
                        fd.setPostid(jo.getString("post_id"));

                        favitems.add(fd);

//for full view
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
                        sectionFirstPosition = count + headerCount;
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
                        hld.setLikedvalue(jo.getString("isliked"));
                        hld.setColors(jo.getString("color"));
                        hld.setConditon(jo.getString("condition"));
                        hld.setCategory(jo.getInt("category_type"));
                        hld.setUserid(jo.getString("user_id"));
                        hld.setBrandname(jo.getString("brand_name"));
                        hld.setTime(TimeAgo.getMilliseconds(jo.getString("date_time")));
                        hld.setProdtype(jo.getString("prod_type"));
                        JSONArray commnets = jo.getJSONArray("postcoments");
                        hld.setSwapstatus(jo.getInt("swap_status"));
                        hld.setIssold(jo.getInt("issold"));

                        hld.setIsfavorate(true);
                        if (commnets.length() > 0) {

                            ArrayList<CommentBean> post_cont = new ArrayList<>();
                            for (int j = 0; j < commnets.length(); j++) {
                                JSONObject jsonObject = commnets.getJSONObject(j);
                                String uname = jsonObject.getString("uname");
                                String comment = jsonObject.getString("comment");
                                CommentBean cb = new CommentBean();
                                cb.setUname(uname);
                                cb.setComment(comment);
                                post_cont.add(cb);
                            }
                            hld.setUserpostcomments(post_cont);

                        } else {
                            ArrayList<CommentBean> post_cont = new ArrayList<>();
                            hld.setUserpostcomments(post_cont);

                        }
                        if (hld.getCategory() == 2) {
                            String size = "";

                            try {
                                String[] lndbagsize = hld.getSize().split(",");
                                if (lndbagsize.length > 1) {
                                    for (int j = 0; j < lndbagsize.length; j++) {
                                        size = size + ConstantValues.bagsize[Integer.parseInt(lndbagsize[j])] + ",";
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
                                    for (int j = 0; j < lndcolormetaltype.length; j++) {
                                        color = color + ConstantValues.metaltype[Integer.parseInt(lndcolormetaltype[j])] + ",";
                                    }
                                    hld.setColors(color);
                                } else
                                    hld.setColors(ConstantValues.metaltype[Integer.parseInt(hld.getColors())]);


                            } catch (Exception ex) {
                                Log.e("error", ex.getMessage());
                            }
                        }
                        //for header
                        hld2.setProfilepicurl(jo.getString("profile_pic"));


                        hld2.setPost_id(jo.getString("post_id"));
                        hld2.setUname(jo.getString("uname"));
                        hld2.setLikedvalue(jo.getString("isliked"));
                        hld2.setUserid(jo.getString("user_id"));
                        hld2.setBrandname(jo.getString("brand_name"));
                        mItems.add(hld);
                        count++;
                    }

                    if (favitems.size() == 0)
                        showinfo.setVisibility(View.VISIBLE);
                    // rv.setAdapter(adapter);
                    skipdata = favitems.size();
                   /* if (jarray.length() < 15) {
                        dataleft = false;

                    }*/
                    adapter.notifyDataSetChanged();
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // LndShopActivity.prog.setVisibility(View.GONE);


                //Log.e("response",error.getMessage()+"");
                try {
                    new com.eowise.recyclerview.stickyheaders.samples.AlertDialog().showAlertDialog(Favorates.this);
                } catch (Exception ex) {

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "18");
                params.put("user_id", SingleTon.pref.getString("user_id", ""));
                params.put("skipdata", skipdata + "");


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

    private void checkFavorate2(Home_List_Data hld) {
        FavoriteData fd = SingleTon.db.getContact(hld.getPost_id());
        if (fd != null) {
            hld.setIsfavorate(true);
        }
    }


}
