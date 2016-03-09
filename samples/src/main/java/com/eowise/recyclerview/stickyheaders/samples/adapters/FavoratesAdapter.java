package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.eowise.recyclerview.stickyheaders.samples.Favorates.Favorates;
import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;

import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SQLDB.FavoriteData;


import java.util.ArrayList;
import java.util.List;

public class FavoratesAdapter extends RecyclerView.Adapter<FavoratesAdapter.TextViewHolder> {
  private static  List<FavoriteData> labels;
  private static Activity activity;
  public FavoratesAdapter(List<FavoriteData> data, Activity activity) {
    this.labels=data;
    this.activity=activity;
  }

  @Override
  public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photositem, parent, false);
    Display display = activity.getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    int width = size.x;
    int height = size.y;


    ViewGroup.LayoutParams params = view.getLayoutParams();

    params.height =(width/3);
    view.requestLayout();

    return new TextViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final TextViewHolder holder, final int position) {
   // final String label = labels.get(position);
    FavoriteData fd=labels.get(position);
    ImageLoaderImage.imageLoader.displayImage(fd.getImageurl(),holder.img,ImageLoaderImage.options);
    holder.price.setText(fd.getCost());

  }

  @Override
  public int getItemCount() {
    return labels.size();
  }

  public  class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView price;
    public ImageView img;

    private ImageButton delfav;
    public TextViewHolder(View itemView) {
      super(itemView);
      price = (TextView) itemView.findViewById(R.id.price);
      img= (ImageView) itemView.findViewById(R.id.image);
      delfav= (ImageButton) itemView.findViewById(R.id.delefav);
       delfav.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
      Favorates fav= (Favorates) activity;
     Log.e("position", getAdapterPosition() + "");

      ImageLoaderImage.db.deleteContact(labels.get(getAdapterPosition()).getPostid());
      fav.delFavorite(getAdapterPosition());
    notifyDataSetChanged();
    }
  }
}