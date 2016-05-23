package com.eowise.recyclerview.stickyheaders.samples.UserProfile;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.facebook.login.LoginManager;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LndLoginFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.signup)
    TextView signup;
    @Bind(R.id.uname)
    EditText uname;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.login_button)
    TextView loginbutton;
    @Bind(R.id.fbsignup)
    TextView fbsignup;
    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.resetpassword)
    TextView resetpass;
    @Bind(R.id.loader)
    AVLoadingIndicatorView loader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lndloginactivity, container, false);
        LndLoginSignup.currentpage = 4;
        ButterKnife.bind(this, view);

        //listener
        signup.setOnClickListener(this);
        loginbutton.setOnClickListener(this);
        fbsignup.setOnClickListener(this);
        resetpass.setOnClickListener(this);
        //applying fonts
        heading.setTypeface(SingleTon.appfont);
        loginbutton.setTypeface(SingleTon.subheading);
        resetpass.setTypeface(SingleTon.subheading);
        fbsignup.setTypeface(SingleTon.subheading);
        signup.setTypeface(SingleTon.mainfont);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup:
                //current page value on stack;

                LndLoginSignup.mViewPager.setCurrentItem(0);
                break;
            case R.id.login_button:
                loginbutton.setText("");
                loader.setVisibility(View.VISIBLE);
                login();


                break;
            case R.id.fbsignup:
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "user_friends", "email", "user_location"));
                break;
            case R.id.resetpassword:
                //current page value on stack;
                LndLoginSignup.currenttab.push(1);
                LndLoginSignup.mViewPager.setCurrentItem(5);

                break;
        }
    }


    public void login() {
        if (uname.getText().length() == 0) {
            uname.requestFocus();
            uname.setError("username is empty");
            return;
        } else if (password.getText().length() == 0) {
            password.requestFocus();
            password.setError("password is empty");
            return;
        }
        getLogin(uname.getText().toString(), password.getText().toString());
    }

    public void getLogin(final String uname, final String pass) {


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/lndusers.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //    Log.e("response private", response.toString());
                try {
                    loginbutton.setText("Log in");
                    loader.setVisibility(View.GONE);

                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status")) {
                        SharedPreferences.Editor edit = SingleTon.pref.edit();
                        edit.putString("uname", jobj.getString("uname"));
                        edit.putString("user_id", jobj.getString("user_id"));
                        edit.putString("utype", jobj.getString("utype"));
                        edit.putString("country", jobj.getString("country"));
                        edit.putString("imageurl", jobj.getString("imageurl"));
                        edit.commit();

                        Intent i = new Intent(getActivity(), Main_TabHost.class);
                        startActivity(i);

                        //finish all previous activities
                        ActivityCompat.finishAffinity(getActivity());
                    } else {
                        showAlert();
                    }
                } catch (Exception ex) {
                    loginbutton.setText("Log in");
                    loader.setVisibility(View.GONE);

                    Log.e("Exception", ex.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.setVisibility(View.GONE);
                loginbutton.setText("Log in");
                //   Log.e("response",error.getMessage()+"");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "8");
                params.put("uname", uname);
                params.put("pass", pass);

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

    private void showAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.alertdailog_showalert_layout, null);

        dialog.setView(view);
        final AlertDialog alert = dialog.create();
        alert.show();
        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
    }


}
