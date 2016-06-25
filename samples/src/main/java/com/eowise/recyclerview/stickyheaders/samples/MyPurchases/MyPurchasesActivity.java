package com.eowise.recyclerview.stickyheaders.samples.MyPurchases;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.Utils.LndUtils;
import com.eowise.recyclerview.stickyheaders.samples.Utils.TimeAgo;
import com.eowise.recyclerview.stickyheaders.samples.adapters.MyPurchasesAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.MySalesPurchasesData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPurchasesActivity extends AppCompatActivity {
    private List<MySalesPurchasesData> items = new ArrayList<MySalesPurchasesData>();
    private RecyclerView recyclerView;
    private MyPurchasesAdapter adapter;
    private AVLoadingIndicatorView loader;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager layoutManager;
    private boolean loading = true;
    private int skipdata=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchases);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        loader = (AVLoadingIndicatorView) findViewById(R.id.loader);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyPurchasesAdapter(this, items);
        recyclerView.setAdapter(adapter);
        getPurchases();

        //load more
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                           getPurchases();

                        }
                    }
                }
            }
        });



    }

    public void back(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void itemAccepted(View v) {
        Intent rateuser = new Intent(this, RateUserActivity.class);
        startActivity(rateuser);
    }

    private void getPurchases() {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_SHIPPINGINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("json", response);
                try {
                    loader.setVisibility(View.GONE);
                    JSONObject jobj = new JSONObject(response);
                    JSONArray jsonArray = jobj.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        MySalesPurchasesData mysales = new MySalesPurchasesData();
                        mysales.setOrder_id(jsonObject.getString("order_id"));
                        mysales.setCourier_type(jsonObject.getString("courier_type"));
                        mysales.setImage_url(jsonObject.getString("image_url"));
                        mysales.setShipping_method(jsonObject.getString("shipping_method"));
                        mysales.setOrder_date(TimeAgo.getMilliseconds(jsonObject.getString("order_date")));
                        mysales.setOrder_number(jsonObject.getString("order_number"));
                        mysales.setSeller_name(Capitalize.capitalize(jsonObject.getString("seller_name")));
                        mysales.setBrand_name(Capitalize.capitalizeFirstLetter(jsonObject.getString("brand_name")));
                        mysales.setPrice_now(jsonObject.getString("price_now"));
                        mysales.setTotal_amount(jsonObject.getString("total_amount"));
                        mysales.setSeller_id(jsonObject.getString("seller_id"));
                        mysales.setSeller_profile_pic(jsonObject.getString("seller_profile_pic"));
                        mysales.setShipping_charge(jsonObject.getString("shipping_charge"));
                        mysales.setService_type(jsonObject.getString("service_type"));
                        mysales.setTracking_no(jsonObject.getString("tracking_no"));
                        mysales.setCancel_description(jsonObject.getString("cancel_description"));
                        if (jsonObject.getString("order_purchase_status").compareToIgnoreCase("1") == 0)
                            mysales.setOrder_purchase_status("In Process");
                        else if (jsonObject.getString("order_purchase_status").compareToIgnoreCase("2") == 0)
                            mysales.setOrder_purchase_status("Shipped");
                        else if (jsonObject.getString("order_purchase_status").compareToIgnoreCase("3") == 0)
                            mysales.setOrder_purchase_status("Delivered");
                        else if (jsonObject.getString("order_purchase_status").compareToIgnoreCase("4") == 0)
                            mysales.setOrder_purchase_status("Item accepted");
                        else if (jsonObject.getString("order_purchase_status").compareToIgnoreCase("5") == 0)
                            mysales.setOrder_purchase_status("Order cancelled1");
                        else if (jsonObject.getString("order_purchase_status").compareToIgnoreCase("6") == 0)
                            mysales.setOrder_purchase_status("Rating reported");
                        else if (jsonObject.getString("order_purchase_status").compareToIgnoreCase("7") == 0)
                            mysales.setOrder_purchase_status("Order cancelled2");

                        items.add(mysales);

                    }
                    adapter.notifyDataSetChanged();
                    skipdata=items.size();
                    if(jsonArray.length()<25)
                        loading=false;
                    else
                        loading=true;
                } catch (JSONException e) {
                    loader.setVisibility(View.GONE);
                    try {
                        new com.eowise.recyclerview.stickyheaders.samples.AlertDialog().showAlertDialog(MyPurchasesActivity.this);
                    } catch (Exception ex) {

                    }
                    Log.e("jsonerror", e.getMessage() + "");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("response", error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "5");
                params.put("skipdata",skipdata+"");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 202 && data != null) {
            if (LndUtils.mysalepos > -1) {
                MySalesPurchasesData mspd = items.get(LndUtils.mysalepos);
                mspd.setOrder_purchase_status("Item accepted");
                adapter.notifyItemChanged(LndUtils.mysalepos);
                LndUtils.mysalepos = -1;
            }
        }
    }
}
