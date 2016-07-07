package com.eowise.recyclerview.stickyheaders.samples.PurchaseAndSwap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.eowise.recyclerview.stickyheaders.samples.LndSwapRequestResponse.SendSwapRequestActivity;
import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.Lnd_Item_Order;
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

public class SwapCheckOutActivity extends LndBaseActivity {
    @Bind(R.id.sameadd)
    TextView sameadd;
    @Bind(R.id.newadd)
    TextView newadd;
    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.newaddressblock)
    LinearLayout newaddress;
    @Bind(R.id.sameaddressblock)
    TextView sameaddress;
    @Bind(R.id.samepayment)
    TextView samepayment;
    @Bind(R.id.newpayment)
    TextView newpayment;


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
    @Bind(R.id.complete)
    TextView completetransaction;
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
    @Bind({R.id.sellertext, R.id.pricetext, R.id.shippingtext, R.id.grandtotaltext, R.id.orderdatetext, R.id.ordernumbertext})
    List<TextView> regularcheckout;
    JSONObject shipto = new JSONObject();
    Lnd_Item_Order orderdata;

    private int sameaddornew = 1;
    private int samepaymentornew = 1;
    private String resp = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_checkout);
        ButterKnife.bind(this);
        //setup font
        heading.setTypeface(SingleTon.robotobold);


        Spannable word = new SpannableString(getResources().getString(R.string.swap_protection));

        word.setSpan(new ForegroundColorSpan(Color.BLACK), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        text.setText(word);
        Spannable wordTwo = new SpannableString(" Click to Learn more.");

        wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#30beff")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //text.append(wordTwo);

        wordTwo.setSpan(new MyClickableSpan(wordTwo.toString()), 0,
                wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        text.append(wordTwo);
        text.setMovementMethod(LinkMovementMethod.getInstance());
        text.setHighlightColor(Color.TRANSPARENT);

        orderdata = new Lnd_Item_Order();

        //applying custom font
        brandname.setTypeface(SingleTon.robotobold);
        sellername.setTypeface(SingleTon.robotoregular);

        for (int i = 0; i < regularcheckout.size(); i++) {
            regularcheckout.get(i).setTypeface(SingleTon.robotomedium);

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
        completetransaction.setClickable(false);
        completetransaction.setBackgroundColor(Color.parseColor("#dadada"));
        completetransaction.setTextColor(Color.parseColor("#757575"));
        fullDialogLoader();
    }

    private void getInfo() {
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            try {
                shipto.put("post_id", extra.getString("post_id"));
                shipto.put("sellerid", extra.getString("seller_id"));
                shipto.put("swap_order_id", extra.getString("swap_order_id"));
                getData(extra.getString("post_id"), extra.getString("seller_id"));
            } catch (JSONException ex) {

            }

        }
    }

    /*
public void pickup(View v)
{
pickup.setBackgroundColor(Color.parseColor("#be4d66"));
pickup.setTextColor(Color.parseColor("#ffffff"));
delivery.setBackgroundResource(R.drawable.purchse_rounded_corners);
delivery.setTextColor(Color.parseColor("#dbdbdb"));
}
  public void delivery(View v)
  {
      delivery.setBackgroundColor(Color.parseColor("#be4d66"));
      delivery.setTextColor(Color.parseColor("#ffffff"));
      pickup.setBackgroundResource(R.drawable.purchse_rounded_corners);
      pickup.setTextColor(Color.parseColor("#dbdbdb"));
  }*/
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

    public void newPayment(View v) {

        //onBuyPressed();

        String authentication_price = "0";
        if (authenticate.isChecked()) {
            authentication_price = "49.99";
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
                    shipto.put("date_time", SingleTon.getCurrentTimeStamp());
                    shipto.put("item_authentication", authentication_price);
                    shipto.put("total_amount", grandtotalprice.getText().toString().replace("$", ""));
                    shipto.put("buyerid", SingleTon.pref.getString("user_id", ""));
                    shipto.put("orderid", ordernumber.getText().toString());
                    shipto.put("price", price.getText().toString().replace("$", ""));
                    shipto.put("shipping_price", shippingprice.getText().toString().replace("$", ""));


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
                    shipto.put("date_time", SingleTon.getCurrentTimeStamp());
                    shipto.put("item_authentication", authentication_price);
                    shipto.put("total_amount", grandtotalprice.getText().toString().replace("$", ""));
                    shipto.put("buyerid", SingleTon.pref.getString("user_id", ""));
                    shipto.put("orderid", ordernumber.getText().toString());
                    shipto.put("price", price.getText().toString().replace("$", ""));
                    shipto.put("shipping_price", shippingprice.getText().toString().replace("$", ""));

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
            orderdata.setAddress(shipto.getString("address") + ",\n" + shipto.getString("asu") + ",\n" + shipto.getString("city") + ",\n" + shipto.getString("country") + ",\n" + shipto.getString("zipcode"));
            JSONObject jobj = new JSONObject(resp);
            shipto.put("order_no", jobj.getString("order_no"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("json", shipto.toString());
        Log.e("json1", resp);
        placeOrder();
    }


    public void showAlert(String paymentdetatils) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(SwapCheckOutActivity.this);
        View view = LayoutInflater.from(SwapCheckOutActivity.this).inflate(R.layout.paypal_payment_dialog_layout, null);
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

    class MyClickableSpan extends ClickableSpan {

        public MyClickableSpan(String string) {
            super();
        }

        public void onClick(View tv) {
            showDialog();
        }

        public void updateDrawState(TextPaint ds) {

            ds.setColor(Color.parseColor("#30beff"));

            ds.setUnderlineText(false); // set to false to remove underline
        }
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.swap_protection_more_dialog, null);
        dialog.setView(view);
        final AlertDialog alert = dialog.create();
        alert.show();
        //cancel dialog
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        //taking reference and apply font
        TextView heading = (TextView) view.findViewById(R.id.heading);
        TextView heading1 = (TextView) view.findViewById(R.id.heading1);
        TextView heading2 = (TextView) view.findViewById(R.id.heading2);
        TextView text1 = (TextView) view.findViewById(R.id.text1);
        TextView text2 = (TextView) view.findViewById(R.id.text2);
        heading.setTypeface(SingleTon.robotobold);
        heading1.setTypeface(SingleTon.robotoregular);
        heading2.setTypeface(SingleTon.robotoregular);
        text1.setTypeface(SingleTon.robotoregular);
        text2.setTypeface(SingleTon.robotoregular);


    }

    public void learnmore(View v) {
        Intent luxurydesign = new Intent(this, LndLuxuryandDesignerAuthentication.class);
        startActivity(luxurydesign);
    }

    public void doPayment(View v) {
        setContentView(R.layout.lnd_purchase_newshipping_level);
    }

    public void back(View v) {
        finish();
    }

    private void getData(final String post_id, final String sellerid) {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_SHIPPINGINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("json", response + "");
                try {

                    JSONObject jobj = new JSONObject(response);
                    JSONObject shipto = jobj.getJSONObject("shipto");
                    JSONObject postinfo = jobj.getJSONObject("post_info");
                    resp = shipto.toString();
                    if (shipto.getBoolean("status")) {

                        String address = shipto.getString("street_address") + "\n" + shipto.getString("apt_suite_unit") + "\n" + shipto.getString("country")
                                + "\n" + shipto.getString("city") + "\n" + shipto.getString("zipcode");
                        sameaddress.setText(address);
                        cardno.setText(shipto.getString("card_no"));
                        int pos = check(getResources().getStringArray(R.array.card_type), shipto.getString("payment_method"));
                        if (pos >= 0)
                            cardspinner.setSelection(pos);
                    } else {
                        newadd(null);
                        newpayment(null);
                    }

                    //post info
                    if (postinfo.getBoolean("status")) {
                        sellername.setText(Capitalize.capitalize(postinfo.getString("swap_seller_name")));
                        brandname.setText(Capitalize.capitalizeFirstLetter(postinfo.getString("brand_name")));
                        showtime.setReferenceTime(TimeAgo.getMilliseconds(postinfo.getString("date_time")));
                        price.setText("$" + postinfo.getString("price_now"));
                        orderdate.setText(TimeAgo.getCurrentDate());
                        ordernumber.setText(postinfo.getString("order_id"));
                        SingleTon.imageLoader.displayImage(postinfo.getString("image_url"), productimage, SingleTon.options4);
                        //saving values for next activiyt
                        orderdata.setBrand(Capitalize.capitalizeFirstLetter(postinfo.getString("brand_name")));
                        orderdata.setPrice(postinfo.getString("price_now"));
                        orderdata.setSellername(Capitalize.capitalize(postinfo.getString("swap_seller_name")));
                        orderdata.setTime(TimeAgo.getMilliseconds(postinfo.getString("date_time")));
                        orderdata.setImageurl(postinfo.getString("image_url"));
                    }
                    purchaseShippingLable("{\"weight\":\"55\",\"height\":\"5\",\"toName\":\"John Doe\",\"toCode\":\"59759\",\"toState\":\"MT\",\"width\":\"25\",\"length\":\"15\",\"toCity\":\"Whitehall\",\"toPhone\":\"1231231234\",\"toCompany\":\"John Doe\",\"toAddr1\":\"111 W Legion\"}");

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

                params.put("rqid", "7");
                params.put("user_id", SingleTon.pref.getString("user_id", ""));
                params.put("post_id", post_id);
                params.put("date_time", SingleTon.getCurrentTimeStamp());


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


    private int check(String[] arr, String match) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].compareToIgnoreCase(match) == 0) {
                return i;

            }
        }
        return -1;
    }

    private void purchaseShippingLable(final String data) {
        //   showProgress("wait processing");
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
                        double val1 = Double.parseDouble(price.getText().toString().replace("$", ""));
                        double val2 = Double.parseDouble(datajsonobj.getString("Charges"));
                        grandtotalprice.setText("$" + (val1 + val2));
                        orderdata.setShipping(val2 + "");
                        orderdata.setTotal((val1 + val2) + "");
                        //JSONArray jarray = datajsonobj.getJSONArray("Packages");
                        //JSONObject jsonpackageinfo = jarray.getJSONObject(0);
                        //active transaction complet button
                        completetransaction.setClickable(true);
                        completetransaction.setBackgroundColor(Color.parseColor("#be4d66"));
                        completetransaction.setTextColor(Color.parseColor("#ffffff"));

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

    Dialog dialog;

    private void fullDialogLoader() {
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.swap_status_check_layout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            try {

                checkBeforeDone(extra.getString("swap_order_id"));
            } catch (Exception ex) {

            }

        }
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
                        Intent swapstepone = new Intent(SwapCheckOutActivity.this, Swap_Checkout_Step__First_Activity.class);
                        swapstepone.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        JSONObject data = new JSONObject();
                        data.put("status", true);
                        data.put("image_url", orderdata.getImageurl());
                        data.put("brand_name", orderdata.getBrand());
                        data.put("order_id", orderdata.getOrderid());
                        data.put("order_date", orderdata.getOrderdate());
                        data.put("total_amount", orderdata.getTotal());
                        data.put("price", orderdata.getPrice());
                        data.put("uname", orderdata.getSellername());
                        data.put("shipping_price", orderdata.getShipping());

                        swapstepone.putExtra("data", data.toString());
                        startActivity(swapstepone);
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
                params.put("rqid", "8");

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

    private void checkBeforeDone(final String swaporderid) {
        showProgress("loading");
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
                        String orderid1 = jobj.getString("order_id1");
                        String orderid2 = jobj.getString("order_id2");
                        if (orderid1.length() > 0 && orderid2.length() > 0) {
                            Intent swapstepone = new Intent(SwapCheckOutActivity.this, Swap_Checkout_Final_Step_Activity.class);
                            swapstepone.putExtra("data", response);
                            Main_TabHost.activity.startActivityForResult(swapstepone, 1000);
                        } else {
                            Intent swapstepone = new Intent(SwapCheckOutActivity.this, Swap_Checkout_Step__First_Activity.class);
                            swapstepone.putExtra("data", response);
                            Main_TabHost.activity.startActivityForResult(swapstepone, 11);
                        }
                        finish();
                    } else {
                        dialog.dismiss();
                        getInfo();
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

                params.put("swap_order_id", swaporderid);
                params.put("rqid", "9");
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

    @Override
    public void onBackPressed() {

        showConfirmationDialog();
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.swap_cancel_confirmation_dialog_layout, null);

        dialog.setView(view);
        final AlertDialog alert = dialog.create();
        alert.show();
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        view.findViewById(R.id.swapcontinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                cancelOrder(ordernumber.getText().toString());
            }
        });
    }

    private void cancelOrder(final String orderid) {

        showProgress("wait cancelling order");

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_SHIPPINGINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgress();
                finish();
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
                params.put("rqid", "17");

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

