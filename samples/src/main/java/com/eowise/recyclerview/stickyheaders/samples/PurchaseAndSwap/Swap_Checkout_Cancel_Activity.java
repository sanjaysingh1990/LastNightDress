package com.eowise.recyclerview.stickyheaders.samples.PurchaseAndSwap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.R;

public class Swap_Checkout_Cancel_Activity extends AppCompatActivity {

    TextView actioninfo;
    EditText cancelreason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swap_checkout_first_step);
    }

    public void cancleOrder(View v) {
        setContentView(R.layout.swap_checkout_cancel_order);
        TextView submit = (TextView) findViewById(R.id.submit);
        actioninfo = (TextView) findViewById(R.id.info);
        cancelreason = (EditText) findViewById(R.id.cancelinfoeditext);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelreason.setVisibility(View.GONE);
                String text = getResources().getString(R.string.swap_checkout_cancel_successfully) + "    ";

                showInfo(text,actioninfo);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            changeLayout();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showInfo(String text,TextView txt) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);
        Bitmap smiley = BitmapFactory.decodeResource(getResources(), R.drawable.shippinglabel_icon);
        ssb.setSpan(new ImageSpan(smiley), text.length() - 3, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txt.setText(ssb, TextView.BufferType.SPANNABLE);
    }

    public void finish(View v) {
        finish();
    }
    private void changeLayout()
    {
        setContentView(R.layout.lnd_swap_purchase_complete);
        TextView traninfo= (TextView) findViewById(R.id.transactioninfo);
        showInfo("Transaction Completed    ",traninfo);

    }
    public void back(View v)
    {
        finish();
    }
}
