package com.eowise.recyclerview.stickyheaders.samples.TabDemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;

import java.util.List;

import butterknife.Bind;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

/**
 * Example about replacing fragments inside a ViewPager. I'm using
 * android-support-v7 to maximize the compatibility.
 *
 * @author Dani Lao (@dani_lao)
 */
public class CategoryFragment extends Fragment implements OnClickListener {
    ImageView dress, handbags, shoes, jewellery;
    TextView dresstext, handbagstext, shoestext, jewellerytext, mainpage;
    boolean dressstatus = true, handbagsstatus = true, shoesstatus = true, jewellerystatus = true;
    private LinearLayout ll;
    Button  mButtonShow;
    @Bind({R.id.dresstext, R.id.handbagstext, R.id.size3, R.id.size4, R.id.size5, R.id.size6, R.id.size7, R.id.sizeall, R.id.numsize1, R.id.numsize2, R.id.numsize3, R.id.numsize4, R.id.numsize5, R.id.numsize6, R.id.numsize7, R.id.numsize8, R.id.numsize9, R.id.numsize10, R.id.numsize11, R.id.numsize12})
    List<TextView> dresssize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater
                .inflate(R.layout.lnd_categories_page, container, false);


        setup(view);
        return view;
    }

    private void setup(View v) {
        dress = (ImageView) v.findViewById(R.id.dress);
        handbags = (ImageView) v.findViewById(R.id.handbags);
        shoes = (ImageView) v.findViewById(R.id.shoes);
        jewellery = (ImageView) v.findViewById(R.id.jewellery);
        //textView reference
        dresstext = (TextView) v.findViewById(R.id.dresstext);
        handbagstext = (TextView) v.findViewById(R.id.handbagstext);
        shoestext = (TextView) v.findViewById(R.id.shoestext);
        jewellerytext = (TextView) v.findViewById(R.id.jewellerytext);
        mainpage = (TextView) v.findViewById(R.id.post);
        mButtonShow = (Button)v.findViewById(R.id.btn_show);
//text
        dresstext.setText("Dresses");
        handbagstext.setText("Handbags");
        shoestext.setText("Shoes");

        //appyling custom fonts
        dresstext.setTypeface(SingleTon.robotoregular);
        handbagstext.setTypeface(SingleTon.robotoregular);
        shoestext.setTypeface(SingleTon.robotoregular);
        jewellerytext.setTypeface(SingleTon.robotoregular);
        mainpage.setTypeface(SingleTon.robotoregular);
        mainpage.setText("MAIN PAGE");
//reference more
        ll = (LinearLayout) v.findViewById(R.id.ll);

//checking user selected
        String value = LndShopActivity.currentcategory;
        //  Toast.makeText(getActivity(),value+"",Toast.LENGTH_SHORT).show();
        if (value.compareToIgnoreCase("dress") == 0) {
            dress.setBackgroundResource(R.drawable.category_rounded_corner_selected);
            dress.setImageResource(R.drawable.category_dress_selected);
            dresstext.setTextColor(Color.parseColor("#be4d66"));
            dressstatus = false;    //for dress

            categorySelected();
        } else if (value.compareToIgnoreCase("handbags") == 0) {


            handbags.setBackgroundResource(R.drawable.category_rounded_corner_selected);
            handbags.setImageResource(R.drawable.category_handbags_selected);
            handbagstext.setTextColor(Color.parseColor("#be4d66"));
            handbagsstatus = false;
            categorySelected();

        } else if (value.compareToIgnoreCase("shoes") == 0) {
            shoes.setBackgroundResource(R.drawable.category_rounded_corner_selected);
            shoes.setImageResource(R.drawable.category_shoes_selected);
            shoestext.setTextColor(Color.parseColor("#be4d66"));
            shoesstatus = false;
            categorySelected();
        } else if (value.compareToIgnoreCase("jewellery") == 0) {
            jewellery.setBackgroundResource(R.drawable.category_rounded_corner_selected);
            jewellery.setImageResource(R.drawable.category_jewellery_selected);
            jewellerytext.setTextColor(Color.parseColor("#be4d66"));
            jewellerystatus = false;
            categorySelected();
        } else {
            mainpage.setBackgroundResource(R.drawable.category_buttonselected_corner);
            mainpage.setTextColor(Color.parseColor("#be4d66"));
        }

        //setting up listeners
        dress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                LndShopActivity.selectedcategory = 1;
                LndShopActivity.isselected = true;
                LndShopActivity.currentcategory = "dress";
                if (dressstatus) {
                    clearAll2();
                    dress.setBackgroundResource(R.drawable.category_rounded_corner_selected);
                    dress.setImageResource(R.drawable.category_dress_selected);
                    dresstext.setTextColor(Color.parseColor("#be4d66"));
                    dressstatus = false;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(200);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        LndShopActivity.mPager.setCurrentItem(0, true);

                                    }
                                });
                                Thread.sleep(700);
                                 deSelect(1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }).start();

                } else {
                    dress.setBackgroundResource(R.drawable.category_rounded_corner);
                    dress.setImageResource(R.drawable.category_dress);
                    dresstext.setTextColor(Color.parseColor("#828282"));
                    dressstatus = true;

                }
            }
        });
        handbags.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LndShopActivity.selectedcategory = 2;
                LndShopActivity.isselected = true;
                LndShopActivity.currentcategory = "handbags";
                if (handbagsstatus) {
                    clearAll2();
                    handbags.setBackgroundResource(R.drawable.category_rounded_corner_selected);
                    handbags.setImageResource(R.drawable.category_handbags_selected);
                    handbagstext.setTextColor(Color.parseColor("#be4d66"));
                    handbagsstatus = false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(200);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        LndShopActivity.mPager.setCurrentItem(0, true);

                                    }
                                });
                                Thread.sleep(700);
                                deSelect(2);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }).start();

                } else {
                    handbags.setBackgroundResource(R.drawable.category_rounded_corner);
                    handbags.setImageResource(R.drawable.category_handbags);
                    handbagstext.setTextColor(Color.parseColor("#828282"));
                    handbagsstatus = true;

                }
            }
        });
        shoes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                LndShopActivity.selectedcategory = 3;
                LndShopActivity.isselected = true;
                LndShopActivity.currentcategory = "shoes";
                if (shoesstatus) {

                    clearAll2();
                    shoes.setBackgroundResource(R.drawable.category_rounded_corner_selected);
                    shoes.setImageResource(R.drawable.category_shoes_selected);
                    shoestext.setTextColor(Color.parseColor("#be4d66"));
                    shoesstatus = false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(200);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        LndShopActivity.mPager.setCurrentItem(0, true);

                                    }
                                });
                                Thread.sleep(700);
                                deSelect(3);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }).start();

                } else {
                    shoes.setBackgroundResource(R.drawable.category_rounded_corner);
                    shoes.setImageResource(R.drawable.category_shoes);
                    shoestext.setTextColor(Color.parseColor("#828282"));
                    shoesstatus = true;

                }
            }
        });
        jewellery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LndShopActivity.selectedcategory = 4;
                LndShopActivity.isselected = true;
                LndShopActivity.currentcategory = "jewellery";
                if (jewellerystatus) {
                    clearAll2();
                    jewellery.setBackgroundResource(R.drawable.category_rounded_corner_selected);
                    jewellery.setImageResource(R.drawable.category_jewellery_selected);
                    jewellerytext.setTextColor(Color.parseColor("#be4d66"));
                    jewellerystatus = false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(200);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        LndShopActivity.mPager.setCurrentItem(0, true);

                                    }
                                });
                                Thread.sleep(700);
                                deSelect(4);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }).start();

                } else {
                    jewellery.setBackgroundResource(R.drawable.category_rounded_corner);
                    jewellery.setImageResource(R.drawable.category_jewellery);
                    jewellerytext.setTextColor(Color.parseColor("#828282"));
                    jewellerystatus = true;

                }
            }
        });
