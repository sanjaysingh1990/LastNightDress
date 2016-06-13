package com.eowise.recyclerview.stickyheaders.samples.PurchaseAndSwap;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.Lnd_Item_Order;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.Utils.RelativeTimeTextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegularCheckoutFinishActivity extends AppCompatActivity {

    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.whatnext)
    TextView whatnext;
    @Bind({R.id.brandname, R.id.sellername, R.id.sellertext, R.id.pricetext, R.id.shippingtext, R.id.grandtotaltext, R.id.orderdatetext, R.id.ordernumbertext})
    List<TextView> regularcheckout;

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
    @Bind(R.id.shippingaddress)
    TextView shippingaddress;
    @Bind(R.id.paymentmethod)
    TextView payment;
    @Bind(R.id.greeting)
    TextView greet;

    //end here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lnd_regular_purchase_complete);
        ButterKnife.bind(this);
        //setup font
        heading.setTypeface(SingleTon.robotobold);
        Spannable word = new SpannableString(getResources().getString(R.string.regular_checkout_instruction));
        word.setSpan(new ForegroundColorSpan(Color.BLACK), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        whatnext.setText(word);
        Spannable wordTwo = new SpannableString(" clicking here.");
        wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#30beff")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //text.append(wordTwo);
        wordTwo.setSpan(new MyClickableSpan(wordTwo.toString()), 0,
                wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        whatnext.append(wordTwo);
        whatnext.setMovementMethod(LinkMovementMethod.getInstance());
        whatnext.setHighlightColor(Color.TRANSPARENT);

        //for greeting
        String s= getResources().getString(R.string.regular_checkout_successful);
        SpannableString ss1=  new SpannableString(s);
        ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#be4d66")), 0,9, 0);// set color
        greet.setText(ss1);

        //applying custom fonts
        regularcheckout.get(0).setTypeface(SingleTon.robotobold);
        regularcheckout.get(1).setTypeface(SingleTon.robotoregular);
        for (int i = 2; i < regularcheckout.size(); i++) {
            regularcheckout.get(i).setTypeface(SingleTon.robotomedium);

        }

        //get data from previous activity
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            Lnd_Item_Order orderdata = (Lnd_Item_Order) extra.get("data");
            brandname.setText(Capitalize.capitalizeFirstLetter(orderdata.getBrand()));
            showtime.setReferenceTime(orderdata.getTime());
            sellername.setText(Capitalize.capitalize(orderdata.getSellername()));
            price.setText("$"+orderdata.getPrice());
            shippingprice.setText("$"+orderdata.getShipping());
            orderdate.setText(orderdata.getOrderdate());
            ordernumber.setText(orderdata.getOrderid());
            shippingaddress.setText(orderdata.getAddress());
            payment.setText(orderdata.getPaymentmethod());
            SingleTon.imageLoader.displayImage(orderdata.getImageurl(), productimage, SingleTon.options4);


        }
        //applying custom font
        brandname.setTypeface(SingleTon.robotobold);
        sellername.setTypeface(SingleTon.robotobold);
        showtime.setTypeface(SingleTon.robotobold);
        price.setTypeface(SingleTon.robotobold);
        shippingprice.setTypeface(SingleTon.robotobold);
        grandtotalprice.setTypeface(SingleTon.robotobold);
        orderdate.setTypeface(SingleTon.robotobold);
        ordernumber.setTypeface(SingleTon.robotobold);
    }

    public void finish(View v) {
        finish();
    }

    class MyClickableSpan extends ClickableSpan {

        public MyClickableSpan(String string) {
            super();
        }

        public void onClick(View tv) {
            String url = "https://www.paypal.com/us/webapps/mpp/security/buyer-protection-resolution";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }

        public void updateDrawState(TextPaint ds) {

            ds.setColor(Color.parseColor("#30beff"));

            ds.setUnderlineText(false); // set to false to remove underline
        }
    }


}
