package com.eowise.recyclerview.stickyheaders.samples;


import android.graphics.Color;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by aurel on 22/09/14.
 */
public class LndCategoryActivity extends AppCompatActivity  {

      ImageView dress,handbags,shoes,jewellery;
      TextView dresstext,handbagstext,shoestext,jewellerytext,mainpage;
    boolean dressstatus=true,handbagsstatus=true,shoesstatus=true,jewellerystatus=true;
    private LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lnd_categories_page);
        //image view reference
        dress= (ImageView) findViewById(R.id.dress);
        handbags= (ImageView) findViewById(R.id.handbags);
        shoes= (ImageView) findViewById(R.id.shoes);
        jewellery= (ImageView) findViewById(R.id.jewellery);
       //textView reference
        dresstext= (TextView) findViewById(R.id.dresstext);
        handbagstext= (TextView) findViewById(R.id.handbagstext);
        shoestext= (TextView) findViewById(R.id.shoestext);
        jewellerytext= (TextView) findViewById(R.id.jewellerytext);


        //appyling custom fonts
        dresstext.setTypeface(SingleTon.robotoregular);
        handbagstext.setTypeface(SingleTon.robotoregular);
        shoestext.setTypeface(SingleTon.robotoregular);
        jewellerytext.setTypeface(SingleTon.robotoregular);
        mainpage.setTypeface(SingleTon.robotoregular);
//reference more
        ll= (LinearLayout) findViewById(R.id.ll);



        //setting up listeners
        dress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dressstatus)
                {
                    deSelect();
                    dress.setBackgroundResource(R.drawable.category_rounded_corner_selected);
                    dress.setImageResource(R.drawable.category_dress_selected);
                    dresstext.setTextColor(Color.parseColor("#be4d66"));
                    dressstatus=false;
                }
                else
                {
                    dress.setBackgroundResource(R.drawable.category_rounded_corner);
                    dress.setImageResource(R.drawable.category_dress);
                    dresstext.setTextColor(Color.parseColor("#f3f4f5"));
                    dressstatus=true;

                }
            }
        });
        handbags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(handbagsstatus)
                {
                    deSelect();
                    handbags.setBackgroundResource(R.drawable.category_rounded_corner_selected);
                    handbags.setImageResource(R.drawable.category_handbags_selected);
                    handbagstext.setTextColor(Color.parseColor("#be4d66"));
                    handbagsstatus=false;
                }
                else
                {
                    handbags.setBackgroundResource(R.drawable.category_rounded_corner);
                    handbags.setImageResource(R.drawable.category_handbags);
                    handbagstext.setTextColor(Color.parseColor("#f3f4f5"));
                    handbagsstatus=true;

                }
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shoesstatus)
                {
                    deSelect();
                    shoes.setBackgroundResource(R.drawable.category_rounded_corner_selected);
                    shoes.setImageResource(R.drawable.category_shoes_selected);
                    shoestext.setTextColor(Color.parseColor("#be4d66"));
                    shoesstatus=false;
                }
                else
                {
                    shoes.setBackgroundResource(R.drawable.category_rounded_corner);
                    shoes.setImageResource(R.drawable.category_shoes);
                    shoestext.setTextColor(Color.parseColor("#f3f4f5"));
                    shoesstatus=true;

                }
            }
        });
        jewellery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(jewellerystatus)
                {
                    deSelect();
                    jewellery.setBackgroundResource(R.drawable.category_rounded_corner_selected);
                    jewellery.setImageResource(R.drawable.category_jewellery_selected);
                    jewellerytext.setTextColor(Color.parseColor("#be4d66"));
                    jewellerystatus=false;
                }
                else
                {
                    jewellery.setBackgroundResource(R.drawable.category_rounded_corner);
                    jewellery.setImageResource(R.drawable.category_jewellery);
                    jewellerytext.setTextColor(Color.parseColor("#f3f4f5"));
                    jewellerystatus=true;

                }
            }
        });

        //device with and height
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        int width = displayMetrics.widthPixels;
        Toast.makeText(this,"width"+width,Toast.LENGTH_SHORT).show();
if(width<=480)
       adjust(30,200);
else if(width>480)
{
    adjust(40,330);
}
 else
{
    adjust(50,360);

}
    }
private void adjust(int margin,int imgheight)
{
   dress.getLayoutParams().height =imgheight;
    handbags.getLayoutParams().height =imgheight;
    shoes.getLayoutParams().height =imgheight;
    jewellery.getLayoutParams().height =imgheight;


//setting padding
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    params.addRule(RelativeLayout.CENTER_IN_PARENT);
    params.setMargins(margin, margin,margin,margin);
    ll.setLayoutParams(params);
}

    private void deSelect()
{
    mainpage.setBackgroundResource(R.drawable.category_buttonselected_corner);
    mainpage.setTextColor(Color.parseColor("#be4d66"));

    //for dress
    dress.setBackgroundResource(R.drawable.category_rounded_corner);
    dress.setImageResource(R.drawable.category_dress);
    dresstext.setTextColor(Color.parseColor("#f3f4f5"));
    dressstatus=true;
//for handbags
    handbags.setBackgroundResource(R.drawable.category_rounded_corner);
    handbags.setImageResource(R.drawable.category_handbags);
    handbagstext.setTextColor(Color.parseColor("#f3f4f5"));
    handbagsstatus=true;
//for shoes
    shoes.setBackgroundResource(R.drawable.category_rounded_corner);
    shoes.setImageResource(R.drawable.category_shoes);
    shoestext.setTextColor(Color.parseColor("#f3f4f5"));
    shoesstatus=true;
//for jewellery
    jewellery.setBackgroundResource(R.drawable.category_rounded_corner);
    jewellery.setImageResource(R.drawable.category_jewellery);
    jewellerytext.setTextColor(Color.parseColor("#f3f4f5"));
    jewellerystatus=true;

}
}
