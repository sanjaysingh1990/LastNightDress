package com.eowise.recyclerview.stickyheaders.samples.MySales;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

public class  MySalesReportRatingActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.submit)
    TextView submit;
    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.cancelinfoeditext)
    EditText cancelinfo;
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
        cancelinfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (radiobtn1.isChecked() || radiobtn2.isChecked())
                    enable();
            }
        });
    }

    public void back(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void enable() {
        submit.setClickable(true);
        submit.setTextColor(ContextCompat.getColor(this, R.color.lndcolor));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                finish();
                break;
            case R.id.radiobutton1:
                if (cancelinfo.getText().length() > 0)
                    enable();
                break;

            case R.id.radiobutton2:
                enable();
                break;
        }
    }
}
