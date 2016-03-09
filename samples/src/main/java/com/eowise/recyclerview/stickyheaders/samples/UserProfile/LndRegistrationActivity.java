package com.eowise.recyclerview.stickyheaders.samples.UserProfile;

import android.support.v7.app.AppCompatActivity;

public class LndRegistrationActivity extends AppCompatActivity {
/*
    @Bind(R.id.priv)TextView pri;
    @Bind(R.id.shop)TextView shop;
    public static int CAMERA_INTENT_CALLED=100;
    public static int GALLERY_INTENT_CALLED=200;
    @Bind(R.id.profileimg)ImageView profilepic;
    private int check=1;
    @Bind(R.id.heading)TextView heading;
    public static boolean statusuname=false;

    public static TextView unametaken;
    static String imageurl="";
    static String filename="";
    int picfrom=0;
    private static final int CAMERA = 0;
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.lnd_registration_activity);

        ButterKnife.bind(this);
        //setting fonts
        unametaken= (TextView) findViewById(R.id.unametaken);
        //setting custom font
        heading.setTypeface(ImageLoaderImage.hfont);

        Bundle extra=getIntent().getExtras();
        if(extra!=null) {
            PrivateFragment.priname.setText(extra.getString("name"));
            PrivateFragment.priemail.setText(extra.getString("email"));
        }

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                      //  Log.e("res", response.toString());

                                        try {


                                            String imgurl = "http://graph.facebook.com/" + object.getString("id") + "/picture?type=large";
                                             Log.d("url",imgurl);

                                            ImageLoaderImage.imageLoader.displayImage(imgurl, profilepic, ImageLoaderImage.options2, new ImageLoadingListener() {
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
                        Toast.makeText(LndRegistrationActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LndRegistrationActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        }

    public void selectFrag(View view) {
        Fragment fr;

        if(view == findViewById(R.id.shop)) {
            fr = new ShopFragment();
            shop.setTextColor(Color.parseColor("#be4d66"));
            pri.setTextColor(Color.parseColor("#dadada"));
            check=2;
            pri.setCompoundDrawablesWithIntrinsicBounds(0,0,0,R.drawable.line_2);
            shop.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.line_1);

        }else {
            check=1;
            fr = new PrivateFragment();
            pri.setTextColor(Color.parseColor("#be4d66"));
            shop.setTextColor(Color.parseColor("#dadada"));
            shop.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.line_2);
            pri.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.line_1);

        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fr);
        fragmentTransaction.commit();

    }
    public void imgoptions(View v)
    {
        TextView fb,camera,gallery,cancel;
       final Dialog dialog = new Dialog(this,R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // dialog.getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c6290")));
        dialog.setContentView(R.layout.image_choose_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#30ffffff")));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
        //gettting dialog reference
        fb= (TextView) dialog.findViewById(R.id.fb);
        camera= (TextView) dialog.findViewById(R.id.camera);
        gallery= (TextView) dialog.findViewById(R.id.gallery);
        cancel= (TextView) dialog.findViewById(R.id.cancel);
        //custom font
        //ceraeting custom font
        Typeface tf=Typeface.createFromAsset(getAssets(),"AvenirNextLTPro-Regular.otf");
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
                LoginManager.getInstance().logInWithReadPermissions(LndRegistrationActivity.this, Arrays.asList("public_profile", "user_friends", "email"));

                dialog.dismiss();

            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle me
                dialog.dismiss();
                Intent intent2 = new Intent(LndRegistrationActivity.this, CustomCameraProfile.class);
                intent2.putExtra(FILENAME, "actress2.jpg");
                intent2.putExtra(IS_PORTRAIT, true);
                intent2.putExtra(QUALITY, 100);

                startActivityForResult(intent2, 0);
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
                startActivityForResult(galleryIntent,GALLERY_INTENT_CALLED);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //   super.onActivityResult(requestCode, resultCode, data);

            callbackManager.onActivityResult(requestCode, resultCode, data);

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
                    profilepic.setImageBitmap(selectedImage);
                    picfrom = 3;

                } catch (Exception ex) {

                }
            }
        }
        else if(requestCode==GALLERY_INTENT_CALLED&&resultCode==RESULT_OK)
        {
            try {
                Uri selectedImageuri = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

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
                picfrom=2;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void back(View v)
    {
        onBackPressed();
    }
    public void saveData(View v)
    {
        String firstname="",lastname="",uname="",email="",pass="",confirmpass="",companyname="",address="",city="",zipcode="",country="";
        //private fragment fields
        JSONObject proinfo = new JSONObject();
        if(check==1)
        {
            firstname= PrivateFragment.firstname.getText().toString();
            lastname= PrivateFragment.lastname.getText().toString();

            uname= PrivateFragment.priname.getText().toString();
        email= PrivateFragment.priemail.getText().toString();
        pass= PrivateFragment.pripass.getText().toString();
        confirmpass= PrivateFragment.priconfirmpass.getText().toString();
            country = PrivateFragment.privatecountry.getSelectedItem().toString();
            if(firstname.length()==0)
            {
                //   Toast.makeText(this,"Name field is empty",Toast.LENGTH_SHORT).show();
                PrivateFragment.firstname.setError("field is empty");
                return;
            }

            if(lastname.length()==0)
            {
                //   Toast.makeText(this,"Name field is empty",Toast.LENGTH_SHORT).show();
                PrivateFragment.lastname.setError("field is empty");
                return;
            }

            if(uname.length()==0)
        {
         //   Toast.makeText(this,"Name field is empty",Toast.LENGTH_SHORT).show();
            PrivateFragment.priname.setError("field is empty");
            return;
        }
        if(email.length()==0)
        {
           // Toast.makeText(this,"Email field is empty",Toast.LENGTH_SHORT).show();
            PrivateFragment.priemail.setError("field is empty");

            return;
        }
            else if(!isValidEmail(email))
        {
          //  Toast.makeText(this,"Enter valid Email",Toast.LENGTH_SHORT).show();
            PrivateFragment.priemail.setError("Enter a valid Email");
            return;
        }

        if(pass.length()==0)
        {
          //  Toast.makeText(this,"Password field is empty",Toast.LENGTH_SHORT).show();
            PrivateFragment.pripass.setError("field is empty");

            return;
        }
        if(confirmpass.length()==0)
        {
            //Toast.makeText(this,"Confirm password field is empty",Toast.LENGTH_SHORT).show();
            PrivateFragment.priconfirmpass.setError("field is empty");

            return;
        }

         if (PrivateFragment.privatecountry.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Select your country", Toast.LENGTH_SHORT).show();
            //   ShopFragment.shopcountry.setError("field is empty");

            return;
        }
            if(statusuname)
            {
                unametaken.setVisibility(View.VISIBLE);
                return;
            }
         if(pass.compareTo(confirmpass)!=0)
         {
             //Toast.makeText(this,"Password mismatch",Toast.LENGTH_SHORT).show();
             PrivateFragment.pripass.setError("Password mismatch");
             return;
         }
           // Toast.makeText(this,"private data",Toast.LENGTH_LONG).show();
            try {
                proinfo.put("utype", "private");

            } catch (Exception ex) {

            }
        }
        else {
            firstname= ShopFragment.firstname.getText().toString();
            lastname= ShopFragment.lastname.getText().toString();
            uname = ShopFragment.shopuname.getText().toString();
            email = ShopFragment.shopemail.getText().toString();
            pass = ShopFragment.shoppass.getText().toString();
            confirmpass = ShopFragment.shopconfirmpass.getText().toString();
            companyname = ShopFragment.shopcompanyname.getText().toString();
            address = ShopFragment.shopaddress.getText().toString();
            city = ShopFragment.shopcity.getText().toString();
            zipcode = ShopFragment.shopzipcode.getText().toString();
            country = ShopFragment.shopcountry.getSelectedItem().toString();
            if(firstname.length()==0)
            {
                //   Toast.makeText(this,"Name field is empty",Toast.LENGTH_SHORT).show();
                ShopFragment.firstname.setError("field is empty");
                return;
            }

            if(lastname.length()==0)
            {
                //   Toast.makeText(this,"Name field is empty",Toast.LENGTH_SHORT).show();
                ShopFragment.lastname.setError("field is empty");
                return;
            }

            if (uname.length() == 0) {
               // Toast.makeText(this, "Name field is empty", Toast.LENGTH_SHORT).show();
                ShopFragment.shopuname.setError("field is empty");
                return;
            }
            if (email.length() == 0) {
               // Toast.makeText(this, "Email field is empty", Toast.LENGTH_SHORT).show();
                ShopFragment.shopemail.setError("field is empty");

                return;
            } else if (!isValidEmail(email)) {
             //   Toast.makeText(this, "Enter valid Email", Toast.LENGTH_SHORT).show();
                ShopFragment.shopemail.setError("Enter a valid Email");
                return;
            } else if (pass.length() == 0) {
                //Toast.makeText(this, "Password field is empty", Toast.LENGTH_SHORT).show();
                ShopFragment.shoppass.setError("field is empty");

                return;
            } else if (confirmpass.length() == 0) {
               // Toast.makeText(this, "Confirm password field is empty", Toast.LENGTH_SHORT).show();
                ShopFragment.shopconfirmpass.setError("field is empty");

                return;
            } else if (companyname.length() == 0) {
                //Toast.makeText(this, "Company field is empty", Toast.LENGTH_SHORT).show();
                ShopFragment.shopcompanyname.setError("field is empty");

                return;
            } else if (address.length() == 0) {
               // Toast.makeText(this, "Address field is empty", Toast.LENGTH_SHORT).show();
                ShopFragment.shopaddress.setError("field is empty");

                return;
            } else if (city.length() == 0) {
               // Toast.makeText(this, "City field is empty", Toast.LENGTH_SHORT).show();
                ShopFragment.shopcity.setError("field is empty");

                return;
            } else if (zipcode.length() == 0) {
              //  Toast.makeText(this, "Zipcode field is empty", Toast.LENGTH_SHORT).show();
                ShopFragment.shopcompanyname.setError("field is empty");

                return;
            } else if (ShopFragment.shopcountry.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Select your country", Toast.LENGTH_SHORT).show();
             //   ShopFragment.shopcountry.setError("field is empty");

                return;
            }


            if (statusuname) {
                unametaken.setVisibility(View.VISIBLE);
                return;
            }
            if (pass.compareTo(confirmpass) != 0) {
               // Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show();
                PrivateFragment.pripass.setError("Password mismatch");
                return;
            }
          //  Toast.makeText(this, "private data", Toast.LENGTH_LONG).show();


            Toast.makeText(this, "public data", Toast.LENGTH_LONG).show();
            try {
                proinfo.put("utype", "shop");

            } catch (Exception ex) {

            }
        }
        try {

            proinfo.put("fname",firstname);
            proinfo.put("lname",lastname);
            proinfo.put("uname",uname);
            proinfo.put("email",email);
            proinfo.put("pass",pass);
            proinfo.put("companyname",companyname);
            proinfo.put("address", address);
            proinfo.put("city",city);
            proinfo.put("zipcode",zipcode);
            proinfo.put("country", country);


            if(picfrom==2||picfrom==3||picfrom==1)
            {
                proinfo.put("isimg","yes");
                proinfo.put("imageurl",imageurl);
                proinfo.put("filename","lnd"+System.currentTimeMillis()+".jpg");

            }
            else {

                proinfo.put("imageurl", "http://52.76.68.122/lnd/photos/lnd"+System.currentTimeMillis()+".jpg");

                proinfo.put("isimg","");
            }
                Log.e("data",proinfo.toString());
             registerprivateUser(proinfo.toString());

        }
        catch(Exception ex)
        {

        }

    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public  void registerprivateUser(final String data){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("wait creating profile...");
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://52.76.68.122/lnd/lndusers.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              pDialog.dismiss();
                Log.e("response private", response.toString());
               try
                {
                    JSONObject jobj=new JSONObject(response.toString());
                    if(jobj.getBoolean("status"))
                    {
                        SharedPreferences.Editor edit= ImageLoaderImage.pref.edit();
                        edit.putString("uname", jobj.getString("uname"));
                        edit.putString("upass",jobj.getString("pass"));
                        edit.putString("utype",jobj.getString("type"));

                        edit.putString("imageurl", jobj.getString("imageurl"));
                        edit.commit();

                        Intent i = new Intent(LndRegistrationActivity.this, Main_TabHost.class);
                        startActivity(i);
                        ActivityCompat.finishAffinity(LndRegistrationActivity.this);
                    }
                    else
                    {
                        unametaken.setVisibility(View.VISIBLE);
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
                pDialog.dismiss();
                Log.e("response",error.getMessage()+"");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("rqid","5");
                params.put("data",data);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);

        queue.add(sr);
    }*/
}
