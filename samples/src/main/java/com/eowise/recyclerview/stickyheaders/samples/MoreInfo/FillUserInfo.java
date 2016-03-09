package com.eowise.recyclerview.stickyheaders.samples.MoreInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.samples.R;

import org.json.JSONObject;

public class FillUserInfo extends AppCompatActivity {

  static  ViewPager mViewPager;
  public static JSONObject jobj=new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_more_info);
        mViewPager = (ViewPager) findViewById(R.id.pager);

//disable viewpager swipe
        mViewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
/** set the adapter for ViewPager */

        mViewPager.setAdapter(new SamplePagerAdapter(
                getSupportFragmentManager()));
//getting bundle from
        Bundle extra=getIntent().getExtras();
        if(extra!=null)
        {
            try
            {

                String data=extra.getString("data");
                JSONObject jsonObject=new JSONObject(data);
                jobj.put("fbid",jsonObject.getString("fbid"));
                jobj.put("fullname",jsonObject.getString("fullname"));

                jobj.put("country",jsonObject.getString("country"));

                jobj.put("email",jsonObject.getString("email"));
                jobj.put("gender", jsonObject.getString("gender"));



            }
            catch(Exception ex)
            {
               Log.e("error",ex.getMessage()+"");
            }
        }
    }

    /** Defining a FragmentPagerAdapter class for controlling the fragments to be shown when user swipes on the screen. */
    public class SamplePagerAdapter extends FragmentPagerAdapter {

        public SamplePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            /** Show a Fragment based on the position of the current screen */
            if (position == 0) {
                return new UserNameFragment();
            } else if(position==1)
                return new UserDescriptionFragment();
            else
                return new UserTypeFragment();

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }


    }

    @Override
    public void onBackPressed() {
    int pos=mViewPager.getCurrentItem();
    if(pos>0) {
        pos--;
    mViewPager.setCurrentItem(pos);
    }
        else
        finish();
    }
}