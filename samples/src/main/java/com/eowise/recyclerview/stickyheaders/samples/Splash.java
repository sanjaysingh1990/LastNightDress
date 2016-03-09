package com.eowise.recyclerview.stickyheaders.samples;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.util.Base64;
import android.util.Log;


import com.eowise.recyclerview.stickyheaders.samples.UserProfile.LndLoginSignup;
import com.eowise.recyclerview.stickyheaders.samples.UserProfile.LoginSignup;
import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.pm.Signature;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_splash);
        //to generate fab
       /* try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.eowise.recyclerview.stickyheaders.samples",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("hashkey", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/

       new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String uname=ImageLoaderImage.pref.getString("uname",null);
               if(uname!=null)
               {
                   Intent lndshop = new Intent(Splash.this, Main_TabHost.class);
                   startActivity(lndshop);
                  finish();

               }
                else {
                   Intent mainact = new Intent(Splash.this, LndLoginSignup.class);
                   startActivity(mainact);
                   finish();
               }
               }
        }).start();
    }

    }
