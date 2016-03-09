package com.eowise.recyclerview.stickyheaders.samples.PostDataShop;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.eowise.recyclerview.stickyheaders.samples.LndCustomCameraPost.CameraReviewFragment;
import com.eowise.recyclerview.stickyheaders.samples.LndCustomCameraPost.CompressImage;
import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
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

public class JewelleryPost extends AppCompatActivity implements View.OnClickListener {
    @Bind({R.id.image1,R.id.image2,R.id.image3,R.id.image4})
    List<ImageView> images;

    //condition reference
    @Bind(R.id.conditionnew)
    TextView conditionnew;
    @Bind(R.id.conditionspinner)
    Spinner conditionspinner;

    @Bind({R.id.size1, R.id.size2, R.id.size3, R.id.size4, R.id.size5, R.id.size6, R.id.size7, R.id.size8, R.id.size9, R.id.size10, R.id.size11, R.id.size12, R.id.size13, R.id.size14, R.id.size15, R.id.size16, R.id.size17, R.id.size18, R.id.size19, R.id.size20, R.id.size21})

    List<TextView> ringsize;
    @Bind(R.id.ringsizelayout)
    LinearLayout ringsizelayout;


    @Bind({R.id.type1, R.id.type2, R.id.type3, R.id.type4, R.id.type5,})
    List<TextView> jewellerytype;

    @Bind({R.id.metal1, R.id.metal2, R.id.metal3, R.id.metal4, R.id.metal5, R.id.metal6, R.id.metal7, R.id.metal8})
    List<TextView> metaltype;

    @Bind(R.id.pricewas)
    EditText pricewas;
    @Bind(R.id.pricenow)
    EditText pricenow;
    @Bind(R.id.desc)
    EmojiconEditText desc;
    @Bind(R.id.brand)
    EditText brand;
    @Bind(R.id.earnings)
    TextView earnings;
    @Bind(R.id.infoview)
    LinearLayout inforview;

    @Bind(R.id.emoji_btn)
    ImageButton emojiButton;
    @Bind(R.id.rootview) View rootView;

    int metal[] = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    int size[] = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};


    String[] sizelist = new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};


    int jewelleryselected = 0;
    int condition = 0;
    String[] metaltypelist = new String[]{"", "", "", "", "", "", "", "", ""};

    PopupWindow popupWindow;
    ValueAnimator mAnimator;
    private boolean sizeopen = true;
    String[] links = new String[4];
    ArrayList<String> filename = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jewellery_post_page);
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


        //jewellerytype listener
        for (int i = 0; i < jewellerytype.size(); i++) {
            jewellerytype.get(i).setOnClickListener(this);
        }

        //condtion  events
        conditionnew.setOnClickListener(this);


        //metaltype listener
        for (int i = 0; i < metaltype.size(); i++) {
            metaltype.get(i).setOnClickListener(this);
        }
