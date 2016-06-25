package com.eowise.recyclerview.stickyheaders.samples.MySales;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.LndBaseActivity;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ColoredRatingBar;
import com.eowise.recyclerview.stickyheaders.samples.Utils.TimeAgo;
import com.eowise.recyclerview.stickyheaders.samples.data.MySalesPurchasesData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReportRating extends LndBaseActivity {

    TextView shippinglabel;

    @Bind({R.id.buyertext, R.id.pricetext, R.id.yourearningtext, R.id.couriertext, R.id.orderdatetext, R.id.ordernumbertext, R.id.shippingmethodtext})
    List<TextView> inprocess;

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
    @Bind(R.id.reportrating)
    TextView reportrating;
    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.reviewmessage)
    TextView reviewmessage;
    @Bind(R.id.ratingBar)
    ColoredRatingBar ratingBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sales_report_rating_layout);
        ButterKnife.bind(this);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            getReference();

            reportrating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle extra = getIntent().getExtras();
                    if (extra != null) {
                        MySalesPurchasesData mspd = (MySalesPurchasesData) extra.getSerializable("data");
                        Intent reportrating = new Intent(ReportRating.this, MySalesReportRatingActivity.class);
                        reportrating.putExtra("data", mspd);
                        startActivity(reportrating);
                    }
                }
            });
        }


        //applying fonts
        heading.setTypeface(SingleTon.robotobold);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void back(View v) {
        onBackPressed();
    }

    public void shipping(View v) {
        Intent shippingtest = new Intent(this, ShippingLabelActivity.class);
        shippingtest.putExtra("orderid", ordernumber.getText().toString());
        startActivityForResult(shippingtest, 2);
    }

    private void getReference() {


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
            // shippingprice.setText("$" + mspd.getShipping_charge() + "");
            //grandtotalprice.setText("$" + mspd.getTotal_amount() + "");
            courier.setText("");
            shippingmethod.setText(mspd.getShipping_method() + "");
            orderdate.setText(TimeAgo.getCurrentDate(mspd.getOrder_date()) + "");
            ordernumber.setText(mspd.getOrder_id() + "");
            //showtime.setReferenceTime(mspd.getOrder_date());
            SingleTon.imageLoader.displayImage(mspd.getImage_url(), productimage, SingleTon.options4);
            //to calculate seller income
            getRating(mspd.getOrder_id());
            try {
                double val = Double.parseDouble(mspd.getPrice_now());
                double earning = val - ((val * 20) / 100);
                yourearning.setText("$" + earning);
            } catch (Exception ex) {
                Log.e("error", ex.getMessage());
            }

        }


    }

    private void getRating(final String orderid) {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_SHIPPINGINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("json", response);
                try {

                    JSONObject jobj = new JSONObject(response);
                    if (jobj.getBoolean("status")) {
                        reviewmessage.setText(jobj.getString("review_msg"));
                        ratingBar.setRating(jobj.getInt("rated_val"));

                    }

                } catch (JSONException e) {

                    Log.e("jsonerror", e.getMessage() + "");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    new com.eowise.recyclerview.stickyheaders.samples.AlertDialog().showAlertDialog(ReportRating.this);
                } catch (Exception ex) {

                }
                Log.e("response", error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "16");
                params.put("order_id", orderid);
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
