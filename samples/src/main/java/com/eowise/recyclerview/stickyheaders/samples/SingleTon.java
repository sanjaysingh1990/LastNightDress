package com.eowise.recyclerview.stickyheaders.samples;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.eowise.recyclerview.stickyheaders.samples.SQLDB.DatabaseHandler;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ApplicationConstants;
import com.eowise.recyclerview.stickyheaders.samples.data.LndAgentBean;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SingleTon extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.


    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.



    public static SharedPreferences pref;
    public static DisplayImageOptions options,options2,options3,options4;
    public static ImageLoader imageLoader,imageLoader2;
    public static Typeface unamefont,hfont,normalfont,robotomedium,robotoregular,robotobold,mainfont,subheading,appfont,settingfont;
    public static DatabaseHandler db;
    static  int  line=0;
    public static HashMap<String,String> lnduserid=new HashMap<>();
    public static DisplayMetrics displayMetrics;
    @Override
    public void onCreate()
    {
        super.onCreate();
        Fabric.with(this, new Crashlytics());


               displayMetrics = getResources().getDisplayMetrics();

        // UNIVERSAL IMAGE LOADER SETUP

        options4 = new DisplayImageOptions.Builder()

                .showImageOnLoading(R.drawable.loading_icon)
                .showImageForEmptyUri(R.drawable.loading_icon)
                .showImageOnFail(R.drawable.loading_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options4)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024)
                .build();

       imageLoader2= ImageLoader.getInstance();
        imageLoader2.init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP

        db = new DatabaseHandler(this);
        unamefont=Typeface.createFromAsset(getAssets(),"AvenirNextLTPro-Demi.otf");
        hfont=Typeface.createFromAsset(getAssets(),"AvenirNextLTPro-Bold.otf");
        normalfont=Typeface.createFromAsset(getAssets(),"AvenirNextLTPro-Regular.otf");
        robotoregular=Typeface.createFromAsset(getAssets(),"Roboto-Regular.ttf");
        robotomedium=Typeface.createFromAsset(getAssets(),"Roboto-Medium.ttf");
        robotobold=Typeface.createFromAsset(getAssets(),"Roboto-Bold.ttf");
        mainfont=Typeface.createFromAsset(getAssets(),"arialms.ttf");
        subheading=Typeface.createFromAsset(getAssets(),"arialbold.ttf");
        appfont=Typeface.createFromAsset(getAssets(),"Mural_Script.ttf");

        settingfont = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-Regular.otf");

        pref=getSharedPreferences("lndlogin", Context.MODE_PRIVATE);
         options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading_icon)
                .showImageForEmptyUri(R.drawable.loading_icon)
                .showImageOnFail(R.drawable.loading_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true) .imageScaleType(ImageScaleType.EXACTLY)
                .considerExifParams(true)

                .build();
         options2 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.empty_user2)
                .showImageForEmptyUri(R.drawable.empty_user2)
                .showImageOnFail(R.drawable.empty_user2)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)

                .build();
        options3 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.empty_user3)
                .showImageForEmptyUri(R.drawable.empty_user3)
                .showImageOnFail(R.drawable.empty_user3)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)


                .build();

        imageLoader=ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(this));
       if(SingleTon.pref.getInt("user_position",0)<6)
        checkUserposition();
    }
    public static double getExchangeRate(String currency) {
        switch (currency) {
            case "USD":
                return 1;
            case "JPY":
                return 102.53;
            case "GBP":
                return 0.60;

            case "CANADA":
                return 1.46;
            case "POUND":
                return 0.71;
            case "RUPEE":
                return 68.01;
            case "EURO":
                return 0.92;
            default:
                throw new IllegalArgumentException(String.format("No rates available for currency %s %n", currency));
        }
    }
    /**
     * Display price in US Dollar currency
     *
     * @param price
     * @param rate
     */
    public static String showPriceInUSD(double price, double rate) {
        double priceInUSD = price * rate;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        String value= currencyFormat.format(priceInUSD);
        return  value.substring(0,value.indexOf("."));
    }
//British money
    public static String showPriceInPound(double price, double rate) {
        double priceInPound = price * rate;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        String value= currencyFormat.format(priceInPound);
        return  value.substring(0,value.indexOf("."));
    }
    public static String showPriceInCanada(double price, double rate) {
        double priceInCANADA = price * rate;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.CANADA);
        String value= currencyFormat.format(priceInCANADA);
        return  value.substring(0, value.indexOf("."));
    }
    public static String showPriceInEuro(double price, double rate) {
        double priceInEuro = price * rate;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.ITALY);
        String value= currencyFormat.format(priceInEuro);
        return  value.substring(0, value.indexOf("."));

    }

    public static String showPriceInRupee(double price, double rate) {
        double priceInIndia = price * rate;
       // NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(null);
        String value = String.format("%.2f", priceInIndia);

        return  value.substring(0,value.indexOf("."));

    }

    /**
     * Display prince in British Pound
     *
     * @param price
     * @param rate
     */
    public static void showPriceInGBP(double price, double rate) {
        double princeInGBP = price * rate;
        NumberFormat GBP = NumberFormat.getCurrencyInstance(Locale.UK);
        System.out.printf("Price in GBP : %s %n", GBP.format(princeInGBP));
    }

    /**
     * Display prince in Japanese Yen
     *
     * @param price
     * @param rate
     */
    public static String showPriceInJPY(double price, double rate) {
        double princeInJPY = price * rate;
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.JAPAN);
        return currency.format(princeInJPY);
    }
