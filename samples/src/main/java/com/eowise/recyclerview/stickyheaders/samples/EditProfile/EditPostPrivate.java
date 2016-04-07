package com.eowise.recyclerview.stickyheaders.samples.EditProfile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.LndCustomCameraPost.CompressImage;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;

import com.eowise.recyclerview.stickyheaders.samples.R;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class EditPostPrivate extends AppCompatActivity implements View.OnClickListener{
    TextView size[]=new TextView[8];
    TextView length1,length2,lengthall;
    TextView pickup,delivery;

    TextView color[]=new TextView[16];

    int col[]=new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    int sze[]=new int[]{0,0,0,0,0,0,0,0};
    int len1=0,len2=0,lenall=0;

    private TextView heading;
    @Bind(R.id.pricewas)EditText pricewas;
    @Bind(R.id.pricenow)EditText pricenow;
    @Bind(R.id.image1)ImageView image1;
    @Bind(R.id.image2)ImageView image2;
    @Bind(R.id.image3)ImageView image3;
    @Bind(R.id.image4)ImageView image4;
    @Bind(R.id.desc)EditText desc;
    String[] links=new String[]{"","","",""};
    String filename[]=new String[]{"","","",""};
    String[] sizelist = new String[] {"", "","","","","",""};
    String[] lengthlist=new String[]{"",""};
    String transportionlist="";
    String[] colorlist = new String[] {"", "","","","","","","","", "","","","","",""};
    public static int GALLERY_INTENT_CALLED[]=new int[]{100,200,300,400};
    public static int CAMERA_INTENT_CALLED[]=new int[]{1,2,3,4};
    private AlertDialog alert;
    private static final int CAMERA = 0;
    private String post_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_private);
        //intializing butter knife
        ButterKnife.bind(this);

        //setting image view height
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        int heightwidth = (displayMetrics.widthPixels)/4;

        image1.getLayoutParams().height=heightwidth;
        image2.getLayoutParams().height=heightwidth;
        image3.getLayoutParams().height=heightwidth;
        image4.getLayoutParams().height=heightwidth;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        heading= (TextView) findViewById(R.id.heading);
        heading.setTypeface(SingleTon.hfont);

//size
        size[0]= (TextView) findViewById(R.id.size1);
        size[1]= (TextView) findViewById(R.id.size2);
        size[2]= (TextView) findViewById(R.id.size3);
        size[3]= (TextView) findViewById(R.id.size4);
        size[4]= (TextView) findViewById(R.id.size5);
        size[5]= (TextView) findViewById(R.id.size6);
        size[6]= (TextView) findViewById(R.id.size7);
        size[7]= (TextView) findViewById(R.id.sizeall);
//settting listener for size
        //color listener
        for(int i=0;i<size.length;i++)
        {
            size[i].setOnClickListener(this);
        }

        //length
        length1= (TextView) findViewById(R.id.len1);
        length2= (TextView) findViewById(R.id.len2);
        lengthall= (TextView) findViewById(R.id.lenall);
        //settting listener
        length1.setOnClickListener(this);
        length2.setOnClickListener(this);
        lengthall.setOnClickListener(this);

        //business
        pickup= (TextView) findViewById(R.id.pickup);
        delivery= (TextView) findViewById(R.id.delivery);
        //settting listener
        pickup.setOnClickListener(this);
        delivery.setOnClickListener(this);

//color
        color[0]=(TextView)findViewById(R.id.color1);
        color[1]=(TextView)findViewById(R.id.color2);
        color[2]=(TextView)findViewById(R.id.color3);
        color[3]=(TextView) findViewById(R.id.color4);
        color[4]=(TextView) findViewById(R.id.color5);
        color[5]=(TextView) findViewById(R.id.color6);
        color[6]=(TextView) findViewById(R.id.color7);
        color[7]=(TextView) findViewById(R.id.color8);
        color[8]=(TextView) findViewById(R.id.color9);
        color[9]=(TextView) findViewById(R.id.color10);
        color[10]=(TextView) findViewById(R.id.color11);
        color[11]=(TextView) findViewById(R.id.color12);
        color[12]=(TextView) findViewById(R.id.color13);
        color[13]=(TextView) findViewById(R.id.color14);
        color[14]=(TextView) findViewById(R.id.color15);
        color[15]=(TextView) findViewById(R.id.color16);
        //color listener
        for(int i=0;i<color.length;i++)
        {
            color[i].setOnClickListener(this);
        }
