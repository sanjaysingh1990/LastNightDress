package com.eowise.recyclerview.stickyheaders.samples.PurchaseAndSwap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.Utils.RelativeTimeTextView;
import com.eowise.recyclerview.stickyheaders.samples.Utils.TimeAgo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Swap_Checkout_Cancel_Activity extends LndBaseActivity {


    @Bind(R.id.brandname)
    TextView brandname;
    @Bind(R.id.sellername)
    TextView sellername;
    @Bind(R.id.showtime)
    RelativeTimeTextView showtime;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.shippingprice)
    TextView shippingprice;
    @Bind(R.id.grandtotalprice)
    TextView grandtotalprice;
    @Bind(R.id.orderdate)
    TextView orderdate;
    @Bind(R.id.ordernumber)
    TextView ordernumber;
    @Bind(R.id.productimage)
    ImageView productimage;
    @Bind({R.id.sellertext, R.id.pricetext, R.id.shippingtext, R.id.grandtotaltext, R.id.orderdatetext, R.id.ordernumbertext})
    List<TextView> regularcheckout;
    @Bind(R.id.submit)
    TextView submit;
    @Bind(R.id.cancelinfoeditext)
    EditText inputedittext;
    @Bind(R.id.heading)
    TextView heading;

    private boolean check1 = true, check2 = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swap_checkout_cancel_order);
        ButterKnife.bind(this);
        for (int i = 0; i < regularcheckout.size(); i++) {
            regularcheckout.get(i).setTypeface(SingleTon.robotomedium);

        }
        //applying custom font
        heading.setTypeface(SingleTon.robotobold);
        brandname.setTypeface(SingleTon.robotobold);
        sellername.setTypeface(SingleTon.robotobold);
        showtime.setTypeface(SingleTon.robotobold);
        price.setTypeface(SingleTon.robotobold);
        shippingprice.setTypeface(SingleTon.robotobold);
        grandtotalprice.setTypeface(SingleTon.robotobold);
        orderdate.setTypeface(SingleTon.robotobold);
        ordernumber.setTypeface(SingleTon.robotobold);

        //read data from previous activity
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            try {
                JSONObject jobj = new JSONObject(extra.getString("data"));
                sellername.setText(Capitalize.capitalize(jobj.getString("uname")));
                price.setText("$" + jobj.getString("price"));
                //shippingprice.setText(jobj.getString("uname"));
                grandtotalprice.setText("$" + jobj.getString("total_amount"));
                // orderdate.setText(jobj.getString("uname"));
                ordernumber.setText(jobj.getString("order_id"));
                brandname.setText(Capitalize.capitalizeFirstLetter(jobj.getString("brand_name")));
                showtime.setReferenceTime(TimeAgo.getMilliseconds(jobj.getString("order_date")));
                SingleTon.imageLoader.displayImage(jobj.getString("image_url"), productimage, SingleTon.options4);

            } catch (JSONException e) {
                Log.e("jsonerror", e.getMessage() + "");
            }

        }
        disable();
        inputedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (check1 && s.length() > 0) {
                    enable();
                    check1 = false;
                    check2 = true;
                } else if (check2 && s.length() == 0) {
                    disable();
                    check2 = false;
                    check1 = true;
                }
            }
        });
    }

    private void enable() {
        submit.setClickable(true);
        submit.setBackgroundColor(getColorfromResource(this, R.color.lndcolor));
        submit.setTextColor(getColorfromResource(this, R.color.white));

    }

    private void disable() {
        submit.setClickable(false);
        submit.setBackgroundColor(getColorfromResource(this, R.color.gray));
        submit.setTextColor(getColorfromResource(this, R.color.sale_purchase_lable_color));
    }

    public void cancleOrder(View v) {

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            changeLayout();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/


    }

    private void showInfo(String text, TextView txt) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);
        Bitmap smiley = BitmapFactory.decodeResource(getResources(), R.drawable.shippinglabel_icon);
        ssb.setSpan(new ImageSpan(smiley), text.length() - 3, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txt.setText(ssb, TextView.BufferType.SPANNABLE);
    }

    public void finish(View v) {
        finish();
    }

    private void changeLayout() {
        setContentView(R.layout.lnd_swap_purchase_complete);
        TextView traninfo = (TextView) findViewById(R.id.transactioninfo);
        showInfo("Transaction Completed    ", traninfo);

    }

    public void back(View v) {
        finish();
    }

    private void cancelSwap(final String data) {
        showProgress("wait cancelling swap");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_FADEX_PURCHASE_SHIPPING_LABLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Log.e("json", response + "");
                dismissProgress();
                try {
                    JSONObject jobj = new JSONObject(response);

                } catch (Exception ex) {
                    dismissProgress();

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

}
