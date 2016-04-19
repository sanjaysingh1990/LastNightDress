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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
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
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.Home_List_Data;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ConstantValues;
import com.eowise.recyclerview.stickyheaders.samples.Utils.HashTagandMention;
import com.eowise.recyclerview.stickyheaders.samples.Utils.InstructionDialogs;
import com.eowise.recyclerview.stickyheaders.samples.Utils.LndTextWatcher;
import com.eowise.recyclerview.stickyheaders.samples.Utils.LndTokenizer;
import com.eowise.recyclerview.stickyheaders.samples.data.CameraData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

public class JewelleryEditPost extends AppCompatActivity implements View.OnClickListener {
    @Bind({R.id.image1, R.id.image2, R.id.image3, R.id.image4})
    List<ImageView> images;

    //condition reference
    @Bind(R.id.conditionnew)
    CheckBox conditionnew;
    @Bind(R.id.conditionspinner)
    Spinner conditionspinner;

    @Bind({R.id.size1, R.id.size2, R.id.size3, R.id.size4, R.id.size5, R.id.size6, R.id.size7, R.id.size8, R.id.size9, R.id.size10, R.id.size11, R.id.size12, R.id.size13, R.id.size14, R.id.size15, R.id.size16, R.id.size17, R.id.size18, R.id.size19, R.id.size20, R.id.size21})

    List<CheckBox> ringsize;
    @Bind(R.id.ringsizelayout)
    LinearLayout ringsizelayout;


    @Bind({R.id.type1, R.id.type2, R.id.type3, R.id.type4, R.id.type5,})
    List<CheckBox> jewellerytype;

    @Bind({R.id.metal1, R.id.metal2, R.id.metal3, R.id.metal4, R.id.metal5, R.id.metal6, R.id.metal7, R.id.metal8})
    List<CheckBox> metaltype;

    @Bind(R.id.pricewas)
    EditText pricewas;
    @Bind(R.id.pricenow)
    EditText pricenow;
    @Bind(R.id.autocomplete)
    MultiAutoCompleteTextView desc;
    @Bind(R.id.brand)
    EditText brand;
    @Bind(R.id.earnings)
    TextView earnings;
    @Bind(R.id.infoview)
    LinearLayout inforview;

    @Bind(R.id.emoji_btn)
    ImageButton emojiButton;
    @Bind(R.id.rootview)
    View rootView;


    int jewelleryselected = 0;
    int condition = 0;

