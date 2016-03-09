package com.eowise.recyclerview.stickyheaders.samples;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by aurel on 22/09/14.
 */
public class MainActivity extends AppCompatActivity{
/*
    private Toolbar toolbar;
    private RecyclerView list;
    private StickyHeadersItemDecoration top;
    private StickyHeadersItemDecoration overlay;
    private PersonDataProvider personDataProvider;
    private PersonAdapter personAdapter;
   private TextView heading;
    public static List<LndHomeData> dataprovider=new ArrayList<LndHomeData>();
    public static List<String> data=new ArrayList<String>();
  static  Toast toast;
    private AVLoadingIndicatorView dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


      heading= (TextView) findViewById(R.id.heading);
       // toolbar = (Toolbar)findViewById(R.id.toolbar);
        list = (RecyclerView)findViewById(R.id.list);

     //   setSupportActionBar(toolbar);

       list.setHasFixedSize(true);
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        // mLayoutManager.
        list.setLayoutManager(mLayoutManager);
        dialog= (AVLoadingIndicatorView) findViewById(R.id.loader);

        getData();


        Typeface tf=Typeface.createFromAsset(getAssets(),"Mural_Script.ttf");
       heading.setTypeface(tf);

        //initializing popup
        //Creating the LayoutInflater instance
        LayoutInflater li = getLayoutInflater();
        //Getting the View object as defined in the customtoast.xml file
        View layout = li.inflate(R.layout.customtoast,
                (ViewGroup) findViewById(R.id.custom_toast_layout));

        //Creating the Toast object
        toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(layout);//setting the view of custom toast layout
    }




//show popup
    public static void showPopup()
    {

toast.show();
    }



    public  void getData(){




        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://52.76.68.122/lnd/postdata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               try {
                   dialog.setVisibility(View.GONE);

                   dataprovider.clear();
                   data.clear();
               }
               catch(Exception ex)
               {

               }
                //Log.e("response", response.toString());
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray=jobj.getJSONArray("data");
                    for(int i=0;i<jarray.length();i++)
                    {
                        JSONObject jo=jarray.getJSONObject(i);
                        List<String> imgurls=new ArrayList<String>();
                        imgurls.add(jo.getString("imageurl1"));
                        imgurls.add(jo.getString("imageurl2"));
                        imgurls.add(jo.getString("imageurl3"));
                        imgurls.add(jo.getString("imageurl4"));

                        LndHomeData lhd=new LndHomeData();
                        lhd.setImgurl(jo.getString("profile_pic"));
                        lhd.setPricenow(jo.getString("price_now"));
                        lhd.setPricewas(jo.getString("price_was"));
                        lhd.setLen(jo.getString("len"));
                        lhd.setCommentstotal(jo.getString("comments"));
                        lhd.setLikes(jo.getString("likes"));
                        lhd.setImages(imgurls);
                        lhd.setPostid(jo.getString("post_id"));
                        lhd.setDesc(jo.getString("description"));
                        lhd.setUname(jo.getString("uname"));
                        data.add(jo.getString("uname"));
                        JSONArray jarray2=jo.getJSONArray("commentsdetails");

                        List<CommentData> commentdata=new ArrayList<CommentData>();
                        for(int j=0;j<jarray2.length();j++)
                        {
                            JSONObject job=jarray2.getJSONObject(j);
                            CommentData cmntdata=new CommentData();
                           try {
                               cmntdata.setUname(job.getString("uname"));
                               cmntdata.setCommenttxxt(job.getString("comment"));
                               commentdata.add(cmntdata);
                           }
                           catch(Exception ex)
                           {

                           }
                        }
                       lhd.setComments(commentdata);
                        check(lhd,lhd.getPostid());
                        dataprovider.add(lhd);
                    }

                    personDataProvider = new PersonDataProvider(data);
                    personAdapter = new PersonAdapter(MainActivity.this, dataprovider);
                    top = new StickyHeadersBuilder()
                            .setAdapter(personAdapter)

                            .setRecyclerView(list)
                            .setStickyHeadersAdapter(new BigramHeaderAdapter(personDataProvider.getItems(), MainActivity.this))
                            .setOnHeaderClickListener(MainActivity.this)
                            .build();




                    // Inflate a menu to be displayed in the toolbar

                    // Set an OnMenuItemClickListener to handle menu item clicks
                  list.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                    PersonAdapter adapter = new PersonAdapter(MainActivity.this, dataprovider);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                   // PersonAdapter adapter = new PersonAdapter(MainActivity.this, dataprovider);
                    list.setAdapter(personAdapter);
                    list.addItemDecoration(top);
                    // rv.setAdapter(adapter);

                }
                catch(Exception ex)
                {
                    Log.e("json parsing error",ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.setVisibility(View.GONE);
               try {
                   new com.eowise.recyclerview.stickyheaders.samples.AlertDialog().showAlertDialog(MainActivity.this);
               }
               catch(Exception ex)
               {

               }
               //Log.e("response",error.getMessage()+"");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("rqid","4");
                params.put("uname", "baba");

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

    @Override
    public void onHeaderClick(View header, long headerId) {
        Toast.makeText(this,"headder clicked",Toast.LENGTH_LONG).show();
    }

    public void instruction(View view) {
        Intent i=new Intent(this,InstructionActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2)
        {
            if(data==null)
                return;

            String postids=data.getStringExtra("postids");
            Log.e("data", postids);

            sendSwapRequest(postids);
        }
    }
    public void sendSwapRequest(final String data) {


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("wait sending swap request...");
       pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://52.76.68.122/lnd/postdata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                Log.e("responsedata", response.toString());
                 toast.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
            //    Log.e("response error", error.getMessage() + "");
              try {
                  new com.eowise.recyclerview.stickyheaders.samples.AlertDialog().showAlertDialog(MainActivity.this);
              }
              catch(Exception ex)
              {

              }
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("data",data);
                params.put("rqid","8");





                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return super.getBody();
            }
        };
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);

        queue.add(sr);
    }
private void check(LndHomeData lndhd,String postid)
{
    FavoriteData fav= ImageLoaderImage.db.getContact(postid);
    if(fav==null)
    {
       lndhd.setFavorate(false);
    }
    else {
        lndhd.setFavorate(true);
    }
}
    @Override
    public void onBackPressed()
    {
        try

        {

           Main_TabHost.currenttab.pop();
              Main_TabHost.tabHost.setCurrentTab(Main_TabHost.currenttab.pop());
        }
        catch(Exception ex)
        {
            finish();
        }

    }
*/
}
