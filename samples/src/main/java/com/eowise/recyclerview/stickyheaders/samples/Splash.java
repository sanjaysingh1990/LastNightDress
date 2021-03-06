package com.eowise.recyclerview.stickyheaders.samples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.samples.UserProfile.LndLoginSignup;
import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

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




}
