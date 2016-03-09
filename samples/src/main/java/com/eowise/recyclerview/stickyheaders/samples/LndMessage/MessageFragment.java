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
import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.adapters.MessageAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.MessageData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MessageFragment extends Fragment {
    MessageAdapter recyclerAdapter;
    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    List<MessageData> itemList = new ArrayList<MessageData>();
    public static MessageFragment createInstance(int itemsCount) {
        MessageFragment partThreeFragment = new MessageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.message_fragment, container, false);
        setupRecyclerView(recyclerView);
        getData();
        return recyclerView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
         recyclerAdapter = new MessageAdapter(getActivity(),itemList);
        recyclerView.setAdapter(recyclerAdapter);
    }


    public  void getData(){


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST,"http://52.76.68.122/lnd/androidiosphpfiles/inboxope.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Log.e("response", response.toString());
                try {
                    itemList.clear();
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray=jobj.getJSONArray("data");
                    for(int i=0;i<jarray.length();i++)
                    {
                        JSONObject jo=jarray.getJSONObject(i);
                        MessageData cd=new MessageData();
                        cd.setProfilepic(jo.getString("imgurl"));
                        cd.setMsgindicator(jo.getString("msg_status"));
                        cd.setUname(jo.getString("uname"));
                        cd.setMessage(jo.getString("msg"));
                        cd.setMsgid(jo.getString("msg_id"));
                        cd.setSender_id(jo.getString("sender_id"));
                        cd.setDatetime(jo.getString("time"));




                        itemList.add(cd);
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

              //  Log.e("response",error.getMessage()+"");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("rqid","4");
                params.put("user_id", ImageLoaderImage.pref.getString("user_id",""));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }
}
