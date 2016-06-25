package com.eowise.recyclerview.stickyheaders.samples.MySales;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.eowise.recyclerview.stickyheaders.samples.Utils.LndUtils;
import com.eowise.recyclerview.stickyheaders.samples.Utils.TimeAgo;
import com.eowise.recyclerview.stickyheaders.samples.data.MySalesPurchasesData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Cancel_Sale_Activity extends LndBaseActivity {
    @Bind({R.id.buyertext, R.id.pricetext, R.id.yourearningtext, R.id.couriertext, R.id.orderdatetext, R.id.ordernumbertext, R.id.shippingmethodtext})
    List<TextView> inprocess;
    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.brandname)
    TextView brandname;
    @Bind(R.id.buyername)
    TextView buyername;

    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.yourearning)
    TextView yourearning;
    @Bind(R.id.courier)
    TextView courier;

    @Bind(R.id.orderdate)
    TextView orderdate;
    @Bind(R.id.ordernumber)
    TextView ordernumber;
    @Bind(R.id.productimage)
    ImageView productimage;
    @Bind(R.id.shipppingmethod)
    TextView shippingmethod;
    @Bind(R.id.cancelinfoeditext)
    EditText canceledittext;
    @Bind(R.id.submit)
    TextView submit;
    private boolean check1 = true, check2 = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_cancelling_layout);
        ButterKnife.bind(this);
        initialize();
    }

    @Override
    public void enable() {
        submit.setClickable(true);
        submit.setTextColor(getColorfromResource(this, R.color.lndcolor));
    }

    @Override
    public void disable() {
        submit.setClickable(false);
        submit.setTextColor(getColorfromResource(this, R.color.sale_purchase_lable_color));


    }

    private void initialize() {

        canceledittext.addTextChangedListener(new TextWatcher() {
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
                    check1 = true;
                    check2 = false;
                }
            }
        });

        //applying custom fonts
        for (int i = 0; i < inprocess.size(); i++) {
            inprocess.get(i).setTypeface(SingleTon.robotomedium);

        }
        //applying custom font
        brandname.setTypeface(SingleTon.robotobold);
        buyername.setTypeface(SingleTon.robotobold);
        //showtime.setTypeface(SingleTon.robotobold);
        price.setTypeface(SingleTon.robotobold);
        yourearning.setTypeface(SingleTon.robotobold);
        courier.setTypeface(SingleTon.robotobold);
        shippingmethod.setTypeface(SingleTon.robotobold);
        orderdate.setTypeface(SingleTon.robotobold);
        ordernumber.setTypeface(SingleTon.robotobold);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            MySalesPurchasesData mspd = (MySalesPurchasesData) extra.getSerializable("data");
            brandname.setText(mspd.getBrand_name() + "");
            buyername.setText(mspd.getSeller_name() + "");
            buyername.setTag(mspd.getBuyer_id());
            price.setText("$" + mspd.getPrice_now() + "");
            // shippingprice.setText("$" + mspd.getShipping_charge() + "");
            //grandtotalprice.setText("$" + mspd.getTotal_amount() + "");
            courier.setText("");
            shippingmethod.setText(mspd.getShipping_method() + "");
            orderdate.setText(TimeAgo.getCurrentDate(mspd.getOrder_date()) + "");
            ordernumber.setText(mspd.getOrder_id() + "");
            //showtime.setReferenceTime(mspd.getOrder_date());
            SingleTon.imageLoader.displayImage(mspd.getImage_url(), productimage, SingleTon.options4);
            //to calculate seller income
            try {
                double val = Double.parseDouble(mspd.getPrice_now());
                double earning = val - ((val * 20) / 100);
                yourearning.setText("$" + earning);
            } catch (Exception ex) {
                Log.e("error", ex.getMessage());
            }

        }


    }

    public void submit(View v) {

        cancelSale(ordernumber.getText().toString());
    }

    public void cancel(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        LndUtils.mysalepos = -1;
        finish();
    }

    public void back(View v) {
        onBackPressed();
    }


    private void cancelSale(final String orderid) {
        showProgress("wait cancelling Buy Request");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_SHIPPINGINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("json", response + "");
                dismissProgress();
                try {
                    JSONObject jobj = new JSONObject(response);
                    if (jobj.getBoolean("status"))
                    {
                        Intent preact = new Intent();
                        preact.putExtra("MESSAGE", "CANCELLEDSALE");
                        setResult(100, preact);
                        finish();
                    }
                    else
                        showToast(jobj.getString("message"));
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

                params.put("order_id", orderid);
                params.put("rqid", "12");
                params.put("cancel_reason", canceledittext.getText().toString());
                params.put("date_time",SingleTon.getCurrentTimeStamp());
                params.put("seller_name",SingleTon.pref.getString("uname",""));
                params.put("buyer_id",buyername.getTag().toString());




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
