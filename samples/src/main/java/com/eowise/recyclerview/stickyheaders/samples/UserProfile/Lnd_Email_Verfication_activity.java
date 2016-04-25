package com.eowise.recyclerview.stickyheaders.samples.UserProfile;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.samples.MainActivity;
import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Lnd_Email_Verfication_activity extends AppCompatActivity {

    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.username)
    TextView username;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.verifybutton)
    TextView verifybutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lnd_email_verification_layout);
        ButterKnife.bind(this);

        //apply font
        heading.setTypeface(SingleTon.appfont);
        username.setTypeface(SingleTon.robotobold);
        verifybutton.setTypeface(SingleTon.robotomedium);
        text.setTypeface(SingleTon.robotoregular);

        //
        Spannable word = new SpannableString("Welcome to Last Night’s Dress. the online social marketplace to buy and sell high end fashions and accessories. We are glad you’re here, should you have any questions email us at ");

        word.setSpan(new ForegroundColorSpan(Color.BLACK), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        text.setText(word);
        Spannable wordTwo = new SpannableString("support@lastnightsdress.com.");

        wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#30beff")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //text.append(wordTwo);

        wordTwo.setSpan(new MyClickableSpan("support@lastnightsdress.com"),0,
                wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        text.append(wordTwo);
        text.setMovementMethod(LinkMovementMethod.getInstance());
        text.setHighlightColor(Color.TRANSPARENT);

        //reading data from previous activity
        Bundle extra=getIntent().getExtras();
        if(extra!=null)

        {
            String uname=extra.getString("uname","");
            username.setText("Hello, "+uname);
        }
    }

    public void verify(View v) {
        Intent home = new Intent(this, Main_TabHost.class);
        startActivity(home);
        finish();
    }

    class MyClickableSpan extends ClickableSpan {

        public MyClickableSpan(String string) {
            super();
        }

        public void onClick(View tv) {
          sendMail();
        }

        public void updateDrawState(TextPaint ds) {

            ds.setColor(Color.parseColor("#30beff"));

            ds.setUnderlineText(false); // set to false to remove underline
        }
    }
    private void sendMail()
    {
        Intent mailIntent = new Intent(Intent.ACTION_SEND);

        String mailTo[] = new String[] { "support@lastnightsdress.com." };
        mailIntent.putExtra(Intent.EXTRA_EMAIL, mailTo);
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        mailIntent.putExtra(Intent.EXTRA_TEXT, "your feedback here.");
        mailIntent.setType("plain/text");
        try {
            startActivity(Intent.createChooser(mailIntent, "only email client"));
        }
        catch(ActivityNotFoundException ex)
        {
            Toast.makeText(this,"No email application found",Toast.LENGTH_SHORT).show();
        }
        }
}
