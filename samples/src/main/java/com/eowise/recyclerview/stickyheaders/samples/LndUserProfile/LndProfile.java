package com.eowise.recyclerview.stickyheaders.samples.LndUserProfile;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.ProgressBar;
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
import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SQLDB.FavoriteData;
import com.eowise.recyclerview.stickyheaders.samples.Settings.ReadMore;
import com.eowise.recyclerview.stickyheaders.samples.Settings.SettingsActivity;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.CommentBean;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.Home_List_Data;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ColoredRatingBar;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ConstantValues;
import com.eowise.recyclerview.stickyheaders.samples.data.ShopData;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LndProfile extends AppCompatActivity {

    private boolean isNormalAdapter = false;
    private RecyclerView mRecyclerView;
    static ParallaxRecyclerAdapter<ShopData> adapter;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static String response = "";
    public static TextView totalswaps;
    @Bind(R.id.totalfollowers)
    TextView totalfollowers;
    @Bind(R.id.totalfollowing)
    TextView totalfollowing;
    @Bind(R.id.totalsales)
    TextView totalsales;

    @Bind(R.id.usertype)
    TextView usertype;
    @Bind(R.id.desc)
    TextView desc;
    @Bind(R.id.mainprofilepic)
    ImageView profilepic;
    @Bind(R.id.rating)
    LinearLayout reviews;
    @Bind(R.id.ratingBar)
    ColoredRatingBar rating;
    TextView heading;

    ArrayList<ShopData> items = new ArrayList<>();
    private int skipdata = 0;
    private AVLoadingIndicatorView prog;
    private Dialog dialog;
    public int check = 0;
    private boolean dataleft = true;
    public static Context con;
    public static ArrayList<Home_List_Data> mItems = new ArrayList<>();
    private int count = 0;
    //data for sticky header
    boolean isprivate = false;

    int sectionManager = -1;
    int headerCount = 0;
    int sectionFirstPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lnd_profile);

        con = this;
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
        prog = (AVLoadingIndicatorView) findViewById(R.id.loader);
        heading = (TextView) findViewById(R.id.heading);
        heading.setTypeface(SingleTon.robotobold);

        //clear list
        mItems.clear();
        getData();

