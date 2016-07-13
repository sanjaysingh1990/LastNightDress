package com.eowise.recyclerview.stickyheaders.samples.TabDemo;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eowise.recyclerview.stickyheaders.samples.R;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;

    public CustomPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ViewGroup layout = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (position == 0)
            layout = (ViewGroup) inflater.inflate(R.layout.lnd_tutorial_layout, collection, false);
        else if (position == 1)
            layout = (ViewGroup) inflater.inflate(R.layout.lnd_tutorial_layout2, collection, false);
        else
            layout = (ViewGroup) inflater.inflate(R.layout.lnd_tutorial_layout, collection, false);

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return "demo";
    }

}