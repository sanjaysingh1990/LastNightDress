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
import com.eowise.recyclerview.stickyheaders.samples.MyPurchases.OrderDetailsPurchased;
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
public class MyPurchasesAdapter extends RecyclerView.Adapter<MyPurchasesAdapter.ViewHolder> {
    private static Map<Integer, Parcelable> scrollStatePositionsMap = new HashMap<>();
    private static List<MySalesPurchasesData> items;
    private PersonDataProvider personDataProvider;
    static Activity activity;
    static int count = 0;
    public static NotificationData nd;

    public MyPurchasesAdapter(Activity act, List<MySalesPurchasesData> data) {
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
        MySalesPurchasesData msd = items.get(position);
        if(msd.getOrder_purchase_status().contains("cancelled"))
            viewHolder.status.setText(msd.getOrder_purchase_status().substring(0,msd.getOrder_purchase_status().length()-1));
        else
            viewHolder.status.setText(msd.getOrder_purchase_status());
        viewHolder.brandname.setText(msd.getBrand_name());
        viewHolder.time.setReferenceTime(msd.getOrder_date());
        viewHolder.selleruname.setText(msd.getSeller_name());
        viewHolder.price.setText("$" + msd.getTotal_amount());
        SingleTon.imageLoader.displayImage(msd.getImage_url(),viewHolder.productimage, SingleTon.options4);

    }





    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView brandname;
        TextView selleruname, price, status;
        TextView sellertext, grandtotal, statustext;
        LinearLayout orderdetails;
        RelativeTimeTextView time;
        ImageView productimage;

        public ViewHolder(View itemView) {
            super(itemView);
            brandname = (TextView) itemView.findViewById(R.id.brandname);
            selleruname = (TextView) itemView.findViewById(R.id.sellername);
            price = (TextView) itemView.findViewById(R.id.totalprice);
            status = (TextView) itemView.findViewById(R.id.status);
            sellertext = (TextView) itemView.findViewById(R.id.sellertext);
            grandtotal = (TextView) itemView.findViewById(R.id.grandtotaltext);
            statustext = (TextView) itemView.findViewById(R.id.statustext);
            orderdetails = (LinearLayout) itemView.findViewById(R.id.orderdetails);
            time = (RelativeTimeTextView) itemView.findViewById(R.id.showtime);
            productimage = (ImageView) itemView.findViewById(R.id.productimage);
            orderdetails.setClickable(true);
//applying custom fonts
            brandname.setTypeface(SingleTon.robotobold);
            selleruname.setTypeface(SingleTon.robotomedium);
            price.setTypeface(SingleTon.robotomedium);
            status.setTypeface(SingleTon.robotomedium);
            sellertext.setTypeface(SingleTon.robotoregular);
            grandtotal.setTypeface(SingleTon.robotoregular);
            statustext.setTypeface(SingleTon.robotoregular);
            orderdetails.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String status=items.get(getAdapterPosition()).getOrder_purchase_status();
            Intent ordetails = new Intent(activity, OrderDetailsPurchased.class);

            if (status.compareToIgnoreCase("in process") == 0) {
                ordetails.putExtra("type", "in process");

            } else if (status.compareToIgnoreCase("delivered") == 0) {
                ordetails.putExtra("type", "delivered");

            }
            else if(status.compareToIgnoreCase("Order cancelled1")==0)
            {
                ordetails.putExtra("type","cancelled2");

            }
            else if(status.compareToIgnoreCase("Order cancelled2")==0)
            {
                 ordetails.putExtra("type","cancelled");

            }

            else if (status.compareToIgnoreCase("shipped") == 0) {
                ordetails.putExtra("type", "shipped");

            } else if (status.compareToIgnoreCase("claim processing") == 0) {
                ordetails.putExtra("type", "claim processing");

            } else if (status.compareToIgnoreCase("rating reported") == 0) {
                ordetails.putExtra("type", "rating reported");

            } else if (status.compareToIgnoreCase("claim approved") == 0) {
                ordetails.putExtra("type", "claim approved");

            } else if (status.compareToIgnoreCase("claim declined") == 0) {
                ordetails.putExtra("type", "claim declined");

            } else if (status.compareToIgnoreCase("Report rating") == 0) {
                ordetails.putExtra("type", "Report rating");

            } else if (status.compareToIgnoreCase("Report rating") == 0) {
                ordetails.putExtra("type", "Report rating");

            } else if (status.compareToIgnoreCase("Report rating") == 0) {
                ordetails.putExtra("type", "Report rating");

            } else if (status.compareToIgnoreCase("Report rating") == 0) {
                ordetails.putExtra("type", "Report rating");

            } else if (status.compareToIgnoreCase("Report rating") == 0) {
                ordetails.putExtra("type", "Report rating");

            } else if (status.compareToIgnoreCase("Report rating") == 0) {
                ordetails.putExtra("type", "Report rating");

            } else if (status.compareToIgnoreCase("Report rating") == 0) {
                ordetails.putExtra("type", "Report rating");

            } else if (status.compareToIgnoreCase("Item accepted") == 0) {
                ordetails.putExtra("type", "Item accepted");

            }
            ordetails.putExtra("data",items.get(getAdapterPosition()));
            activity.startActivity(ordetails);
        }
    }


}
