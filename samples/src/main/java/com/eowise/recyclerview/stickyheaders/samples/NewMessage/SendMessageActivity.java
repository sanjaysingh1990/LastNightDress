package com.eowise.recyclerview.stickyheaders.samples.NewMessage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.Home_List_Data;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.adapters.SendMsgListAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.Chat_Banner_Data;
import com.eowise.recyclerview.stickyheaders.samples.data.MessageData;
import com.eowise.recyclerview.stickyheaders.samples.data.UserType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

public class SendMessageActivity extends AppCompatActivity {
    @Bind(R.id.list)
    RecyclerView chatRecyclerView;
    @Bind(R.id.smiley)
    ImageButton emojiButton;

    @Bind(R.id.uname)
    TextView heading;
    private List<MessageData> data = new ArrayList<MessageData>();
    private SendMsgListAdapter recyclerAdapter;
    @Bind(R.id.commentbox)
    EmojiconEditText cmntbox;
    private Bundle extra;

    int pos = -1;
    private static final int CAMERA_PIC_REQUEST = 1337;
    public static Home_List_Data chatbanner;
    private boolean istop = false;
    private boolean firsttime = true;
    private int skipcount = 0;
    int visibleItemCount, totalItemCount, firstVisibleItemIndex, previousTotal;
    boolean loading = true;
    private boolean loadmore = false;
    private boolean isrunning = false;
    private int lastscrollposition = 0;
    private boolean once = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        //initialize butter knife
        ButterKnife.bind(this);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(manager);
        recyclerAdapter = new SendMsgListAdapter(this, data);
        chatRecyclerView.setAdapter(recyclerAdapter);
        heading.setTypeface(SingleTon.hfont);

        //data from previous page
        extra = getIntent().getExtras();
        if (extra != null) {
            chatbanner = (Home_List_Data) extra.get("bannerdata");

            pos = extra.getInt("pos");


            heading.setText(Capitalize.capitalize(chatbanner.getUname()));
        }
        final View rootView = findViewById(R.id.rootview);

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
                if (cmntbox == null || emojicon == null) {
                    return;
                }

                int start = cmntbox.getSelectionStart();
                int end = cmntbox.getSelectionEnd();
                if (start < 0) {
                    cmntbox.append(emojicon.getEmoji());
                } else {
                    cmntbox.getText().replace(Math.min(start, end),
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
                cmntbox.dispatchKeyEvent(event);
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
                        cmntbox.setFocusableInTouchMode(true);
                        cmntbox.requestFocus();
                        popup.showAtBottomPending();
                        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(cmntbox, InputMethodManager.SHOW_IMPLICIT);
                        changeEmojiKeyboardIcon(emojiButton, R.drawable.keyboard);
                    }
                }

