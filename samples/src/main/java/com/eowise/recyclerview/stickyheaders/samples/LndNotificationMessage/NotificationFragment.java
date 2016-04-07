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

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;
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
    private ArrayList<String> notificationids=new ArrayList<String>();

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
    private RecyclerView.LayoutManager mLayoutManager;
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;
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
        notification=this;
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
        getData();
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
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/inboxope.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                 try {
                    LndNotificationMessageActivity.loader.setVisibility(View.GONE);
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");
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
                        notificationids.add(jo.getString("notification_id"));
                        mProvider.addItem(nd);
                        mAdapter.notifyDataSetChanged();

                    }
                     //adding blank at bottom of notification
                     NotificationData nd = new NotificationData();
                     nd.setNotitype("8");
                     mProvider.addItem(nd);
                     mAdapter.notifyDataSetChanged();

                     if(notificationids.size()>0) {
                            JSONObject data=new JSONObject();
                            JSONArray notiarray = new JSONArray(notificationids);
                            data.put("notiids",notiarray);
                            markNotificaitonRead(data.toString());


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
                params.put("rqid", "13");
                params.put("user_id", SingleTon.pref.getString("user_id", ""));

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

    private void markNotificaitonRead(final String data)
    {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/inboxope.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               try {
                     JSONObject jobj = new JSONObject(response.toString());
                         if(jobj.getBoolean("status"))
                         {

                             Main_TabHost.notification.setText("0");
                             Main_TabHost.followers.setText("0");
                             if(Main_TabHost.message.getText().toString().compareToIgnoreCase("0")==0)
                                 Main_TabHost.popupWindow.dismiss();
                         }
                    }


                 catch (Exception ex) {
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
                params.put("data",data);

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
public void setCheckout(int pos,NotificationData nd)
{
    mProvider.removeItem(pos);
    mAdapter.notifyDataSetChanged();
    NotificationData nd2 = new NotificationData();
    nd.setNotification_id(nd.getNotification_id());
    nd.setProfilepicimg(nd.getProfilepicimg());
    nd.setMessage(nd.getMessage());
    nd.setTime(nd.getTime());
    nd.setUname(nd.getUname());
    nd.setNotitype("9");
    nd.setPostid(nd.getPostid());
    nd.setImgurl(nd.getImgurl());
    mProvider.addItem(nd);
    mAdapter.notifyDataSetChanged();
}
    public void cancelSwap(int pos)
    {
        mProvider.removeItem(pos);
        mAdapter.notifyDataSetChanged();

    }
    static long getMilliseconds(String datetime)
    {
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
}
