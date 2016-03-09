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
import com.eowise.recyclerview.stickyheaders.samples.PostDataShop.Lnd_Post_Instruction;
import com.eowise.recyclerview.stickyheaders.samples.R;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

public class ShoesPostPrivate extends AppCompatActivity implements View.OnClickListener {

    @Bind({R.id.size1, R.id.size2, R.id.size3, R.id.size4, R.id.size5, R.id.size6, R.id.size7, R.id.size8, R.id.size9, R.id.size10, R.id.size11, R.id.size12, R.id.size13, R.id.size14, R.id.size15})
    List<TextView> shoesize;
    @Bind({R.id.color1, R.id.color2, R.id.color3, R.id.color4, R.id.color5, R.id.color6, R.id.color7, R.id.color8, R.id.color9, R.id.color10, R.id.color11, R.id.color12, R.id.color13, R.id.color14, R.id.color15})
    List<TextView> color;
    @Bind({R.id.flats, R.id.pumps, R.id.platforms, R.id.boots, R.id.wedges, R.id.bridal, R.id.sandals})
    List<TextView> shoestype;
    @Bind({R.id.image1, R.id.image2, R.id.image3, R.id.image4})
    List<ImageView> images;

    @Bind(R.id.conditionnew)
    TextView conditionnew;


    @Bind(R.id.heading)
    TextView heading;


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
    @Bind(R.id.rootview)
    View rootView;

    @Bind(R.id.conditionspinner)
    Spinner conditionspinner;


    String sizelist = "";
    int shoetype = 0;

    String colortype = "";

    int condition = 0;
    String[] links = {"", "", "", ""};
    ArrayList<String> filename = new ArrayList<>();

    PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoes_post_page);
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
        heading.setTypeface(ImageLoaderImage.hfont);


        //shoetype listener
        for (int i = 0; i < shoestype.size(); i++) {
            shoestype.get(i).setOnClickListener(this);
        }

        //shoe size listener
        for (int i = 0; i < shoesize.size(); i++) {
            shoesize.get(i).setOnClickListener(this);
        }


        //color listener
        for (int i = 0; i < color.size(); i++) {
            color.get(i).setOnClickListener(this);
        }

        //condtion  events
        conditionnew.setOnClickListener(this);

        conditionspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if (pos > 0) {

                    condition = pos;
                    ((TextView) parent.getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);

                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#be4d66"));
                    conditionnew.setBackgroundColor(Color.parseColor("#1d1f21"));
                } else {
                    ((TextView) parent.getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                    condition = pos;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//  adding defualt values
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
        switch (v.getId()) {

            //events for size
            case R.id.size1:
                sizelist = "5";
                changeShoeSize((TextView) v);
                break;
            case R.id.size2:
                sizelist = "5.5";
                changeShoeSize((TextView) v);
                break;
            case R.id.size3:
                sizelist = "6";
                changeShoeSize((TextView) v);
                break;
            case R.id.size4:
                sizelist = "6.5";
                changeShoeSize((TextView) v);
                break;
            case R.id.size5:
                sizelist = "7";
                changeShoeSize((TextView) v);
                break;
            case R.id.size6:
                sizelist = "7.5";
                changeShoeSize((TextView) v);
                break;
            case R.id.size7:
                sizelist = "8";
                changeShoeSize((TextView) v);
                break;
            case R.id.size8:
                sizelist = "8.5";
                changeShoeSize((TextView) v);
                break;
            case R.id.size9:
                sizelist = "9";
                changeShoeSize((TextView) v);
                break;
            case R.id.size10:
                sizelist = "9.5";
                changeShoeSize((TextView) v);

                break;
            case R.id.size11:
                sizelist = "10";
                changeShoeSize((TextView) v);
                break;
            case R.id.size12:
                sizelist = "10.5";
                changeShoeSize((TextView) v);
                break;
            case R.id.size13:
                sizelist = "11";
                changeShoeSize((TextView) v);
                break;
            case R.id.size14:
                sizelist = "11.5";
                changeShoeSize((TextView) v);

                break;
            case R.id.size15:
                sizelist = "12";
                changeShoeSize((TextView) v);
                break;


            //for shoetype events
            case R.id.flats:
                shoetype = 1;
                changeShoeType((TextView) v);
                break;
            case R.id.pumps:
                shoetype = 2;
                changeShoeType((TextView) v);
                break;
            case R.id.platforms:
                shoetype = 3;
                changeShoeType((TextView) v);
                break;
            case R.id.boots:
                shoetype = 4;
                changeShoeType((TextView) v);
                break;
            case R.id.wedges:
                shoetype = 5;
                changeShoeType((TextView) v);
                break;
            case R.id.bridal:
                shoetype = 6;
                changeShoeType((TextView) v);
                break;

            case R.id.sandals:
                shoetype = 7;
                changeShoeType((TextView) v);
                break;


            //color events
            case R.id.color1:
                colortype = "black";
                changeShoeColor((TextView) v);
                break;
            case R.id.color2:
                colortype = "silver";
                changeShoeColor((TextView) v);

                break;
            case R.id.color3:
                colortype = "orange";
                changeShoeColor((TextView) v);

                break;
            case R.id.color4:
                colortype = "white";
                changeShoeColor((TextView) v);
                break;
            case R.id.color5:
                colortype = "gold";
                changeShoeColor((TextView) v);

                break;
            case R.id.color6:
                colortype = "brown";
                changeShoeColor((TextView) v);


                break;
            case R.id.color7:
                colortype = "red";
                changeShoeColor((TextView) v);

                break;
            case R.id.color8:
                colortype = "purple";
                changeShoeColor((TextView) v);
                break;
            case R.id.color9:
                colortype = "nude";
                changeShoeColor((TextView) v);
                break;
            case R.id.color10:
                colortype = "blue";
                changeShoeColor((TextView) v);
                break;
            case R.id.color11:
                colortype = "yellow";
                changeShoeColor((TextView) v);

                break;
            case R.id.color12:
                colortype = "gray";
                changeShoeColor((TextView) v);
                break;
            case R.id.color13:
                colortype = "green";
                changeShoeColor((TextView) v);

                break;
            case R.id.color14:
                colortype = "pink";
                changeShoeColor((TextView) v);
                break;
            case R.id.color15:
                colortype = "pattern";
                changeShoeColor((TextView) v);
                break;

            case R.id.conditionnew:
                conditionnew.setBackgroundColor(Color.parseColor("#be4d66"));


                condition = 11;


        }
    }

    public void done(View v) {

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
        } else if (shoetype == 0) {
            Toast.makeText(this, "select shoe type", Toast.LENGTH_SHORT).show();
            return;
        } else if (sizelist.length() == 0) {
            Toast.makeText(this, "select dress size", Toast.LENGTH_SHORT).show();
            return;
        } else if (colortype.length() == 0) {
            Toast.makeText(this, "select shoe color", Toast.LENGTH_SHORT).show();
            return;
        } else if (condition == 0) {
            Toast.makeText(this, "select shoe condition", Toast.LENGTH_SHORT).show();
            return;

        }



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
            mainObj.put("user_id", userid);
            mainObj.put("condition", condition);
            mainObj.put("brand", brand.getText().toString());
            mainObj.put("categorytype", 3);

            mainObj.put("shoesize",sizelist);
            mainObj.put("shoetype",shoetype);


            mainObj.put("color",colortype);
            mainObj.put("images", imagesarray);
            mainObj.put("description", desc.getText().toString());
            mainObj.put("pricenow", pricenow.getText().toString());
            mainObj.put("pricewas", pricewas.getText().toString());
            mainObj.put("datetime", strDate);
           // uploadImage(mainObj.toString());

            Log.e("json", mainObj.toString());
        } catch (Exception ex) {
            Log.e("jsonex", ex.getMessage()+"");
        }
    }

    public void postShoe(final String data) {

        Log.e("test", "check");
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("wait posting shoe...");
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
                        Toast.makeText(ShoesPostPrivate.this, jobj.getString("message") + "", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(ShoesPostPrivate.this, jobj.getString("message") + "", Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException ex) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                //   Log.e("response error", error.getMessage() + "");

                Toast.makeText(ShoesPostPrivate.this, "Shoe not posted please try again", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("data", data);
                params.put("rqid", "3");

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

    private void changeShoeType(TextView txtview) {
        for (int i = 0; i < shoestype.size() - 1; i++)

            shoestype.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));

        txtview.setBackgroundColor(Color.parseColor("#be4d66"));

    }

    private void changeShoeColor(TextView txtview) {
        for (int i = 0; i < color.size() - 1; i++)

            color.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
        txtview.setBackgroundColor(Color.parseColor("#be4d66"));

    }

    private void changeShoeSize(TextView txtview)

    {
        for (int i = 0; i < shoesize.size() - 1; i++)
            shoesize.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
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
