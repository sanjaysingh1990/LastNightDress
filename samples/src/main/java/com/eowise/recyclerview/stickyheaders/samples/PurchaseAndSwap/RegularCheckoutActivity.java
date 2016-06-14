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
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.Lnd_Item_Order;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.Utils.RelativeTimeTextView;
import com.eowise.recyclerview.stickyheaders.samples.Utils.TimeAgo;
import com.google.gson.JsonParseException;


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
    @Bind(R.id.authentication)
    RadioButton authenticate;

    //end here

    //for new shipping address
    @Bind(R.id.fullname)
    EditText fullname;
    @Bind(R.id.address)
    EditText address;
    @Bind(R.id.asu)
    EditText asu;
    @Bind(R.id.country)
    EditText country;
    @Bind(R.id.province)
    EditText province;
    @Bind(R.id.city)
    EditText city;
    @Bind(R.id.zipcode)
    EditText zipcode;
    @Bind(R.id.phone)
    EditText phone;
    //for new card payment
    @Bind(R.id.cardno)
    EditText cardno;
    @Bind(R.id.cardspinner)
    Spinner cardspinner;


    private int sameaddornew = 1;
    private int samepaymentornew = 1;


 /*   private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AQROuxZHCry7zhjtDYgK2S0uq1P2XQThAEb6UEUB3ntPe7p0RW2gfiupZDlHLEAtZVHlDt9x9VHkc_fd";
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);*/

    JSONObject shipto = new JSONObject();
    Lnd_Item_Order orderdata;

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
        Bundle extra = getIntent().getExtras();
        orderdata = new Lnd_Item_Order();
        if (extra != null) {
            Home_List_Data hld = (Home_List_Data) extra.getSerializable("data");
            brandname.setText(Capitalize.capitalizeFirstLetter(hld.getBrandname()));
            sellername.setText(Capitalize.capitalize(hld.getUname()));
            showtime.setReferenceTime(hld.getTime());
            price.setText("$" + hld.getPricenow());
            orderdate.setText(TimeAgo.getCurrentDate());
            SingleTon.imageLoader.displayImage(hld.getImageurls().get(0), productimage, SingleTon.options4);
            try {
                shipto.put("post_id", hld.getPost_id());
                shipto.put("sellerid", hld.getUserid());
                shipto.put("buyerid", SingleTon.pref.getString("user_id", ""));
                 orderdata.setBrand(hld.getBrandname());
                orderdata.setPrice(hld.getPricenow());
                orderdata.setSellername(hld.getUname());
                orderdata.setShipping("4.45");
                orderdata.setTime(hld.getTime());
                orderdata.setImageurl(hld.getImageurls().get(0));


            } catch (Exception ex) {

            }
            //to create new order id

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

        getPreviousTransationInfo();
        purchaseShippingLable("{\"weight\":\"55\",\"height\":\"5\",\"toName\":\"John Doe\",\"toCode\":\"59759\",\"toState\":\"MT\",\"width\":\"25\",\"length\":\"15\",\"toCity\":\"Whitehall\",\"toPhone\":\"1231231234\",\"toCompany\":\"John Doe\",\"toAddr1\":\"111 W Legion\"}");

    }


    public void sameadd(View v) {
        sameaddornew = 1;
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

        if (this.newadd.getText().toString().compareToIgnoreCase("save") == 0) {
            if (!validateandsavenewaddress())
                return;
        }
        sameaddornew = 2;

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

    private boolean validateandsavenewaddress() {
        if (fullname.getText().length() == 0) {
            fullname.requestFocus();
            fullname.setError("Filed Empty");
            return false;
        } else {
            fullname.setError(null);
        }
        if (address.getText().length() == 0) {
            address.requestFocus();
            address.setError("Filed Empty");
            return false;
        } else
            address.setError(null);
        if (asu.getText().length() == 0) {
            asu.requestFocus();
            asu.setError("Filed Empty");
            return false;
        } else
            asu.setError(null);
        if (country.getText().length() == 0) {
            country.requestFocus();
            country.setError("Filed Empty");
            return false;
        } else
            country.setError(null);
        if (province.getText().length() == 0) {
            province.requestFocus();
            province.setError("Filed Empty");
            return false;
        } else
            province.setError(null);
        if (city.getText().length() == 0) {
            city.requestFocus();
            city.setError("Filed Empty");
            return false;
        } else
            city.setError(null);
        if (zipcode.getText().length() == 0) {
            zipcode.requestFocus();
            zipcode.setError("Filed Empty");
            return false;
        } else
            zipcode.setError(null);
        if (phone.getText().length() == 0) {
            phone.requestFocus();
            phone.setError("Filed Empty");
            return false;
        } else
            phone.setError(null);
        return true;
    }

    private boolean validateandsavenewpayment() {
        if (cardspinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "select card type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cardno.getText().length() == 0) {
            cardno.requestFocus();
            cardno.setError("Filed Empty");
            return false;
        } else
            cardno.setError(null);

        return true;
    }

    public void newpayment(View v) {

        if (newpayment.getText().toString().compareToIgnoreCase("save") == 0) {
            if (!validateandsavenewpayment()) {
                return;
            }


        }

        samepaymentornew = 2;

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

        samepaymentornew = 1;
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
        String authentication_price="0";
        if(authenticate.isChecked())
        {
            authentication_price="49.99";
        }


        if (sameaddornew == 2) {
            if (newadd.getText().toString().compareToIgnoreCase("save") == 0) {
                Toast.makeText(this, "save new address", Toast.LENGTH_SHORT).show();
                return;
            } else {
                try {
                    shipto.put("fullname", fullname.getText().toString());
                    shipto.put("address", address.getText().toString());
                    shipto.put("asu", asu.getText().toString());
                    shipto.put("country", country.getText().toString());
                    shipto.put("province", province.getText().toString());
                    shipto.put("city", city.getText().toString());
                    shipto.put("zipcode", zipcode.getText().toString());
                    shipto.put("phone", phone.getText().toString());
                    shipto.put("date_time",SingleTon.getCurrentTimeStamp());
                    shipto.put("item_authentication",authentication_price);
                    shipto.put("total_amount", grandtotalprice.getText().toString().replace("$", ""));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } else {
            if (resp.length() > 0) {
                try {

                    JSONObject jobj = new JSONObject(resp);
                    shipto.put("fullname", jobj.getString("fullname"));
                    shipto.put("address", jobj.getString("street_address"));
                    shipto.put("asu", jobj.getString("apt_suite_unit"));
                    shipto.put("country", jobj.getString("country"));
                    shipto.put("province", jobj.getString("province"));
                    shipto.put("city", jobj.getString("city"));
                    shipto.put("zipcode", jobj.getString("zipcode"));
                    shipto.put("phone", jobj.getString("phone"));
                    shipto.put("date_time",SingleTon.getCurrentTimeStamp());
                    shipto.put("item_authentication",authentication_price);
                    shipto.put("total_amount", grandtotalprice.getText().toString().replace("$", ""));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        if (samepaymentornew == 2) {
            if (newpayment.getText().toString().compareToIgnoreCase("save") == 0) {
                Toast.makeText(this, "save new payment", Toast.LENGTH_SHORT).show();
                return;
            } else {
                try {
                    shipto.put("cardtype", cardspinner.getSelectedItem().toString());
                    shipto.put("cardno", cardno.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                JSONObject jobj = new JSONObject(resp);
                shipto.put("cardtype", jobj.getString("payment_method"));
                shipto.put("cardno", jobj.getString("card_no"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            orderdata.setAddress(shipto.getString("address") + ",\n" + shipto.getString("asu") + ",\n" + shipto.getString("city") + ",\n" + shipto.getString("country")+ ",\n" + shipto.getString("zipcode"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        placeOrder();
    }

    public void learnmore(View v) {
        Intent luxurydesign = new Intent(this, LndLuxuryandDesignerAuthentication.class);
        startActivity(luxurydesign);
    }

    private void purchaseShippingLable(final String data) {
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

                        shippingprice.setText("$" + datajsonobj.getString("Charges"));
                        double val1=Double.parseDouble(orderdata.getPrice());
                        double val2=Double.parseDouble(datajsonobj.getString("Charges"));
                        grandtotalprice.setText("$" +(val1+val2 ));
                        orderdata.setShipping(val2+"");
                        orderdata.setTotal((val1+val2 )+"");
                        //JSONArray jarray = datajsonobj.getJSONArray("Packages");
                        //JSONObject jsonpackageinfo = jarray.getJSONObject(0);


                    }
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

    private String resp = "";
private int check(String[] arr,String match)
{
    for(int i=0;i<arr.length;i++)
    {
        if(arr[i].compareToIgnoreCase(match)==0) {
            return i;

        }
        }
return -1;
}
    private void getPreviousTransationInfo() {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_SHIPPINGINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("json", response + "");
                try {

                    JSONObject jobj = new JSONObject(response);
                    if (jobj.getBoolean("status")) {
                        resp = response;
                        String address = jobj.getString("street_address") + "\n" + jobj.getString("apt_suite_unit") + "\n" + jobj.getString("country")
                                + "\n" + jobj.getString("city") + "\n" + jobj.getString("zipcode");
                        sameaddress.setText(address);
                        cardno.setText(jobj.getString("card_no"));
                     int pos=   check(getResources().getStringArray(R.array.card_type),jobj.getString("payment_method"));
                         if(pos>=0)
                        cardspinner.setSelection(pos);
                    } else {
                        newadd(null);
                        newpayment(null);
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

                params.put("rqid", "3");
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
        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);

        queue.add(sr);

    }

    public void back(View v) {
        onBackPressed();
    }


    private void placeOrder() {
        showProgress("wait processing");

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_SHIPPINGINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("json", response + "");
                dismissProgress();
                try {
                   orderdata.setPaymentmethod(cardspinner.getSelectedItem().toString());
                    JSONObject jobj = new JSONObject(response);
                    if (jobj.getBoolean("status")) {
                        orderdata.setOrderdate(TimeAgo.getCurrentDate());
                        orderdata.setOrderid(jobj.getString("orderid"));
                        orderdata.setPaymentmethod(cardspinner.getSelectedItem().toString());
                        Intent checkoutfinishh = new Intent(RegularCheckoutActivity.this, RegularCheckoutFinishActivity.class);
                        checkoutfinishh.putExtra("data", orderdata);
                        checkoutfinishh.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(checkoutfinishh);
                        finish();
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

                params.put("data", shipto.toString());
                params.put("rqid", "4");

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
