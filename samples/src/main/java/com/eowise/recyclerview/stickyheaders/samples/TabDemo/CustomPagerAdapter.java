package com.eowise.recyclerview.stickyheaders.samples.TabDemo;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
        else if (position == 1) {
            layout = (ViewGroup) inflater.inflate(R.layout.lnd_tutorial_layout2, collection, false);
            adjust((ImageView) layout.findViewById(R.id.circleimg));
    } else
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

    private void adjust(ImageView img) {
        int h = 0, w = 0;

        //device with and height
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();

        int width = displayMetrics.widthPixels;
        //Toast.makeText(getActivity(), "width" + width, Toast.LENGTH_SHORT).show();
        if (width <= 480) {
            h = 170 * 2;
            w = 190 * 2;

        } else if (width > 480 && width <= 550) {
            h = 190 * 2;
            w = 210 * 2;

        } else if (width > 480 && width <= 720) {
            h = 220 * 2;
            w = 240 * 2;

        } else if (width > 720 && width <= 1280) {
            h = 320 * 2;
            w = 340 * 2;

        } else {
            h = 350 * 2;
            w = 370 * 2;


        }
        img.getLayoutParams().height =h+(h*5)/10;
        img.getLayoutParams().width = w+(w*5)/10;
    }
}