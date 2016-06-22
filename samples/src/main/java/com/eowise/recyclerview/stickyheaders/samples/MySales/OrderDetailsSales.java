package com.eowise.recyclerview.stickyheaders.samples.MySales;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.Utils.RelativeTimeTextView;
import com.eowise.recyclerview.stickyheaders.samples.Utils.TimeAgo;
import com.eowise.recyclerview.stickyheaders.samples.data.MySalesPurchasesData;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderDetailsSales extends LndBaseActivity {

    TextView shippinglabel;

    @Bind({R.id.buyertext, R.id.pricetext, R.id.yourearningtext, R.id.couriertext, R.id.orderdatetext, R.id.ordernumbertext, R.id.shippingmethodtext})
    List<TextView> inprocess;
    @Bind(R.id.heading)
    TextView heading;
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
    private TextView couriercompany, trackingnumber;
    Bundle extra;

    private void updateKeyboardStatusText(boolean isOpen) {
        // Toast.makeText(this,String.format("keyboard is %s", (isOpen ? "visible" : "hidden")),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String status = extra.getString("type");

            if (status.compareToIgnoreCase("in process") == 0) {
                setContentView(R.layout.sales_inprocess_layout);
                ButterKnife.bind(this);
                getReference();
                KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        updateKeyboardStatusText(isOpen);
                    }
                });

                updateKeyboardStatusText(KeyboardVisibilityEvent.isKeyboardVisible(this));
            } else if (status.compareToIgnoreCase("Shipped") == 0) {
                setContentView(R.layout.sales_item_shipped_layout);
                ButterKnife.bind(this);
                getReference();

            } else if (status.compareToIgnoreCase("delivered") == 0) {
                setContentView(R.layout.sales_item_delivered_layout);
                ButterKnife.bind(this);
                getReference();

            } else if (status.compareToIgnoreCase("cancelled1") == 0) {
                setContentView(R.layout.sales_cancelled_layout);
                ButterKnife.bind(this);
                getReference();

            } else if (status.compareToIgnoreCase("cancelled2") == 0) {
                setContentView(R.layout.sales_item_cancelled_layout);
                ButterKnife.bind(this);
                getReference();
                TextView cancelled = (TextView) findViewById(R.id.cancelled);
                //appying spannable
                String text = cancelled.getText().toString() + "    ";
                SpannableStringBuilder ssb = new SpannableStringBuilder(text);
                Bitmap smiley = BitmapFactory.decodeResource(getResources(), R.drawable.shippinglabel_icon);
                ssb.setSpan(new ImageSpan(smiley), text.length() - 3, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                cancelled.setText(ssb, TextView.BufferType.SPANNABLE);

            } else if (status.compareToIgnoreCase("claim processing") == 0) {
                setContentView(R.layout.sales_claimprocessing_layout);

            } else if (status.compareToIgnoreCase("claim declined") == 0) {
                setContentView(R.layout.sales_claimdeclined_layout);

            } else if (status.compareToIgnoreCase("Report rating") == 0) {

                setContentView(R.layout.sales_report_rating_layout);
                ButterKnife.bind(this);
                getReference();

                //report rating
                TextView reportrating = (TextView) findViewById(R.id.reportrating);
                reportrating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle extra = getIntent().getExtras();
                        if (extra != null) {
                            MySalesPurchasesData mspd = (MySalesPurchasesData) extra.getSerializable("data");
                            Intent reportrating = new Intent(OrderDetailsSales.this, MySalesReportRatingActivity.class);
                            reportrating.putExtra("data", mspd);
                            startActivity(reportrating);
                        }
                    }
                });
            }

        }
        heading = (TextView) findViewById(R.id.heading);
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
        shippingtest.putExtra("orderid",ordernumber.getText().toString());
        startActivityForResult(shippingtest, 2);
    }

    private void getReference() {

        couriercompany = (TextView) findViewById(R.id.couriercompany);
        trackingnumber = (TextView) findViewById(R.id.edittexttrackingno);
        shippinglabel = (TextView) findViewById(R.id.shippingstatus);


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
        extra = getIntent().getExtras();
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
            try {
                double val = Double.parseDouble(mspd.getPrice_now());
                double earning = val - ((val * 20) / 100);
                yourearning.setText("$" + earning);
            } catch (Exception ex) {
                Log.e("error", ex.getMessage());
            }

        }


    }

    private void getReference2() {


        //applying custom fonts
        inprocess.get(0).setTypeface(SingleTon.robotobold);
        inprocess.get(1).setTypeface(SingleTon.robotoregular);
        for (int i = 2; i < inprocess.size(); i++) {
            inprocess.get(i).setTypeface(SingleTon.robotomedium);

        }
        final TextView submit = (TextView) findViewById(R.id.submit);
        final TextView cancel = (TextView) findViewById(R.id.cancel);

        EditText yourreason = (EditText) findViewById(R.id.cancelinfoeditext);
        //setting listener
        yourreason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    submit.setTextColor(Color.parseColor("#be4d66"));
                    submit.setClickable(true);

                } else {
                    submit.setTextColor(Color.parseColor("#e8e9eb"));
                    submit.setClickable(false);
                }
            }
        });

        //submit click
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }


        });
        //submit click
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.sales_inprocess_layout);
                ButterKnife.bind(OrderDetailsSales.this);
                getReference();

            }


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && data != null) {


            String postids = data.getStringExtra("shipprice");

            Log.e("data", postids);
            String text = shippinglabel.getText().toString() + "    ";
            SpannableStringBuilder ssb = new SpannableStringBuilder(text);
            Bitmap smiley = BitmapFactory.decodeResource(getResources(), R.drawable.shippinglabel_icon);
            ssb.setSpan(new ImageSpan(smiley), text.length() - 3, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            shippinglabel.setText(ssb, TextView.BufferType.SPANNABLE);

            shippinglabel.setVisibility(View.VISIBLE);
            trackingnumber.setText(data.getStringExtra("trackingno") + "");
            couriercompany.setText(data.getStringExtra("couriercompany") + "");

        } else if (requestCode == 100 && data != null) {
            Intent preact = new Intent();
            preact.putExtra("MESSAGE", "TASKDONE");
            setResult(200, preact);
            finish();
        }
    }

    public void cancleOrder(View v) {
      /*  setContentView(R.layout.sales_cancelling_layout);
        ButterKnife.bind(this);
        getReference2();*/
        if (extra != null) {
            MySalesPurchasesData mspd = (MySalesPurchasesData) extra.getSerializable("data");

            Intent cancelorder = new Intent(this, Cancel_Sale_Activity.class);
            //  cancelorder.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            cancelorder.putExtra("data", mspd);
            startActivityForResult(cancelorder, 100);
        }
    }

    public void check() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.lnd_trackingnumber_error_dialog
                , null);

        dialog.setView(view);
        final AlertDialog alert = dialog.create();
        alert.show();
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

    }

    public void itemshipped(View v) {
        if (couriercompany.getText().length() == 0) {
            couriercompany.requestFocus();
            couriercompany.setError("field empty");
            check();
            return;
        } else {
            couriercompany.setError(null);
        }
        if (trackingnumber.getText().length() == 0) {
            trackingnumber.requestFocus();
            trackingnumber.setError("field empty");
            check();
            return;
        } else {
            trackingnumber.setError(null);
        }
        shippedItem(ordernumber.getText().toString());
    }

    private void shippedItem(final String orderid) {
        showProgress("wait Shipping Item");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_SHIPPINGINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("json", response + "");
                dismissProgress();
                try {
                    JSONObject jobj = new JSONObject(response);
                    if (jobj.getBoolean("status")) {
                        Intent preact = new Intent();
                        preact.putExtra("MESSAGE", "ITEMSHIPPED");
                        setResult(200, preact);
                        finish();
                    } else
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
                params.put("rqid", "13");
                params.put("date_time", SingleTon.getCurrentTimeStamp());
                params.put("courier_type", trackingnumber.getText().toString());


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
