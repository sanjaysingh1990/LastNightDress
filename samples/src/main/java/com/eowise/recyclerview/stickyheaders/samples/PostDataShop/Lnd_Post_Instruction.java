package com.eowise.recyclerview.stickyheaders.samples.PostDataShop;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;

/**
 * Created by sanju on 19/2/16.
 */
public class Lnd_Post_Instruction
{
    Context con;
    public Lnd_Post_Instruction(Context con)
    {
        this.con=con;
    }
    public PopupWindow instruction()
    {
        final LayoutInflater layoutInflater =(LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.post_description_dialog, null);
   final  PopupWindow   popupWindow = new PopupWindow(
                popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView heading1= (TextView) popupView.findViewById(R.id.heading);
        TextView heading2= (TextView) popupView.findViewById(R.id.heading2);
        TextView heading3= (TextView) popupView.findViewById(R.id.heading3);
        TextView text1= (TextView) popupView.findViewById(R.id.text1);
        TextView text2= (TextView) popupView.findViewById(R.id.text2);
        TextView text3= (TextView) popupView.findViewById(R.id.text3);
        TextView text4= (TextView) popupView.findViewById(R.id.text4);
        TextView text5= (TextView) popupView.findViewById(R.id.text5);
        TextView text6= (TextView) popupView.findViewById(R.id.text6);

        TextView close= (TextView) popupView.findViewById(R.id.close);
        //apply custom font
        heading1.setTypeface(SingleTon.robotobold);
        heading2.setTypeface(SingleTon.robotomedium);
        heading3.setTypeface(SingleTon.robotomedium);
        text1.setTypeface(SingleTon.robotoregular);
        text2.setTypeface(SingleTon.robotoregular);
        text3.setTypeface(SingleTon.robotoregular);
        text4.setTypeface(SingleTon.robotoregular);
        text5.setTypeface(SingleTon.robotoregular);
        text6.setTypeface(SingleTon.robotoregular);

       /* String textval4 = "<font color=#000000><b>rate. </b></font> <font>According to research, posts with hashtags receive twice as much engagement as those that don’t. Put it in another way, you can double your engagement and increase click through rates by including hashtags.</font>";
        String textval3 = "<font color=#000000><b>audience. </b></font> <font>Many people search for specific hashtags. By using the hashtags that are of interest to your ideal customer, you can increase the chances of being found.</font>";
        String textval2 = "<font color=#000000><b>Hashtags. </b></font> <font>When it comes to social media, the hashtag is used to draw attention, to organize, and to promote.</font>";

        text4.setText(Html.fromHtml(textval4));
        text3.setText(Html.fromHtml(textval3));
        text2.setText(Html.fromHtml(textval2));*/


        //     popupWindow.showAsDropDown(mTextView, 50, -30);
        // popupWindow.showAtLocation(mTextView,1,0,0);

        close.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }});
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);


return popupWindow;
    }

}
