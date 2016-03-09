package com.eowise.recyclerview.stickyheaders.samples.LndUserProfile;


import android.app.AlertDialog;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;

import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.eowise.recyclerview.stickyheaders.samples.EditProfile.EditProfilePrivate;
import com.eowise.recyclerview.stickyheaders.samples.EditProfile.EditProfileShop;
import com.eowise.recyclerview.stickyheaders.samples.Followers.FollowersActivity;

import com.eowise.recyclerview.stickyheaders.samples.Followers.FollowingActivity;
import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;

import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.Settings.PrivacyPolicy;
import com.eowise.recyclerview.stickyheaders.samples.Settings.SettingsActivity;
import com.eowise.recyclerview.stickyheaders.samples.data.ShopData;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import butterknife.Bind;
import butterknife.ButterKnife;
public class LndProfile extends AppCompatActivity {

    private boolean isNormalAdapter = false;
    private RecyclerView mRecyclerView;
    ParallaxRecyclerAdapter<ShopData> adapter;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static String response="";
    public static TextView totalswaps;
    @Bind(R.id.totalfollowers)TextView totalfollowers;
    @Bind(R.id.totalfollowing)TextView totalfollowing;
    @Bind(R.id.totalsales)TextView totalsales;

    @Bind(R.id.usertype)TextView usertype;
    @Bind(R.id.desc)TextView desc;
    @Bind(R.id.mainprofilepic)ImageView profilepic;
    @Bind(R.id.rating)LinearLayout reviews;

    TextView heading;
    private ImageView[] rating=new ImageView[5];
    ArrayList<ShopData> items  = new ArrayList<>();
    private int skipdata=0;
    private AVLoadingIndicatorView prog;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lnd_profile);



        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        createAdapter(mRecyclerView);
        //setting button
        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                Intent i = new Intent(LndProfile.this, SettingsActivity.class);
                startActivity(i);
            }
        });

     //reference and appyling custom font
        prog= (AVLoadingIndicatorView) findViewById(R.id.loader);
        heading= (TextView) findViewById(R.id.heading);
        heading.setTypeface(ImageLoaderImage.robotobold);
      if(items.size()==0)
        getData();
        else {
          createAdapter(mRecyclerView);
          adapter.notifyDataSetChanged();
      }
