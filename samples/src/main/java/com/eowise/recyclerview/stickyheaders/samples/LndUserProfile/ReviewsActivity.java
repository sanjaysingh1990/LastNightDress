package com.eowise.recyclerview.stickyheaders.samples.LndUserProfile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.adapters.ReviewsAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.ReviewsData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReviewsActivity extends AppCompatActivity {
    private List<ReviewsData> itemList = new ArrayList<ReviewsData>();
    @Bind(R.id.loader) AVLoadingIndicatorView prog;
    @Bind(R.id.list) RecyclerView recyclerView;
    @Bind(R.id.heading)TextView heading;
    ReviewsAdapter recyclerAdapter;
    int skipdata=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        heading.setTypeface(SingleTon.robotobold);
        recyclerAdapter = new ReviewsAdapter(this, itemList);
        recyclerView.setAdapter(recyclerAdapter);


        Bundle extra=getIntent().getExtras();
        if(extra!=null)
        {
            String userid=extra.getString("user_id","");
            getData(userid);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void back(View v)
    {
        onBackPressed();
    }

    public  void getData(final String userid){


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://52.76.68.122/lnd/androidiosphpfiles/lndusers.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                prog.setVisibility(View.GONE);

                 Log.e("reviews", response.toString());
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray=jobj.getJSONArray("data");

                    for(int i=0;i<jarray.length();i++)
                    {
                        JSONObject jo=jarray.getJSONObject(i);
                        ReviewsData rd1 = new ReviewsData();
                        rd1.setRated_value(jo.getInt("rated_value"));
                        rd1.setReviewbyuname(jo.getString("byuname"));
                        rd1.setReviewmessage(jo.getString("review_message"));
                        rd1.setReviewreplied(jo.getString("review_replied"));
                        if(rd1.getReviewreplied().length()>0)
                        rd1.setIsreplied(true);
                        else
                        rd1.setIsreplied(false);
                        rd1.setReviewid(jo.getString("review_id"));
                        rd1.setProfilepic(jo.getString("image_url"));
                        itemList.add(rd1);
                    }


                    skipdata=itemList.size();
                    recyclerAdapter.notifyDataSetChanged();
                   /* if(jarray.length()==0)
                        loading=false;
                    else
                        loading=true;*/
                }
                catch(Exception ex)
                {
                    //Log.e("json parsing error",ex.getMessage());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prog.setVisibility(View.GONE);
                //  Log.e("response",error.getMessage()+"");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("rqid","15");
                params.put("skipdata",0+"");
                params.put("user_id",userid);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

}