package com.eowise.recyclerview.stickyheaders.samples.LndMessage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.adapters.MarginDecoration;
import com.eowise.recyclerview.stickyheaders.samples.adapters.NotificationAdapter;
import com.eowise.recyclerview.stickyheaders.samples.adapters.NumberedAdapter2;
import com.eowise.recyclerview.stickyheaders.samples.data.NotificationData;
import com.eowise.recyclerview.stickyheaders.samples.data.ShopData;
import com.eowise.recyclerview.stickyheaders.samples.listeners.OnLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SwapRequestActivity extends AppCompatActivity {


    RecyclerView recyclerv;
    ArrayList<ShopData> shopdata  = new ArrayList<>();
    public static ShopData selectedpost;
    private NumberedAdapter2 adapter;
    protected Handler handler;
    private  int skipdata=0;
    private  boolean loadmore=false;
    private boolean dataleft=true;
    String swapreceiverid="",postids="";
    @Bind(R.id.swap) TextView sendswap;
    @Bind(R.id.nothanks) TextView noswaps;

    @Bind(R.id.heading)TextView heading;
    NotificationData nd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        //custom font
        heading.setTypeface(ImageLoaderImage.hfont);
        sendswap.setTypeface(ImageLoaderImage.unamefont);
        noswaps.setTypeface(ImageLoaderImage.unamefont);

        if(ImageLoaderImage.pref.getBoolean("notshow2",true))
            showAlert();
        //events
        sendswap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    JSONObject jobj = new JSONObject();

                    jobj.put("profilepic", ImageLoaderImage.pref.getString("imageurl", "https://"));
                    jobj.put("sendername",ImageLoaderImage.pref.getString("uname","uname"));
                    jobj.put("posturl",selectedpost.getImageurl());
                    jobj.put("receivername",selectedpost.getUname());
                    jobj.put("postid",selectedpost.getPostid());
                    jobj.put("notificationid",nd.getNotification_id());



                    swapRequested(jobj.toString());
                }
                catch (Exception ex)
                {

                }


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
        recyclerv = (RecyclerView)findViewById(R.id.list);
        recyclerv.addItemDecoration(new MarginDecoration(this));
        recyclerv.setHasFixedSize(true);
        recyclerv.setLayoutManager(new GridLayoutManager(this, 3));
        adapter=new NumberedAdapter2(shopdata,this, recyclerv,this);
        recyclerv.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
            /*    shopdata.add(null);
                adapter.notifyItemInserted(shopdata.size() - 1);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadmore = true;
                        if (dataleft) {
                         //   getData(skipdata, swapreceiverid);
                        }
                            else {

                            shopdata.remove(shopdata.size() - 1);

                            adapter.notifyItemRemoved(shopdata.size());

                            adapter.setLoaded();

                        }
                    }
                }, 1000);*/

            }
        });
        Bundle extra=getIntent().getExtras();

        if(extra!=null)
        {
             nd= (NotificationData) extra.get("data");
            heading.setText("Swap with " +nd.getUname());
            postids=nd.getPostid();
            Log.e("postids", postids + "");
            swapreceiverid=nd.getUname();
        }
        handler = new Handler();
        JSONArray jsArray = new JSONArray(Arrays.asList(postids.split(",")));
        JSONObject jobj = new JSONObject();

        try {
           jobj.put("postids", jsArray);
       }
       catch(Exception ex)
       {

       }
           getData(skipdata,swapreceiverid,jobj.toString());
    }

    private String createJsonString( Map<String, ShopData> map)
    {
        ArrayList<String> postid=new ArrayList<>();
        ArrayList<String> imageurl=new ArrayList<>();


        for(Map.Entry<String, ShopData> entry: map.entrySet()) {
            Log.e("values", entry.getKey() + " : " + entry.getValue());

            postid.add(entry.getValue().getPostid());
            imageurl.add(entry.getValue().getImageurl());

        }
        try
        {
            String swapsenderid=ImageLoaderImage.pref.getString("uname","user");
            String profilepic=ImageLoaderImage.pref.getString("imageurl","http:\\");

            JSONArray jsArray = new JSONArray(postid);
            JSONArray imgarray = new JSONArray(imageurl);
            JSONObject mainObj = new JSONObject();
            mainObj.put("postids", jsArray);
            mainObj.put("imagesarray",imgarray);

            mainObj.put("swapsendername",swapsenderid);
            mainObj.put("swapreceivername",swapreceiverid);

            mainObj.put("profilepic",profilepic);

            return mainObj.toString();
        }
        catch(Exception ex)
        {

        }
        return null;
    }
