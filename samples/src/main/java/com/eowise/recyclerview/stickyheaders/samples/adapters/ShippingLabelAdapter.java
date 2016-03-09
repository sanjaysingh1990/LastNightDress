package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityTestCase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.LndMessage.SwapRequestActivity;
import com.eowise.recyclerview.stickyheaders.samples.MyPurchases.PurchaseShipplingLabelActivity;
import com.eowise.recyclerview.stickyheaders.samples.MySales.ShippingLabelActivity;
import com.eowise.recyclerview.stickyheaders.samples.NewMessage.SendMessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.data.MessageToFriendsData;
import com.eowise.recyclerview.stickyheaders.samples.data.PersonDataProvider;
import com.eowise.recyclerview.stickyheaders.samples.data.ShippingLabelData;
import com.eowise.recyclerview.stickyheaders.samples.data.ShopData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aurel on 22/09/14.
 */
public class ShippingLabelAdapter extends RecyclerView.Adapter<ShippingLabelAdapter.ViewHolder> {
    private static Map<Integer, Parcelable> scrollStatePositionsMap = new HashMap<>();
    private static List<ShippingLabelData> items;
    private PersonDataProvider personDataProvider;
    static Activity activity;
    static int count=0;
    private ShippingLabelData sld;
    public ShippingLabelAdapter(Activity act, List<ShippingLabelData> data) {
        this.activity =act;
        this.personDataProvider = personDataProvider;
        this.items = data;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shippinglevel_item_row, viewGroup, false);

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
          ShippingLabelData shiplabeldata=items.get(position);
        if(shiplabeldata.isSelected())
             viewHolder.labelselected.setVisibility(View.VISIBLE);
        else
            viewHolder.labelselected.setVisibility(View.GONE);

           viewHolder.packageweight.setText(shiplabeldata.getPackageweight());
        viewHolder.packageprice.setText(shiplabeldata.getPackageprice());
        viewHolder.ll.setOnClickListener(new mySelection(viewHolder.labelselected, position));


    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public  class ViewHolder extends RecyclerView.ViewHolder  {
        TextView packageweight,packageprice;
        RelativeLayout labelselected;
        LinearLayout ll;
        public ViewHolder(View itemView) {
        super(itemView);
        packageweight= (TextView) itemView.findViewById(R.id.packagewight);
        packageprice= (TextView) itemView.findViewById(R.id.packagerate);
        packageweight.setTypeface(ImageLoaderImage.robotomedium);
        packageprice.setTypeface(ImageLoaderImage.robotomedium);
        labelselected= (RelativeLayout) itemView.findViewById(R.id.labelselected);
        ll= (LinearLayout) itemView.findViewById(R.id.mainlable);

            ll.setClickable(true);

        }


    }
    private class mySelection implements View.OnClickListener
    {
        RelativeLayout fl;
        int pos;
        public mySelection(RelativeLayout fl,int pos)
        {
            this.fl=fl;
            this.pos=pos;
        }
        @Override
        public void onClick(View v)
        {
            try {
                ShippingLabelActivity shipping = (ShippingLabelActivity) activity;

                shipping.changeColor(items.get(pos).getPackageprice());
            }
            catch(Exception ex)
            {
                PurchaseShipplingLabelActivity shipping = (PurchaseShipplingLabelActivity) activity;

                shipping.changeColor(items.get(pos).getPackageprice());

            }
                if(sld!=null)
            {
                sld.setSelected(false);
                notifyDataSetChanged();
            }
            ShippingLabelData sd=items.get(pos);
            sld=sd;
            if(sd.isSelected())
            {

                fl.setVisibility(View.GONE);
                sd.setSelected(false);

            }
            else
            {

                fl.setVisibility(View.VISIBLE);
                sd.setSelected(true);

            }

        }

    }



}