    PopupWindow popupWindow;
    ValueAnimator mAnimator;
    private boolean sizeopen = true;
    String[] links = new String[4];
    ArrayList<String> filename = new ArrayList<>();
    private Bundle extra;
    InstructionDialogs lndcommistiondialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jewellery_post_page);
        //intialiaing dialog
        lndcommistiondialog = new InstructionDialogs(this);


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


        //condition spinner selection events
        conditionspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if (pos > 0) {


                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#ffffff"));
                    ((TextView) parent.getChildAt(0)).setBackgroundColor(Color.parseColor("#be4d66"));
                    conditionspinner.setBackgroundColor(Color.parseColor("#be4d66"));
                    condition = pos;
                    condition();
                } else {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#ffffff"));
                    ((TextView) parent.getChildAt(0)).setBackgroundColor(Color.parseColor("#1d1f21"));
                    conditionspinner.setBackgroundColor(Color.parseColor("#1d1f21"));
                    if (condition != 11)
                        condition = pos;
                }
                ((TextView) parent.getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);


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

        //username selected from list
        desc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int index, long position) {
                desc.setText(new HashTagandMention().addClickablePart(desc.getText().toString(), "#be4d66"));
                desc.setSelection(desc.getText().length());
            }
        });


        desc.setThreshold(1); //Set number of characters before the dropdown should be shown

        desc.addTextChangedListener(new LndTextWatcher(desc, this));

        //Create a new Tokenizer which will get text after '@' and terminate on ' '
        desc.setTokenizer(new LndTokenizer());


        setupEmoji();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //read data
        extra = getIntent().getExtras();
        if (extra != null) {
            Home_List_Data hld = (Home_List_Data) extra.getSerializable("data");
            setValues(hld);
        }


        for (Map.Entry<String, CameraData> entry : CameraReviewFragment.urls.entrySet()) {
            //    System.out.println(entry.getKey());
            CameraData cd = entry.getValue();
            int value = Integer.parseInt(entry.getKey()) - 1;
            filename.add(value, cd.getFilename());

            new AsyncTaskLoadImage(images.get(value), value).execute(cd.getImageurl());


        }

    }

    private void setValues(Home_List_Data hld) {
        //set images
        for (int i = 0; i < hld.getImageurls().size(); i++) {
            if (hld.getImageurls().get(i).length() > 0)
                SingleTon.imageLoader.displayImage(hld.getImageurls().get(i), images.get(i), SingleTon.options);
        }
        //set brand
        brand.setText(hld.getBrandname());
        //set description
        desc.setText(new HashTagandMention().addClickablePart(hld.getDescription(), "#be4d66"));

        //pricewas
        pricewas.setText(hld.getPricewas());
        //pricenow
        pricenow.setText(hld.getPricenow());

        // condition
        try {
            int val = Integer.parseInt(hld.getConditon());
            if (val >= 1 && val <= 10)
                conditionspinner.setSelection(val);

            else
                conditionnew.setChecked(true);

        } catch (Exception ex) {

        }

        //jewellery type
        try {


            int val = Integer.parseInt(hld.getProdtype());
            jewellerytype.get(val - 1).setChecked(true);
            jewelleryselected = val;
        } catch (Exception ex) {

        }
        try {
            //metal

            String[] jewellerymetal = hld.getColors().split(",");
            for (int i = 0; i < jewellerymetal.length; i++) {
                int index = Integer.parseInt(jewellerymetal[i]);
                index--;
                this.metaltype.get(index).setChecked(true);


            }
        } catch (Exception ex)

        {

        }
        //size1
        try {
            int val = Integer.parseInt(hld.getProdtype());
            if (val == 2) {
                String[] size1 = hld.getSize().split(",");

                for (int i = 0; i < size1.length; i++) {
                    Arrays.sort(ConstantValues.ringsize);
                    int index = Arrays.binarySearch(ConstantValues.ringsize, size1[i]);
                    this.ringsize.get(index).setChecked(true);


                }
            }
        } catch (Exception ex) {

        }


    }

    @Override
    public void onBackPressed() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else
            finish();
    }

    public void back(View v) {
        onBackPressed();
    }

    private void selectJewelleryType(CheckBox cb) {
        for (int i = 0; i < jewellerytype.size(); i++)
            jewellerytype.get(i).setChecked(false);
        cb.setChecked(true);
    }

    @Override
    public void onClick(View v) {


     /*   int pw = 0, pn = 0;
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
*/
        switch (v.getId()) {
            //for jewellery type events
            case R.id.type1:
                selectJewelleryType((CheckBox) v);
                sizeopen = true;
                collapse();
                break;
            case R.id.type2:
                selectJewelleryType((CheckBox) v);
                if (sizeopen) {
                    expand();
                    sizeopen = false;
                }
                break;
            case R.id.type3:
                selectJewelleryType((CheckBox) v);
                sizeopen = true;
                collapse();
                break;
            case R.id.type4:
                selectJewelleryType((CheckBox) v);
                sizeopen = true;
                collapse();
                break;
            case R.id.type5:
                selectJewelleryType((CheckBox) v);
                sizeopen = true;
                collapse();
                break;

            // condition events
            case R.id.conditionnew:
                conditionnew.setChecked(true);
                condition = 11;
                conditionspinner.setSelection(0);

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
        conditionnew.setChecked(false);


    }

    public void done(View v) {
        boolean rsize = true, mtype = true;
        int pw = 0, pn = 0;
        ArrayList<String> ringsize = new ArrayList<>();
        ArrayList<String> metaltype = new ArrayList<>();

        try {
            pw = Integer.parseInt(pricewas.getText().toString());
            pn = Integer.parseInt(pricenow.getText().toString());

        } catch (Exception ex) {

        }

        //to check atleast one size selected
        if (jewelleryselected == 2) {
            for (int i = 0; i < this.ringsize.size(); i++) {
                if (this.ringsize.get(i).isChecked()) {
                    rsize = false;
                    ringsize.add(this.ringsize.get(i).getTag().toString());
                }
            }
        } else
            ringsize.add("os");
        //to check atleast one metaltype selected
        for (int i = 0; i < this.metaltype.size(); i++) {
            if (this.metaltype.get(i).isChecked()) {
                mtype = false;
                metaltype.add(this.metaltype.get(i).getTag().toString());
            }
        }

        //to check jewellery type
        for (int i = 0; i < jewellerytype.size(); i++) {
            if (this.jewellerytype.get(i).isChecked()) {
                jewelleryselected = i + 1;
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
        } else if (rsize && (jewelleryselected == 2)) {
            Toast.makeText(this, "select ring  size", Toast.LENGTH_SHORT).show();
            return;
        } else if (jewelleryselected == 0) {
            Toast.makeText(this, "select  type", Toast.LENGTH_SHORT).show();
            return;
        } else if (mtype) {
            Toast.makeText(this, "select metal type", Toast.LENGTH_SHORT).show();
            return;
        } else if (condition == 0) {
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


            //images array
            JSONArray imagesarray = new JSONArray();
            imagesarray.put(image1);
            imagesarray.put(image2);
            imagesarray.put(image3);
            imagesarray.put(image4);

            String userid = SingleTon.pref.getString("user_id", "");

            JSONObject mainObj = new JSONObject();


            mainObj.put("user_id", userid);
            mainObj.put("brand", brand.getText().toString());
            mainObj.put("categorytype", 4);
            mainObj.put("jewellerytype", jewelleryselected);

            mainObj.put("condition", condition);


            mainObj.put("jewellerysize", sizeArray);
            mainObj.put("datetime", SingleTon.getCurrentTimeStamp());
            mainObj.put("description", desc.getText().toString());
            mainObj.put("pricenow", pricenow.getText().toString());
            mainObj.put("pricewas", pricewas.getText().toString());
            mainObj.put("metaltype", metalArray);
            mainObj.put("images", imagesarray);
            if (extra == null)
                jewelleryPost(mainObj.toString());

            //Log.e("json", mainObj.toString());
        } catch (Exception ex) {
            Log.e("json error", ex.getMessage() + "");
        }
    }

    public void priceins(View v) {
        if(!lndcommistiondialog.popupWindow.isShowing())
            lndcommistiondialog.show(v);

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
                        Toast.makeText(JewelleryEditPost.this, jobj.getString("message") + "", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(JewelleryEditPost.this, jobj.getString("message") + "", Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException ex) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                //   Log.e("response error", error.getMessage() + "");

                Toast.makeText(JewelleryEditPost.this, "Jewellery not posted please try again", Toast.LENGTH_LONG).show();
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
