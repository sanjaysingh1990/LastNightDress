package com.eowise.recyclerview.stickyheaders.samples.Settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TipsActivity extends AppCompatActivity {


    TextView headertext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            int type = extra.getInt("type");
            if (type == 1) {
                setContentView(R.layout.activity_returns_tips);
            } else if (type == 2) {
                setContentView(R.layout.activity_photos_tips);
            } else if (type == 3) {
                setContentView(R.layout.activity_swapping_tips);
            } else if (type == 4) {
                setContentView(R.layout.activity_social_tips);
            } else if (type == 5) {
                setContentView(R.layout.lnd_luxury_designer_authentication);
            }
            ButterKnife.bind(this);
            headertext = (TextView) findViewById(R.id.heading);
            if (headertext != null) {
                headertext.setTypeface(SingleTon.robotobold);
                if (type == 1)
                    headertext.setText("Returns");
                else if (type == 2)
                    headertext.setText("Taking Photos");
                else if (type == 3)
                    headertext.setText("Swap");
                else if (type == 4)
                    headertext.setText("Social");
                else if (type == 5)
                    headertext.setText("Luxury and Designer Authentication");
            }

        }
    }

    public void back(View v) {
        finish();
    }
}
