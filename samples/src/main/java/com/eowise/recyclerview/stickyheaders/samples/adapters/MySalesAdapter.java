package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.MySales.OrderDetailsSales;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.Utils.RelativeTimeTextView;
import com.eowise.recyclerview.stickyheaders.samples.data.MySalesPurchasesData;
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
    private static List<MySalesPurchasesData> items;
    private PersonDataProvider personDataProvider;
    static Activity activity;
    static int count=0;
    public static NotificationData nd;
        public MySalesAdapter(Activity act, List<MySalesPurchasesData> data) {
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

    public MyEvents(String str)
    {

        status=str;

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
     MySalesPurchasesData msd=items.get(position);
        viewHolder.orderdetails.setOnClickListener(new MyEvents(msd.getOrder_purchase_status()));
        viewHolder.status.setText(msd.getOrder_purchase_status());
        viewHolder.brandname.setText(msd.getBrand_name());
        viewHolder.time.setReferenceTime(msd.getOrder_date());
        viewHolder.buyeruname.setText(msd.getSeller_name());
        SingleTon.imageLoader.displayImage(msd.getImage_url(),viewHolder.productimage, SingleTon.options3);
        SingleTon.imageLoader.displayImage(msd.getProfile_pic(), viewHolder.profilepic, SingleTon.options3);

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
        ImageView profilepic,productimage;
        RelativeTimeTextView time;
        public ViewHolder(View itemView) {
            super(itemView);
            buyeruname= (TextView) itemView.findViewById(R.id.buyeruname);
            brandname= (TextView) itemView.findViewById(R.id.brandname);
            status= (TextView) itemView.findViewById(R.id.status);
            statustext= (TextView) itemView.findViewById(R.id.statustext);
            orderdetails= (LinearLayout) itemView.findViewById(R.id.orderdetails);
            profilepic= (ImageView) itemView.findViewById(R.id.userprofilepic);
            productimage= (ImageView) itemView.findViewById(R.id.productimage);
            time= (RelativeTimeTextView) itemView.findViewById(R.id.showtime);
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
