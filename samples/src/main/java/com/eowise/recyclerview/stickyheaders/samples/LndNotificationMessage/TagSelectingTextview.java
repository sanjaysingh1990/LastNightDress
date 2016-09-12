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



public class TagSelectingTextview {

    String mhastTagColor;
    TagClick mTagClick;
    int mHypeLinkEnabled;

    public SpannableStringBuilder addClickablePart(String nTagString,
                                                   TagClick TagClick, int hypeLinkEnabled, String hastTagColor, int len) {

        this.mhastTagColor = hastTagColor;

        this.mHypeLinkEnabled = hypeLinkEnabled;

        this.mTagClick = TagClick;

        // Pattern for getting the hash tags from a string


        Pattern hashTagsPattern = Pattern.compile("(#[a-zA-Z0-9_-]+)");
        Pattern hashTagsPattern2 = Pattern.compile("(@[a-zA-Z0-9_-]+)");

        SpannableStringBuilder string = new SpannableStringBuilder(nTagString);

        CharSequence spanText;
        int start;
        int end;

        // Matching the pattern with the existing string

        Matcher m = hashTagsPattern.matcher(nTagString);

        while (m.find()) {

            start = m.start();
            end = m.end();

            spanText = nTagString.subSequence(start, end);


            final CharSequence mLastTextSpan = spanText;

            string.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    // Click on each tag will get here

                    //  Log.d("TAg--HAsh", String.format("Clicked", mLastTextSpan));
                    mTagClick.clickedTag(mLastTextSpan);
                }

                @Override
                public void updateDrawState(TextPaint ds) {

                    // color for the hash tag
                    ds.setColor(Color.parseColor(mhastTagColor));

                    if (mHypeLinkEnabled == 0) {
                        ds.setUnderlineText(false);// Disable the
                        // underline for
                        // hash Tags.
                    } else {
                        ds.setUnderlineText(true);// Enables the
                        // underline for
                        // hash Tags.

                    }
                }
            }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        }


        // Matching the pattern with the existing string

        Matcher m2 = hashTagsPattern2.matcher(nTagString);

        while (m2.find()) {

            start = m2.start();
            end = m2.end();

            spanText = nTagString.subSequence(start, end);


            final CharSequence mLastTextSpan = spanText;

            string.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    // Click on each tag will get here

                    Log.d("TAg--HAsh", String.format("Clicked", mLastTextSpan));
                    mTagClick.clickedTag(mLastTextSpan);
                }

                @Override
                public void updateDrawState(TextPaint ds) {

                    // color for the hash tag
                    ds.setColor(Color.parseColor(mhastTagColor));
                 //hahaha   ds.setTextSize(20);
                    if (mHypeLinkEnabled == 0) {
                        ds.setUnderlineText(false);// Disable the
                        // underline for
                        // hash Tags.
                    } else {
                        ds.setUnderlineText(true);// Enables the
                        // underline for
                        // hash Tags.

                    }
                }
            }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        }

        //for username

        spanText = nTagString.subSequence(0, len);

        final CharSequence mLastTextSpan = spanText;

        string.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {

                // Click on each tag will get here

                //  Log.d("TAg--HAsh", String.format("Clicked", mLastTextSpan));
                mTagClick.clickedTag(mLastTextSpan);
            }

            @Override
            public void updateDrawState(TextPaint ds) {

                // color for the hash tag
                ds.setColor(Color.parseColor(mhastTagColor));
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                if (mHypeLinkEnabled == 0) {
                    ds.setUnderlineText(false);// Disable the
                    // underline for
                    // hash Tags.
                } else {
                    ds.setUnderlineText(true);// Enables the
                    // underline for
                    // hash Tags.

                }
            }
        }, 0, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        return string;
    }


    public SpannableStringBuilder addClickablePart(String nTagString,
                                                   TagClick TagClick, int hypeLinkEnabled, String hastTagColor, int len, int len2) {

        this.mhastTagColor = hastTagColor;

        this.mHypeLinkEnabled = hypeLinkEnabled;

        this.mTagClick = TagClick;

        // Pattern for getting the hash tags from a string


        Pattern hashTagsPattern = Pattern.compile("(#[a-zA-Z0-9_-]+)");
        Pattern hashTagsPattern2 = Pattern.compile("(@[a-zA-Z0-9_-]+)");

        SpannableStringBuilder string = new SpannableStringBuilder(nTagString);

        CharSequence spanText;
        int start;
        int end;

        // Matching the pattern with the existing string

        Matcher m = hashTagsPattern.matcher(nTagString);

        while (m.find()) {

            start = m.start();
            end = m.end();

            spanText = nTagString.subSequence(start, end);


            final CharSequence mLastTextSpan = spanText;

            string.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    // Click on each tag will get here

                    //  Log.d("TAg--HAsh", String.format("Clicked", mLastTextSpan));
                    mTagClick.clickedTag(mLastTextSpan);
                }

                @Override
                public void updateDrawState(TextPaint ds) {

                    // color for the hash tag
                    ds.setColor(Color.parseColor(mhastTagColor));
                    ds.setTextSize(16);

                    if (mHypeLinkEnabled == 0) {
                        ds.setUnderlineText(false);// Disable the
                        // underline for
                        // hash Tags.
                    } else {
                        ds.setUnderlineText(true);// Enables the
                        // underline for
                        // hash Tags.

                    }
                }
            }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        }


        // Matching the pattern with the existing string

        Matcher m2 = hashTagsPattern2.matcher(nTagString);

        while (m2.find()) {

            start = m2.start();
            end = m2.end();

            spanText = nTagString.subSequence(start, end);


            final CharSequence mLastTextSpan = spanText;

            string.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    // Click on each tag will get here

                    Log.d("TAg--HAsh", String.format("Clicked", mLastTextSpan));
                    mTagClick.clickedTag(mLastTextSpan);
                }

                @Override
                public void updateDrawState(TextPaint ds) {

                    // color for the hash tag
                    ds.setColor(Color.parseColor(mhastTagColor));

                    if (mHypeLinkEnabled == 0) {
                        ds.setUnderlineText(false);// Disable the
                        // underline for
                        // hash Tags.
                    } else {
                        ds.setUnderlineText(true);// Enables the
                        // underline for
                        // hash Tags.

                    }
                }
            }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        }

        //for username

        spanText = nTagString.subSequence(0, len);

        final CharSequence mLastTextSpan = spanText;

        string.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {

                // Click on each tag will get here

                //  Log.d("TAg--HAsh", String.format("Clicked", mLastTextSpan));
                mTagClick.clickedTag(mLastTextSpan);
            }

            @Override
            public void updateDrawState(TextPaint ds) {

                // color for the hash tag
                ds.setColor(Color.parseColor(mhastTagColor));
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                if (mHypeLinkEnabled == 0) {
                    ds.setUnderlineText(false);// Disable the
                    // underline for
                    // hash Tags.
                } else {
                    ds.setUnderlineText(true);// Enables the
                    // underline for
                    // hash Tags.

                }
            }
        }, 0, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //for time


        String str = nTagString.substring(nTagString.length() - len2, nTagString.length());
        Log.e("time", str);

        string.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {

                // Click on each tag will get here

                //  Log.d("TAg--HAsh", String.format("Clicked", mLastTextSpan));
                // mTagClick.clickedTag(mLastTextSpan2);
            }

            @Override
            public void updateDrawState(TextPaint ds) {

                // color for the hash tag
                ds.setColor(Color.parseColor("#dadada"));
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                ds.setTextSize(16.0f);
                if (mHypeLinkEnabled == 0) {
                    ds.setUnderlineText(false);// Disable the
                    // underline for
                    // hash Tags.
                } else {
                    ds.setUnderlineText(true);// Enables the
                    // underline for
                    // hash Tags.

                }
            }
        }, nTagString.length() - len2, nTagString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        return string;
    }



    public SpannableStringBuilder addClickablePart(String nTagString,
                                                   TagClick TagClick, int hypeLinkEnabled, String hastTagColor,int len,int len2,String str,String typeofmore) {

        this.mhastTagColor = hastTagColor;

        this.mHypeLinkEnabled = hypeLinkEnabled;

        this.mTagClick = TagClick;

        // Pattern for getting the hash tags from a string


        SpannableStringBuilder string = new SpannableStringBuilder(nTagString);

        CharSequence spanText;

        //for username

        spanText = nTagString.subSequence(0,len);

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

                // color for the hash tag
                ds.setColor(Color.parseColor(mhastTagColor));
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                if (mHypeLinkEnabled == 0) {
                    ds.setUnderlineText(false);// Disable the
                    // underline for
                    // hash Tags.
                } else {
                    ds.setUnderlineText(true);// Enables the
                    // underline forng;
                    // hash Tags.

                }
            }
        }, 0,len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int pos=len+5;
        CharSequence  spanText2 = nTagString.substring(pos,pos+len2);
        CharSequence text="";
        if(nTagString.contains("more"))
             text =str+",more,"+typeofmore;

        else
                text =spanText2;

        final CharSequence mLastTextSpan2 =text;

        string.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {

                // Click on each tag will get here

                //  Log.d("TAg--HAsh", String.format("Clicked", mLastTextSpan));
                mTagClick.clickedTag(mLastTextSpan2);
            }

            @Override
            public void updateDrawState(TextPaint ds) {

                // color for the hash tag
                ds.setColor(Color.parseColor(mhastTagColor));
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                if (mHypeLinkEnabled == 0) {
                    ds.setUnderlineText(false);// Disable the
                    // underline for
                    // hash Tags.
                } else {
                    ds.setUnderlineText(true);// Enables the
                    // underline forng;
                    // hash Tags.

                }
            }
        }, pos,pos+len2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        return string;
    }

    public SpannableStringBuilder addClickablePart(String nTagString,
                                                   TagClick TagClick, int hypeLinkEnabled, String hastTagColor) {

        this.mhastTagColor = hastTagColor;

        this.mHypeLinkEnabled = hypeLinkEnabled;

        this.mTagClick = TagClick;

        // Pattern for getting the hash tags from a string


        Pattern hashTagsPattern = Pattern.compile("(#[a-zA-Z0-9_-]+)");
        Pattern hashTagsPattern2 = Pattern.compile("(@[a-zA-Z0-9_-]+)");

        SpannableStringBuilder string = new SpannableStringBuilder(nTagString);

        CharSequence spanText;
        int start;
        int end;

        // Matching the pattern with the existing string

        Matcher m = hashTagsPattern.matcher(nTagString);

        while (m.find()) {

            start = m.start();
            end = m.end();

            spanText = nTagString.subSequence(start, end);


            final CharSequence mLastTextSpan = spanText;

            string.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    // Click on each tag will get here

                    //  Log.d("TAg--HAsh", String.format("Clicked", mLastTextSpan));
                    mTagClick.clickedTag(mLastTextSpan);
                }

                @Override
                public void updateDrawState(TextPaint ds) {

                    // color for the hash tag
                    ds.setColor(Color.parseColor(mhastTagColor));

                    if (mHypeLinkEnabled == 0) {
                        ds.setUnderlineText(false);// Disable the
                        // underline for
                        // hash Tags.
                    } else {
                        ds.setUnderlineText(true);// Enables the
                        // underline for
                        // hash Tags.

                    }
                }
            }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        }


        // Matching the pattern with the existing string

        Matcher m2 = hashTagsPattern2.matcher(nTagString);

        while (m2.find()) {

            start = m2.start();
            end = m2.end();

            spanText = nTagString.subSequence(start, end);


            final CharSequence mLastTextSpan = spanText;

            string.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    // Click on each tag will get here

                    Log.d("TAg--HAsh", String.format("Clicked", mLastTextSpan));
                    mTagClick.clickedTag(mLastTextSpan);
                }

                @Override
                public void updateDrawState(TextPaint ds) {

                    // color for the hash tag
                    ds.setColor(Color.parseColor(mhastTagColor));

                    if (mHypeLinkEnabled == 0) {
                        ds.setUnderlineText(false);// Disable the
                        // underline for
                        // hash Tags.
                    } else {
                        ds.setUnderlineText(true);// Enables the
                        // underline for
                        // hash Tags.

                    }
                }
            }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        }

        //for username

        spanText = nTagString.subSequence(0, 12);

        final CharSequence mLastTextSpan = spanText;

        string.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {

                // Click on each tag will get here

                //  Log.d("TAg--HAsh", String.format("Clicked", mLastTextSpan));
                mTagClick.clickedTag(mLastTextSpan);
            }

            @Override
            public void updateDrawState(TextPaint ds) {

                // color for the hash tag
                ds.setColor(Color.parseColor(mhastTagColor));
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                if (mHypeLinkEnabled == 0) {
                    ds.setUnderlineText(false);// Disable the
                    // underline for
                    // hash Tags.
                } else {
                    ds.setUnderlineText(true);// Enables the
                    // underline for
                    // hash Tags.

                }
            }
        }, 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//for username
        int index=nTagString.indexOf("23");
        spanText = nTagString.subSequence(index,index+3);

        final CharSequence mLastTextSpan2 = spanText;

        string.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {

                // Click on each tag will get here

                //  Log.d("TAg--HAsh", String.format("Clicked", mLastTextSpan));
                mTagClick.clickedTag(mLastTextSpan2);
            }

            @Override
            public void updateDrawState(TextPaint ds) {

                // color for the hash tag
                ds.setColor(Color.parseColor("#dadada"));
                //ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                ds.setTextSize(19);
                if (mHypeLinkEnabled == 0) {
                    ds.setUnderlineText(false);// Disable the
                    // underline for
                    // hash Tags.
                } else {
                    ds.setUnderlineText(true);// Enables the
                    // underline for
                    // hash Tags.

                }
            }
        }, index,index+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return string;
    }

}