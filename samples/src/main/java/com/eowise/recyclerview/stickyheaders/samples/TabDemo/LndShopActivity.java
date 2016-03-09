package com.eowise.recyclerview.stickyheaders.samples.TabDemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.R;

/**
 * Example about replacing fragments inside a ViewPager. I'm using
 * android-support-v7 to maximize the compatibility.
 * 
 * @author Dani Lao (@dani_lao)
 * 
 */
public class LndShopActivity extends AppCompatActivity implements Animation.AnimationListener{

	// For this example, only two pages
	static final int NUM_ITEMS = 2;
    static AVLoadingIndicatorView prog;
	static ViewPager mPager;
	SlidePagerAdapter mPagerAdapter;
	static String currentcategory="";
	static Activity act;
	Animation anim1,anim2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lnd_shop);
		//storing current tab

		prog= (AVLoadingIndicatorView) findViewById(R.id.loader);
		/* Instantiate a ViewPager and a PagerAdapter. */
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new SlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		act=this;
       //initializing animation
		anim1= AnimationUtils.loadAnimation(this,R.anim.slide_out_left);
		anim2= AnimationUtils.loadAnimation(this,R.anim.slide_in_left);

		//animation listener
		 anim1.setAnimationListener(this);
		 anim2.setAnimationListener(this);

		mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	     }

	@Override
	public void onPageSelected(int position) {
if(position==0)
{
	Main_TabHost.tabWidget.startAnimation(anim2);

}
		else
{
	Main_TabHost.tabWidget.startAnimation(anim1);

}
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}
});
	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationEnd(Animation animation) {
 if(animation==anim1)
 {
	 Main_TabHost.tabWidget.setVisibility(View.GONE);
 }
		else
 {
	 Main_TabHost.tabWidget.setVisibility(View.VISIBLE);

 }
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}

	/* PagerAdapter class */
	public class SlidePagerAdapter extends FragmentPagerAdapter {
		public SlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			/*
			 * IMPORTANT: This is the point. We create a RootFragment acting as
			 * a container for other fragments
			 */
			if (position == 0)
				return new LndFragment();
			else {

					return new RootFragment();

			}
		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}
	}
	@Override
	public void onBackPressed()
	{
		if(mPager.getCurrentItem()>0)
			mPager.setCurrentItem(0);
else

		try

		{


			Main_TabHost.currenttab.pop();
			Main_TabHost.tabHost.setCurrentTab(Main_TabHost.currenttab.pop());
		}
		catch(Exception ex)
		{
			finish();
		}
	}

}
