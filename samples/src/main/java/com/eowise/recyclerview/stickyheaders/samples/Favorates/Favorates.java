package com.eowise.recyclerview.stickyheaders.samples.Favorates;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SQLDB.FavoriteData;
import com.eowise.recyclerview.stickyheaders.samples.adapters.FavoratesAdapter;
import com.eowise.recyclerview.stickyheaders.samples.adapters.MarginDecoration;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Favorates extends AppCompatActivity {
   private RecyclerView recyclerv;
    private FavoratesAdapter adapter;
    private List<FavoriteData> favitems=new ArrayList<FavoriteData>();
    @Bind(R.id.heading) TextView heading;
    @Bind(R.id.showinfo) TextView showinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorates);
       //initizlizing butter knife library

        ButterKnife.bind(this);
        //Sqllite db object reference here

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerv = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerv.addItemDecoration(new MarginDecoration(this));
        recyclerv.setHasFixedSize(true);
        recyclerv.setLayoutManager(new GridLayoutManager(this, 3));
        intialize();
        adapter=new FavoratesAdapter(favitems,this);
        recyclerv.setAdapter(adapter);
       //custom fonts
        heading.setTypeface(ImageLoaderImage.hfont);

    }
private void intialize()
{
    List<FavoriteData> contacts = ImageLoaderImage.db.getAllContacts();

   // Toast.makeText(this,"size"+contacts.size(),Toast.LENGTH_LONG).show();
    for (FavoriteData cn : contacts) {
        /*String log = "Id: "+cn.getPostid()+" ,Name: " + cn.getImageurl() + " ,Phone: " +
                cn.getCost();
        // Writing Contacts to log
        Log.e("Name: ", log);*/
        FavoriteData favdata=new FavoriteData();
        favdata.setPostid(cn.getPostid());
        favdata.setCost(cn.getCost());
        favdata.setImageurl(cn.getImageurl());
        favitems.add(favdata);
    }
    if(contacts.size()==0)
        showinfo.setVisibility(View.VISIBLE);
}
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
    public void back(View v)
    {
        onBackPressed();
    }
public void delFavorite(int pos)
{
    favitems.remove(pos);
    adapter.notifyDataSetChanged();
}

}
