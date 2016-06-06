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
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.data.LndAgentBean;

import org.json.JSONArray;
import org.json.JSONException;
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
private void addUser(JSONObject jsonObject) throws Exception
{

        LndAgentBean agent = new LndAgentBean();
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


    private void addHeader(int headertype,String totalcount) throws Exception
    {

        LndAgentBean regionaldirectorheader = new LndAgentBean();
        regionaldirectorheader.setType(1);
        regionaldirectorheader.setHeaderType(headertype);
        regionaldirectorheader.setTotal(totalcount);

        data.add(regionaldirectorheader);
    }
    public void getData() {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_POST_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jobj = new JSONObject(response.toString());
                     boolean status=jobj.getBoolean("status");
                    if(status)
                    {

                        //for user
                      LndAgentBean agentbean=  data.get(0);
                      int totalagents=jobj.getInt("total_agent")+jobj.getInt("total_basicuser")+jobj.getInt("total_agency")+jobj.getInt("total_areamanager") +jobj.getInt("total_regionaldirector");
                      agentbean.setUsertotalagents(totalagents+"");
                      agentbean.setUsertotalshops(jobj.getInt("total_shops")+"");
                       //for regiional director
                        JSONArray regionaldirector=jobj.getJSONArray("regionaldirector");
                        if(regionaldirector.length()>0)
                        {
                            addHeader(5,jobj.getString("total_regionaldirector"));
                        }
                        for(int i=0;i<regionaldirector.length();i++)
                        {
                           addUser(regionaldirector.getJSONObject(i));
                        }
                        //for area manager
                        JSONArray areamanager=jobj.getJSONArray("areamanager");
                        if(areamanager.length()>0)
                        {
                            addHeader(4,jobj.getString("total_areamanager"));
                        }
                        for(int i=0;i<areamanager.length();i++)
                        {
                            addUser(areamanager.getJSONObject(i));
                        }

                        //for agency
                        JSONArray agency=jobj.getJSONArray("agency");
                        if(agency.length()>0)
                        {
                            addHeader(3,jobj.getString("total_agency"));
                        }
                        for(int i=0;i<agency.length();i++)
                        {
                            addUser(agency.getJSONObject(i));
                        }

                        //for agent
                        JSONArray agent=jobj.getJSONArray("agents");
                        if(agent.length()>0)
                        {
                            addHeader(2,jobj.getString("total_agent"));
                        }
                        for(int i=0;i<agent.length();i++)
                        {
                            addUser(agent.getJSONObject(i));
                        }
                        //for agent
                        JSONArray basicuser=jobj.getJSONArray("basicuser");
                        if(agent.length()>0)
                        {
                            addHeader(1,jobj.getString("total_basicuser"));
                        }
                        for(int i=0;i<basicuser.length();i++)
                        {
                            addUser(basicuser.getJSONObject(i));
                        }
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
                params.put("rqid", "20");
                params.put("ref_code", 233+"");

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
