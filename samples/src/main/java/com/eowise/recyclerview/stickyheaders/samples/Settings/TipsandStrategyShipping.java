package com.eowise.recyclerview.stickyheaders.samples.Settings;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TipsandStrategyShipping extends AppCompatActivity {

    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.heading)
    TextView heading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tipsand_strategy_shipping);
        ButterKnife.bind(this);
        CustomPagerAdapter cuspageradapt = new CustomPagerAdapter(this);
        viewpager.setAdapter(cuspageradapt);
        //appying custom font
        heading.setTypeface(SingleTon.robotobold);
    }

    public void back(View v) {
        finish();
    }
}
