package com.eowise.recyclerview.stickyheaders.samples.NewMessage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.adapters.SendMsgListAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.Chat_Banner_Data;
import com.eowise.recyclerview.stickyheaders.samples.data.MessageData;
import com.eowise.recyclerview.stickyheaders.samples.data.UserType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

public class SendMessageActivity extends AppCompatActivity {
    @Bind(R.id.chat_list_view)
    ListView chatListView;
    @Bind(R.id.smiley)
    ImageButton emojiButton;

    @Bind(R.id.uname)
    TextView heading;
    private List<MessageData> data = new ArrayList<MessageData>();
    private SendMsgListAdapter listAdapter;
    @Bind(R.id.commentbox)
    EmojiconEditText cmntbox;
    private Bundle extra;
    String sendername = "";
    String senderid = "";
    private static final int CAMERA_PIC_REQUEST = 1337;
    public static Chat_Banner_Data chatbanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        //initialize butter knife
        ButterKnife.bind(this);
        listAdapter = new SendMsgListAdapter(data, this);
        chatListView.setAdapter(listAdapter);
        heading.setTypeface(ImageLoaderImage.hfont);

        //data from previous page
        extra = getIntent().getExtras();

        if (extra != null) {
            sendername = extra.getString("uname");
            senderid = extra.getString("user_id");
            chatbanner= (Chat_Banner_Data) extra.get("bannerdata");
           if(extra.getInt("msgstatus")==0)
            changeStatus(extra.getInt("msgid"));
        }

        heading.setText(capitalize(sendername));
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


        getData(senderid);

    }

    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId) {
        iconToBeChanged.setImageResource(drawableResourceId);
    }

    private String capitalize(final String line) {
        String[] split = line.split(" ");
        String output = "";
        for (String str : split) {

            output += Character.toUpperCase(str.charAt(0)) + str.substring(1) + " ";
        }
        return output;
    }

    public void getData(final String senderid) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/inboxope.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pDialog.dismiss();

                } catch (Exception ex) {

                }
                String uname=ImageLoaderImage.pref.getString("uname","");
               // Log.e("msg", response.toString());
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jo = jarray.getJSONObject(i);
                        MessageData md = new MessageData();
                        md.setMessage(jo.getString("msg"));
                        md.setUname(jo.getString("uname"));
                        md.setTime(jo.getString("date_time"));
                        md.setProfilepic(jo.getString("imgurl"));
                        if (jo.getString("uname").compareTo(uname) == 0) {
                            md.setUserType(UserType.SELF);
                        } else {
                            md.setUserType(UserType.OTHER);
                        }
                        data.add(md);
                    }
                    if (data.size() == 0)
                        Toast.makeText(SendMessageActivity.this, "No conversation yet !", Toast.LENGTH_LONG).show();

                    if (extra != null) {
//banner message
                        //check
                        if (extra.getBoolean("fromhome", false)) {
                            MessageData md1 = new MessageData();
                            md1.setUserType(UserType.BANNER);
                            data.add(md1);
                        }
                    }


                    listAdapter.notifyDataSetChanged();
                    //listview scroll to bottom
                    chatListView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            chatListView.setSelection(data.size());
                        }
                    }, 0);

                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
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
                params.put("receiver_id", ImageLoaderImage.pref.getString("user_id", ""));
                params.put("sender_id", senderid);

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
        super.onBackPressed();
    }

    public void back(View v) {
        onBackPressed();
    }

    public void send(View v) {
        String message="";
        message=cmntbox.getText().toString();
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        String time1 = sdf.format(dt);

        String uname = ImageLoaderImage.pref.getString("uname", "uname");
        //Log.e("check",cmntxt+","+uname);
        if (message.length() == 0)
            return;
        MessageData md = new MessageData();
        md.setMessage(message);
        md.setUname(uname);
        md.setTime(time1);
        md.setUserType(UserType.SELF);
        data.add(md);
        listAdapter.notifyDataSetChanged();
        cmntbox.setText("");
        //send message
      SimpleDateFormat   DATE_FORMAT = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
      String  datetime = DATE_FORMAT.format(new Date());
      try
      {
          JSONObject msg=new JSONObject();
          msg.put("message",message);
          msg.put("date_time",datetime);
          msg.put("sender_id",ImageLoaderImage.pref.getString("user_id", ""));
          msg.put("receiver_id",senderid);


          sendMessage(msg.toString());
      }
      catch (JSONException ex)
      {

      }

    }

    public void sendMessage(final String data) {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/inboxope.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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
    public void camera(View v)
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == RESULT_OK) {

            //    imageData = (Bitmap) data.getExtras().get("data");
              //  ImageView image = (ImageView) findViewById(R.id.imageView1);

            } else if (resultCode == RESULT_CANCELED){

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
                params.put("msgid", id+"");

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
