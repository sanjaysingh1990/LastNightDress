package com.eowise.recyclerview.stickyheaders.samples.TabDemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.eowise.recyclerview.stickyheaders.samples.Favorates.Favorates;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SQLDB.FavoriteData;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.Home_List_Data;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ConstantValues;
import com.eowise.recyclerview.stickyheaders.samples.adapters.MarginDecoration;
import com.eowise.recyclerview.stickyheaders.samples.adapters.NumberedAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.ShopData;
import com.eowise.recyclerview.stickyheaders.samples.peopleandhashtag.PeopleHashTagActivity;

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


public class LndFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    RecyclerView recyclerv;
    public static ArrayList<ShopData> shopdata = new ArrayList<>();
    public static NumberedAdapter adapter;
    protected Handler handler;
    public int skipdata = 0;
    private boolean loadmore = false;
    private boolean dataleft = true;
    private int ft = 0;
    @Bind(R.id.search)
    TextView search;
    static ImageButton favorate;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int previousTotal = 0;
    private boolean pullrefresh = false;
    boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static ArrayList<Home_List_Data> mItems = new ArrayList<>();
    public static LndFragment lndshopactivity;
    //for sticky header
    boolean isprivate = false;
    String lastHeader = "";
    int sectionManager = -1;
    int headerCount = 0;
    int sectionFirstPosition = 0;
    private int count = 0;
    private boolean isfirttime = true;
    private String query = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View shopview = inflater.inflate(
                R.layout.fragment_lnd_shop, container, false);
        lndshopactivity = this;
        ButterKnife.bind(this, shopview);
        search.setHint("Search People, Brands & Hashtags");
        search.setTypeface(SingleTon.robotoregular);
        setupRecyclerView(shopview);
        handler = new Handler();
        try {
            shopdata.clear();

            getData(skipdata, query);


        } catch (Exception ex) {

        }

        //favorate button reference
        favorate = (ImageButton) shopview.findViewById(R.id.favorate);
        shopview.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent peoplebrandhashtag = new Intent(getActivity(), PeopleHashTagActivity.class);
                startActivity(peoplebrandhashtag);
            }
        });
        shopview.findViewById(R.id.favorate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favorate = new Intent(getActivity(), Favorates.class);
                startActivity(favorate);
            }
        });

        //checking favorate
        checkFavorate();
