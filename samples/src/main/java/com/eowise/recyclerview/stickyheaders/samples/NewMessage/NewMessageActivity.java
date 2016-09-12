package com.eowise.recyclerview.stickyheaders.samples.NewMessage;

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
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.adapters.NewMessageAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.MessageToFriendsData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewMessageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<MessageToFriendsData> data = new ArrayList<MessageToFriendsData>();
    private NewMessageAdapter recyclerAdapter;
    private TextView heading;
    boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int skipdata = 0;
    private EditText searchbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchbox= (EditText) findViewById(R.id.searchbox);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerAdapter = new NewMessageAdapter(this, data);
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
                            Toast.makeText(NewMessageActivity.this,"called",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
        addTextListener();


    }

    public void getData() {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/followfollowing.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //   Log.e("response", response.toString());
                try {
                    pDialog.dismiss();
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jo = jarray.getJSONObject(i);
                        MessageToFriendsData msgtofrnd = new MessageToFriendsData();
                        msgtofrnd.setUname(jo.getString("uname"));
                        msgtofrnd.setProfilepic(jo.getString("image_url"));
                        msgtofrnd.setUserid(jo.getString("user_id"));
                        data.add(msgtofrnd);
                    }


                    recyclerAdapter.notifyDataSetChanged();
                   skipdata=data.size();
                   if(jarray.length()<25)
                       loading=false;
                    else
                       loading=true;
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Log.e("response", error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "4");
                params.put("user_id", SingleTon.pref.getString("user_id", ""));
                params.put("skipdata", skipdata+"");


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

    public void addTextListener(){

        searchbox.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final List<MessageToFriendsData> filteredList = new ArrayList<>();

                for (int i = 0; i < data.size(); i++) {

                    final String text = data.get(i).getUname().toLowerCase();
                    if (text.contains(query)) {

                        filteredList.add(data.get(i));
                    }
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(NewMessageActivity.this));
                recyclerAdapter = new NewMessageAdapter(NewMessageActivity.this,filteredList);

                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }
}

