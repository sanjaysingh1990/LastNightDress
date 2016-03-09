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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.samples.LndMessage.MessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.LndNotificationMessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.LndUserProfile.LndProfile;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.StickyActivity;
import com.eowise.recyclerview.stickyheaders.samples.TabDemo.LndFragment;
import com.eowise.recyclerview.stickyheaders.samples.TabDemo.LndShopActivity;
import com.eowise.recyclerview.stickyheaders.samples.Utils.BlankActivity;

import java.util.Stack;

public class Main_TabHost extends AppCompatActivity
{
    public static TabWidget tabWidget;
    public static  TabHost tabHost;
    public static Activity activity;
    static  Toast toast;
    Intent intent;
    int[] icons={R.drawable.home_gray,R.drawable.shopping_gray,R.drawable.camera,R.drawable.message_gray,R.drawable.profile_gray};
    int[] icons2={R.drawable.home,R.drawable.shopping,R.drawable.camera,R.drawable.message,R.drawable.profile};
   public static Stack<Integer> currenttab=new Stack<Integer>();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabhost_main);
         activity=this;
        tabHost =(TabHost)findViewById(R.id.tabhost);
        tabWidget = (TabWidget) findViewById(android.R.id.tabs);


        //  tabHost.setup();
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState); // state will be bundle your activity state which you get in onCreate
        tabHost.setup(mLocalActivityManager);

        TabHost.TabSpec spec;



        setupTab(new ImageView(this), new Intent().setClass(this,StickyActivity.class),R.drawable.home_gray);
        setupTab(new ImageView(this),new Intent().setClass(this, LndShopActivity.class),R.drawable.shopping_gray);
        setupTab(new ImageView(this), new Intent().setClass(this, BlankActivity.class),R.drawable.camera);
        setupTab(new ImageView(this), new Intent().setClass(this, LndNotificationMessageActivity.class),R.drawable.message_icon_gray);
        setupTab(new ImageView(this), new Intent().setClass(this, LndProfile.class), R.drawable.profile_gray);
        tabHost.setCurrentTab(1);

        //changing current selected tab back
        int tab = tabHost.getCurrentTab();
        View view=  tabHost.getTabWidget().getChildAt(tab);
        ImageView imgView = (ImageView)view. findViewById(R.id.image);
        imgView.setImageResource(icons2[tab]);

        //
        View view2=  tabHost.getTabWidget().getChildAt(2);
        LinearLayout ll = (LinearLayout)view2. findViewById(R.id.tabsLayout);
        ll.setBackgroundResource(R.drawable.gradient_back_button_camera);
        //setting event to tabhost
      tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
          @Override
          public void onTabChanged(String tabId) {
              int currenttab= tabHost.getCurrentTab();
              /*if(currenttab==2)
                  return;*/

             try {
                 if (Main_TabHost.currenttab.search(currenttab) < 0) {
                     Main_TabHost.currenttab.push(currenttab);

                 }
                     else {
                     Main_TabHost.currenttab.remove(currenttab);
                     Main_TabHost.currenttab.push(currenttab);


                 }
             }
             catch(Exception ex)
             {

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
    }
    private void setupTab(final View view, final Intent intent,int iconid) {
        View tabview = createTabView(getApplicationContext(), iconid);
        TabHost.TabSpec setContent = tabHost.newTabSpec("").setIndicator(tabview).setContent(intent);
        tabHost.addTab(setContent);
    }

    private static View createTabView(final Context context,int iconid) {
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

                if(currenttab.peek()==2) {
                    currenttab.pop();
                    tabHost.setCurrentTab(1);
                }


            }
        }
            catch(Exception ex)
            {

            }

        }


    @Override
    protected void onResume() {
        super.onResume();

        if(LndProfile.totalswaps!=null)
        {
            if(ImageLoaderImage.pref.getBoolean("swap",false)) {
                LndProfile.totalswaps.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                LndProfile.totalswaps.setText("0");
            }
            else
            {
                LndProfile.totalswaps.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bank_lock_icon, 0, 0, 0);
                LndProfile.totalswaps.setText("");

            }
            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2)
        {
            if (data == null)
                return;

            Boolean status = data.getBooleanExtra("status",false);
           if(status)
           {
              tabHost.setCurrentTab(3);
               tabHost.setCurrentTab(4);
           }
          //
           // NotificationFragment.recyclerAdapter.notifyDataSetChanged();

        }
        else if (requestCode == 4)
        {
            if (data == null)
                return;
            String postids = data.getStringExtra("postids");
          // Toast.makeText(this,"status"+postids,Toast.LENGTH_SHORT).show();
           Log.e("data",postids);
           showPopup();
        }

        }

    @Override
    public void onBackPressed() {


try

        {
            Toast.makeText(this,"status"+Main_TabHost.currenttab.pop(),Toast.LENGTH_SHORT).show();
            Main_TabHost.tabHost.setCurrentTab( Main_TabHost.currenttab.pop());
        }
        catch(Exception ex)
        {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    SharedPreferences.Editor edit= ImageLoaderImage.pref.edit();
     edit.putString("category","");
     edit.commit();
    }

    private void showPopup()
    {
        //initializing popup
        //Creating the LayoutInflater instance
        LayoutInflater li = getLayoutInflater();
        //Getting the View object as defined in the customtoast.xml file
        View layout = li.inflate(R.layout.customtoast,(ViewGroup) findViewById(R.id.custom_toast_layout));

        //Creating the Toast object
        toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(layout);//setting the view of custom toast layout
        toast.show();
    }
}
