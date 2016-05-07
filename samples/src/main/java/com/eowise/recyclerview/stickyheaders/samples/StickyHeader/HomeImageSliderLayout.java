package com.eowise.recyclerview.stickyheaders.samples.StickyHeader;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.LndUserProfile.LndUserFullPostAdapter;
import com.eowise.recyclerview.stickyheaders.samples.NotificationFullPost;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.adapters.FullViewImageSliderAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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
    private ImageView[] myimg = new ImageView[4];
    private Animation anim1, anim2;
    private int count = 0;
    private ImageButton forward, backward;
    private MotionEvent mLastOnDownEvent = null;
    private int width;
    private Home_List_Data hld;
    private int pos;
    private Object lha;
    private Context con;
    private NotificationFullPost notifullpost;

    public HomeImageSliderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        con = context;
    }

    public HomeImageSliderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        con = context;
    }

    public HomeImageSliderLayout(Context context) {
        super(context);
        con = context;
    }
    public void setFeatureItems(ImageButton forward, ImageButton backward, int pos, Object lha) {
    }
    public void setFeatureItems(ImageButton forward, ImageButton backward, int pos, Object lha,final ProgressBar prog) {

        if (lha instanceof LndHomeAdapter)
            this.hld = ((LndHomeAdapter) lha).mItems.get(pos);

        else if (lha instanceof LndUserFullPostAdapter)
            this.hld = ((LndUserFullPostAdapter) lha).mItems.get(pos);
        this.lha = lha;
        this.pos = pos;
        this.forward = forward;
        this.backward = backward;
        //hide backward first
        this.backward.setVisibility(View.INVISIBLE);

        LinearLayout internalWrapper = new LinearLayout(getContext());
        internalWrapper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        internalWrapper.setOrientation(LinearLayout.HORIZONTAL);
        this.removeAllViews();
        addView(internalWrapper);
        this.mItems = hld.getImageurls();
        if (this.mItems.size() < 2)
            this.forward.setVisibility(View.INVISIBLE);
        else
            this.forward.setVisibility(View.VISIBLE);

        for (int i = 0; i < mItems.size(); i++) {
            View featureLayout = View.inflate(this.getContext(), R.layout.image_layout, null);
            ImageView img = (ImageView) featureLayout.findViewById(R.id.img);
            //image loader start
            if (mItems.get(i).length() == 0)
                img.setImageResource(R.drawable.loader_white);
            else
                ImageLoader.getInstance()
                        .displayImage(mItems.get(i), img, SingleTon.options4, new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                                prog.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                prog.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                prog.setVisibility(View.GONE);
                            }
                        }, new ImageLoadingProgressListener() {
                            @Override
                            public void onProgressUpdate(String imageUri, View view, int current, int total) {

                            }
                        });


            //end here
            //runtime width and height
            myimg[i] = img;
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            width = displayMetrics.widthPixels;

            int height = displayMetrics.heightPixels;


