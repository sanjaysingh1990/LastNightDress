package com.eowise.recyclerview.stickyheaders.samples.LndAgent;

import android.app.Dialog;
import android.content.Intent;
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

public class AgentCongratulation extends AppCompatActivity {
    private CallbackManager callbackManager;

    @Bind(R.id.refcode)
    TextView refcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        Intent agentproile = new Intent(this, Lnd_Agent_Profile.class);
        startActivity(agentproile);
        finish();
    }
    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
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
                Main_TabHost.main.twitter();

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
    ShareDialog shareDialog;

    private void fbSharing() {
        shareDialog = new ShareDialog(this);  // intialize facebook shareDialog.

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Last Night Dress")
                    .setImageUrl(Uri.parse("http://52.76.68.122/lnd/images/lndlogo.png"))
                    .setContentDescription(
                            "Please use following code 0233")
                    .setContentUrl(Uri.parse("http://sikhdiary.com/lnd-landing/"))
                    .build();

            shareDialog.show(linkContent, ShareDialog.Mode.AUTOMATIC);  // Show facebook ShareDialog
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    // Toast.makeText(Agent_Signup.this, "Shared Successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(AgentCongratulation.this, "Share Cancelled", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(AgentCongratulation.this, "Not Shared", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void whatsappShare() {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "http://www.codeofaninja.com");
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
        }
    }
    public void share(View v)
    {
        shareDialog();
    }
}
