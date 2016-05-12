package com.eowise.recyclerview.stickyheaders.samples.MySales;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MySalesReportRatingActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.submit)
    TextView submit;
    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.cancelinfoeditext)
    EditText comment;
    @Bind(R.id.radiobutton1)
    RadioButton radiobtn1;
    @Bind(R.id.radiobutton2)
    RadioButton radiobtn2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saller_reporting_rating_layout);
        ButterKnife.bind(this);
        heading.setTypeface(SingleTon.robotobold);
        submit.setOnClickListener(this);
        radiobtn1.setOnClickListener(this);
        radiobtn2.setOnClickListener(this);
        submit.setClickable(false);
    }

    public void back(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    public void onClick(View v) {
     switch (v.getId())
     {
         case R.id.submit:
             finish();
             break;
         case R.id.radiobutton1:
             submit.setClickable(true);
             break;

         case R.id.radiobutton2:
             submit.setClickable(true);
             break;
     }
    }
}
