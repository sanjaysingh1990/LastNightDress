package com.eowise.recyclerview.stickyheaders.samples.MyPurchases;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.LndBaseActivity;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ColoredRatingBar;
import com.eowise.recyclerview.stickyheaders.samples.Utils.TimeAgo;
import com.eowise.recyclerview.stickyheaders.samples.adapters.MyPurchasesAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.MySalesPurchasesData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RateUserActivity extends LndBaseActivity {

    private MyPurchasesAdapter adapter;
    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.sellername)
    TextView sellername;
    @Bind(R.id.actionbutton)
    TextView actionbutton;
    @Bind(R.id.ratingBar)
    ColoredRatingBar ratingbar;
    @Bind(R.id.yourcomment)
    EditText yourcomment;
    @Bind(R.id.sellerprofilepfic)
    CircleImageView profilepic;
    boolean check1 = true, check2 = true;
    JSONObject rateuser = new JSONObject();
    MySalesPurchasesData mspd;
    private String forwhat="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchases_rateuser_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        //appyling font
        heading.setTypeface(SingleTon.robotobold);
        actionbutton.setTypeface(SingleTon.robotomedium);
        //comment text
        yourcomment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (check1 && s.length() > 0) {
                    enable();
                } else if (check2 && s.length() == 0) {
                    disable();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
           /* ratingbar.setRating(extra.getInt("ratedvalue",0));

            yourcomment.setText(extra.getString("comment", ""));
            actionbutton.setText("Resubmit My Rating");
        */
            try {
                mspd = (MySalesPurchasesData) extra.getSerializable("data");
                sellername.setText(mspd.getSeller_name());
                SingleTon.imageLoader.displayImage(mspd.getSeller_profile_pic(), profilepic, SingleTon.options3);
                forwhat=mspd.getForwhat();
                if (mspd.getForwhat().compareToIgnoreCase("update") == 0) {
                    ratingbar.setRating(mspd.getRated_val());
                    yourcomment.setText(mspd.getReview_msg());
                } else {

                }
            } catch (Exception ex) {

            }
            //creating json request


        }
    }

    @Override
    public void enable() {
        actionbutton.setClickable(true);
        actionbutton.setTextColor(Color.parseColor("#be4d66"));
        check1 = false;
        check2 = true;
    }

    @Override
    public void disable() {
        actionbutton.setClickable(false);
        actionbutton.setTextColor(Color.parseColor("#b4b4b4"));
        check2 = false;
        check1 = true;
    }

    public void back(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void submitRating(View v) {
        int value = (int) ratingbar.getRating();
        if (ratingbar.getRating() == 0) {
            Toast.makeText(this, "Please select your rating", Toast.LENGTH_SHORT).show();
            return;
        } else if (yourcomment.getText().length() == 0) {
            yourcomment.setError("comment field empty");
            return;
        }
      /*  SharedPreferences.Editor edit = SingleTon.pref.edit();
        edit.putBoolean("rated", true);
        edit.putInt("ratedvalue", value);
        edit.putString("comment", yourcomment.getText().toString());

        edit.commit();
        Toast.makeText(this, "Thank you for rating", Toast.LENGTH_SHORT).show();
        finish();*/
        if (mspd != null) {
            try {
                rateuser.put("date_time", SingleTon.getCurrentTimeStamp());
                rateuser.put("order_id", mspd.getOrder_id());
                rateuser.put("reviewby", SingleTon.pref.getString("user_id", "'"));
                rateuser.put("reviewto", mspd.getSeller_id());
                rateuser.put("review_msg", yourcomment.getText().toString());
                rateuser.put("rated_val", value);
                if(forwhat.compareToIgnoreCase("update")==0)
                {
                    rateSeller(15);
                }
                else
                rateSeller(14);
            } catch (JSONException ex) {

            }
        }
    }

    private void rateSeller(final int rqid) {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_SHIPPINGINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("json", response);
                try {

                    JSONObject jobj = new JSONObject(response);
                    if (jobj.getBoolean("status")) {
                        if(forwhat.compareToIgnoreCase("update")==0) {
                            Intent backcall = new Intent();
                            backcall.putExtra("review_msg",yourcomment.getText().toString());
                            backcall.putExtra("rating_val",(int)ratingbar.getRating());
                            setResult(203, backcall);
                            finish();
                        }
                        else
                        {
                            Intent backcall = new Intent();
                            backcall.putExtra("MESSAGE", "ITEMACCEPTED");
                            setResult(202, backcall);
                            finish();
                        }
                        } else {
                        Toast.makeText(RateUserActivity.this, "some thing went wrong", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                    Log.e("jsonerror", e.getMessage() + "");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    new com.eowise.recyclerview.stickyheaders.samples.AlertDialog().showAlertDialog(RateUserActivity.this);
                } catch (Exception ex) {

                }
                Log.e("response", error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", rqid+"");
                params.put("data", rateuser.toString());
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
