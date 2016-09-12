/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;

import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.Utils.LndUtils;
import com.eowise.recyclerview.stickyheaders.samples.data.NotificationData;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NotificationFragment extends Fragment {
    private static final String ARG_DATA_PROVIDER = "data provider";
    private static final String ARG_CAN_SWIPE_LEFT = "can swipe left";
    public static AbstractDataProvider mProvider;
    public static NotificationFragment notification;
    private ArrayList<String> notificationids = new ArrayList<String>();
    private boolean firsttime = true;
    private LinearLayout indicator;
    private TextView instructiontextview;

    public static NotificationFragment newInstance(String dataProvider, boolean canSwipeLeft) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATA_PROVIDER, dataProvider);
        args.putBoolean(ARG_CAN_SWIPE_LEFT, canSwipeLeft);
        fragment.setArguments(args);
        return fragment;
    }

    private String mDataProvider;
    private boolean mCanSwipeLeft;

    public static RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean loading = true;
    private int skipdata = 0;

    public NotificationFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataProvider = getArguments().getString(ARG_DATA_PROVIDER);
        mCanSwipeLeft = getArguments().getBoolean(ARG_CAN_SWIPE_LEFT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        notification = this;
        return inflater.inflate(R.layout.notification_fragment_recycler_list_view, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //noinspection ConstantConditions
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);

        // swipe manager
        mRecyclerViewSwipeManager = new RecyclerViewSwipeManager();
        mProvider = getDataProvider();
        //adapter
        final NotificationSwipeableItemAdapter myItemAdapter = new NotificationSwipeableItemAdapter(mProvider, mCanSwipeLeft, getActivity());
        myItemAdapter.setEventListener(new NotificationSwipeableItemAdapter.EventListener() {

            @Override
            public void onItemRemovedNotification(int position) {
                ((LndNotificationMessageActivity) getActivity()).onItemRemovedNotification(position);
            }
        });
        mAdapter = myItemAdapter;

        mWrappedAdapter = mRecyclerViewSwipeManager.createWrappedAdapter(myItemAdapter);      // wrap for swiping

        final GeneralItemAnimator animator = new SwipeDismissItemAnimator();

        // Change animations are enabled by default since support-v7-recyclerview v22.
        // Disable the change animation in order to make turning back animation of swiped item works properly.
        animator.setSupportsChangeAnimations(false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);

        // additional decorations
        //noinspection StatementWithEmptyBody
        if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            //   mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z1)));
        }
        //  mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));

        // NOTE:
        // The initialization order is very important! This order determines the priority of touch event handling.
        //
        // priority: TouchActionGuard > Swipe > DragAndDrop
        mRecyclerViewTouchActionGuardManager.attachRecyclerView(mRecyclerView);
        mRecyclerViewSwipeManager.attachRecyclerView(mRecyclerView);

        // for debugging
