package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.data.ReviewsData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aurel on 22/09/14.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private static List<ReviewsData> items;
    static Context mContext;

    public ReviewsAdapter(Context context, List<ReviewsData> data) {
        this.mContext = context;
        this.items = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lnd_review_item_layout, viewGroup, false);


        ViewHolder vh = new ViewHolder(itemView);
        return vh;

    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
      final  ReviewsData rd = items.get(position);
        viewHolder.rating.setNumStars(rd.getRated_value());
        viewHolder.uname.setText(Capitalize.capitalizeFirstLetter(rd.getReviewbyuname()));
        viewHolder.message.setText(rd.getReviewmessage());
        if (rd.isreplied()==1) {
            viewHolder.reply.setText("Replied");
            viewHolder.reply.setVisibility(View.VISIBLE);
            viewHolder.replyby.setText(Capitalize.capitalizeFirstLetter(SingleTon.pref.getString("uname", "")));
            viewHolder.replymessage.setText(rd.getReviewreplied());
            viewHolder.reply.setOnClickListener(new MyEvent(viewHolder.repliedview));

        }
        // viewHolder.reply.setVisibility(View.GONE);
        else if(rd.isreplied()==2) {
            viewHolder.reply.setText("Reply");
            viewHolder.reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reply(rd);
                }
            });

        }
        else if(rd.isreplied()==3) {
            viewHolder.reply.setText("");


        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    private void reply(final ReviewsData rd) {
        final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // dialog.getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c6290")));
        dialog.setContentView(R.layout.lnd_reviewreply_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
        dialog.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //view reference
        RatingBar rating= (RatingBar) dialog.findViewById(R.id.ratingBar);
        TextView reviewbyuser = (TextView) dialog.findViewById(R.id.reviewbyuser);
        TextView reviewmessage = (TextView) dialog.findViewById(R.id.reviewmessage);
        final EditText replybox = (EditText) dialog.findViewById(R.id.replybox);
        //setting value
        rating.setNumStars(rd.getRated_value());
        reviewbyuser.setText(rd.getReviewbyuname());
        reviewmessage.setText(rd.getReviewmessage());
        TextView submit = (TextView) dialog.findViewById(R.id.submit);
        submit.setTypeface(SingleTon.robotomedium);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(replybox.getText().length()==0)
                    return;
                RequestQueue queue = Volley.newRequestQueue(mContext);
                StringRequest sr = new StringRequest(Request.Method.POST,"http://52.76.68.122/lnd/androidiosphpfiles/lndusers.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // prog.setVisibility(View.GONE);

                         Log.e("reply", response.toString());
                        try {
                            JSONObject jobj = new JSONObject(response.toString());
                            if(jobj.getBoolean("status")) {
                                dialog.dismiss();
                                rd.setIsreplied(1);
                                rd.setReviewreplied(replybox.getText().toString());
                                notifyDataSetChanged();

                            }
                            }
                        catch(Exception ex)
                        {
                            //Log.e("json parsing error",ex.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Log.e("response",error.getMessage()+"");
                    }
                }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("rqid","16");
                        params.put("reply",replybox.getText().toString());

                        params.put("review_id", rd.getReviewid());


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
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView uname, message, reply;
        public RatingBar rating;
        public LinearLayout repliedview;
        public TextView replyby,replymessage;

        public ViewHolder(View itemView) {
            super(itemView);
            uname = (TextView) itemView.findViewById(R.id.uname);
            message = (TextView) itemView.findViewById(R.id.message);
            reply = (TextView) itemView.findViewById(R.id.reply);
            rating = (RatingBar) itemView.findViewById(R.id.ratingBar);
            repliedview = (LinearLayout) itemView.findViewById(R.id.repliedview);
            replyby = (TextView) itemView.findViewById(R.id.replyby);
            replymessage = (TextView) itemView.findViewById(R.id.replytext);

        }


    }

    class MyEvent implements View.OnClickListener {
        LinearLayout reply;

        public MyEvent(LinearLayout ll) {
            reply = ll;
        }

        @Override
        public void onClick(View view) {
            ((TextView) view).setVisibility(View.GONE);
            reply.setVisibility(View.VISIBLE);
        }
    }

}
