package com.eowise.recyclerview.stickyheaders.samples.MyPurchases;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.adapters.MyPurchasesAdapter;
import com.eowise.recyclerview.stickyheaders.samples.adapters.MySalesAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.MySalesData;

import java.util.ArrayList;
import java.util.List;

public class MyPurchasesActivity extends AppCompatActivity {
    private List<MySalesData> items=new ArrayList<MySalesData>();
    private RecyclerView recyclerView;
    private MyPurchasesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchases);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyPurchasesAdapter(this,items);
        recyclerView.setAdapter(adapter);
        initialize();

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
        String[] status={"In Process","Delivered","Cancelled","Shipped","Claim Processing","Rating Reported","Claim Approved","Claim Declined","Cancelled"};

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
    public void itemAccepted(View v)
    {
        Intent rateuser=new Intent(this,RateUserActivity.class);
        startActivity(rateuser);
    }
}