                //If popup is showing, simply dismiss it to show the undelying text keyboard
                else {
                    popup.dismiss();
                }
            }
        });


        chatRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = manager.getItemCount();
                firstVisibleItemIndex = manager.findFirstVisibleItemPosition();

                //synchronizew loading state when item count changes
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading)
                    if ((totalItemCount - visibleItemCount) <= firstVisibleItemIndex) {
                        // Loading NOT in progress and end of list has been reached
                        // also triggered if not enough items to fill the screen
                        // if you start loading
                        //Toast.makeText(LndComments.this,"bottom",Toast.LENGTH_SHORT).show();

                        //loading = true;
                    } else if (firstVisibleItemIndex == 0) {
                        // top of list reached
                        // if you start loading
                        loadmore = true;
                        loading = true;
                        if (!isrunning) {
                            isrunning = true;
                            lastscrollposition = data.size();
                            getData(chatbanner.getUserid());

                        }
                    }
            }
        });


        getData(chatbanner.getUserid());

    }

    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId) {
        iconToBeChanged.setImageResource(drawableResourceId);
    }

    private int yesterdayMsg(Date lasttimeran) {


        Date now = new Date();
        long duration = now.getTime() - lasttimeran.getTime();

        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
        // int days= (int) (diffInHours/24);
        if (diffInHours < 24)
            return 1;
        else if (diffInHours > 24 && diffInHours < 48)
            return 2;
        else
            return 3;
    }

    public void getData(final String senderid) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        // pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_INBOXOPERATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pDialog.dismiss();

                } catch (Exception ex) {

                }
                String uname = SingleTon.pref.getString("uname", "");
                Log.e("json", response.toString());
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jo = jarray.getJSONObject(i);
                        MessageData md = new MessageData();
                        md.setMessage(jo.getString("msg"));
                        md.setUname(jo.getString("uname"));
                        md.setTime(jo.getString("date_time"));
                        md.setProfilepic(jo.getString("profile_pic"));
                        md.setCurrenttimestamp(jo.getString("date_time"));
                        if (jo.isNull("brand_name")) {
                            if (jo.getString("uname").compareTo(uname) == 0) {
                                md.setUserType(UserType.SELF);
                            } else {
                                md.setUserType(UserType.OTHER);
                            }
                        } else {
                            if (jo.getString("uname").compareTo(uname) == 0)
                                md.setSellername(chatbanner.getUname());
                            md.setBrandname(jo.getString("brand_name"));
                            md.setImageurl(jo.getString("image_url"));
                            md.setSize(jo.getString("size"));
                            md.setPrice(jo.getString("price_now"));
                            md.setUserType(UserType.BANNER);
                        }
                        //formatting date and time
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date testDate = null;
                        try {
                            testDate = sdf.parse(md.getTime());
                        } catch (Exception ex) {
                            Log.e("error", ex.getMessage());
                        }
                        //           SimpleDateFormat formatter = new SimpleDateFormat("MMM dd,yyyy hh:mm a");


                        int val = yesterdayMsg(testDate);
                        if (val == 1) {
                            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");

                            String newFormat = formatter.format(testDate);
                            if (newFormat.startsWith("0")) {
                                md.setTime(newFormat.toUpperCase().substring(1));
                            } else
                                md.setTime(newFormat.toUpperCase());
                        } else if (val == 2) {
                            md.setTime("yesterday");
                        } else {
                            Format formatter = new SimpleDateFormat("MM-dd-yyyy");

                            String datestring = formatter.format(testDate.getTime());
                            md.setTime(datestring);
                        }
                       /* if (loadmore)
                            data.add(0, md);
                        else
                            data.add(md);*/
                        data.add(0, md);
                    }
                    if (data.size() == 0 && firsttime) {
                        Toast.makeText(SendMessageActivity.this, "No conversation yet !", Toast.LENGTH_LONG).show();
                        istop = false;
                    } else if (data.size() == 0)
                        istop = false;
                    else {
                        firsttime = false;

                    }
                    skipcount = data.size();

                    if (extra != null && !loadmore) {
//banner message
                        //check
                        if (extra.getBoolean("fromhome", false)) {
                            MessageData md1 = new MessageData();
                            md1.setUserType(UserType.BANNER);
                            data.add(md1);
                        }
                    }

                    recyclerAdapter.notifyDataSetChanged();
                    if (!loadmore) {
                        chatRecyclerView.scrollToPosition(data.size() - 1);

                    } else {
                        chatRecyclerView.scrollToPosition(data.size() - lastscrollposition);

                    }

                    if (jarray.length() < 25) {
                        loading = true;
                        isrunning = true;
                    } else {
                        loading = false;
                        isrunning = false;
                    }


                } catch (Exception ex) {
                    Log.e("json parsing error", "" + ex.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Log.e("response", error.getMessage() + "");

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "3");
                params.put("receiver_id", SingleTon.pref.getString("user_id", ""));
                params.put("sender_id", senderid);
                params.put("skipdata", skipcount + "");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

    @Override
    public void onBackPressed() {
        if (data.size() != 0) {
            Intent intent = new Intent();
            MessageData md = data.get(data.size() - 1);
            intent.putExtra("message", md.getMessage());
            intent.putExtra("pos", pos);
            intent.putExtra("time", md.getCurrenttimestamp());
            setResult(200, intent);
            finish();//finishing activity
        }
        finish();
    }

    public void back(View v) {
        onBackPressed();
    }

    public void send(View v) {
        String message = "";
        message = cmntbox.getText().toString();
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        String time1 = sdf.format(dt);


        String uname = SingleTon.pref.getString("uname", "uname");
        //Log.e("check",cmntxt+","+uname);
        if (message.length() == 0)
            return;
        MessageData md = new MessageData();
        md.setMessage(message);
        md.setUname(uname);
        md.setProfilepic(SingleTon.pref.getString("imageurl", ""));
        md.setUserType(UserType.SELF);
        //check for start with zero

        if (time1.startsWith("0")) {
            md.setTime(time1.toUpperCase().substring(1));
        } else
            md.setTime(time1.toUpperCase());
        md.setCurrenttimestamp(SingleTon.getCurrentTimeStamp());
        data.add(md);
        recyclerAdapter.notifyDataSetChanged();
        chatRecyclerView.scrollToPosition(data.size() - 1);

        cmntbox.setText("");

        try {
            JSONObject msg = new JSONObject();
            msg.put("message", message);
            msg.put("date_time", SingleTon.getCurrentTimeStamp());
            msg.put("sender_id", SingleTon.pref.getString("user_id", ""));
            msg.put("receiver_id", chatbanner.getUserid());
            if (extra != null && once) {
                once = false;
                msg.put("banner", 1);
                msg.put("post_id", chatbanner.getPost_id());

            } else
                msg.put("banner", 0);


            sendMessage(msg.toString());
        } catch (JSONException ex) {

        }

    }

    public void sendMessage(final String data) {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_INBOXOPERATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Log.e("json",response+"");
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    //  Toast.makeText(SendMessageActivity.this,response,Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("response", error.getMessage() + "");

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "2");
                params.put("data", data);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

    public void camera(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {


        if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == RESULT_OK) {

                Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 85, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                //end here


                Date dt = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                String time1 = sdf.format(dt);

                String uname = SingleTon.pref.getString("uname", "uname");
                //Log.e("check",cmntxt+","+uname);

                MessageData md = new MessageData();

                md.setUname(uname);
                md.setBase64_imgage_url(encoded);
                md.setProfilepic(SingleTon.pref.getString("imageurl", ""));
                md.setUserType(UserType.SELF_IMAGE);
                //check for start with zero
                if (time1.startsWith("0")) {
                    md.setTime(time1.toUpperCase().substring(1));
                } else
                    md.setTime(time1.toUpperCase());
                md.setCurrenttimestamp(SingleTon.getCurrentTimeStamp());

                data.add(md);
                recyclerAdapter.notifyDataSetChanged();


               /* try {
                    JSONObject msg = new JSONObject();
                    msg.put("message", message);
                    msg.put("date_time", SingleTon.getCurrentTimeStamp());
                    msg.put("sender_id", SingleTon.pref.getString("user_id", ""));
                    msg.put("receiver_id", senderid);


                    sendMessage(msg.toString());
                } catch (JSONException ex) {
*
                }*/


            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    public void changeStatus(final int id) {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/inboxope.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status")) {
                        if (Main_TabHost.message.getText().toString().compareToIgnoreCase("0") == 0)
                            Main_TabHost.popupWindow.dismiss();
                        else {
                            int val = Integer.parseInt(Main_TabHost.message.getText().toString());
                            Main_TabHost.message.setText((val - 1) + "");
                            if (Main_TabHost.message.getText().toString().compareToIgnoreCase("0") == 0)
                                Main_TabHost.popupWindow.dismiss();

                        }
                    }
                    //    Toast.makeText(SendMessageActivity.this,response,Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("response", error.getMessage() + "");

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "10");
                params.put("msgid", id + "");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

}
