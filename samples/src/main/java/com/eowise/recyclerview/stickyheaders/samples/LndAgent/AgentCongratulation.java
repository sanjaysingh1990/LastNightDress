package com.eowise.recyclerview.stickyheaders.samples.LndAgent;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AgentCongratulation extends LndShareActivity {
    private CallbackManager callbackManager;

    @Bind(R.id.refcode)
    TextView refcode;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.activity_agent_congratulation);
        ButterKnife.bind(this);
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email,publish_actions");
        //show refcode
        refcode.setText(SingleTon.pref.getString("ref_code",""));
    }
    public void profile(View v)
    {
        //to enable check
        SharedPreferences.Editor edit = SingleTon.pref.edit();
        edit.putBoolean("agent_welcome", true);
        edit.commit();
        Intent agentproile = new Intent(this, Lnd_Agent_Profile.class);
        startActivity(agentproile);
        finish();
    }

    private void shareDialog() {
        TextView fb, twitter, whatsapp, email;

        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // dialog.getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c6290")));
        dialog.setContentView(R.layout.lnd_agent_share_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#30ffffff")));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
        //gettting dialog reference
        fb = (TextView) dialog.findViewById(R.id.facebook);
        twitter = (TextView) dialog.findViewById(R.id.twitter);
        whatsapp = (TextView) dialog.findViewById(R.id.whatsapp);
        email = (TextView) dialog.findViewById(R.id.email);
        //custom font
        //applying custom fonts
        fb.setTypeface(SingleTon.robotomedium);
        twitter.setTypeface(SingleTon.robotomedium);
        whatsapp.setTypeface(SingleTon.robotomedium);
        email.setTypeface(SingleTon.robotomedium);


        //setting events
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                dialog.dismiss();
                fbSharing();
            }
        });


        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                dialog.dismiss();
                setUpViewsForTweetComposer();

            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                dialog.dismiss();
                whatsappShare();

            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                dialog.dismiss();
            }
        });
    }

    public void share(View v)
    {
        shareDialog();
    }
}
