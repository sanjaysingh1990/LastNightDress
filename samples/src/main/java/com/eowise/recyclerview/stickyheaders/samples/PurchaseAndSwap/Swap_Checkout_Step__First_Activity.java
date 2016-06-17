package com.eowise.recyclerview.stickyheaders.samples.PurchaseAndSwap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.LndBaseActivity;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.Utils.RelativeTimeTextView;
import com.eowise.recyclerview.stickyheaders.samples.Utils.TimeAgo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Swap_Checkout_Step__First_Activity extends LndBaseActivity {

    TextView actioninfo;
    EditText cancelreason;
//refrence for post

    @Bind(R.id.brandname)
    TextView brandname;
    @Bind(R.id.sellername)
    TextView sellername;
    @Bind(R.id.showtime)
    RelativeTimeTextView showtime;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.shippingprice)
    TextView shippingprice;
    @Bind(R.id.grandtotalprice)
    TextView grandtotalprice;
    @Bind(R.id.orderdate)
    TextView orderdate;
    @Bind(R.id.ordernumber)
    TextView ordernumber;
    @Bind(R.id.productimage)
    ImageView productimage;
    @Bind({R.id.sellertext, R.id.pricetext, R.id.shippingtext, R.id.grandtotaltext, R.id.orderdatetext, R.id.ordernumbertext})
    List<TextView> regularcheckout;
    @Bind(R.id.message_text)
    TextView msgtext;
    @Bind(R.id.heading)
    TextView heading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swap_checkout_first_step);
        ButterKnife.bind(this);
        for (int i = 0; i < regularcheckout.size(); i++) {
            regularcheckout.get(i).setTypeface(SingleTon.robotomedium);

        }
        //applying custom font
        heading.setTypeface(SingleTon.robotobold);
        brandname.setTypeface(SingleTon.robotobold);
        sellername.setTypeface(SingleTon.robotobold);
        showtime.setTypeface(SingleTon.robotobold);
        price.setTypeface(SingleTon.robotobold);
        shippingprice.setTypeface(SingleTon.robotobold);
        grandtotalprice.setTypeface(SingleTon.robotobold);
        orderdate.setTypeface(SingleTon.robotobold);
        ordernumber.setTypeface(SingleTon.robotobold);

        //read data from previous activity
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            try {
                JSONObject jobj = new JSONObject(extra.getString("data"));
                sellername.setText(Capitalize.capitalize(jobj.getString("uname")));
                price.setText("$" + jobj.getString("price"));
                //shippingprice.setText(jobj.getString("uname"));
                grandtotalprice.setText("$" + jobj.getString("total_amount"));
                // orderdate.setText(jobj.getString("uname"));
                ordernumber.setText(jobj.getString("order_id"));
                brandname.setText(Capitalize.capitalizeFirstLetter(jobj.getString("brand_name")));
                showtime.setReferenceTime(TimeAgo.getMilliseconds(jobj.getString("order_date")));
                SingleTon.imageLoader.displayImage(jobj.getString("image_url"), productimage, SingleTon.options4);
                spannableText();
            } catch (Exception e) {
                Log.e("jsonerror", e.getMessage() + "");
            }

        }
    }
private void spannableText()
{
    Spannable word = new SpannableString(getResources().getString(R.string.first_step_heading));

    word.setSpan(new ForegroundColorSpan(getColorfromResource(this,R.color.lndcolor)), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    msgtext.setText(word);



}
    public void cancleOrder(View v) {
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            Intent canceswap = new Intent(this, Swap_Checkout_Cancel_Activity.class);
            canceswap.putExtra("data",extra.getString("data"));
            startActivity(canceswap);

        }


    }

    private void showInfo(String text, TextView txt) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);
        Bitmap smiley = BitmapFactory.decodeResource(getResources(), R.drawable.shippinglabel_icon);
        ssb.setSpan(new ImageSpan(smiley), text.length() - 3, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txt.setText(ssb, TextView.BufferType.SPANNABLE);
    }

    public void finish(View v) {
        finish();
    }


    public void back(View v) {
        finish();
    }
}
