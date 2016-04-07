package com.eowise.recyclerview.stickyheaders.samples.MoreInfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class UserTypeFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.close)
    TextView close;
    @Bind(R.id.shopuser)
    RadioButton shopuser;
    @Bind(R.id.privateuser)
    RadioButton privateuser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.usertype_fragment_activity, container,
                false);
        //initializing butterknife
        ButterKnife.bind(this, rootView);
        close.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {

        try {
            if (shopuser.isChecked())
                FillUserInfo.jobj.put("utype", "shop");
            else
                FillUserInfo.jobj.put("utype", "private");


            Log.e("data", FillUserInfo.jobj.toString());
            registeruser(FillUserInfo.jobj.toString());
        } catch (JSONException ex) {
            Log.e("error", ex.getMessage() + "");
        }

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
}