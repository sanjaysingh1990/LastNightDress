package com.eowise.recyclerview.stickyheaders.samples.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.Settings.ReadMore;

/**
 * Created by INIT on 4/19/2016.
 */
public class InstructionDialogs {
    public PopupWindow popupWindow;

    public InstructionDialogs(final Context con) {
        final LayoutInflater layoutInflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.custom_popup_menu, null);
        popupWindow = new PopupWindow(
                popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        TextView btnDismiss = (TextView) popupView.findViewById(R.id.close);
        TextView LearnMore = (TextView) popupView.findViewById(R.id.moreagent);

        btnDismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
//link for learn more
        LearnMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent learnmore = new Intent(con, ReadMore.class);
                learnmore.putExtra("pos",3);
                con.startActivity(learnmore);
            }
        });


        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
    }


    public void show(View v) {
        popupWindow.showAtLocation(v, Gravity.TOP, 0, 0);
    }
}
