package com.eowise.recyclerview.stickyheaders.samples.PrivatePost;

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
import android.view.View;
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
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.LndCustomCameraPost.CameraReviewFragment;
import com.eowise.recyclerview.stickyheaders.samples.LndCustomCameraPost.CompressImage;
import com.eowise.recyclerview.stickyheaders.samples.PostDataShop.Lnd_Post_Instruction;
import com.eowise.recyclerview.stickyheaders.samples.R;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

public class HandBagsPostPrivate extends AppCompatActivity implements View.OnClickListener {
    @Bind({R.id.image1, R.id.image2, R.id.image3, R.id.image4})
    List<ImageView> images;

    @Bind({R.id.size1, R.id.size2, R.id.size3})
    List<TextView> handbagsize;

    @Bind({R.id.color1, R.id.color2, R.id.color3, R.id.color4, R.id.color5, R.id.color6, R.id.color7, R.id.color8, R.id.color9, R.id.color10, R.id.color11, R.id.color12, R.id.color13, R.id.color14, R.id.color15})
    List<TextView> color;
    @Bind(R.id.earnings)
    TextView earnings;
    @Bind(R.id.infoview)
    LinearLayout inforview;
    @Bind(R.id.emoji_btn)
    ImageButton emojiButton;
    @Bind(R.id.rootview)
    View rootView;

    @Bind({R.id.purses, R.id.clutches,})
    List<TextView> handbagtype;
    @Bind(R.id.conditionspinner)
    Spinner conditionspinner;
    @Bind(R.id.autocomplete)
    MultiAutoCompleteTextView desc;
    @Bind(R.id.brand)
    EditText brand;
    @Bind(R.id.pricewas)
    EditText pricewas;
    @Bind(R.id.pricenow)
    EditText pricenow;
    @Bind(R.id.lndconditontext)
    TextView lnditemcondition;
    //condition reference
    @Bind(R.id.conditionnew)
    TextView conditionnew;

    //included layout shipping
    @Bind(R.id.actualcost1)
    CheckBox ActualCost1;
    @Bind(R.id.fixedcost1)
    CheckBox FixedCost1;
    @Bind(R.id.actualcost2)
    CheckBox ActualCost2;
    @Bind(R.id.fixedcost2)
    CheckBox FixedCost2;

    @Bind(R.id.chargefixedcost)
    LinearLayout chargefixedcost;
    @Bind(R.id.chargeactualcost)
    LinearLayout chargeactualcost;

    @Bind(R.id.chargefixedcostinternational)
    LinearLayout chargefixedcostinternaltional;
    @Bind(R.id.chargeactualcostinternational)
    LinearLayout chargeactualcostinternational;

    //for shipping national fixed cost
    @Bind(R.id.nationalfixedcostservicespinner)
    Spinner nationalfixedcostservicespinner;
    @Bind(R.id.nationalfixedcostedittext)
    EditText nationalfixedcostinputbox;
    @Bind(R.id.nationfixedcostfreeshipping)
    CheckBox nationalfixedcostfreeshipping;

    //for shipping national actual cost
    @Bind(R.id.nationalactualcostservicespinner)
    Spinner nationalactualcostservicespinner;

    @Bind(R.id.nationalactualweightpackagespinner)
    Spinner nationalactualweightpackagespinner;
    @Bind(R.id.nationalactualcostfressshipping)
    CheckBox nationalactualcostfressshipping;
    @Bind(R.id.nationalactualcostlength)
    EditText nationalactualcostlength;
    @Bind(R.id.nationalactualcostwidth)
    EditText nationalactualcostwidth;
    @Bind(R.id.nationalactualcostheight)
    EditText nationalactualcostheight;
    String querypart1 = "";
    String querypart2 = "";

    //end here

    //for shipping international actualcost

    @Bind(R.id.internationalactualcostservicespinner)
    Spinner internationalactualcostservicespinner;

    @Bind(R.id.internationalweightpackagespinner)
    Spinner internationalweightpackagespinner;
    @Bind(R.id.internationalactualcostfreeshipping)
    CheckBox internationalactualcostfreeshipping;
    @Bind(R.id.internationalactualcostnointernationshipping)
    CheckBox internationalactualcostnointernationshipping;

    @Bind(R.id.internationalactualcostlength)
    EditText internationalactualcostlength;
    @Bind(R.id.internationalactualcostwidth)
    EditText internationalactualcostwidth;
    @Bind(R.id.internationalactualcostheight)
    EditText internationalactualcostheight;

