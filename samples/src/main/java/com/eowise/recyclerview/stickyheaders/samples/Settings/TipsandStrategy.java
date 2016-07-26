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

    @OnClick(R.id.returntips)
    public void returnItem(View view) {
        Intent returnitem = new Intent(this, TipsActivity.class);
        returnitem.putExtra("type", 1);
        startActivity(returnitem);
    }

    @OnClick(R.id.photos)
    public void photos(View view) {
        Intent photos = new Intent(this, TipsActivity.class);
        photos.putExtra("type", 2);
        startActivity(photos);
    }

    @OnClick(R.id.swap)
    public void swap(View view) {
        Intent swap = new Intent(this, TipsActivity.class);
        swap.putExtra("type", 3);
        startActivity(swap);
    }

    @OnClick(R.id.social)
    public void social(View view) {
        Intent social = new Intent(this, TipsActivity.class);
        social.putExtra("type", 4);
        startActivity(social);
    }

    @OnClick(R.id.authentication)
    public void authentication(View view) {
        Intent authentication = new Intent(this, TipsActivity.class);
        authentication.putExtra("type", 5);
        startActivity(authentication);
    }


    public void back(View v) {
        finish();
    }
}