//ring size listener
        for (int i = 0; i < ringsize.size(); i++) {
            ringsize.get(i).setOnClickListener(this);
        }
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
                    earnings.setText("Your earnings: C$" + (value-(value * 20) / 100));
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
        for(Map.Entry<String, CameraData> entry: CameraReviewFragment.urls.entrySet()) {
            //    System.out.println(entry.getKey());
            CameraData cd=entry.getValue();
            int value=Integer.parseInt(entry.getKey())-1;
            filename.add(value, cd.getFilename());
            new AsyncTaskLoadImage(images.get(value), value).execute(cd.getImageurl());


        }

    }

    @Override
    public void onBackPressed() {
        if(popupWindow!=null && popupWindow.isShowing())
        {
            popupWindow.dismiss();
        }
        else
            finish();
    }

    public void back(View v) {
        onBackPressed();
    }

    private void selectJewelleryType(TextView txt) {
        for (int i = 0; i < jewellerytype.size(); i++)
            jewellerytype.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
        txt.setBackgroundColor(Color.parseColor("#be4d66"));
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

        switch (v.getId()) {
            //for jewellery type events
            case R.id.type1:
                selectJewelleryType((TextView) v);

                jewelleryselected = 1;
                sizeopen = true;
                collapse();
                break;
            case R.id.type2:
                selectJewelleryType((TextView) v);
                jewelleryselected = 2;
                if (sizeopen) {
                    expand();
                    sizeopen = false;
                }
                break;
            case R.id.type3:
                selectJewelleryType((TextView) v);
                jewelleryselected = 3;
                sizeopen = true;
                collapse();
                break;
            case R.id.type4:
                selectJewelleryType((TextView) v);
                jewelleryselected = 4;
                sizeopen = true;
                collapse();
                break;
            case R.id.type5:
                selectJewelleryType((TextView) v);
                jewelleryselected = 5;
                sizeopen = true;
                collapse();
                break;

            // condition events
            case R.id.conditionnew:
                conditionnew.setBackgroundColor(Color.parseColor("#be4d66"));
                condition =11;
                conditionspinner.setSelection(0);

                break;


            //for metal type events
            case R.id.metal1:
                if (metal[0] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    metal[0] = 1;
                    metaltypelist[0] = "1";
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    metal[0] = 0;
                    metaltypelist[0] = "";
                }
                break;
            case R.id.metal2:
                if (metal[1] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    metal[1] = 1;
                    metaltypelist[1] = "2";
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    metal[1] = 0;
                    metaltypelist[1] = "";
                }
                break;
            case R.id.metal3:
                if (metal[2] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    metal[2] = 1;
                    metaltypelist[2] = "3";
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    metal[2] = 0;
                    metaltypelist[2] = "";
                }
                break;
            case R.id.metal4:
                if (metal[3] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    metal[3] = 1;
                    metaltypelist[3] = "4";
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    metal[3] = 0;
                    metaltypelist[3] = "";
                }
                break;
            case R.id.metal5:
                if (metal[4] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    metal[4] = 1;
                    metaltypelist[4] ="5";
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    metal[4] = 0;
                    metaltypelist[4] = "";
                }
                break;
            case R.id.metal6:
                if (metal[5] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    metal[5] = 1;
                    metaltypelist[5] = "6";
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    metal[5] = 0;
                    metaltypelist[5] = "";
                }
                break;
            case R.id.metal7:
                if (metal[6] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    metal[6] = 1;
                    metaltypelist[6] = "7";
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    metal[6] = 0;
                    metaltypelist[6] = "";
                }
                break;
            case R.id.metal8:
                if (metal[7] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    metal[7] = 1;
                    metaltypelist[7] = "8";
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    metal[7] = 0;
                    metaltypelist[7] = "";
                }
                break;

            //events for size
            case R.id.size1:
                if (size[0] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[0] = 1;
                    sizelist[0] = "os";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[0] = 0;
                    sizelist[0] = "";
                }
                break;
            case R.id.size2:
                if (size[1] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[1] = 1;
                    sizelist[1] = "1";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[1] = 0;
                    sizelist[1] = "";
                }
                break;
            case R.id.size3:
                if (size[2] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[2] = 1;
                    sizelist[2] = "1.5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[2] = 0;
                    sizelist[2] = "";
                }
                break;
            case R.id.size4:
                if (size[3] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[3] = 1;
                    sizelist[3] = "2";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[3] = 0;
                    sizelist[3] = "";

                }
                break;
            case R.id.size5:
                if (size[4] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[4] = 1;
                    sizelist[4] = "2.5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[4] = 0;
                    sizelist[4] = "";

                }
                break;
            case R.id.size6:
                if (size[5] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[5] = 1;
                    sizelist[5] = "3";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[5] = 0;
                    sizelist[5] = "";
                }
                break;
            case R.id.size7:
                if (size[6] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[6] = 1;
                    sizelist[6] = "3.5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[6] = 0;
                    sizelist[6] = "";

                }
                break;
            case R.id.size8:
                if (size[7] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[7] = 1;
                    sizelist[7] = "4";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[7] = 0;
                    sizelist[7] = "";

                }
                break;
            case R.id.size9:


                if (size[8] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[8] = 1;
                    sizelist[8] = "4.5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[8] = 0;
                    sizelist[8] = "";

                }
                break;
            case R.id.size10:


                if (size[9] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[9] = 1;
                    sizelist[9] = "6";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[9] = 0;
                    sizelist[9] = "";

                }
                break;
            case R.id.size11:


                if (size[10] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[10] = 1;
                    sizelist[10] = "6.5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[10] = 0;
                    sizelist[10] = "";

                }
                break;
            case R.id.size12:


                if (size[11] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[11] = 1;
                    sizelist[11] = "7";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[11] = 0;
                    sizelist[11] = "";

                }
                break;
            case R.id.size13:


                if (size[12] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[12] = 1;
                    sizelist[12] = "7.5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[12] = 0;
                    sizelist[12] = "";

                }
                break;
            case R.id.size14:


                if (size[13] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[13] = 1;
                    sizelist[13] = "8";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[13] = 0;
                    sizelist[13] = "";

                }
                break;
            case R.id.size15:


                if (size[14] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[14] = 1;
                    sizelist[14] = "8.5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[14] = 0;
                    sizelist[14] = "";

                }
                break;
            case R.id.size16:


                if (size[15] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[15] = 1;
                    sizelist[15] = "9";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[15] = 0;
                    sizelist[15] = "";

                }
                break;
            case R.id.size17:


                if (size[16] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[16] = 1;
                    sizelist[16] = "9.5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[16] = 0;
                    sizelist[16] = "";

                }
                break;
            case R.id.size18:


                if (size[17] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[17] = 1;
                    sizelist[17] = "10";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[17] = 0;
                    sizelist[17] = "";

                }
                break;
            case R.id.size19:


                if (size[18] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[18] = 1;
                    sizelist[18] = "10.5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[18] = 0;
                    sizelist[18] = "";

                }
                break;
            case R.id.size20:


                if (size[19] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[19] = 1;
                    sizelist[19] = "11";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[19] = 0;
                    sizelist[19] = "";

                }
                break;
            case R.id.size21:


                if (size[20] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[20] = 1;
                    sizelist[20] = "11.5+";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[20] = 0;
                    sizelist[20] = "";

                }
                break;




        }

    }

    private void collapse() {
        int finalHeight = ringsizelayout.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                ringsizelayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }


    private void expand() {


        ringsizelayout.setVisibility(View.VISIBLE);

        // Remove and used in preDrawListener
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        ringsizelayout.measure(widthSpec, heightSpec);

        mAnimator = slideAnimator(0, ringsizelayout.getMeasuredHeight());


        mAnimator.start();

    }

    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = ringsizelayout.getLayoutParams();
                layoutParams.height = value;
                ringsizelayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
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

    private void condition() {
        conditionnew.setBackgroundColor(Color.parseColor("#1d1f21"));


    }

    public void done(View v) {
        boolean rsize=true, mtype = true;
        int pw=0,pn=0;
        ArrayList<String> ringsize=new ArrayList<>();
        ArrayList<String> metaltype=new ArrayList<>();

        try
        {
            pw=Integer.parseInt(pricewas.getText().toString());
            pn=Integer.parseInt(pricenow.getText().toString());

        }
        catch(Exception ex)
        {

        }

        //to check atleast one size selected
        if(jewelleryselected==2) {
            for (int i = 0; i < sizelist.length; i++) {
                if (sizelist[i].length() > 0) {
                    rsize = false;
                    ringsize.add(sizelist[i]);
                }
            }
        }
        else
        ringsize.add("os");
        //to check atleast one metaltype selected
        for (int i = 0; i < metaltypelist.length; i++) {
            if (metaltypelist[i].length()> 0) {
                mtype = false;
                metaltype.add(metaltypelist[i]);
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
        }
        else if(pn<50)
        {
            pricenow.setError("minimum price should be 50");
            pricenow.requestFocus();
            return;
        }
        else if(pw<pn)
        {
            pricewas.setError("pricewas must be greater than pricenow");
            pricewas.requestFocus();
            return;
        }

        else if (rsize&&(jewelleryselected==2)) {
            Toast.makeText(this, "select ring  size", Toast.LENGTH_SHORT).show();
            return;
        } else if (mtype) {
            Toast.makeText(this, "select  type", Toast.LENGTH_SHORT).show();
            return;
        } else if (condition==0) {
            Toast.makeText(this, "select condition", Toast.LENGTH_SHORT).show();
            return;

        }


        JSONArray sizeArray = new JSONArray(ringsize);
        JSONArray metalArray = new JSONArray(metaltype);
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

            //taking date time
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            String strDate = sdf.format(cal.getTime());

            //images array
            JSONArray imagesarray = new JSONArray();
            imagesarray.put(image1);
            imagesarray.put(image2);
            imagesarray.put(image3);
            imagesarray.put(image4);

            String userid = ImageLoaderImage.pref.getString("user_id", "");

            JSONObject mainObj = new JSONObject();



            mainObj.put("user_id",userid);
            mainObj.put("brand", brand.getText().toString());
            mainObj.put("categorytype",4);
            mainObj.put("jewellerytype", jewelleryselected);

            mainObj.put("condition", condition);


            mainObj.put("jewellerysize", sizeArray);
            mainObj.put("datetime", strDate);
            mainObj.put("description", desc.getText().toString());
            mainObj.put("pricenow", pricenow.getText().toString());
            mainObj.put("pricewas", pricewas.getText().toString());
            mainObj.put("metaltype", metalArray);
            mainObj.put("images", imagesarray);

           jewelleryPost(mainObj.toString());

            //Log.e("json", mainObj.toString());
        } catch (Exception ex) {
            Log.e("json error", ex.getMessage() + "");
        }
    }
    public void priceins(View v)
    {
        final LayoutInflater layoutInflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.custom_popup_menu, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        //     popupWindow.showAsDropDown(mTextView, 50, -30);
        // popupWindow.showAtLocation(mTextView,1,0,0);
        TextView btnDismiss = (TextView)popupView.findViewById(R.id.close);
        TextView above = (TextView)popupView.findViewById(R.id.above);
        above.setText("$400 & above - 10%");

        btnDismiss.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }});

        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);

        popupWindow.showAtLocation(v, Gravity.TOP, 0, 0);

    }
    public void jewelleryPost(final String data) {

        Log.e("test", "check");
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("wait posting jewellery...");
        pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/lndpost.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                Log.e("responsedata", response.toString());
                try {
                    JSONObject jobj = new JSONObject(response);
                    if (jobj.getBoolean("status")) {
                        Toast.makeText(JewelleryPost.this, jobj.getString("message")+"", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(JewelleryPost.this, jobj.getString("message")+"", Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException ex) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                //   Log.e("response error", error.getMessage() + "");

                Toast.makeText(JewelleryPost.this, "Jewellery not posted please try again", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("data", data);
                params.put("rqid", "4");

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
    public void instruction(View v)
    {
        //popup window reference
        popupWindow=  new Lnd_Post_Instruction(this).instruction();
        popupWindow.showAtLocation(v, Gravity.TOP, 0, 0);
    }
    private void setupEmoji()
    {
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
    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId){
        iconToBeChanged.setImageResource(drawableResourceId);
    }
}
