package com.eowise.recyclerview.stickyheaders.samples.Settings;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.R;

public class ReadMore extends AppCompatActivity {

    TextView heading, heading1, heading2, about1, about2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            int pos = extra.getInt("pos");
            if (pos == 1) {
                setContentView(R.layout.activity_privacy_policy);
                privacyPolicyview();
            } else if (pos == 2) {
                setContentView(R.layout.agent_layout);
                setHeader("Agent");
            } else if (pos == 3) {
                setContentView(R.layout.lnd_sale_world_wide);
                setHeader("Learn More");
            }
        }
    }

    private void privacyPolicyview() {
        //reference
        heading1 = (TextView) findViewById(R.id.heading1);
        heading2 = (TextView) findViewById(R.id.heading2);
        about1 = (TextView) findViewById(R.id.info1);
        about2 = (TextView) findViewById(R.id.info2);
        //custom font
        Typeface tf = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-Bold.otf");
        Typeface tf1 = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-Regular.otf");

        //setting font
        heading1.setTypeface(tf);
        heading2.setTypeface(tf);
        about1.setTypeface(tf1);
        about2.setTypeface(tf1);
        setHeader("Privacy Policy");
    }

    private void setHeader(String text) {
        heading = (TextView) findViewById(R.id.heading);
        heading.setText(text);
//setting font
        Typeface tf = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-Bold.otf");
        heading.setTypeface(tf);

    }

    public void back(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