//setting margins around imageimageview
            int h = (height * 70) / 100; //left, top, right, bottom
            int w = displayMetrics.widthPixels;
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(w, h);
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
                    mActiveFeature = ((scrollX + (featureWidth / 2)) / featureWidth);
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

    public void setFeatureItems(ImageButton forward, ImageButton backward, ArrayList<String> urls, Context context) {

        notifullpost = (NotificationFullPost) context;
        this.forward = forward;
        this.backward = backward;
        //hide backward first
        this.backward.setVisibility(View.INVISIBLE);
        LinearLayout internalWrapper = new LinearLayout(getContext());
        internalWrapper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        internalWrapper.setOrientation(LinearLayout.HORIZONTAL);
        this.removeAllViews();
        addView(internalWrapper);
        this.mItems = urls;

        for (int i = 0; i < mItems.size(); i++) {
            View featureLayout = View.inflate(this.getContext(), R.layout.image_layout, null);
            ImageView img = (ImageView) featureLayout.findViewById(R.id.img);
            if (mItems.get(i).length() > 0) {
                // Log.e("url",mItems.get(i)+"");
                SingleTon.imageLoader.displayImage(mItems.get(i), img, SingleTon.options);
            }
            //runtime width and height
            myimg[i] = img;
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            width = displayMetrics.widthPixels;

            int height = displayMetrics.heightPixels;


//setting margins around imageimageview
            int h = (height * 70) / 100; //left, top, right, bottom
            int w = displayMetrics.widthPixels;
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(w, h);
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
                    mActiveFeature = ((scrollX + (featureWidth / 2)) / featureWidth);
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

    private class MyTouchListener implements OnTouchListener {
        private GestureDetector gestureScanner;
        int pos;

        public MyTouchListener(int pos) {
            gestureScanner = new GestureDetector(new DoubleSingleTap(pos));
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            return gestureScanner.onTouchEvent(event);
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
                if (e1 == null)
                    e1 = mLastOnDownEvent;
                if (e1 == null || e2 == null)
                    return false;


                //right to left
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //Log.e("position","right ot left");

                    int featureWidth = getMeasuredWidth();
                    mActiveFeature = (mActiveFeature < (mItems.size() - 1)) ? mActiveFeature + 1 : mItems.size() - 1;
                    smoothScrollTo(mActiveFeature * featureWidth, 0);
                    if (mItems.size() == 1)
                        return true;
                    count++;
                    forward.setVisibility(View.VISIBLE);
                    backward.setVisibility(View.VISIBLE);

                    if (count > 2) {
                        count = 3;
                        forward.setVisibility(View.GONE);
                    }
                    smoothScrollTo(width * count, 0);

                    return true;
                }
                //left to right
                else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    int featureWidth = getMeasuredWidth();
                    mActiveFeature = (mActiveFeature > 0) ? mActiveFeature - 1 : 0;
                    smoothScrollTo(mActiveFeature * featureWidth, 0);
                    // Log.e("position", "left ot right");
                    if (mItems.size() == 1)
                        return true;
                    count--;
                    backward.setVisibility(View.VISIBLE);
                    forward.setVisibility(View.VISIBLE);

                    if (count < 1) {
                        backward.setVisibility(View.GONE);


                        count = 0;
                    }
                    smoothScrollTo(width * count, 0);

                    return true;
                }
            } catch (Exception e) {
                //     Log.e("position", "There was an error processing the Fling event:" + e.getMessage());
            }
            return false;
        }
    }

    public class DoubleSingleTap implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
        int pos;

        public DoubleSingleTap(int pos) {
            this.pos = pos;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setTitle("Comments");

            // dialog.getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c6290")));
            dialog.setContentView(R.layout.fullview_images_slider);
            ViewPager viewPager = (ViewPager) dialog.findViewById(R.id.view_pager);
            FullViewImageSliderAdapter adapter = new FullViewImageSliderAdapter(getContext(), mItems);
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
            if (lha != null)

                postLiked(hld.getPost_id(),hld.getUserid());
            else
                notifullpost.postnotiLiked();
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

    private void postLiked(final String postid,final String userid) {
        Home_List_Data hld = null;
        if (lha instanceof LndHomeAdapter)
            hld = ((LndHomeAdapter) lha).mItems.get(pos - 1);

        else if (lha instanceof LndUserFullPostAdapter)
            hld = ((LndUserFullPostAdapter) lha).mItems.get(pos - 1);
        if (hld != null)
            if (hld.getLikedvalue().compareTo("1") == 0)
                hld.setLikedvalue(0 + "");
            else
                hld.setLikedvalue(1 + "");
        if (lha instanceof LndHomeAdapter)
            ((LndHomeAdapter) lha).notifyDataSetChanged();

        else if (lha instanceof LndUserFullPostAdapter)
            ((LndUserFullPostAdapter) lha).notifyDataSetChanged();
        RequestQueue queue = Volley.newRequestQueue(con);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/lndlikeunlike.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                 Log.e("json", response.toString());
                try {

                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status")) {
                        int val = jobj.getInt("value");
                        if (val == 1) {
                            Home_List_Data hld1 = null;// hld2 = null;
                            if (lha instanceof LndHomeAdapter) {
                                hld1 = ((LndHomeAdapter) lha).mItems.get(pos);
                              //  hld2 = ((LndHomeAdapter) lha).mItems.get(pos - 1);
                            } else if (lha instanceof LndUserFullPostAdapter) {
                                hld1 = ((LndUserFullPostAdapter) lha).mItems.get(pos);
                               // hld2 = ((LndUserFullPostAdapter) lha).mItems.get(pos - 1);
                            }
                            if (hld1.getLikestotal() != 0) {
                                hld1.setLikestotal(hld1.getLikestotal() - 1);
                                //hld2.setLikestotal(hld2.getLikestotal() - 1);

                            }
                            if (lha instanceof LndHomeAdapter)
                                ((LndHomeAdapter) lha).notifyDataSetChanged();
                            else if (lha instanceof LndUserFullPostAdapter)
                                ((LndUserFullPostAdapter) lha).notifyDataSetChanged();

                        } else {
                            Home_List_Data hld1 = null;// hld2 = null;
                            if (lha instanceof LndHomeAdapter) {
                                hld1 = ((LndHomeAdapter) lha).mItems.get(pos);
                               // hld2 = ((LndHomeAdapter) lha).mItems.get(pos - 1);
                            } else if (lha instanceof LndUserFullPostAdapter) {
                                hld1 = ((LndUserFullPostAdapter) lha).mItems.get(pos);
                               // hld2 = ((LndUserFullPostAdapter) lha).mItems.get(pos - 1);
                            }

                            hld1.setLikestotal(hld1.getLikestotal() + 1);
                           // hld2.setLikestotal(hld2.getLikestotal() + 1);
                            if (lha instanceof LndHomeAdapter)
                                ((LndHomeAdapter) lha).notifyDataSetChanged();

                            else if (lha instanceof LndUserFullPostAdapter)
                                ((LndUserFullPostAdapter) lha).notifyDataSetChanged();
                        }
                    }

                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage() + "");
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
                params.put("user_id", SingleTon.pref.getString("user_id", ""));
                params.put("post_id", postid);
                params.put("date_time", SingleTon.getCurrentTimeStamp());
                params.put("whos_post",userid);



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

