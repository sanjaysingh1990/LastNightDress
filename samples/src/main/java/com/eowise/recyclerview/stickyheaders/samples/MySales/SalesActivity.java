package com.eowise.recyclerview.stickyheaders.samples.MySales;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.adapters.MySalesAdapter;
import com.eowise.recyclerview.stickyheaders.samples.adapters.NotificationAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.MySalesData;
import com.eowise.recyclerview.stickyheaders.samples.data.NotificationData;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SalesActivity extends AppCompatActivity {
    private List<MySalesData> items=new ArrayList<MySalesData>();
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    @Bind(R.id.heading) TextView heading;
    private MySalesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        //initializing butter knife library
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MySalesAdapter(this,items);
        recyclerView.setAdapter(adapter);
        initialize();
        //appyling custom fonts
        heading.setTypeface(ImageLoaderImage.robotobold);
    }
    public void back(View v)
    {
     onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initialize()
    {
       String[] status={"In Process","Pending Acceptance","Delivered","Cancelled","Claim Processing","Claim Declined","Claim Approved","Cancelled"};
        for(int i=0;i<status.length;i++)
        {
            MySalesData mysales=new MySalesData();
            mysales.setStatus(status[i]);
            mysales.setUsertype("seller");
            if(i==status.length-1)
            {
                mysales.setUsertype("buyer");
            }

            items.add(mysales);
            adapter.notifyItemInserted(i);
        }
    }

}
