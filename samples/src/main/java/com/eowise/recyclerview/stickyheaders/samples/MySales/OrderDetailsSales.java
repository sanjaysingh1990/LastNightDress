package com.eowise.recyclerview.stickyheaders.samples.MySales;

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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderDetailsSales extends AppCompatActivity {

    TextView heading;
    TextView shippinglabel;

    @Bind({R.id.brandname, R.id.buyername, R.id.listprice, R.id.yourearning, R.id.orderdate, R.id.ordernumber, R.id.statustext, R.id.courier, R.id.shipppingmethod})
    List<TextView> inprocess;
    @Bind({R.id.brandname, R.id.buyername, R.id.listprice, R.id.yourearning, R.id.orderdate, R.id.ordernumber, R.id.statustext, R.id.courier, R.id.shipppingmethod})
    List<TextView> canceltextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            } else if (status.compareToIgnoreCase("Shipped") == 0) {
                setContentView(R.layout.sales_item_shipped_layout);
                ButterKnife.bind(this);
                getReference();

            } else if (status.compareToIgnoreCase("delivered") == 0) {
                setContentView(R.layout.sales_item_delivered_layout);
                ButterKnife.bind(this);
                getReference();

            } else if (status.compareToIgnoreCase("cancelled") == 0) {
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
                TextView reportrating= (TextView) findViewById(R.id.reportrating);
                reportrating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContentView(R.layout.saller_reporting_rating_layout);

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
        startActivityForResult(shippingtest, 2);
    }

    private void getReference() {

        shippinglabel = (TextView) findViewById(R.id.shippingstatus);


        //applying custom fonts
        inprocess.get(0).setTypeface(SingleTon.robotobold);
        inprocess.get(1).setTypeface(SingleTon.robotoregular);
        for (int i = 2; i < inprocess.size(); i++) {
            inprocess.get(i).setTypeface(SingleTon.robotomedium);

        }


    }

    private void getReference2() {


        //applying custom fonts
        inprocess.get(0).setTypeface(SingleTon.robotobold);
        inprocess.get(1).setTypeface(SingleTon.robotoregular);
        for (int i = 2; i < canceltextview.size(); i++) {
            canceltextview.get(i).setTypeface(SingleTon.robotomedium);

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
        if (requestCode == 2) {
            if (data == null)
                return;

            String postids = data.getStringExtra("shipprice");

            Log.e("data", postids);
            String text = shippinglabel.getText().toString() + "    ";
            SpannableStringBuilder ssb = new SpannableStringBuilder(text);
            Bitmap smiley = BitmapFactory.decodeResource(getResources(), R.drawable.shippinglabel_icon);
            ssb.setSpan(new ImageSpan(smiley), text.length() - 3, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            shippinglabel.setText(ssb, TextView.BufferType.SPANNABLE);

            shippinglabel.setVisibility(View.VISIBLE);
        }
    }

    public void cancleOrder(View v) {
        setContentView(R.layout.sales_cancelling_layout);
        ButterKnife.bind(this);
        getReference2();
    }
}
