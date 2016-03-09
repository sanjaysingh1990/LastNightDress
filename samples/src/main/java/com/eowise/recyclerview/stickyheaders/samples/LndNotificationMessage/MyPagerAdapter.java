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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return NotificationFragment.newInstance(
                        LndNotificationMessageActivity.FRAGMENT_TAG_DATA_PROVIDER_1, false);
            case 1:
                return MessageFragment.newInstance(
                        LndNotificationMessageActivity.FRAGMENT_TAG_DATA_PROVIDER_2, true);
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
       if(position==0)
        return "NOTIFICATION";
    else
           return "MESSAGES";
    }
}
