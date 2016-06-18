package com.eowise.recyclerview.stickyheaders.samples.MyPurchases;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.Utils.RelativeTimeTextView;
import com.eowise.recyclerview.stickyheaders.samples.Utils.TimeAgo;
import com.eowise.recyclerview.stickyheaders.samples.data.MySalesPurchasesData;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderDetailsPurchased extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String status = extra.getString("type");
            if (status.compareToIgnoreCase("in process") == 0) {
                setContentView(R.layout.purchases_inprocess_layout);
                ButterKnife.bind(this);
                getReference();

            } else if (status.compareToIgnoreCase("delivered") == 0) {
                if (SingleTon.pref.getBoolean("rated", false))
                    setContentView(R.layout.purchases_itemaccepted_layout);
                else {
                    setContentView(R.layout.purchases_delivered_layout);
                    ButterKnife.bind(this);
                    getReference();

                }
            } else if (status.compareToIgnoreCase("cancelled") == 0) {
                setContentView(R.layout.purchases_item_cancelled_layout);
                ButterKnife.bind(this);
                getReference();
                TextView cancelled = (TextView) findViewById(R.id.cancelled);
                //appying spannable
                String text = cancelled.getText().toString() + "    ";
                SpannableStringBuilder ssb = new SpannableStringBuilder(text);
                Bitmap smiley = BitmapFactory.decodeResource(getResources(), R.drawable.shippinglabel_icon);
                ssb.setSpan(new ImageSpan(smiley), text.length() - 3, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                cancelled.setText(ssb, TextView.BufferType.SPANNABLE);
            } else if (status.compareToIgnoreCase("cancelled2") == 0) {
                setContentView(R.layout.purchases_cancelled_layout);
                ButterKnife.bind(this);
                getReference();

            } else if (status.compareToIgnoreCase("shipped") == 0) {
                setContentView(R.layout.purchase_item_shipped_layout);
                ButterKnife.bind(this);
                getReference();

            } else if (status.compareToIgnoreCase("claim processing") == 0) {
                setContentView(R.layout.purchases_claimprocessing_layout);

            } else if (status.compareToIgnoreCase("rating reported") == 0) {
                setContentView(R.layout.purchases_ratingreported_layout);

            } else if (status.compareToIgnoreCase("claim approved") == 0) {
                setContentView(R.layout.purchases_claimapproved_layout);

            } else if (status.compareToIgnoreCase("claim declined") == 0) {
                setContentView(R.layout.purchases_claimdeclined_layout);

            } else if (status.compareToIgnoreCase("Rating reported") == 0) {
                setContentView(R.layout.purchases_ratingreported_layout);
                ButterKnife.bind(this);
                getReference();
                TextView editrating = (TextView) findViewById(R.id.editrating);
                editrating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent rateuser = new Intent(OrderDetailsPurchased.this, RateUserActivity.class);
                        startActivity(rateuser);
                    }
                });
            } else if (status.compareToIgnoreCase("Item accepted") == 0) {
                setContentView(R.layout.purchases_itemaccepted_layout);
                ButterKnife.bind(this);
                getReference();

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

    public void itemAccepted(View v) {
        Intent rateuser = new Intent(this, RateUserActivity.class);
        startActivity(rateuser);
        finish();
    }

    public void returnItem(View v) {
        setContentView(R.layout.purchases_claim_layout);
        final TextView continueclaim = (TextView) findViewById(R.id.claimcontinue);
        final EditText commentvalue = (EditText) findViewById(R.id.yourcomment);
        continueclaim.setClickable(false);
        commentvalue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    continueclaim.setClickable(true);
                    continueclaim.setTextColor(Color.parseColor("#be4d66"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        continueclaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent purchasereturn = new Intent(OrderDetailsPurchased.this, PurchaseShipplingLabelActivity.class);
                purchasereturn.putExtra("claimtext", commentvalue.getText() + "");
                startActivity(purchasereturn);
                finish();

            }
        });
    }

    public void cancelClaim(View v) {
        setContentView(R.layout.purchases_delivered_layout);

    }

    public void continueShopping(View v) {
        finish();
    }

    public void editRating(View v) {
        Intent purchasereturn = new Intent(OrderDetailsPurchased.this, RateUserActivity.class);
        purchasereturn.putExtra("ratedvalue", 2);
        purchasereturn.putExtra("comment", "Terrible service, I will never buy anything from this user again.");

        startActivity(purchasereturn);
        finish();
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
            MySalesPurchasesData mspd = (MySalesPurchasesData) extra.getSerializable("data");
            brandname.setText(mspd.getBrand_name() + "");
            sellername.setText(mspd.getSeller_name() + "");
            price.setText("$" + mspd.getPrice_now() + "");
            shippingprice.setText("$" + mspd.getShipping_charge() + "");
            grandtotalprice.setText("$" + mspd.getTotal_amount() + "");
            shippingmethod.setText(mspd.getShipping_method() + "");
            orderdate.setText(mspd.getOrder_date() + "");
            ordernumber.setText(mspd.getOrder_id() + "");
            showtime.setReferenceTime(mspd.getOrder_date());
            SingleTon.imageLoader.displayImage(mspd.getImage_url(), productimage, SingleTon.options4);


        }
    }

}
