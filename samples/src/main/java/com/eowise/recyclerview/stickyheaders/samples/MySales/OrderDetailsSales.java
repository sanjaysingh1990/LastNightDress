package com.eowise.recyclerview.stickyheaders.samples.MySales;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.R;

public class OrderDetailsSales extends AppCompatActivity {

    TextView heading;
    TextView brandtext,buyertext,listpricetext,yourearningtext,orderdatetext,ordernumbertext,statustext,inprocesstext,shippinglabel;
    TextView buyername,listprice,yourearning,orderdate,ordernumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    Bundle extra=   getIntent().getExtras();
    if(extra!=null)
    {
        String status=extra.getString("type");

        if(status.compareToIgnoreCase("in process")==0)
        {
            setContentView(R.layout.sales_inprocess_layout);
            getReference();
        }
        else if(status.compareToIgnoreCase("pending acceptance")==0)
        {
            setContentView(R.layout.sales_pending_acceptance_layout);

        }
        else if(status.compareToIgnoreCase("delivered")==0)
        {
            setContentView(R.layout.sales_delivered_layout);

        }
        else if(status.compareToIgnoreCase("cancelled")==0)
        {
            setContentView(R.layout.sales_cancelled_layout);

        }
        else if(status.compareToIgnoreCase("cancelled2")==0)
        {
            setContentView(R.layout.sales_item_cancelled_layout);
            TextView cancelled= (TextView) findViewById(R.id.cancelled);
            //appying spannable
            String text=cancelled.getText().toString()+"    ";
            SpannableStringBuilder ssb = new SpannableStringBuilder(text);
            Bitmap smiley = BitmapFactory.decodeResource(getResources(), R.drawable.shippinglabel_icon);
            ssb.setSpan( new ImageSpan( smiley ), text.length()-3,text.length()-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE );
            cancelled.setText(ssb, TextView.BufferType.SPANNABLE);

        }
        else if(status.compareToIgnoreCase("claim processing")==0)
        {
            setContentView(R.layout.sales_claimprocessing_layout);

        }
        else if(status.compareToIgnoreCase("claim declined")==0)
        {
            setContentView(R.layout.sales_claimdeclined_layout);

        }
        else if(status.compareToIgnoreCase("claim approved")==0)
        {
            setContentView(R.layout.sales_claimapproved_layout);

        }
    }
       heading= (TextView) findViewById(R.id.heading);
       //applying fonts
        heading.setTypeface(ImageLoaderImage.robotobold);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
    public void back(View v)
    {
        onBackPressed();
    }
public void shipping(View v)
{
    Intent shippingtest=new Intent(this,ShippingLabelActivity.class);
    startActivityForResult(shippingtest, 2);
}
    private void getReference()
    {
        brandtext=(TextView)findViewById(R.id.brandname);
        buyertext=(TextView)findViewById(R.id.buyertext);
        buyername=(TextView)findViewById(R.id.buyername);
        listpricetext=(TextView)findViewById(R.id.listpricetext);
        listprice=(TextView)findViewById(R.id.listprice);
        yourearningtext=(TextView)findViewById(R.id.yourearningtext);
        yourearning=(TextView)findViewById(R.id.yourearning);
        orderdatetext=(TextView)findViewById(R.id.orderdatetext);
        orderdate=(TextView)findViewById(R.id.orderdate);
        ordernumbertext=(TextView)findViewById(R.id.ordernumbertext);
        ordernumber=(TextView)findViewById(R.id.ordernumber);
        statustext=(TextView)findViewById(R.id.statustext);
        inprocesstext=(TextView)findViewById(R.id.statusprocesstext);
        shippinglabel=(TextView)findViewById(R.id.shippingstatus);

        //applying custom fonts
        brandtext.setTypeface(ImageLoaderImage.robotobold);
        buyertext.setTypeface(ImageLoaderImage.robotoregular);
        listpricetext.setTypeface(ImageLoaderImage.robotoregular);
        yourearningtext.setTypeface(ImageLoaderImage.robotoregular);
        orderdatetext.setTypeface(ImageLoaderImage.robotoregular);
        ordernumbertext.setTypeface(ImageLoaderImage.robotoregular);
        buyername.setTypeface(ImageLoaderImage.robotomedium);
        listprice.setTypeface(ImageLoaderImage.robotomedium);
        yourearning.setTypeface(ImageLoaderImage.robotomedium);
        orderdate.setTypeface(ImageLoaderImage.robotomedium);
        ordernumber.setTypeface(ImageLoaderImage.robotomedium);

        inprocesstext.setTypeface(ImageLoaderImage.robotomedium);
        statustext.setTypeface(ImageLoaderImage.robotoregular);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (data == null)
                return;

            String postids = data.getStringExtra("shipprice");

             Log.e("data", postids);
            String text=shippinglabel.getText().toString()+"    ";
            SpannableStringBuilder ssb = new SpannableStringBuilder(text);
            Bitmap smiley = BitmapFactory.decodeResource(getResources(), R.drawable.shippinglabel_icon);
            ssb.setSpan( new ImageSpan( smiley ), text.length()-3,text.length()-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE );
            shippinglabel.setText(ssb, TextView.BufferType.SPANNABLE);

            shippinglabel.setVisibility(View.VISIBLE);
        }
    }
    public void cancleOrder(View v)
    {
        setContentView(R.layout.sales_cancelling_layout);

    }
}
