package com.eowise.recyclerview.stickyheaders.samples.LndMore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LndLuxuryandDesignerAuthentication extends AppCompatActivity {

    @Bind(R.id.heading)
    TextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lnd_luxury_designer_authentication);
        ButterKnife.bind(this);

        //apply font
        heading.setTypeface(SingleTon.robotobold);
    }

    public void back(View v) {
        finish();
    }
}
