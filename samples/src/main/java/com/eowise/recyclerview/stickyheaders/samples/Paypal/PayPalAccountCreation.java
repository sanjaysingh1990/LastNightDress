package com.eowise.recyclerview.stickyheaders.samples.Paypal;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;


import com.eowise.recyclerview.stickyheaders.samples.R;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PayPalAccountCreation extends FragmentActivity {

    AccountAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    public static String textarray[];
    @Bind(R.id.back)ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paypalaccountcreation);

         ButterKnife.bind(this);

        mAdapter = new AccountAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator = indicator;
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;
        indicator.setBackgroundColor(Color.parseColor("#000000"));
        indicator.setRadius(5 * density);
        indicator.setPageColor(Color.parseColor("#000000"));
        indicator.setFillColor(Color.parseColor("#ffffff"));
        indicator.setStrokeColor(Color.parseColor("#dadada"));
        indicator.setStrokeWidth(2 * density);
        textarray= getResources().getStringArray(R.array.paypalinstruction);

    }

public void back(View view)
{
    finish();
}

}