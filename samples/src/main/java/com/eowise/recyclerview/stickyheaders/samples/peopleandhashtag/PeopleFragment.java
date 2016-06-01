package com.eowise.recyclerview.stickyheaders.samples.peopleandhashtag;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.adapters.PeopleBrandHashTapAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.PeopleData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PeopleFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
     List<PeopleData> itemList = new ArrayList<PeopleData>();
     List<PeopleData> itemList2 = new ArrayList<PeopleData>();
  //   private TextView showinfo;
    static PeopleBrandHashTapAdapter recyclerAdapter;
    static RecyclerView recyclerView;
    public static PeopleFragment createInstance(int itemsCount) {
        PeopleFragment partThreeFragment = new PeopleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.people_fragment, container, false);
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);
       // showinfo=(TextView)view.findViewById(R.id.showinfo);
        setupRecyclerView(recyclerView);


        PeopleHashTagActivity.Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //showinfo.setVisibility(View.GONE);

                // final List<PeopleData> filteredModelList = filter(itemList2, s.toString());
                //PeopleFragment.recyclerAdapter.animateTo(filteredModelList);
                // PeopleFragment.recyclerView.scrollToPosition(0);

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (PeopleHashTagActivity.viewPager.getCurrentItem() == 0) {
                    if (s.length() == 0) {
                        intialize();
                    } else

                        getData(s.toString());

                }
            }
        });



        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
         recyclerAdapter = new PeopleBrandHashTapAdapter(getActivity(), itemList);
        recyclerView.setAdapter(recyclerAdapter);

       /* recyclerAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom


                itemList.add(null);
                recyclerAdapter.notifyItemInserted(itemList.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                            try {
                                itemList.remove(itemList.size() - 1);
                            }
                            catch(Exception ex)
                            {

                            }
                                recyclerAdapter.notifyItemRemoved(itemList.size());
                            //add items one by one

                            recyclerAdapter.setLoaded();
                            //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();


                        }
                }, 2000);

            }
        });*/
      //  intialize();
    }

    private void intialize()
    {
        itemList.clear();
        recyclerAdapter.notifyDataSetChanged();

        List<PeopleData> users = SingleTon.db.getAllUsers();

        for (PeopleData user : users)
        {
            PeopleData peopledata=new PeopleData();
            peopledata.setUserid(user.getUserid());

            peopledata.setUname(user.getUname());
            peopledata.setImageurl(user.getImageurl());

            peopledata.setType(1);
            itemList.add(peopledata);


           }

        recyclerAdapter.notifyDataSetChanged();
        }

    public void getData(final String keyword) {

//clear user list
        itemList.clear();
        PeopleData pd=new PeopleData();
        pd.setType(0);
        itemList.add(pd);
        recyclerAdapter.notifyDataSetChanged();
        RequestQueue queue = Volley.newRequestQueue(PeopleHashTagActivity.act);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/readname.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                  // Log.e("data", response.toString());
               itemList.clear();


                try {

                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");
                    for (int i = 0; i < jarray.length(); i++)
                    {
                        JSONObject jo = jarray.getJSONObject(i);
                        PeopleData pd = new PeopleData();
                        pd.setUname(jo.getString("uname"));
                        pd.setImageurl(jo.getString("imgurl"));
                        pd.setUserid(jo.getString("user_id"));

                        pd.setType(1);
                        itemList.add(pd);

                    }
                    if(jarray.length()==0)
                    {
                        PeopleData pd=new PeopleData();

                        pd.setType(2);
                        itemList.add(pd);

                    }

                       recyclerAdapter.notifyDataSetChanged();

                }
                catch (Exception ex)
                {
                    Log.e("response", ex.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("response", error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "5");
                params.put("keyword", keyword);

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

    @Override
    public void onResume() {
        super.onResume();
        //checking on swipe
        String people=PeopleHashTagActivity.Search.getText()+"";
        if(people.length()>0)
            getData(people);
        //load from database
        intialize();
    }
}