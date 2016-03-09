package com.eowise.recyclerview.stickyheaders.samples.PostDataShop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.LndCustomCameraPost.CameraReviewFragment;
import com.eowise.recyclerview.stickyheaders.samples.LndCustomCameraPost.CompressImage;
import com.eowise.recyclerview.stickyheaders.samples.Paypal.PayPalAccountCreation;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.data.CameraData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

public class DressPost extends AppCompatActivity implements View.OnClickListener {
    @Bind({R.id.size1, R.id.size2, R.id.size3, R.id.size4, R.id.size5, R.id.size6, R.id.size7, R.id.numsize1, R.id.numsize2, R.id.numsize3, R.id.numsize4, R.id.numsize5, R.id.numsize6, R.id.numsize7, R.id.numsize8, R.id.numsize9, R.id.numsize10, R.id.numsize11, R.id.numsize12})
    List<TextView> dresssize;
    @Bind(R.id.dresstype1)
    TextView dresstype1;
    @Bind(R.id.dresstype2)
    TextView dresstype2;
    @Bind(R.id.dresstype3)
    TextView dresstype3;
    @Bind(R.id.dresstype4)
    TextView dresstype4;


    String[] sizetypelist1 = new String[]{"", "", "", "", "", "", ""};
    String[] sizetypelist2 = new String[]{"", "", "", "", "", "", "", "", "", "", "", ""};

    int dresstype = 0;

    int condition = 0;
    String[] colorlist = new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

    //for dress condition
    @Bind(R.id.lastnightdress)
    TextView lastnightdress;
    @Bind(R.id.conditionspinner)
    Spinner conditionspinner;
    @Bind(R.id.conditionnew)
    TextView conditionnew;


    @Bind({R.id.color1, R.id.color2, R.id.color3, R.id.color4, R.id.color5, R.id.color6, R.id.color7, R.id.color8, R.id.color9, R.id.color10, R.id.color11, R.id.color12, R.id.color13, R.id.color14, R.id.color15})
    List<TextView> color;


    int col[] = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    int sizetype1[] = new int[]{0, 0, 0, 0, 0, 0, 0};
    int sizetype2[] = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    int dress1 = 0, dress2 = 0, dress3 = 0, dress4 = 0;
    int condi1 = 0, condi2 = 0;

    private TextView heading;
    @Bind({R.id.image1, R.id.image2, R.id.image3, R.id.image4})
    List<ImageView> images;

    @Bind(R.id.pricewas)
    EditText pricewas;
    @Bind(R.id.pricenow)
    EditText pricenow;
    @Bind(R.id.desc)
    EmojiconEditText desc;
    @Bind(R.id.brand)
    EditText brand;
    @Bind(R.id.infoview)
    LinearLayout inforview;
    @Bind(R.id.emoji_btn)
    ImageButton emojiButton;
    @Bind(R.id.earnings)
    TextView earnings;
    @Bind(R.id.rootview)
    View rootView;
    String[] links = {"", "", "", ""};
    ArrayList<String> filename = new ArrayList<>();
    PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dress_post_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        int heightwidth = (displayMetrics.widthPixels) / 4;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(heightwidth - 8, heightwidth - 8);
        images.get(0).setLayoutParams(layoutParams);
        images.get(1).setLayoutParams(layoutParams);
        images.get(2).setLayoutParams(layoutParams);
        images.get(3).setLayoutParams(layoutParams);


        heading = (TextView) findViewById(R.id.heading);
        heading.setTypeface(ImageLoaderImage.hfont);


        //size listener
        for (int i = 0; i < dresssize.size(); i++) {
            dresssize.get(i).setOnClickListener(this);
        }


        //dress type listener
        dresstype1.setOnClickListener(this);
        dresstype2.setOnClickListener(this);
        dresstype3.setOnClickListener(this);
        dresstype4.setOnClickListener(this);




        //color listener
        for (int i = 0; i < color.size(); i++) {
            color.get(i).setOnClickListener(this);
        }
//condtion  events
        conditionnew.setOnClickListener(this);

        lastnightdress.setOnClickListener(this);


