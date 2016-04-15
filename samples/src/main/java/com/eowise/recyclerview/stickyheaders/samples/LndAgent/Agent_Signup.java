package com.eowise.recyclerview.stickyheaders.samples.LndAgent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.eowise.recyclerview.stickyheaders.samples.R;

public class Agent_Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent__signup);
    }
    public void signup(View view)
    {
        Intent agentproile=new Intent(this,Lnd_Agent_Profile.class);
        startActivity(agentproile);
    }
}
