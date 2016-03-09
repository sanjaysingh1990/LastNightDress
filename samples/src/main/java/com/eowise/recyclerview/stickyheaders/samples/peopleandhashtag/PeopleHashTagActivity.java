package com.eowise.recyclerview.stickyheaders.samples.peopleandhashtag;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // setTheme(R.style.AppThemeBlue);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_hashtag);
        Search= (EditText) findViewById(R.id.search);

        initToolbar();
        initViewPagerAndTabs();
    }


    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

    }

    private void initViewPagerAndTabs() {
         viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(PeopleFragment.createInstance(20), "People");
        pagerAdapter.addFragment(BrandNameFragment.createInstance(21), "Brands");
        pagerAdapter.addFragment(HashTagFragment.createInstance(4), "Hashtags");
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0)
                    Search.setHint("Search people");
                else if(position==1)
                    Search.setHint("Search brands");
                else
                    Search.setHint("Search hashtags");


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    static class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
public void newmessage(View v)
{
    Intent nm=new Intent(this, NewMessageActivity.class);
    startActivity(nm);
}
    public void back(View v)
    {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}