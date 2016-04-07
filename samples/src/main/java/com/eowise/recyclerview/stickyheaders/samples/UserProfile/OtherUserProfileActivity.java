package com.eowise.recyclerview.stickyheaders.samples.UserProfile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.Followers.FollowersActivity;
import com.eowise.recyclerview.stickyheaders.samples.Followers.FollowingActivity;
import com.eowise.recyclerview.stickyheaders.samples.LndUserProfile.ReviewsActivity;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.LndUserProfile.LndUserFullStickyActivity;
import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.NewMessage.SendMessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.LndUserProfile.ParallaxRecyclerAdapter;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ColoredRatingBar;
import com.eowise.recyclerview.stickyheaders.samples.data.ShopData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OtherUserProfileActivity extends Activity {

    private boolean isNormalAdapter = false;
    private RecyclerView mRecyclerView;
    ParallaxRecyclerAdapter<ShopData> adapter;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static String response = "";
    @Bind(R.id.totalswaps)
    TextView totalswaps;
    @Bind(R.id.totalfollowers)
    TextView totalfollowers;
    @Bind(R.id.totalfollowing)
    TextView totalfollowing;
    @Bind(R.id.totalsales)
    TextView totalsales;
    @Bind(R.id.userfollow)
    TextView followunfollow;

    @Bind(R.id.desc)
    TextView desc;
    @Bind(R.id.message)
    TextView message;
    @Bind(R.id.rating)
    LinearLayout reviews;
    @Bind(R.id.mainprofilepic)
    ImageView profilepic;
    TextView heading;
    @Bind(R.id.ratingBar)
    ColoredRatingBar rating;
    ArrayList<ShopData> items = new ArrayList<>();
    private int skipdata = 0;
    private AVLoadingIndicatorView prog;
    String profileuname = "", userid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_other_user_profile);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        createAdapter(mRecyclerView);
        //setting button
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                finish();
            }
        });
        Bundle data = getIntent().getExtras();
        if (data != null) {
            profileuname = data.getString("uname").trim();
          //  Log.e("uname",profileuname.trim()+"");
            userid = data.getString("user_id");
        }
        //reference and appyling custom font


        prog = (AVLoadingIndicatorView) findViewById(R.id.loader);
        heading = (TextView) findViewById(R.id.heading);
        heading.setText(capitalize(profileuname));
        heading.setTypeface(SingleTon.robotobold);
        getData(userid,profileuname);
        getPorfile(SingleTon.pref.getString("user_id", ""), userid,profileuname);
    }

    private String capitalize(final String line) {
        String[] split = line.split(" ");
        String output = "";
        for (String str : split) {

            output += Character.toUpperCase(str.charAt(0)) + str.substring(1) + " ";
        }
        return output;
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
                String country = SingleTon.pref.getString("country", "united states");
                double price_now = 0.0;

                if (i == 0) {

                    //image1
                    try {
                        ((MyPost) viewHolder).image1.setVisibility(View.VISIBLE);
                        ((MyPost) viewHolder).price1.setVisibility(View.VISIBLE);
                        SingleTon.imageLoader.displayImage(items.get(0).getImageurl(), ((MyPost) viewHolder).image1, SingleTon.options);
                        ((MyPost) viewHolder).image1.setOnClickListener(new MyClass(0));
                        //settting price
                        try {

                            price_now = Double.parseDouble(items.get(0).getPrice());
                        } catch (Exception ex) {

                        }
                        SingleTon.showValue(country, ((MyPost) viewHolder).price1, price_now);

                    } catch (Exception ex) {
                        ((MyPost) viewHolder).image1.setVisibility(View.INVISIBLE);
                        ((MyPost) viewHolder).price1.setVisibility(View.INVISIBLE);

                    }

                    //image2
                    try {
                        ((MyPost) viewHolder).image2.setVisibility(View.VISIBLE);
                        ((MyPost) viewHolder).price2.setVisibility(View.VISIBLE);

                        SingleTon.imageLoader.displayImage(items.get(1).getImageurl(), ((MyPost) viewHolder).image2, SingleTon.options);
                        ((MyPost) viewHolder).image2.setOnClickListener(new MyClass(1));
                        //settting price
                        try {

                            price_now = Double.parseDouble(items.get(1).getPrice());
                        } catch (Exception ex) {

                        }
                        SingleTon.showValue(country, ((MyPost) viewHolder).price2, price_now);

                    } catch (Exception ex) {
                        ((MyPost) viewHolder).image2.setVisibility(View.INVISIBLE);
                        ((MyPost) viewHolder).price2.setVisibility(View.INVISIBLE);

                    }
//image3
                    try {
                        ((MyPost) viewHolder).price3.setVisibility(View.VISIBLE);

                        ((MyPost) viewHolder).image3.setVisibility(View.VISIBLE);
                        SingleTon.imageLoader.displayImage(items.get(2).getImageurl(), ((MyPost) viewHolder).image3, SingleTon.options);
                        ((MyPost) viewHolder).image3.setOnClickListener(new MyClass(2));

                        //settting price
                        try {

                            price_now = Double.parseDouble(items.get(2).getPrice());
                        } catch (Exception ex) {

                        }
                        SingleTon.showValue(country, ((MyPost) viewHolder).price3, price_now);

                    } catch (Exception ex) {
                        ((MyPost) viewHolder).image3.setVisibility(View.INVISIBLE);
                        ((MyPost) viewHolder).price3.setVisibility(View.INVISIBLE);

                    }


                } else {
                    //image1
                    try {
                        ((MyPost) viewHolder).price1.setVisibility(View.VISIBLE);

                        ((MyPost) viewHolder).image1.setVisibility(View.VISIBLE);
                        SingleTon.imageLoader.displayImage(items.get((2 * i + 1 + i) - 1).getImageurl(), ((MyPost) viewHolder).image1, SingleTon.options);
                        ((MyPost) viewHolder).image1.setOnClickListener(new MyClass((2 * i + 1 + i) - 1));
//settting price
                        try {

                            price_now = Double.parseDouble(items.get((2 * i + 1 + i) - 1).getPrice());
                        } catch (Exception ex) {

                        }
                        SingleTon.showValue(country, ((MyPost) viewHolder).price1, price_now);

                    } catch (Exception ex) {
                        ((MyPost) viewHolder).image1.setVisibility(View.INVISIBLE);
                        ((MyPost) viewHolder).price1.setVisibility(View.INVISIBLE);

                    }

                    //image2
                    try {
                        ((MyPost) viewHolder).price2.setVisibility(View.VISIBLE);

                        ((MyPost) viewHolder).image2.setVisibility(View.VISIBLE);
                        SingleTon.imageLoader.displayImage(items.get((2 * i + 2 + i) - 1).getImageurl(), ((MyPost) viewHolder).image2, SingleTon.options);

                        ((MyPost) viewHolder).image2.setOnClickListener(new MyClass((2 * i + 2 + i) - 1));
                        try {

                            price_now = Double.parseDouble(items.get((2 * i + 2 + i) - 1).getPrice());
                        } catch (Exception ex) {

                        }
                        SingleTon.showValue(country, ((MyPost) viewHolder).price2, price_now);


                    } catch (Exception ex) {
                        ((MyPost) viewHolder).image2.setVisibility(View.INVISIBLE);
                        ((MyPost) viewHolder).price2.setVisibility(View.INVISIBLE);
                    }

                    //image3
                    try {

                        SingleTon.imageLoader.displayImage(items.get((2 * i + 3 + i) - 1).getImageurl(), ((MyPost) viewHolder).image3, SingleTon.options);
                        ((MyPost) viewHolder).price3.setText("$" + items.get((2 * i + 3 + i) - 1).getPrice());
                        ((MyPost) viewHolder).image3.setOnClickListener(new MyClass((2 * i + 3 + i) - 1));

                        ((MyPost) viewHolder).price3.setVisibility(View.VISIBLE);

                        ((MyPost) viewHolder).image3.setVisibility(View.VISIBLE);

                        try {

                            price_now = Double.parseDouble(items.get((2 * i + 3 + i) - 1).getPrice());
                        } catch (Exception ex) {

                        }
                        SingleTon.showValue(country, ((MyPost) viewHolder).price3, price_now);


                    } catch (Exception ex) {
                        ((MyPost) viewHolder).image3.setVisibility(View.GONE);
                        ((MyPost) viewHolder).price3.setVisibility(View.GONE);
                    }


                }


            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, final ParallaxRecyclerAdapter<ShopData> adapter, int i) {
                View view = getLayoutInflater().inflate(R.layout.row_recyclerview, viewGroup, false);


                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;


                ViewGroup.LayoutParams params = view.getLayoutParams();

                params.height = (width / 3);
                view.requestLayout();
                return new MyPost(view);
            }

            @Override
            public int getItemCountImpl(ParallaxRecyclerAdapter<ShopData> adapter) {
                if (items.size() % 3 == 0)
                    return items.size() / 3;
                else
                    return items.size() / 3 + 1;

            }
        };
      /*  adapter.setOnClickEvent(new ParallaxRecyclerAdapter.OnClickEvent() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(MainActivity.this, "You clicked '" + position + "'", Toast.LENGTH_SHORT).show();
            }
        });*/

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
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
                            //  Toast.makeText(LndProfile.this,"loading more",Toast.LENGTH_SHORT).show();
                            getData(userid,profileuname);
                        }
                    }
                }
            }
        });
        View header = getLayoutInflater().inflate(R.layout.header2, recyclerView, false);
        /*header.findViewById(R.id.followers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                Intent i = new Intent(OtherUserProfileActivity.this, FollowersActivity.class);
                startActivity(i);
            }
        });*/


        ButterKnife.bind(this, header);
        //send message to user
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendreceive = new Intent(OtherUserProfileActivity.this, SendMessageActivity.class);
                sendreceive.putExtra("uname", profileuname);
                startActivity(sendreceive);
            }
        });

        followunfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followunfollow(userid, SingleTon.pref.getString("user_id", ""));
            }
        });

        //to go followers page
        header.findViewById(R.id.followers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                Intent i = new Intent(OtherUserProfileActivity.this, FollowersActivity.class);
                i.putExtra("user_id", userid);
                i.putExtra("userp", 2);

                startActivity(i);
            }
        });
