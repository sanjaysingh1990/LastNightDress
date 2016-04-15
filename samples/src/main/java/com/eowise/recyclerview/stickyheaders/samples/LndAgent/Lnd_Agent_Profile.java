package com.eowise.recyclerview.stickyheaders.samples.LndAgent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.data.LndAgentBean;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Lnd_Agent_Profile extends AppCompatActivity {
    @Bind(R.id.recycler)RecyclerView recyclerView;
    ArrayList<LndAgentBean> data=new ArrayList<>();
    AgentListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lnd__agent__profile);
        ButterKnife.bind(this);
         adapter=new AgentListAdapter(this,data);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
      getdata();

    }
    private void getdata()
    {

        LndAgentBean db=new LndAgentBean();
        db.setType(1);
        data.add(db);
        LndAgentBean db2=new LndAgentBean();
        db2.setType(2);
        data.add(db2);
        LndAgentBean db3=new LndAgentBean();
        db3.setType(3);
        data.add(db3);

        LndAgentBean db4=new LndAgentBean();
        db4.setType(1);
        data.add(db4);
        LndAgentBean db5=new LndAgentBean();
        db5.setType(2);
        data.add(db5);
        LndAgentBean db6=new LndAgentBean();
        db6.setType(2);
        data.add(db6);
        LndAgentBean db7=new LndAgentBean();
        db7.setType(2);
        data.add(db7);
       adapter.notifyDataSetChanged();
    }

}
