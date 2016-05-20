package com.eowise.recyclerview.stickyheaders.samples.EditProfile;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.eowise.recyclerview.stickyheaders.samples.ChangePassword.ChangePassword;
import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.LndCustomCameraPost.CompressImage;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class EditProfilePrivate extends AppCompatActivity implements TextWatcher {

    public static int CAMERA_INTENT_CALLED = 100;
    public static int GALLERY_INTENT_CALLED = 200;
    @Bind(R.id.fullname)
    EditText fullname;

    @Bind(R.id.country)
    Spinner country;
    @Bind(R.id.desc)
    EditText desc;
    @Bind(R.id.uname)
    EditText uname;
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.editprofilepic)
    ImageView profilepic;
    @Bind(R.id.updateinfo)
    ImageButton updateinfo;
    @Bind(R.id.loader)

    AVLoadingIndicatorView loader;
    @Bind(R.id.leftchar)
    TextView leftchar;
    static String imageurl = "";
    static String filename = "";
    int picfrom = 0;
    private static final int CAMERA = 0;
    private CallbackManager callbackManager;

    private boolean once = false;
    private boolean isprofileupdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_edit_profile_private);
        ButterKnife.bind(this);

        //custom font
        Typeface tf = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-Bold.otf");

        //applying custom font
        heading.setTypeface(tf);
        //getting bundle
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try {
                JSONObject jobj = new JSONObject(extras.getString("response"));
                uname.setText(jobj.getString("uname") + "");

                fullname.setText(jobj.getString("fullname"));


                email.setText(jobj.getString("email") + "");

                SingleTon.imageLoader.displayImage(jobj.getString("imageurl"), profilepic, SingleTon.options2);
                String des = jobj.getString("desc");
                if (des.length() == 0)
                    desc.setHint("your last night dress status here...");
                else
                    desc.setText(des);

                filename = "lnd" + System.currentTimeMillis() + ".jpg";
                int pos = Integer.parseInt(jobj.getString("country"));
                country.setSelection(pos);

            } catch (Exception ex) {
                Log.e("json parsing error", ex.getMessage());
            }
        }
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e("Success", "Login");
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        Log.e("res", response.toString());

                                        try {

                                            picfrom = 1;
                                            String imgurl = "http://graph.facebook.com/" + object.getString("id") + "/picture?type=large";
                                            //  Log.d("url",imgurl);
                                            SingleTon.imageLoader.displayImage(imgurl, profilepic, SingleTon.options2, new ImageLoadingListener() {
                                                @Override
                                                public void onLoadingStarted(String imageUri, View view) {

                                                }

                                                @Override
                                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                                                }

                                                @Override
                                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                    // Must compress the Image to reduce image size to make upload easy
                                                    loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                                    byte[] byte_arr = stream.toByteArray();
                                                    picfrom = 1;
                                                    // Encode Image to String
                                                    imageurl = Base64.encodeToString(byte_arr, 0);

                                                }

                                                @Override
                                                public void onLoadingCancelled(String imageUri, View view) {

                                                }
                                            });

                                        } catch (Exception ex) {
                                            Log.d("Error", ex.getMessage());
                                        }


                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(EditProfilePrivate.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(EditProfilePrivate.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {

                try {
                    if (pos == 0) {


                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#dadada"));

                        ((TextView) parent.getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);

                    } else {
                        ((TextView) parent.getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                        if (once)
                            updateinfo.setVisibility(View.VISIBLE);
                        else once = true;
                    }
                } catch (NullPointerException ex) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //characters left
        leftchar.setText(150 - desc.length() + " Characters");
        //char left
        desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                leftchar.setText(150 - charSequence.length() + " Characters");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //setup text change listener
        desc.addTextChangedListener(this);
        fullname.addTextChangedListener(this);
        uname.addTextChangedListener(this);
        email.addTextChangedListener(this);

    }

    public void edit(View v) {
        TextView fb, camera, gallery, cancel;

        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // dialog.getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c6290")));
        dialog.setContentView(R.layout.image_choose_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#30ffffff")));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
        //gettting dialog reference
        fb = (TextView) dialog.findViewById(R.id.fb);
        camera = (TextView) dialog.findViewById(R.id.camera);
        gallery = (TextView) dialog.findViewById(R.id.gallery);
        cancel = (TextView) dialog.findViewById(R.id.cancel);
        //custom font
        //ceraeting custom font
        Typeface tf = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-Regular.otf");
        //applying custom fonts
        fb.setTypeface(tf);
        camera.setTypeface(tf);
        gallery.setTypeface(tf);
        cancel.setTypeface(tf);


        //setting events
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                dialog.dismiss();
                LoginManager.getInstance().logInWithReadPermissions(EditProfilePrivate.this, Arrays.asList("public_profile", "user_friends", "email"));

            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
              /*  dialog.dismiss();
                Intent intent2 = new Intent(EditProfilePrivate.this, CustomCameraActivity.class);
                intent2.putExtra(FILENAME, "actress2.jpg");

                startActivityForResult(intent2, 0);*/
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                dialog.dismiss();
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, GALLERY_INTENT_CALLED);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                dialog.dismiss();
            }
        });
    }

    public void back(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (isprofileupdated) {
            Intent intent = new Intent();
            intent.putExtra("status", true);
            setResult(2, intent);

        }
        finish();
    }

    public void changepass(View v) {
        Intent changepass = new Intent(this, ChangePassword.class);
        startActivity(changepass);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //   super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA) {

            // If image available
            if (resultCode == Activity.RESULT_OK) {
                try {

                    // Log.e("url", data.getExtras().getString(IMAGE_URI));
                    String path = data.getExtras().getString("url");

                    // bitmap = Bitmap.createScaledBitmap(bitmap,40);
                    Bitmap selectedImage = CompressImage.compressImage(path);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // Must compress the Image to reduce image size to make upload easy
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byte_arr = stream.toByteArray();
                    // Encode Image to String
                    imageurl = Base64.encodeToString(byte_arr, 0);
                    profilepic.setImageBitmap(selectedImage);
                    picfrom = 3;
                    updateinfo.setVisibility(View.VISIBLE);

                } catch (Exception ex) {

                }
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
                Bitmap selectedImage = CompressImage.compressImage(imgDecodableString);//BitmapFactory.decodeStream(imageStream);
                profilepic.setImageBitmap(selectedImage);
                picfrom = 2;
                //first image

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                imageurl = Base64.encodeToString(byte_arr, 0);
                updateinfo.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void changepas(View v) {
        Intent changepass = new Intent(this, ChangePassword.class);
        startActivity(changepass);
    }

    public void update(View v) {
        String desc, fullname, email, country, uname;
        desc = this.desc.getText().toString();
        fullname = this.fullname.getText().toString();
        if (this.country.getSelectedItemPosition() > 0)
            country = this.country.getSelectedItem().toString();
        else
            country = "";
        email = this.email.getText().toString();
        uname = this.uname.getText().toString();

        if (fullname.length() == 0) {
            this.fullname.requestFocus();
            this.fullname.setError("filed is empty");
            return;
        } else if (uname.length() == 0) {
            this.uname.requestFocus();
            this.uname.setError("field is empty");
            return;
        }


        try {

            JSONObject proinfo = new JSONObject();
            proinfo.put("fullname", fullname);
            proinfo.put("country", this.country.getSelectedItemPosition());
            proinfo.put("uname", uname);
            proinfo.put("description", desc);
            proinfo.put("user_id", SingleTon.pref.getString("user_id", ""));
            proinfo.put("email", email);
            if (picfrom == 2 || picfrom == 3 || picfrom == 1) {
                proinfo.put("filename", filename);
                proinfo.put("imageurl", imageurl);


            } else
                proinfo.put("imageurl", "");
            //  Log.i("data",proinfo.toString());
            updateProfile(proinfo.toString());
        } catch (Exception ex) {

        }
        //
    }

    public void updateProfile(final String data) {
        loader.setVisibility(View.VISIBLE);
        updateinfo.setVisibility(View.GONE);


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/lndusers.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                loader.setVisibility(View.GONE);
                updateinfo.setVisibility(View.GONE);
                try {

                    JSONObject jobj = new JSONObject(response.toString());
                    if (jobj.getBoolean("status")) {

                        SharedPreferences.Editor edit = SingleTon.pref.edit();
                        edit.putString("imageurl", jobj.getString("profile_pic"));
                        edit.commit();

                        isprofileupdated = true;
                    } else {
                        Toast.makeText(EditProfilePrivate.this, jobj.getString("message"), Toast.LENGTH_LONG).show();
                        uname.requestFocus();
                        uname.setError("uname already taken");
                    }
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                updateinfo.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
                //  Log.e("response", error.getMessage() + "");
                Toast.makeText(EditProfilePrivate.this, "Profile not update please try again", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "10");
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
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        queue.add(sr);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        updateinfo.setVisibility(View.VISIBLE);
    }
}


