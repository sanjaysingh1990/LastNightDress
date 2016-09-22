package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.eowise.recyclerview.stickyheaders.samples.Favorates.Favorates;
import com.eowise.recyclerview.stickyheaders.samples.LndPostFullView.LndFullFavorateStickyActivity;
import com.eowise.recyclerview.stickyheaders.samples.LndPostFullView.LndFullStickyActivity;
import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;

import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SQLDB.FavoriteData;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.Home_List_Data;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoratesAdapter extends RecyclerView.Adapter<FavoratesAdapter.TextViewHolder> {
    private static List<FavoriteData> labels;
    private static Activity activity;

    public FavoratesAdapter(List<FavoriteData> data, Activity activity) {
        this.labels = data;
        this.activity = activity;
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photositem, parent, false);
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        ViewGroup.LayoutParams params = view.getLayoutParams();

        params.height = (width / 3);
        view.requestLayout();

        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TextViewHolder holder, final int position) {
        // final String label = labels.get(position);
        FavoriteData fd = labels.get(position);
        SingleTon.imageLoader.displayImage(fd.getImageurl(), holder.img, SingleTon.options);
        holder.price.setText("$" + fd.getCost());
        if ((position + 1) % 3 == 0)
            holder.separator.setVisibility(View.GONE);
        else
            holder.separator.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return labels.size();
    }

    public class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView price;
        public ImageView img;
        private View separator;
        private ImageButton delfav;

        public TextViewHolder(View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.price);
            img = (ImageView) itemView.findViewById(R.id.image);
            delfav = (ImageButton) itemView.findViewById(R.id.delefav);
            separator = itemView.findViewById(R.id.separator);
            delfav.setOnClickListener(this);
            img.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
           switch (v.getId()) {

               case R.id.delefav:
               Favorates fav = (Favorates) activity;
               delFav(SingleTon.pref.getString("user_id",""),labels.get(getAdapterPosition()).getPostid());

                   fav.delFavorite(getAdapterPosition());
                   notifyDataSetChanged();
               break;
               case R.id.image:
                   Intent fullfav = new Intent(activity, LndFullFavorateStickyActivity.class);
                   fullfav.putExtra("post_location", getAdapterPosition());

                   activity.startActivityForResult(fullfav,9);
                   break;

           }
           }
    }
    public void  delFav(final String userid,final String postid)
    {

        RequestQueue queue = Volley.newRequestQueue(activity);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_NOTIFICATION_SETTING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //  Log.e("json", response.toString());
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status")) {

                        Toast.makeText(activity, jobj.getString("message"), Toast.LENGTH_LONG).show();


                    } else {
                        Toast.makeText(activity, jobj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //  Log.e("response",error.getMessage()+"");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",userid);
                params.put("rqid", "6");
                params.put("post_id",postid);


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