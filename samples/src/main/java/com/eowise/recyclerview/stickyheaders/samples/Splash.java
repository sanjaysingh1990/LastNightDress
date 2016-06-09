package com.eowise.recyclerview.stickyheaders.samples;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;


import com.crashlytics.android.Crashlytics;
import com.eowise.recyclerview.stickyheaders.samples.UserProfile.LndLoginSignup;
import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_splash);

        //to generate fab
       /* try {
          String  PACKAGE_NAME = getApplicationContext().getPackageName();
            PackageInfo info = getPackageManager().getPackageInfo(
                    PACKAGE_NAME,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("hashkey", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.e("package",PACKAGE_NAME);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("error1",e.getMessage());

        } catch (NoSuchAlgorithmException e) {
            Log.e("error",e.getMessage());
        }*/
createJson();
       new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String uname= SingleTon.pref.getString("uname",null);
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


private void createJson()
{
    JSONObject shippingrequest=new JSONObject();
try {
    shippingrequest.put("toCompany", "John Doe");
    shippingrequest.put("toName", "John Doe");
    shippingrequest.put("toPhone", "1231231234");
    shippingrequest.put("toAddr1", "111 W Legion");
    shippingrequest.put("toCity", "Whitehall");
    shippingrequest.put("toState", "MT");
    shippingrequest.put("toCode", "59759");

    shippingrequest.put("length", "15");
    shippingrequest.put("width", "25");
    shippingrequest.put("height", "5");
    shippingrequest.put("weight", "55");
    Log.e("json",shippingrequest.toString());
}
catch(JSONException ex)
{
    Log.e("error",ex.getMessage());
}
}

}
