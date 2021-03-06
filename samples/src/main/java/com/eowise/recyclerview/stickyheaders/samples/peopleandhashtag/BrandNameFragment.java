package com.eowise.recyclerview.stickyheaders.samples.peopleandhashtag;

import android.os.Bundle;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.adapters.PeopleBrandHashTapAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.PeopleData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BrandNameFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    private List<PeopleData> itemList = new ArrayList<PeopleData>();
    private PeopleBrandHashTapAdapter recyclerAdapter;
    static String previouskeyword = "";

    public static BrandNameFragment createInstance(int itemsCount) {
        BrandNameFragment partThreeFragment = new BrandNameFragment();
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

        //on text change
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
                if (PeopleHashTagActivity.viewPager.getCurrentItem() == 1) {
                    if (s.length() == 0) {
                        // intialize();
                    } else

                        getData(s.toString());

                }
            }
        });
        return recyclerView;
    }


    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAdapter = new PeopleBrandHashTapAdapter(getActivity(), itemList);
        recyclerView.setAdapter(recyclerAdapter);
    }


    public void getData(final String keyword) {

        itemList.clear();
        PeopleData pd = new PeopleData();
        pd.setType(0);
        itemList.add(pd);
        if (recyclerAdapter != null)
            recyclerAdapter.notifyDataSetChanged();
        RequestQueue queue = Volley.newRequestQueue(PeopleHashTagActivity.act);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/readname.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                // Log.e("response hashtag", response.toString());
                try {
                    itemList.clear();
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jo = jarray.getJSONObject(i);
                        PeopleData brand = new PeopleData();
                        brand.setBrandname(jo.getString("brand_name"));
                        brand.setTotal(jo.getString("total"));
                        brand.setType(3);
                        itemList.add(brand);
                    }

                    if (jarray.length() == 0) {
                        PeopleData pd = new PeopleData();

                        pd.setType(2);
                        itemList.add(pd);

                    }
                    if (recyclerAdapter != null)
                        recyclerAdapter.notifyDataSetChanged();


                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
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
                params.put("rqid", "3");
                params.put("skipdata", "0");
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
        Log.e("frag","brand name fragment");
        String hashtag = PeopleHashTagActivity.Search.getText() + "";

        if (hashtag.length() > 0 && hashtag.compareTo(previouskeyword) != 0) {
            previouskeyword = hashtag;
            getData(hashtag);

        }
    }
}
