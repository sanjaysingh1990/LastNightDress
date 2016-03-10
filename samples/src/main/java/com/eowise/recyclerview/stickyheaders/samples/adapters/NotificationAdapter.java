package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.LndMessage.SwapRequestActivity;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.data.NotificationData;
import com.eowise.recyclerview.stickyheaders.samples.data.PersonDataProvider;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aurel on 22/09/14.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private static Map<Integer, Parcelable> scrollStatePositionsMap = new HashMap<>();
    private static List<NotificationData> items;
    private PersonDataProvider personDataProvider;
    static Activity activity;
    static int count=0;
    public static NotificationData nd;
        public NotificationAdapter(Activity act, List<NotificationData> data) {
        this.activity = act;
        this.personDataProvider = personDataProvider;
        this.items = data;
        setHasStableIds(true);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item_row, viewGroup, false);



        ViewHolder vh = new ViewHolder(itemView);
        return vh;

    }

    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
      NotificationData nd=items.get(position);

       viewHolder.swapcheck.setVisibility(View.GONE);
       /* if(nd.getNotitype().compareTo("2")==0) {
            ImageLoaderImage.imageLoader.displayImage(nd.getImgurl(), viewHolder.notiimage, ImageLoaderImage.options);
            ImageLoaderImage.imageLoader.displayImage(nd.getProfilepicimg(), viewHolder.notiprofilepic, ImageLoaderImage.options2);

            String text = "<font color=#be4d66><b>"+UpperName(nd.getUname())+"</b></font><medium> commented on </medium>"+"<font color=#be4d66><b> Gorgeous Dresses </b></font>post: <medium>"+nd.getComment()+"</medium>"+"<font color=#dadada><small>  "+nd.getTime()+"</small></font>";
                   viewHolder.notiuname.setText(Html.fromHtml(text));

        }*/

        //swap request
         if(nd.getNotitype().compareTo("3")==0)

        {
            if(nd.getSwapstatus().compareTo("1")==0)
            viewHolder.swapcheck.setVisibility(View.VISIBLE);
            else
                viewHolder.swapcheck.setVisibility(View.GONE);

            ImageLoaderImage.imageLoader.displayImage(nd.getImgurl(), viewHolder.notiimage, ImageLoaderImage.options);
            ImageLoaderImage.imageLoader.displayImage(nd.getProfilepicimg(), viewHolder.notiprofilepic, ImageLoaderImage.options3);

            String text = "<font color=#be4d66><b>"+UpperName(nd.getUname())+"</b></font><medium> requested a </medium>"+"<font color=#be4d66><b> SWAP </b></font><font color=#dadada><small>"+nd.getTime()+"</small></font>";
            viewHolder.notiuname.setText(Html.fromHtml(text));


        }
        else if(nd.getNotitype().compareTo("4")==0)

        {
            ImageLoaderImage.imageLoader.displayImage(nd.getProfilepicimg(), viewHolder.notiprofilepic, ImageLoaderImage.options2);
            viewHolder.notiimage.setVisibility(View.GONE);
            String text = "<font color=#be4d66><b>"+UpperName(nd.getUname())+"</b></font><medium> started following you. </medium>"+"<font color=#dadada><small>   "+nd.getTime()+"</small></font>";
            viewHolder.notiuname.setText(Html.fromHtml(text));


        }
        else if(nd.getNotitype().compareTo("1")==0)

        {
            ImageLoaderImage.imageLoader.displayImage(nd.getImgurl(), viewHolder.notiimage, ImageLoaderImage.options);
            ImageLoaderImage.imageLoader.displayImage(nd.getProfilepicimg(), viewHolder.notiprofilepic, ImageLoaderImage.options2);

            String text = "<font color=#be4d66><b>"+UpperName(nd.getUname())+"</b></font><medium> liked you photo. </medium><font color=#dadada><small>"+nd.getTime()+"</small></font>";
            viewHolder.notiuname.setText(Html.fromHtml(text));


        }
    }
private String UpperName(String uname)
{
   return Character.toUpperCase(uname.charAt(0)) + uname.substring(1);
}
    @Override
    public int getItemCount() {
        return items.size();
    }



    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView notiuname;
        ImageView notiimage,notiprofilepic;
        LinearLayout swapcheck;
        TextView swapcontinue,swapcancle;
        public ViewHolder(View itemView) {
            super(itemView);
         notiuname= (TextView) itemView.findViewById(R.id.notiuname);
         notiimage=(ImageView)itemView.findViewById(R.id.notiimage);
         notiprofilepic=(ImageView)itemView.findViewById(R.id.notipropic);
         swapcheck=(LinearLayout)itemView.findViewById(R.id.swapcheck);
         swapcontinue= (TextView) itemView.findViewById(R.id.swapcontinue);
         swapcancle= (TextView) itemView.findViewById(R.id.swapcancel);
         swapcontinue.setOnClickListener(this);
         swapcancle.setOnClickListener(this);
         notiimage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.swapcontinue)
            {
                Intent swaprequest=new Intent(activity, SwapRequestActivity.class);
                swaprequest.putExtra("data",items.get(getAdapterPosition()));
                nd=items.get(getAdapterPosition());
                activity.startActivityForResult(swaprequest, 2);
            }
            else if(v.getId()==R.id.swapcancel)
            {
               getData(items.get(getAdapterPosition()).getNotification_id(),items.get(getAdapterPosition()));

            }
         }
    }
    public  void getData(final String notiid,final NotificationData nd) {
        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(activity);
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
                        nd.setSwapstatus("0");
                     //   NotificationFragment.recyclerAdapter.notifyDataSetChanged();
                       notifyDataSetChanged();

                        Toast.makeText(activity,jobj.getString("message"),Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(activity,jobj.getString("message"),Toast.LENGTH_LONG).show();
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

}
