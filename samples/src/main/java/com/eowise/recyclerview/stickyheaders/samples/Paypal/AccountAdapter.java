package com.eowise.recyclerview.stickyheaders.samples.Paypal;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class AccountAdapter extends FragmentPagerAdapter  {



    public AccountAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new TextFragment(position);
    }

    @Override
    public int getCount() {
        return 6;
    }


}