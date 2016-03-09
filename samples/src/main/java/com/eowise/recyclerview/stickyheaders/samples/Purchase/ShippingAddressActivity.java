package com.eowise.recyclerview.stickyheaders.samples.Purchase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

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
        heading.setTypeface(ImageLoaderImage.robotobold);


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