        //condition spinner selection events
        conditionspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if (pos > 0) {


                    ((TextView) parent.getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);

                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#be4d66"));
                    condition = pos;
                    condition();
                } else {
                    ((TextView) parent.getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                    condition = pos;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        for (int i = 0; i < 4; i++)
            filename.add("");
        //text change listener
        pricenow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int value = Integer.parseInt(editable.toString());
                    earnings.setText("Your earnings: C$" + (value - (value * 20) / 100));
                    inforview.setVisibility(View.VISIBLE);

                } catch (Exception ex) {
                    inforview.setVisibility(View.GONE);

                }
            }
        });


        setupEmoji();


    }

    @Override
    protected void onResume() {
        super.onResume();


        for (Map.Entry<String, CameraData> entry : CameraReviewFragment.urls.entrySet()) {
            //    System.out.println(entry.getKey());
            CameraData cd = entry.getValue();
            int value = Integer.parseInt(entry.getKey()) - 1;
            filename.add(value, cd.getFilename());
            new AsyncTaskLoadImage(images.get(value), value).execute(cd.getImageurl());


        }

    }

    public void back(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {


        int pw = 0, pn = 0;
        try {
            pn = Integer.parseInt(pricenow.getText().toString());

           } catch (Exception ex) {
        }

        try {

            pw = Integer.parseInt(pricewas.getText().toString());


        } catch (Exception ex) {
        }

        if (brand.getText().length() == 0) {
            brand.setError("field is empty");
            brand.requestFocus();
            return;
        }

        else  if (desc.getText().length() == 0) {
            desc.setError("field is empty");
            desc.requestFocus();
            return;
        }
     else   if (pn < 50) {
            pricenow.setError("minimum price should be 50");
            pricenow.requestFocus();
            return;
        } else if (pw < pn) {
            pricewas.setError("pricewas must be greater than pricenow");
            pricewas.requestFocus();
            return;
        }


        if (v.getId() == R.id.size1 || v.getId() == R.id.size2 || v.getId() == R.id.size3 || v.getId() == R.id.size4 || v.getId() == R.id.size5 || v.getId() == R.id.size6 || v.getId() == R.id.size7)
            clearSizetype2();
        else if (v.getId() == R.id.numsize1 || v.getId() == R.id.numsize2 || v.getId() == R.id.numsize3 || v.getId() == R.id.numsize4 || v.getId() == R.id.numsize5 || v.getId() == R.id.numsize6 || v.getId() == R.id.numsize7 || v.getId() == R.id.numsize8 || v.getId() == R.id.numsize9 || v.getId() == R.id.numsize10 || v.getId() == R.id.numsize11 || v.getId() == R.id.numsize12)
            clearSizetype1();

        switch (v.getId()) {

            //events for size
            case R.id.size1:
                if (sizetype1[0] == 0) {

                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype1[0] = 1;
                    sizetypelist1[0] = "2xs";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype1[0] = 0;
                    sizetypelist1[0] = "";
                }
                break;
            case R.id.size2:
                if (sizetype1[1] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype1[1] = 1;
                    sizetypelist1[1] = "xs";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype1[1] = 0;
                    sizetypelist1[1] = "";
                }
                break;
            case R.id.size3:
                if (sizetype1[2] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype1[2] = 1;
                    sizetypelist1[2] = "s";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype1[2] = 0;
                    sizetypelist1[2] = "";
                }
                break;
            case R.id.size4:
                if (sizetype1[3] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype1[3] = 1;
                    sizetypelist1[3] = "m";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype1[3] = 0;
                    sizetypelist1[3] = "";

                }
                break;
            case R.id.size5:
                if (sizetype1[4] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype1[4] = 1;
                    sizetypelist1[4] = "l";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype1[4] = 0;
                    sizetypelist1[4] = "";

                }
                break;
            case R.id.size6:
                if (sizetype1[5] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype1[5] = 1;
                    sizetypelist1[5] = "xl";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype1[5] = 0;
                    sizetypelist1[5] = "";
                }
                break;
            case R.id.size7:
                if (sizetype1[6] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype1[6] = 1;
                    sizetypelist1[6] = "2xl";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype1[6] = 0;
                    sizetypelist1[6] = "";

                }
                break;

            case R.id.numsize1:
                if (sizetype2[0] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[0] = 1;
                    sizetypelist2[0] = "00";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[0] = 0;
                    sizetypelist2[0] = "";

                }
                break;
            case R.id.numsize2:


                if (sizetype2[1] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[1] = 1;
                    sizetypelist2[1] = "0";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[1] = 0;
                    sizetypelist2[1] = "";

                }
                break;
            case R.id.numsize3:


                if (sizetype2[2] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[2] = 1;
                    sizetypelist2[2] = "2";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[2] = 0;
                    sizetypelist2[2] = "";

                }
                break;
            case R.id.numsize4:


                if (sizetype2[3] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[3] = 1;
                    sizetypelist2[3] = "4";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[3] = 0;
                    sizetypelist2[3] = "";

                }
                break;
            case R.id.numsize5:


                if (sizetype2[4] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[4] = 1;
                    sizetypelist2[4] = "6";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[4] = 0;
                    sizetypelist2[4] = "";

                }
                break;
            case R.id.numsize6:


                if (sizetype2[5] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[5] = 1;
                    sizetypelist2[5] = "8";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[5] = 0;
                    sizetypelist2[5] = "";

                }
                break;
            case R.id.numsize7:


                if (sizetype2[6] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[6] = 1;
                    sizetypelist2[6] = "10";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[6] = 0;
                    sizetypelist2[7] = "";

                }
                break;
            case R.id.numsize8:


                if (sizetype2[7] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[7] = 1;
                    sizetypelist2[7] = "12";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[7] = 0;
                    sizetypelist2[7] = "";

                }
                break;
            case R.id.numsize9:


                if (sizetype2[8] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[8] = 1;
                    sizetypelist2[8] = "14";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[8] = 0;
                    sizetypelist2[8] = "";

                }
                break;
            case R.id.numsize10:


                if (sizetype2[9] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[9] = 1;
                    sizetypelist2[9] = "16";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[9] = 0;
                    sizetypelist2[9] = "";

                }
                break;
            case R.id.numsize11:


                if (sizetype2[10] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[10] = 1;
                    sizetypelist2[10] = "18";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[10] = 0;
                    sizetypelist2[10] = "";

                }
                break;
            case R.id.numsize12:


                if (sizetype2[11] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[11] = 1;
                    sizetypelist2[11] = "19";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[11] = 0;
                    sizetypelist2[11] = "";

                }
                break;

            //for length events
            case R.id.dresstype1:
                if (dress1 == 0) {
                    selectDress();

                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    dress1 = 1;
                    dresstype = 1;
                } else {
                    ((TextView) v).requestFocus();

                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    dress1 = 0;
                    dresstype = 0;
                }
                break;
            case R.id.dresstype2:
                if (dress2 == 0) {
                    selectDress();
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    dress2 = 1;
                    dresstype = 2;

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    dress2 = 0;
                    dresstype = 0;


                }
                break;
            case R.id.dresstype3:
                if (dress3 == 0) {
                    selectDress();
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    dress3 = 1;
                    dresstype = 3;


                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));

                    dress3 = 0;
                    dresstype = 0;


                }
                break;
            case R.id.dresstype4:
                if (dress4 == 0) {
                    selectDress();
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    dress4 = 1;
                    dresstype = 4;


                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));

                    dress4 = 0;
                    dresstype = 0;


                }
                break;


