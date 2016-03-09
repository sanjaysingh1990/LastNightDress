package com.eowise.recyclerview.stickyheaders.samples;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.samples.adapters.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class OnSwipeTouchListener extends SimpleOnGestureListener {

    private static final int MIN_DISTANCE = 25;
    private static final String TAG = "MyGestureListener";
    private ImageView img1,img2,img3,img4;
    private LinearLayout frontLayout;
    private Animation inFromRight,outToRight,outToLeft,inFromLeft;
    private Context con;
    private int count=0;
    private int currpos=0;
    private List<String> images=new ArrayList<String>();
    private ImageView[] img=new ImageView[4];
  //  private ImageView mainimgage;
    public OnSwipeTouchListener(Context ctx,View convertView,int count,List<String> data) {
con=ctx;
        this.count=count;
        this.images=data;
        img[0]= (ImageView) convertView.findViewById(R.id.icon1);
        img[1]= (ImageView) convertView.findViewById(R.id.icon2);
        img[2]= (ImageView) convertView.findViewById(R.id.icon3);
        img[3]= (ImageView) convertView.findViewById(R.id.icon4);
      //  mainimgage= (ImageView) convertView.findViewById(R.id.image);

        Log.e("size",images.size()+"");
    }


    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Dialog dialog = new Dialog(con,R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("Comments");

        // dialog.getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c6290")));
        dialog.setContentView(R.layout.imageslider_layout);
        ViewPager viewPager = (ViewPager)dialog. findViewById(R.id.view_pager);
      //  ImageAdapter adapter = new ImageAdapter(con,images);
       // viewPager.setAdapter(adapter);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        return super.onDoubleTap(e);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        float diffX = e2.getX() - e1.getX();
        float diffY = e2.getY() - e1.getY();

        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > MIN_DISTANCE) {
                if(diffX<0){
                    currpos++;
                    if(currpos>3)
                        currpos=3;
                    img[0].setImageResource(R.drawable.black_icon);
                    img[1].setImageResource(R.drawable.black_icon);
                    img[2].setImageResource(R.drawable.black_icon);
                    img[3].setImageResource(R.drawable.black_icon);
                    img[currpos].setImageResource(R.drawable.color_icon);

                   // Toast.makeText(con,"swipe right"+count,Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Swipe Right to Left");

                }else{
                    currpos--;
                    if(currpos<0)
                      currpos=0;
                    img[0].setImageResource(R.drawable.black_icon);
                    img[1].setImageResource(R.drawable.black_icon);
                    img[2].setImageResource(R.drawable.black_icon);
                    img[3].setImageResource(R.drawable.black_icon);

                    img[currpos].setImageResource(R.drawable.color_icon);

                    // Toast.makeText(con,"swipe left"+count,Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Swipe Left to Right");

                }
            }
        }

        return true;
    }

}