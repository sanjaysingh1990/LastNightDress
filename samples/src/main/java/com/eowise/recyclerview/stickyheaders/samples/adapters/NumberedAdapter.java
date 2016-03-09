package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.LndPostFullView.LndFullStickyActivity;
import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.LndUserProfile.LndUserFullStickyActivity;
import com.eowise.recyclerview.stickyheaders.samples.data.ShopData;
import com.eowise.recyclerview.stickyheaders.samples.listeners.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class NumberedAdapter extends RecyclerView.Adapter {

  List<ShopData> shopdata;
    Activity con;
  private int forwhich=0;
  private OnLoadMoreListener onLoadMoreListener;
  private final int VIEW_ITEM = 1;
  private final int VIEW_PROG = 0;
  private boolean loading;
  private int visibleThreshold = 5;
  private int lastVisibleItem, totalItemCount;
  public NumberedAdapter(ArrayList<ShopData> sd,Activity con,int value,RecyclerView recyclerView) {

  shopdata=sd;
  this.con=con;
  this.forwhich=value;
    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

      final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
              .getLayoutManager();


      recyclerView
              .addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView,
                                       int dx, int dy) {
                  super.onScrolled(recyclerView, dx, dy);

                  totalItemCount = linearLayoutManager.getItemCount();
                  lastVisibleItem = linearLayoutManager
                          .findLastVisibleItemPosition();
                  if (!loading
                          && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached
                    // Do something
                    if (onLoadMoreListener != null) {
                      onLoadMoreListener.onLoadMore();
                    }
                    loading = true;
                  }
                }
              });
    }
  }
  public NumberedAdapter(ArrayList<ShopData> sd,Activity con,int value) {

    shopdata = sd;
    this.con = con;
    this.forwhich = value;
  }
  @Override
  public int getItemViewType(int position) {
    return shopdata.get(position) != null ? VIEW_ITEM : VIEW_PROG;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RecyclerView.ViewHolder vh;
    if (viewType == VIEW_ITEM) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
      Display display = con.getWindowManager().getDefaultDisplay();
      Point size = new Point();
      display.getSize(size);
      int width = size.x;
      int height = size.y;


      ViewGroup.LayoutParams params = view.getLayoutParams();

      params.height =(width/3);
      view.requestLayout();

      vh = new StudentViewHolder(view);
    }
    else {
      View v = LayoutInflater.from(parent.getContext()).inflate(
              R.layout.progressbar_item, parent, false);

      vh = new ProgressViewHolder(v);
    }
    return vh;



  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
double price_now=0.0;

    String country=ImageLoaderImage.pref.getString("country","united states");

    if (holder instanceof StudentViewHolder) {

      if((position+1)%3==0)
      ((StudentViewHolder) holder).separator.setVisibility(View.GONE);
        else
        ((StudentViewHolder) holder).separator.setVisibility(View.VISIBLE);


      ShopData sd=shopdata.get(position);
    //convert value
      try
      {

        price_now=Double.parseDouble(sd.getPrice());
      }
      catch(Exception ex)
      {

      }
    //settting price
     ImageLoaderImage.showValue(country, ((StudentViewHolder) holder).price,price_now);

      if(sd.getImageurl().endsWith(".jpg"))
      ImageLoaderImage.imageLoader.displayImage(sd.getImageurl(), ((StudentViewHolder) holder).img, ImageLoaderImage.options);
      ((StudentViewHolder) holder).img.setOnClickListener(new MyEvent(position));


    } else {
     // ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
    }



  }
  public void setLoaded() {
    loading = false;
  }
  @Override
  public int getItemCount() {
    return shopdata.size();
  }
  private class MyEvent implements View.OnClickListener
  {
    int pos;
     public MyEvent(int pos)
     {
       this.pos=pos;
     }
    @Override
    public void onClick(View v) {
      if(forwhich==100) {
        Intent i = new Intent(con,LndUserFullStickyActivity.class);
       // i.putExtra("post_id",shopdata.get(pos).getPostid());
        i.putExtra("post_location",pos);

      //  con.startActivity(i);
      }
      else if(forwhich==200)
      {
        Intent i = new Intent(con, LndFullStickyActivity.class);
        i.putExtra("post_location",pos);

        con.startActivity(i);
      }
    }
  }
  public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
    this.onLoadMoreListener = onLoadMoreListener;
  }
  public static class ProgressViewHolder extends RecyclerView.ViewHolder {
    public AVLoadingIndicatorView progressBar;

    public ProgressViewHolder(View v) {
      super(v);
      progressBar = (AVLoadingIndicatorView) v.findViewById(R.id.progress);
    }
  }
  public static class StudentViewHolder extends RecyclerView.ViewHolder {
    public TextView price;
    public ImageView img;
    private Context con;
    private View separator;
    public StudentViewHolder(View itemView) {
      super(itemView);
      price = (TextView) itemView.findViewById(R.id.price);
      img= (ImageView) itemView.findViewById(R.id.image);
    separator=  itemView.findViewById(R.id.separator);
    }
    public StudentViewHolder(View itemView,Context con) {
      super(itemView);
      this.con=con;

      price = (TextView) itemView.findViewById(R.id.price);
      img= (ImageView) itemView.findViewById(R.id.image);

    }
  }
}