public static void showValue(String country, TextView pricewas,TextView pricenow,double price_was,double price_now)
{
    if(country.compareTo("united states")==0)
    {  String value=showPriceInUSD(price_was, getExchangeRate("USD"));
        Spannable word = new SpannableString(value+"   ");



        word.setSpan(new ForegroundColorSpan(Color.parseColor("#b4b4b4")), 0, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        pricewas.setText(word);
        value=showPriceInUSD(price_now, getExchangeRate("USD"));
        Spannable wordTwo = new SpannableString(value);

        wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#be4d66")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            pricewas.append(wordTwo);

    }
    else if(country.compareTo("canada")==0)
    {
        String value="C"+showPriceInCanada(price_was, getExchangeRate("CANADA"));
        Spannable word = new SpannableString(value+"   ");
        // pricewas.setVisibility(View.GONE);
       String text=value + "   " + value;
        pricewas.setText(text);



        word.setSpan(new ForegroundColorSpan(Color.parseColor("#b4b4b4")), 0, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        pricewas.setText(word);
        value="C"+showPriceInCanada(price_now, getExchangeRate("CANADA"));
        Spannable wordTwo = new SpannableString(value);

        wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#be4d66")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        pricewas.append(wordTwo);
    }
    else if(country.compareTo("england")==0)
    {
        String value=showPriceInPound(price_was, getExchangeRate("POUND"));
        Spannable word = new SpannableString(value+"   ");
        // pricewas.setVisibility(View.GONE);
        pricewas.setText(value + "   " + value);



        word.setSpan(new ForegroundColorSpan(Color.parseColor("#b4b4b4")), 0, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        pricewas.setText(word);
        value=showPriceInPound(price_now, getExchangeRate("POUND"));
        Spannable wordTwo = new SpannableString(value);

        wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#be4d66")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            pricewas.append(wordTwo);

    }
    else if(country.compareTo("india")==0)
    {
        String value="Rs."+showPriceInRupee(price_was, getExchangeRate("RUPEE"));
        Spannable word = new SpannableString(value+"   ");
        String text=value + "   " + value;
        pricewas.setText(text);



        word.setSpan(new ForegroundColorSpan(Color.parseColor("#b4b4b4")), 0, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        pricewas.setText(word);
        value="Rs."+showPriceInRupee(price_now, getExchangeRate("RUPEE"));
        Spannable wordTwo = new SpannableString(value);

        wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#be4d66")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        pricewas.append(wordTwo);

    }
    else if(country.compareTo("italy")==0)
    {
        pricewas.setText(SingleTon.showPriceInEuro(price_was, getExchangeRate("EURO")));

        pricenow.setText(SingleTon.showPriceInEuro(price_now, getExchangeRate("EURO")));

    }
    else {
       pricewas.setText(SingleTon.showPriceInUSD(price_was, getExchangeRate("USD")));
        pricenow.setText(SingleTon.showPriceInUSD(price_now, getExchangeRate("USD")));
    }
}

    public static void showValue(String country,TextView pricenow,double price_now)
    {
        if(country.compareTo("united states")==0)
        {
            pricenow.setText(SingleTon.showPriceInUSD(price_now, getExchangeRate("USD")));
        }
        else if(country.compareTo("canada")==0)
        {
             pricenow.setText("C" + SingleTon.showPriceInCanada(price_now, getExchangeRate("CANADA")));

        }
        else if(country.compareTo("england")==0)
        {
           pricenow.setText(SingleTon.showPriceInPound(price_now, getExchangeRate("POUND")));

        }
        else if(country.compareTo("india")==0)
        {
            pricenow.setText("Rs." + SingleTon.showPriceInRupee(price_now, getExchangeRate("RUPEE")));

        }
        else if(country.compareTo("italy")==0)
        {
            pricenow.setText(SingleTon.showPriceInEuro(price_now, getExchangeRate("EURO")));

        }


        else {
          pricenow.setText(SingleTon.showPriceInUSD(price_now, getExchangeRate("USD")));
        }
    }
    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }


    public void checkUserposition() {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jobj = new JSONObject(response.toString());
                    boolean status = jobj.getBoolean("status");
                    if (status) {
                        if(jobj.getInt("count")>=5)
                          //  Toast.makeText(SingleTon.this,"level changed",Toast.LENGTH_SHORT).show();
                           updateUserposition();
                    }


                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage() + "");
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
                params.put("rqid", "17");



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

    public void updateUserposition() {


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, ApplicationConstants.APP_SERVER_URL_LND_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jobj = new JSONObject(response.toString());
                    boolean status = jobj.getBoolean("status");
                    if (status) {

                        //    Toast.makeText(SingleTon.this,"user_position updated",Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage() + "");
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
                params.put("rqid", "18");
                params.put("user_id",  SingleTon.pref.getString("user_id", ""));




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
