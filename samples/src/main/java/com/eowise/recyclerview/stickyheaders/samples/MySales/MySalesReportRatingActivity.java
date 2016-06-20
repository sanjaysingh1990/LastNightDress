package com.eowise.recyclerview.stickyheaders.samples.MySales;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.eowise.recyclerview.stickyheaders.samples.Utils.TimeAgo;
import com.eowise.recyclerview.stickyheaders.samples.data.MySalesPurchasesData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MySalesReportRatingActivity extends LndBaseActivity implements View.OnClickListener {
    @Bind(R.id.submit)
    TextView submit;
    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.cancelinfoeditext)
    EditText cancelinfo;
    @Bind(R.id.radiobutton1)
    RadioButton radiobtn1;
    @Bind(R.id.radiobutton2)
    RadioButton radiobtn2;
    @Bind(R.id.brandname)
    TextView brandname;
    @Bind(R.id.buyername)
    TextView buyername;
    /* @Bind(R.id.showtime)
     RelativeTimeTextView showtime;*/
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.yourearning)
    TextView yourearning;
    @Bind(R.id.courier)
    TextView courier;
    /* @Bind(R.id.shippingprice)
    TextView shippingprice;
    @Bind(R.id.grandtotalprice)
    TextView grandtotalprice;*/
    @Bind(R.id.orderdate)
    TextView orderdate;
    @Bind(R.id.ordernumber)
    TextView ordernumber;
    @Bind(R.id.productimage)
    ImageView productimage;
    @Bind(R.id.shipppingmethod)
    TextView shippingmethod;
    @Bind({R.id.buyertext, R.id.pricetext, R.id.yourearningtext, R.id.couriertext, R.id.orderdatetext, R.id.ordernumbertext, R.id.shippingmethodtext})
    List<TextView> inprocess;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saller_reporting_rating_layout);
        ButterKnife.bind(this);
        heading.setTypeface(SingleTon.robotobold);
        submit.setOnClickListener(this);
        radiobtn1.setOnClickListener(this);
        radiobtn2.setOnClickListener(this);
        submit.setClickable(false);
        cancelinfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (radiobtn1.isChecked() || radiobtn2.isChecked())
                    enable();
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
            price.setText("$" + mspd.getPrice_now() + "");
            submit.setTag(mspd.getOrder_id());
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

    public void back(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void enable() {
        submit.setClickable(true);
        submit.setTextColor(ContextCompat.getColor(this, R.color.lndcolor));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:

                submitReportRating(submit.getTag().toString());
                break;
            case R.id.radiobutton1:
                if (cancelinfo.getText().length() > 0)
                    enable();
                break;

            case R.id.radiobutton2:
                enable();
                break;
        }
    }

    public void submitReportRating(final String orderid) {


        showProgress("Please wait...");

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_SHIPPINGINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgress();
                Log.e("json", response.toString());
                try {
                    JSONObject jobj = new JSONObject(response);
                    if (jobj.getBoolean("status")) {
                        finish();//finishing activity

                    } else {
                        Toast.makeText(MySalesReportRatingActivity.this, jobj.getString("message") + "", Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException ex) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgress();
                //   Log.e("response error", error.getMessage() + "");

                Toast.makeText(MySalesReportRatingActivity.this, "some problem occured", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String seloption = "";
                if (radiobtn1.isChecked()) {
                    seloption = radiobtn1.getText().toString();
                }
                else
                seloption=radiobtn2.getText().toString();

                Map<String, String> params = new HashMap<String, String>();
                params.put("order_id", orderid);
                params.put("option_selected",seloption);
                params.put("cancel_reason",cancelinfo.getText().toString());
                params.put("rqid", "11");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return super.getBody();
            }
        };
        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);

        queue.add(sr);
    }
}
