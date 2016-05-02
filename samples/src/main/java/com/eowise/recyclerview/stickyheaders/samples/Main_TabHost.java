package com.eowise.recyclerview.stickyheaders.samples;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.LndNotificationMessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.NotificationFragment;
import com.eowise.recyclerview.stickyheaders.samples.LndUserProfile.LndProfile;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.StickyActivity;
import com.eowise.recyclerview.stickyheaders.samples.TabDemo.LndFragment;
import com.eowise.recyclerview.stickyheaders.samples.TabDemo.LndShopActivity;
import com.eowise.recyclerview.stickyheaders.samples.Utils.BlankActivity;
import com.eowise.recyclerview.stickyheaders.samples.data.NotificationData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Main_TabHost extends AppCompatActivity {
    public static TabWidget tabWidget;
    public static TabHost tabHost;
    public static Activity activity;
    static Toast toast;

    Intent intent;
    int[] icons = {R.drawable.home_gray, R.drawable.shopping_gray, R.drawable.camera, R.drawable.message_gray, R.drawable.profile_gray};
    int[] icons2 = {R.drawable.home, R.drawable.shopping, R.drawable.camera, R.drawable.message, R.drawable.profile};
    public static Stack<Integer> currenttab = new Stack<Integer>();
    Button showpopup;
    private String data = "";
    public static TextView message, notification, followers;
    public static PopupWindow popupWindow;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabhost_main);
        activity = this;
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabWidget = (TabWidget) findViewById(android.R.id.tabs);


        //  tabHost.setup();
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState); // state will be bundle your activity state which you get in onCreate
        tabHost.setup(mLocalActivityManager);

        TabHost.TabSpec spec;


        setupTab(new ImageView(this), new Intent().setClass(this, StickyActivity.class), R.drawable.home_gray);
        setupTab(new ImageView(this), new Intent().setClass(this, LndShopActivity.class), R.drawable.shopping_gray);
        setupTab(new ImageView(this), new Intent().setClass(this, BlankActivity.class), R.drawable.camera);
        setupTab(new ImageView(this), new Intent().setClass(this, Dummy.class), R.drawable.message_icon_gray);
        setupTab(new ImageView(this), new Intent().setClass(this, LndProfile.class), R.drawable.profile_gray);
        tabHost.setCurrentTab(1);

        //changing current selected tab back
        int tab = tabHost.getCurrentTab();
        View view = tabHost.getTabWidget().getChildAt(tab);
        ImageView imgView = (ImageView) view.findViewById(R.id.image);
        imgView.setImageResource(icons2[tab]);

        //
        View view2 = tabHost.getTabWidget().getChildAt(2);
        LinearLayout ll = (LinearLayout) view2.findViewById(R.id.tabsLayout);
        ll.setBackgroundResource(R.drawable.gradient_back_button_camera);
        //setting event to tabhost
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int currenttab = tabHost.getCurrentTab();
              /*if(currenttab==2)
                  return;*/

                try {
                    if (Main_TabHost.currenttab.search(currenttab) < 0) {
                        Main_TabHost.currenttab.push(currenttab);

                    } else {
                        Main_TabHost.currenttab.remove(currenttab);
                        Main_TabHost.currenttab.push(currenttab);


                    }
                } catch (Exception ex) {

                }
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    View view = tabHost.getTabWidget().getChildTabViewAt(i);
                    if (view != null) {

                        // get title text view
                        ImageView imgView = (ImageView) view.findViewById(R.id.image);
                        imgView.setImageResource(icons[i]);
                    }

                }
                int tab = tabHost.getCurrentTab();
                View view = tabHost.getTabWidget().getChildAt(tab);
                ImageView imgView = (ImageView) view.findViewById(R.id.image);
                imgView.setImageResource(icons2[tab]);


            }
        });

        showpopup = (Button) findViewById(R.id.show);
        showpopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotification(view);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getNotification();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void setupTab(final View view, final Intent intent, int iconid) {
        View tabview = createTabView(getApplicationContext(), iconid);
        TabHost.TabSpec setContent = tabHost.newTabSpec("").setIndicator(tabview).setContent(intent);
        tabHost.addTab(setContent);
    }

    private static View createTabView(final Context context, int iconid) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tabs, null);
        ImageView tv = (ImageView) view.findViewById(R.id.image);
        tv.setImageResource(iconid);

        return view;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LndFragment.checkFavorate();
        try {
            if (currenttab.size() > 0) {

                if (currenttab.peek() == 2) {
                    currenttab.pop();
                    tabHost.setCurrentTab(0);
                }


            }
        } catch (Exception ex) {

        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (LndProfile.totalswaps != null) {
            if (SingleTon.pref.getBoolean("swap", false)) {
                LndProfile.totalswaps.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                LndProfile.totalswaps.setText("0");
            } else {
                LndProfile.totalswaps.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bank_lock_icon, 0, 0, 0);
                LndProfile.totalswaps.setText("");

            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this, resultCode + "", Toast.LENGTH_SHORT).show();
        int pos =0;
        switch (requestCode) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                if (data == null)
                    return;

                Boolean status = data.getBooleanExtra("status", false);
                if (status) {
                    tabHost.setCurrentTab(3);
                    tabHost.setCurrentTab(4);
                }
                break;
            case 3:
                break;
            case 4:
                if (data == null)
                    return;
                String swapdata = data.getStringExtra("postids");
                sendSwap(swapdata);
                break;
            case 5:
                if (data == null)
                    return;
                LndProfile profile = (LndProfile) LndProfile.con;
                profile.updateList(data.getStringArrayListExtra("posarray"));
                break;
            case 6:
                if (data == null)
                    return;
                pos = data.getIntExtra("pos", 0);
                NotificationData nd = (NotificationData) data.getExtras().get("data");
                NotificationFragment.notification.setCheckout(pos, nd);
                break;
            case 7:
                if (data == null)
                    return;
                boolean check = data.getBooleanExtra("check", false);
                if (check)
                    ((LndProfile) LndProfile.con).getPorfile(SingleTon.pref.getString("user_id", ""));
                break;
            case 8:
                if (data == null)
                    return;
                pos = data.getIntExtra("pos", -1);

                NotificationFragment.notification.cancelSwap(pos);
                break;
            case 9:
                if (data == null)
                    return;
                LndFragment.lndshopactivity.updateList(data.getStringArrayListExtra("posarray"));
                break;
            case 10:
                break;

        }


    }


    @Override
    public void onBackPressed() {


        try

        {
            Toast.makeText(this, "status" + Main_TabHost.currenttab.pop(), Toast.LENGTH_SHORT).show();
            Main_TabHost.tabHost.setCurrentTab(Main_TabHost.currenttab.pop());
        } catch (Exception ex) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor edit = SingleTon.pref.edit();
        edit.putString("category", "");
        edit.commit();
    }

    private void showPopup() {
        //initializing popup
        //Creating the LayoutInflater instance
        LayoutInflater li = getLayoutInflater();
        //Getting the View object as defined in the customtoast.xml file
        View layout = li.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.custom_toast_layout));

        //Creating the Toast object
        toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(layout);//setting the view of custom toast layout
        toast.show();
    }

    private void showNotification(View v) {

        int msg = 0, noti = 0, foll = 0;
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.lnd_notification_popup, null);
        message = (TextView) popupView.findViewById(R.id.message);
        notification = (TextView) popupView.findViewById(R.id.notification);
        followers = (TextView) popupView.findViewById(R.id.followers);


        try {
            JSONObject jobj = new JSONObject(data);
            msg = jobj.getInt("messages");
            if (msg > 9)
                message.setText(msg + "+");
            else
                message.setText(msg + "");

            noti = jobj.getInt("notifications");
            if (noti > 9)
                notification.setText(noti + "+");
            else
                notification.setText(noti + "");

            foll = jobj.getInt("followers");
            if (foll > 9)
                followers.setText(foll + "+");
            else
                followers.setText(foll + "");

        } catch (JSONException ex) {
            Log.e("error", ex.getMessage());
        }
        popupWindow = new PopupWindow(
                popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);


        //to position popup window on the screen


        int width = SingleTon.displayMetrics.widthPixels;
        int margin = ((width / 5) * 30) / 100;

        if (msg > 0 || noti > 0 || foll > 0)
            popupWindow.showAsDropDown(v, -margin, 0);
        popupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabHost.setCurrentTab(3);
            }
        });

    }

    public void sendSwap(final String data) {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/inboxope.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status"))
                        showPopup();
                    else
                        Toast.makeText(getApplicationContext(), jobj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("rqid", "12");
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
        queue.add(sr);
    }

    public void getNotification() {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/inboxope.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                data = response;
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status"))
                        showpopup.performClick();
                    else
                        Toast.makeText(getApplicationContext(), jobj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("rqid", "14");
                params.put("user_id", SingleTon.pref.getString("user_id", ""));

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
