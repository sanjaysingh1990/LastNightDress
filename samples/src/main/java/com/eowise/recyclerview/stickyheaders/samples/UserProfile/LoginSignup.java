package com.eowise.recyclerview.stickyheaders.samples.UserProfile;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;

import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.MoreInfo.FillUserInfo;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginSignup extends Fragment implements View.OnClickListener {
    Dialog dialog;
    ImageView logo;
    private TextView loginButton;
    public static CallbackManager callbackManager;
    private int MY_PERMISSIONS_REQUEST = 100;
    private Intent intent;
    @Bind(R.id.login)
    TextView login;
    @Bind(R.id.signup)
    TextView signup;
    @Bind(R.id.fbtext)
    TextView fbtext;
    @Bind(R.id.fbsignup)
    RelativeLayout fbsignup;
    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.subheading)
    TextView subheading;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        LndLoginSignup.currentpage = 1;


        View view = inflater.inflate(R.layout.lndsignupactivity, container, false);
        ButterKnife.bind(this, view);

        //custom fonts
         heading.setTypeface(ImageLoaderImage.appfont);
         subheading.setTypeface(ImageLoaderImage.mainfont);
        fbtext.setTypeface(ImageLoaderImage.robotomedium);
        signup.setTypeface(ImageLoaderImage.subheading);
        login.setTypeface(ImageLoaderImage.mainfont);

        //login lnd user
        login.setOnClickListener(this);
        //login registration
        signup.setOnClickListener(this);
        fbtext.setOnClickListener(this);
        // facebook signup
        fbsignup.setOnClickListener(this);
        checkPermission();
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code

                                        if (LndLoginSignup.currentpage == 2) {
                                            PrivateFragment.update(object);
                                            return;
                                        }
                                        else if(LndLoginSignup.currentpage==5)
                                        {
                                            ShopFragment.update(object);
                                            return;
                                        }

                                        //Log.e("res", response.toString());
                                        String id = "", fname = "", lname = "", email = "", gender = "", country = "canada";
                                        try {

                                            id = object.getString("id");
                                            fname = object.getString("first_name");
                                            lname = object.getString("last_name");

                                            email = object.getString("email");
                                            gender = object.getString("gender");

                                            //creating json object
                                            JSONObject jobj = new JSONObject();
                                            jobj.put("fbid", id);
                                            jobj.put("fullname", fname+" "+lname);
                                            jobj.put("lname", lname);
                                            jobj.put("country",country);

                                            jobj.put("email", email);
                                            jobj.put("gender", gender);
                                            Log.e("data", jobj.toString());
                                            intent = new Intent(getActivity(), FillUserInfo.class);
                                            intent.putExtra("data", jobj.toString());
                                            checkFbuser(id);

                                        } catch (Exception ex) {
                                            //    Log.d("Error",ex.getMessage());
                                        }


                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,first_name,last_name,email,gender, birthday,locale,location{location}");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getActivity(), "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                //current page value on stack;
                LndLoginSignup.currenttab.push(0);
                LndLoginSignup.mViewPager.setCurrentItem(1);
                break;

            case R.id.signup:
                //current page value on stack;
                LndLoginSignup.currenttab.push(0);
                LndLoginSignup.mViewPager.setCurrentItem(2);
                break;
            case R.id.fbsignup:
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "user_friends", "email", "user_location"));

                break;
            case R.id.fbtext:
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "user_friends", "email", "user_location"));

                break;

        }
    }
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login_signup);
//initializing facebook
        loginButton = (TextView)findViewById(R.id.login_button);
        TextView heading= (TextView) findViewById(R.id.heading);
        TextView about= (TextView) findViewById(R.id.about);
        Typeface tf=Typeface.createFromAsset(getAssets(),"Mural_Script.ttf");
        Typeface tf2=Typeface.createFromAsset(getAssets(),"AvenirNextLTPro-Demi.otf");
        heading.setTypeface(tf);
        about.setTypeface(tf2);

        logo= (ImageView) findViewById(R.id.logo);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int fullwidth=displayMetrics.widthPixels;
        int height = (displayMetrics.heightPixels*9)/100;
        int width=(int)(displayMetrics.widthPixels*10)/100;
        int aboutsize=(int)(displayMetrics.widthPixels*3)/100;
        Log.e("width", displayMetrics.widthPixels + "," + width + "," + aboutsize);


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(height,height);
        logo.setLayoutParams(layoutParams);
        if(fullwidth<=480)
        {
            heading.setTextSize(TypedValue.COMPLEX_UNIT_SP, width);
            about.setTextSize(TypedValue.COMPLEX_UNIT_SP, aboutsize);
        }
        else if(fullwidth>480 && fullwidth<=720)
        {
            heading.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int)(fullwidth*7)/100);
            about.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int)(fullwidth*2)/100);
        }
        else if(fullwidth>720 && fullwidth<=1280)
        {
           // Toast.makeText(this, "width" + fullwidth, Toast.LENGTH_SHORT).show();
            heading.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int)(fullwidth*5)/100);
            about.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int)(fullwidth*1.5)/100);

        }
        else if(fullwidth>1280 && fullwidth<=1290)
        {
             heading.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int)(fullwidth*7)/100);
            about.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int)(fullwidth*1.5)/100);

        }

        else
        {


            heading.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int)(fullwidth*7)/100);
            about.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int)(fullwidth*1.5)/100);

        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginSignup.this, Arrays.asList("public_profile", "user_friends","email","user_location"));


            }
        });




    }*/


    /*
public void login(View v)
{

  //  Intent intent=new Intent(this, LoginActivity2.class);
    startActivity(intent);
    return;
    /*dialog = new Dialog(this,R.style.DialogSlideAnim);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


    // dialog.getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c6290")));
    dialog.setContentView(R.layout.logindialog);

     dialog.getWindow().

     setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")

     ));
     dialog.getWindow().

     setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

     dialog.show();
//textview reference
final ImageView logo= (ImageView) dialog.findViewById(R.id.logo);
   final TextView uname= (TextView) dialog.findViewById(R.id.uname);
    final TextView pass= (TextView) dialog.findViewById(R.id.pass);
     TextView heading= (TextView) dialog.findViewById(R.id.heading);
    heading.setTypeface(ImageLoaderImage.hfont);

//dialog listener
    dialog.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // handle me
            dialog.dismiss();
        }
    });

    dialog.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // handle me

            if(uname.getText().length()==0) {
                uname.setError("username is empty");
                return;
            }
                else if(pass.getText().length()==0) {
                uname.setError("password is empty");
                return;
            }
            getLogin(uname.getText().toString(), pass.getText().toString());
        }
    });
 }

public void signup(View v)
{
    Intent i=new Intent(LoginSignup.this, LndRegistrationActivity.class);
    startActivity(i);

}

    public  void getLogin(final String uname,final String pass){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://52.76.68.122/lnd/lndusers.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pDialog.dismiss();

                }
                catch(Exception ex)
                {

                }
                Log.e("response private", response.toString());
               try
               {
                   JSONObject jobj=new JSONObject(response.toString());
                   if(jobj.getBoolean("status"))
                   {
                     SharedPreferences.Editor edit= ImageLoaderImage.pref.edit();
                     edit.putString("uname", jobj.getString("uname"));
                     edit.putString("upass",jobj.getString("pass"));
                       edit.putString("utype",jobj.getString("utype"));

                       edit.putString("imageurl",jobj.getString("imageurl"));
                       edit.commit();
                       dialog.dismiss();
                       Intent i = new Intent(LoginSignup.this, Main_TabHost.class);
                       startActivity(i);
                       finish();
                   }
                   else
                   {
                       showAlert();
                   }
               }
               catch(Exception ex)
               {
                   Log.e("Exception", ex.getMessage()+"");
               }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Log.e("response",error.getMessage()+"");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("rqid","8");
                params.put("uname",uname);
                params.put("pass", pass);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }
private void showAlert()
{
    AlertDialog.Builder dialog=new AlertDialog.Builder(this);
    View view = LayoutInflater.from(this).inflate(R.layout.alertdailog_showalert_layout, null);

    dialog.setView(view);
    final    AlertDialog alert=dialog.create();
    alert.show();
    view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            alert.dismiss();
        }
    });
}*/
    public void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void checkFbuser(final String fbid) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("wait connecting facebook...");
        pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/lndusers.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //      Log.e("check", response.toString());
                try {
                    pDialog.dismiss();
                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status")) {
                        SharedPreferences.Editor edit = ImageLoaderImage.pref.edit();
                        edit.putString("uname", jobj.getString("uname"));
                        edit.putString("utype", jobj.getString("type"));
                        edit.putString("country", jobj.getString("country"));
                        edit.putString("user_id", jobj.getString("user_id"));

                        edit.putString("imageurl", jobj.getString("imageurl"));
                        edit.commit();

                        Intent home = new Intent(getActivity(), Main_TabHost.class);
                        startActivity(home);
                        ActivityCompat.finishAffinity(getActivity());
                    } else {
                        startActivity(intent);
                        //  finishAffinity();
                        ActivityCompat.finishAffinity(getActivity());
                    }
                } catch (Exception ex) {
                    Log.e("Exception", ex.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Log.e("response", error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "1");
                params.put("fbid", fbid);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

}
