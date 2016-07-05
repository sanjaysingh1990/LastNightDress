package com.eowise.recyclerview.stickyheaders.samples.UserProfile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ForgetPasswordReset extends AppCompatActivity {

    @Bind(R.id.next)TextView next;
    @Bind(R.id.back)ImageButton back;
    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.useremail)
    EditText useremail ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lnd_reset_password_layout1);
        //initializing butter knife
        ButterKnife.bind(this);
        // set custom font
        heading.setTypeface(SingleTon.robotobold);
        //listener for email sent
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(useremail.getText().length()==0)
                {
                    useremail.requestFocus();
                    useremail.setError("Email is empty");
                    return;
                }
                //current page value on stack;
                LndLoginSignup.currenttab.push(3);
                LndLoginSignup.mViewPager.setCurrentItem(7);
            }
        });
        // on back click
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              finish();
            }
        });

    }
}