//main page
        mainpage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
                mainpage.setBackgroundResource(R.drawable.category_buttonselected_corner);
                mainpage.setTextColor(Color.parseColor("#be4d66"));
                LndShopActivity.selectedcategory = 0;
                LndShopActivity.mPager.setCurrentItem(0);
            }
        });
        //device with and height
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        int width = displayMetrics.widthPixels;
        //Toast.makeText(getActivity(), "width" + width, Toast.LENGTH_SHORT).show();
        if (width <= 480)
            adjust(30, 170, 190);
        else if (width > 480 && width <= 550) {
            adjust(40, 190, 210);
        } else if (width > 480 && width <= 720) {
            adjust(40, 220, 240);
        } else if (width > 720 && width <= 1280) {
            adjust(40, 320, 340);
        } else {
            adjust(45, 350, 370);


        }

        presentShowcaseView(1000);
    }

    private void adjust(int margin, int imgheight, int imgwidth) {
        dress.getLayoutParams().height = imgheight;
        dress.getLayoutParams().width = imgwidth;
        handbags.getLayoutParams().height = imgheight;
        handbags.getLayoutParams().width = imgwidth;
        shoes.getLayoutParams().height = imgheight;
        shoes.getLayoutParams().width = imgwidth;
        jewellery.getLayoutParams().height = imgheight;
        jewellery.getLayoutParams().width = imgwidth;

//setting padding
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(margin, 0, margin, 0);
        ll.setLayoutParams(params);
    }


    private void categorySelected() {
        mainpage.setBackgroundResource(R.drawable.category_buttonnotselected_corner);
        mainpage.setTextColor(Color.parseColor("#828282"));
    }

    private void clearAll() {
        LndShopActivity.currentcategory = "";


        //for dress
        dress.setBackgroundResource(R.drawable.category_rounded_corner);
        dress.setImageResource(R.drawable.category_dress);
        dresstext.setTextColor(Color.parseColor("#828282"));
        dressstatus = true;
//for handbags
        handbags.setBackgroundResource(R.drawable.category_rounded_corner);
        handbags.setImageResource(R.drawable.category_handbags);
        handbagstext.setTextColor(Color.parseColor("#828282"));
        handbagsstatus = true;
//for shoes
        shoes.setBackgroundResource(R.drawable.category_rounded_corner);
        shoes.setImageResource(R.drawable.category_shoes);
        shoestext.setTextColor(Color.parseColor("#828282"));
        shoesstatus = true;
//for jewellery
        jewellery.setBackgroundResource(R.drawable.category_rounded_corner);
        jewellery.setImageResource(R.drawable.category_jewellery);
        jewellerytext.setTextColor(Color.parseColor("#828282"));
        jewellerystatus = true;

    }

    private void clearAll2() {
        //for main button
        mainpage.setBackgroundResource(R.drawable.category_buttonnotselected_corner);
        mainpage.setTextColor(Color.parseColor("#828282"));

        //for dress
        dress.setBackgroundResource(R.drawable.category_rounded_corner);
        dress.setImageResource(R.drawable.category_dress);
        dresstext.setTextColor(Color.parseColor("#828282"));
        dressstatus = true;
//for handbags
        handbags.setBackgroundResource(R.drawable.category_rounded_corner);
        handbags.setImageResource(R.drawable.category_handbags);
        handbagstext.setTextColor(Color.parseColor("#828282"));
        handbagsstatus = true;
//for shoes
        shoes.setBackgroundResource(R.drawable.category_rounded_corner);
        shoes.setImageResource(R.drawable.category_shoes);
        shoestext.setTextColor(Color.parseColor("#828282"));
        shoesstatus = true;
//for jewellery
        jewellery.setBackgroundResource(R.drawable.category_rounded_corner);
        jewellery.setImageResource(R.drawable.category_jewellery);
        jewellerytext.setTextColor(Color.parseColor("#828282"));
        jewellerystatus = true;

    }

    @Override
    public void onClick(View view) {

    }

    private void deSelect(int cate) {
        FragmentTransaction trans = getFragmentManager()
                .beginTransaction();


        //saving
        if (cate== 1) {


            trans.replace(R.id.root_frame, new RootFragment());
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            //trans.addToBackStack(null);
            trans.commit();
        } else if (cate == 2) {

            trans.replace(R.id.root_frame, new HandbagsFilterFragment());
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            //trans.addToBackStack(null);
            trans.commit();
        } else if (cate== 3) {

            trans.replace(R.id.root_frame, new ShoesFilterFragment());
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            //trans.addToBackStack(null);
            trans.commit();
        } else {

            trans.replace(R.id.root_frame, new JewelleryFilterFragment());
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            //trans.addToBackStack(null);
            trans.commit();
        }

    }
    String SHOWCASE_ID="123";
    private void presentShowcaseView(int withDelay) {
        Log.e("status","called");
        new MaterialShowcaseView.Builder(getActivity())
                .setTarget(mButtonShow)
                .setTitleText("Hello")
                .setDismissText("GOT IT")
                .setContentText("This is some amazing feature you should know about")
                .setDelay(withDelay) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse(SHOWCASE_ID) // provide a unique ID used to ensure it is only shown once
                .show();
    }
}
