package com.eowise.recyclerview.stickyheaders.samples.LndMessage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.adapters.NotificationAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.NotificationData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NotificationFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    private List<NotificationData> notiitems=new ArrayList<NotificationData>();
    public static NotificationAdapter recyclerAdapter;

    public static NotificationFragment createInstance(int itemsCount) {
        NotificationFragment partThreeFragment = new NotificationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.notification_fragment, container, false);
        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);

        setupRecyclerView(recyclerView);
        getData();
        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAdapter = new NotificationAdapter(getActivity(),notiitems);
        recyclerView.setAdapter(recyclerAdapter);
    }



    public void getData() {


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/inboxope.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response.toString());
                try {
                   MessageActivity.prog.setVisibility(View.GONE);
                //    pDialog.dismiss();
                }
                catch(Exception ex)
                {

                }

                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray=jobj.getJSONArray("data");
                    for(int i=0;i<jarray.length();i++)
                    {
                        JSONObject jo=jarray.getJSONObject(i);
                        NotificationData nd=new NotificationData();
                        nd.setPostid(jo.getString("post_id"));
                        nd.setUname(jo.getString("uname"));
                        nd.setReceiverid(jo.getString("receivername"));
                        nd.setNotification_id(jo.getString("notification_id"));
                        nd.setSwapstatus(jo.getString("swapstatus"));
                        nd.setProfilepicimg(jo.getString("profile_pic"));
                        nd.setTime("5m");
                        nd.setComment(jo.getString("comment"));
                        nd.setNotitype(jo.getString("noti_type"));
                        String imgurl=jo.getString("image_url");
                        String[] imageurls=imgurl.split(",");
                        nd.setImgurl(imageurls[0]);
                        notiitems.add(nd);
                    }

                  recyclerAdapter.notifyDataSetChanged();


                }
                catch(Exception ex)
                {
                    Log.e("json parsing error",ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageActivity.prog.setVisibility(View.GONE);
               // Log.e("response", error.getMessage() + "");
             try {
                 new com.eowise.recyclerview.stickyheaders.samples.AlertDialog().showAlertDialog(getActivity());
             }
             catch(Exception ex)
             {

             }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "5");
                params.put("uname", "o");

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