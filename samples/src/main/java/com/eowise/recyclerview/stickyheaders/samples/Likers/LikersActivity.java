package com.eowise.recyclerview.stickyheaders.samples.Likers;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.adapters.FollowersAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.FollowersFollowingData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LikersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<FollowersFollowingData> data = new ArrayList<FollowersFollowingData>();
    private FollowersAdapter recyclerAdapter;
    @Bind(R.id.heading)
    TextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        ButterKnife.bind(this);
        //setting custom font
        heading.setTypeface(SingleTon.hfont);
        heading.setText("Likers");
        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        // mLayoutManager.
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerAdapter = new FollowersAdapter(this, data);
        recyclerView.setAdapter(recyclerAdapter);
        //reading bundle
        String postid="";
        Bundle extra = getIntent().getExtras();
        if (extra != null) {

            postid=extra.getString("postid","");
        }
        getLikers(postid);
    }

    public void getLikers(final String postid) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/lndlikeunlike.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                // Log.e("response", response);
               String userid= SingleTon.pref.getString("user_id","");
                 try {
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jsonArray = jobj.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        FollowersFollowingData fd = new FollowersFollowingData();
                        fd.setUname(jsonObject.getString("uname"));
                        fd.setUserpic(jsonObject.getString("user_pic"));
                        fd.setUserid(jsonObject.getString("userid"));
                        if(userid.compareTo(fd.getUserid())==0)
                        fd.setStatus(-1+"");
                        else
                        fd.setStatus(jsonObject.getString("check_val"));
                        data.add(fd);
                    }
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                }
                recyclerAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                //  Log.e("response",error.getMessage()+"");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "1");
                params.put("user_id", SingleTon.pref.getString("user_id",""));
                params.put("post_id", postid);


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
