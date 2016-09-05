package com.eowise.recyclerview.stickyheaders.samples.LndAgent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.samples.LndShare.LndShareToContacts;
import com.eowise.recyclerview.stickyheaders.samples.R;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

public abstract class LndShareActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
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

    public void whatsappShare() {
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


    public void sendsms() {
        Intent contact = new Intent(LndShareActivity.this, LndShareToContacts.class);
        startActivity(contact);
    }

    public void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setData(Uri.parse("mailto:"));
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject));
        i.putExtra(Intent.EXTRA_TEXT, "");
        try {
            startActivity(Intent.createChooser(i, getString(R.string.mail_to)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No mail client available", Toast.LENGTH_SHORT).show();
        }
    }

    private CallbackManager callbackManager;
    ShareDialog shareDialog;

    public void fbSharing() {
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
                    Toast.makeText(LndShareActivity.this, "Share Cancelled", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(LndShareActivity.this, "Not Shared", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public void setUpViewsForTweetComposer()
    {
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text("Hey friends check this out cool app. and use my referal code 0233 while login. https://play.google.com/store/apps/details?id=com.init.sikhdiary&hl=en ");
        builder.show();
    }
}