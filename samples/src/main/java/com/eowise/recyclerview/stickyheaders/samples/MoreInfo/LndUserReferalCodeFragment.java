package com.eowise.recyclerview.stickyheaders.samples.MoreInfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.UserProfile.LndUserDescriptionFragment;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sanju on 26/1/16.
 */
public class LndUserReferalCodeFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.finish)
    TextView finish;
    @Bind(R.id.ref_code)
    EditText ref_code;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lnd_user_reference_code, container, false);
        ButterKnife.bind(this, view);

        //font setup

        finish.setTypeface(SingleTon.mainfont);
        finish.setTypeface(SingleTon.subheading);

        //bind with listener
        finish.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {

        try {
            if (ref_code.getText().length() == 0) {
                FillUserInfo.jobj.put("refcode", 0);
                registeruser();
            } else {
                FillUserInfo.jobj.put("refcode", ref_code.getText());
                checkingrefcode(ref_code.getText().toString());

            }
        } catch (JSONException ex) {
            Log.e("error", ex.getMessage());
        }


    }

    private void registeruser()
    {
        registeruser(FillUserInfo.jobj.toString());
    }

    public void registeruser(final String data) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("wait creating profile...");
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/lndusers.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                Log.e("afterlogin", response.toString());
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status")) {
                        SharedPreferences.Editor edit = SingleTon.pref.edit();
                        edit.putString("uname", jobj.getString("uname"));
                        edit.putString("upass", jobj.getString("logintype"));
                        edit.putString("utype", jobj.getString("type"));
                        edit.putString("user_id", jobj.getString("user_id"));
                        edit.putString("country", jobj.getString("country"));
                        edit.putInt("user_position", jobj.getInt("user_position"));
                        edit.putString("ref_code", jobj.getString("ref_code"));
                        edit.putString("imageurl", jobj.getString("imageurl"));
                        edit.commit();

                        Intent i = new Intent(getActivity(), Main_TabHost.class);
                        startActivity(i);
                        getActivity().finish();
                    } else {
                        Log.e("error", "error");
                    }
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
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
                params.put("rqid", "2");
                params.put("data", data);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);

        queue.add(sr);
    }

    public void checkingrefcode(final String refcode) {


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_NOTIFICATION_SETTING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("json", response);
                try {
                    JSONObject jobj = new JSONObject(response.toString());

                    if (jobj.getBoolean("status")) {
                        registeruser();
                    } else {
                        ref_code.requestFocus();
                        ref_code.setError(jobj.getString("message"));

                    }

                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("response", error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "2");
                params.put("refcode", refcode);

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
