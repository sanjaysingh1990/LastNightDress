package com.eowise.recyclerview.stickyheaders.samples.LndAgent;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserLevelUpCongratulation extends AppCompatActivity {

    @Bind(R.id.user_level_logo)
    ImageView user_logo;
    @Bind(R.id.agent_level_text)
    TextView level_text;
    @Bind(R.id.user_level_info)
    TextView level_info;

     int[] user_level_commission = {0, 0, R.string.agent_level, R.string.agency_level, R.string.area_manager_level, R.string.regional_manager_level, R.string.sales_director_level};
    String[] user_level_text = {"", "", "Agent Level", "Agency Level", "Area Manager Level", "Regional Manager Level", "Sales Director Level"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_agent_level_congratulation);
        ButterKnife.bind(this);
        int user_pos = SingleTon.pref.getInt("user_position", 0);
        switch (user_pos) {
            case 2:
                setValues(2);
                break;
            case 3:
                setValues(3);
                break;
            case 4:
                setValues(4);
                break;
            case 5:
                setValues(5);
                break;
            case 6:
                setValues(6);
                break;

        }
    }

    private void setValues(int pos) {
        user_logo.setImageResource(ApplicationConstants.icons[pos]);
        level_text.setText(user_level_text[pos]);
        level_info.setText(user_level_commission[pos]);
    }

    public void profile(View v) {
        Intent agentproile = new Intent(this, Lnd_Agent_Profile.class);
        startActivity(agentproile);
        finish();
    }


}
