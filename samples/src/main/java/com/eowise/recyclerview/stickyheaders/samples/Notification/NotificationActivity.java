package com.eowise.recyclerview.stickyheaders.samples.Notification;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NotificationActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.heading)
    TextView heading;
    @Bind({R.id.followtext, R.id.likestext, R.id.commenttext, R.id.sharetext, R.id.swaptext, R.id.saletext, R.id.messagetext})
    List<TextView> notification;
    @Bind({R.id.followsubtext, R.id.likessubtext, R.id.commentsubtext, R.id.sharesubtext, R.id.swapsubtext, R.id.salesubtext, R.id.messagesubtext})
    List<TextView> subtext;
    @Bind({R.id.followstatus, R.id.likesstatus, R.id.commentstatus, R.id.sharestatus, R.id.swapstatus, R.id.salestatus, R.id.messagestatus})
    List<Switch> switches;
    private boolean calledonce=true;
    private String query="";
    private String[] colums={"follow_noti","likes_noti","comment_noti","share_noti","swap_noti","sale_noti","message_noti"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        //initialize
        ButterKnife.bind(this);
        heading.setTypeface(SingleTon.robotobold);
        //custom font

        for (int i = 0; i < notification.size(); i++) {
            notification.get(i).setTypeface(SingleTon.robotomedium);
            subtext.get(i).setTypeface(SingleTon.robotoregular);
            switches.get(i).setOnCheckedChangeListener(this);
        }

        //to check locally
        if (SingleTon.pref.getBoolean("notisettings", false)) {
            loadLocal();
        } else {
            getSettings();
        }


    }

    private void loadLocal() {
        ArrayList<Integer> settings = SingleTon.db.getSettings(SingleTon.pref.getString("user_id", ""));
        if (settings != null) {
            for (int i = 0; i < switches.size(); i++) {
                if (settings.get(i) == 1)
                    switches.get(i).setChecked(true);
                else
                    switches.get(i).setChecked(false);

            }
        } else
            Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if(query.length()>0)
        savesettings(query);
        finish();
    }

    public void back(View v) {
        onBackPressed();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if(calledonce)
        {
            calledonce=false;
            return;
        }
        settingsinfo.clear();
        query="set ";
       for(int i=0;i<switches.size()-1;i++)
       {
           if(switches.get(i).isChecked())
           {
             query=query+colums[i]+"=1,";
             settingsinfo.add(1);
           }
           else
           {
               query=query+colums[i]+"=0,";
               settingsinfo.add(0);
           }
       }
        if(switches.get(6).isChecked())
        {
            query=query+colums[6]+"=1"+" where user_id="+SingleTon.pref.getString("user_id","");
            settingsinfo.add(1);
        }
        else
        {
            query=query+colums[6]+"=0"+" where user_id="+SingleTon.pref.getString("user_id","");
            settingsinfo.add(0);
        }
       int val= SingleTon.db.updateSettings(settingsinfo);
       // Log.e("query",query+settingsinfo.size()+"resule"+val);
    }

    final ArrayList<Integer> settingsinfo = new ArrayList<>();

    public void getSettings() {

        settingsinfo.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_NOTIFICATION_SETTING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.e("json", response.toString());

                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status")) {
                        JSONObject settings = jobj.getJSONObject("settings");
                        Notification_Settings noti = new Notification_Settings();
                        noti.setFollow(settings.getInt("follow_enable"));
                        noti.setLikes(settings.getInt("likes_enable"));
                        noti.setComment(settings.getInt("comment_enable"));
                        noti.setShare(settings.getInt("share_enable"));
                        noti.setSwap(settings.getInt("swap_enable"));
                        noti.setSale(settings.getInt("sale_enable"));
                        noti.setMessage(settings.getInt("message_enable"));
                        //adding values to array list
                        settingsinfo.add(settings.getInt("follow_enable"));
                        settingsinfo.add(settings.getInt("likes_enable"));
                        settingsinfo.add(settings.getInt("comment_enable"));
                        settingsinfo.add(settings.getInt("share_enable"));
                        settingsinfo.add(settings.getInt("swap_enable"));
                        settingsinfo.add(settings.getInt("sale_enable"));
                        settingsinfo.add(settings.getInt("message_enable"));
                        //update setting ui
                         updateSettingsUI();
                        SingleTon.db.addValues(noti, SingleTon.pref.getString("user_id", ""));
                        //change shared preference value
                        SharedPreferences.Editor edit = SingleTon.pref.edit();
                        edit.putBoolean("notisettings", true);
                        edit.commit();

                    }
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)

            {
                // Toast.makeText(SettingsActivity.this, "can't change swap status", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "4");
                params.put("user_id", SingleTon.pref.getString("user_id", 0 + ""));
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

    private void updateSettingsUI() {
        for (int i = 0; i < switches.size(); i++) {
            if (settingsinfo.get(i) == 1)
                switches.get(i).setChecked(true);
            else
                switches.get(i).setChecked(false);

        }

    }

    public void savesettings(final String query) {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_NOTIFICATION_SETTING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                 Log.e("json", response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)

            {
               Log.e("error",error.getMessage()+"");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "3");
                params.put("query",query);


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
