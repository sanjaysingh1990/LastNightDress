package com.eowise.recyclerview.stickyheaders.samples.UserProfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sanju on 26/1/16.
 */
public class LndUserReferalCodeFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.finish)
    TextView finish;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lnd_user_reference_code, container, false);

        ButterKnife.bind(this, view);

        //font setup

        finish.setTypeface(SingleTon.mainfont);
        finish.setTypeface(SingleTon.subheading);

        //bind with listener
        finish.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {


        Intent i = new Intent(getActivity(), Main_TabHost.class);
        startActivity(i);
        ActivityCompat.finishAffinity(getActivity());
    }
}
