package com.eowise.recyclerview.stickyheaders.samples.Settings;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.R;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;

    public CustomPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ViewGroup layout = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (position == 0) {
            layout = (ViewGroup) inflater.inflate(R.layout.lnd_shipping_info_page1, collection, false);
            changeStyle((TextView) layout.findViewById(R.id.page1heading1));
            changeStyle((TextView) layout.findViewById(R.id.page1heading2));
            changeStyle((TextView) layout.findViewById(R.id.page1heading3));

        }
            else if (position == 1) {
            layout = (ViewGroup) inflater.inflate(R.layout.lnd_shipping_info_page2, collection, false);

        } else if(position==2) {
            layout = (ViewGroup) inflater.inflate(R.layout.lnd_shipping_info_page3, collection, false);
            changeStyle((TextView) layout.findViewById(R.id.page3heading1));
            changeStyle((TextView) layout.findViewById(R.id.page3heading2));
        }
            else
            layout = (ViewGroup) inflater.inflate(R.layout.lnd_shipping_info_page4, collection, false);

        collection.addView(layout);
        return layout;
    }
 private void changeStyle(TextView txt)
 {
    String text=txt.getText().toString();
     Spannable word = new SpannableString(text.substring(0,text.indexOf(":")));

     word.setSpan(new ForegroundColorSpan(Color.BLACK), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
     word.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0,word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

     txt.setText(word);
     Spannable wordTwo = new SpannableString(text.substring(text.indexOf(":")));

     wordTwo.setSpan(new ForegroundColorSpan(Color.BLACK),0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
     txt.append(wordTwo);
 }
    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return "demo";
    }


}