package com.eowise.recyclerview.stickyheaders.samples;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.LndCustomCameraPost.CustomCamera;

public class InstructionActivity extends AppCompatActivity {

    private ImageView img1,img2;
    TextView heading,subheading1,subheading2;
    private CheckBox checkbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taking_effective_photo_instruction);

       //reference

        heading= (TextView) findViewById(R.id.heading);
        subheading1= (TextView) findViewById(R.id.subheading1);
        subheading2= (TextView) findViewById(R.id.subheading2);
        checkbox= (CheckBox) findViewById(R.id.checkbox);
//custom fonts
        Typeface tf=Typeface.createFromAsset(getAssets(),"arialbold.ttf");


      //appyling fonts
        heading.setTypeface(tf);
        subheading1.setTypeface(tf);
        subheading2.setTypeface(tf);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        int width = displayMetrics.widthPixels;

        if(width<=480)
        {
            subheading1.setTextSize(11.5f);
            subheading2.setTextSize(11.5f);

        }
        else if(width>480 && width<=720)
        {
            subheading1.setTextSize(12.5f);
            subheading2.setTextSize(12.5f);

        }
        else if(width>720 && width<=1280)
        {
            subheading1.setTextSize(13.5f);
            subheading2.setTextSize(13.5f);

        }
        else
        {
            subheading1.setTextSize(14.5f);
            subheading2.setTextSize(14.5f);


        }

    }
public void capture(View v)
{
    if(checkbox.isChecked()) {
        SharedPreferences.Editor edit = SingleTon.pref.edit();
        edit.putBoolean("next",true);
        edit.commit();
    }
        Intent cap=new Intent(InstructionActivity.this, CustomCamera.class);
        startActivity(cap);
        finish();
}


}