//get profile information
        String uname = SingleTon.pref.getString("uname", "uname");
        heading.setText(Capitalize.capitalizeFirstLetter(uname));
        String user_id = SingleTon.pref.getString("user_id", "");

        getPorfile(user_id);

    }


    private void showInfo() {
        try {
            // Log.e("lnduser", response);
            JSONObject jobj = new JSONObject(response);
            totalsales.setText(jobj.getString("sales") + "");
            // totalswaps.setText(jobj.getString("swaps") + "");
            totalfollowers.setText(jobj.getString("followers") + "");
            totalfollowing.setText(jobj.getString("following") + "");
            //Saving user country
            SharedPreferences.Editor edit = SingleTon.pref.edit();
            edit.putString("country", jobj.getString("country").toLowerCase() + "");
            edit.commit();
            if (jobj.getString("type").compareToIgnoreCase("private") == 0)
                usertype.setText("");
            else
                usertype.setText(jobj.getString("type"));
            SingleTon.imageLoader.displayImage(jobj.getString("imageurl"), profilepic, SingleTon.options2);
            String des = jobj.getString("desc");
            if (des.length() == 0)
                desc.setVisibility(View.GONE);
            else {
                desc.setVisibility(View.VISIBLE);
                desc.setText(des);
            }
            float ratingcount = Float.parseFloat(jobj.getString("rating"));
            rating.setRating(ratingcount);
        } catch (Exception ex) {
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
            public void onBindViewHolderImpl(final RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<ShopData> adapter, int i) {
                String country = SingleTon.pref.getString("country", "united states");
                double price_now = 0.0;
                if (i == 0) {

                    //image1
                    try {
                        ((MyPost) viewHolder).image1.setVisibility(View.VISIBLE);
                        ((MyPost) viewHolder).price1.setVisibility(View.VISIBLE);


                        ImageLoader.getInstance()
                                .displayImage(items.get(0).getImageurl(), ((MyPost) viewHolder).image1, SingleTon.options4, new SimpleImageLoadingListener() {
                                    @Override
                                    public void onLoadingStarted(String imageUri, View view) {

                                        ((MyPost) viewHolder).prog1.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                        ((MyPost) viewHolder).prog1.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                        ((MyPost) viewHolder).prog1.setVisibility(View.GONE);
                                    }
                                }, new ImageLoadingProgressListener() {
                                    @Override
                                    public void onProgressUpdate(String imageUri, View view, int current, int total) {

                                    }
                                });

                        ((MyPost) viewHolder).price1.setText("$" + items.get(0).getPrice());
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

                        ImageLoader.getInstance()
                                .displayImage(items.get(1).getImageurl(), ((MyPost) viewHolder).image2, SingleTon.options4, new SimpleImageLoadingListener() {
                                    @Override
                                    public void onLoadingStarted(String imageUri, View view) {

                                        ((MyPost) viewHolder).prog2.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                        ((MyPost) viewHolder).prog2.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                        ((MyPost) viewHolder).prog2.setVisibility(View.GONE);
                                    }
                                }, new ImageLoadingProgressListener() {
                                    @Override
                                    public void onProgressUpdate(String imageUri, View view, int current, int total) {

                                    }
                                });


                        ((MyPost) viewHolder).price2.setText("$" + items.get(1).getPrice());
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

                        ImageLoader.getInstance()
                                .displayImage(items.get(2).getImageurl(), ((MyPost) viewHolder).image3, SingleTon.options4, new SimpleImageLoadingListener() {
                                    @Override
                                    public void onLoadingStarted(String imageUri, View view) {

                                        ((MyPost) viewHolder).prog3.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                        ((MyPost) viewHolder).prog3.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                        ((MyPost) viewHolder).prog3.setVisibility(View.GONE);
                                    }
                                }, new ImageLoadingProgressListener() {
                                    @Override
                                    public void onProgressUpdate(String imageUri, View view, int current, int total) {

                                    }
                                });


                        ((MyPost) viewHolder).price3.setText("$" + items.get(2).getPrice());
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
                        ShopData sd = items.get((2 * i + 1 + i) - 1);
                        ((MyPost) viewHolder).price1.setVisibility(View.VISIBLE);

                        ((MyPost) viewHolder).image1.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance()
                                .displayImage(sd.getImageurl(), ((MyPost) viewHolder).image1, SingleTon.options4, new SimpleImageLoadingListener() {
                                    @Override
                                    public void onLoadingStarted(String imageUri, View view) {

                                        ((MyPost) viewHolder).prog1.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                        ((MyPost) viewHolder).prog1.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                        ((MyPost) viewHolder).prog1.setVisibility(View.GONE);
                                    }
                                }, new ImageLoadingProgressListener() {
                                    @Override
                                    public void onProgressUpdate(String imageUri, View view, int current, int total) {

                                    }
                                });

                        ((MyPost) viewHolder).image1.setOnClickListener(new MyClass((2 * i + 1 + i) - 1));

                        //settting price
                        try {

                            price_now = Double.parseDouble(sd.getPrice());
                        } catch (Exception ex) {

                        }
                        SingleTon.showValue(country, ((MyPost) viewHolder).price1, price_now);

                    } catch (Exception ex) {
                        ((MyPost) viewHolder).image1.setVisibility(View.INVISIBLE);
                        ((MyPost) viewHolder).price1.setVisibility(View.INVISIBLE);

                    }

                    //image2
                    try {
                        ShopData sd = items.get((2 * i + 2 + i) - 1);
                        ((MyPost) viewHolder).price2.setVisibility(View.VISIBLE);

                        ((MyPost) viewHolder).image2.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance()
                                .displayImage(sd.getImageurl(), ((MyPost) viewHolder).image2, SingleTon.options4, new SimpleImageLoadingListener() {
                                    @Override
                                    public void onLoadingStarted(String imageUri, View view) {

                                        ((MyPost) viewHolder).prog2.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                        ((MyPost) viewHolder).prog2.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                        ((MyPost) viewHolder).prog2.setVisibility(View.GONE);
                                    }
                                }, new ImageLoadingProgressListener() {
                                    @Override
                                    public void onProgressUpdate(String imageUri, View view, int current, int total) {

                                    }
                                });


                        ((MyPost) viewHolder).image2.setOnClickListener(new MyClass((2 * i + 2 + i) - 1));

                        //settting price
                        try {

                            price_now = Double.parseDouble(sd.getPrice());
                        } catch (Exception ex) {

                        }
                        SingleTon.showValue(country, ((MyPost) viewHolder).price2, price_now);

                    } catch (Exception ex) {
                        ((MyPost) viewHolder).image2.setVisibility(View.INVISIBLE);
                        ((MyPost) viewHolder).price2.setVisibility(View.INVISIBLE);
                    }

                    //image3
                    try {

                        ShopData sd = items.get((2 * i + 3 + i) - 1);
                        ImageLoader.getInstance()
                                .displayImage(sd.getImageurl(), ((MyPost) viewHolder).image3, SingleTon.options4, new SimpleImageLoadingListener() {
                                    @Override
                                    public void onLoadingStarted(String imageUri, View view) {

                                        ((MyPost) viewHolder).prog3.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                        ((MyPost) viewHolder).prog3.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                        ((MyPost) viewHolder).prog3.setVisibility(View.GONE);
                                    }
                                }, new ImageLoadingProgressListener() {
                                    @Override
                                    public void onProgressUpdate(String imageUri, View view, int current, int total) {

                                    }
                                });


                        ((MyPost) viewHolder).price3.setText("$" + sd.getPrice());
                        ((MyPost) viewHolder).image3.setOnClickListener(new MyClass((2 * i + 3 + i) - 1));

                        ((MyPost) viewHolder).price3.setVisibility(View.VISIBLE);

                        ((MyPost) viewHolder).image3.setVisibility(View.VISIBLE);

                        //settting price
                        try {

                            price_now = Double.parseDouble(sd.getPrice());
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


        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


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


                            if (dataleft)
                                try {
                                    loading = false;
                                    getData();
                                } catch (Exception ex) {

                                }
                            else {

                                if (check < 10) {
                                    check++;
                                    items.add(null);
                                    adapter.notifyItemChanged(items.size() - 1);

                                } else
                                    loading = false;
                            }


                        }
                    }
                }
            }
        });


        View header = getLayoutInflater().inflate(R.layout.header, recyclerView, false);
        TextView edit = (TextView) header.findViewById(R.id.editprofile);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String utype = SingleTon.pref.getString("utype", "type");
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
        final String userid = SingleTon.pref.getString("user_id", "");

        //to go followers page
        header.findViewById(R.id.followers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                Intent followers = new Intent(LndProfile.this, FollowersActivity.class);
                followers.putExtra("user_id", userid);
                followers.putExtra("userp", 1);

                Main_TabHost.activity.startActivityForResult(followers, 7);
            }
        });
