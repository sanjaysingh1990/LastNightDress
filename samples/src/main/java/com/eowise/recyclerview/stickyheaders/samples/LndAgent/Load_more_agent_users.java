package com.eowise.recyclerview.stickyheaders.samples.LndAgent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.data.LndAgentBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Load_more_agent_users extends AppCompatActivity {
    @Bind(R.id.recycler)
    RecyclerView recyclerView;
    @Bind(R.id.header)
    TextView header;
    @Bind(R.id.loader)
    AVLoadingIndicatorView loader;
    ArrayList<LndAgentBean> data = new ArrayList<>();
    AgentListAdapter adapter;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager layoutManager;
    private boolean loading = true;
    private int skipdata = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_more_agent_users);
        ButterKnife.bind(this);
        adapter = new AgentListAdapter(this, data);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        header.setTypeface(SingleTon.robotobold);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            int type = extra.getInt("usertype", 0);
            if (type == 1)
                header.setText("Basic User");
            else if (type == 2)
                header.setText("Agent");
            else if (type == 3)
                header.setText("Agency");

            else if (type == 4)
                header.setText("Area Manager");

            else if (type == 5)
                header.setText("Regional Director");

        }
        //load more
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            getData(1);

                        }
                    }
                }
            }
        });

        getData(1);
    }

    public void getData(final int usertype) {

        loader.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_POST_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    loader.setVisibility(View.GONE);
                    JSONObject jobj = new JSONObject(response.toString());
                    boolean status = jobj.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jobj.getJSONArray("users");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            addUser(jsonObject);
                        }
                        skipdata = data.size();
                        if (jsonArray.length() < 25)
                            loading = false;
                        else
                            loading = true;
                    }


                    adapter.notifyDataSetChanged();

                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.setVisibility(View.GONE);

                Log.e("response", error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "21");
                params.put("user_type", usertype + "");
                params.put("ref_code", SingleTon.pref.getString("ref_code", ""));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void back(View v) {
        onBackPressed();
    }

    private void addUser(JSONObject jsonObject) throws Exception {

        LndAgentBean agent = new LndAgentBean();
        if (jsonObject.getString("user_type").compareToIgnoreCase("shop") == 0)
            agent.setType(3);
        else
            agent.setType(2);

        agent.setUname(jsonObject.getString("uname"));
        agent.setTotalpost(jsonObject.getString("total_posts"));
        agent.setTotalsales(jsonObject.getString("total_sales"));
        agent.setTotalrefuser(jsonObject.getString("total_ref_user"));
        agent.setProfilepic(jsonObject.getString("profile_pic"));
        agent.setUserposition(jsonObject.getInt("user_position"));
        data.add(agent);
    }
}
