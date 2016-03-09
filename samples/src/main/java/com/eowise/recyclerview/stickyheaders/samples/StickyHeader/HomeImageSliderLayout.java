package com.eowise.recyclerview.stickyheaders.samples.StickyHeader;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.adapters.FullViewImageSliderAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeImageSliderLayout extends HorizontalScrollView {
    private static final int SWIPE_MIN_DISTANCE = 50;
    private static final int SWIPE_THRESHOLD_VELOCITY = 250;

    private ArrayList<String> mItems = null;
    private GestureDetector mGestureDetector;
    private int mActiveFeature = 0;
    private ImageView[] myimg=new ImageView[4];
    private Animation anim1,anim2;
    private int count=0;
    private ImageButton forward,backward;
    private MotionEvent mLastOnDownEvent = null;
    private int width;
    private Home_List_Data hld,hld2;
    private TextView likecount;
    private ImageButton likebutton;

    public HomeImageSliderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public HomeImageSliderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeImageSliderLayout(Context context) {
        super(context);
    }

    public void setFeatureItems(Home_List_Data items,Home_List_Data items2,ImageButton forward,ImageButton backward,TextView lkecount,ImageButton likebutton){

        this.likebutton=likebutton;
        this.hld=items;
        this.hld2=items2;
        this.likecount=lkecount;
        this.forward=forward;
        this.backward=backward;
        //hide backward first
       // this.backward.setVisibility(View.GONE);
        LinearLayout internalWrapper = new LinearLayout(getContext());
        internalWrapper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        internalWrapper.setOrientation(LinearLayout.HORIZONTAL);
        this.removeAllViews();
        addView(internalWrapper);
        this.mItems = items2.getImageurls();

        for(int i = 0; i< mItems.size();i++){
            View featureLayout =  View.inflate(this.getContext(), R.layout.image_layout, null);
            ImageView img= (ImageView) featureLayout.findViewById(R.id.img);
            if(mItems.get(i).endsWith(".jpg"))
            ImageLoaderImage.imageLoader.displayImage(mItems.get(i),img,ImageLoaderImage.options);

           //runtime width and height
            myimg[i]=img;
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            width=displayMetrics.widthPixels;

            int height = displayMetrics.heightPixels;


//setting margins around imageimageview
         int    h=(height*70)/100; //left, top, right, bottom
         int w=displayMetrics.widthPixels;
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(w,h);
            img.setLayoutParams(parms);
            img.setOnTouchListener(new MyTouchListener(i));

            //...
            //Create the view for each screen in the scroll view
            //...
            internalWrapper.addView(featureLayout);
        }
        // anim1= AnimationUtils.loadAnimation(getContext(),R.anim.fadein);
       // anim2= AnimationUtils.loadAnimation(getContext(),R.anim.fadeout);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //If the user swipes

                if (mGestureDetector.onTouchEvent(event)) {
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    int scrollX = getScrollX();
                    int featureWidth = v.getMeasuredWidth();
                    mActiveFeature = ((scrollX + (featureWidth /2)) / featureWidth);
                    int scrollTo = mActiveFeature * featureWidth;
                   // Log.e("status",scrollTo+"");

                    smoothScrollTo(scrollTo, 0);


                    return true;
                } else {
                    return false;
                }
            }
        });
        mGestureDetector = new GestureDetector(new MyGestureDetector());
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        this.mGestureDetector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }
    private class MyTouchListener implements OnTouchListener
    {
        private GestureDetector gestureScanner;
        int pos;
        public MyTouchListener(int pos)
        {
            gestureScanner = new GestureDetector(new DoubleSingleTap(pos));
        }
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            return gestureScanner.onTouchEvent(event);
        }
    }
    private class MyEvents extends DoubleClickListener {
        int pos;
        MyEvents(int pos)
        {
            this.pos=pos;
        }


        @Override
        public void onSingleClick(View v) {
            // Log.e("sliderurl",mItems.get(0));
           /* Dialog dialog = new Dialog(getContext(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setTitle("Comments");

            // dialog.getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c6290")));
            dialog.setContentView(R.layout.fullview_images_slider);
            ViewPager viewPager = (ViewPager) dialog.findViewById(R.id.view_pager);
            FullViewImageSliderAdapter adapter = new FullViewImageSliderAdapter(getContext(),mItems);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(pos);
            viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
                @Override
                public void transformPage(View page, float position) {
                    final float normalizedposition = Math.abs(Math.abs(position) - 1);
                    page.setScaleX(normalizedposition / 2 + 0.5f);
                    page.setScaleY(normalizedposition / 2 + 0.5f);
                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.show();*/
            Toast.makeText(getContext(),"single tap",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDoubleClick(View v) {
            Toast.makeText(getContext(),"DOuble tap",Toast.LENGTH_LONG).show();
        }
    }
    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
        //    mLastOnDownEvent = e;
            return true;

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (e1==null)
                    e1 = mLastOnDownEvent;
                if (e1==null || e2==null)
                    return false;


                //right to left
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //Log.e("position","right ot left");

                    int featureWidth = getMeasuredWidth();
                    mActiveFeature = (mActiveFeature < (mItems.size() - 1))? mActiveFeature + 1:mItems.size() -1;
                    smoothScrollTo(mActiveFeature * featureWidth, 0);
                    count++;
                    forward.setVisibility(View.VISIBLE);
                    backward.setVisibility(View.VISIBLE);

                    if (count > 2) {
                        count = 3;
                        forward.setVisibility(View.GONE);
                    }
                    smoothScrollTo(width*count, 0);

                    return true;
                }
                //left to right
                else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    int featureWidth = getMeasuredWidth();
                    mActiveFeature = (mActiveFeature > 0)? mActiveFeature - 1:0;
                    smoothScrollTo(mActiveFeature * featureWidth, 0);
                   // Log.e("position", "left ot right");
                    count--;
                    backward.setVisibility(View.VISIBLE);
                    forward.setVisibility(View.VISIBLE);

                    if (count < 1) {
                        backward.setVisibility(View.GONE);


                        count = 0;
                    }
                    smoothScrollTo(width*count, 0);

                    return true;
                }
            } catch (Exception e) {
           //     Log.e("position", "There was an error processing the Fling event:" + e.getMessage());
            }
            return false;
        }
    }
    public abstract class DoubleClickListener implements OnClickListener {

        private static final long DOUBLE_CLICK_TIME_DELTA = 500;//milliseconds

        long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                onDoubleClick(v);
            } else {
                onSingleClick(v);
            }
            lastClickTime = clickTime;
        }

        public abstract void onSingleClick(View v);
        public abstract void onDoubleClick(View v);
    }
    public class DoubleSingleTap  implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
    {
         int pos;
        public DoubleSingleTap(int pos)
        {
            this.pos=pos;
        }
        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            Dialog dialog = new Dialog(getContext(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setTitle("Comments");

            // dialog.getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c6290")));
            dialog.setContentView(R.layout.fullview_images_slider);
            ViewPager viewPager = (ViewPager) dialog.findViewById(R.id.view_pager);
            FullViewImageSliderAdapter adapter = new FullViewImageSliderAdapter(getContext(),mItems);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(pos);
            viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
                @Override
                public void transformPage(View page, float position) {
                    final float normalizedposition = Math.abs(Math.abs(position) - 1);
                    page.setScaleX(normalizedposition / 2 + 0.5f);
                    page.setScaleY(normalizedposition / 2 + 0.5f);
                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.show();
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            //Toast.makeText(getContext(),"double tap"+pos,Toast.LENGTH_LONG).show();
            setLike(hld.getPost_id(),likecount,likebutton);
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            return true;
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }
    }
    private void setLike(final String postid,final TextView likescount,final ImageButton likebutton)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/lndlikeunlike.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Log.e("response", response.toString());
                try {

                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status")) {
                        int val = jobj.getInt("value");
                        if (val == 1) {
                            String str = likescount.getText().toString();
                            String numberOnly = str.replaceAll("[^0-9]", "");
                            int value = Integer.parseInt(numberOnly);
                            likescount.setText(value - 1 + " likes");
                            likebutton.setImageResource(R.drawable.like_icon);
                            hld.setLikedvalue(0+"");
                          try {
                              int totallikes=Integer.parseInt(hld2.getLikestotal());
                              hld2.setLikestotal((totallikes-1)+"");

                              }
                            catch(Exception ex)
                            {

                            }
                        } else {
                            String str = likescount.getText().toString();
                            String numberOnly = str.replaceAll("[^0-9]", "");

                            int value = Integer.parseInt(numberOnly);
                            likescount.setText(value + 1 + " likes");
                            likebutton.setImageResource(R.drawable.liked_icon);
                            hld.setLikedvalue(1 + "");
                            try {
                                int totallikes=Integer.parseInt(hld2.getLikestotal());
                                hld2.setLikestotal((totallikes+1)+"");

                            }
                            catch(Exception ex)
                            {

                            }
                        }
                    }

                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //showing snakebar here
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",ImageLoaderImage.pref.getString("user_id",""));
                params.put("post_id", postid);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }
    }
