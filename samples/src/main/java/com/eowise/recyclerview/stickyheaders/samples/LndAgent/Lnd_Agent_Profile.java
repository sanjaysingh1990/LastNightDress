package com.eowise.recyclerview.stickyheaders.samples.LndAgent;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
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
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.data.LndAgentBean;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Lnd_Agent_Profile extends LndShareActivity implements View.OnClickListener {
    @Bind(R.id.recycler)
    RecyclerView recyclerView;
    @Bind(R.id.header)
    TextView header;
    @Bind(R.id.info)
    ImageButton info;
    ArrayList<LndAgentBean> data = new ArrayList<>();
    AgentListAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();

        setContentView(R.layout.activity_lnd__agent__profile);
        ButterKnife.bind(this);
        adapter = new AgentListAdapter(this, data);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        header.setTypeface(SingleTon.robotobold);
        header.setText(Capitalize.capitalize(SingleTon.pref.getString("uname", "")));
        setHeader();
        getData();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email,publish_actions");
    }

    private void setHeader() {

        LndAgentBean header = new LndAgentBean();

        header.setType(0);
        header.setUsertotalagents("0");
        header.setUsertotalshops("0");
        data.add(header);

        adapter.notifyDataSetChanged();
    }


    private void addUser(JSONObject jsonObject) throws Exception {

        LndAgentBean agent = new LndAgentBean();
        if (jsonObject.getString("user_type").equals("private"))
            agent.setType(2);
        else
            agent.setType(3);

        agent.setUname(jsonObject.getString("uname"));
        agent.setTotalpost(jsonObject.getString("total_posts"));
        agent.setTotalsales(jsonObject.getString("total_sales"));
        agent.setTotalrefuser(jsonObject.getString("total_ref_user"));
        agent.setProfilepic(jsonObject.getString("profile_pic"));
        agent.setUserposition(jsonObject.getInt("user_position"));
        data.add(agent);
    }


    private void addHeader(int headertype, String totalcount) throws Exception {

        LndAgentBean regionaldirectorheader = new LndAgentBean();
        regionaldirectorheader.setType(1);
        regionaldirectorheader.setHeaderType(headertype);
        regionaldirectorheader.setTotal(totalcount);

        data.add(regionaldirectorheader);
    }

    private void addMoreUserHeader(int headertype) throws Exception {

        LndAgentBean regionaldirectorheader = new LndAgentBean();
        regionaldirectorheader.setType(5);
        regionaldirectorheader.setHeaderType(headertype);

        data.add(regionaldirectorheader);
    }

    private void sharecode(int headertype) throws Exception {

        LndAgentBean regionaldirectorheader = new LndAgentBean();
        regionaldirectorheader.setType(7);
        regionaldirectorheader.setHeaderType(headertype);

        data.add(regionaldirectorheader);
    }

    public void getData() {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_POST_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    boolean status = jobj.getBoolean("status");
                    if (status) {
                        info.setVisibility(View.VISIBLE);
                        //for user
                        LndAgentBean agentbean = data.get(0);
                        int totalagents = jobj.getInt("total_agent") + jobj.getInt("total_basicuser") + jobj.getInt("total_agency") + jobj.getInt("total_areamanager") + jobj.getInt("total_regionaldirector");
                        agentbean.setUsertotalagents(totalagents + "");
                        agentbean.setUsertotalshops(jobj.getInt("total_shops") + "");
                        //for regiional director
                        JSONArray regionaldirector = jobj.getJSONArray("regionaldirector");
                        if (regionaldirector.length() > 0) {
                            addHeader(5, jobj.getString("total_regionaldirector"));
                        }
                        for (int i = 0; i < regionaldirector.length(); i++) {
                            addUser(regionaldirector.getJSONObject(i));
                        }
                        if (regionaldirector.length() == 10)
                            addMoreUserHeader(5);
                        //for area manager
                        JSONArray areamanager = jobj.getJSONArray("areamanager");
                        if (areamanager.length() > 0) {
                            addHeader(4, jobj.getString("total_areamanager"));
                        }
                        for (int i = 0; i < areamanager.length(); i++) {
                            addUser(areamanager.getJSONObject(i));
                        }
                        if (areamanager.length() == 10)
                            addMoreUserHeader(5);
                        //for agency
                        JSONArray agency = jobj.getJSONArray("agency");
                        if (agency.length() > 0) {
                            addHeader(3, jobj.getString("total_agency"));
                        }
                        for (int i = 0; i < agency.length(); i++) {
                            addUser(agency.getJSONObject(i));
                        }
                        if (agency.length() == 10)
                            addMoreUserHeader(5);
                        //for agent
                        JSONArray agent = jobj.getJSONArray("agents");
                        if (agent.length() > 0) {
                            addHeader(2, jobj.getString("total_agent"));
                        }
                        for (int i = 0; i < agent.length(); i++) {
                            addUser(agent.getJSONObject(i));
                        }
                        //for agent
                        JSONArray basicuser = jobj.getJSONArray("basicuser");
                        if (agent.length() > 0) {
                            addHeader(1, jobj.getString("total_basicuser"));
                        }
                        for (int i = 0; i < basicuser.length(); i++) {
                            addUser(basicuser.getJSONObject(i));
                        }

                        if (basicuser.length() == 6)
                            addMoreUserHeader(5);

                        sharecode(7);

                    } else {
                        showinfo();
                        info.setVisibility(View.GONE);
                    }


                    adapter.notifyDataSetChanged();

                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage() + "");
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
                params.put("rqid", "20");
                params.put("ref_code", SingleTon.pref.getString("ref_code", "") + "");

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void back(View v) {
        onBackPressed();
    }

    TextView goal, benefits, heading1, heading2, steps;

    public void info(View v) {
        RelativeLayout emailshare, whatsappshare, fbshare, twittershare, messageshare;

        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.agent_feautre_showinfo_layout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
        ImageButton back = (ImageButton) dialog.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                }

        );
        TextView header = (TextView) dialog.findViewById(R.id.header);
        header.setTypeface(SingleTon.robotobold);
        header.setText(Capitalize.capitalize(SingleTon.pref.getString("uname", "")));
        Toolbar toolbar = (Toolbar) dialog.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);

        emailshare = (RelativeLayout) dialog.findViewById(R.id.shareonmail);
        whatsappshare = (RelativeLayout) dialog.findViewById(R.id.shareonwhatsapp);
        fbshare = (RelativeLayout) dialog.findViewById(R.id.shareonfb);
        twittershare = (RelativeLayout) dialog.findViewById(R.id.shareontwitter);
        messageshare = (RelativeLayout) dialog.findViewById(R.id.shareonsms);

        emailshare.setOnClickListener(this);
        whatsappshare.setOnClickListener(this);
        fbshare.setOnClickListener(this);
        twittershare.setOnClickListener(this);
        messageshare.setOnClickListener(this);
        goal = (TextView) dialog.findViewById(R.id.goal_text);
        benefits = (TextView) dialog.findViewById(R.id.benefit_text);
        heading1 = (TextView) dialog.findViewById(R.id.heading1);
        heading2 = (TextView) dialog.findViewById(R.id.heading2);
        steps = (TextView) dialog.findViewById(R.id.steps);
        showInfo();
        //for goal and beneifts
    }

    private void showInfo() {
        int user_position = 5;//SingleTon.pref.getInt("user_position",0);
        String goaltext = "", benefitstext = "";
        switch (user_position) {
            case 1:
                goaltext = getResources().getString(R.string.agent_goal);
                benefitstext = getResources().getString(R.string.agent_level_commission_text);
                heading1.setText(getResources().getString(R.string.agent_how_to_achieve));
                heading2.setText(getResources().getString(R.string.agent_sub_heading));
                steps.setText(getResources().getString(R.string.become_agent_condition));
                break;
            case 2:
                goaltext = getResources().getString(R.string.agency_goal);
                benefitstext = getResources().getString(R.string.agency_benifits);
                heading1.setText(getResources().getString(R.string.to_become_agency_heading1));
                heading2.setText(getResources().getString(R.string.to_become_agecny_heading2));
                steps.setText(getResources().getString(R.string.steps_to_become_agency));
                break;
            case 3:
                goaltext = getResources().getString(R.string.area_manager_goal);
                benefitstext = getResources().getString(R.string.area_manager_benifits);
                heading1.setText(getResources().getString(R.string.to_become_area_manager_heading1));
                heading2.setText(getResources().getString(R.string.to_become_area_manager_heading2));
                steps.setText(getResources().getString(R.string.steps_to_become_area_manager));
                break;
            case 4:
                goaltext = getResources().getString(R.string.regional_manager_goal);
                benefitstext = getResources().getString(R.string.regional_manager_benifits);
                heading1.setText(getResources().getString(R.string.to_become_regional_manager_heading1));
                heading2.setText(getResources().getString(R.string.to_become_regional_heading2));
                steps.setText(getResources().getString(R.string.steps_to_become_regional_manager));
                break;
            case 5:
                goaltext = getResources().getString(R.string.sales_director_goal);
                benefitstext = getResources().getString(R.string.sales_director_benifits);
                heading1.setText(getResources().getString(R.string.to_become_salse_director_heading1));
                heading2.setText(getResources().getString(R.string.to_become_sales_director_heading2));
                steps.setText(getResources().getString(R.string.steps_to_become_sales_director));
                break;
        }

        //for goal
        Spannable word = new SpannableString(goaltext.substring(0, 10));
        word.setSpan(new ForegroundColorSpan(Color.parseColor("#be4d66")), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        word.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        goal.setText(word);
        Spannable wordTwo = new SpannableString(goaltext.substring(10));
        wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#222427")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        goal.append(wordTwo);
//for beneifts
        Spannable word1 = new SpannableString(benefitstext.substring(0, 9));
        word1.setSpan(new ForegroundColorSpan(Color.parseColor("#be4d66")), 0, word1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        word1.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        benefits.setText(word1);
        Spannable wordTwo1 = new SpannableString(benefitstext.substring(9));
        wordTwo1.setSpan(new ForegroundColorSpan(Color.parseColor("#222427")), 0, wordTwo1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        benefits.append(wordTwo1);

    }

    private void showinfo() {


        LndAgentBean header = new LndAgentBean();
        header.setType(6);
        data.add(header);

        adapter.notifyDataSetChanged();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shareonmail:
                sendEmail();
                break;
            case R.id.shareonwhatsapp:
                whatsappShare();
                break;
            case R.id.shareonfb:
                fbSharing();
                break;
            case R.id.shareonsms:
                sendsms();
                break;
            case R.id.shareontwitter:
                setUpViewsForTweetComposer();
                break;

        }
    }
}
