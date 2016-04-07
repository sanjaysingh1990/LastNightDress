package com.eowise.recyclerview.stickyheaders.samples.Notification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NotificationActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    @Bind(R.id.heading)
    TextView heading;
    @Bind({R.id.followtext, R.id.likestext, R.id.commenttext, R.id.sharetext, R.id.swaptext, R.id.saletext, R.id.messagetext})
    List<TextView> notification;
    @Bind({R.id.followsubtext, R.id.likessubtext, R.id.commentsubtext, R.id.sharesubtext, R.id.swapsubtext, R.id.salesubtext, R.id.messagesubtext})
    List<TextView> subtext;
    @Bind({R.id.followstatus, R.id.likesstatus, R.id.commentstatus, R.id.sharestatus, R.id.swapstatus, R.id.salestatus, R.id.messagestatus})
    List<Switch> switches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        //initialize
        ButterKnife.bind(this);
        heading.setTypeface(SingleTon.robotobold);
       //custom font

      for(int i=0;i<notification.size();i++)
      {
          notification.get(i).setTypeface(SingleTon.robotomedium);
          subtext.get(i).setTypeface(SingleTon.robotoregular);
          switches.get(i).setOnCheckedChangeListener(this);
      }


        //setup listeners

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void back(View v)
    {
        onBackPressed();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId())
        {

        }
    }
}
