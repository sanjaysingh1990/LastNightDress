package com.eowise.recyclerview.stickyheaders.samples.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.R;

public class TextViewHolder extends RecyclerView.ViewHolder {
  public TextView price;
  public ImageView img;
  private Context con;
 private ImageButton delfav;
  public TextViewHolder(View itemView) {
    super(itemView);
    price = (TextView) itemView.findViewById(R.id.price);
    img= (ImageView) itemView.findViewById(R.id.image);
  }




}