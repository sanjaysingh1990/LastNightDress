package com.eowise.recyclerview.stickyheaders.samples.MyPurchases;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.R;

public class OrderDetailsPurchased extends AppCompatActivity {

    TextView heading;
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
            setContentView(R.layout.purchases_inprocess_layout);

        }
        else if(status.compareToIgnoreCase("delivered")==0)
        {
            if(ImageLoaderImage.pref.getBoolean("rated",false))
            setContentView(R.layout.purchases_editrating_layout);
            else
            setContentView(R.layout.purchases_delivered_layout);

        }
        else if(status.compareToIgnoreCase("cancelled")==0)
        {
            setContentView(R.layout.purchases_item_cancelled_layout);
            TextView cancelled= (TextView) findViewById(R.id.cancelled);
            //appying spannable
            String text=cancelled.getText().toString()+"    ";
            SpannableStringBuilder ssb = new SpannableStringBuilder(text);
            Bitmap smiley = BitmapFactory.decodeResource(getResources(), R.drawable.shippinglabel_icon);
            ssb.setSpan( new ImageSpan( smiley ), text.length()-3,text.length()-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE );
            cancelled.setText(ssb, TextView.BufferType.SPANNABLE);
        }
        else if(status.compareToIgnoreCase("cancelled2")==0)
        {
            setContentView(R.layout.purchases_cancelled_layout);

        }
        else if(status.compareToIgnoreCase("shipped")==0)
        {
            setContentView(R.layout.purchases_shipped_layout);

        }
        else if(status.compareToIgnoreCase("claim processing")==0)
        {
            setContentView(R.layout.purchases_claimprocessing_layout);

        }
        else if(status.compareToIgnoreCase("rating reported")==0)
        {
            setContentView(R.layout.purchases_ratingreported_layout);

        }
        else if(status.compareToIgnoreCase("claim approved")==0)
        {
            setContentView(R.layout.purchases_claimapproved_layout);

        }
        else if(status.compareToIgnoreCase("claim declined")==0)
        {
            setContentView(R.layout.purchases_claimdeclined_layout);

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
public void itemAccepted(View v)
{
    Intent rateuser=new Intent(this,RateUserActivity.class);
    startActivity(rateuser);
    finish();
}
    public void returnItem(View v)
    {
      setContentView(R.layout.purchases_claim_layout);
     final TextView continueclaim= (TextView) findViewById(R.id.claimcontinue);
     final EditText commentvalue= (EditText) findViewById(R.id.yourcomment);
     continueclaim.setClickable(false);
     commentvalue.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
             if (s.length() > 0) {
                 continueclaim.setClickable(true);
                 continueclaim.setTextColor(Color.parseColor("#be4d66"));
             }
         }

         @Override
         public void afterTextChanged(Editable s) {

         }
     });

        continueclaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent purchasereturn = new Intent(OrderDetailsPurchased.this, PurchaseShipplingLabelActivity.class);
                purchasereturn.putExtra("claimtext", commentvalue.getText() + "");
                startActivity(purchasereturn);
                finish();

            }
        });
    }
    public void cancelClaim(View v)
    {
        setContentView(R.layout.purchases_delivered_layout);

    }
    public void continueShopping(View v)
    {
         finish();
    }
    public void editRating(View v)
    {
        Intent purchasereturn = new Intent(OrderDetailsPurchased.this,RateUserActivity.class);
        purchasereturn.putExtra("ratedvalue",2);
        purchasereturn.putExtra("comment", "Terrible service, I will never buy anything from this user again.");

        startActivity(purchasereturn);
        finish();
    }

}
