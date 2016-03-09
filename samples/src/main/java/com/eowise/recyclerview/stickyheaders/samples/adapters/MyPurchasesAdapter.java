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

import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.MyPurchases.OrderDetailsPurchased;
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
public class MyPurchasesAdapter extends RecyclerView.Adapter<MyPurchasesAdapter.ViewHolder> {
    private static Map<Integer, Parcelable> scrollStatePositionsMap = new HashMap<>();
    private static List<MySalesData> items;
    private PersonDataProvider personDataProvider;
    static Activity activity;
    static int count=0;
    public static NotificationData nd;
        public MyPurchasesAdapter(Activity act, List<MySalesData> data) {
        this.activity = act;
        this.personDataProvider = personDataProvider;
        this.items = data;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mypurchases_item_row, viewGroup, false);



        ViewHolder vh = new ViewHolder(itemView);
        return vh;

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
            Intent ordetails=new Intent(activity, OrderDetailsPurchased.class);

            if(status.compareToIgnoreCase("in process")==0)
            {
                ordetails.putExtra("type","in process");

            }

            else if(status.compareToIgnoreCase("delivered")==0)
            {
                ordetails.putExtra("type","delivered");

            }
            else if(status.compareToIgnoreCase("cancelled")==0)
            {
                if(utype.compareTo("buyer")==0)
                ordetails.putExtra("type","cancelled");
                else
                    ordetails.putExtra("type","cancelled2");

            }
            else if(status.compareToIgnoreCase("shipped")==0)
            {
                ordetails.putExtra("type","shipped");

            }
            else if(status.compareToIgnoreCase("claim processing")==0)
            {
                ordetails.putExtra("type","claim processing");

            }
            else if(status.compareToIgnoreCase("rating reported")==0)
            {
                ordetails.putExtra("type","rating reported");

            }
            else if(status.compareToIgnoreCase("claim approved")==0)
            {
                ordetails.putExtra("type","claim approved");

            }
            else if(status.compareToIgnoreCase("claim declined")==0)
            {
                ordetails.putExtra("type","claim declined");

            }
            activity.startActivity(ordetails);
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
        TextView brandname;
        TextView selleruname,price,status;
        TextView sellertext,grandtotal,statustext;
        LinearLayout orderdetails;

        public ViewHolder(View itemView) {
            super(itemView);
         brandname= (TextView) itemView.findViewById(R.id.brandname);
         selleruname= (TextView) itemView.findViewById(R.id.sellername);
         price= (TextView) itemView.findViewById(R.id.brandname);
         status= (TextView) itemView.findViewById(R.id.status);
            sellertext= (TextView) itemView.findViewById(R.id.sellertext);
            grandtotal= (TextView) itemView.findViewById(R.id.grandtotaltext);
            statustext= (TextView) itemView.findViewById(R.id.statustext);
            orderdetails= (LinearLayout) itemView.findViewById(R.id.orderdetails);
            orderdetails.setClickable(true);
//applying custom fonts
            brandname.setTypeface(ImageLoaderImage.robotobold);
            selleruname.setTypeface(ImageLoaderImage.robotomedium);
            price.setTypeface(ImageLoaderImage.robotomedium);
            status.setTypeface(ImageLoaderImage.robotomedium);
            sellertext.setTypeface(ImageLoaderImage.robotoregular);
            grandtotal.setTypeface(ImageLoaderImage.robotoregular);
            statustext.setTypeface(ImageLoaderImage.robotoregular);

        }

        @Override
        public void onClick(View v) {

         }
    }


}