//        animator.setDebug(true);
//        animator.setMoveDuration(2000);
//        animator.setRemoveDuration(2000);
//        mRecyclerViewSwipeManager.setMoveToOutsideWindowAnimationDuration(2000);
//        mRecyclerViewSwipeManager.setReturnToDefaultPositionAnimationDuration(2000);
        indicator = (LinearLayout) getView().findViewById(R.id.indicator);
        instructiontextview = (TextView) getView().findViewById(R.id.instructiontextview);
        //for load more
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            mProvider.removeItem(mProvider.getCount()-1);
                            mAdapter.notifyDataSetChanged();
                            getData();
                            // Toast.makeText(getActivity(), "called", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        applySpannable();
        getData();
    }

    private void applySpannable() {

        Spannable word = new SpannableString("Tap on the camera ");

        word.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, word.length(), 0);
        word.setSpan(new ForegroundColorSpan(Color.parseColor("#222427")), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        instructiontextview.setText(word);
        Spannable wordTwo = new SpannableString("to post your first item");

        wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#757575")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        instructiontextview.append(wordTwo);
    }

    @Override
    public void onDestroyView() {
        if (mRecyclerViewSwipeManager != null) {
            mRecyclerViewSwipeManager.release();
            mRecyclerViewSwipeManager = null;
        }

        if (mRecyclerViewTouchActionGuardManager != null) {
            mRecyclerViewTouchActionGuardManager.release();
            mRecyclerViewTouchActionGuardManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mAdapter = null;
        mLayoutManager = null;

        super.onDestroyView();
    }

    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    public AbstractDataProvider getDataProvider() {
        return ((LndNotificationMessageActivity) getActivity()).getDataProvider(mDataProvider);
    }

    public void notifyItemChanged(int position) {
        mAdapter.notifyItemChanged(position);
    }

    public void notifyItemInserted(int position) {
        mAdapter.notifyItemInserted(position);
        mRecyclerView.scrollToPosition(position);
    }

    public void getData() {

        notificationids.clear();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_INBOXOPERATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    LndNotificationMessageActivity.loader.setVisibility(View.GONE);
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");

                    //to show instruction page
                    if (jarray.length() == 0) {

                        if (firsttime) {
                            firsttime = false;
                            ((LndNotificationMessageActivity) getActivity()).showInstruction(1);
                            showInstruction();
                        }

                        return;

                    }

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jo = jarray.getJSONObject(i);
                        NotificationData nd = new NotificationData();
                        nd.setNotification_id(jo.getString("notification_id"));
                        nd.setProfilepicimg(jo.getString("profile_pic"));
                        nd.setMessage(jo.getString("message"));
                        nd.setTime(getMilliseconds(jo.getString("date_time")));
                        nd.setUname(jo.getString("sender_uname"));
                        nd.setNotitype(jo.getString("notification_type"));
                        nd.setPostid(jo.getString("post_id"));
                        nd.setImgurl(jo.getString("image_url"));
                        nd.setSwappostids(jo.getString("swappost_id"));
                        nd.setSenderid(jo.getString("sender_id"));
                        nd.setSwap_order_id(jo.getString("swap_order_id"));
                        notificationids.add(jo.getString("notification_id"));

                        mProvider.addItem(nd);
                        mAdapter.notifyDataSetChanged();

                    }
                    //adding blank at bottom of notification
                    NotificationData nd = new NotificationData();
                    nd.setNotitype("11");
                    mProvider.addItem(nd);
                    mAdapter.notifyDataSetChanged();
                    LndUtils.lastnotificationid = mProvider.getItem(0).getNotificationdata().getNotification_id();
                    firsttime = false;
                    if (notificationids.size() > 0) {
                        JSONObject data = new JSONObject();
                        JSONArray notiarray = new JSONArray(notificationids);
                        data.put("notiids", notiarray);
                        markNotificaitonRead(data.toString());


                    }
                    skipdata = mProvider.getCount();
                    if (jarray.length() < 25)
                        loading = false;
                    else
                        loading = true;
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LndNotificationMessageActivity.loader.setVisibility(View.GONE);

                //  Log.e("response",error.getMessage()+"");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "13");
                params.put("user_id", SingleTon.pref.getString("user_id", ""));
                params.put("skipdata", skipdata + "");


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

    private void markNotificaitonRead(final String data) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/inboxope.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status")) {

                        Main_TabHost.notification.setText("0");
                        Main_TabHost.followers.setText("0");
                        if (Main_TabHost.message.getText().toString().compareToIgnoreCase("0") == 0)
                            Main_TabHost.popupWindow.dismiss();
                    }
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LndNotificationMessageActivity.loader.setVisibility(View.GONE);

                //  Log.e("response",error.getMessage()+"");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "15");
                params.put("user_id", SingleTon.pref.getString("user_id", ""));
                params.put("data", data);

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

    public void setCheckout(int pos, NotificationData nd3) {
        NotificationData nd = mProvider.getItem(pos).getNotificationdata();
        nd.setNotification_id(nd.getNotification_id());
        nd.setProfilepicimg(nd.getProfilepicimg());
        nd.setMessage(nd.getMessage());
        nd.setTime(nd.getTime());
        nd.setUname(nd.getUname());
        nd.setNotitype("9");
        nd.setPostid(nd3.getPostid());
        nd.setImgurl(nd3.getImgurl());
        nd.setSwap_order_id(nd3.getSwap_order_id());
        mProvider.removeItem(pos);
        mAdapter.notifyDataSetChanged();
        mProvider.addItemat(nd, pos);
        mAdapter.notifyDataSetChanged();

    }

    public void cancelSwap(int pos) {
        if (pos >= 0) {

            mProvider.removeItem(pos);
            mAdapter.notifyDataSetChanged();
            //LndUtils.pos = -1;
        //    Toast.makeText(getActivity(), pos + "rquest completed", Toast.LENGTH_SHORT).show();

        }
    }

    static long getMilliseconds(String datetime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            Date date = formatter.parse(datetime);
            // Log.e("date",date.toString());
            // Log.e("date2",formatter.format(date));

            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void showInstruction() {
        //Load animation
        final Animation slide_up = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_up);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            indicator.setVisibility(View.VISIBLE);
                            indicator.startAnimation(slide_up);
                        }
                    });
                } catch (Exception ex) {

                }
            }
        }).start();
    }

    public void refreshlist(final String lastnotiid) {


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_INBOXOPERATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");
                    LndUtils.lastnotificationid="";
                   // Log.e("json",response);
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jo = jarray.getJSONObject(i);
                        NotificationData nd = new NotificationData();
                        nd.setNotification_id(jo.getString("notification_id"));
                        nd.setProfilepicimg(jo.getString("profile_pic"));
                        nd.setMessage(jo.getString("message"));
                        nd.setTime(getMilliseconds(jo.getString("date_time")));
                        nd.setUname(jo.getString("sender_uname"));
                        nd.setNotitype(jo.getString("notification_type"));
                        nd.setPostid(jo.getString("post_id"));
                        nd.setImgurl(jo.getString("image_url"));
                        nd.setSwappostids(jo.getString("swappost_id"));
                        nd.setSenderid(jo.getString("sender_id"));
                        nd.setSwap_order_id(jo.getString("swap_order_id"));
                        notificationids.add(jo.getString("notification_id"));

                        mProvider.addItematFirst(nd);
                        mAdapter.notifyDataSetChanged();
                        playSound();
                    }
                       LndUtils.lastnotificationid = mProvider.getItem(0).getNotificationdata().getNotification_id();

                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                  Log.e("json error",error.getMessage()+"");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "16");
                params.put("user_id", SingleTon.pref.getString("user_id", ""));
                params.put("last_noti_id", lastnotiid);


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
private void playSound()
{
    SoundPool soundPool;
    int shutterSound;
    //play sound
    soundPool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 0);
    shutterSound = soundPool.load(getActivity(), R.raw.notification_sound, 0);
}
}

