package com.eowise.recyclerview.stickyheaders.samples.UserProfile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sanju on 28/1/16.
 */
public class ResetPasswordEmailSentFragment extends Fragment

{
    @Bind(R.id.back)
    ImageButton back;
    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.sendemail)
    TextView sendemail;
    @Bind(R.id.emailsendtext)
    TextView emailsendtext;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.lnd_reset_password_layout2, container, false);

        //initializing butter knife
        ButterKnife.bind(this, view);
        // set custom font
        heading.setTypeface(ImageLoaderImage.robotobold);
        // on back click
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    LndLoginSignup.mViewPager.setCurrentItem(LndLoginSignup.currenttab.pop());
                } catch (Exception ex) {

                }
            }
        });
        emailSent();
//send email in trouble
        sendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String to = "support@lastnightdress.com";
                    String subject = "LND login problem";
                    String message = "you problem ?";

                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                    email.putExtra(Intent.EXTRA_SUBJECT,subject);
                    email.putExtra(Intent.EXTRA_TEXT,message);
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));

                }
                catch (Exception ex)
                {
                    Toast.makeText(getActivity(), "No Client " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
private void emailSent()
{

    String  text= "Email Sent    ";

    SpannableStringBuilder ssb = new SpannableStringBuilder(text);
    Bitmap smiley = BitmapFactory.decodeResource(getResources(), R.drawable.shippinglabel_icon);
    ssb.setSpan( new ImageSpan( smiley ), text.length()-3,text.length()-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE );
    emailsendtext.setText(ssb, TextView.BufferType.SPANNABLE);

}
}
