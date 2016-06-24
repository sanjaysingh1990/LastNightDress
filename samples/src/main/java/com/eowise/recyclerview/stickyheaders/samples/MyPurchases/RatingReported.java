package com.eowise.recyclerview.stickyheaders.samples.MyPurchases;

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
import com.eowise.recyclerview.stickyheaders.samples.Utils.RelativeTimeTextView;
import com.eowise.recyclerview.stickyheaders.samples.Utils.TimeAgo;
import com.eowise.recyclerview.stickyheaders.samples.data.MySalesPurchasesData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RatingReported extends LndBaseActivity {

    @Bind({R.id.sellertext, R.id.pricetext, R.id.shippingtext, R.id.grandtotaltext, R.id.orderdatetext, R.id.ordernumbertext, R.id.shippingmethodtext})
    List<TextView> inprocess;
    TextView heading;
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
    @Bind(R.id.shipppingmethod)
    TextView shippingmethod;
    @Bind(R.id.ratingcomment)
    TextView ratingcomment;
    @Bind(R.id.ratingBar)
    ColoredRatingBar ratingBar;
    @Bind(R.id.timeleft)
    TextView timeleft;
    @Bind(R.id.editrating)
    TextView editrating;
    MySalesPurchasesData mspd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchases_ratingreported_layout);
        ButterKnife.bind(this);
        getReference();
    }


    public void back(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getReference() {


        //applying custom fonts
        for (int i = 0; i < inprocess.size(); i++) {
            inprocess.get(i).setTypeface(SingleTon.robotomedium);

        }
        //applying custom font
        brandname.setTypeface(SingleTon.robotobold);
        sellername.setTypeface(SingleTon.robotobold);
        showtime.setTypeface(SingleTon.robotobold);
        price.setTypeface(SingleTon.robotobold);
        shippingprice.setTypeface(SingleTon.robotobold);
        grandtotalprice.setTypeface(SingleTon.robotobold);
        orderdate.setTypeface(SingleTon.robotobold);
        ordernumber.setTypeface(SingleTon.robotobold);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            mspd = (MySalesPurchasesData) extra.getSerializable("data");
            brandname.setText(mspd.getBrand_name() + "");
            sellername.setText(mspd.getSeller_name() + "");
            price.setText("$" + mspd.getPrice_now() + "");
            shippingprice.setText("$" + mspd.getShipping_charge() + "");
            grandtotalprice.setText("$" + mspd.getTotal_amount() + "");
            shippingmethod.setText(mspd.getShipping_method() + "");
            orderdate.setText(TimeAgo.getCurrentDate(mspd.getOrder_date()) + "");
            ordernumber.setText(mspd.getOrder_id() + "");
            showtime.setReferenceTime(mspd.getOrder_date());
            SingleTon.imageLoader.displayImage(mspd.getImage_url(), productimage, SingleTon.options4);
            editRating(mspd.getOrder_id());

        }
    }

    @Override
    public void enable() {
        editrating.setClickable(true);
        editrating.setTextColor(getColorfromResource(this, R.color.lndcolor));
    }

    private void editRating(final String orderid) {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_SHIPPINGINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("json", response);
                try {

                    JSONObject jobj = new JSONObject(response);
                    if (jobj.getBoolean("status")) {
                        ratingcomment.setText(jobj.getString("review_msg"));
                        ratingBar.setRating(jobj.getInt("rated_val"));
                        int time_passed = jobj.getInt("time_left");
                        mspd.setForwhat("update");
                        mspd.setRated_val(jobj.getInt("rated_val"));
                        mspd.setReview_msg(jobj.getString("review_msg"));
                        int diff = 24 - time_passed;
                        if (diff > 0) {
                            timeleft.setText("You have " + diff + "hrs to edit your rating");
                            enable();
                        } else
                            timeleft.setText("can't edit 24 hrs passed");

                    } else {
                        Toast.makeText(RatingReported.this, "some thing went wrong", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                    Log.e("jsonerror", e.getMessage() + "");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    new com.eowise.recyclerview.stickyheaders.samples.AlertDialog().showAlertDialog(RatingReported.this);
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

    public void editRating(View v) {
        Intent rateuser = new Intent(this, RateUserActivity.class);
        rateuser.putExtra("data", mspd);
        startActivityForResult(rateuser, 203);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 203 && data != null) {
            ratingcomment.setText(data.getStringExtra("review_msg"));
            ratingBar.setRating(data.getIntExtra("rating_val", 0));
        }
    }


}