//get profile information
        String uname = ImageLoaderImage.pref.getString("uname", "uname");
        heading.setText(capitalize(uname));
        String user_id = ImageLoaderImage.pref.getString("user_id", "");

        getPorfile(user_id);

        }



    private  void showInfo()
{
    try {
       // Log.e("lnduser",response);
        JSONObject jobj = new JSONObject(response);
         //totalsales.setText(jobj.getString("bank")+"");
        // totalswaps.setText(jobj.getString("swaps")+"");
         totalfollowers.setText(jobj.getString("followers") + "");
         totalfollowing.setText(jobj.getString("following") + "");
        //Saving user country
        SharedPreferences.Editor edit=ImageLoaderImage.pref.edit();
        edit.putString("country",jobj.getString("country").toLowerCase()+"");
        edit.commit();
        usertype.setText(jobj.getString("type"));
        ImageLoaderImage.imageLoader.displayImage(jobj.getString("imageurl"), profilepic, ImageLoaderImage.options2);
        String des=jobj.getString("desc");
        if(des.length()==0)
            desc.setVisibility(View.GONE);
        else {
         desc.setVisibility(View.VISIBLE);
            desc.setText(des);
        }
       /* int ratingcount=Integer.parseInt(jobj.getString("rating_status"));
        for(int i=0;i<ratingcount;i++)
        {
            rating[i].setImageResource(R.drawable.rating_star_filled);
        }*/

    }
    catch(Exception ex)
    {
        Log.e("json parsing error", ex.getMessage());
    }
}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void createAdapter(RecyclerView recyclerView) {

        adapter = new ParallaxRecyclerAdapter<ShopData>(items) {
            @Override
            public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<ShopData> adapter, int i) {
                String country=ImageLoaderImage.pref.getString("country","united states");
                double price_now=0.0;
                if(i==0) {

                   //image1
                    try {
                                ((MyPost) viewHolder).image1.setVisibility(View.VISIBLE);
                                 ((MyPost) viewHolder).price1.setVisibility(View.VISIBLE);
                                 ImageLoaderImage.imageLoader.displayImage(items.get(0).getImageurl(), ((MyPost) viewHolder).image1, ImageLoaderImage.options);
                                 ((MyPost) viewHolder).price1.setText("$" + items.get(0).getPrice());
                                ((MyPost) viewHolder).image1.setOnClickListener(new MyClass(0));
                        //settting price
                        try
                        {

                            price_now=Double.parseDouble(items.get(0).getPrice());
                        }
                        catch(Exception ex)
                        {

                        }
                        ImageLoaderImage.showValue(country, ((MyPost) viewHolder).price1, price_now);



                    }
                   catch(Exception ex)
                   {
                       ((MyPost) viewHolder).image1.setVisibility(View.INVISIBLE);
                       ((MyPost) viewHolder).price1.setVisibility(View.INVISIBLE);

                   }

                    //image2
                    try {
                        ((MyPost) viewHolder).image2.setVisibility(View.VISIBLE);
                        ((MyPost) viewHolder).price2.setVisibility(View.VISIBLE);

                        ImageLoaderImage.imageLoader.displayImage(items.get(1).getImageurl(), ((MyPost) viewHolder).image2, ImageLoaderImage.options);
                        ((MyPost) viewHolder).price2.setText("$"+items.get(1).getPrice());
                        ((MyPost) viewHolder).image2.setOnClickListener(new MyClass(1));
                        //settting price
                        try
                        {

                            price_now=Double.parseDouble(items.get(1).getPrice());
                        }
                        catch(Exception ex)
                        {

                        }
                        ImageLoaderImage.showValue(country, ((MyPost) viewHolder).price2, price_now);

                    }
                    catch(Exception ex)
                    {
                        ((MyPost) viewHolder).image2.setVisibility(View.INVISIBLE);
                        ((MyPost) viewHolder).price2.setVisibility(View.INVISIBLE);

                    }
//image3
                    try {
                        ((MyPost) viewHolder).price3.setVisibility(View.VISIBLE);

                        ((MyPost) viewHolder).image3.setVisibility(View.VISIBLE);
                        ImageLoaderImage.imageLoader.displayImage(items.get(2).getImageurl(), ((MyPost) viewHolder).image3, ImageLoaderImage.options);
                        ((MyPost) viewHolder).price3.setText("$"+items.get(2).getPrice());
                        ((MyPost) viewHolder).image3.setOnClickListener(new MyClass(2));
                        //settting price
                        try
                        {

                            price_now=Double.parseDouble(items.get(2).getPrice());
                        }
                        catch(Exception ex)
                        {

                        }
                        ImageLoaderImage.showValue(country, ((MyPost) viewHolder).price3, price_now);

                    }
                    catch(Exception ex)
                    {
                        ((MyPost) viewHolder).image3.setVisibility(View.INVISIBLE);
                        ((MyPost) viewHolder).price3.setVisibility(View.INVISIBLE);

                    }


                }
                else
                {
                    //image1
                    try {
                        ((MyPost) viewHolder).price1.setVisibility(View.VISIBLE);

                        ((MyPost) viewHolder).image1.setVisibility(View.VISIBLE);
                        ImageLoaderImage.imageLoader.displayImage(items.get((2 * i + 1 + i) - 1).getImageurl(), ((MyPost) viewHolder).image1, ImageLoaderImage.options);

                        ((MyPost)viewHolder).image1.setOnClickListener(new MyClass((2 * i + 1+i)-1));

                        //settting price
                        try
                        {

                            price_now=Double.parseDouble(items.get((2 * i + 1 + i)-1).getPrice());
                        }
                        catch(Exception ex)
                        {

                        }
                        ImageLoaderImage.showValue(country, ((MyPost) viewHolder).price1, price_now);

                    }
                    catch(Exception ex)
                    {
                        ((MyPost) viewHolder).image1.setVisibility(View.INVISIBLE);
                        ((MyPost) viewHolder).price1.setVisibility(View.INVISIBLE);

                    }

                    //image2
                    try
                    {
                        ((MyPost) viewHolder).price2.setVisibility(View.VISIBLE);

                        ((MyPost) viewHolder).image2.setVisibility(View.VISIBLE);
                        ImageLoaderImage.imageLoader.displayImage(items.get((2 * i + 2 + i) - 1).getImageurl(), ((MyPost) viewHolder).image2, ImageLoaderImage.options);

                        ((MyPost)viewHolder).image2.setOnClickListener(new MyClass((2 * i + 2+i)-1));

                        //settting price
                        try
                        {

                            price_now=Double.parseDouble(items.get((2 * i + 2+i)-1).getPrice());
                        }
                        catch(Exception ex)
                        {

                        }
                        ImageLoaderImage.showValue(country, ((MyPost) viewHolder).price2, price_now);

                    }
                    catch(Exception ex)
                    {
                        ((MyPost) viewHolder).image2.setVisibility(View.INVISIBLE);
                        ((MyPost) viewHolder).price2.setVisibility(View.INVISIBLE);
                    }

                    //image3
                    try {

                        ImageLoaderImage.imageLoader.displayImage(items.get((2 * i + 3 + i) - 1).getImageurl(), ((MyPost) viewHolder).image3, ImageLoaderImage.options);
                        ((MyPost) viewHolder).price3.setText("$" + items.get((2 * i + 3 + i) - 1).getPrice());
                        ((MyPost)viewHolder).image3.setOnClickListener(new MyClass((2 * i + 3+i)-1));

                        ((MyPost) viewHolder).price3.setVisibility(View.VISIBLE);

                        ((MyPost) viewHolder).image3.setVisibility(View.VISIBLE);

                        //settting price
                        try
                        {

                            price_now=Double.parseDouble(items.get((2 * i + 3 + i) - 1).getPrice());
                        }
                        catch(Exception ex)
                        {

                        }
                        ImageLoaderImage.showValue(country, ((MyPost) viewHolder).price3, price_now);

                    }
                    catch(Exception ex)
                    {
                        ((MyPost) viewHolder).image3.setVisibility(View.GONE);
                        ((MyPost) viewHolder).price3.setVisibility(View.GONE);
                    }


                }


            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, final ParallaxRecyclerAdapter<ShopData> adapter, int i) {
                View view=getLayoutInflater().inflate(R.layout.row_recyclerview, viewGroup, false);


                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;


                ViewGroup.LayoutParams params = view.getLayoutParams();

                params.height =(width/3);
                view.requestLayout();
                return new MyPost(view);
            }

            @Override
            public int getItemCountImpl(ParallaxRecyclerAdapter<ShopData> adapter) {
                if(items.size()%3==0)
                    return items.size()/3;
                else
                    return items.size()/3+1;

            }
        };
      /*  adapter.setOnClickEvent(new ParallaxRecyclerAdapter.OnClickEvent() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(MainActivity.this, "You clicked '" + position + "'", Toast.LENGTH_SHORT).show();
            }
        });*/

        final LinearLayoutManager mLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        // recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            //Do pagination.. i.e. fetch new data
                         //  Toast.makeText(LndProfile.this, "loading more", Toast.LENGTH_SHORT).show();
                            getData();
                        }
                    }
                    else
                    {
                        items.add(null);
                        adapter.notifyItemChanged(items.size()-1);
                    }
                }
            }
        });
        View header = getLayoutInflater().inflate(R.layout.header, recyclerView, false);
        TextView edit= (TextView) header.findViewById(R.id.editprofile);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String utype = ImageLoaderImage.pref.getString("utype", "type");
                if (utype.compareTo("shop") == 0) {
                   Intent i = new Intent(LndProfile.this, EditProfileShop.class);
                    i.putExtra("response", response);
                    Main_TabHost.activity.startActivityForResult(i, 2);

                } else if (utype.compareTo("private") == 0) {
                    Intent i = new Intent(LndProfile.this, EditProfilePrivate.class);
                    i.putExtra("response", response);
                    Main_TabHost.activity.startActivityForResult(i, 2);



                }
            }
        });
        //reading username
        final  String userid=ImageLoaderImage.pref.getString("user_id","");

        //to go followers page
        header.findViewById(R.id.followers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                Intent i = new Intent(LndProfile.this, FollowersActivity.class);
                i.putExtra("user_id",userid);
                startActivity(i);
            }
        });
