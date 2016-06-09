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
import android.widget.TextView;
import android.widget.Toast;

import com.alihafizji.library.CreditCardEditText;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;

import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.adapters.ShippingLabelAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.ShippingLabelData;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShippingLabelActivity extends AppCompatActivity {

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
    private String shipvalue = "";
    @Bind(R.id.processmyorder)
    TextView processmyorder;
    @Bind(R.id.cardno)
    CreditCardEditText cardno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lnd_purchase_newshipping_level);
        ButterKnife.bind(this);
       /* recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new ShippingLabelAdapter(this,data);
        recyclerView.setAdapter(recyclerAdapter);*/
       // processmyorder.setClickable(false);
        //applying custom font
        heading.setTypeface(SingleTon.robotobold);
//initialize();
        PurchaseShippingLable();
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
        super.onBackPressed();
    }

    public void shippingChange(View v) {

       /* Intent intent = new Intent();
        intent.putExtra("shipprice", shipvalue);
        setResult(2, intent);
        finish();//finishing activity*/
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

    public void doPayment(View v) {

      setContentView(R.layout.lnd_shipping_label_purchase_complete);
        TextView info = (TextView)findViewById(R.id.info);
        Spannable word = new SpannableString("Thank you");

        word.setSpan(new ForegroundColorSpan(Color.parseColor("#be4d66")), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        word.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),  0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        info.setText(word);
        Spannable wordTwo = new SpannableString(", your order is completed.");

        wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#222427")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        info.append(wordTwo);
    }
    private void PurchaseShippingLable() {
        final   String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_FADEX_PURCHASE_SHIPPING_LABLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("json",response+"");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("response", error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("data", "1");
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
