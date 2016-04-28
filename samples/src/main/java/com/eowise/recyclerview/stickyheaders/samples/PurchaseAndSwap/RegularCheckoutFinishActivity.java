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
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegularCheckoutFinishActivity extends AppCompatActivity {

    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.whatnext)
    TextView whatnext;

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
