package com.eowise.recyclerview.stickyheaders.samples.LndAgent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.eowise.recyclerview.stickyheaders.samples.data.LndAgentBean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Lnd_Agent_Profile extends AppCompatActivity {
    @Bind(R.id.recycler)RecyclerView recyclerView;
    ArrayList<LndAgentBean> data=new ArrayList<>();
    AgentListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lnd__agent__profile);
        ButterKnife.bind(this);
         adapter=new AgentListAdapter(this,data);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
      getdata();

    }
    private void getdata()
    {

        LndAgentBean header=new LndAgentBean();
        header.setType(0);
        data.add(header);
        LndAgentBean db=new LndAgentBean();
        db.setType(1);
        data.add(db);
        LndAgentBean db2=new LndAgentBean();
        db2.setType(2);
        data.add(db2);
        LndAgentBean db3=new LndAgentBean();
        db3.setType(3);
        data.add(db3);
        LndAgentBean db8=new LndAgentBean();
        db8.setType(4);
        data.add(db8);
        LndAgentBean db4=new LndAgentBean();
        db4.setType(1);
        data.add(db4);
        LndAgentBean db5=new LndAgentBean();
        db5.setType(2);
        data.add(db5);
        LndAgentBean db6=new LndAgentBean();
        db6.setType(2);
        data.add(db6);
        LndAgentBean db7=new LndAgentBean();
        db7.setType(2);
        data.add(db7);
       adapter.notifyDataSetChanged();
    }
    public void getData() {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/postdata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             boolean normaluser=true;
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                  
                    switch(jobj.getInt("user_position"))
                    {

                        case 1:
                          if(normaluser)
                          {

                          }
                            break;


                        case 2:

                            break;


                        case 3:

                            break;


                        case 4:

                            break;


                        case 5:

                            break;
                    }
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
}
