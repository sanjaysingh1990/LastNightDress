package com.eowise.recyclerview.stickyheaders.samples;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.TagSelectingTextview;
import com.eowise.recyclerview.stickyheaders.samples.interfaces.TagClick;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import github.ankushsachdeva.emojicon.EmojiconTextView;

public class Demo extends AppCompatActivity implements TagClick {
    TagSelectingTextview mTagSelectingTextview;
    public static int hashTagHyperLinkEnabled = 1;
    public static int hashTagHyperLinkDisabled = 0;
    String hastTagColorBlue = "#22cff1";
    public static final String textcolor="#22cff1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hockey_profile);
      /*  setContentView(R.layout.hockey_home_demo);
        mTagSelectingTextview = new TagSelectingTextview();
        TextView heading= (TextView) findViewById(R.id.heading);
        TextView text1 = (EmojiconTextView) findViewById(R.id.desc);
        TextView uname = (TextView) findViewById(R.id.uname);
        uname.setTypeface(SingleTon.unamefont);

        TextView text2 = (TextView) findViewById(R.id.noti2);
        TextView text3 = (TextView) findViewById(R.id.noti3);
        change(text1, "Alanna Miles", "”.", "23m");
        change(text2, "Bianca Watson", "purchased your item, ship it now!", "1d");
        change(text3,"Alexia Mendoza","commented on your post: I really like this, I wish it was my size   .","2w");
        text1.setText(addClickablePart("Luke Miles I hate to see these skates go, I’ve only worn it once and it’s in perfect condition. These #Nike skates are perfect for anyone who has flat feet.                   ",
                this, hashTagHyperLinkDisabled, hastTagColorBlue,10),
                TextView.BufferType.SPANNABLE);*/

    }

    private void change(TextView txt, String name, String message, String time) {

        Spannable word = new SpannableString(name + " ");

        word.setSpan(new ForegroundColorSpan(Color.parseColor(textcolor)), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        word.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txt.setText(word);
        Spannable wordTwo = new SpannableString(message + " ");

        wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txt.append(wordTwo);

        Spannable wordThree = new SpannableString(time + " ");

        wordThree.setSpan(new ForegroundColorSpan(Color.parseColor("#dadada")), 0, wordThree.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordThree.setSpan(new RelativeSizeSpan(0.8f), 0,wordThree.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txt.append(wordThree);

    }

    @Override
    public void clickedTag(CharSequence tag) {

    }

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
                        ds.setTextSize(15);
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

            Drawable image = getResources().getDrawable(R.drawable.emoji_26c4);
            image.setBounds(0, 0, 32, 32
            );

            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            string.setSpan(imageSpan, nTagString.length()-15, nTagString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            Drawable image1 = getResources().getDrawable(R.drawable.emoji_2744);
            image1.setBounds(0, 0, 35, 35);

            ImageSpan imageSpan1 = new ImageSpan(image1, ImageSpan.ALIGN_BOTTOM);
            string.setSpan(imageSpan1, nTagString.length()-10, nTagString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return string;
        }
}
