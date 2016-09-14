package com.eowise.recyclerview.stickyheaders.samples.NewMessage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.eowise.recyclerview.stickyheaders.samples.data.MessageData;
import com.eowise.recyclerview.stickyheaders.samples.data.UserMessageType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SendMessageActivity extends AppCompatActivity {
    @Bind(R.id.list)
    RecyclerView chatRecyclerView;
    @Bind(R.id.smiley)
    ImageButton emojiButton;
    @Bind(R.id.commentbox)
    EmojiconEditText cmntbox;
    @Bind(R.id.uname)
    TextView heading;
    @Bind(R.id.typinglastseen)


    TextView mUsertype;
    private List<MessageData> data = new ArrayList<MessageData>();
    private SendMsgListAdapter recyclerAdapter;

    private Bundle extra;

    private int pos = -1;
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
    private Socket mSocket;
    private String msg_from = "", msg_to = "", u_name = "", image_url = "http://";
    private boolean mTyping = false;
    private Handler mTypingHandler = new Handler();
    private Boolean isConnected = true;
    private static final int TYPING_TIMER_LENGTH = 600;
    public static int GALLERY_INTENT_CALLED = 200;
    private boolean banneronce=true;
    //to open gallery intent
    @OnClick(R.id.share_gallery_image)
    public void gallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, GALLERY_INTENT_CALLED);
    }

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
            msg_to = chatbanner.getUserid();
            msg_from = SingleTon.pref.getString("user_id", "");
            u_name = SingleTon.pref.getString("uname", "");
            image_url = SingleTon.pref.getString("imageurl", "");
            pos = extra.getInt("pos", -1);


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


        cmntbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!mSocket.connected()) return;

                if (!mTyping) {
                    mTyping = true;

                    mSocket.emit("typing", msg_to);


                }

                mTypingHandler.removeCallbacks(onTypingTimeout);
                mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        //setting up socket
        SingleTon app = (SingleTon) getApplication();
        mSocket = app.getSocket();
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("new message", chatMessage);
        mSocket.on("new banner", onBanner);
        mSocket.on("user left", onUserLeft);
        mSocket.on("typing", onTyping);
        mSocket.on("stop typing", onStopTyping);
        mSocket.on("set online", onOnline);
        // mSocket.on("message read", msgread);
        mSocket.on("last seen", lastseen);

        mSocket.connect();


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

    private void scrollToBottom() {
        chatRecyclerView.scrollToPosition(data.size() - 1);
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
                        md.setUname(jo.getString("uname"));
                        md.setTime(jo.getString("date_time"));
                        md.setProfilepic(jo.getString("profile_pic"));
                        md.setShared_imgage_url(jo.getString("shared_image_url"));
                        md.setMessage(jo.getString("msg"));
                        md.setDatetime(getLocalTime(jo.getString("date_time")));
                        if (jo.isNull("brand_name")) {

                            if (jo.getString("uname").compareTo(uname) == 0) {
                                if (jo.getString("shared_image_url").length() == 0)

                                    md.setUserMessageType(UserMessageType.SELF);

                                else {
                                    md.setUserMessageType(UserMessageType.SELF_IMAGE_SERVER);
                                    md.setMessage("image");

                                }
                            } else {
                                if (jo.getString("shared_image_url").length() == 0)
                                    md.setUserMessageType(UserMessageType.OTHER);
                                else {
                                    md.setUserMessageType(UserMessageType.OTHER_IMAGE_SERVER);
                                    md.setMessage("image");
                                }
                            }
                        } else {
                            if (jo.getString("uname").compareTo(uname) == 0)
                                md.setSellername(chatbanner.getUname());
                            else
                                md.setSellername("You");

                            md.setBrandname(jo.getString("brand_name"));
                            md.setImageurl(jo.getString("image_url"));
                            md.setSize(jo.getString("size"));
                            md.setPrice(jo.getString("price_now"));
                            md.setUserMessageType(UserMessageType.BANNER);
                        }

                        Date testDate = getDate(md.getTime());
                        int val = yesterdayMsg(testDate);
                        if (val == 1) {

                            String newFormat = getTime(md.getTime());
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
                            md1.setUserMessageType(UserMessageType.BANNER);
                            md1.setImageurl(chatbanner.getImageurls().get(0));
                            md1.setSellername(chatbanner.getUname());
                            md1.setSize(chatbanner.getSize());
                            md1.setPrice(chatbanner.getPricenow());
                            md1.setPostid(chatbanner.getPost_id());
                            md1.setBrandname(chatbanner.getBrandname());
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
            intent.putExtra("time", md.getDatetime());
            intent.putExtra("uname",chatbanner.getUname());
            setResult(200, intent);
            finish();//finishing activity
        }
        finish();
    }

    public void back(View v) {
        onBackPressed();
    }

    public void send(View v) {
        //start sending message to socket
        if (!mSocket.connected()) return;

        mTyping = false;

        String message = cmntbox.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            cmntbox.requestFocus();
            return;
        }


        message = cmntbox.getText().toString();
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String time1 = sdf.format(dt);
        String uname = SingleTon.pref.getString("uname", "uname");
        MessageData md = new MessageData();
        md.setMessage(message);
        md.setUname(uname);
        md.setProfilepic(SingleTon.pref.getString("imageurl", ""));
        Log.e("image_url",md.getProfilepic());
        md.setUserMessageType(UserMessageType.SELF);
        //check for start with zero
        if (time1.startsWith("0")) {
            md.setTime(time1.toUpperCase().substring(1));
        } else
            md.setTime(time1.toUpperCase());
        md.setDatetime(SingleTon.getCurrentTimeStamp());
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


            //  sendMessage(msg.toString());
        } catch (JSONException ex) {

        }

        //send banner message
        try {
            if (extra != null && extra.getBoolean("fromhome", false)&&banneronce) {
                banneronce=false;
                JSONObject jobj = new JSONObject();
                jobj.put("msg_from", msg_from);
                jobj.put("msg_to", msg_to);
                jobj.put("post_id", chatbanner.getPost_id());
                mSocket.emit("new banner", jobj.toString());
            }

        } catch (Exception ex) {

        }


        //send message
        try {

            JSONObject jobj = new JSONObject();
            jobj.put("msg_from", msg_from);
            jobj.put("msg_to", msg_to);
            jobj.put("u_name", u_name);
            jobj.put("image_url", image_url);
            jobj.put("msg", message);
            jobj.put("shared_image_url", "");

            mSocket.emit("new message", jobj.toString());


        } catch (Exception ex) {

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == RESULT_OK) {

               /* Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
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
                md.setUserMessageType(UserMessageType.SELF_IMAGE);
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


            }
        } else if (requestCode == GALLERY_INTENT_CALLED && resultCode == RESULT_OK) {
            try {
                Uri selectedImageuri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImageuri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                Log.e("path", imgDecodableString);

                //create message
                Date dt = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                String time1 = sdf.format(dt);
                String uname = SingleTon.pref.getString("uname", "uname");
                MessageData md = new MessageData();
                md.setUname(uname);
                md.setProfilepic(image_url);
                md.setShared_imgage_url(imgDecodableString);
                md.setUserMessageType(UserMessageType.SELF_IMAGE_LOCAL_UPLOAD);
                md.setMessage("image");
                //check for start with zero
                if (time1.startsWith("0")) {
                    md.setTime(time1.toUpperCase().substring(1));
                } else
                    md.setTime(time1.toUpperCase());
                this.data.add(md);
                recyclerAdapter.notifyDataSetChanged();
                chatRecyclerView.scrollToPosition(this.data.size() - 1);


            } catch (Exception e) {
                e.printStackTrace();
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


    @Override
    protected void onResume() {
        super.onResume();
        try {
            JSONObject json = new JSONObject();
            json.put("user_id", msg_from);
            json.put("friend_id", msg_to);
            mSocket.emit("set login", msg_from);
            mSocket.emit("set friend", json.toString());
            mSocket.emit("set online", msg_from);
            mSocket.emit("check online", msg_to);

            //addLog(getResources().getString(R.string.message_welcome));
            // addParticipantsLog(numUsers);
        } catch (Exception ex) {

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.emit("user gone", msg_to);

        mSocket.disconnect();

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("chat message", chatMessage);
        mSocket.off("new banner", onBanner);
        mSocket.off("user left", onUserLeft);
        mSocket.off("typing", onTyping);
        mSocket.off("stop typing", onStopTyping);
        mSocket.off("set online", onOnline);
        // mSocket.off("message read", msgread);
        mSocket.off("last seen", lastseen);


    }


    private void addTyping(String username) {
        /*mMessages.add(new Message.Builder(Message.TYPE_ACTION)
                .username(username).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();*/
        mUsertype.setText("is typing.....");
    }

    private void removeTyping(String username) {
   /*     for (int i = mMessages.size() - 1; i >= 0; i--) {
            Message message = mMessages.get(i);
            if (message.getType() == Message.TYPE_ACTION && message.getUsername().equals(username)) {
                mMessages.remove(i);
                mAdapter.notifyItemRemoved(i);
            }
        }*/
        mUsertype.setText("Online");

    }

    private void attemptSend() {


        // perform the sending message attempt.
    }

    public void sendImageMessage(String url, String msg_id) {

        try {


            JSONObject jobj = new JSONObject();
            jobj.put("msg_from", msg_from);
            jobj.put("msg_to", msg_to);
            jobj.put("u_name", u_name);
            jobj.put("image_url", image_url);
            jobj.put("msg", "");
            jobj.put("shared_image_url", url);
            jobj.put("user_unqiue_msg_id", msg_id);

            mSocket.emit("new message", jobj.toString());

        } catch (Exception ex) {

        }

    }

    private void leave() {

        mSocket.disconnect();
        mSocket.connect();

    }


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isConnected) {


                        isConnected = true;
                    }
                }
            });
        }
    };


    private Emitter.Listener lastseen = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final JSONObject data = (JSONObject) args[0];

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String lastseenat = getTime(data.getString("last_seen"));
                        mUsertype.setText("Last seen " + lastseenat);
                    } catch (JSONException ex) {
                        Log.e("error", ex.getMessage());
                    }

                }

            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = false;
                    mUsertype.setText("");
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Error on connection", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onOnline = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final String user = args[0].toString();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*Toast.makeText(getApplicationContext(),
                            user.toString() + " Online", Toast.LENGTH_LONG).show();*/
                    mUsertype.setText("Online");


                }
            });
        }
    };

    private Emitter.Listener chatMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsondata = (JSONObject) args[0];
                    // Log.e("json", data + "");
                    String username = "";
                    String message;
                    String from;
                    String messageto;
                    String url;
                    String shared_image_url;
                    String msg_id;
                    // String unqiue_msg_id;
                    String created_at;
                    try {
                        username = jsondata.getString("username");
                        message = jsondata.getString("message");
                        from = jsondata.getString("msg_from");
                        messageto = jsondata.getString("msg_to");
                        url = jsondata.getString("image_url");
                        shared_image_url = jsondata.getString("shared_image_url");
                      //  msg_id = jsondata.getString("msg_id");
                        /// unqiue_msg_id = jsondata.getString("user_unqiue_msg_id");
                        created_at = getTime(jsondata.getString("created_at"));
                        if(from.compareToIgnoreCase(msg_to)!=0)
                            return;
                        MessageData chatResponse = new MessageData();
                        chatResponse.setMessage(message);

                        if (shared_image_url.length() == 0)
                            chatResponse.setUserMessageType(UserMessageType.OTHER);
                        else {
                            chatResponse.setUserMessageType(UserMessageType.OTHER_IMAGE_SERVER);
                            chatResponse.setMessage("Image");

                        }
                        chatResponse.setProfilepic(url);
                        chatResponse.setUname(from);
                        chatResponse.setShared_imgage_url(shared_image_url);
                        if (created_at.startsWith("0")) {
                            chatResponse.setTime(created_at.toUpperCase());
                        } else
                            chatResponse.setTime(created_at.toUpperCase());
                        chatResponse.setDatetime(SingleTon.getCurrentTimeStamp());

                        data.add(chatResponse);
                        recyclerAdapter.notifyDataSetChanged();
                        scrollToBottom();
                      /*  JSONObject jobj = new JSONObject();
                        jobj.put("msg_to", msg_to);
                        jobj.put("msg_id", msg_id);
                        jobj.put("user_unqiue_msg_id", unqiue_msg_id);

                        //read back to inform message read
                        mSocket.emit("message read", jobj.toString());*/
                        // backupdata.put(msg_id, chatResponse);
                        //  Log.e("msg_to",msg_to);
                    } catch (JSONException e) {
                        Log.e("error", e.getMessage() + "");
                    }

                    removeTyping(username);
                    // addMessage(username, message);
                }
            });
        }
    };


    private Emitter.Listener onBanner= new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsondata = (JSONObject) args[0];

                    try {
                        String from=jsondata.getString("msg_from");
                        if(from.compareToIgnoreCase(msg_to)!=0)
                            return;
                        MessageData md1 = new MessageData();
                        md1.setUserMessageType(UserMessageType.BANNER);
                        md1.setImageurl(jsondata.getString("post_image_url"));
                        md1.setSellername(jsondata.getString("user_name"));
                        md1.setSize(jsondata.getString("size"));
                        md1.setPrice(jsondata.getString("price"));
                        md1.setBrandname(jsondata.getString("brand_name"));
                        data.add(md1);
                     recyclerAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("error",e.getMessage());
                    }

                    // addLog(getResources().getString(R.string.message_user_joined, username));
                    // addParticipantsLog(numUsers);
                }
            });
        }
    };


    private Emitter.Listener msgread = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Toast.makeText(ChatActivity.this, "message read", Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject data = (JSONObject) args[0];
                        //  Log.e("size", backupdata.size() + "");
                        //backupdata.get(data.getString("user_unqiue_msg_id")).setIsread(1);
                        recyclerAdapter.notifyDataSetChanged();
                        scrollToBottom();
                    } catch (Exception ex) {
                        Log.e("error", ex.getMessage());
                    }
                    // addLog(getResources().getString(R.string.message_user_joined, username));
                    // addParticipantsLog(numUsers);
                }
            });
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mUsertype.setText("");
                    } catch (Exception e) {
                        return;
                    }

                    // addLog(getResources().getString(R.string.message_user_left, username));
                    // addParticipantsLog(numUsers);
                    // removeTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String userid;
                    try {
                        userid = data.getString("user_id");
                        if(userid.compareToIgnoreCase(msg_to)!=0)
                            return;
                    } catch (JSONException e) {
                        return;
                    }
                    addTyping(userid);
                }
            });
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String userid;
                    try {
                        userid = data.getString("user_id");
                        if(userid.compareToIgnoreCase(msg_to)!=0)
                            return;
                    } catch (JSONException e) {
                        return;
                    }
                    removeTyping(userid);
                }
            });
        }
    };

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;

            mTyping = false;
            mSocket.emit("stop typing", msg_to);

        }
    };

    private Date getDate(String datetimestring) {
        try {


            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            return format.parse(datetimestring);

        } catch (Exception ex) {

        }
        return null;
    }

    private String getTime(String datetimestring) {
        String chattime = "00:00 AM";
        try {


            String time = datetimestring.split("\\s")[1].split("\\.")[0];
            String _24HourTime = time;
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            _24HourSDF.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            return _12HourSDF.format(_24HourDt).toUpperCase();

        } catch (Exception ex) {

        }
        return chattime.toUpperCase();
    }

    private String getLocalTime(String datetimestring) {
        String chattime = "0000-00-00 00:00:00 am";
        try {


            String time = datetimestring;
            String _24HourTime = time;
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            _24HourSDF.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            // Log.e("datetime", _12HourSDF.format(_24HourDt));
            return _12HourSDF.format(_24HourDt);


        } catch (Exception ex) {
            // Log.e("timerissue", ex.getMessage());
        }
        return chattime.toUpperCase();
    }


}
