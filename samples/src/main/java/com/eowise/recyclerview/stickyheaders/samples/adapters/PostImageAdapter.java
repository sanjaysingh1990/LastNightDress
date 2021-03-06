package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;

import java.util.ArrayList;
import java.util.List;

public class PostImageAdapter extends PagerAdapter {
    Context context;
    private List<String> GalImages = new ArrayList<String>();


    public PostImageAdapter(Context context, List<String> data){
        this.context=context;
        GalImages=data;
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
      /*  ImageView imageView = new ImageView(context);
        int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
      //  imageView.setImageResource(GalImages.get(position));*/
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.fullimage_layout, container,
                false);
        ImageView imgDisplay = (ImageView) viewLayout.findViewById(R.id.touchimage);

       SingleTon.imageLoader.displayImage(GalImages.get(position), imgDisplay, SingleTon.options);
                ((ViewPager) container).addView(viewLayout, 0);
       imgDisplay.setOnClickListener(new MyEvent(position));
        return viewLayout;
    }
private class MyEvent implements View.OnClickListener
{
    int pos;
public MyEvent(int pos)
{
    this.pos=pos;
}
    @Override
    public void onClick(View v) {
        Log.e("currimg", pos + "");
        Dialog dialog = new Dialog(context,R.style.DialogSlideAnim2);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        // dialog.getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c6290")));
        dialog.setContentView(R.layout.fullview_images_slider);
        ViewPager viewPager = (ViewPager) dialog.findViewById(R.id.view_pager);
        FullViewImageSliderAdapter adapter = new FullViewImageSliderAdapter(context,GalImages);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(pos);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
    }
}
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }
}