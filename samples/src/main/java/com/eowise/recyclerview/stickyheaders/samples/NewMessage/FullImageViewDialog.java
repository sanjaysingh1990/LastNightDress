package com.eowise.recyclerview.stickyheaders.samples.NewMessage;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;

/**
 * Created by INIT on 9/9/2016.
 */
public class FullImageViewDialog {
    public void show(String url,Context con)
    {
        final Dialog dialog = new Dialog(con, android.R.style.DeviceDefault_Light_ButtonBar); // making dialog full screen

//inflating dialog layout
        dialog.setContentView(R.layout.chat_image_full_view_layout);
        ImageView fullview= (ImageView) dialog.findViewById(R.id.fullview);
        SingleTon.imageLoader.displayImage(url,fullview, SingleTon.options3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
