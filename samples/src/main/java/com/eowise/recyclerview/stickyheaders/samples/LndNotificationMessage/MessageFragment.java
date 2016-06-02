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

import android.content.Intent;
import android.graphics.Color;
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
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.Utils.TimeAgo;
import com.eowise.recyclerview.stickyheaders.samples.data.ConcreteData1;
import com.eowise.recyclerview.stickyheaders.samples.data.MessageData;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MessageFragment extends Fragment {
    private static final String ARG_DATA_PROVIDER = "data provider";
    private static final String ARG_CAN_SWIPE_LEFT = "can swipe left";
    public static AbstractDataProvider2 mProvider;
    public static MessageFragment messageFragment;
    private TextView instructiontextview;

    public static MessageFragment newInstance(String dataProvider, boolean canSwipeLeft) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATA_PROVIDER, dataProvider);
        args.putBoolean(ARG_CAN_SWIPE_LEFT, canSwipeLeft);
        fragment.setArguments(args);
        return fragment;
    }

    private String mDataProvider;
    private boolean mCanSwipeLeft;

    public static RecyclerView mRecyclerView;
    private LinearLayout indicator;
    private LinearLayoutManager mLayoutManager;
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;
    static MessageSwipeableItemAdapter2 myItemAdapter;
    private boolean firsttime = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean loading = true;
    private int skipdata = 0;

    public MessageFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageFragment = this;
        mDataProvider = getArguments().getString(ARG_DATA_PROVIDER);
        mCanSwipeLeft = getArguments().getBoolean(ARG_CAN_SWIPE_LEFT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.message_fragment_recycler_list_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);

        // swipe manager
        mRecyclerViewSwipeManager = new RecyclerViewSwipeManager();
        mProvider = getDataProvider();
        //adapter
        myItemAdapter = new MessageSwipeableItemAdapter2(mProvider, mCanSwipeLeft, getActivity());
        mAdapter = myItemAdapter;

        myItemAdapter.setEventListener(new MessageSwipeableItemAdapter2.EventListener() {
            @Override
            public void onItemRemoved(int position) {
                ((LndNotificationMessageActivity) getActivity()).onItemRemoved(position);
            }

            @Override
            public void onItemPinned(int position) {
                //((ViewPagerSwipeableExampleActivity) getActivity()).onItemPinned(position);
            }

            @Override
            public void onItemViewClicked(View v, boolean pinned) {
                // onItemViewClick(v, pinned);
            }
        });

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
            //mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z1)));
        }
        // mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));

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
        //load more
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

                             getData();
                            //Toast.makeText(getActivity(), "called", Toast.LENGTH_LONG).show();
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

    public AbstractDataProvider2 getDataProvider() {
        return ((LndNotificationMessageActivity) getActivity()).getDataProvider2(mDataProvider);
    }

    public void notifyItemChanged(int position) {
        mAdapter.notifyItemChanged(position);
    }

    public void notifyItemInserted(int position) {

        mAdapter.notifyItemInserted(position);
        mRecyclerView.scrollToPosition(position);
    }


    public void getData() {


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_INBOXOPERATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Log.e("response", response.toString());
                try {

                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");
                    if (jarray.length() == 0) {
                        if (firsttime) {
                            firsttime = false;
                            ((LndNotificationMessageActivity) getActivity()).showInstruction(2);
                            showInstruction();
                        }
                        return;

                    }
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jo = jarray.getJSONObject(i);
                        MessageData cd = new MessageData();
                        cd.setProfilepic(jo.getString("profile_pic"));
                        cd.setMsgindicator(jo.getInt("msg_status"));
                        cd.setUname(jo.getString("uname"));
                        cd.setMessage(jo.getString("msg"));
                        cd.setMsgid(jo.getInt("msg_id"));
                        cd.setSender_id(jo.getString("sender_id"));
                        cd.setDatetime(jo.getString("time"));
                        cd.setTimeago(TimeAgo.getMilliseconds(jo.getString("time")));


                        mProvider.addItem(cd);
                        mAdapter.notifyDataSetChanged();

                    }
                    skipdata = mProvider.getCount();
                    if (jarray.length() < 25)
                        loading = false;
                    else
                        loading = true;
                    firsttime = false;
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //  Log.e("response",error.getMessage()+"");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "4");
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

    public void updateList(Bundle extra) {

        MessageData md = mProvider.getItem(extra.getInt("pos")).getMessage();
        md.setMessage(extra.getString("message"));
        md.setTimeago(TimeAgo.getMilliseconds(extra.getString("time")));
        md.setDatetime(extra.getString("time"));

        mAdapter.notifyDataSetChanged();
        order(mProvider.getList());
        mAdapter.notifyDataSetChanged();

    }

    private void order(List<ConcreteData1> list) {

        Collections.sort(list, byDate);
    }

    final Comparator<ConcreteData1> byDate = new Comparator<ConcreteData1>() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public int compare(ConcreteData1 ord1, ConcreteData1 ord2) {

            try {

                if (ord1.getMessage().getTimeago() > ord2.getMessage().getTimeago()) {


                    return -1;
                } else {


                    return 1;

                }
            } catch (Exception ex) {
                // TODO Auto-generated catch block
                Log.e("error", ex.getMessage());

            }


            // return (d1.getTime() > d2.getTime() ? -1 : 1);     //descending
            //Log.e("satus",(d1.getTime() > d2.getTime() ? 1 : -1)+"");
            return 0;     //ascending
        }
    };
}

