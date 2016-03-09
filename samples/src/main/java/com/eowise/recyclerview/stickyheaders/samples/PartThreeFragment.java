package com.eowise.recyclerview.stickyheaders.samples;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import com.eowise.recyclerview.stickyheaders.samples.adapters.MarginDecoration;
import com.eowise.recyclerview.stickyheaders.samples.adapters.NumberedAdapter;
import com.eowise.recyclerview.stickyheaders.samples.listeners.OnLoadMoreListener;
import com.eowise.recyclerview.stickyheaders.samples.data.ShopData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PartThreeFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    RecyclerView recyclerv;
    ArrayList<ShopData> shopdata  = new ArrayList<>();
    private NumberedAdapter adapter;
    protected Handler handler;
    private  int skipdata=0;
    private  boolean loadmore=false;
    private boolean dataleft=true;

    public static PartThreeFragment createInstance(int itemsCount) {
        PartThreeFragment partThreeFragment = new PartThreeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View recyclerView =  inflater.inflate(
                R.layout.fragment_lnd_shop, container, false);
        setupRecyclerView(recyclerView);
        handler = new Handler();

        getData(skipdata);
        return recyclerView;
    }

    private void setupRecyclerView(View recyclerView) {
         recyclerv = (RecyclerView) recyclerView.findViewById(R.id.recycler_view);
        recyclerv.addItemDecoration(new MarginDecoration(getActivity()));
        recyclerv.setHasFixedSize(true);
        recyclerv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        adapter=new NumberedAdapter(shopdata, getActivity(), 200, recyclerv);
        recyclerv.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                shopdata.add(null);
                adapter.notifyItemInserted(shopdata.size() - 1);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadmore = true;
                        if (dataleft)
                            getData(skipdata);
                        else {

                            shopdata.remove(shopdata.size() - 1);

                            adapter.notifyItemRemoved(shopdata.size());

                            adapter.setLoaded();

                        }
                    }
                }, 1000);

            }
        });

    }

    public  void getData(final int dataskip){



        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST,"http://52.76.68.122/lnd/postdata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             //   LndShop.prog.setVisibility(View.GONE);
                if(loadmore)
                {
                    shopdata.remove(shopdata.size() - 1);

                    adapter.notifyItemRemoved(shopdata.size());

                    adapter.setLoaded();

                }
                Log.e("response", response.toString());
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray=jobj.getJSONArray("data");
                    if(jarray.length()==0) {
                        dataleft = false;
                        return;
                    }
                        for(int i=0;i<jarray.length();i++)
                    {
                        JSONObject jo=jarray.getJSONObject(i);
                        ShopData pdb = new ShopData();
                        pdb.setPrice(jo.getString("price_now"));


                        pdb.setImageurl(jo.getString("image_url"));
                        shopdata.add(pdb);
                    }


                   // rv.setAdapter(adapter);
                    skipdata=shopdata.size();
                     adapter.notifyDataSetChanged();
                }
                catch(Exception ex)
                {
                    Log.e("json parsing error",ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //LndShop.prog.setVisibility(View.GONE);
               try {
                   shopdata.remove(shopdata.size() - 1);

                   adapter.notifyItemRemoved(shopdata.size());

                   adapter.setLoaded();
               }
               catch(Exception ex)
               {

               }
                Log.e("response",error.getMessage()+"");
               try {
                   new com.eowise.recyclerview.stickyheaders.samples.AlertDialog().showAlertDialog(getActivity());
               }
               catch(Exception ex)
               {

               }
               }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("rqid","5");
                params.put("uname","");

                params.put("skipdata",dataskip+"");


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
