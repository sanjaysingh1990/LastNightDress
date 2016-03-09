package com.eowise.recyclerview.stickyheaders.samples.TabDemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.eowise.recyclerview.stickyheaders.samples.Favorates.Favorates;
import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SQLDB.FavoriteData;
import com.eowise.recyclerview.stickyheaders.samples.adapters.MarginDecoration;
import com.eowise.recyclerview.stickyheaders.samples.adapters.NumberedAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.ShopData;
import com.eowise.recyclerview.stickyheaders.samples.listeners.OnLoadMoreListener;
import com.eowise.recyclerview.stickyheaders.samples.peopleandhashtag.PeopleHashTagActivity;
import com.example.sanju.pullrefresh.CustomSwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LndFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    RecyclerView recyclerv;
    public static ArrayList<ShopData> shopdata = new ArrayList<>();
    private NumberedAdapter adapter;
    protected Handler handler;
    public int skipdata = 0;
    private boolean loadmore = false;
    private boolean dataleft = true;
    private int ft = 0;
    @Bind(R.id.search)
    TextView search;
    static ImageButton favorate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View shopview = inflater.inflate(
                R.layout.fragment_lnd_shop, container, false);
        ButterKnife.bind(this, shopview);
        search.setHint("Search People, Brands & Hashtags");
        search.setTypeface(ImageLoaderImage.robotoregular);
        setupRecyclerView(shopview);
        handler = new Handler();
        try {
            shopdata.clear();
            getData(skipdata);


        } catch (Exception ex) {

        }

        //favorate button reference
        favorate = (ImageButton) shopview.findViewById(R.id.favorate);
        shopview.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent peoplebrandhashtag = new Intent(getActivity(), PeopleHashTagActivity.class);
                startActivity(peoplebrandhashtag);
            }
        });
        shopview.findViewById(R.id.favorate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favorate = new Intent(getActivity(), Favorates.class);
                startActivity(favorate);
            }
        });

        //checking favorate
        checkFavorate();

        return shopview;
    }

    private void setupRecyclerView(View recyclerView) {
        recyclerv = (RecyclerView) recyclerView.findViewById(R.id.recycler_view);
        recyclerv.addItemDecoration(new MarginDecoration(getActivity()));
        recyclerv.setHasFixedSize(true);
        recyclerv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        adapter = new NumberedAdapter(shopdata, getActivity(), 200, recyclerv);
        recyclerv.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                if (ft == 0) {
                    ft = 1;
                    return;
                }
                shopdata.add(null);
                adapter.notifyItemInserted(shopdata.size() - 1);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadmore = true;
                        if (dataleft)
                            try {
                                getData(skipdata);
                            } catch (Exception ex) {

                            }
                        else {

                            shopdata.remove(shopdata.size() - 1);

                            adapter.notifyItemRemoved(shopdata.size() - 1);

                            adapter.setLoaded();

                        }
                    }
                }, 1000);

            }
        });

    }

    public void getData(final int dataskip) throws Exception {
        if (!loadmore) {
            LndShopActivity.prog.setVisibility(View.VISIBLE);
        }


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/postdata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                LndShopActivity.prog.setVisibility(View.GONE);
                if (loadmore) {
                    shopdata.remove(shopdata.size() - 1);

                    adapter.notifyItemRemoved(shopdata.size());

                    adapter.setLoaded();

                }
                //Log.e("response", response.toString());
                try {

                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");
                    if (jarray.length() == 0) {
                        dataleft = false;
                        return;
                    }
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jo = jarray.getJSONObject(i);
                        ShopData pdb = new ShopData();
                        pdb.setPrice(jo.getString("price_now"));


                        pdb.setImageurl(jo.getString("image_url"));
                        shopdata.add(pdb);
                    }


                    // rv.setAdapter(adapter);
                    skipdata = shopdata.size();
                    adapter.notifyDataSetChanged();
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LndShopActivity.prog.setVisibility(View.GONE);
                /*try {
					shopdata.remove(shopdata.size() - 1);

					adapter.notifyItemRemoved(shopdata.size());

					adapter.setLoaded();
				}
				catch(Exception ex)
				{

				}*/
                //Log.e("response",error.getMessage()+"");
                try {
                    new com.eowise.recyclerview.stickyheaders.samples.AlertDialog().showAlertDialog(getActivity());
                } catch (Exception ex) {

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "5");
                params.put("uname", "");

                params.put("skipdata", dataskip + "");


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

    public static void checkFavorate() {
        try {
            List<FavoriteData> fav = ImageLoaderImage.db.getAllContacts();

            if (fav.size() > 0) {
                favorate.setImageResource(R.drawable.filled_favorate_icon2);
            } else
                favorate.setImageResource(R.drawable.empty_favorate_icon);
        } catch (Exception ex)


        {

        }
    }
}
