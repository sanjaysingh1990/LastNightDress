package com.eowise.recyclerview.stickyheaders.samples.Settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.ChangePassword.ChangePassword;
import com.eowise.recyclerview.stickyheaders.samples.EditProfile.EditProfilePrivate;
import com.eowise.recyclerview.stickyheaders.samples.EditProfile.EditProfileShop;
import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.MyPurchases.MyPurchasesActivity;
import com.eowise.recyclerview.stickyheaders.samples.MySales.SalesActivity;
import com.eowise.recyclerview.stickyheaders.samples.Notification.NotificationActivity;
import com.eowise.recyclerview.stickyheaders.samples.LndUserProfile.LndProfile;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.UserProfile.LndLoginSignup;
import com.eowise.recyclerview.stickyheaders.samples.contacts.ContactsActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SettingsActivity extends AppCompatActivity {
    @Bind(R.id.heading)
    TextView heading;
    @Bind({R.id.fbfrnds, R.id.contactsfrnds, R.id.editprofiletext, R.id.changepasstext, R.id.allowswaps, R.id.privacypolicytext, R.id.banktext, R.id.mysales, R.id.mypurchases, R.id.logout, R.id.notificationtext})
    List<TextView> settingstext;
    CallbackManager sCallbackManager;
    @Bind(R.id.swapstatus)
    SwitchCompat swap_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //reference
        ButterKnife.bind(this);

        //ceraeting custom font
        Typeface tf = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-Bold.otf");
        Typeface tf1 = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-Regular.otf");
        //applying custom font
        heading.setTypeface(tf);
        for (int i = 0; i < settingstext.size(); i++) {
            settingstext.get(i).setTypeface(tf1);
        }
        //find contacts
        findViewById(R.id.contactsfrnds).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                Intent contact = new Intent(SettingsActivity.this, ContactsActivity.class);
                startActivity(contact);
            }
        });
        //privacy policy
        findViewById(R.id.privacypolicy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                Intent i = new Intent(SettingsActivity.this, PrivacyPolicy.class);
                startActivity(i);
            }
        });
        findViewById(R.id.changepass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                Intent i = new Intent(SettingsActivity.this, ChangePassword.class);
                startActivity(i);
            }
        });

        findViewById(R.id.editprofile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me

                String utype = ImageLoaderImage.pref.getString("utype", "type");
                if (utype.compareTo("shop") == 0) {
                    Intent i = new Intent(SettingsActivity.this, EditProfileShop.class);
                    i.putExtra("response", LndProfile.response);
                    startActivity(i);
                } else if (utype.compareTo("private") == 0) {
                    Intent i = new Intent(SettingsActivity.this, EditProfilePrivate.class);
                    i.putExtra("response", LndProfile.response);

                    startActivity(i);

                }

            }
        });

        //my sales
        findViewById(R.id.mysalesparent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                Intent i = new Intent(SettingsActivity.this, SalesActivity.class);
                startActivity(i);
            }
        });

        //notification
        findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                Intent notification = new Intent(SettingsActivity.this, NotificationActivity.class);
                startActivity(notification);
            }
        });

        //my purchases
        findViewById(R.id.mypurchasesparent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                Intent i = new Intent(SettingsActivity.this, MyPurchasesActivity.class);
                startActivity(i);
            }
        });
        //logout
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor edit = ImageLoaderImage.pref.edit();
                edit.remove("uname");
                edit.apply();
                // handle me

                Intent intent = new Intent(getApplicationContext(), LndLoginSignup.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //  finishAffinity();
                ActivityCompat.finishAffinity(SettingsActivity.this);

            }
        });

        //send requests
        findViewById(R.id.fbfrnds).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String AppURl = "https://fb.me/1290239177669120";  //Generated from //fb developers

                String previewImageUrl = "https://lh3.googleusercontent.com/YxCdiUIkiV88w6N1NvTqKsLZtCpBB7VX6VxBEHVZbwZrhiKoiFbkOctm-_sdJCSnacki=h900";


                sCallbackManager = CallbackManager.Factory.create();

                if (AppInviteDialog.canShow()) {
                    AppInviteContent content = new AppInviteContent.Builder()
                            .setApplinkUrl(AppURl).setPreviewImageUrl(previewImageUrl)
                            .build();

                    AppInviteDialog appInviteDialog = new AppInviteDialog(SettingsActivity.this);
                    appInviteDialog.registerCallback(sCallbackManager,
                            new FacebookCallback<AppInviteDialog.Result>() {
                                @Override
                                public void onSuccess(AppInviteDialog.Result result) {
                                    Toast.makeText(SettingsActivity.this, "Invitation sent", Toast.LENGTH_SHORT).show();
                                    // Log.e("Invitation", "Invitation Sent Successfully");
                                    //finish();
                                }

                                @Override
                                public void onCancel() {
                                    Toast.makeText(SettingsActivity.this, "Invitation Cancelled", Toast.LENGTH_SHORT).show();

                                    //  Log.e("Invitation", "Invitation Sent Successfully");

                                }

                                @Override
                                public void onError(FacebookException e) {
                                    Toast.makeText(SettingsActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();

                                    // Log.e("Invitation", "Error Occured");
                                }
                            });

                    appInviteDialog.show(content);
                }

            }
        });


//switch listener
        swap_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String userid = ImageLoaderImage.pref.getString("user_id", "");
                SharedPreferences.Editor edit = ImageLoaderImage.pref.edit();
                if (b) {
                    changeSwapstatus(1, userid);
                      edit.putBoolean("swap", true);
                } else {
                    changeSwapstatus(0, userid);
                    edit.putBoolean("swap", false);
                }
                edit.commit();
            }
        });

        //check swap on or off
        if(ImageLoaderImage.pref.getBoolean("swap",false)) {
           swap_status.setChecked(true);
        }
        else
        {
            swap_status.setChecked(false);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void back(View v) {
        onBackPressed();
    }


    public void changeSwapstatus(final int swapstatus, final String userid) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("wait changing...");
        pDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/lndusers.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
              /*  Log.e("response", response.toString());
               try {
                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status")) {
                        if (jobj.getString("value").compareTo("0") == 0)
                            Toast.makeText(SettingsActivity.this, "swap locked", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(SettingsActivity.this, "swap unlocked", Toast.LENGTH_SHORT).show();

                    } else

                        Toast.makeText(SettingsActivity.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)

            {
                pDialog.dismiss();
                Toast.makeText(SettingsActivity.this, "can't change swap status", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "11");
                params.put("user_id", userid);

                params.put("value", swapstatus + "");


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