//color events
            case R.id.color1:
                if (col[0] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[0] = 1;
                    colorlist[0] = "black";
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[0] = 0;
                    colorlist[0] = "";
                }
                break;
            case R.id.color2:
                if (col[1] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[1] = 1;
                    colorlist[1] = "silver";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[1] = 0;
                    colorlist[1] = "";
                }
                break;
            case R.id.color3:
                if (col[2] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[2] = 1;
                    colorlist[2] = "orange";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[2] = 0;
                    colorlist[2] = "";
                }
                break;
            case R.id.color4:
                if (col[3] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[3] = 1;
                    colorlist[3] = "white";
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[3] = 0;
                    colorlist[3] = "";
                }
                break;
            case R.id.color5:
                if (col[4] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[4] = 1;
                    colorlist[4] = "gold";
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[4] = 0;
                    colorlist[4] = "";

                }
                break;
            case R.id.color6:
                if (col[5] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[5] = 1;
                    colorlist[5] = "brown";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[5] = 0;
                    colorlist[5] = "";

                }
                break;
            case R.id.color7:
                if (col[6] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[6] = 1;
                    colorlist[6] = "red";
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[6] = 0;
                    colorlist[6] = "";

                }
                break;
            case R.id.color8:
                if (col[7] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[7] = 1;
                    colorlist[7] = "purple";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[7] = 0;
                    colorlist[7] = "";

                }
                break;
            case R.id.color9:
                if (col[8] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[8] = 1;
                    colorlist[8] = "nude";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[8] = 0;
                    colorlist[8] = "";


                }
                break;
            case R.id.color10:
                if (col[9] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[9] = 1;
                    colorlist[9] = "blue";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[9] = 0;
                    colorlist[9] = "";

                }
                break;
            case R.id.color11:
                if (col[10] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[10] = 1;
                    colorlist[10] = "yellow";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[10] = 0;
                    colorlist[10] = "";

                }
                break;
            case R.id.color12:
                if (col[11] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[11] = 1;
                    colorlist[11] = "gray";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[11] = 0;
                    colorlist[11] = "";

                }
                break;
            case R.id.color13:
                if (col[12] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[12] = 1;
                    colorlist[12] = "green";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[12] = 0;
                    colorlist[12] = "";

                }
                break;
            case R.id.color14:
                if (col[13] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[13] = 1;
                    colorlist[13] = "pink";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[13] = 0;
                    colorlist[13] = "";

                }
                break;
            case R.id.color15:
                if (col[14] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[14] = 1;
                    colorlist[14] = "pattern";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[14] = 0;
                    colorlist[14] = "";

                }
                break;


            //cases for condition
            case R.id.lastnightdress:
                if (condi1 == 0) {
                    condition();
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    condi1 = 1;
                    condition = 11;
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    condi1 = 0;
                    condition = 0;

                }

                break;
            case R.id.conditionnew:
                if (condi2 == 0) {
                    condition();
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    condi2 = 1;
                    condition = 12;
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    condi2 = 0;
                    condition = 0;

                }
                break;


        }


    }

    private void selectDress() {
        dresstype1.setBackgroundColor(Color.parseColor("#1d1f21"));
        dresstype2.setBackgroundColor(Color.parseColor("#1d1f21"));
        dresstype3.setBackgroundColor(Color.parseColor("#1d1f21"));
        dresstype4.setBackgroundColor(Color.parseColor("#1d1f21"));
        dress1 = dress2 = dress3 = dress4 = 0;

    }

    public void done(View v) {
        boolean size = true, color = true;
        ArrayList<String> dresssize = new ArrayList<>();
        ArrayList<String> dresscolor = new ArrayList<>();

        int pw = 0, pn = 0;
        try {
            pw = Integer.parseInt(pricewas.getText().toString());
            pn = Integer.parseInt(pricenow.getText().toString());

        } catch (Exception ex) {
        }

        //to check atleast one size selected
        for (int i = 0; i < sizetypelist1.length; i++) {
            if (sizetypelist1[i].length() > 0) {
                dresssize.add(sizetypelist1[i]);
                size = false;
            }
        }
        if (size) {
            for (int i = 0; i < sizetypelist2.length; i++) {
                if (sizetypelist2[i].length() > 0) {
                    dresssize.add(sizetypelist2[i]);
                    size = false;
                }
            }
        }

//to check atleast one color selected
        for (int i = 0; i < colorlist.length; i++) {
            if (colorlist[i].length() > 0) {
                color = false;
                dresscolor.add(colorlist[i]);

            }
        }
        if (brand.getText().length() == 0) {
            brand.setError("field is empty");
            brand.requestFocus();
            return;
        }

        if (desc.getText().length() == 0) {
            desc.setError("field is empty");
            desc.requestFocus();
            return;
        } else if (pricenow.getText().length() == 0) {
            pricenow.setError("field is empty");
            pricenow.requestFocus();
            return;
        } else if (pricewas.getText().length() == 0) {
            pricewas.setError("field is empty");
            pricewas.requestFocus();
            return;
        } else if (pn < 50) {
            pricenow.setError("minimum price should be 50");
            pricenow.requestFocus();
            return;
        } else if (pw < pn) {
            pricewas.setError("pricewas must be greater than pricenow");
            pricewas.requestFocus();
            return;
        } else if (size) {
            Toast.makeText(this, "select  size", Toast.LENGTH_SHORT).show();
            return;
        } else if (dresstype == 0) {
            Toast.makeText(this, "select dress type", Toast.LENGTH_SHORT).show();
            return;
        } else if (color) {
            Toast.makeText(this, "select color", Toast.LENGTH_SHORT).show();
            return;
        } else if (condition == 0) {
            Toast.makeText(this, "select condition", Toast.LENGTH_SHORT).show();
            return;

        }

        //taking date time
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        String strDate = sdf.format(cal.getTime());


        JSONArray dressArray = new JSONArray(dresssize);
        JSONArray colorArray = new JSONArray(dresscolor);

        try {
            //image1 json
            JSONObject image1 = new JSONObject();
            image1.put("imagename", filename.get(0));
            image1.put("imageurl", links[0]);

            //image2 json
            JSONObject image2 = new JSONObject();
            image2.put("imagename", filename.get(1));
            image2.put("imageurl", links[1]);

            //image3 json
            JSONObject image3 = new JSONObject();
            image3.put("imagename", filename.get(2));
            image3.put("imageurl", links[2]);

            //image4 json
            JSONObject image4 = new JSONObject();
            image4.put("imagename", filename.get(3));
            image4.put("imageurl", links[3]);


            //images array
            JSONArray imagesarray = new JSONArray();
            imagesarray.put(image1);
            imagesarray.put(image2);
            imagesarray.put(image3);
            imagesarray.put(image4);


            String userid = ImageLoaderImage.pref.getString("user_id", "");
            JSONObject mainObj = new JSONObject();

            mainObj.put("user_id", userid);
            mainObj.put("condition", condition);
            mainObj.put("brand", brand.getText().toString());
            mainObj.put("categorytype", 1);

            mainObj.put("condition", condition);


            mainObj.put("size1", dressArray);

            mainObj.put("dresstype", dresstype);

            mainObj.put("color", colorArray);
            mainObj.put("images", imagesarray);
            mainObj.put("description", desc.getText().toString());
            mainObj.put("pricenow", pricenow.getText().toString());
            mainObj.put("pricewas", pricewas.getText().toString());
            mainObj.put("datetime", strDate);

              uploadImage(mainObj.toString());

          //  Log.e("json", mainObj.toString());
        } catch (Exception ex) {
            Log.e("json error", ex.getMessage() + "");
        }
    }

    public void uploadImage(final String data) {

        Log.e("test", "check");
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("wait posting dress...");
        pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/lndpost.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                // Log.e("responsedata", response.toString());
                try {
                    JSONObject jobj = new JSONObject(response);
                    if (jobj.getBoolean("status")) {
                        Toast.makeText(DressPost.this, jobj.getString("message") + "", Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(DressPost.this, PayPalAccountCreation.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(DressPost.this, jobj.getString("message") + "", Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException ex) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                //   Log.e("response error", error.getMessage() + "");

                Toast.makeText(DressPost.this, "Dress not posted please try again", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("data", data);
                params.put("rqid", "1");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return super.getBody();
            }
        };
        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);

        queue.add(sr);
    }

    private class AsyncTaskLoadImage extends AsyncTask<String, Bitmap, Bitmap> {

        ImageView img;
        int pos;

        public AsyncTaskLoadImage(ImageView image, int pos) {
            img = image;
            this.pos = pos;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            final Bitmap myImg = CompressImage.compressImage(url[0]);                         //BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            myImg.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            links[pos] = Base64.encodeToString(byte_arr, 0);

            return myImg;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            img.setImageBitmap(bitmap);
        }
       /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */

    }

    private void clearSizetype1() {
        for (int i = 0; i < 7; i++) {
            dresssize.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
            sizetypelist1[i] = "";
        }
    }

    private void clearSizetype2() {
        for (int i = 7; i < dresssize.size(); i++) {
            dresssize.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));

        }
        for (int i = 0; i < sizetypelist2.length; i++) {
            sizetypelist2[i] = "";
        }
    }

    private void condition(TextView txt) {
        conditionspinner.setSelection(0);
        lastnightdress.setBackgroundColor(Color.parseColor("#1d1f21"));

        conditionnew.setBackgroundColor(Color.parseColor("#1d1f21"));

        txt.setBackgroundColor(Color.parseColor("#be4d66"));
        condi1 = condi2 = 0;


    }

    private void condition() {
        lastnightdress.setBackgroundColor(Color.parseColor("#1d1f21"));

        conditionnew.setBackgroundColor(Color.parseColor("#1d1f21"));
    }

    public void priceins(View v) {
        final LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.custom_popup_menu, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        //     popupWindow.showAsDropDown(mTextView, 50, -30);
        // popupWindow.showAtLocation(mTextView,1,0,0);
        TextView btnDismiss = (TextView) popupView.findViewById(R.id.close);
        TextView above = (TextView) popupView.findViewById(R.id.above);
        above.setText("$400 & above - 10%");

        btnDismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);

        popupWindow.showAtLocation(v, Gravity.TOP, 0, 0);

    }

    public void instruction(View v) {
        //popup window reference
        popupWindow = new Lnd_Post_Instruction(this).instruction();
        popupWindow.showAtLocation(v, Gravity.TOP, 0, 0);
    }

    private void setupEmoji() {
// Give the topmost view of your activity layout hierarchy. This will be used to measure soft keyboard height
        final EmojiconsPopup popup = new EmojiconsPopup(rootView, this);

        //Will automatically set size according to the soft keyboard size
        popup.setSizeForSoftKeyboard();

        //If the emoji popup is dismissed, change emojiButton to smiley icon
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                changeEmojiKeyboardIcon(emojiButton, R.drawable.smiley);
            }
        });
