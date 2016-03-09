package com.eowise.recyclerview.stickyheaders.samples.MoreInfo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.UserProfile.LndRegistrationActivity;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserNameFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.next)TextView next;
    @Bind(R.id.uname)EditText uname;
    @Bind(R.id.loader)AVLoadingIndicatorView loader;
    @Bind(R.id.notipropic)ImageView profilepic;
    public static String imageurl="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.username_fragment_activity, container,
                false);
        //initializing butterknife
        ButterKnife.bind(this, rootView);
        next.setOnClickListener(this);
        try {
            imageurl = "http://graph.facebook.com/" + FillUserInfo.jobj.getString("fbid") + "/picture?type=large";
        }
        catch(Exception ex)
        {

        }

        //setting facebook profile  image
        ImageLoaderImage.imageLoader.displayImage(imageurl, profilepic, ImageLoaderImage.options2, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                try {

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                imageurl = Base64.encodeToString(byte_arr, 0);
                   FillUserInfo.jobj.put("imageurl", imageurl);
               }
               catch(Exception ex)
               {
                   Log.e("error",ex.getMessage()+"");
               }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });





        return rootView;


    }

    @Override
    public void onClick(View view) {
        if(uname.getText().length()==0) {
            uname.setError("username field can't be empty");
            return;
        }
            loader.setVisibility(View.VISIBLE);
            check(uname.getText().toString());

    }

    public  void check(final String username){


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST,"http://52.76.68.122/lnd/androidiosphpfiles/lndusers.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                loader.setVisibility(View.GONE);
                // Log.e("response private", response.toString());
                try {
                    JSONObject jobj=new JSONObject(response.toString());
                    boolean status=jobj.getBoolean("status");
                    if(status)
                    {
                      uname.setError("user name already taken");

                     }
                    else
                    {
                        try {
                            FillUserInfo.jobj.put("uname",uname.getText().toString());
                        }
                        catch(JSONException ex)
                        {
                            Log.e("error",ex.getMessage()+"");
                        }

                        FillUserInfo.mViewPager.setCurrentItem(1);

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
                loader.setVisibility(View.GONE);
                Log.e("response",error.getMessage()+"");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("rqid","7");
                params.put("uname",username);

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


}
