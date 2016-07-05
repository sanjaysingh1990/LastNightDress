package com.eowise.recyclerview.stickyheaders.samples.UserProfile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LndUserDescriptionFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.finish)
    TextView finish;
    @Bind(R.id.leftchar)
    TextView leftchar;
    @Bind(R.id.desc_text)
    EditText descText;
    public static JSONObject jsonprivate = new JSONObject();
    public static JSONObject jsonshop = new JSONObject();
    public static int profiletype = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lnd_user_description, container, false);
        LndLoginSignup.currentpage = 6;
        ButterKnife.bind(this, rootView);
        //custom font
        finish.setTypeface(SingleTon.subheading);
        //setting up click listener
        finish.setOnClickListener(this);
        descText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                leftchar.setText(150 - charSequence.length() + " Characters");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return rootView;
    }

    @Override
    public void onClick(View view) {

        try {
           if(profiletype==1) {
               jsonprivate.put("description", descText.getText().toString());
              // Log.e("privatedata", jsonprivate.toString());
               registerprivateUser(jsonprivate.toString());
           }
            else if(profiletype==2)
           {
               jsonshop.put("description", descText.getText().toString());
              registerprivateUser(jsonshop.toString());
               //Log.e("json", jsonshop.toString());

           }
           } catch (JSONException ex) {
            Log.e("error", ex.getMessage() + "");
        }

    }
    public  void registerprivateUser(final String data){
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("wait creating profile...");
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
               Log.e("json", response.toString());
                try
                {
                    JSONObject jobj=new JSONObject(response.toString());
                    if(jobj.getBoolean("status"))
                    {
                        SharedPreferences.Editor edit= SingleTon.pref.edit();
                        edit.putString("uname", jobj.getString("uname"));
                        edit.putString("user_id",jobj.getString("user_id"));
                        edit.putString("utype",jobj.getString("type"));
                        edit.putString("country",jobj.getString("country"));
                        edit.putString("imageurl", jobj.getString("imageurl"));
                        edit.putInt("user_position", jobj.getInt("user_position"));
                        edit.putString("ref_code",jobj.getString("ref_code"));
                        Log.e("code",jobj.getString("ref_code"));
                        edit.commit();
                        LndUserReferalCodeFragment.ref_code.setText(jobj.getString("ref_code"));
                        LndLoginSignup.currentpage = 6;
                        //current page value on stack;
                        LndLoginSignup.currenttab.push(5);
                        LndLoginSignup.mViewPager.setCurrentItem(6);

                    }
                    else
                    {
                       Toast.makeText(getActivity(),jobj.getString("message")+"",Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception ex)
                {
                    Log.e("json parsing error", ex.getMessage());
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
                params.put("rqid","5");
                params.put("data",data);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);

        queue.add(sr);
    }

}