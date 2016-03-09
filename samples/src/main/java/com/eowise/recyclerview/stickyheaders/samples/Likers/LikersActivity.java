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
import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
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
        heading.setTypeface(ImageLoaderImage.hfont);
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
        String userid = "",postid="";
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            userid = extra.getString("user_id", "");
            postid=extra.getString("postid","");
        }
        getLikers(userid,postid);
    }

    public void getLikers(final String userid,final String postid) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/lndlikeunlike.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
             //  Log.e("fuck", response.toString());
             //    Toast.makeText(LikersActivity.this,postid+","+uname,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jsonArray = jobj.getJSONArray("data");
                   // Toast.makeText(LikersActivity.this,uname+","+postid,Toast.LENGTH_LONG).show();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        FollowersFollowingData fd = new FollowersFollowingData();
                        fd.setUname(jsonObject.getString("uname"));
                        fd.setUserpic(jsonObject.getString("user_pic"));
                        fd.setStatus(0+"");
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
                params.put("user_id",userid);
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
