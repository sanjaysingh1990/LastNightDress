/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.NewMessage.NewMessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;


public class LndNotificationMessageActivity extends AppCompatActivity {
    public static final String FRAGMENT_TAG_DATA_PROVIDER_1 = "data provider 1";
    public static final String FRAGMENT_TAG_DATA_PROVIDER_2 = "data provider 2";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    public static Display display;
    private TextView heading;
    private ImageButton newmessage;
    public static AVLoadingIndicatorView loader;
    public LinearLayout instructionview;
    private FrameLayout container;
    private TextView insheading,inssubheading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_viewpager);
         display = getWindowManager().getDefaultDisplay();
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        heading = (TextView) findViewById(R.id.heading);
        newmessage = (ImageButton) findViewById(R.id.newmessage);
        loader = (AVLoadingIndicatorView) findViewById(R.id.loader);
        instructionview = (LinearLayout) findViewById(R.id.instructionview);
        container= (FrameLayout) findViewById(R.id.container);
//        Typeface tf=Typeface.createFromAsset(getAssets(),"Mural_Script.ttf");
        // heading.setTypeface(tf);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        mTabLayout.setupWithViewPager(mViewPager);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(new ExampleDataProviderFragment(), FRAGMENT_TAG_DATA_PROVIDER_1)
                    .add(new ExampleDataProviderFragment2(), FRAGMENT_TAG_DATA_PROVIDER_2)
                    .commit();
        }
        mTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#be4d66"));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1)
                    newmessage.setVisibility(View.VISIBLE);
                else
                    newmessage.setVisibility(View.GONE);
             if(isnotification||ismessage)
                 showinfo(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

         insheading= (TextView) findViewById(R.id.insheading);
         inssubheading= (TextView) findViewById(R.id.inssubheading);
        insheading.setTypeface(SingleTon.robotomedium);
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) container.getLayoutParams();
        params.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        container.requestLayout();
    }

    public AbstractDataProvider getDataProvider(String dataProviderName) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(dataProviderName);
        return ((ExampleDataProviderFragment) fragment).getDataProvider();
    }

    public AbstractDataProvider2 getDataProvider2(String dataProviderName) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(dataProviderName);
        return ((ExampleDataProviderFragment2) fragment).getDataProvider();
    }

    private void onItemUndoActionClicked() {
        int position;
        try {
            position = getDataProvider(FRAGMENT_TAG_DATA_PROVIDER_2).undoLastRemoval();
        } catch (Exception ex) {
            position = getDataProvider2(FRAGMENT_TAG_DATA_PROVIDER_2).undoLastRemoval();

        }
        int index = mViewPager.getCurrentItem();
        MyPagerAdapter adapter = (MyPagerAdapter) mViewPager.getAdapter();
        final Fragment fragment = adapter.getItem(mViewPager.getCurrentItem());
        if (fragment == null)
            Log.e("status", "is null");
        else
            Log.e("status", "is not null");

        if (position >= 0) {
            ((MessageFragment) fragment).notifyItemInserted(position);
        }


    }

    public void onItemRemoved(int position) {
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.pager),
                R.string.snack_bar_text_item_removed,
                Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.snack_bar_action_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemUndoActionClicked();
            }
        });
        //  snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.snackbar_action_color_done));
        final View snackBarView = snackbar.getView();
        final CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackBarView.getLayoutParams();

        params.setMargins(0, 0, 0, 100);

        snackBarView.setLayoutParams(params);
        snackbar.show();
    }

    public void onItemRemovedNotification(int position) {
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.pager),
                R.string.snack_bar_text_item_removed,
                Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.snack_bar_action_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemUndoActionClicked2();
            }
        });
        // snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.snackbar_action_color_done));

        final View snackBarView = snackbar.getView();
        final CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackBarView.getLayoutParams();

        params.setMargins(0, 0, 0, 100);

        snackBarView.setLayoutParams(params);
        snackbar.show();
    }

    private void onItemUndoActionClicked2() {
        int position = getDataProvider(FRAGMENT_TAG_DATA_PROVIDER_1).undoLastRemoval();
        int index = mViewPager.getCurrentItem();
        MyPagerAdapter adapter = (MyPagerAdapter) mViewPager.getAdapter();
        final Fragment fragment = adapter.getItem(mViewPager.getCurrentItem());

        if (position >= 0) {
            ((NotificationFragment) fragment).notifyItemInserted(position);
        }

    }

    public void newmessage(View v) {
        Intent nm = new Intent(this, NewMessageActivity.class);
        startActivity(nm);
    }

    public void showInstruction(int from) {
        if(from==1)
            isnotification=true;
        else if(from==2)
        ismessage=true;
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) container.getLayoutParams();
        params.setBehavior(null);
        container.requestLayout();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);  // or however you need to do it for your code
        AppBarLayout.LayoutParams params2 = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params2.setScrollFlags(0);
        instructionview.setVisibility(View.VISIBLE);
    }
private boolean isnotification=false,ismessage=false;

    private void showinfo(int pos)
    {

        if(pos==0)
        {
            insheading.setText(getResources().getString(R.string.noti_heading));
            inssubheading.setText(getResources().getString(R.string.noti_ins_subheading));
        }
        else
        {

            insheading.setText(getResources().getString(R.string.message_heading));
            inssubheading.setText(getResources().getString(R.string.message_ins_subheading));

        }
    }
}
