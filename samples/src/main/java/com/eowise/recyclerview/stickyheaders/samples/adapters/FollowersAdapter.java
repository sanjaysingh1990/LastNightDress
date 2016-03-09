package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.eowise.recyclerview.stickyheaders.samples.UserProfile.OtherUserProfileActivity;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.data.FollowersFollowingData;
import com.eowise.recyclerview.stickyheaders.samples.data.PersonDataProvider;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by aurel on 22/09/14.
 */
public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {
    private static Map<Integer, Parcelable> scrollStatePositionsMap = new HashMap<>();
    private static List<FollowersFollowingData> items;
    private PersonDataProvider personDataProvider;
    static Context mContext;
    static int count=0;

    public FollowersAdapter(Context context, List<FollowersFollowingData> data) {
        this.mContext = context;
        this.items = data;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.follower_row, viewGroup, false);

        //Perhaps the first most crucial part. The ViewPager loses its width information when it is put
        //inside a RecyclerView. It needs to be explicitly resized, in this case to the width of the
        //screen. The height must be provided as a fixed value.
        /*DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        v.getLayoutParams().width = displayMetrics.widthPixels;
        v.requestLayout();*/

        ViewHolder vh = new ViewHolder(itemView);
        return vh;

    }

    @Override
    public long getItemId(int position) {
//        return items.get(position).hashCode();
        return 1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        FollowersFollowingData fd=items.get(position)   ;
        ImageLoaderImage.imageLoader.displayImage(fd.getUserpic(), viewHolder.profilepic, ImageLoaderImage.options2);
           viewHolder.folleruname.setText(Capitalize.capitalize(fd.getUname()));
           if(fd.getStatus().compareTo("0")==0)
           {



               viewHolder.follerstatus.setBackgroundResource(R.drawable.rounded_corners3);
               viewHolder.follerstatus.setTextColor(Color.parseColor("#be4d66"));
               viewHolder.follerstatus.setText("+ Follow");
           }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
     @Bind(R.id.followeruname)   TextView folleruname;
        @Bind(R.id.followstatus)   TextView follerstatus;

        @Bind(R.id.followerprofilepic) ImageView profilepic;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
         follerstatus.setOnClickListener(this);
         itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.followstatus) {
                String user_id = ImageLoaderImage.pref.getString("user_id", "");
                followunfollow(user_id, items.get(getAdapterPosition()).getUserid(), (TextView) v);
            }
            else
            {
                Intent otherlnduser=new Intent(mContext, OtherUserProfileActivity.class);
                otherlnduser.putExtra("uname",items.get(getAdapterPosition()).getUname());
                mContext.startActivity(otherlnduser);
            }
            }
    }

    public  static void followunfollow(final String followerid,final String followingid,final TextView v){
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://52.76.68.122/lnd/androidiosphpfiles/followfollowing.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();

              //  Log.e("follunfoll", response.toString());

                try {
                    JSONObject jobj=new JSONObject(response.toString());
                    if(jobj.getBoolean("status"))
                    {
                        if(jobj.getString("value").compareTo("2")==0)
                        {
                            v.setBackgroundResource(R.drawable.rounded_corners);
                            v.setTextColor(Color.parseColor("#dadada"));
                            v.setText("Following");
                        }
                        else
                        {
                            v.setBackgroundResource(R.drawable.rounded_corners3);
                            v.setTextColor(Color.parseColor("#be4d66"));
                            v.setText("+ Follow");
                        }
                    }
                  else
                    {
                        Toast.makeText(mContext,"Something went wrong",Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception ex)
                {
                    Log.e("json parsing error", ex.getMessage());
                }
               // recyclerAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                //  Log.e("response",error.getMessage()+"");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("rqid","2");
                params.put("followerid",followerid);
                params.put("followingid",followingid);

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