private void showAlert()
{
    AlertDialog.Builder dialog=new AlertDialog.Builder(this);
    View view = LayoutInflater.from(this).inflate(R.layout.swap_selection_dialog_layout, null);
    //custom fonts on alert views
    TextView title= (TextView) view.findViewById(R.id.alertTitle);
    TextView message= (TextView) view.findViewById(R.id.alertmessage);
    final CheckBox alertcbox= (CheckBox) view.findViewById(R.id.alertcheckBox);
   title.setTypeface(ImageLoaderImage.unamefont);
   message.setTypeface(ImageLoaderImage.normalfont);
    alertcbox.setTypeface(ImageLoaderImage.normalfont);
    //end here
    dialog.setView(view);
    final    AlertDialog alert=dialog.create();
    alert.show();
    view.findViewById(R.id.alertcontinue).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            alert.dismiss();
            if(alertcbox.isChecked())
            {
                SharedPreferences.Editor edit= ImageLoaderImage.pref.edit();
                edit.putBoolean("notshow2",false);
                edit.commit();
            }
        }
    });

}
    public void back(View v)
    {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public  void getData(final int dataskip,final String uname,final String postids){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        if(!loadmore)
            pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://52.76.68.122/lnd/postdata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                if(loadmore)
                {
                    shopdata.remove(shopdata.size() - 1);

                    adapter.notifyItemRemoved(shopdata.size());

                    adapter.setLoaded();

                }
                Log.e("postids", response.toString());
              //  Toast.makeText(SwapRequestActivity.this,response.toString()+"",Toast.LENGTH_LONG).show();
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray=jobj.getJSONArray("data");
                    if(jarray.length()==0) {
                        dataleft = false;
                        return;
                    }
                    for(int i=0;i<jarray.length();i++)
                    {
                        JSONObject jo=jarray.getJSONObject(i);
                        ShopData pdb = new ShopData();
                        pdb.setPrice(jo.getString("price_now"));
                        pdb.setPostid(jo.getString("post_id"));
                        pdb.setItemchecked(false);

                        pdb.setUname(jo.getString("uname"));
                        pdb.setImageurl(jo.getString("image_url"));
                        shopdata.add(pdb);
                    }


                    // rv.setAdapter(adapter);
                    skipdata=shopdata.size();
                    adapter.notifyDataSetChanged();
                }
                catch(Exception ex)
                {
                    Log.e("json parsing error",ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                try {
                    shopdata.remove(shopdata.size() - 1);

                    adapter.notifyItemRemoved(shopdata.size());

                    adapter.setLoaded();
                }
                catch(Exception ex)
                {

                }
                Log.e("response",error.getMessage()+"");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("rqid","12");
                params.put("data",postids);

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
    public  void discardSwap(final String notiid) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/inboxope.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response.toString());
                try {
                    pDialog.dismiss();
                }
                catch(Exception ex)
                {

                }

                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    if(jobj.getBoolean("status"))
                    {
                        //   NotificationFragment.recyclerAdapter.notifyDataSetChanged();
                        Intent intent=new Intent();
                        intent.putExtra("discard", "yes");
                        setResult(2, intent);
                        NotificationAdapter.nd.setSwapstatus("0");
                        finish();

                        Toast.makeText(SwapRequestActivity.this, jobj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(SwapRequestActivity.this,jobj.getString("message"),Toast.LENGTH_LONG).show();
                    }
                }




                catch(Exception ex)
                {
                    Log.e("json parsing error",ex.getMessage());
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
                params.put("noti_id",notiid);

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
    public void changeColor()
    {
            sendswap.setTextColor(Color.parseColor("#be4d66"));
            sendswap.setClickable(true);

    }


    public  void swapRequested(final String info) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/inboxope.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response.toString());
                try {
                    pDialog.dismiss();
                }
                catch(Exception ex)
                {

                }

                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    if(jobj.getBoolean("status"))
                    {
                        //   NotificationFragment.recyclerAdapter.notifyDataSetChanged();
                        Intent intent=new Intent();
                        intent.putExtra("discard", "yes");
                        NotificationAdapter.nd.setSwapstatus("0");
                        setResult(2, intent);

                       finish();

                        Toast.makeText(SwapRequestActivity.this, jobj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(SwapRequestActivity.this,jobj.getString("message"),Toast.LENGTH_LONG).show();
                    }
                }




                catch(Exception ex)
                {
                    Log.e("json parsing error",ex.getMessage());
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
                params.put("data",info);

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