//pull
        swipeRefreshLayout = (SwipeRefreshLayout) shopview.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                skipdata = 0;
                dataleft = true;
                loading = true;
                isprivate = false;
                sectionManager = -1;
                headerCount = 0;
                sectionFirstPosition = 0;
                count = 0;
                try {
                    // Toast.makeText(getActivity(), LndShopActivity.selectedcategory + "", Toast.LENGTH_SHORT).show();
                    pullrefresh = true;
                    getData(skipdata, "");
                } catch (Exception ex) {

                }
            }
        });

        //clear list
        return shopview;
    }

    private void setupRecyclerView(View recyclerView) {
        recyclerv = (RecyclerView) recyclerView.findViewById(R.id.recycler_view);
        recyclerv.addItemDecoration(new MarginDecoration(getActivity()));
        recyclerv.setHasFixedSize(true);
        final GridLayoutManager gridlayoutm = new GridLayoutManager(getActivity(), 3);
        recyclerv.setLayoutManager(gridlayoutm);
        adapter = new NumberedAdapter(shopdata, getActivity(), 200, recyclerv);
        recyclerv.setAdapter(adapter);

//load more
        recyclerv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = gridlayoutm.getChildCount();
                    totalItemCount = gridlayoutm.getItemCount();
                    pastVisiblesItems = gridlayoutm.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;

                            shopdata.add(null);
                            adapter.notifyItemInserted(shopdata.size() - 1);

                            if (dataleft)
                                try {
                                    loadmore = true;
                                    getData(skipdata, query);
                                } catch (Exception ex) {

                                }
                            else {

                                try {
                                    shopdata.remove(shopdata.size() - 1);

                                    adapter.notifyItemRemoved(shopdata.size() - 1);


                                } catch (Exception ex) {

                                }
                            }


                        }
                    }
                }
            }
        });


    }

    public void getData(final int dataskip, final String query) throws Exception {
        if (pullrefresh) {
            LndShopActivity.prog.setVisibility(View.GONE);
        }
        if (isfirttime) {
            LndShopActivity.prog.setVisibility(View.VISIBLE);
            isfirttime = false;
        }
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_POST_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (pullrefresh) {
                    shopdata.clear();
                    mItems.clear();
                    adapter.notifyDataSetChanged();


                }
                pullrefresh = false;
                loading = true;

                try {
                    LndShopActivity.prog.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    if (loadmore) {
                        shopdata.remove(shopdata.size() - 1);

                        adapter.notifyItemRemoved(shopdata.size() - 1);
                        loadmore = false;

                    }
                } catch (Exception ex) {

                }
                //Log.e("response", response.toString());
                try {

                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jo = jarray.getJSONObject(i);
                        ShopData pdb = new ShopData();
                        pdb.setPrice(jo.getString("price_now"));
                        pdb.setImageurl(jo.getString("imageurl1"));
                        pdb.setIssold(jo.getInt("issold"));
                        shopdata.add(pdb);

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
                        hld.setLikedvalue(jo.getString("like"));
                        hld.setColors(jo.getString("color"));
                        hld.setConditon(jo.getString("condition"));
                        hld.setCategory(jo.getInt("category_type"));
                        hld.setUserid(jo.getString("user_id"));
                        hld.setBrandname(jo.getString("brand_name"));
                        hld.setTime(getMilliseconds(jo.getString("date_time")));
                        hld.setProdtype(jo.getString("prod_type"));
                        hld.setIssold(jo.getInt("issold"));
                        hld.setTotalcomments(jo.getInt("post_total_comment"));
                        JSONArray commnets = jo.getJSONArray("postcoments");
                        if (commnets.length() > 0) {

                            ArrayList<SpannableString> post_cont = new ArrayList<>();
                            for (int j = 0; j < commnets.length(); j++) {
                                JSONObject jsonObject = commnets.getJSONObject(j);
                                String uname = jsonObject.getString("uname");
                                String comment = jsonObject.getString("comment");

                                SpannableString word = new SpannableString(Capitalize.capitalizeFirstLetter(uname + " " + comment));

                                word.setSpan(new ForegroundColorSpan(Color.parseColor("#be4d66")), 0, uname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                word.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, uname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                post_cont.add(word);
                            }
                            hld.setPostcomments(post_cont);

                        } else {
                            ArrayList<SpannableString> post_cont = new ArrayList<>();
                            hld.setPostcomments(post_cont);

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
                        hld2.setLikedvalue(jo.getString("like"));
                        hld2.setUserid(jo.getString("user_id"));
                        hld2.setBrandname(jo.getString("brand_name"));
                        hld2.setIssold(jo.getInt("issold"));

                        checkFavorate2(hld);
                        mItems.add(hld);
                        count++;
                    }


                    // rv.setAdapter(adapter);
                    skipdata = shopdata.size();
                    if (jarray.length() < 15) {
                        dataleft = false;

                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LndShopActivity.prog.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                dataleft = true;
                if (!pullrefresh)
                    try {
                        shopdata.remove(shopdata.size() - 1);

                        adapter.notifyItemRemoved(shopdata.size() - 1);


                    } catch (Exception ex) {

                    }
                pullrefresh = false;

                //Log.e("response",error.getMessage()+"");
                try {
                    new com.eowise.recyclerview.stickyheaders.samples.AlertDialog().showAlertDialog(getActivity());
                } catch (Exception ex) {

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "14");
                params.put("user_id", SingleTon.pref.getString("user_id", ""));
                params.put("skipdata", dataskip + "");
                params.put("query", query);


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

    public static void checkFavorate() {
        try {
            List<FavoriteData> fav = SingleTon.db.getAllContacts();

            if (fav.size() > 0) {
                favorate.setImageResource(R.drawable.filled_favorate_icon2);
            } else
                favorate.setImageResource(R.drawable.empty_favorate_icon);
        } catch (Exception ex)


        {

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

    private void checkFavorate2(Home_List_Data hld) {
        FavoriteData fd = SingleTon.db.getContact(hld.getPost_id());
        if (fd != null) {
            hld.setIsfavorate(true);
        }
    }

    public void updateList(ArrayList<String> pos) {

        for (String position : pos) {
            int repos = Integer.parseInt(position);
            shopdata.remove(repos);
            adapter.notifyDataSetChanged();


        }
    }

    public void reset(String data) {
        isprivate = false;
        lastHeader = "";
        sectionManager = -1;
        headerCount = 0;
        sectionFirstPosition = 0;

        skipdata = 0;
        query = data;
        isfirttime = true;
        try {
            shopdata.clear();
            mItems.clear();
            dataleft = true;
            count = 0;
            getData(skipdata, query);
        } catch (Exception ex) {

        }
    }
}
