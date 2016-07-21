package com.eowise.recyclerview.stickyheaders.samples.TabDemo;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.Loading.AVLoadingIndicatorView;
import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.R;

/**
 * Example about replacing fragments inside a ViewPager. I'm using
 * android-support-v7 to maximize the compatibility.
 *
 * @author Dani Lao (@dani_lao)
 */
public class LndShopActivity extends AppCompatActivity implements Animation.AnimationListener {

    // For this example, only two pages
    static final int NUM_ITEMS = 2;
    static AVLoadingIndicatorView prog;
    static ViewPager mPager;
    SlidePagerAdapter mPagerAdapter;
    static String currentcategory = "";
    static Activity act;
    Animation anim1, anim2;
    public static int selectedcategory = 0;
    static boolean isselected = false;
    static int filterselected = 0;
    static String prequery = "";
    private String previousurl = " ";
    private boolean isvisible = false;
    private int[] layoutids = {R.layout.lnd_tutorial_layout, R.layout.lnd_tutorial_layout2, R.layout.lnd_tutorial_dresspage_layout};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lnd_shop);
        //storing current tab

        prog = (AVLoadingIndicatorView) findViewById(R.id.loader);
        /* Instantiate a ViewPager and a PagerAdapter. */
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new SlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        act = this;
        //initializing animation
        anim1 = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        anim2 = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);

        //animation listener
        anim1.setAnimationListener(this);
        anim2.setAnimationListener(this);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    try {
                        CategoryFragment.cf.presentShowcaseView(1000);
                    } catch (Exception ex) {

                    }

                }
                String query = "";
                if (LndShopActivity.selectedcategory == 1 && position == 0) {
                    query = dressfilterquery();
                    Log.e("query", query);
                    if (prequery.compareToIgnoreCase(query) != 0) {
                        prequery = query;
                        getData(query);
                    }
                    showTutorial(layoutids[2], "Dress PAGE");
                } else if (LndShopActivity.selectedcategory == 2 && position == 0) {
                    query = handbagsfilterquery();
                    Log.e("query", query);
                    if (prequery.compareToIgnoreCase(query) != 0) {
                        prequery = query;
                        getData(query);
                    }
                    showTutorial(layoutids[2], "Handbags PAGE");
                } else if (LndShopActivity.selectedcategory == 3 && position == 0) {
                    query = shoefilterquery();
                    Log.e("query", query);
                    if (prequery.compareToIgnoreCase(query) != 0) {
                        prequery = query;
                        getData(query);
                        showTutorial(layoutids[2], "Shoes PAGE");
                    }
                } else if (LndShopActivity.selectedcategory == 4 && position == 0) {
                    query = jewelleryfilterquery();
                    Log.e("query", query);
                    if (prequery.compareToIgnoreCase(query) != 0) {
                        prequery = query;
                        getData(query);
                    }
                    showTutorial(layoutids[2], "Jewellery PAGE");
                } else if (LndShopActivity.selectedcategory == 0 && position == 0) {
                    query = " ";

                    // Log.e("query",query);
                    if (prequery.compareToIgnoreCase(query) != 0) {
                        prequery = query;
                        getData(query);
                    }
                }
                if (position == 0) {
                    Main_TabHost.tabWidget.startAnimation(anim2);

                } else {
                    Main_TabHost.tabWidget.startAnimation(anim1);

                }
                if (!isselected)
                    return;
                else
                    isselected = false;


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        showTutorial(layoutids[0], "This is your shopping page");
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == anim1) {
            Main_TabHost.tabWidget.setVisibility(View.GONE);


            if (Main_TabHost.popupWindow != null && Main_TabHost.popupWindow.isShowing()) {
                isvisible = true;
                Main_TabHost.popupWindow.dismiss();
            }
        } else {
            Main_TabHost.tabWidget.setVisibility(View.VISIBLE);
            if (isvisible) {
                Main_TabHost.main.showPopup();
            }

        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    /* PagerAdapter class */
    public class SlidePagerAdapter extends FragmentStatePagerAdapter {
        public SlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            /*
             * IMPORTANT: This is the point. We create a RootFragment acting as
			 * a container for other fragments
			 */
            if (position == 0)
                return new LndFragment();
            else {

                return new RootFragment();

            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() > 0)
            mPager.setCurrentItem(0);
        else

            try

            {


                Main_TabHost.currenttab.pop();
                Main_TabHost.tabHost.setCurrentTab(Main_TabHost.currenttab.pop());
            } catch (Exception ex) {
                finish();
            }
    }

    private String dressfilterquery() {
        boolean size1 = true, size2 = true, dresstype = true, condition = true, color = true;
        int len = 0;

        String query = " where ";
        String query2 = "", query3 = "", query4 = "", query5 = "", query6 = "", query7 = "";
        if (DressFilterFragment.price1 > 0 || DressFilterFragment.price2 < 1000)
            query2 = " ( price_now between " + DressFilterFragment.price1 + " and " + DressFilterFragment.price2 + ") ";


        for (int i = 0; i < DressFilterFragment.dresstypelist.length; i++) {
            if (DressFilterFragment.dresstypelist[i].length() > 0) {
                if (dresstype) {

                    query3 = " (";
                    dresstype = false;
                }

                query3 = query3 + " prod_type=" + (i + 1) + " or ";
            }

        }


        if (!dresstype)
            query3 = query3 + " )";

//for size type

        for (int i = 0; i < DressFilterFragment.sizetypelist1.length; i++) {
            if (DressFilterFragment.sizetypelist1[i].length() > 0) {
                if (size1) {

                    query4 = query4 + " (";
                    size1 = false;
                }
                query4 = query4 + " FIND_IN_SET('" + DressFilterFragment.sizetypelist1[i] + "',size)" + " or ";
            }


        }

        if (!size1)
            query4 = query4 + " )";


        //for size2 type
        for (int i = 0; i < DressFilterFragment.sizetypelist2.length; i++) {
            if (DressFilterFragment.sizetypelist2[i].length() > 0) {
                if (size2) {

                    query5 = query5 + " (";
                    size2 = false;
                }
                query5 = query5 + " FIND_IN_SET('" + DressFilterFragment.sizetypelist2[i] + "',size)" + " or ";
            }
        }
        if (!size2)

            query5 = query5 + " )";

        //for condition
        for (int i = 0; i < DressFilterFragment.conditionlist.length; i++) {

            if (DressFilterFragment.conditionlist[i].length() > 0) {
                if (condition) {

                    query6 = query6 + " (";
                    condition = false;
                }

                if (DressFilterFragment.conditionlist[i].compareToIgnoreCase("used") == 0) {
                    for (i = 1; i <= 10; i++)
                        query6 = query6 + " prod_condition=" + i + " or ";
                } else
                    query6 = query6 + " prod_condition=" + DressFilterFragment.conditionlist[i] + " or ";

            }
        }

        if (!condition)
            query6 = query6 + " )";

        //for color

        for (int i = 0; i < DressFilterFragment.colorlist.length; i++) {


            if (DressFilterFragment.colorlist[i].length() > 0) {
                if (color) {

                    query7 = query7 + " (";
                    color = false;
                }

                query7 = query7 + " FIND_IN_SET('" + DressFilterFragment.colorlist[i] + "',color_metaltype)" + " or ";

            }
        }

        if (!color)
            query7 = query7 + " )";


        if (query2.length() > 0)

            query = query + query2 + " and ";
        if (query3.lastIndexOf("or") > 0)

            query = query + query3.substring(0, query3.lastIndexOf("or")) + ") and ";
        if (query4.lastIndexOf("or") > 0)
            query = query + query4.substring(0, query4.lastIndexOf("or")) + ") and ";

        if (query5.lastIndexOf("or") > 0)
            query = query + query5.substring(0, query5.lastIndexOf("or")) + ") and ";

        if (query6.lastIndexOf("or") > 0)
            query = query + query6.substring(0, query6.lastIndexOf("or")) + ") and ";

        if (query7.lastIndexOf("or") > 0)
            query = query + query7.substring(0, query7.lastIndexOf("or")) + ") and ";


        query = query + " category_type=1";

        return query;
    }


    private String handbagsfilterquery() {
        boolean size = true, type = true, condition = true, color = true;

        String query = " where ";
        String query1 = "", query2 = "", query3 = "", query4 = "", query5 = "";
        if (HandbagsFilterFragment.price1 > 0 || HandbagsFilterFragment.price2 < 1000)
            query1 = " ( price_now between " + DressFilterFragment.price1 + " and " + DressFilterFragment.price2 + ") ";

        //for type
        for (int i = 0; i < HandbagsFilterFragment.handbagtypelist.length; i++) {
            if (HandbagsFilterFragment.handbagtypelist[i].length() > 0) {
                if (type) {

                    query2 = query2 + " (";
                    type = false;
                }

                query2 = query2 + " prod_type=" + (i + 1) + " or ";
            }

        }


        if (!type)
            query2 = query2 + " )";

//for size

        for (int i = 0; i < HandbagsFilterFragment.sizelist.length; i++) {
            if (HandbagsFilterFragment.sizelist[i].length() > 0) {
                if (size) {

                    query3 = query3 + " (";
                    size = false;
                }
                query3 = query3 + " FIND_IN_SET('" + HandbagsFilterFragment.sizelist[i] + "',size)" + " or ";
            }


        }

        if (!size)
            query3 = query3 + " )";


        //for condition
        for (int i = 0; i < HandbagsFilterFragment.conditionlist.length; i++) {

            if (HandbagsFilterFragment.conditionlist[i].length() > 0) {
                if (condition) {

                    query4 = query4 + " (";
                    condition = false;
                }

                if (HandbagsFilterFragment.conditionlist[i].compareToIgnoreCase("used") == 0) {
                    for (i = 1; i <= 10; i++)
                        query4 = query4 + " prod_condition=" + i + " or ";
                } else
                    query4 = query4 + " prod_condition=" + HandbagsFilterFragment.conditionlist[i] + " or ";

            }
        }

        if (!condition)
            query4 = query4 + " )";

        //for color

        for (int i = 0; i < HandbagsFilterFragment.colorlist.length; i++) {
            if (HandbagsFilterFragment.colorlist.length == 15)
                break;

            if (HandbagsFilterFragment.colorlist[i].length() > 0) {
                if (color) {

                    query5 = query5 + " (";
                    color = false;
                }

                query5 = query5 + " FIND_IN_SET('" + HandbagsFilterFragment.colorlist[i] + "',color_metaltype)" + " or ";

            }
        }

        if (!color)
            query5 = query5 + " )";


        if (query1.length() > 0)
            query = query + query1 + " and ";
        if (query2.lastIndexOf("or") > 0)

            query = query + query2.substring(0, query2.lastIndexOf("or")) + ") and ";
        if (query3.lastIndexOf("or") > 0)
            query = query + query3.substring(0, query3.lastIndexOf("or")) + ") and ";

        if (query4.lastIndexOf("or") > 0)
            query = query + query4.substring(0, query4.lastIndexOf("or")) + ") and ";

        if (query5.lastIndexOf("or") > 0)
            query = query + query5.substring(0, query5.lastIndexOf("or")) + ") and ";

        query = query + " category_type=2";

        return query;
    }

    private String jewelleryfilterquery() {
        boolean size = true, type = true, condition = true, color = true;

        String query = " where ";
        String query1 = "", query2 = "", query3 = "", query4 = "", query5 = "";
        if (JewelleryFilterFragment.price1 > 0 || JewelleryFilterFragment.price2 < 1000)
            query1 = " ( price_now between " + JewelleryFilterFragment.price1 + " and " + JewelleryFilterFragment.price2 + ") ";

        //for metal type
        for (int i = 0; i < JewelleryFilterFragment.metaltypelist.length; i++) {
            if (JewelleryFilterFragment.metaltypelist[i].length() > 0) {
                if (type) {

                    query2 = " (";
                    type = false;
                }

                query2 = query2 + " FIND_IN_SET(" + (i + 1) + ",color_metaltype)" + " or ";
            }

        }


        if (!type)
            query2 = query2 + " )";

//for size

        for (int i = 0; i < JewelleryFilterFragment.sizelist.length; i++) {
            if (JewelleryFilterFragment.sizelist[i].length() > 0) {
                if (size) {

                    query3 = query3 + " (";
                    size = false;
                }
                query3 = query3 + " FIND_IN_SET('" + JewelleryFilterFragment.sizelist[i] + "',size)" + " or ";
            }


        }

        if (!size)
            query3 = query3 + " )";


        //for condition
        for (int i = 0; i < JewelleryFilterFragment.conditionlist.length; i++) {

            if (JewelleryFilterFragment.conditionlist[i].length() > 0) {
                if (condition) {

                    query4 = query4 + " (";
                    condition = false;
                }

                if (JewelleryFilterFragment.conditionlist[i].compareToIgnoreCase("used") == 0) {
                    for (i = 1; i <= 10; i++)
                        query4 = query4 + " prod_condition=" + i + " or ";
                } else
                    query4 = query4 + " prod_condition=" + JewelleryFilterFragment.conditionlist[i] + " or ";

            }
        }

        if (!condition)
            query4 = query4 + " )";


        //for color

        Log.e("type", JewelleryFilterFragment.jewellerytypelist.length + "");
        for (int i = 0; i < JewelleryFilterFragment.jewellerytypelist.length; i++) {


            if (JewelleryFilterFragment.jewellerytypelist[i].length() > 0) {
                if (color) {

                    query5 = query5 + " (";
                    color = false;
                }

                query5 = query5 + " prod_type=" + (i + 1) + " or ";

            }
        }

        if (!color)
            query5 = query5 + " )";


        if (query1.length() > 0)
            query = query + query1 + " and ";
        if (query2.lastIndexOf("or") > 0)

            query = query + query2.substring(0, query2.lastIndexOf("or")) + ") and ";
        if (query3.lastIndexOf("or") > 0)
            query = query + query3.substring(0, query3.lastIndexOf("or")) + ") and ";

        if (query4.lastIndexOf("or") > 0)
            query = query + query4.substring(0, query4.lastIndexOf("or")) + ") and ";

        if (query5.length() > 0)
            query = query + query5.substring(0, query5.lastIndexOf("or")) + " ) and ";


        query = query + " category_type=4";

        return query;
    }

    private String shoefilterquery() {
        boolean size = true, type = true, condition = true, color = true;

        String query = " where ";
        String query1 = "", query2 = "", query3 = "", query4 = "", query5 = "";
        if (ShoesFilterFragment.price1 > 0 || ShoesFilterFragment.price2 < 1000)
            query1 = " ( price_now between " + ShoesFilterFragment.price1 + " and " + ShoesFilterFragment.price2 + ") ";

        //for type
        for (int i = 0; i < ShoesFilterFragment.shoetypelist.length; i++) {
            if (ShoesFilterFragment.shoetypelist[i].length() > 0) {
                if (type) {

                    query2 = " (";
                    type = false;
                }

                query2 = query2 + " prod_type=" + (i + 1) + " or ";
            }

        }


        if (!type)
            query2 = query2 + " )";

//for size

        for (int i = 0; i < ShoesFilterFragment.sizelist.length; i++) {
            if (ShoesFilterFragment.sizelist[i].length() > 0) {
                if (size) {

                    query3 = query3 + " (";
                    size = false;
                }
                query3 = query3 + " FIND_IN_SET('" + ShoesFilterFragment.sizelist[i] + "',size)" + " or ";
            }


        }

        if (!size)
            query3 = query3 + " )";


        //for condition
        for (int i = 0; i < ShoesFilterFragment.conditionlist.length; i++) {

            if (ShoesFilterFragment.conditionlist[i].length() > 0) {
                if (condition) {

                    query4 = query4 + " (";
                    condition = false;
                }

                if (ShoesFilterFragment.conditionlist[i].compareToIgnoreCase("used") == 0) {
                    for (i = 1; i <= 10; i++)
                        query4 = query4 + " prod_condition=" + i + " or ";
                } else
                    query4 = query4 + " prod_condition=" + ShoesFilterFragment.conditionlist[i] + " or ";

            }
        }

        if (!condition)
            query4 = query4 + " )";

        //for color

        for (int i = 0; i < ShoesFilterFragment.colorlist.length; i++) {


            if (ShoesFilterFragment.colorlist[i].length() > 0) {
                if (color) {

                    query5 = query5 + " (";
                    color = false;
                }

                query5 = query5 + " FIND_IN_SET('" + ShoesFilterFragment.colorlist[i] + "',color_metaltype)" + " or ";

            }
        }

        if (!color)
            query5 = query5 + " )";


        if (query1.length() > 0)
            query = query + query1 + " and ";
        if (query2.lastIndexOf("or") > 0)

            query = query + query2.substring(0, query2.lastIndexOf("or")) + ") and ";
        if (query3.lastIndexOf("or") > 0)
            query = query + query3.substring(0, query3.lastIndexOf("or")) + ") and ";

        if (query4.lastIndexOf("or") > 0)
            query = query + query4.substring(0, query4.lastIndexOf("or")) + ") and ";

        if (query5.lastIndexOf("or") > 0)
            query = query + query5.substring(0, query5.lastIndexOf("or")) + ") and ";

        query = query + " category_type=3";
        return query;
    }

    public void getData(final String query) {
     /*   LndShopActivity.prog.setVisibility(View.VISIBLE);
        LndFragment.shopdata.clear();
        RequestQueue queue = Volley.newRequestQueue(LndShopActivity.this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/postdata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                LndShopActivity.prog.setVisibility(View.GONE);
                // Log.e("response", response.toString());
                try {

                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jo = jarray.getJSONObject(i);
                        ShopData pdb = new ShopData();
                        pdb.setPrice(jo.getString("price_now"));
                        pdb.setImageurl(jo.getString("imageurl1"));
                        LndFragment.shopdata.add(pdb);
                    }


                    // rv.setAdapter(adapter);

                    LndFragment.adapter.notifyDataSetChanged();
                } catch (Exception ex) {
                    Log.e("json parsing error", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LndShopActivity.prog.setVisibility(View.GONE);
                try {
                    new com.eowise.recyclerview.stickyheaders.samples.AlertDialog().showAlertDialog(LndShopActivity.this);
                } catch (Exception ex) {

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "14");
                params.put("query", query);
                params.put("skipdata",0+"");
                params.put("user_id", SingleTon.pref.getString("user_id", ""));



                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);*/
        LndFragment.lndshopactivity.reset(query);
    }

    Dialog dialog;

    private void showTutorial(int id, String currentcategory) {
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = getLayoutInflater().inflate(id, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
        TextView categoryselected = (TextView) view.findViewById(R.id.central_heading);
        /*ViewPager viewPager = (ViewPager)dialog.findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                 if(position==1)
                     mPager.setCurrentItem(1);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

        if (categoryselected != null && !currentcategory.contains("shopping"))
            categoryselected.setText(currentcategory.toUpperCase());
        else
            categoryselected.setText(currentcategory);

        view.setOnTouchListener(new SwipeListener(mPager, this));
    }
}
