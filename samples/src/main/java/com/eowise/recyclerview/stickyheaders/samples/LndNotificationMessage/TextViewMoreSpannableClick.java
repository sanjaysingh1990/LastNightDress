/*
 * Class handling the hash tags in a string.
 *  @auther Ramesh M Nair
 *
 * */

package com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import com.eowise.recyclerview.stickyheaders.samples.interfaces.TagClick;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TextViewMoreSpannableClick {

       TagClick mTagClick;





    public void addClickablePart(String nTagString,
                                                   TagClick TagClick) {


        this.mTagClick = TagClick;

        // Pattern for getting the hash tags from a string


        SpannableStringBuilder string = new SpannableStringBuilder(nTagString);

        CharSequence spanText;

        //for username

        spanText = nTagString.subSequence(0,nTagString.length());

        final CharSequence mLastTextSpan =spanText;

        string.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {

                // Click on each tag will get here

                //  Log.d("TAg--HAsh", String.format("Clicked", mLastTextSpan));
                mTagClick.clickedTag(mLastTextSpan);
            }

            @Override
            public void updateDrawState(TextPaint ds) {


                    ds.setUnderlineText(false);// Disable the

            }
        }, 0,nTagString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    }
}