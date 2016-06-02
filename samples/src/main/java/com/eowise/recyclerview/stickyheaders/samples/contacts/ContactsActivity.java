package com.eowise.recyclerview.stickyheaders.samples.contacts;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.adapters.ContactsAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.PeopleData;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactsActivity extends AppCompatActivity
{
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    private List<PeopleData> data=new ArrayList<PeopleData>();
    private ContactsAdapter recyclerAdapter;
    @Bind(R.id.heading)TextView heading;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ButterKnife.bind(this);
        //setting custom font
        heading.setTypeface(SingleTon.hfont);


        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        // mLayoutManager.
        recyclerView.setLayoutManager(mLayoutManager);

       //load contacts
        if(data.size()==0) {
            LoadContactsAyscn lca = new LoadContactsAyscn();
            lca.execute();
        }
        else
            recyclerAdapter.notifyDataSetChanged();
        }
    class LoadContactsAyscn extends AsyncTask<Void, Void, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pd = ProgressDialog.show(ContactsActivity.this, "Loading Contacts",
                    "Please Wait");
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
            while (phones.moveToNext())
            {

                PeopleData peopleData=new PeopleData();
                peopleData.setUname(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                peopleData.setNumber(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                peopleData.setContactid(Long.parseLong(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID))));
                data.add(peopleData);
            }
            phones.close();
return null;
        }



        @Override
        protected void onPostExecute(String str)
        {

            pd.cancel();
            recyclerAdapter = new ContactsAdapter(ContactsActivity.this,data);
            recyclerView.setAdapter(recyclerAdapter);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void back(View v)
    {
        onBackPressed();
    }
}