//to go following page
        header.findViewById(R.id.following).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                Intent following = new Intent(LndProfile.this, FollowingActivity.class);
                following.putExtra("user_id", userid);
                following.putExtra("userp", 1);
                Main_TabHost.activity.startActivityForResult(following, 7);
            }
        });


        totalswaps = (TextView) header.findViewById(R.id.totalswaps);

        //checking swap on or off
        if (SingleTon.pref.getBoolean("swap", false)) {
            LndProfile.totalswaps.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            LndProfile.totalswaps.setText("0");
        } else {
            LndProfile.totalswaps.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bank_lock_icon, 0, 0, 0);
            LndProfile.totalswaps.setText("");

        }
        ButterKnife.bind(this, header);
        //setting custom font
        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reviews = new Intent(LndProfile.this, ReviewsActivity.class);
                reviews.putExtra("user_id", SingleTon.pref.getString("user_id", ""));
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
        public ProgressBar prog1, prog2, prog3;

        public MyPost(View itemView) {
            super(itemView);
            image1 = (ImageView) itemView.findViewById(R.id.image1);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            image3 = (ImageView) itemView.findViewById(R.id.image3);
            price1 = (TextView) itemView.findViewById(R.id.price1);
            price2 = (TextView) itemView.findViewById(R.id.price2);
            price3 = (TextView) itemView.findViewById(R.id.price3);
            prog1 = (ProgressBar) itemView.findViewById(R.id.progressBar1);
            prog2 = (ProgressBar) itemView.findViewById(R.id.progressBar2);
            prog3 = (ProgressBar) itemView.findViewById(R.id.progressBar3);

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

            Intent i = new Intent(LndProfile.this, LndUserFullStickyActivity.class);
            i.putExtra("uname", SingleTon.pref.getString("uname", "uname"));
            i.putExtra("post_location", pos);
            i.putExtra("profiletype", 1);

            Main_TabHost.activity.startActivityForResult(i, 5);


        }
    }


    public void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bank_unlock_dialog_layout, null);

        dialog.setView(view);
        final AlertDialog alert = dialog.create();
        alert.show();
        ((TextView) view.findViewById(R.id.dialogmsg)).setTypeface(SingleTon.normalfont);
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
                Intent pp = new Intent(LndProfile.this, ReadMore.class);
                startActivity(pp);
            }
        });
    }

    public void getPorfile(final String userid) {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/lndusers.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    LndProfile.response = response;
                    showInfo();
                } catch (Exception ex) {

                }
                 Log.e("response user profile", response.toString());


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
                params.put("rqid", "3");
                params.put("user_id", userid);

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

    /*
        public void getData() {

            prog.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/postdata.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    prog.setVisibility(View.GONE);
                    loading=true;
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

                        if (jarray.length() == 0) {

                            dataleft = false;
                        }

                    } catch (Exception ex) {
                        //Log.e("json parsing error",ex.getMessage());

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
                    params.put("user_id", SingleTon.pref.getString("user_id", ""));

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
    */
    @Override
    public void onBackPressed() {
        try

        {

            Main_TabHost.currenttab.pop();
            Main_TabHost.tabHost.setCurrentTab(Main_TabHost.currenttab.pop());
        } catch (Exception ex) {
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


    public void updateList(ArrayList<String> pos) {

        for (String position : pos) {
            int repos = Integer.parseInt(position);
            items.remove(repos);
            adapter.notifyDataSetChanged();


        }


    }

    public void getData() {

        prog.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_POST_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                prog.setVisibility(View.GONE);
                loading = true;

                // Log.e("json", response.toString());

                try {


                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");

                    for (int i = 0; i < jarray.length(); i++) {


                        JSONObject jo = jarray.getJSONObject(i);

                        //for grid items
                        ShopData pdb = new ShopData();
                        pdb.setPrice(jo.getString("price_now"));
                        pdb.setPostid(jo.getString("post_id"));

                        pdb.setImageurl(jo.getString("imageurl1"));
                        items.add(pdb);


                        ArrayList<String> imgurls = new ArrayList<String>();

                        String imgurl = jo.getString("imageurl1");

                        imgurls.add(imgurl);

                        imgurl = jo.getString("imageurl2");
                        if (imgurl.length() > 0)
                            imgurls.add(imgurl);

                        imgurl = jo.getString("imageurl3");
                        if (imgurl.length() > 0)
                            imgurls.add(imgurl);
                        imgurl = jo.getString("imageurl4");
                        if (imgurl.length() > 0)
                            imgurls.add(imgurl);

                        //adding for headers
                        String header = jo.getString("uname") + "";

                        sectionManager = (sectionManager + 1) % 1;
                        sectionFirstPosition = count + headerCount;

                        headerCount += 1;
                        Home_List_Data hld2 = new Home_List_Data(header, "header", sectionManager, sectionFirstPosition);
                        mItems.add(hld2);

                        if (jo.getString("utype").compareTo("private") == 0)
                            isprivate = true;
                        else
                            isprivate = false;

                        //content
                        Home_List_Data hld = new Home_List_Data(jo.getString("uname") + "", isprivate, "contentuser", sectionManager, sectionFirstPosition);
                        hld.setProfilepicurl(jo.getString("profile_pic"));
                        hld.setPricenow(jo.getString("price_now"));
                        hld.setPricewas(jo.getString("price_was"));
                        hld.setSize(jo.getString("size"));
                        hld.setLikestotal(jo.getInt("post_total_likes"));
                        hld.setImageurls(imgurls);
                        hld.setPost_id(jo.getString("post_id"));
                        hld.setDescription(jo.getString("description"));
                        hld.setUname(jo.getString("uname"));
                        hld.setLikedvalue(jo.getString("isliked"));
                        hld.setColors(jo.getString("color"));
                        hld.setConditon(jo.getString("condition"));
                        hld.setCategory(jo.getInt("category_type"));
                        hld.setBrandname(jo.getString("brand_name"));
                        hld.setUserid(jo.getString("user_id"));
                        hld.setProdtype(jo.getString("prod_type"));
                        hld.setTime(getMilliseconds(jo.getString("date_time")));
                        hld.setTotalcomments(jo.getInt("post_total_comment"));
                        hld.setIssold(jo.getInt("issold"));

                        JSONArray commnets = jo.getJSONArray("postcoments");

                        if (commnets.length() > 0) {

                            ArrayList<CommentBean> post_cont = new ArrayList<>();
                            for (int j = 0; j < commnets.length(); j++) {
                                JSONObject jsonObject = commnets.getJSONObject(j);
                                String uname = jsonObject.getString("uname");
                                String comment = jsonObject.getString("comment");
                                CommentBean cb = new CommentBean();
                                cb.setUname(uname);
                                cb.setComment(comment);
                                post_cont.add(cb);
                            }
                            hld.setUserpostcomments(post_cont);

                        } else {
                            ArrayList<CommentBean> post_cont = new ArrayList<>();
                            hld.setUserpostcomments(post_cont);

                        }
                        if (hld.getCategory() == 2) {
                            String size = "";

                            try {
                                String[] lndbagsize = hld.getSize().split(",");
                                if (lndbagsize.length > 1) {
                                    for (int j = 0; j < lndbagsize.length; j++) {
                                        size = size + ConstantValues.bagsize[Integer.parseInt(lndbagsize[j])] + ",";
                                    }
                                    hld.setSize(size);
                                } else
                                    hld.setSize(ConstantValues.bagsize[Integer.parseInt(hld.getSize())]);


                            } catch (Exception ex) {
                                Log.e("error", ex.getMessage());
                            }
                        } else if (hld.getCategory() == 4) {
                            String color = "";

                            try {
                                String[] lndcolormetaltype = hld.getColors().split(",");
                                if (lndcolormetaltype.length > 1) {
                                    for (int j = 0; j < lndcolormetaltype.length; j++) {
                                        color = color + ConstantValues.metaltype[Integer.parseInt(lndcolormetaltype[j])] + ",";
                                    }
                                    hld.setColors(color);
                                } else
                                    hld.setColors(ConstantValues.metaltype[Integer.parseInt(hld.getColors())]);


                            } catch (Exception ex) {
                                Log.e("error", ex.getMessage());
                            }
                        }

                        //for header
                        hld2.setProfilepicurl(jo.getString("profile_pic"));
                        hld2.setIssold(jo.getInt("issold"));
                        hld2.setHeadertype(0);
                        hld2.setPost_id(jo.getString("post_id"));

                        hld2.setUname(jo.getString("uname"));
                        hld2.setLikedvalue(jo.getString("isliked"));
                        //  hld2.setLikestotal(jo.getInt("likes"));

                        hld2.setUserid(jo.getString("user_id"));
                        hld2.setBrandname(jo.getString("brand_name"));
                        hld2.setSwapstatus(jo.getInt("swap_status"));
                        checkFavorate(hld);
                        mItems.add(hld);
                        count++;
                    }


                    adapter.notifyDataSetChanged();
                    skipdata = items.size();

                    if (jarray.length() == 0) {

                        dataleft = false;
                    }

                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  dialog.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "11");
                params.put("user_id", SingleTon.pref.getString("user_id", ""));
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

    private void checkFavorate(Home_List_Data hld) {
        FavoriteData fd = SingleTon.db.getContact(hld.getPost_id());
        if (fd != null) {
            hld.setIsfavorate(true);
        }
    }
}
