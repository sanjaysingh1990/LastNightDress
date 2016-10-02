package com.eowise.recyclerview.stickyheaders.samples.LndSwapRequestResponse;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
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
import com.eowise.recyclerview.stickyheaders.samples.adapters.MarginDecoration;
import com.eowise.recyclerview.stickyheaders.samples.adapters.SwapRequestAcceptdAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.NotificationData;
import com.eowise.recyclerview.stickyheaders.samples.data.ShopData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SwapRequestAcceptedActivity extends AppCompatActivity {


    RecyclerView recyclerv;
    ArrayList<ShopData> shopdata = new ArrayList<>();
    public static ShopData selectedpost;
    private SwapRequestAcceptdAdapter adapter;
    protected Handler handler;

    private boolean dataleft = true;

    public  String swapingpostid = "";
    public  String imageurl = "";

    int pos=-1, skipdata = 0;
    @Bind(R.id.swap)
    TextView sendswap;
    @Bind(R.id.nothanks)
    TextView noswaps;

    @Bind(R.id.heading)
    TextView heading;
    NotificationData nd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        //custom font
        heading.setTypeface(SingleTon.hfont);
        sendswap.setTypeface(SingleTon.unamefont);
        noswaps.setTypeface(SingleTon.unamefont);

        //events
        sendswap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = createJsonString(swapingpostid);

                sendSwapRequest(data);
            }
        });
        sendswap.setClickable(false);
        noswaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                discardSwap(nd.getNotification_id());
                //finishing activity
            }
        });
        //reference here
        recyclerv = (RecyclerView) findViewById(R.id.list);
        recyclerv.addItemDecoration(new MarginDecoration(this));
        recyclerv.setHasFixedSize(true);
        recyclerv.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new SwapRequestAcceptdAdapter(shopdata, this, recyclerv, this);
        recyclerv.setAdapter(adapter);
        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            nd = (NotificationData) extra.get("data");
            heading.setText("Swap with " + nd.getUname());
           // postids = nd.getSwappostids();
            pos = extra.getInt("pos");
           // notification_id=nd.getNotification_id();*/
        }
        handler = new Handler();
        JSONArray jsArray = new JSONArray(Arrays.asList(nd.getSwappostids().split(",")));
        JSONObject jobj = new JSONObject();

        try {
            jobj.put("postids", jsArray);
        } catch (Exception ex) {

        }

        if (SingleTon.pref.getBoolean("notshow2", true))
            showAlert();
        getData(jobj.toString());
    }

    private String createJsonString(String swapingpostid) {

        try {
            String swapconfirmsenderid = SingleTon.pref.getString("user_id", "");

            JSONObject mainObj = new JSONObject();
            mainObj.put("swapedpostid", swapingpostid);
            mainObj.put("postid",nd.getPostid());
            mainObj.put("senderid", swapconfirmsenderid);
            mainObj.put("receiverid", nd.getSenderid());
            mainObj.put("date_time", SingleTon.getCurrentTimeStamp());
            mainObj.put("noti_id",nd.getNotification_id());
            mainObj.put("uname", (SingleTon.pref.getString("uname","")));

            return mainObj.toString();
        } catch (Exception ex) {
            Log.e("json error", ex.getMessage() + "");
        }
        return null;
    }

    private void showAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.swap_selection_dialog_layout, null);
        //custom fonts on alert views
        TextView title = (TextView) view.findViewById(R.id.alertTitle);
        TextView message = (TextView) view.findViewById(R.id.alertmessage);
        final CheckBox alertcbox = (CheckBox) view.findViewById(R.id.alertcheckBox);
        title.setTypeface(SingleTon.unamefont);
        message.setTypeface(SingleTon.normalfont);
        alertcbox.setTypeface(SingleTon.normalfont);
        title.setText("Swap with "+nd.getUname());
        //end here
        dialog.setView(view);
        final AlertDialog alert = dialog.create();
        alert.show();
        view.findViewById(R.id.alertcontinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                if (alertcbox.isChecked()) {
                    SharedPreferences.Editor edit = SingleTon.pref.edit();
                    edit.putBoolean("notshow2", false);
                    edit.commit();
                }
            }
        });

    }

    public void back(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void getData(final String postids) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");

        pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/postdata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");
                    if (jarray.length() == 0) {
                        dataleft = false;
                        return;
                    }
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jo = jarray.getJSONObject(i);
                        ShopData pdb = new ShopData();
                        pdb.setPrice(jo.getString("price_now"));
                        pdb.setPostid(jo.getString("post_id"));
                        pdb.setItemchecked(false);
                        pdb.setUname(jo.getString("uname"));
                        pdb.setImageurl(jo.getString("image_url"));
                        shopdata.add(pdb);
                    }


                    // rv.setAdapter(adapter);
                    skipdata = shopdata.size();
                    adapter.notifyDataSetChanged();
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
                params.put("rqid", "12");
                params.put("data", postids);

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

    public void discardSwap(final String notiid) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/inboxope.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  Log.e("response", response.toString());
                try {
                    pDialog.dismiss();
                } catch (Exception ex) {

                }

                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status")) {
                        //   NotificationFragment.recyclerAdapter.notifyDataSetChanged();
                        Intent intent = new Intent();
                        intent.putExtra("pos",pos);
                        intent.putExtra("isswapok",false);

                        setResult(6, intent);
                        finish();

                        Toast.makeText(SwapRequestAcceptedActivity.this, jobj.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SwapRequestAcceptedActivity.this, jobj.getString("message"), Toast.LENGTH_LONG).show();
                    }
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
                params.put("rqid", "11");
                params.put("noti_id", notiid);

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

    public void changeColor(int color) {
        if (color == 1) {
            sendswap.setTextColor(Color.parseColor("#be4d66"));
            sendswap.setClickable(true);
        } else {
            sendswap.setTextColor(Color.parseColor("#b4b4b4"));
            sendswap.setClickable(false);
        }
    }


    public void sendSwapRequest(final String info) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/inboxope.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("json", response.toString());
                try {
                    pDialog.dismiss();
                } catch (Exception ex) {

                }

                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status")) {
                        Intent intent = new Intent();
                        intent.putExtra("isswapok",true);
                        nd.setSwap_order_id(jobj.getString("swap_order_id"));
                        nd.setImgurl(imageurl);
                        nd.setPostid(swapingpostid);
                        intent.putExtra("data",nd);
                        intent.putExtra("pos",pos);

                        setResult(6, intent);

                        finish();

                        Toast.makeText(SwapRequestAcceptedActivity.this, jobj.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SwapRequestAcceptedActivity.this, jobj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage() + "");
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
                params.put("rqid", "5");
                params.put("data", info);

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
