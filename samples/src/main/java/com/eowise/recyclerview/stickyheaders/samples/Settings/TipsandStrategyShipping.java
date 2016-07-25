package com.eowise.recyclerview.stickyheaders.samples.Settings;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.eowise.recyclerview.stickyheaders.samples.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TipsandStrategyShipping extends AppCompatActivity {

    @Bind(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tipsand_strategy_shipping);
        ButterKnife.bind(this);
        CustomPagerAdapter cuspageradapt = new CustomPagerAdapter(this);
       viewpager.setAdapter(cuspageradapt);

    }
}