//listener to image
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        image4.setOnClickListener(this);

        Bundle extra= getIntent().getExtras();
        if(extra!=null)
        {
            String data=extra.getString("data",null);
            Log.e("data",data);
            try {
                JSONObject jobj1=new JSONObject(data);

                JSONObject jobj2=jobj1.getJSONObject("data");
                //reading images
                JSONObject jimagesobj=jobj2.getJSONObject("images");

                SingleTon.imageLoader.displayImage(jimagesobj.getString("url1"),image1, SingleTon.options);
                SingleTon.imageLoader.displayImage(jimagesobj.getString("url2"),image2, SingleTon.options);
                SingleTon.imageLoader.displayImage(jimagesobj.getString("url3"),image3, SingleTon.options);
                SingleTon.imageLoader.displayImage(jimagesobj.getString("url4"),image4, SingleTon.options);
                links[0]=jimagesobj.getString("url1");
                links[1]=jimagesobj.getString("url2");
                links[2]=jimagesobj.getString("url3");
                links[3]=jimagesobj.getString("url4");

                //read old and new price
                JSONObject jpriceobj=jobj2.getJSONObject("price");

                pricenow.setText(jpriceobj.getString("price_now"));
                pricewas.setText(jpriceobj.getString("price_was"));

                //read length info
                JSONObject jlenobj=jobj2.getJSONObject("length");
                if(jlenobj.getString("lenshort").length()>0)
                {
                    length1.setBackgroundColor(Color.parseColor("#be4d66"));
                    len1=1;
                    lengthlist[0]="short";
                }
                if(jlenobj.getString("lenlong").length()>0)
                {
                    length2.setBackgroundColor(Color.parseColor("#be4d66"));
                    len2=1;
                    lengthlist[1]="long";
                }
//read business info
              /*  JSONObject jbusiobj=jobj2.getJSONObject("business");
                if(jbusiobj.getString("businessprivate").length()>0)
                {
                    business1.setBackgroundColor(Color.parseColor("#be4d66"));
                    biss1=1;
                    businesslist[0]="private";

                }
                if(jbusiobj.getString("businessshop").length()>0)
                {
                    business2.setBackgroundColor(Color.parseColor("#be4d66"));
                    biss2=1;
                    businesslist[1]="short";
                }
*/
                //read size
                JSONArray jsizeobj=jobj2.getJSONArray("size");
                for(int i=0;i<jsizeobj.length();i++)
                {
                    sizelist[i]=jsizeobj.getString(i);
                    if(jsizeobj.getString(i).length()>0)
                    {
                        size[i].setBackgroundColor(Color.parseColor("#be4d66"));

                        sze[i]=1;
                    }
                }

                //read color
                JSONArray jcolorobj=jobj2.getJSONArray("color");
                for(int i=0;i<jcolorobj.length();i++)
                {
                    colorlist[i]=jcolorobj.getString(i);
                    if(jcolorobj.getString(i).length()>0)
                    {
                        color[i].setBackgroundColor(Color.parseColor("#be4d66"));
                        col[i]=1;
                    }
                }

                //read basic info
                JSONObject jobj=jobj2.getJSONObject("basicinfo");

                desc.setText(jobj.getString("description"));
                post_id=jobj.getString("postid");
            }
            catch(Exception ex)
            {
                Log.e("json parsing error",ex.getMessage());
            }
        }
    }

    @Override
    public void onClick(View v)
    {



        switch(v.getId())
        {
            //events for size
            case R.id.size1:
                if(sze[0]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sze[0]=1;
                    sizelist[0]="2xs";

                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sze[0]=0;
                    sizelist[0]="";
                }
                break;
            case R.id.size2:
                if(sze[1]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sze[1]=1;
                    sizelist[1]="xs";

                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sze[1]=0;
                    sizelist[1]="";
                }
                break;
            case R.id.size3:
                if(sze[2]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sze[2]=1;
                    sizelist[2]="s";

                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sze[2]=0;
                    sizelist[2]="";
                }
                break;
            case R.id.size4:
                if(sze[3]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sze[3]=1;
                    sizelist[3]="m";

                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sze[3]=0;
                    sizelist[3]="";

                }
                break;
            case R.id.size5:
                if(sze[4]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sze[4]=1;
                    sizelist[4]="l";

                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sze[4]=0;
                    sizelist[4]="";

                }
                break;
            case R.id.size6:
                if(sze[5]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sze[5]=1;
                    sizelist[5]="xl";

                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sze[5]=0;
                    sizelist[5]="";
                }
                break;
            case R.id.size7:
                if(sze[6]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sze[6]=1;
                    sizelist[6]="2xl";

                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sze[6]=0;
                    sizelist[6]="";

                }
                break;
            case R.id.sizeall:
                if(sze[7]==0)
                {

                    for(int i=0;i<size.length-1;i++)
                        size[i].setBackgroundColor(Color.parseColor("#be4d66"));
                    sze[7]=1;
                    sizelist[0]="2xs";
                    sizelist[1]="xs";
                    sizelist[2]="s";
                    sizelist[3]="m";
                    sizelist[4]="l";
                    sizelist[5]="xl";
                    sizelist[6]="2xl";

                }
                else
                {
                    for(int i=0;i<size.length-1;i++)
                        size[i].setBackgroundColor(Color.parseColor("#1d1f21"));

                    sze[7]=0;
                    sizelist[0]="";
                    sizelist[1]="";
                    sizelist[2]="";
                    sizelist[3]="";
                    sizelist[4]="";
                    sizelist[5]="";
                    sizelist[6]="";


                }
                break;
            //for length events
            case R.id.len1:
                if(len1==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    len1=1;
                    lengthlist[0]="short";
                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    len1=0;
                    lengthlist[0]="";
                }
                break;
            case R.id.len2:
                if(len2==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    len2=1;
                    lengthlist[1]="long";

                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    len2=0;
                    lengthlist[0]="";

                }
                break;
            case R.id.lenall:
                if(lenall==0)
                {
                    length1.setBackgroundColor(Color.parseColor("#be4d66"));
                    length2.setBackgroundColor(Color.parseColor("#be4d66"));

                    lenall=1;
                    lengthlist[0]="short";
                    lengthlist[1]="long";

                }
                else
                {
                    length1.setBackgroundColor(Color.parseColor("#1d1f21"));
                    length2.setBackgroundColor(Color.parseColor("#1d1f21"));

                    lenall=0;
                    lengthlist[0]="";
                    lengthlist[1]="";

                }
                break;

            //for transportation events
            case R.id.pickup:

                pickup.setBackgroundColor(Color.parseColor("#be4d66"));
                delivery.setBackgroundColor(Color.parseColor("#1d1f21"));
                transportionlist="private";




                break;
            case R.id.delivery:

                delivery.setBackgroundColor(Color.parseColor("#be4d66"));
                pickup.setBackgroundColor(Color.parseColor("#1d1f21"));
                transportionlist="shop";


                break;

            //color events
            case R.id.color1:
                if(col[0]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[0]=1;
                    colorlist[0]="black";
                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[0]=0;
                    colorlist[0]="";
                }
                break;
            case R.id.color2:
                if(col[1]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[1]=1;
                    colorlist[1]="silver";

                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[1]=0;
                    colorlist[1]="";
                }
                break;
            case R.id.color3:
                if(col[2]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[2]=1;
                    colorlist[2]="orange";

                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[2]=0;
                    colorlist[2]="";
                }
                break;
            case R.id.color4:
                if(col[3]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[3]=1;
                    colorlist[3]="white";
                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[3]=0;
                    colorlist[3]="";
                }
            case R.id.color5:
                if(col[4]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[4]=1;
                    colorlist[4]="gold";
                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[4]=0;
                    colorlist[4]="";

                }
                break;
            case R.id.color6:
                if(col[5]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[5]=1;
                    colorlist[5]="brown";

                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[5]=0;
                    colorlist[5]="";

                }
                break;
            case R.id.color7:
                if(col[6]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[6]=1;
                    colorlist[6]="red";
                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[6]=0;
                    colorlist[6]="";

                }
                break;
            case R.id.color8:
                if(col[7]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[7]=1;
                    colorlist[7]="purple";

                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[7]=0;
                    colorlist[7]="";

                }
                break;
            case R.id.color9:
                if(col[8]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[8]=1;
                    colorlist[8]="nude";

                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[8]=0;
                    colorlist[8]="";


                }
                break;
            case R.id.color10:
                if(col[9]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[9]=1;
                    colorlist[9]="blue";

                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[9]=0;
                    colorlist[9]="";

                }
                break;
            case R.id.color11:
                if(col[10]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[10]=1;
                    colorlist[10]="yellow";

                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[10]=0;
                    colorlist[10]="";

                }
                break;
            case R.id.color12:
                if(col[11]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[11]=1;
                    colorlist[11]="gray";

                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[11]=0;
                    colorlist[11]="";

                }
                break;
            case R.id.color13:
                if(col[12]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[12]=1;
                    colorlist[12]="green";

                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[12]=0;
                    colorlist[12]="";

                }
                break;
            case R.id.color14:
                if(col[13]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[13]=1;
                    colorlist[13]="pink";

                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[13]=0;
                    colorlist[13]="";

                }
                break;
            case R.id.color15:
                if(col[14]==0)
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[14]=1;
                    colorlist[14]="pattern";

                }
                else
                {
                    ((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[14]=0;
                    colorlist[14]="";

                }
                break;

            case R.id.color16:
                if(col[15]==0)
                {
                    for(int i=0;i<col.length-1;i++)
                        color[i].setBackgroundColor(Color.parseColor("#be4d66"));
                    col[15]=1;
                    colorlist[0]="black";
                    colorlist[1]="silver";
                    colorlist[2]="orange";
                    colorlist[3]="white";
                    colorlist[4]="gold";
                    colorlist[5]="brown";
                    colorlist[6]="red";
                    colorlist[7]="purple";
                    colorlist[8]="nude";
                    colorlist[9]="blue";
                    colorlist[10]="yellow";
                    colorlist[11]="gray";
                    colorlist[12]="green";
                    colorlist[13]="pink";
                    colorlist[14]="pattern";

                }
                else
                {
                    for(int i=0;i<col.length-1;i++)
                        color[i].setBackgroundColor(Color.parseColor("#1d1f21"));

                    col[15]=0;
                    colorlist[0]="";
                    colorlist[1]="";
                    colorlist[2]="";
                    colorlist[3]="";
                    colorlist[4]="";
                    colorlist[5]="";
                    colorlist[6]="";
                    colorlist[7]="";
                    colorlist[8]="";
                    colorlist[9]="";
                    colorlist[10]="";
                    colorlist[11]="";
                    colorlist[12]="";
                    colorlist[13]="";
                    colorlist[14]="";

                }
                break;



            case R.id.image1:
                showImageDialog(0);
                break;
            case R.id.image2:
                showImageDialog(1);
                break;
            case R.id.image3:
                showImageDialog(2);
                break;
            case R.id.image4:
                showImageDialog(3);
                break;

        }


    }
    public void save(View v)
    {
        boolean size=true,len=true,business=true,color=true;

        //to check atleast one size selected
        for(int i=0;i<sizelist.length;i++)
        {
            if(sizelist[i].length()>0)
            {
                size=false;
                break;
            }
        }

        //to check atleast one length selected
        for(int i=0;i<lengthlist.length;i++)
        {
            if(lengthlist[i].length()>0)
            {
                len=false;
                break;
            }
        }
//to check atleast one business selected

        if(transportionlist.length()>0)
        {
            business=false;


        }
//to check atleast one color selected
        for(int i=0;i<colorlist.length;i++)
        {
            if(colorlist[i].length()>0)
            {
                color=false;
                break;
            }
        }
        if(desc.getText().length()==0)
        {
            Toast.makeText(this, "description field is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(pricenow.getText().length()==0)
        {
            Toast.makeText(this,"pricenow field is empty",Toast.LENGTH_SHORT).show();
            return;
        }

        else if(pricewas.getText().length()==0)
        {
            Toast.makeText(this,"pricewas field is empty",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(size)
        {
            Toast.makeText(this,"select  size",Toast.LENGTH_SHORT).show();
            return;
        }
        else  if(len)
        {
            Toast.makeText(this,"select length",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(business)
        {
            Toast.makeText(this,"select business type",Toast.LENGTH_SHORT).show();
            return;
        }
        else  if(color)
        {
            Toast.makeText(this,"select color",Toast.LENGTH_SHORT).show();
            return;
        }



        JSONArray jsArray = new JSONArray(Arrays.asList(sizelist));
        JSONArray jsArray2 = new JSONArray(Arrays.asList(lengthlist));

        JSONArray jsArray4 = new JSONArray(Arrays.asList(colorlist));

        Log.e("size", jsArray.toString());
        Log.e("length", jsArray2.toString());
        Log.e("transportation",transportionlist);
        Log.e("color", jsArray4.toString());
        for(int i=0;i<links.length;i++)
            Log.e("links",links[i]);
        // uploadImage(desc.getText().toString(),pricewas.getText().toString(),pricenow.getText().toString(),jsArray.toString(),jsArray2.toString(),jsArray3.toString(),jsArray4.toString());
        try {
            //image1 json
            JSONObject image1 = new JSONObject();
            if(filename[0].length()>0) {
                image1.put("imagename", filename[0]);
                image1.put("imageurl", links[0]);
            }
            else
            {
                image1.put("imagename", filename[0]);
                image1.put("imageurl","");
            }
            //image2 json
            JSONObject image2 = new JSONObject();
            if(filename[1].length()>0) {
                image2.put("imagename", filename[1]);
                image2.put("imageurl", links[1]);
            }
            else
            {
                image2.put("imagename", filename[1]);
                image2.put("imageurl","");
            }
            //image3 json
            JSONObject image3 = new JSONObject();
            if(filename[2].length()>0) {
                image3.put("imagename", filename[2]);
                image3.put("imageurl", links[2]);
            }
            else
            {
                image3.put("imagename", filename[2]);
                image3.put("imageurl","");
            }
            //image4 json
            JSONObject image4 = new JSONObject();
            if(filename[3].length()>0) {
                image4.put("imagename", filename[3]);
                image4.put("imageurl", links[3]);
            }
            else
            {
                image4.put("imagename", filename[3]);
                image4.put("imageurl","");
            }

            //images array
            JSONArray imagesarray = new JSONArray();
            imagesarray.put(image1);
            imagesarray.put(image2);
            imagesarray.put(image3);
            imagesarray.put(image4);


            String uname= SingleTon.pref.getString("uname","user");
            String profilepic= SingleTon.pref.getString("imageurl","http://");

            JSONObject mainObj = new JSONObject();
            mainObj.put("post_id",post_id);
            mainObj.put("uname",uname);
            mainObj.put("profile_pic",profilepic);

            mainObj.put("size",jsArray );
            mainObj.put("length",jsArray2 );
            mainObj.put("transportation",transportionlist );
            mainObj.put("color",jsArray4 );
            mainObj.put("images",imagesarray );
            mainObj.put("description",desc.getText().toString());
            mainObj.put("pricenow",pricenow.getText().toString());
            mainObj.put("pricewas", pricewas.getText().toString());
            saveChanges(mainObj.toString());

            Log.e("json", mainObj.toString());

        }
        catch(Exception ex)
        {
            Log.e("error",ex.getMessage());
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void back(View v)
    {
        onBackPressed();
    }
    public void saveChanges(final String data) {


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("wait updating dress...");
        pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://52.76.68.122/lnd/edshoppost.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                Log.e("responsedata", response.toString());

                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    if(jobj.getBoolean("status")) {
                        Toast.makeText(EditPostPrivate.this, jobj.getString("message"), Toast.LENGTH_LONG).show();
                        for(int i=0;i<4;i++) {
                            MemoryCacheUtils.removeFromCache("http://52.76.68.122/lnd/photos/" + filename[i], SingleTon.imageLoader.getMemoryCache());
                            DiskCacheUtils.removeFromCache("http://52.76.68.122/lnd/photos/"+filename[i], SingleTon.imageLoader.getDiskCache());
                        }
                    }
                    else
                        Toast.makeText(EditPostPrivate.this,jobj.getString("message"),Toast.LENGTH_LONG).show();


                }
                catch(Exception ex)
                {
                    Log.e("json parsing error",ex.getMessage());
                }

                //  startActivity(home);
                //finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Log.e("response error",error.getMessage()+"");
                if(error.getMessage()==null)
                    Toast.makeText(EditPostPrivate.this,"Dress not posted please try again",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("data",data);



                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return super.getBody();
            }
        };
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);

        queue.add(sr);
    }

    private void showImageDialog(final int val)
    {    AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        LayoutInflater inflater= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(R.layout.alertdialog_layout,null);
        // dialog.setTitle("Email Verification");
        //dialog .setMessage("A verfication mail has been sent to your registered email id.Please verify first to continue");
        dialog.setView(v);
        ((TextView)v.findViewById(R.id.gallery)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent,GALLERY_INTENT_CALLED[val]);



            }
        });
        ((TextView)v.findViewById(R.id.camera)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
               /* Intent intent2 = new Intent(EditPostPrivate.this, CustomCameraActivity.class);
                intent2.putExtra(FILENAME, "actress2.jpg");
                intent2.putExtra(IS_PORTRAIT, true);
                intent2.putExtra(QUALITY, 100);

                startActivityForResult(intent2, CAMERA_INTENT_CALLED[val]);*/

            }
        });


        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.dismiss();

            }
        });

        alert=dialog.create();
        alert.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //   super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_INTENT_CALLED[0]||requestCode == CAMERA_INTENT_CALLED[1]||requestCode == CAMERA_INTENT_CALLED[2]||requestCode == CAMERA_INTENT_CALLED[3]) {
            // If image available
            if (resultCode == Activity.RESULT_OK) {
                try {

                   // Log.e("url", data.getExtras().getString(IMAGE_URI));
                    String path = data.getExtras().getString("url");

                    // bitmap = Bitmap.createScaledBitmap(bitmap,40);
                    Bitmap selectedImage = CompressImage.compressImage(path);
                    if(requestCode==CAMERA_INTENT_CALLED[0])
                    {
                        image1.setImageBitmap(selectedImage);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        // Must compress the Image to reduce image size to make upload easy
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byte_arr = stream.toByteArray();
                        // Encode Image to String
                        if(links[0].length()>0) {

                            filename[0] = links[0].substring(links[0].lastIndexOf("/") + 1);


                        }
                        links[0] = Base64.encodeToString(byte_arr, 0);
                    }
                    else if(requestCode==CAMERA_INTENT_CALLED[1])
                    {
                        image2.setImageBitmap(selectedImage);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        // Must compress the Image to reduce image size to make upload easy
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byte_arr = stream.toByteArray();
                        // Encode Image to String
                        if(links[1].length()>0) {

                            filename[1] = links[1].substring(links[1].lastIndexOf("/") + 1);


                        }
                        links[1] = Base64.encodeToString(byte_arr, 0);

                    }
                    else if(requestCode==CAMERA_INTENT_CALLED[2])
                    {
                        image3.setImageBitmap(selectedImage);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        // Must compress the Image to reduce image size to make upload easy
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byte_arr = stream.toByteArray();
                        // Encode Image to String
                        if(links[2].length()>0) {

                            filename[2] = links[2].substring(links[2].lastIndexOf("/") + 1);


                        }
                        links[2] = Base64.encodeToString(byte_arr, 0);
                    }
                    else if(requestCode==CAMERA_INTENT_CALLED[3])
                    {
                        image4.setImageBitmap(selectedImage);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        // Must compress the Image to reduce image size to make upload easy
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byte_arr = stream.toByteArray();
                        // Encode Image to String
                        if(links[3].length()>0) {

                            filename[3] = links[3].substring(links[3].lastIndexOf("/") + 1);


                        }
                        links[3] = Base64.encodeToString(byte_arr, 0);
                    }


                } catch (Exception ex) {

                }
            }
        }
        if(requestCode==GALLERY_INTENT_CALLED[0]||requestCode==GALLERY_INTENT_CALLED[1]||requestCode==GALLERY_INTENT_CALLED[2]||requestCode==GALLERY_INTENT_CALLED[3]&&resultCode==RESULT_OK)
        {
            try {
                // Get the Image from data

                Uri selectedImageuri = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImageuri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                Bitmap selectedImage = CompressImage.compressImage(imgDecodableString);//BitmapFactory.decodeStream(imageStream);
                if(requestCode==GALLERY_INTENT_CALLED[0])
                {
                    image1.setImageBitmap(selectedImage);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // Must compress the Image to reduce image size to make upload easy
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byte_arr = stream.toByteArray();
                    // Encode Image to String
                    if(links[0].length()>0) {

                        filename[0] = links[0].substring(links[0].lastIndexOf("/") + 1);


                    }
                    links[0] = Base64.encodeToString(byte_arr, 0);
                }
                else if(requestCode==GALLERY_INTENT_CALLED[1])
                {
                    image2.setImageBitmap(selectedImage);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // Must compress the Image to reduce image size to make upload easy
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byte_arr = stream.toByteArray();
                    // Encode Image to String
                    if(links[1].length()>0) {

                        filename[1] = links[1].substring(links[1].lastIndexOf("/") + 1);


                    }
                    links[1] = Base64.encodeToString(byte_arr, 0);

                }
                else if(requestCode==GALLERY_INTENT_CALLED[2])
                {
                    image3.setImageBitmap(selectedImage);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // Must compress the Image to reduce image size to make upload easy
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byte_arr = stream.toByteArray();
                    // Encode Image to String
                    if(links[2].length()>0) {

                        filename[2] = links[2].substring(links[2].lastIndexOf("/") + 1);


                    }
                    links[2] = Base64.encodeToString(byte_arr, 0);
                }
                else if(requestCode==GALLERY_INTENT_CALLED[3])
                {
                    image4.setImageBitmap(selectedImage);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // Must compress the Image to reduce image size to make upload easy
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byte_arr = stream.toByteArray();
                    // Encode Image to String
                    if(links[3].length()>0) {

                        filename[3] = links[3].substring(links[3].lastIndexOf("/") + 1);


                    }
                    links[3] = Base64.encodeToString(byte_arr, 0);
                }


                /*filename.add("lnd"+System.currentTimeMillis()+".jpg");

                if(pos==0)
                {   imageurl.add(imgDecodableString);
                    mainimg.setImageBitmap(selectedImage);
                    pos++;
                }
                else if(pos== 1) {
                    imageurl.add(imgDecodableString);
                    BitmapDrawable ob = new BitmapDrawable(getResources(),selectedImage);
                    image1.setBackground(ob);

                    pos++;
                }
                else if(pos== 2) {
                    imageurl.add(imgDecodableString);
                    BitmapDrawable ob = new BitmapDrawable(getResources(),selectedImage);
                    image2.setBackground(ob);
                    pos++;
                }

                else if(pos==3)
                {
                    imageurl.add(imgDecodableString);
                    //   BitmapDrawable ob = new BitmapDrawable(getResources(),selectedImage);
                    image3.setImageBitmap(selectedImage);
                    Toast.makeText(CaptureActivity.this,"called",Toast.LENGTH_SHORT).show();
                }
*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == CAMERA) {
            // If image available
            if (resultCode == Activity.RESULT_OK) {
                try {

                    //Log.e("url",data.getExtras().getString(IMAGE_URI));
                    String path=data.getExtras().getString("url");

                    // bitmap = Bitmap.createScaledBitmap(bitmap,40);
                    Bitmap selectedImage=CompressImage.compressImage(path);
                   /* filename.add("lnd"+System.currentTimeMillis()+".jpg");

                    if(pos==0)
                    {    imageurl.add(path);
                        mainimg.setImageBitmap(selectedImage);
                        pos++;
                    }
                    else if(pos== 1) {
                        imageurl.add(path);
                        BitmapDrawable ob = new BitmapDrawable(getResources(),selectedImage);
                        image1.setBackground(ob);

                        pos++;
                    }
                    else if(pos== 2) {
                        imageurl.add(path);
                        BitmapDrawable ob = new BitmapDrawable(getResources(),selectedImage);
                        image2.setBackground(ob);
                        pos++;
                    }

                    else if(pos==3)
                    {
                        imageurl.add(path);
                        BitmapDrawable ob = new BitmapDrawable(getResources(),selectedImage);
                        image3.setBackground(ob);
                        Log.e("pos",pos+"");
                    }
*/
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this,"Error capturing image.",Toast.LENGTH_SHORT).show();
                }
            }

            // If cancelled
            else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this,"lndCamera cancelled.",Toast.LENGTH_SHORT).show();
            }

            // If something else
            else {
                Toast.makeText(this,"Did not complete!",Toast.LENGTH_LONG).show();
            }
        }
    }

}
