package com.eowise.recyclerview.stickyheaders.samples.UserProfile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import com.eowise.recyclerview.stickyheaders.samples.R;

import org.json.JSONObject;

import java.util.Stack;

public class LndLoginSignup extends AppCompatActivity {

    static ViewPager mViewPager;
    public static JSONObject jobj = new JSONObject();
    public static int currentpage = -1;
    public static Stack<Integer> currenttab = new Stack<>();
    public static Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_more_info);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        act = this;
//disable viewpager swipe
       /* mViewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });*/
        // mViewPager.beginFakeDrag();
/** set the adapter for ViewPager */

        mViewPager.setAdapter(new SamplePagerAdapter(
                getSupportFragmentManager()));

    }

    /**
     * Defining a FragmentPagerAdapter class for controlling the fragments to be shown when user swipes on the screen.
     */
    public class SamplePagerAdapter extends FragmentPagerAdapter {

        public SamplePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            /** Show a Fragment based on the position of the current screen */
            if (position == 0) {
                return new LoginSignup();
            } else if (position == 1)
                return new LndLoginFragment();
            else if (position == 2)
                return new LndUserTypeFragment();
            else if (position == 3)
                return new PrivateFragment();
            else if (position == 4)
                return new ShopFragment();
            else if (position == 5)
                return new ResetPasswordFragment();
            else if (position == 6)
                return new LndUserDescriptionFragment();
            else if (position == 7)
                return new LndUserReferalCodeFragment();
            else

                return new ResetPasswordEmailSentFragment();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.    LndLoginSignup.mViewPager.setCurrentItem(0);

            return 9;
        }

        public int getItemPosition(Object object) {
            notifyDataSetChanged();
            return POSITION_NONE;
        }

    }

    @Override
    public void onBackPressed() {

        try {
            mViewPager.setCurrentItem(currenttab.pop());
        } catch (Exception ex) {
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  Toast.makeText(this,requestCode+"",Toast.LENGTH_SHORT).show();

        if (requestCode == 100 || requestCode == 200) {
            PrivateFragment.setResult(requestCode, resultCode, data);
        } else if (requestCode == 150 || requestCode == 250) {
            ShopFragment.setResult(requestCode, resultCode, data);
        } else
            LoginSignup.callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT < 16) {
            // Hide the status bar
            // getWindow().setFlag(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            // Hide the action bar

        } else {
            // Hide the status bar
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);


        }
    }
}