//to go following page
        header.findViewById(R.id.following).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                Intent i = new Intent(LndProfile.this, FollowingActivity.class);
                i.putExtra("user_id",userid);
                startActivity(i);
            }
        });

        //rating stars
        rating[0]= (ImageView) header.findViewById(R.id.star1);
        rating[1]= (ImageView) header.findViewById(R.id.star2);
        rating[2]= (ImageView) header.findViewById(R.id.star3);
        rating[3]= (ImageView) header.findViewById(R.id.star4);
        rating[4]= (ImageView) header.findViewById(R.id.star5);
        totalswaps= (TextView) header.findViewById(R.id.totalswaps);

      //checking swap on or off
        if(ImageLoaderImage.pref.getBoolean("swap",false)) {
            LndProfile.totalswaps.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            LndProfile.totalswaps.setText("0");
        }
        else
        {
            LndProfile.totalswaps.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bank_lock_icon, 0, 0, 0);
            LndProfile.totalswaps.setText("");

        }
        ButterKnife.bind(this, header);
        //setting custom font
        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reviews=new Intent(LndProfile.this,ReviewsActivity.class);
                startActivity(reviews);
            }
        });
        adapter.setParallaxHeader(header, recyclerView);
        adapter.setData(items);
        recyclerView.setAdapter(adapter);
    }


    static class MyPost extends RecyclerView.ViewHolder {
        public ImageView image1,image2,image3;
        public TextView price1,price2,price3;

        public MyPost(View itemView) {
            super(itemView);
            image1 = (ImageView) itemView.findViewById(R.id.image1);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            image3 = (ImageView) itemView.findViewById(R.id.image3);
            price1 = (TextView) itemView.findViewById(R.id.price1);
            price2 = (TextView) itemView.findViewById(R.id.price2);
            price3 = (TextView) itemView.findViewById(R.id.price3);

            image1.setClickable(true);
            image2.setClickable(true);
            image3.setClickable(true);
        }
    }
    class MyClass implements View.OnClickListener
    {
        int pos;
        public MyClass(int pos)
        {
            this.pos=pos;
        }

        @Override
        public void onClick(View v) {

                Intent i = new Intent(LndProfile.this, LndUserFullStickyActivity.class);
                i.putExtra("uname", ImageLoaderImage.pref.getString("uname","uname"));
                i.putExtra("post_location",pos);
                startActivity(i);


        }
    }


    public void showDialog()
    {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bank_unlock_dialog_layout, null);

        dialog.setView(view);
        final    AlertDialog alert=dialog.create();
        alert.show();
        ((TextView)view.findViewById(R.id.dialogmsg)).setTypeface(ImageLoaderImage.normalfont);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        view.findViewById(R.id.nomore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent pp = new Intent(LndProfile.this, PrivacyPolicy.class);
                startActivity(pp);
            }
        });
    }
    public  void getPorfile(final String userid){


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://52.76.68.122/lnd/androidiosphpfiles/lndusers.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                      LndProfile.response=response;
                       showInfo();
                }
                catch(Exception ex)
                {

                }
               // Log.e("response user profile", response.toString());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("response", error.getMessage() + "");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("rqid","3");
                params.put("user_id", userid);

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


    public  void getData(){


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://52.76.68.122/lnd/androidiosphpfiles/postdata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               prog.setVisibility(View.GONE);

                // Log.e("response", response.toString());
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray=jobj.getJSONArray("data");

                    for(int i=0;i<jarray.length();i++)
                    {
                        JSONObject jo=jarray.getJSONObject(i);
                        ShopData pdb = new ShopData();
                        pdb.setPrice(jo.getString("price_now"));
                        pdb.setPostid(jo.getString("post_id"));

                        pdb.setImageurl(jo.getString("image_url"));
                        items.add(pdb);
                    }


                   skipdata=items.size();
                   adapter.notifyDataSetChanged();
                   if(jarray.length()==0)
                       loading=false;
                    else
                       loading=true;
                }
                catch(Exception ex)
                {
                    //Log.e("json parsing error",ex.getMessage());
                    loading=false;
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
                params.put("rqid","1");
                params.put("skipdata",skipdata+"");
                params.put("user_id",ImageLoaderImage.pref.getString("user_id", ""));

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
    public void onBackPressed()
    {
        try

        {

            Main_TabHost.currenttab.pop();
            Main_TabHost.tabHost.setCurrentTab( Main_TabHost.currenttab.pop());
        }
        catch(Exception ex)
        {
            finish();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "updated", Toast.LENGTH_LONG).show();


        if (requestCode == 2) {
            if (data == null)
                return;

            Toast.makeText(this, "updated", Toast.LENGTH_LONG).show();


        }
    }

    private String capitalize(final String line) {
        String[] split=line.split(" ");
        String output="";
        for(String str:split)
        {

            output+=Character.toUpperCase(str.charAt(0)) + str.substring(1)+" ";
        }
        return output;
    }
}