//If the text keyboard closes, also dismiss the emoji popup
        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

            @Override
            public void onKeyboardOpen(int keyBoardHeight) {

            }

            @Override
            public void onKeyboardClose() {
                if (popup.isShowing())
                    popup.dismiss();
            }
        });

        //On emoji clicked, add it to edittext
        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (desc == null || emojicon == null) {
                    return;
                }

                int start = desc.getSelectionStart();
                int end = desc.getSelectionEnd();
                if (start < 0) {
                    desc.append(emojicon.getEmoji());
                } else {
                    desc.getText().replace(Math.min(start, end),
                            Math.max(start, end), emojicon.getEmoji(), 0,
                            emojicon.getEmoji().length());
                }
            }
        });

        //On backspace clicked, emulate the KEYCODE_DEL key event
        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

            @Override
            public void onEmojiconBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                desc.dispatchKeyEvent(event);
            }
        });

        // To toggle between text keyboard and emoji keyboard keyboard(Popup)
        emojiButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //If popup is not showing => emoji keyboard is not visible, we need to show it
                if (!popup.isShowing()) {

                    //If keyboard is visible, simply show the emoji popup
                    if (popup.isKeyBoardOpen()) {
                        popup.showAtBottom();
                        changeEmojiKeyboardIcon(emojiButton, R.drawable.keyboard);
                    }

                    //else, open the text keyboard first and immediately after that show the emoji popup
                    else {
                        desc.setFocusableInTouchMode(true);
                        desc.requestFocus();
                        popup.showAtBottomPending();
                        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(desc, InputMethodManager.SHOW_IMPLICIT);
                        changeEmojiKeyboardIcon(emojiButton, R.drawable.keyboard);
                    }
                }

                //If popup is showing, simply dismiss it to show the undelying text keyboard
                else {
                    popup.dismiss();
                }
            }
        });


    }

    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId) {
        iconToBeChanged.setImageResource(drawableResourceId);
    }
}

