package com.eowise.recyclerview.stickyheaders.samples.UserProfile;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.LndCustomCameraPost.CompressImage;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShopFragment extends Fragment {
   @Bind(R.id.companyname) EditText companyname;
    @Bind(R.id.fullname) EditText fullname;
    @Bind(R.id.uname) EditText username;
    @Bind(R.id.address) EditText address;
    @Bind(R.id.email) EditText email;
    @Bind(R.id.city) EditText city;
    @Bind(R.id.postalzip) EditText posttalzip;
    @Bind(R.id.pass) EditText password;
    @Bind(R.id.country) Spinner country;
    @Bind(R.id.imgoptions)
    TextView imgoptions;

   static ImageView profileimage;
    @Bind(R.id.next)
    TextView next;
    private static int GALLERY_INTENT_CALLED = 250;
    private static final int CAMERA = 150;
    private static String IMAGE_URI = "ImageUri";
    static int picfrom = 0;
    static String imageurl = "";
    private CallbackManager callbackManager;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(
                R.layout.lnd_shop_user_registration, container, false);
        //making input views scrollable when keyboar opens
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //initialize butter knife
        ButterKnife.bind(this, view);
     profileimage= (ImageView) view.findViewById(R.id.profileimg);
        //custom font
        imgoptions.setTypeface(SingleTon.subheading);
        companyname.setTypeface(SingleTon.mainfont);
        address.setTypeface(SingleTon.mainfont);
        city.setTypeface(SingleTon.mainfont);
        posttalzip.setTypeface(SingleTon.mainfont);

        next.setTypeface(SingleTon.mainfont);
        fullname.setTypeface(SingleTon.mainfont);
        username.setTypeface(SingleTon.mainfont);
        email.setTypeface(SingleTon.mainfont);
        password.setTypeface(SingleTon.mainfont);
        //focus change listener
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    check(username.getText().toString());

            }
        });

        //making input views scrollable when keyboar opens
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //dialog for image options
        imgoptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgoptions();
            }
        });

        //next to finish
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if (pos == 0)
                    try {


                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#dadada"));

                        ((TextView) parent.getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                    } catch (NullPointerException ex) {

                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    public  void check(final String uname){


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST,"http://52.76.68.122/lnd/androidiosphpfiles/lndusers.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                 Log.e("response private", response.toString());
                try {
                    JSONObject jobj=new JSONObject(response.toString());
                    if(jobj.getBoolean("status"))
                     {
                         username.requestFocus();
                         username.setError("Choose another username");
                    }

                }
                catch(Exception ex)
                {
                    Log.e("json parsing error", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("response",error.getMessage()+"");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("rqid","7");
                params.put("uname",uname);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }
    public void imgoptions() {
        TextView fb, camera, gallery, cancel;
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
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
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "AvenirNextLTPro-Regular.otf");
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
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "user_friends", "email"));
                LndLoginSignup.currentpage=5;
                dialog.dismiss();

            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                dialog.dismiss();
             /*   Intent intent2 = new Intent(getActivity(), CustomCameraProfile.class);

                LndLoginSignup.act.startActivityForResult(intent2, CAMERA);*/
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
                LndLoginSignup.act.startActivityForResult(galleryIntent, GALLERY_INTENT_CALLED);
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


    public static void setResult(int requestCode, int resultCode, Intent data) {


//        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA) {
            // If image available
            if (resultCode == Activity.RESULT_OK) {
                try {

                    Log.e("url", data.getExtras().getString(IMAGE_URI));
                    String path = data.getExtras().getString(IMAGE_URI);

                    // bitmap = Bitmap.createScaledBitmap(bitmap,40);
                    Bitmap selectedImage = CompressImage.compressImage(path);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // Must compress the Image to reduce image size to make upload easy
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byte_arr = stream.toByteArray();
                    // Encode Image to String
                    imageurl = Base64.encodeToString(byte_arr, 0);
                    profileimage.setImageBitmap(selectedImage);
                    picfrom = 3;

                } catch (Exception ex) {

                }
            }
        } else if (requestCode == GALLERY_INTENT_CALLED && resultCode == Activity.RESULT_OK) {
            try {
                Uri selectedImageuri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor =LndLoginSignup.act.getContentResolver().query(selectedImageuri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                Bitmap selectedImage = CompressImage.compressImage(imgDecodableString);//BitmapFactory.decodeStream(imageStream);
                profileimage.setImageBitmap(selectedImage);
                picfrom = 2;
                //first image

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                imageurl = Base64.encodeToString(byte_arr, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void saveData()
    {
        String fullname="",username="",email="",password="",companyname="",address="",city="",zipcode="",country="";
            fullname= this.fullname.getText().toString();
            username= this.username.getText().toString();
            email = this.email.getText().toString();
            password = this.password.getText().toString();
            companyname = this.companyname.getText().toString();
            address = this.address.getText().toString();
            city = this.city.getText().toString();
            zipcode = this.posttalzip.getText().toString();
            country = this.country.getSelectedItem().toString();
        if (companyname.length() == 0) {
            //Toast.makeText(this, "Company field is empty", Toast.LENGTH_SHORT).show();
            this.companyname.requestFocus();
            this.companyname.setError("field is empty");

            return;
        }

        if(fullname.length()==0)
            {
                //   Toast.makeText(this,"Name field is empty",Toast.LENGTH_SHORT).show();
                this.fullname.requestFocus();
                this.fullname.setError("field is empty");
                return;
            }

            if(username.length()==0)
            {
                //   Toast.makeText(this,"Name field is empty",Toast.LENGTH_SHORT).show();
                this.username.requestFocus();
                this.username.setError("field is empty");
                return;
            }

            if (email.length() == 0) {
               // Toast.makeText(this, "Name field is empty", Toast.LENGTH_SHORT).show();
                this.email.requestFocus();

                this.email.setError("field is empty");
                return;
            }
        if (!isValidEmail(email)) {
            //   Toast.makeText(this, "Enter valid Email", Toast.LENGTH_SHORT).show();
            this.email.requestFocus();

            this.email.setError("Enter a valid Email");
            return;
        }

        if (address.length() == 0) {
            // Toast.makeText(this, "Address field is empty", Toast.LENGTH_SHORT).show();
            this.address.requestFocus();

            this.address.setError("field is empty");

            return;
        }

        if (city.length() == 0) {
            // Toast.makeText(this, "City field is empty", Toast.LENGTH_SHORT).show();
            this.city.requestFocus();

            this.city.setError("field is empty");

            return;
        }
        if (zipcode.length() == 0) {
            //  Toast.makeText(this, "Zipcode field is empty", Toast.LENGTH_SHORT).show();
            this.posttalzip.requestFocus();

            this.posttalzip .setError("field is empty");

            return;
        }
        if (this.country.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Select your country", Toast.LENGTH_SHORT).show();
            //   ShopFragment.shopcountry.setError("field is empty");

            return;
        }

        if (password.length() == 0) {
            //Toast.makeText(this, "Password field is empty", Toast.LENGTH_SHORT).show();
            this.password.requestFocus();

            this.password.setError("field is empty");

            return;
        }



        try {
            LndUserDescriptionFragment.profiletype=2;
            LndUserDescriptionFragment.jsonshop.put("usertype","shop");
            LndUserDescriptionFragment.jsonshop.put("fullname", fullname);
            LndUserDescriptionFragment.jsonshop.put("username",username);
            LndUserDescriptionFragment.jsonshop.put("email",email);
            LndUserDescriptionFragment.jsonshop.put("password",password);
            LndUserDescriptionFragment.jsonshop.put("companyname",companyname);
            LndUserDescriptionFragment.jsonshop.put("address", address);
            LndUserDescriptionFragment.jsonshop.put("city",city);
            LndUserDescriptionFragment.jsonshop.put("zipcode", zipcode);
            LndUserDescriptionFragment.jsonshop.put("country",this.country.getSelectedItemPosition());


            if(picfrom==2||picfrom==3||picfrom==1)
            {

                LndUserDescriptionFragment.jsonshop.put("imageurl",imageurl);
                LndUserDescriptionFragment.jsonshop.put("filename","lnd"+System.currentTimeMillis()+".jpg");

            }
            else {

                LndUserDescriptionFragment.jsonshop.put("imageurl","");

                LndUserDescriptionFragment.jsonshop.put("filename","");
            }
            //current page value on stack;
            LndLoginSignup.currenttab.push(5);
            LndLoginSignup.mViewPager.setCurrentItem(6);
        }
        catch(Exception ex)
        {
           Log.e("error",ex.getMessage()+"");
        }

    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void update(JSONObject object)

    {
        try {


            String imgurl = "http://graph.facebook.com/" + object.getString("id") + "/picture?type=large";
            // Log.d("url", imgurl);
            //  Toast.makeText(getActivity(), imgurl, Toast.LENGTH_SHORT).show();
            SingleTon.imageLoader.displayImage(imgurl, profileimage, SingleTon.options2, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    picfrom = 1;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // Must compress the Image to reduce image size to make upload easy
                    loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byte_arr = stream.toByteArray();
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
    @Override
    public void onResume() {
        super.onResume();
        country.post(new Runnable() {
            @Override
            public void run() {
                country.setSelection(1);
                country.setSelection(0);
            }
        });


    }

}