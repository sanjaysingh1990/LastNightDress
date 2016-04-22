package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.MySales.OrderDetailsSales;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.data.MySalesData;
import com.eowise.recyclerview.stickyheaders.samples.data.NotificationData;
import com.eowise.recyclerview.stickyheaders.samples.data.PersonDataProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aurel on 22/09/14.
 */
public class MySalesAdapter extends RecyclerView.Adapter<MySalesAdapter.ViewHolder> {
    private static Map<Integer, Parcelable> scrollStatePositionsMap = new HashMap<>();
    private static List<MySalesData> items;
    private PersonDataProvider personDataProvider;
    static Activity activity;
    static int count=0;
    public static NotificationData nd;
        public MySalesAdapter(Activity act, List<MySalesData> data) {
        this.activity = act;

        this.items = data;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_sales_row_layout, viewGroup, false);



        ViewHolder vh = new ViewHolder(itemView);
        return vh;

    }
class MyEvents implements View.OnClickListener
{
    String status;
    String utype;
    public MyEvents(String str,String utype)
    {

        status=str;
        this.utype=utype;
    }
    @Override
    public void onClick(View v) {
        Intent ordetails=new Intent(activity, OrderDetailsSales.class);

        if(status.compareToIgnoreCase("in process")==0)
        {
           ordetails.putExtra("type","in process");

        }
       else if(status.compareToIgnoreCase("Shipped")==0)
        {
            ordetails.putExtra("type","Shipped");

        }
        else if(status.compareToIgnoreCase("delivered")==0)
        {
            ordetails.putExtra("type","delivered");

        }
        else if(status.compareToIgnoreCase("Order cancelled")==0)
        {
            if(utype.compareTo("buyer")==0)
                ordetails.putExtra("type","cancelled");
            else
                ordetails.putExtra("type","cancelled2");


        }
        else if(status.compareToIgnoreCase("claim processing")==0)
        {
            ordetails.putExtra("type","claim processing");

        }
        else if(status.compareToIgnoreCase("claim approved")==0)
        {
            ordetails.putExtra("type","claim approved");

        }

        else if(status.compareToIgnoreCase("claim declined")==0)
        {
            ordetails.putExtra("type","claim declined");

        }
        else if(status.compareToIgnoreCase("Report rating")==0)
        {
            ordetails.putExtra("type","Report rating");

        }
        activity.startActivity(ordetails);
    }
}
    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
     MySalesData msd=items.get(position);
       viewHolder.orderdetails.setOnClickListener(new MyEvents(msd.getStatus(),msd.getUsertype()));
        viewHolder.status.setText(msd.getStatus());
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
        TextView buyeruname;
        TextView brandname,status;
        TextView statustext;
        LinearLayout orderdetails;
        public ViewHolder(View itemView) {
            super(itemView);
            buyeruname= (TextView) itemView.findViewById(R.id.buyeruname);
            brandname= (TextView) itemView.findViewById(R.id.brandname);
            status= (TextView) itemView.findViewById(R.id.status);
            statustext= (TextView) itemView.findViewById(R.id.statustext);
            orderdetails= (LinearLayout) itemView.findViewById(R.id.orderdetails);
            orderdetails.setClickable(true);
         //   orderdetails.setOnClickListener(this);
//appyling font

            buyeruname.setTypeface(SingleTon.robotobold);
            brandname.setTypeface(SingleTon.robotomedium);
            status.setTypeface(SingleTon.robotomedium);
            statustext.setTypeface(SingleTon.robotoregular);

        }

        @Override
        public void onClick(View v) {
         }
    }


}
