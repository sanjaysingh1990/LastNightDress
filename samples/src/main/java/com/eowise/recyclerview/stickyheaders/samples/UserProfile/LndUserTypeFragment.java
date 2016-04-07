package com.eowise.recyclerview.stickyheaders.samples.UserProfile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sanju on 26/1/16.
 */
public class LndUserTypeFragment extends Fragment implements View.OnClickListener {
   @Bind(R.id.shopuser)TextView shopuser;
    @Bind(R.id.privateuser)TextView privateuser;
    @Bind(R.id.ortext)TextView ortext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lnd_slect_usertype_layout, container, false);

        ButterKnife.bind(this, view);

       //font setup
        shopuser.setTypeface(SingleTon.subheading);
        privateuser.setTypeface(SingleTon.subheading);
        ortext.setTypeface(SingleTon.mainfont);

        //bind with listener
        shopuser.setOnClickListener(this);
        privateuser.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.shopuser:
                LndLoginSignup.currentpage=5;
                //current page value on stack;
                LndLoginSignup.currenttab.push(2);
                LndLoginSignup.mViewPager.setCurrentItem(5);
                break;
            case R.id.privateuser:
                LndLoginSignup.currentpage=3;
                //current page value on stack;
                LndLoginSignup.currenttab.push(2);
                LndLoginSignup.mViewPager.setCurrentItem(4);
                break;
        }
    }
}
