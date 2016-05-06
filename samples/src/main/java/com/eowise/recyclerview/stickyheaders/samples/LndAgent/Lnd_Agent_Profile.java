package com.eowise.recyclerview.stickyheaders.samples.LndAgent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.data.LndAgentBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Lnd_Agent_Profile extends AppCompatActivity {
    @Bind(R.id.recycler)
    RecyclerView recyclerView;
    @Bind(R.id.header)
    TextView header;
    ArrayList<LndAgentBean> data = new ArrayList<>();
    AgentListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lnd__agent__profile);
        ButterKnife.bind(this);
        adapter = new AgentListAdapter(this, data);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        header.setTypeface(SingleTon.robotobold);
        header.setText(Capitalize.capitalize(SingleTon.pref.getString("uname", "")));
        setHeader();
        getData();

    }

    private void setHeader() {

        LndAgentBean header = new LndAgentBean();
        header.setType(0);
        data.add(header);

        adapter.notifyDataSetChanged();
    }

    public void getData() {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/postdata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean normaluser = true, agentuser = true, agencyuser = true, areamanager = true, regionalmanager = true, salesdirector = true;

                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jsonArray = jobj.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        LndAgentBean agent = new LndAgentBean();
                        switch (jsonObject.getInt("user_position")) {

                            case 1:
                                if (normaluser) {
                                    LndAgentBean basicuserheader = new LndAgentBean();
                                    basicuserheader.setType(1);
                                    basicuserheader.setHeaderType(1);
                                    basicuserheader.setTotal(jsonObject.getString("total_basicuser"));

                                    normaluser = false;
                                    data.add(basicuserheader);

                                }

                                break;


                            case 2:
                                if (agentuser) {
                                    LndAgentBean agentheader = new LndAgentBean();
                                    agentheader.setType(1);
                                    agentheader.setHeaderType(2);
                                    agentheader.setTotal(jsonObject.getString("total_agent"));
                                    agentuser = false;
                                    data.add(agentheader);

                                }
                               break;

                            case 3:
                                if (agencyuser) {
                                    LndAgentBean agencyheader = new LndAgentBean();
                                    agencyheader.setType(1);
                                    agencyheader.setHeaderType(3);
                                    agencyheader.setTotal(jsonObject.getString("total_agency"));
                                    agencyuser = false;
                                    data.add(agencyheader);

                                }


                                break;


                            case 4:
                                if (areamanager) {
                                    LndAgentBean areamanagerheader = new LndAgentBean();
                                    areamanagerheader.setType(1);
                                    areamanagerheader.setHeaderType(4);
                                    areamanagerheader.setTotal(jsonObject.getString("total_areamanager"));
                                    areamanager = false;
                                    data.add(areamanagerheader);

                                }


                                break;


                            case 5:
                                if (regionalmanager) {
                                    LndAgentBean regionaldirectorheader = new LndAgentBean();
                                    regionaldirectorheader.setType(1);
                                    regionaldirectorheader.setHeaderType(5);
                                    regionaldirectorheader.setTotal(jsonObject.getString("total_regionaldirector"));
                                    regionalmanager = false;
                                    data.add(regionaldirectorheader);

                                }


                            break;

                        }
                        if (jsonObject.getString("user_type").equals("private"))
                            agent.setType(2);
                        else
                            agent.setType(3);

                        agent.setUname(jsonObject.getString("uname"));
                        agent.setTotalpost(jsonObject.getString("total_posts"));
                        agent.setTotalsales(jsonObject.getString("total_sales"));
                        agent.setTotalrefuser(jsonObject.getString("total_ref_user"));
                        agent.setProfilepic(jsonObject.getString("profile_pic"));
                        agent.setUserposition(jsonObject.getInt("user_position"));
                        data.add(agent);
                    }
                    adapter.notifyDataSetChanged();

                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage() + "");
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
                params.put("rqid", "19");
                params.put("ref_code", 233 + "");

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
}
