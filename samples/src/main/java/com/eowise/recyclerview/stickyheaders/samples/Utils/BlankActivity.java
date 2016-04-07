package com.eowise.recyclerview.stickyheaders.samples.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.InstructionActivity;
import com.eowise.recyclerview.stickyheaders.samples.LndCustomCameraPost.CustomCamera;


public class BlankActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SingleTon.pref.getBoolean("next",false))
        {
            Intent cap=new Intent(this, CustomCamera.class);
            startActivity(cap);

        }
        else {
            Intent instruction = new Intent(this, InstructionActivity.class);
            startActivityForResult(instruction, 2);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
