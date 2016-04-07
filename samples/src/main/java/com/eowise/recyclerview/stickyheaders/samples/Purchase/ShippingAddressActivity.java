package com.eowise.recyclerview.stickyheaders.samples.Purchase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShippingAddressActivity extends AppCompatActivity {

    @Bind(R.id.heading)
    TextView heading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);
        ButterKnife.bind(this);
        //setup font
        heading.setTypeface(SingleTon.robotobold);


    }

    public void close(View v) {
        finish();
    }

    public void save(View v) {
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            Intent payment = new Intent(this, SwapCheckOutActivity.class);
            startActivity(payment);
        } else {
            Intent payment = new Intent(this, PurchaseActivity.class);
            startActivity(payment);

        }
    }
}
