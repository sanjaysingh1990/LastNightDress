package com.eowise.recyclerview.stickyheaders.samples.Followers;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class FollowersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<FollowersFollowingData> data = new ArrayList<FollowersFollowingData>();
    private FollowersAdapter recyclerAdapter;
    private int skipdata=0;
    private boolean canrequest=true;
    @Bind(R.id.heading)
    TextView heading;
    private boolean isfollowedunfollowed=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        ButterKnife.bind(this);
        //setting custom font
        heading.setTypeface(SingleTon.hfont);
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
        String userid = "";
        Bundle extra = getIntent().getExtras();
        if (extra != null)
            userid = extra.getString("user_id", "");
        int userprofile = extra.getInt("userp");
        if (userprofile == 1)
            getFollowers(userid, 1);
        else
            getFollowers(userid, 7);

    }

    public void getFollowers(final String userid, final int rqid) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/followfollowing.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                String userid= SingleTon.pref.getString("user_id","");

                //  Log.e("response", response.toString());
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jsonArray = jobj.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        FollowersFollowingData fd = new FollowersFollowingData();
                        fd.setUname(jsonObject.getString("uname"));
                        fd.setUserpic(jsonObject.getString("user_pic"));
                        fd.setUserid(jsonObject.getString("followerid"));
                        if(userid.compareTo(fd.getUserid())==0)
                            fd.setStatus(-1+"");
                        else
                            fd.setStatus(jsonObject.getString("check"));
                        data.add(fd);
                    }
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                }
                skipdata=data.size();
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
                params.put("rqid", rqid + "");
                params.put("user_id", userid);
                params.put("skipdata",skipdata+"");
                params.put("other_userid", SingleTon.pref.getString("user_id",""));

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
        Intent intent=new Intent();
        intent.putExtra("check",isfollowedunfollowed);
        setResult(7, intent);
        finish();//finishing activity
    }

    public void back(View v) {
        onBackPressed();
    }

    public void changeValue()
    {
        isfollowedunfollowed=true;
    }

}
