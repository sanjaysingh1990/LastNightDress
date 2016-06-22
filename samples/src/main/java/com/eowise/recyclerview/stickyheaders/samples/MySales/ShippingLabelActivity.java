package com.eowise.recyclerview.stickyheaders.samples.MySales;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alihafizji.library.CreditCardEditText;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.LndBaseActivity;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;

import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.adapters.ShippingLabelAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.ShippingLabelData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShippingLabelActivity extends LndBaseActivity {

    // @Bind(R.id.recyclerView)
    //zaRecyclerView recyclerView;
    private List<ShippingLabelData> data = new ArrayList<ShippingLabelData>();
    private ShippingLabelAdapter recyclerAdapter;
    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.samepayment)
    TextView samepayment;
    @Bind(R.id.newpayment)
    TextView newpayment;
    @Bind(R.id.processmyorder)
    TextView processmyorder;
    @Bind(R.id.cardno)
    CreditCardEditText cardno;
    @Bind(R.id.weight)
    Spinner weightspinner;
    @Bind(R.id.service)
    Spinner servicespinner;
    @Bind(R.id.length)
    EditText length;
    @Bind(R.id.width)
    EditText width;
    @Bind(R.id.height)
    EditText height;
    @Bind(R.id.shippinglable)
    TextView shippinglable;
    @Bind(R.id.processinglabel)
    TextView processinglable;
    @Bind(R.id.total)
    TextView total;

    private String shipvalue = "", trackingno = "", couriercompany = "", orderid = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lnd_purchase_newshipping_level);
        ButterKnife.bind(this);

        heading.setTypeface(SingleTon.robotobold);
        //read order number from previous activity
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            orderid = extra.getString("orderid");
        }
    }

    private void initialize() {
        String[] weight = {"Up to 5", "6 pounds", "7 pounds", "8 pounds", "9 pounds", "10 pounds"};
        String[] price = {"pounds Free", "$3.99", "$7.98", "$11.97", "$15.96", "$19.95"};
        for (int i = 0; i < weight.length; i++) {
            ShippingLabelData shipdata = new ShippingLabelData();
            shipdata.setPackageweight(weight[i]);
            shipdata.setPackageprice(price[i]);
            shipdata.setSelected(false);
            data.add(shipdata);
        }
        recyclerAdapter.notifyDataSetChanged();
    }

    public void back(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("shipprice", shipvalue);
        intent.putExtra("trackingno", trackingno);
        intent.putExtra("couriercompany", couriercompany);

        setResult(2, intent);
        finish();
    }

    public void shippingChange(View v) {

       /* //finishing activity*/
    }

    public void changeColor(String value) {
        shipvalue = value;
        processmyorder.setClickable(true);
        processmyorder.setTextColor(Color.parseColor("#be4d66"));

    }

    public void samepayment(View v) {
        this.samepayment.setBackgroundColor(Color.parseColor("#be4d66"));
        this.samepayment.setTextColor(Color.parseColor("#ffffff"));
        this.samepayment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.payment_selection, 0, 0, 0);
        this.newpayment.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        this.newpayment.setBackgroundResource(R.drawable.purchse_rounded_corners);
        this.newpayment.setTextColor(Color.parseColor("#dbdbdb"));
        this.cardno.setEnabled(false);

    }

    public void newpayment(View v) {
        this.newpayment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.payment_selection, 0, 0, 0);
        this.samepayment.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        this.newpayment.setBackgroundColor(Color.parseColor("#be4d66"));
        this.newpayment.setTextColor(Color.parseColor("#ffffff"));
        this.samepayment.setBackgroundResource(R.drawable.purchse_rounded_corners);
        this.samepayment.setTextColor(Color.parseColor("#dbdbdb"));
        this.cardno.setEnabled(true);

    }

    public void PurchaseShippment(View v) {

        if (weightspinner.getSelectedItemPosition() == 0) {
            showToast("Select Package weight");
            return;
        } else if (length.getText().length() == 0) {
            showToast("Select Package Length");
            return;

        } else if (width.getText().length() == 0) {
            showToast("Select Package Width");
            return;

        } else if (height.getText().length() == 0) {
            showToast("Select Package Height");
            return;

        } else if (servicespinner.getSelectedItemPosition() == 0) {
            showToast("Select Package Service");
            return;
        }

        if (servicespinner.getSelectedItemPosition() == 1) {
            PurchaseShippingLable(createJsonFedex());
        } else if (servicespinner.getSelectedItemPosition() == 2) {
            PurchaseShippingLable(createJsonCanadaPost());
        }
    }

    private void shippmentDone() {
        setContentView(R.layout.lnd_shipping_label_purchase_complete);
        TextView info = (TextView) findViewById(R.id.info);
        Spannable word = new SpannableString("Thank you");

        word.setSpan(new ForegroundColorSpan(Color.parseColor("#be4d66")), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        word.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        info.setText(word);
        Spannable wordTwo = new SpannableString(", your order is completed.");

        wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#222427")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        info.append(wordTwo);
    }

    private void PurchaseShippingLable(final String data) {
        showProgress("wait processing");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_FADEX_PURCHASE_SHIPPING_LABLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                 Log.e("json", response + "");
                dismissProgress();
                try {
                    JSONObject jobj = new JSONObject(response);
                    JSONObject metajsonobj = jobj.getJSONObject("Meta");
                    int responsecode = metajsonobj.getInt("Code");
                    if (responsecode == 200) {
                        JSONObject datajsonobj = jobj.getJSONObject("Data");
                        shippinglable.setText("$" + datajsonobj.getString("Charges"));
                        total.setText("$" + datajsonobj.getString("Charges"));

                        JSONArray jarray = datajsonobj.getJSONArray("Packages");
                        JSONObject jsonpackageinfo = jarray.getJSONObject(0);


                        //create jsonobject to save to database
                        JSONObject requestobject = new JSONObject();
                        requestobject.put("post_id", 116);
                        requestobject.put("order_id", 116123);
                        requestobject.put("date_time", SingleTon.getCurrentTimeStamp());
                        requestobject.put("shipping_label_image", jsonpackageinfo.getString("Label"));
                        requestobject.put("weight", weightspinner.getSelectedItem().toString());
                        requestobject.put("length", length.getText().toString());
                        requestobject.put("width", width.getText().toString());
                        requestobject.put("height", height.getText().toString());
                        requestobject.put("service_type", servicespinner.getSelectedItem().toString());
                        requestobject.put("total_charges", datajsonobj.getString("Charges"));
                        requestobject.put("shipping_id", datajsonobj.getInt("TrackingNumber"));
                        requestobject.put("tracking_no", datajsonobj.getInt("ShipmentId"));
                        requestobject.put("user_id", SingleTon.pref.getString("user_id", ""));
                        requestobject.put("order_id", orderid);
                        shipvalue = datajsonobj.getString("Charges");
                        trackingno = datajsonobj.getInt("TrackingNumber") + "";
                        couriercompany = servicespinner.getSelectedItem().toString();

                        // Log.e("json",requestobject.toString());
                        saveShippingLable(requestobject.toString());
                    }
                } catch (Exception ex) {
                    Log.e("jsonerror", ex.getMessage() + "");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgress();
                Log.e("response", error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("data", data);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);

        queue.add(sr);

    }

    private String createJsonFedex() {

        JSONObject shippingrequest = new JSONObject();
        try {
            shippingrequest.put("toCompany", "John Doe");
            shippingrequest.put("toName", "John Doe");
            shippingrequest.put("toPhone", "1231231234");
            shippingrequest.put("toAddr1", "111 W Legion");
            shippingrequest.put("toCity", "Whitehall");
            shippingrequest.put("toState", "MT");
            shippingrequest.put("toCode", "59759");

            shippingrequest.put("length", "15");
            shippingrequest.put("width", "25");
            shippingrequest.put("height", "5");
            shippingrequest.put("weight", "55");
            Log.e("json", shippingrequest.toString());
        } catch (JSONException ex) {
            Log.e("error", ex.getMessage());
        }
        return shippingrequest.toString();
    }

    private String createJsonCanadaPost() {

        JSONObject shippingrequest = new JSONObject();
        try {
            shippingrequest.put("shipCountry", "Abc");
            shippingrequest.put("shipState", "ON");
            shippingrequest.put("shipCity", "Ottawa");
            shippingrequest.put("shipCode", "K1A0B1");
            shippingrequest.put("toName", "Mark Sanborn");
            shippingrequest.put("toAddr1", "361A Old Finch Avenue");
            shippingrequest.put("toCity", "Toronto");
            shippingrequest.put("toState", "ON");
            shippingrequest.put("toCode", "M1B5K7");
            shippingrequest.put("toCountry", "CA");
            shippingrequest.put("width", "5");
            shippingrequest.put("height", "5");
            shippingrequest.put("weight", "5");

        } catch (JSONException ex) {
            Log.e("error", ex.getMessage());
        }
        return shippingrequest.toString();
    }

    private void saveShippingLable(final String data) {
        showProgress("wait saving shipping information");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_SHIPPINGINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("json", response + "");
                dismissProgress();
                try {

                    shippmentDone();
                } catch (Exception ex) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgress();
                Log.e("response", error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("data", data);
                params.put("rqid", 2 + "");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);

        queue.add(sr);

    }

}