//to go following page
        header.findViewById(R.id.following).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                Intent i = new Intent(OtherUserProfileActivity.this, FollowingActivity.class);
                i.putExtra("user_id", userid);
                i.putExtra("userp", 2);
                startActivity(i);
            }
        });
        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reviews = new Intent(OtherUserProfileActivity.this, ReviewsActivity.class);
                reviews.putExtra("user_id",userid);

                startActivity(reviews);
            }
        });
        adapter.setParallaxHeader(header, recyclerView);
        adapter.setData(items);
        recyclerView.setAdapter(adapter);
    }


    static class MyPost extends RecyclerView.ViewHolder {
        public ImageView image1, image2, image3;
        public TextView price1, price2, price3;

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

    class MyClass implements View.OnClickListener {
        int pos;

        public MyClass(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {

            Intent i = new Intent(OtherUserProfileActivity.this, LndUserFullStickyActivity.class);

            i.putExtra("uname", profileuname);
            i.putExtra("post_location", pos);
            // startActivity(i);


        }
    }

    public void getPorfile(final String followerid, final String followingid,final String uname) {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/lndusers.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("otherprofile", response.toString());
                try {

                    JSONObject jobj = new JSONObject(response.toString());
                    totalsales.setText(jobj.getString("sales"));
                    totalswaps.setText(jobj.getString("swaps") + "");
                    SingleTon.imageLoader.displayImage(jobj.getString("imageurl"), profilepic, SingleTon.options2);
                    String des = jobj.getString("desc");
                    if (des.length() == 0)
                        desc.setVisibility(View.GONE);
                    else
                        desc.setText(des);

                    totalfollowers.setText(jobj.getString("followers") + "");
                    totalfollowing.setText(jobj.getString("following") + "");

                    if (jobj.getBoolean("follow")) {
                        followunfollow.setText("Following");
                        followunfollow.setTextColor(Color.parseColor("#9ea6b0"));
                        followunfollow.setCompoundDrawablesWithIntrinsicBounds(
                                0, //left
                                0, //top
                                R.drawable.following_icon, //right
                                0);//bottom
                    } else {
                        followunfollow.setText("+ Follow");
                        followunfollow.setTextColor(Color.parseColor("#be4d66"));
                        followunfollow.setCompoundDrawablesWithIntrinsicBounds(
                                0, //left
                                0, //top
                                0, //right
                                0);//bottom
                    }
//follow unfollow event
                    followunfollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            followunfollow(userid, SingleTon.pref.getString("user_id", ""));
                        }
                    });

                    float ratingcount = Float.parseFloat(jobj.getString("rating"));
                    rating.setRating(ratingcount);

                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("response", error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "14");
                params.put("followerid", followerid);
                params.put("followingid", followingid);
                params.put("uname",uname);



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


    public void getData(final String userid,final String profileuname) {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/postdata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                prog.setVisibility(View.GONE);

                // Log.e("response", response.toString());
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jo = jarray.getJSONObject(i);
                        ShopData pdb = new ShopData();
                        pdb.setPrice(jo.getString("price_now"));
                        pdb.setPostid(jo.getString("post_id"));

                        pdb.setImageurl(jo.getString("image_url"));
                        items.add(pdb);
                    }
                    skipdata = items.size();
                    adapter.notifyDataSetChanged();
                    loading = true;
                } catch (Exception ex) {
                    //Log.e("json parsing error",ex.getMessage());
                    loading = false;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prog.setVisibility(View.GONE);
                //  Log.e("response",error.getMessage()+"");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "1");
                params.put("skipdata", skipdata + "");
                params.put("user_id", userid);
                params.put("uname",profileuname);

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

    public void followunfollow(final String followingid, final String followerid) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/followfollowing.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                //Toast.makeText(OtherUserProfileActivity.this,response+"",Toast.LENGTH_LONG).show();
                //  Log.e("following", response.toString());
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status")) {
                        if (jobj.getString("value").compareTo("1") == 0) {
                            followunfollow.setText("Following");
                            followunfollow.setTextColor(Color.parseColor("#9ea6b0"));
                            followunfollow.setCompoundDrawablesWithIntrinsicBounds(
                                    0, //left
                                    0, //top
                                    R.drawable.following_icon, //right
                                    0);//bottom
                            int value = Integer.parseInt(totalfollowers.getText().toString());
                            totalfollowers.setText(value + 1 + "");


                        } else {
                            followunfollow.setText("+ Follow");
                            followunfollow.setTextColor(Color.parseColor("#be4d66"));
                            followunfollow.setCompoundDrawablesWithIntrinsicBounds(
                                    0, //left
                                    0, //top
                                    0, //right
                                    0);//bottom
                            int value = Integer.parseInt(totalfollowers.getText().toString());

                            totalfollowers.setText(value - 1 + "");
                        }
                    }
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                    loading = false;
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
                params.put("rqid", "2");
                params.put("followerid", followerid);
                params.put("followingid", followingid);
                params.put("date_time", SingleTon.getCurrentTimeStamp());

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
