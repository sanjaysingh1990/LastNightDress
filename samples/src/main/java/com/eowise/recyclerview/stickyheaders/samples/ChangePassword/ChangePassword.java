package com.eowise.recyclerview.stickyheaders.samples.ChangePassword;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChangePassword extends AppCompatActivity {

  @Bind(R.id.heading) TextView heading;
    @Bind(R.id.oldpass) EditText oldpass;
    @Bind(R.id.newpass) EditText newpass;
    @Bind(R.id.newpassagain) EditText newpassagain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        //custom fonts
        Typeface tf=Typeface.createFromAsset(getAssets(),"AvenirNextLTPro-Bold.otf");
        //applying font
        heading.setTypeface(tf);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void back(View v)
    {
        onBackPressed();
    }

public void changePass(View v)
{
    String oldpassval=oldpass.getText().toString();
    String newpassval=newpass.getText().toString();
    String newpassagainval=newpassagain.getText().toString();
   if(oldpassval.length()==0)
   {
       oldpass.setError("can't be empty");

   }
    if(newpassval.length()==0)
    {
        newpass.setError("can't be empty");

    }
    if(newpassagainval.length()==0)
    {
        newpassagain.setError("can't be empty");
        return;
    }
if(newpassval.compareTo(newpassagainval)!=0)
{
    newpassagain.setError("password field not equal");
    return;
}
    JSONObject jobj=new JSONObject();
    try
    {
        jobj.put("uname", SingleTon.pref.getString("uname",""));
        jobj.put("oldpass",oldpassval);
        jobj.put("newpass",newpassval);
    }
    catch(Exception ex)
    {

    }
   changePassword(jobj.toString());
}

    public void changePassword(final String data) {


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("wait applying changes...");
        pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://52.76.68.122/lnd/lndusers.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                //Log.e("responsedata", response.toString());
                try {
                    JSONObject jobj=new JSONObject(response.toString());
                    if(jobj.getBoolean("status")) {
                        oldpass.setText("");
                        newpass.setText("");
                        newpassagain.setText("");

                        Toast.makeText(ChangePassword.this, jobj.getString("message") + "", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                        else {
                        oldpass.setError("your old password is not correct");
                        Toast.makeText(ChangePassword.this, jobj.getString("message") + "", Toast.LENGTH_SHORT).show();
                    }
                }
                    catch(Exception ex)
                    {

                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                //    Log.e("response error", error.getMessage() + "");
              try {
                  new com.eowise.recyclerview.stickyheaders.samples.AlertDialog().showAlertDialog(ChangePassword.this);
              }
              catch(Exception ex)
              {

              }
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("data",data);
                params.put("rqid","6");





                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return super.getBody();
            }
        };
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);

        queue.add(sr);
    }
}

