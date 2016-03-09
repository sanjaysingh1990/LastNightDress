package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.R;

import java.util.ArrayList;
import java.util.List;

public class FullViewImageSliderAdapter extends PagerAdapter {
    Context context;
    private List<String> GalImages = new ArrayList<String>();

    public FullViewImageSliderAdapter(Context context, List<String> data) {
        this.context = context;
        GalImages = data;
    }

    @Override
    public int getCount() {
        return GalImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.fullscreen_imagepinchzoom_layout, container,
                false);
        ImageView imgDisplay = (ImageView) viewLayout.findViewById(R.id.touchimage);

        ImageLoaderImage.imageLoader.displayImage(GalImages.get(position), imgDisplay, ImageLoaderImage.options);
        ((ViewPager) container).addView(viewLayout, 0);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }
}