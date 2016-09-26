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


           // Log.e("data", FillUserInfo.jobj.toString());
            FillUserInfo.mViewPager.setCurrentItem(3);
        } catch (JSONException ex) {
            Log.e("error", ex.getMessage() + "");
        }

    }


}