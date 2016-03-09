package com.eowise.recyclerview.stickyheaders.samples.LndMessage;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ImageButton;


import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.NewMessage.NewMessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.R;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    ImageButton newmessage;
    public static AVLoadingIndicatorView prog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // setTheme(R.style.AppThemeBlue);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        //storing current tab

        prog= (AVLoadingIndicatorView) findViewById(R.id.loader);
        newmessage= (ImageButton) findViewById(R.id.newmessage);
        initToolbar();
        initViewPagerAndTabs();
    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("Activity");
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
    }

    private void initViewPagerAndTabs() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(NotificationFragment.createInstance(20), "Notification");
        pagerAdapter.addFragment(MessageFragment.createInstance(4), "Messages");
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position)
            {
              if(position==1)
                  newmessage.setVisibility(View.VISIBLE);
            else
                  newmessage.setVisibility(View.GONE);

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (data == null)
                return;

            String postids = data.getStringExtra("discard");
            Log.e("data", postids);
            NotificationFragment.recyclerAdapter.notifyDataSetChanged();

        }
    }
    @Override
    public void onBackPressed()
    {
        try

        {

            Main_TabHost.currenttab.pop();
            Main_TabHost.tabHost.setCurrentTab( Main_TabHost.currenttab.pop());
        }
        catch(Exception ex)
        {
            finish();
        }

    }
}