    //for shipping international fixedcost
    //for shipping national fixed cost
    @Bind(R.id.internationalfixedcostservicespinner)
    Spinner internationalfixedcostservicespinner;
    @Bind(R.id.internationalfixedcostedittext)
    EditText internationalfixedcostedittext;
    @Bind(R.id.internationalfixedcostnointernationshipping)
    CheckBox internationalfixedcostnointernationshipping;
    @Bind(R.id.internationalfixedcostfreeshipping)
    CheckBox internationalfixedcostfreeshipping;



    int typehandbag = 0;
    int condition = 0;
    String sizetype = "";
    String colortype = "";
    String[] links = {"", "", "", ""};
    PopupWindow popupWindow;
    String filename[] = {"","","",""};
    InstructionDialogs lndcommistiondialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbags_post_page);
        //intialiaing dialog
        lndcommistiondialog= new InstructionDialogs(this);


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


//handbagtype listener
        for (int i = 0; i < handbagtype.size(); i++) {
            handbagtype.get(i).setOnClickListener(this);
        }

        //handbag size listener
        for (int i = 0; i < handbagsize.size(); i++) {
            handbagsize.get(i).setOnClickListener(this);
        }

        //color listener
        for (int i = 0; i < color.size(); i++) {
            color.get(i).setOnClickListener(this);
        }

        //condtion  events
        conditionnew.setOnClickListener(this);


        //condition spinner selection events
        conditionspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if (pos > 0) {


                    ((TextView) parent.getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                    lnditemcondition.setText(ConstantValues.conditiondesciptions[pos]);

                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#be4d66"));
                    condition = pos;
                    conditionnew.setBackgroundColor(Color.parseColor("#1d1f21"));
                } else {
                    ((TextView) parent.getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                    condition = pos;
                    lnditemcondition.setText(ConstantValues.conditiondesciptions[pos]);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

        ActualCost1.setOnClickListener(this);
        ActualCost2.setOnClickListener(this);
        FixedCost2.setOnClickListener(this);
        FixedCost1.setOnClickListener(this);


        setupEmoji();

    }

    @Override
    protected void onResume() {
        super.onResume();
        for (Map.Entry<String, CameraData> entry : CameraReviewFragment.urls.entrySet()) {
            //    System.out.println(entry.getKey());
            CameraData cd = entry.getValue();
            int value = Integer.parseInt(entry.getKey()) - 1;
            filename[value]= cd.getFilename();
            new AsyncTaskLoadImage(images.get(value), value).execute(cd.getImageurl());


        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void back(View v) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //events for size
            case R.id.size1:
                sizetype = "s";
                changeHandbagSize((TextView) v);
                break;
            case R.id.size2:
                sizetype = "m";
                changeHandbagSize((TextView) v);
                break;
            case R.id.size3:
                sizetype = "l";
                changeHandbagSize((TextView) v);
                break;

//for handbag type events
            case R.id.purses:
                typehandbag = 1;
                changeHandbagType((TextView) v);
                break;
            case R.id.clutches:
                typehandbag = 2;
                changeHandbagType((TextView) v);

                break;
//color events
            case R.id.color1:
                colortype = "black";
                changeHandbagColor((TextView) v);
                break;
            case R.id.color2:
                colortype = "silver";
                changeHandbagColor((TextView) v);

                break;
            case R.id.color3:
                colortype = "orange";
                changeHandbagColor((TextView) v);

                break;
            case R.id.color4:
                colortype = "white";
                changeHandbagColor((TextView) v);
                break;
            case R.id.color5:
                colortype = "gold";
                changeHandbagColor((TextView) v);

                break;
            case R.id.color6:
                colortype = "brown";
                changeHandbagColor((TextView) v);


                break;
            case R.id.color7:
                colortype = "red";
                changeHandbagColor((TextView) v);

                break;
            case R.id.color8:
                colortype = "purple";
                changeHandbagColor((TextView) v);
                break;
            case R.id.color9:
                colortype = "nude";
                changeHandbagColor((TextView) v);
                break;
            case R.id.color10:
                colortype = "blue";
                changeHandbagColor((TextView) v);
                break;
            case R.id.color11:
                colortype = "yellow";
                changeHandbagColor((TextView) v);

                break;
            case R.id.color12:
                colortype = "gray";
                changeHandbagColor((TextView) v);
                break;
            case R.id.color13:
                colortype = "green";
                changeHandbagColor((TextView) v);

                break;
            case R.id.color14:
                colortype = "pink";
                changeHandbagColor((TextView) v);
                break;
            case R.id.color15:
                colortype = "pattern";
                changeHandbagColor((TextView) v);
                break;

            case R.id.color16:
                colortype = "all";
                changeHandbagColor((TextView) v);
                break;
            // condition events
            case R.id.conditionnew:
                conditionnew.setBackgroundColor(Color.parseColor("#be4d66"));
                condition = 11;
                conditionspinner.setSelection(0);
                lnditemcondition.setText(ConstantValues.conditiondesciptions[11]);

                break;
            case R.id.actualcost1:
                unselectactualPrice();
                ((CheckBox) v).setChecked(true);
                ((CheckBox) v).setTextColor(Color.parseColor("#ffffff"));
                chargeactualcost.setVisibility(View.VISIBLE);
                chargefixedcost.setVisibility(View.GONE);
                break;
            case R.id.actualcost2:
                unselectfixedPrice();
                ((CheckBox) v).setChecked(true);
                ((CheckBox) v).setTextColor(Color.parseColor("#ffffff"));
                chargeactualcostinternational.setVisibility(View.VISIBLE);
                chargefixedcostinternaltional.setVisibility(View.GONE);
                break;
            case R.id.fixedcost1:

                unselectactualPrice();
                ((CheckBox) v).setChecked(true);
                ((CheckBox) v).setTextColor(Color.parseColor("#ffffff"));
                chargeactualcost.setVisibility(View.GONE);
                chargefixedcost.setVisibility(View.VISIBLE);

                break;
            case R.id.fixedcost2:
                unselectfixedPrice();
                ((CheckBox) v).setChecked(true);
                ((CheckBox) v).setTextColor(Color.parseColor("#ffffff"));
                chargeactualcostinternational.setVisibility(View.GONE);
                chargefixedcostinternaltional.setVisibility(View.VISIBLE);

                break;

        }

    }


    private void changeHandbagType(TextView txtview) {
        handbagtype.get(0).setBackgroundColor(Color.parseColor("#1d1f21"));
        handbagtype.get(1).setBackgroundColor(Color.parseColor("#1d1f21"));

        txtview.setBackgroundColor(Color.parseColor("#be4d66"));

    }

    private void changeHandbagColor(TextView txtview) {
        for (int i = 0; i < color.size() - 1; i++)
            color.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
        txtview.setBackgroundColor(Color.parseColor("#be4d66"));

    }

    private void changeHandbagSize(TextView txtview) {
        handbagsize.get(0).setBackgroundColor(Color.parseColor("#1d1f21"));
        handbagsize.get(1).setBackgroundColor(Color.parseColor("#1d1f21"));
        handbagsize.get(2).setBackgroundColor(Color.parseColor("#1d1f21"));

        txtview.setBackgroundColor(Color.parseColor("#be4d66"));

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
            links[1] = Base64.encodeToString(byte_arr, 0);

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
    public void priceins(View v) {
        if(!lndcommistiondialog.popupWindow.isShowing())
            lndcommistiondialog.show(v);
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

    public void done(View v) {

        ArrayList<String> hashtags = new ArrayList<>();
        ArrayList<Integer> usermentions = new ArrayList<>();
        String querypart1 = "insert into lnd_table_how_to_ship(";
        String querypart2 = " values(";

        int pw = 0, pn = 0;
        try {
            pw = Integer.parseInt(pricewas.getText().toString());
            pn = Integer.parseInt(pricenow.getText().toString());

        } catch (Exception ex) {
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
        } else if (typehandbag == 0) {
            Toast.makeText(this, "select purse type", Toast.LENGTH_SHORT).show();

            return;
        } else if (sizetype.length() == 0) {
            Toast.makeText(this, "select  purse size", Toast.LENGTH_SHORT).show();
            return;
        } else if (colortype.length() == 0) {
            Toast.makeText(this, "select purse color", Toast.LENGTH_SHORT).show();
            return;
        } else if (condition == 0) {
            Toast.makeText(this, "select purse condition", Toast.LENGTH_SHORT).show();
            return;

        }
        if(!validateShipping())
            return;


        try {
            //image1 json
            JSONObject image1 = new JSONObject();
            image1.put("imagename", filename[0]);
            image1.put("imageurl", links[0]);

            //image2 json
            JSONObject image2 = new JSONObject();
            image2.put("imagename", filename[1]);
            image2.put("imageurl", links[1]);

            //image3 json
            JSONObject image3 = new JSONObject();
            image3.put("imagename", filename[2]);
            image3.put("imageurl", links[2]);

            //image4 json
            JSONObject image4 = new JSONObject();
            image4.put("imagename", filename[3]);
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
            mainObj.put("condition", condition);
            mainObj.put("brand", brand.getText().toString());
            mainObj.put("categorytype", 2);
            mainObj.put("pursesize", sizetype);
            mainObj.put("pursetype", typehandbag);
            mainObj.put("color", colortype);
            mainObj.put("images", imagesarray);
            mainObj.put("description", desc.getText().toString());
            mainObj.put("pricenow", pricenow.getText().toString());
            mainObj.put("pricewas", pricewas.getText().toString());
            mainObj.put("datetime",SingleTon.getCurrentTimeStamp());
            //end of shipping query here
            querypart1 = querypart1 + ") ";
            querypart2 = querypart2 + ") ";
            mainObj.put("shippingquery",querypart1+querypart2);

            //get hashtag and user mentions
            String text = desc.getText().toString();
            String regexPattern1 = "(#\\w+)";
            String regexPattern2 = "(@\\w+)";
            //get all hashtags
            Pattern p1 = Pattern.compile(regexPattern1);
            Matcher m1 = p1.matcher(text);
            while (m1.find()) {
                String hashtag = m1.group(1).substring(1);
                hashtags.add(hashtag);
            }
            JSONArray hashtagArray = new JSONArray(hashtags);
            //get all username mentions
            p1 = Pattern.compile(regexPattern2);
            mainObj.put("hashtags", hashtagArray);
            m1 = p1.matcher(text);
            while (m1.find()) {
                String usermention = m1.group(1).substring(1);
                if (LndTextWatcher.users.containsKey(usermention))
                    usermentions.add(LndTextWatcher.users.get(usermention));
            }

            JSONArray usermentionArray = new JSONArray(usermentions);
            mainObj.put("usermentions", usermentionArray);
            //postPurse(mainObj.toString());

               Log.e("json", mainObj.toString());
        } catch (Exception ex) {
            Log.e("json error", ex.getMessage() + "");
        }
    }

    public void postPurse(final String data) {


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("wait posting dress...");
        pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/lndpost.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                //    Log.e("responsedata", response.toString());
                try {
                    JSONObject jobj = new JSONObject(response);
                    if (jobj.getBoolean("status")) {
                        Toast.makeText(HandBagsPostPrivate.this, jobj.getString("message") + "", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(HandBagsPostPrivate.this, jobj.getString("message") + "", Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException ex) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Log.e("error", error.getMessage() + "");

                Toast.makeText(HandBagsPostPrivate.this, "Purse not posted please try again", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("data", data);
                params.put("rqid", "2");

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
    public void unselectactualPrice() {
        ActualCost1.setChecked(false);
        FixedCost1.setChecked(false);
        ActualCost1.setTextColor(Color.parseColor("#ffffff"));
        FixedCost1.setTextColor(Color.parseColor("#ffffff"));

    }

    public void unselectfixedPrice() {
        FixedCost2.setChecked(false);
        ActualCost2.setChecked(false);
        FixedCost2.setTextColor(Color.parseColor("#ffffff"));
        ActualCost2.setTextColor(Color.parseColor("#ffffff"));

    }

    public boolean validateShipping() {
        //for shipping national fixed cost
        if (FixedCost1.isChecked() && !(nationalfixedcostfreeshipping.isChecked())) {
            if (nationalfixedcostservicespinner.getSelectedItemPosition() == 0) {

                Toast.makeText(this, "select national shipping service", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                querypart1 = querypart1 + "service_type_national";
                querypart2 = querypart2 + ",\"" + nationalfixedcostservicespinner.getSelectedItem() + "\"";
            }
            if (nationalfixedcostinputbox.getText().length() == 0) {
                nationalfixedcostinputbox.requestFocus();
                nationalfixedcostinputbox.setError("enter charge fixed cost");
                return false;
            } else {
                querypart1 = querypart1 + ",charge_cost_national";
                querypart2 = querypart2 + "," + nationalfixedcostinputbox.getText();
            }
        }
        //for shipping national actual cost
        if (ActualCost1.isChecked() && !(nationalactualcostfressshipping.isChecked())) {

            if (nationalactualweightpackagespinner.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "select weight of packaged item", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                querypart1 = querypart1 + "package_weight_national";
                querypart2 = querypart2 + "\"" + nationalactualweightpackagespinner.getSelectedItem() + "\"";
            }
            if (nationalactualcostlength.getText().length() == 0) {
                nationalactualcostlength.requestFocus();
                nationalactualcostlength.setError("Length ?");
                return false;
            } else {
                querypart1 = querypart1 + ",length_national";
                querypart2 = querypart2 + "," + nationalactualcostlength.getText();

            }
            if (nationalactualcostwidth.getText().length() == 0) {
                nationalactualcostwidth.requestFocus();
                nationalactualcostwidth.setError("Width ?");
                return false;
            } else {
                querypart1 = querypart1 + ",width_national";
                querypart2 = querypart2 + "," + nationalactualcostwidth.getText();

            }
            if (nationalactualcostheight.getText().length() == 0) {
                nationalactualcostheight.requestFocus();
                nationalactualcostheight.setError("Height ?");
                return false;
            } else {
                querypart1 = querypart1 + ",height_national";
                querypart2 = querypart2 + "," + nationalactualcostheight.getText();

            }
            if (nationalactualcostservicespinner.getSelectedItemPosition() == 0) {

                Toast.makeText(this, "select national shipping service", Toast.LENGTH_SHORT).show();
                return false;
            } else
                querypart1 = querypart1 + ",service_type_national";
            querypart2 = querypart2 + ",\"" + nationalactualcostservicespinner.getSelectedItem() + "\"";

        }

        //for shipping international fixed cost
        if (FixedCost2.isChecked() && !((internationalfixedcostfreeshipping.isChecked() || internationalfixedcostnointernationshipping.isChecked()))) {
            if (internationalfixedcostservicespinner.getSelectedItemPosition() == 0) {

                Toast.makeText(this, "select international shipping service", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                querypart1 = querypart1 + ",service_type_international";
                querypart2 = querypart2 + ",\"" + nationalfixedcostservicespinner.getSelectedItem() + "\"";
            }
            if (internationalfixedcostedittext.getText().length() == 0) {
                internationalfixedcostedittext.requestFocus();
                internationalfixedcostedittext.setError("enter charge fixed cost");
                return false;
            } else {
                querypart1 = querypart1 + ",charge_cost_international";
                querypart2 = querypart2 + "," + nationalfixedcostinputbox.getText();
            }
        }
        //for shipping international actual cost
        if (ActualCost2.isChecked() && !((internationalactualcostfreeshipping.isChecked() || internationalactualcostnointernationshipping.isChecked()))) {

            if (internationalweightpackagespinner.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "select weight of packaged item", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                querypart1 = querypart1 + ",package_weight_international";
                querypart2 = querypart2 + ",\"" + internationalweightpackagespinner.getSelectedItem() + "\"";

            }
            if (internationalactualcostlength.getText().length() == 0) {
                internationalactualcostlength.requestFocus();
                internationalactualcostlength.setError("Length ?");
                return false;
            } else {
                querypart1 = querypart1 + ",length_international";
                querypart2 = querypart2 + "," + internationalactualcostlength.getText();
            }
            if (internationalactualcostwidth.getText().length() == 0) {
                internationalactualcostwidth.requestFocus();
                internationalactualcostwidth.setError("Width ?");
                return false;
            } else {
                querypart1 = querypart1 + ",width_international";
                querypart2 = querypart2 + "," + internationalactualcostwidth.getText();
            }
            if (internationalactualcostheight.getText().length() == 0) {
                internationalactualcostheight.requestFocus();
                internationalactualcostheight.setError("Height ?");
                return false;
            } else {
                querypart1 = querypart1 + ",height_international";
                querypart2 = querypart2 + "," + internationalactualcostheight.getText();
            }
            if (internationalactualcostservicespinner.getSelectedItemPosition() == 0) {

                Toast.makeText(this, "select international shipping service", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                querypart1 = querypart1 + ",service_type_international";
                querypart2 = querypart2 + ",\"" + internationalactualcostservicespinner.getSelectedItem() + "\"";

            }
        }
        //national free shipping for fixed cost and actual cost
        if (nationalactualcostfressshipping.isChecked()) {
            querypart1 = querypart1 + "isfree_shipping_national";
            querypart2 = querypart2 + "1";
        } else if (nationalfixedcostfreeshipping.isChecked()) {
            querypart1 = querypart1 + "isfree_shipping_national";
            querypart2 = querypart2 + "1";

        }
        if (ActualCost2.isChecked() || FixedCost2.isChecked()) {

            //international free shipping for fixed cost and actual cost

            if (internationalfixedcostfreeshipping.isChecked()) {
                querypart1 = querypart1 + ",isfree_shipping_international";
                querypart2 = querypart2 + ",1";
            } else if (internationalactualcostfreeshipping.isChecked()) {
                querypart1 = querypart1 + ",isfree_shipping_international";
                querypart2 = querypart2 + ",1";

            }
            if (internationalfixedcostnointernationshipping.isChecked()) {
                querypart1 = querypart1 + ",isno_shipping_international";
                querypart2 = querypart2 + ",1";
            } else if (internationalactualcostnointernationshipping.isChecked()) {
                querypart1 = querypart1 + ",isno_shipping_international";
                querypart2 = querypart2 + ",1";

            }

        }

        return true;
    }

}
