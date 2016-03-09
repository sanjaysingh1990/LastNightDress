package com.eowise.recyclerview.stickyheaders.samples.UserProfile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sanju on 28/1/16.
 */
public class ResetPasswordFragment extends Fragment

{

    @Bind(R.id.next)TextView next;
    @Bind(R.id.back)ImageButton back;
    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.useremail)
    EditText useremail ;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.lnd_reset_password_layout1, container, false);


        //initializing butter knife
        ButterKnife.bind(this, view);
        // set custom font
        heading.setTypeface(ImageLoaderImage.robotobold);
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

             try {
                 LndLoginSignup.mViewPager.setCurrentItem(LndLoginSignup.currenttab.pop());
             } catch (Exception ex) {

             }
         }
     });


        return view;
    }

}
