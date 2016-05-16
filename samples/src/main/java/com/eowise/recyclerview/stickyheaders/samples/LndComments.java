package com.eowise.recyclerview.stickyheaders.samples;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.adapters.LndCommentsAdapter;
import com.eowise.recyclerview.stickyheaders.samples.adapters.NewMessageAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.CommentData;
import com.eowise.recyclerview.stickyheaders.samples.data.MessageToFriendsData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import github.ankushsachdeva.emojicon.EmojiconEditText;

public class LndComments extends AppCompatActivity {

    @Bind(R.id.list)
    RecyclerView recyclerView;
    @Bind(R.id.loader)
    AVLoadingIndicatorView loader;
    @Bind(R.id.commentbox)
    EmojiconEditText commentbox;
    private List<CommentData> data = new ArrayList<CommentData>();
    private LndCommentsAdapter recyclerAdapter;
    private TextView heading;
    boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int skipdata = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lnd_comments);
        ButterKnife.bind(this);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerAdapter = new LndCommentsAdapter(this, data);
        recyclerView.setAdapter(recyclerAdapter);
        //reference here
        heading = (TextView) findViewById(R.id.heading);
        //applying custom font
        heading.setTypeface(SingleTon.hfont);
        getData();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = manager.getChildCount();
                    totalItemCount = manager.getItemCount();
                    pastVisiblesItems = manager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;

                            // getData();
                            Toast.makeText(LndComments.this, "called", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

    }

    public void getData() {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/lndcomments.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //   Log.e("response", response.toString());
                try {
                    loader.setVisibility(View.GONE);
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jo = jarray.getJSONObject(i);
                        CommentData cmntdata = new CommentData();
                        cmntdata.setUname(jo.getString("uname"));
                        cmntdata.setProfilepic(jo.getString("image_url"));
                        cmntdata.setCommenttxxt(jo.getString("comment_text"));
                        cmntdata.setTime(getMilliseconds(jo.getString("date_time")));

                        data.add(cmntdata);
                    }


                    recyclerAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(data.size()-1);
                    skipdata = data.size();
                    if (jarray.length() < 25)
                        loading = false;
                    else
                        loading = true;
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
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
                params.put("rqid", "0");
                params.put("postid", 13 + "");
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

    public void back(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    static long getMilliseconds(String datetime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            Date date = formatter.parse(datetime);
            // Log.e("date",date.toString());
            // Log.e("date2",formatter.format(date));

            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void send(View v) {
        String cmnttext = commentbox.getText().toString();
        if (cmnttext.length() == 0)
            return;
        CommentData cmntdata = new CommentData();
        cmntdata.setCommenttxxt(cmnttext);
        cmntdata.setTime(getMilliseconds(SingleTon.getCurrentTimeStamp()));
        cmntdata.setProfilepic(SingleTon.pref.getString("imageurl", ""));
        cmntdata.setUname(SingleTon.pref.getString("uname", ""));

        data.add(cmntdata);
        recyclerAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(data.size()-1);
        commentbox.setText("");
    }
}

