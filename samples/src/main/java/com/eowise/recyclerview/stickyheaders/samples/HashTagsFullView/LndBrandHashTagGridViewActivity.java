package com.eowise.recyclerview.stickyheaders.samples.HashTagsFullView;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.adapters.MarginDecoration;
import com.eowise.recyclerview.stickyheaders.samples.adapters.NumberedAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.ShopData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LndBrandHashTagGridViewActivity extends AppCompatActivity {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerv;
    public ArrayList<ShopData> shopdata = new ArrayList<>();
    public static NumberedAdapter adapter;
    protected Handler handler;
    public int skipdata = 0;
    private boolean loadmore = false;
    private boolean dataleft = true;
    private int ft = 0;
    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.loader)
    AVLoadingIndicatorView prog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int previousTotal = 0;
    private boolean pullrefresh = false;
    public static String hashtagorbrand = "";
    public static String data = "";
    private int  type=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lnd_brand_hash_tag_grid_view);
        ButterKnife.bind(this);

        //pull to refresh here
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                skipdata = 0;
                shopdata.clear();
                try {
                    // Toast.makeText(getActivity(), LndShopActivity.selectedcategory + "", Toast.LENGTH_SHORT).show();
                    pullrefresh = true;
                    // getData(skipdata, LndShopActivity.selectedcategory);
                } catch (Exception ex) {

                }
            }
        });


        //checking request
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String hashtag = extra.getString("hashtag");
            hashtagorbrand = hashtag;
            heading.setTextSize(18);
             type = extra.getInt("type");

            if (type == 1) {
                heading.setText("#" + hashtag);
                getData(hashtag, 15);
            } else {
                heading.setText(hashtag);
                getData(hashtag, 16);
            }
            setupRecyclerView();
        }

    }

    private void setupRecyclerView() {
        recyclerv.addItemDecoration(new MarginDecoration(this));
        recyclerv.setHasFixedSize(true);
        final GridLayoutManager gridlayoutm = new GridLayoutManager(this, 3);
        recyclerv.setLayoutManager(gridlayoutm);
        adapter = new NumberedAdapter(shopdata, this, 300,type);
        recyclerv.setAdapter(adapter);
        /*adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom

                shopdata.add(null);
                adapter.notifyItemInserted(shopdata.size() - 1);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadmore = true;
                        if (dataleft)
                            try {
                                getData(skipdata, LndShopActivity.selectedcategory);
                            } catch (Exception ex) {

                            }
                        else {

                            try {
                                shopdata.remove(shopdata.size() - 1);

                                adapter.notifyItemRemoved(shopdata.size() - 1);

                                adapter.setLoaded();
                            }
                            catch (Exception ex)
                            {

                            }
                        }
                    }
                }, 1000);

            }
        });*/


    }

    public void getData(final String hashtagorbrand, final int rqid) {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_POST_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Log.e("dataresponse", response.toString());
                try {
                    prog.setVisibility(View.GONE);
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");
                    if (jarray.length() == 0) {
                        dataleft = false;
                        return;
                    }
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jo = jarray.getJSONObject(i);
                        ShopData pdb = new ShopData();
                        pdb.setPrice(jo.getString("price_now"));
                        pdb.setImageurl(jo.getString("imageurl1"));
                        pdb.setIssold(jo.getInt("issold"));

                        shopdata.add(pdb);
                    }
                    //storing data here for full view in next
                    data = response;
                    // rv.setAdapter(adapter);
                    skipdata = shopdata.size();
                    adapter.notifyDataSetChanged();
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    prog.setVisibility(View.GONE);

                    new com.eowise.recyclerview.stickyheaders.samples.AlertDialog().showAlertDialog(LndBrandHashTagGridViewActivity.this);
                } catch (Exception ex) {
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", rqid + "");
                params.put("skipdata", 0 + "");
                params.put("user_id", SingleTon.pref.getString("user_id", ""));
                params.put("hashtagorbrand", hashtagorbrand);

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
