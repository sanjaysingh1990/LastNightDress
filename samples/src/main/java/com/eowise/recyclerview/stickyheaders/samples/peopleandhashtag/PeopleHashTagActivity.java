package com.eowise.recyclerview.stickyheaders.samples.peopleandhashtag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.eowise.recyclerview.stickyheaders.samples.NewMessage.NewMessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.R;

import java.util.ArrayList;
import java.util.List;

public class PeopleHashTagActivity extends AppCompatActivity {
     static EditText Search;
     static  ViewPager viewPager;
    MyPagerAdapter adapter;
    static Activity act;
      @Override
    protected void onCreate(Bundle savedInstanceState) {
       // setTheme(R.style.AppThemeBlue);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_hashtag);
        Search= (EditText) findViewById(R.id.search);
        act=this;
        initToolbar();
        initViewPagerAndTabs();
    }


    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

    }

    private void initViewPagerAndTabs() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("People"));
        tabLayout.addTab(tabLayout.newTab().setText("Brand"));
        tabLayout.addTab(tabLayout.newTab().setText("Hashtag"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
         adapter = new MyPagerAdapter
                (getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Search.setHint(tab.getText());
                Fragment fragment = adapter.getFragment(tab.getPosition());
                if (fragment != null) {
                    fragment.onResume();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
public void back(View v)
{
    onBackPressed();
}
}
