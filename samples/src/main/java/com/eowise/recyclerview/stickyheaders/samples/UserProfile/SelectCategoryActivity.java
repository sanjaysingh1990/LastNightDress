package com.eowise.recyclerview.stickyheaders.samples.UserProfile;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.PostDataShop.DressPost;
import com.eowise.recyclerview.stickyheaders.samples.PostDataShop.HandBagsPost;
import com.eowise.recyclerview.stickyheaders.samples.PostDataShop.JewelleryPost;
import com.eowise.recyclerview.stickyheaders.samples.PostDataShop.ShoesPost;
import com.eowise.recyclerview.stickyheaders.samples.PrivatePost.DressPostPrivate;
import com.eowise.recyclerview.stickyheaders.samples.PrivatePost.HandBagsPostPrivate;
import com.eowise.recyclerview.stickyheaders.samples.PrivatePost.JewelleryPostPrivate;
import com.eowise.recyclerview.stickyheaders.samples.PrivatePost.ShoesPostPrivate;
import com.eowise.recyclerview.stickyheaders.samples.R;


import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectCategoryActivity extends AppCompatActivity {
    @Bind(R.id.dress)
    ImageView dress;
    @Bind(R.id.handbags)
    ImageView handbags;
    @Bind(R.id.shoes)
    ImageView shoes;
    @Bind(R.id.jewellery)
    ImageView jewellery;
    @Bind(R.id.ll)
    LinearLayout ll;


    @Bind(R.id.dresstext)
    TextView dresstext;
    @Bind(R.id.handbagstext)
    TextView handbagstext;
    @Bind(R.id.shoestext)
    TextView shoestext;
    @Bind(R.id.jewellerytext)
    TextView jewellerytext;
    @Bind(R.id.post)
    TextView posttext;
    @Bind(R.id.category)
    TextView choosecateory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lnd_categories_page);
        ButterKnife.bind(this);

        showChoice();
    }

    private void showChoice() {


        posttext.setVisibility(View.GONE);
        //cancel image events

        //appyling custom fonts
        dresstext.setTypeface(ImageLoaderImage.robotoregular);
        handbagstext.setTypeface(ImageLoaderImage.robotoregular);
        shoestext.setTypeface(ImageLoaderImage.robotoregular);
        jewellerytext.setTypeface(ImageLoaderImage.robotoregular);
        choosecateory.setTypeface(ImageLoaderImage.robotoregular);


        //device with and height
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        int width = displayMetrics.widthPixels;
        //	Toast.makeText(getActivity(), "width" + width, Toast.LENGTH_SHORT).show();
        if (width <= 480)
            adjust(30, 170, 190);
        else if (width > 480 && width <= 550) {
            adjust(40, 190, 210);
        } else if (width > 480 && width <= 720) {
            adjust(40, 220, 240);
        } else if (width > 720 && width <= 1280) {
            adjust(40, 320, 340);
        } else {
            adjust(45, 350, 370);


        }

        setupListener();

    }

    private void adjust(int margin, int imgheight, int imgwidth) {
        dress.getLayoutParams().height = imgheight;
        dress.getLayoutParams().width = imgwidth;
        handbags.getLayoutParams().height = imgheight;
        handbags.getLayoutParams().width = imgwidth;
        shoes.getLayoutParams().height = imgheight;
        shoes.getLayoutParams().width = imgwidth;
        jewellery.getLayoutParams().height = imgheight;
        jewellery.getLayoutParams().width = imgwidth;

//setting padding
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(margin, 0, margin, 0);
        ll.setLayoutParams(params);
    }

    private void setupListener() {
        //setting up listeners
        posttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String utype = ImageLoaderImage.pref.getString("utype", "");
                Log.e("utype", utype + "");
                if (posttext.getText().toString().compareToIgnoreCase("post dress") == 0) {
                    if (utype.compareToIgnoreCase("shop") == 0) {
                        Intent intent = new Intent(SelectCategoryActivity.this, DressPost.class);

                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SelectCategoryActivity.this, DressPostPrivate.class);

                        startActivity(intent);

                    }

                } else if (posttext.getText().toString().compareToIgnoreCase("post handbags") == 0) {
                    if (utype.compareToIgnoreCase("shop") == 0) {

                        Intent intent = new Intent(SelectCategoryActivity.this, HandBagsPost.class);

                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SelectCategoryActivity.this, HandBagsPostPrivate.class);

                        startActivity(intent);

                   }
                } else if (posttext.getText().toString().compareToIgnoreCase("post shoes") == 0) {
                    if (utype.compareToIgnoreCase("shop") == 0) {

                        Intent intent = new Intent(SelectCategoryActivity.this, ShoesPost.class);

                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SelectCategoryActivity.this, ShoesPostPrivate.class);

                        startActivity(intent);

                    }
                } else if (posttext.getText().toString().compareToIgnoreCase("post jewellery") == 0) {
                    if (utype.compareToIgnoreCase("shop") == 0) {

                        Intent intent = new Intent(SelectCategoryActivity.this, JewelleryPost.class);

                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SelectCategoryActivity.this, JewelleryPostPrivate.class);
                        startActivity(intent);

                   }
                }

                finish();
            }
        });
        dress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                deSelect("dress", (ImageView) v);


            }
        });
        handbags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deSelect("handbags", (ImageView) v);


            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deSelect("shoes", (ImageView) v);


            }
        });
        jewellery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deSelect("jewellery", (ImageView) v);

            }
        });

    }

    private void deSelect(String cate, ImageView category) {

        posttext.setVisibility(View.VISIBLE);
        //saving

        posttext.setBackgroundResource(R.drawable.category_buttonselected_corner);
        posttext.setTextColor(Color.parseColor("#be4d66"));
        //for dress
        dress.setBackgroundResource(R.drawable.category_rounded_corner);
        dress.setImageResource(R.drawable.category_dress);
        dresstext.setTextColor(Color.parseColor("#828282"));
//for handbags
        handbags.setBackgroundResource(R.drawable.category_rounded_corner);
        handbags.setImageResource(R.drawable.category_handbags);
        handbagstext.setTextColor(Color.parseColor("#828282"));
//for shoes
        shoes.setBackgroundResource(R.drawable.category_rounded_corner);
        shoes.setImageResource(R.drawable.category_shoes);
        shoestext.setTextColor(Color.parseColor("#828282"));
//for jewellery
        jewellery.setBackgroundResource(R.drawable.category_rounded_corner);
        jewellery.setImageResource(R.drawable.category_jewellery);
        jewellerytext.setTextColor(Color.parseColor("#828282"));

        if (cate.compareTo("dress") == 0) {
            posttext.setText("POST DRESS");
            dress.setBackgroundResource(R.drawable.category_rounded_corner_selected);
            dress.setImageResource(R.drawable.category_dress_selected);
            dresstext.setTextColor(Color.parseColor("#be4d66"));

        } else if (cate.compareTo("handbags") == 0) {
            posttext.setText("POST HANDBAGS");
            handbags.setBackgroundResource(R.drawable.category_rounded_corner_selected);
            handbags.setImageResource(R.drawable.category_handbags_selected);
            handbagstext.setTextColor(Color.parseColor("#be4d66"));

        } else if (cate.compareTo("shoes") == 0) {
            posttext.setText("POST SHOES");
            shoes.setBackgroundResource(R.drawable.category_rounded_corner_selected);
            shoes.setImageResource(R.drawable.category_shoes_selected);
            shoestext.setTextColor(Color.parseColor("#be4d66"));

        } else {
            posttext.setText("POST JEWELLERY");
            jewellery.setBackgroundResource(R.drawable.category_rounded_corner_selected);
            jewellery.setImageResource(R.drawable.category_jewellery_selected);
            jewellerytext.setTextColor(Color.parseColor("#be4d66"));

        }

    }
}