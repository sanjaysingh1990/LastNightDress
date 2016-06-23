package com.eowise.recyclerview.stickyheaders.samples.MyPurchases;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ColoredRatingBar;
import com.eowise.recyclerview.stickyheaders.samples.adapters.MyPurchasesAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.MySalesPurchasesData;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RateUserActivity extends AppCompatActivity {

    private MyPurchasesAdapter adapter;
    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.sellername)
    TextView sellername;
    @Bind(R.id.actionbutton)
    TextView actionbutton;
    @Bind(R.id.ratingBar)
    ColoredRatingBar ratingbar;
    @Bind(R.id.yourcomment)
    EditText yourcomment;
    @Bind(R.id.sellerprofilepfic)
    CircleImageView profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchases_rateuser_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        //appyling font
        heading.setTypeface(SingleTon.robotobold);
        actionbutton.setTypeface(SingleTon.robotomedium);
        actionbutton.setClickable(false);
        //comment text
        yourcomment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    actionbutton.setClickable(true);
                    actionbutton.setTextColor(Color.parseColor("#be4d66"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
           /* ratingbar.setRating(extra.getInt("ratedvalue",0));

            yourcomment.setText(extra.getString("comment", ""));
            actionbutton.setText("Resubmit My Rating");
        */
            MySalesPurchasesData mspd = (MySalesPurchasesData) extra.getSerializable("data");
            sellername.setText(mspd.getSeller_name());
            SingleTon.imageLoader.displayImage(mspd.getSeller_profile_pic(), profilepic, SingleTon.options3);


        }
    }

    public void back(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void submitRating(View v) {
        int value = (int) ratingbar.getRating();
        if (ratingbar.getRating() == 0) {
            Toast.makeText(this, "Please select your rating", Toast.LENGTH_SHORT).show();
            return;
        } else if (yourcomment.getText().length() == 0) {
            yourcomment.setError("comment field empty");
            return;
        }
        SharedPreferences.Editor edit = SingleTon.pref.edit();
        edit.putBoolean("rated", true);
        edit.putInt("ratedvalue", value);
        edit.putString("comment", yourcomment.getText().toString());

        edit.commit();
        Toast.makeText(this, "Thank you for rating", Toast.LENGTH_SHORT).show();
        finish();
    }
}
