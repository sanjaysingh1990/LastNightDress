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

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.R;


public class LndNotificationMessageActivity extends AppCompatActivity {
    public static final String FRAGMENT_TAG_DATA_PROVIDER_1 = "data provider 1";
    public static final String FRAGMENT_TAG_DATA_PROVIDER_2 = "data provider 2";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    public static Display display;
    private TextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_viewpager);
        display = getWindowManager().getDefaultDisplay();
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        heading = (TextView) findViewById(R.id.heading);
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
}
