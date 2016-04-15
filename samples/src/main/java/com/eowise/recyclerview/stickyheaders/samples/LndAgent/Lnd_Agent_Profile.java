package com.eowise.recyclerview.stickyheaders.samples.LndAgent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.eowise.recyclerview.stickyheaders.samples.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Lnd_Agent_Profile extends AppCompatActivity {
    @Bind(R.id.recycler)RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lnd__agent__profile);
        ButterKnife.bind(this);


    }

}
