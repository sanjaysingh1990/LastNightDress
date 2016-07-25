package com.eowise.recyclerview.stickyheaders.samples.Settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TipsandStrategy extends AppCompatActivity {

    @Bind(R.id.heading)
    TextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipsand_strategy);
        //initialize butter knife library
        ButterKnife.bind(this);

        //appying custom font
        heading.setTypeface(SingleTon.robotobold);

    }

    @OnClick(R.id.shippingmore)
    public void submit(View view) {
        Intent shipping = new Intent(this, TipsandStrategyShipping.class);
        startActivity(shipping);
    }

    public void back(View v) {
        finish();
    }
}
