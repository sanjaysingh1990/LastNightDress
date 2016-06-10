package com.eowise.recyclerview.stickyheaders.samples.PurchaseAndSwap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.eowise.recyclerview.stickyheaders.samples.LndMore.LndLuxuryandDesignerAuthentication;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.Home_List_Data;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.Utils.RelativeTimeTextView;
import com.eowise.recyclerview.stickyheaders.samples.Utils.TimeAgo;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegularCheckoutActivity extends LndBaseActivity {
    @Bind(R.id.sameadd)
    TextView sameadd;
    @Bind(R.id.newadd)
    TextView newadd;
    @Bind(R.id.heading)

    TextView heading;
    @Bind(R.id.newaddressblock)
    LinearLayout newaddress;
    @Bind(R.id.sameaddressblock)
    TextView sameaddress;
    @Bind(R.id.samepayment)
    TextView samepayment;
    @Bind(R.id.newpayment)
    TextView newpayment;
    @Bind(R.id.cardno)
    EditText cardno;
    @Bind({R.id.brandname, R.id.sellername, R.id.sellertext, R.id.pricetext, R.id.shippingtext, R.id.grandtotaltext, R.id.orderdatetext, R.id.ordernumbertext})
    List<TextView> regularcheckout;
    @Bind(R.id.sameaddrellayout)
    RelativeLayout sameaddrellayout;
    @Bind(R.id.newaddrellayout)
    RelativeLayout newaddrellayout;

    @Bind(R.id.samepayrellayout)
    RelativeLayout samepayrellayout;
    @Bind(R.id.newpayrellayout)
    RelativeLayout newpayrellayout;
    //refrence for post

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
    //end here

 /*   private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AQROuxZHCry7zhjtDYgK2S0uq1P2XQThAEb6UEUB3ntPe7p0RW2gfiupZDlHLEAtZVHlDt9x9VHkc_fd";
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_checkout);
        ButterKnife.bind(this);
       /* Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);*/
//applying custom fonts
        regularcheckout.get(0).setTypeface(SingleTon.robotobold);
        regularcheckout.get(1).setTypeface(SingleTon.robotoregular);
        for (int i = 2; i < regularcheckout.size(); i++) {
            regularcheckout.get(i).setTypeface(SingleTon.robotomedium);

        }
        Bundle extra=getIntent().getExtras();
        if(extra!=null)
        {
            Home_List_Data hld= (Home_List_Data) extra.getSerializable("data");
            brandname.setText(Capitalize.capitalizeFirstLetter(hld.getBrandname()));
            sellername.setText(Capitalize.capitalize(hld.getUname()));
            showtime.setReferenceTime(hld.getTime());
            price.setText("$"+hld.getPricenow());
            orderdate.setText(TimeAgo.getCurrentDate());
            SingleTon.imageLoader.displayImage(hld.getImageurls().get(0),productimage,SingleTon.options4);

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


    }


    public void sameadd(View v) {
        this.sameaddrellayout.setBackgroundColor(Color.parseColor("#be4d66"));
        this.newaddrellayout.setBackgroundColor(Color.parseColor("#dbdbdb"));

        this.sameadd.setBackgroundColor(Color.parseColor("#be4d66"));
        this.sameadd.setTextColor(Color.parseColor("#ffffff"));
        this.sameadd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.payment_selection, 0, 0, 0);
        this.newadd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        this.newadd.setBackgroundResource(R.drawable.purchse_rounded_corners);
        this.newadd.setTextColor(Color.parseColor("#be4d66"));
        this.newadd.setBackgroundColor(Color.parseColor("#dbdbdb"));
        newaddress.setVisibility(View.GONE);
        sameaddress.setVisibility(View.VISIBLE);
        if (sameadd.getText().toString().compareToIgnoreCase("cancel") == 0) {

            this.newadd.setText("New Address");
            this.sameadd.setText("Same Address");
        }

    }

    public void newadd(View v) {
        this.newaddrellayout.setBackgroundColor(Color.parseColor("#be4d66"));
        this.sameaddrellayout.setBackgroundColor(Color.parseColor("#dbdbdb"));


        this.newadd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.payment_selection, 0, 0, 0);
        this.sameadd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        this.newadd.setBackgroundColor(Color.parseColor("#be4d66"));
        this.newadd.setTextColor(Color.parseColor("#ffffff"));
        this.sameadd.setBackgroundResource(R.drawable.purchse_rounded_corners);
        this.sameadd.setTextColor(Color.parseColor("#be4d66"));
        this.sameadd.setBackgroundColor(Color.parseColor("#dbdbdb"));
        newaddress.setVisibility(View.VISIBLE);
        sameaddress.setVisibility(View.GONE);
        if (newadd.getText().toString().compareToIgnoreCase("new address") == 0) {
            this.newadd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            this.newadd.setText("Save");
            this.sameadd.setText("Cancel");
        } else {
            this.newadd.setText("New Address");
            this.sameadd.setText("Same Address");
            newaddress.setVisibility(View.GONE);
            sameaddress.setVisibility(View.VISIBLE);
            this.newadd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.payment_selection, 0, 0, 0);

        }
    }

    public void newpayment(View v) {
        this.newpayrellayout.setBackgroundColor(Color.parseColor("#be4d66"));
        this.samepayrellayout.setBackgroundColor(Color.parseColor("#dbdbdb"));


        this.newpayment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.payment_selection, 0, 0, 0);
        this.samepayment
                .setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        this.newpayment.setBackgroundColor(Color.parseColor("#be4d66"));
        this.newpayment.setTextColor(Color.parseColor("#ffffff"));
        this.samepayment.setBackgroundResource(R.drawable.purchse_rounded_corners);
        this.samepayment.setTextColor(Color.parseColor("#be4d66"));
        this.samepayment.setBackgroundColor(Color.parseColor("#dbdbdb"));
        this.cardno.setEnabled(true);
        if (newpayment.getText().toString().compareToIgnoreCase("new payment") == 0) {
            this.newpayment.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            this.newpayment.setText("Save");
            this.samepayment.setText("Cancel");
        } else {
            this.newpayment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.payment_selection, 0, 0, 0);
            this.newpayment.setText("New Payment");
            this.samepayment.setText("Same Payment");

        }
    }

    public void samepayment(View v) {
        this.samepayrellayout.setBackgroundColor(Color.parseColor("#be4d66"));
        this.newpayrellayout.setBackgroundColor(Color.parseColor("#dbdbdb"));


        this.samepayment.setBackgroundColor(Color.parseColor("#be4d66"));
        this.samepayment.setTextColor(Color.parseColor("#ffffff"));
        this.samepayment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.payment_selection, 0, 0, 0);
        this.newpayment.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        this.newpayment.setBackgroundResource(R.drawable.purchse_rounded_corners);
        this.newpayment.setTextColor(Color.parseColor("#be4d66"));
        this.newpayment.setBackgroundColor(Color.parseColor("#dbdbdb"));
        this.cardno.setEnabled(false);
        if (samepayment.getText().toString().compareToIgnoreCase("cancel") == 0) {
            this.newpayment.setText("New Payment");
            this.samepayment.setText("Same Payment");
        }

    }

    public void close(View v) {
        finish();
    }

    public void done(View v) {
        //onBuyPressed();
    }

  /*  public void onBuyPressed() {
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_ORDER);
        Intent intent = new Intent(RegularCheckoutActivity.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);


        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal("1.75"), "USD", "bisht.sanjaysingh97@gmail.com",
                paymentIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.e("Show", confirm.toJSONObject().toString(4));
                        Log.e("Show", confirm.getPayment().toJSONObject().toString(4));
                        showAlert(confirm.getPayment().toJSONObject().toString(4));

                        //   *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification

                        Toast.makeText(getApplicationContext(), "PaymentConfirmation info received" +
                                " from PayPal", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "an extremely unlikely failure" +
                                " occurred:", Toast.LENGTH_LONG).show();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "The user canceled.", Toast.LENGTH_LONG).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(getApplicationContext(), "An invalid Payment or PayPalConfiguration" +
                        " was submitted. Please see the docs.", Toast.LENGTH_LONG).show();
            }
        }
    }
*/
    @Override
    public void onDestroy() {
        // Stop service when done
       // stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    public void showAlert(String paymentdetatils) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(RegularCheckoutActivity.this);
        View view = LayoutInflater.from(RegularCheckoutActivity.this).inflate(R.layout.paypal_payment_dialog_layout, null);
        TextView alertmsg = (TextView) view.findViewById(R.id.alertmessage);
        alertmsg.setText(paymentdetatils);
        dialog.setView(view);
        final AlertDialog alert = dialog.create();
        alert.show();

        view.findViewById(R.id.alertcontinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                finish();

            }
        });
    }

    public void completePurchase(View v) {
        Intent checkoutfinishh = new Intent(this, RegularCheckoutFinishActivity.class);
        startActivity(checkoutfinishh);
        finish();
    }

    public void learnmore(View v) {
        Intent luxurydesign = new Intent(this, LndLuxuryandDesignerAuthentication.class);
        startActivity(luxurydesign);
    }

    private void PurchaseShippingLable(final String data) {
        showProgress("wait processing");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_FADEX_PURCHASE_SHIPPING_LABLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("json", response + "");
                dismissProgress();
                try
                {
                    JSONObject jobj=new JSONObject(response);
                    JSONObject metajsonobj=jobj.getJSONObject("Meta");
                    int responsecode=metajsonobj.getInt("Code");
                    if(responsecode==200) {
                        JSONObject datajsonobj = jobj.getJSONObject("Data");
                        //shippinglable.setText("$" + datajsonobj.getString("Charges"));
                      //  total.setText("$" + datajsonobj.getString("Charges"));

                        JSONArray jarray = datajsonobj.getJSONArray("Packages");
                        JSONObject jsonpackageinfo=jarray.getJSONObject(0);



                    }
                }
                catch(Exception ex)
                {
                    Log.e("jsonerror",ex.getMessage()+"");
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
    private void getPostInfo(final String data) {
        showProgress("wait processing");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_SHIPPINGINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("json", response + "");
                dismissProgress();
                try
                {



                }
                catch(Exception ex)
                {
                    Log.e("jsonerror",ex.getMessage()+"");
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
