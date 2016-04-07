package com.eowise.recyclerview.stickyheaders.samples.LndMessage;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.Home_List_Data;
import com.eowise.recyclerview.stickyheaders.samples.adapters.MarginDecoration;
import com.eowise.recyclerview.stickyheaders.samples.adapters.NumberedAdapter2;
import com.eowise.recyclerview.stickyheaders.samples.data.ShopData;
import com.eowise.recyclerview.stickyheaders.samples.listeners.OnLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SendSwapRequestActivity extends AppCompatActivity {

    @Bind(R.id.heading)TextView heading;
    RecyclerView recyclerv;
    ArrayList<ShopData> shopdata  = new ArrayList<>();
   public static HashMap<String,ShopData> selectedpost  = new HashMap<>();
    private NumberedAdapter2 adapter;
    protected Handler handler;
    private  int skipdata=0;
    private  boolean loadmore=false;
    private boolean dataleft=true;
    String swapreceiverid="";
    private Home_List_Data lndhome;
   @Bind(R.id.sendrequest) TextView sendswapreq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_swap_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        //custom font
        heading.setTypeface(SingleTon.hfont);
        sendswapreq.setTypeface(SingleTon.unamefont);
         if(SingleTon.pref.getBoolean("notshow",true))
          showAlert();
        //events
        sendswapreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.putExtra("postids",createJsonString(selectedpost));
                setResult(4, intent);
                selectedpost.clear();
                finish();//finishing activity
            }
        });
        sendswapreq.setClickable(false);
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
                shopdata.add(null);
                adapter.notifyItemInserted(shopdata.size() - 1);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadmore = true;
                        if (dataleft)
                            getData(skipdata);
                        else {

                            shopdata.remove(shopdata.size() - 1);

                            adapter.notifyItemRemoved(shopdata.size());

                            adapter.setLoaded();

                        }
                    }
                }, 1000);

            }
        });
        Bundle extra=getIntent().getExtras();

        if(extra!=null)
        {
            lndhome= (Home_List_Data) extra.get("data");
            swapreceiverid=lndhome.getUserid();

        }

        handler = new Handler();
        getData(skipdata);
    }
    private String createJsonString( Map<String, ShopData> map)
    {
        ArrayList<String> postid=new ArrayList<>();

       //read all the images
        for(Map.Entry<String, ShopData> entry: map.entrySet()) {
            //Log.e("values", entry.getKey() + " : " + entry.getValue());

            postid.add(entry.getValue().getPostid());


        }
     try
     {
         String swapsenderid= SingleTon.pref.getString("user_id","");

         JSONArray jsArray = new JSONArray(postid);

         JSONObject mainObj = new JSONObject();

         //swaping with item
          mainObj.put("postid",lndhome.getPost_id());
          mainObj.put("swappostids", jsArray);

         mainObj.put("swapsenderid",swapsenderid);
         mainObj.put("swapreceiverid",swapreceiverid);
         mainObj.put("date_time", SingleTon.getCurrentTimeStamp());


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
   title.setTypeface(SingleTon.unamefont);
   message.setTypeface(SingleTon.normalfont);
    alertcbox.setTypeface(SingleTon.normalfont);

    title.setText("My items");
    message.setText("To increase your chance for your\nrequest,select all the items you\nare willing to swap with for the\nselected product.");
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
              SharedPreferences.Editor edit= SingleTon.pref.edit();
                edit.putBoolean("notshow",false);
                edit.commit();
            }
        }
    });

}
    public void back(View v)
    {
        selectedpost.clear();
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public  void getData(final int dataskip){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        if(!loadmore)
            pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://52.76.68.122/lnd/androidiosphpfiles/postdata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                if(loadmore)
                {
                    shopdata.remove(shopdata.size() - 1);

                    adapter.notifyItemRemoved(shopdata.size());

                    adapter.setLoaded();

                }
               // Log.e("response", response.toString());

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
                params.put("rqid","13");
                params.put("user_id", SingleTon.pref.getString("user_id",""));

                params.put("skipdata",dataskip+"");

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
public void changeColor(int color)
{
    if(color==1) {
        sendswapreq.setTextColor(Color.parseColor("#be4d66"));
        sendswapreq.setClickable(true);
    }
        else
    {
        sendswapreq.setTextColor(Color.parseColor("#b4b4b4"));
        sendswapreq.setClickable(false);
    }

}
}
