package com.eowise.recyclerview.stickyheaders.samples.TabDemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;
import com.eowise.recyclerview.stickyheaders.samples.LndRangeBar.RangeSeekBar;
import com.eowise.recyclerview.stickyheaders.samples.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Example about replacing fragments inside a ViewPager. I'm using
 * android-support-v7 to maximize the compatibility.
 *
 * @author Dani Lao (@dani_lao)
 */
public class JewelleryFilterFragment extends Fragment implements View.OnClickListener {

    @Bind({R.id.size1, R.id.size2, R.id.size3, R.id.size4, R.id.size5, R.id.size6, R.id.size7, R.id.size8, R.id.size9, R.id.size10, R.id.size11, R.id.size12, R.id.size13, R.id.size14, R.id.size15, R.id.size16, R.id.size17, R.id.size18, R.id.size19, R.id.size20, R.id.size21, R.id.sizeall})
    List<TextView> ringsize;


    @Bind({R.id.type1, R.id.type2, R.id.type3, R.id.type4, R.id.type5, R.id.jewelleryall})
    List<TextView> jewellerytype;

    @Bind({R.id.metal1, R.id.metal2, R.id.metal3, R.id.metal4, R.id.metal5, R.id.metal6, R.id.metal7, R.id.metal8, R.id.metalall})
    List<TextView> metaltype;

    @Bind(R.id.reset)
    ImageButton reset;

    @Bind({R.id.conditionused, R.id.conditionnew, R.id.conditionall})
    List<TextView> jewellerycondition;

    @Bind(R.id.rangebar1)
    RangeBar rangebar;

    @Bind(R.id.price)
    TextView price;

    static String[] conditionlist = new String[]{"", ""};


    int jewellery[] = new int[]{0, 0, 0, 0, 0, 0};
    int metal[] = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    int size[] = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int condition[] = new int[]{0, 0, 0};

    static String[] sizelist = new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
    static String[] metaltypelist = new String[]{"", "", "", "", "", "", "", ""};
    static String[] jewellerytypelist = new String[]{"", "", "", "", ""};


    static int price1 = 0;
    static int price2 = 1000;
    LinearLayout ringsizelayout;
    ValueAnimator mAnimator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        LndShopActivity.filterselected = 4;

        View view = inflater
                .inflate(R.layout.jwellery_filter_page, container, false);
        ButterKnife.bind(this, view);
        TextView btn = (TextView) view.findViewById(R.id.choosecate);
        ringsizelayout = (LinearLayout) view.findViewById(R.id.ringsizelayout);

        //jewellery type listener
        for (int i = 0; i < jewellerytype.size(); i++) {
            jewellerytype.get(i).setOnClickListener(this);
        }

        //metal type listener
        for (int i = 0; i < metaltype.size(); i++) {
            metaltype.get(i).setOnClickListener(this);
        }

        //ring size listener
        for (int i = 0; i < ringsize.size(); i++) {
            ringsize.get(i).setOnClickListener(this);
        }


        //jewellery condition listener
        for (int i = 0; i < jewellerycondition.size(); i++) {
            jewellerycondition.get(i).setOnClickListener(this);
        }


        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                /*
                 * IMPORTANT: We use the "root frame" defined in
				 * "root_fragment.xml" as the reference to replace fragment
				 */
                trans.replace(R.id.root_frame, new CategoryFragment());

				/*
                 * IMPORTANT: The following lines allow us to add the fragment
				 * to the stack and return to it later, by pressing back
				 */
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                //trans.addToBackStack(null);

                trans.commit();
            }
        });

        //range bar
        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String minValue, String maxValue) {
                int value2 = Integer.parseInt(minValue.toString());
                int value1 = Integer.parseInt(maxValue.toString());

                if (value1 < 1000)
                    price.setText("$" + value2 + " - " + "$" + value1);
                else
                    price.setText("$" + value2 + " - " + "$" + value1 + "+");

                price1 = value2;
                price2 = value1;
            }
        });

        //reset button
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
        return view;
    }

    private void reset() {
        price.setText("$0 - " + "$1000+");
        //dress size1
        for (int i = 0; i < jewellerytype.size() - 1; i++) {
            jewellerytype.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
            jewellerytypelist[i] = "";
            jewellery[i] = 0;
        }
        jewellery[jewellerytype.size() - 1] = 0;
//dress size2
        for (int i = 0; i < ringsize.size() - 1; i++) {
            ringsize.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
            sizelist[i] = "";
            size[i] = 0;
        }
        size[ringsize.size() - 1] = 0;
        //dress rest
        for (int i = 0; i < metaltype.size() - 1; i++) {
            metaltype.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
            metaltypelist[i] = "";
            metal[i] = 0;
        }
        metal[metaltype.size() - 1] = 0;

        //condition reset
        for (int i = 0; i < jewellerycondition.size() - 1; i++) {
            jewellerycondition.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
            conditionlist[i] = "";
            condition[i] = 0;
        }
        condition[jewellerycondition.size() - 1] = 0;

        //range bar
        rangebar.setRangePinsByValue(0, 1000);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            //for jewellerytype events
            case R.id.type1:

                if (jewellery[0] == 0) {

                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    jewellery[0] = 1;
                    jewellerytypelist[0] = "1";
                } else {

                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    jewellery[0] = 0;
                    jewellerytypelist[0] = "";
                }
                break;
            case R.id.type2:

                if (jewellery[1] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    jewellery[1] = 1;
                    jewellerytypelist[1] = "2";
                    expand();

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    jewellery[1] = 0;
                    jewellerytypelist[1] = "";
                    collapse();
                }
                break;
            case R.id.type3:

                if (jewellery[2] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    jewellery[2] = 1;
                    jewellerytypelist[2] = "3";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));

                    jewellery[2] = 0;
                    jewellerytypelist[2] = "";

                }
                break;
            case R.id.type4:
                collapse();
                if (jewellery[3] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    jewellery[3] = 1;
                    jewellerytypelist[3] = "4";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));

                    jewellery[3] = 0;
                    jewellerytypelist[3] = "";

                }
                break;
            case R.id.type5:
                collapse();
                if (jewellery[4] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    jewellery[4] = 1;
                    jewellerytypelist[4] = "5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));

                    jewellery[4] = 0;
                    jewellerytypelist[4] = "";

                }
                break;
            case R.id.jewelleryall:

                if (jewellery[5] == 0) {
                    for (int i = 0; i < jewellerytype.size() - 1; i++) {
                        jewellerytype.get(i).setBackgroundColor(Color.parseColor("#be4d66"));
                        jewellerytypelist[i] = (i + 1) + "";
                    }
                    jewellery[5] = 1;
                    expand();

                } else {
                    for (int i = 0; i < jewellerytype.size() - 1; i++) {
                        jewellerytype.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
                        jewellerytypelist[i] = "";
                    }
                    jewellery[5] = 0;

                    collapse();
                }
                break;

            //events for size
            case R.id.size1:
                if (size[0] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[0] = 1;
                    sizelist[0] = "OS";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[0] = 0;
                    sizelist[0] = "";
                }
                break;
            case R.id.size2:
                if (size[1] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[1] = 1;
                    sizelist[1] = "1";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[1] = 0;
                    sizelist[1] = "";
                }
                break;
            case R.id.size3:
                if (size[2] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[2] = 1;
                    sizelist[2] = "1.5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[2] = 0;
                    sizelist[2] = "";
                }
                break;
            case R.id.size4:
                if (size[3] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[3] = 1;
                    sizelist[3] = "2";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[3] = 0;
                    sizelist[3] = "";

                }
                break;
            case R.id.size5:
                if (size[4] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[4] = 1;
                    sizelist[4] = "2.5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[4] = 0;
                    sizelist[4] = "";

                }
                break;
            case R.id.size6:
                if (size[5] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[5] = 1;
                    sizelist[5] = "3";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[5] = 0;
                    sizelist[5] = "";
                }
                break;
            case R.id.size7:
                if (size[6] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[6] = 1;
                    sizelist[6] = "3.5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[6] = 0;
                    sizelist[6] = "";

                }
                break;
            case R.id.size8:
                if (size[7] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[7] = 1;
                    sizelist[7] = "4";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[7] = 0;
                    sizelist[7] = "";

                }
                break;
            case R.id.size9:


                if (size[8] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[8] = 1;
                    sizelist[8] = "4.5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[8] = 0;
                    sizelist[8] = "";

                }
                break;
            case R.id.size10:


                if (size[9] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[9] = 1;
                    sizelist[9] = "6";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[9] = 0;
                    sizelist[9] = "";

                }
                break;
            case R.id.size11:


                if (size[10] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[10] = 1;
                    sizelist[10] = "6.5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[10] = 0;
                    sizelist[10] = "";

                }
                break;
            case R.id.size12:


                if (size[11] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[11] = 1;
                    sizelist[11] = "7";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[11] = 0;
                    sizelist[11] = "";

                }
                break;
            case R.id.size13:


                if (size[12] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[12] = 1;
                    sizelist[12] = "7.5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[12] = 0;
                    sizelist[12] = "";

                }
                break;
            case R.id.size14:


                if (size[13] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[13] = 1;
                    sizelist[13] = "8";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[13] = 0;
                    sizelist[13] = "";

                }
                break;
            case R.id.size15:


                if (size[14] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[14] = 1;
                    sizelist[14] = "8.5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[14] = 0;
                    sizelist[14] = "";

                }
                break;
            case R.id.size16:


                if (size[15] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[15] = 1;
                    sizelist[15] = "9";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[15] = 0;
                    sizelist[15] = "";

                }
                break;
            case R.id.size17:


                if (size[16] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[16] = 1;
                    sizelist[16] = "9.5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[16] = 0;
                    sizelist[16] = "";

                }
                break;
            case R.id.size18:


                if (size[17] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[17] = 1;
                    sizelist[17] = "10";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[17] = 0;
                    sizelist[17] = "";

                }
                break;
            case R.id.size19:


                if (size[18] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[18] = 1;
                    sizelist[18] = "10.5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[18] = 0;
                    sizelist[18] = "";

                }
                break;
            case R.id.size20:


                if (size[19] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[19] = 1;
                    sizelist[19] = "11";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[19] = 0;
                    sizelist[19] = "";

                }

                break;

            case R.id.size21:


                if (size[20] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[20] = 1;
                    sizelist[20] = "11.5+";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    size[20] = 0;
                    sizelist[20] = "";

                }

                break;
            case R.id.sizeall:
                if (size[21] == 0) {
                    for (int i = 0; i < ringsize.size() - 1; i++)
                        ringsize.get(i).setBackgroundColor(Color.parseColor("#be4d66"));
                    size[21] = 1;
                    sizelist[0] = "OS";
                    sizelist[1] = "1";
                    sizelist[2] = "1.5";
                    sizelist[3] = "2";
                    sizelist[4] = "2.5";
                    sizelist[5] = "3";
                    sizelist[6] = "3.5";
                    sizelist[7] = "4";
                    sizelist[8] = "4.5";
                    sizelist[9] = "6";
                    sizelist[10] = "6.5";
                    sizelist[11] = "7";
                    sizelist[12] = "7.5";
                    sizelist[13] = "8";
                    sizelist[14] = "8.5";
                    sizelist[15] = "9";
                    sizelist[16] = "9.5";
                    sizelist[17] = "10";
                    sizelist[18] = "10.5";
                    sizelist[19] = "11";
                    sizelist[20] = "11+";

                } else {
                    for (int i = 0; i < ringsize.size() - 1; i++) {
                        ringsize.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
                        sizelist[i] = "";
                    }
                    size[21] = 0;


                }
                break;


//for metaltype events
            case R.id.metal1:
                if (metal[0] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    metal[0] = 1;
                    metaltypelist[0] = "1";
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    metal[0] = 0;
                    metaltypelist[0] = "";
                }
                break;
            case R.id.metal2:
                if (metal[1] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    metal[1] = 1;
                    metaltypelist[1] = "2";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    metal[1] = 0;
                    metaltypelist[1] = "";

                }
                break;
            case R.id.metal3:
                if (metal[2] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    metal[2] = 1;
                    metaltypelist[2] = "3";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));

                    metal[2] = 0;
                    metaltypelist[2] = "";

                }
                break;
            case R.id.metal4:
                if (metal[3] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    metal[3] = 1;
                    metaltypelist[3] = "4";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));

                    metal[3] = 0;
                    metaltypelist[3] = "";

                }
                break;
            case R.id.metal5:
                if (metal[4] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    metal[4] = 1;
                    metaltypelist[4] = "5";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));

                    metal[4] = 0;
                    metaltypelist[4] = "";

                }
                break;
            case R.id.metal6:
                if (metal[5] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    metal[5] = 1;
                    metaltypelist[5] = "6";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));

                    metal[5] = 0;
                    metaltypelist[5] = "";

                }
                break;
            case R.id.metal7:
                if (metal[6] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    metal[6] = 1;
                    metaltypelist[6] = "7";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));

                    metal[6] = 0;
                    metaltypelist[6] = "";

                }
                break;
            case R.id.metal8:
                if (metal[7] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    metal[7] = 1;
                    metaltypelist[7] = "8";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));

                    metal[7] = 0;
                    metaltypelist[7] = "";

                }
                break;


            case R.id.metalall:
                if (metal[8] == 0) {
                    for (int i = 0; i < metaltype.size() - 1; i++) {
                        metaltype.get(i).setBackgroundColor(Color.parseColor("#be4d66"));
                        metaltypelist[i] = (i + 1) + "";
                    }
                    metal[8] = 1;


                } else {
                    for (int i = 0; i < metaltype.size() - 1; i++) {
                        metaltype.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
                        metaltypelist[i] = "";
                    }
                    metal[8] = 0;


                }
                break;


// condition events
            case R.id.conditionnew:
                if (condition[0] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    condition[0] = 1;
                    conditionlist[0] = "11";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    condition[0] = 0;
                    conditionlist[0] = "";

                }
                break;

            case R.id.conditionused:
                if (condition[1] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    condition[1] = 1;
                    conditionlist[1] = "used";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    condition[1] = 0;
                    conditionlist[1] = "";

                }
                break;

            case R.id.conditionall:
                if (condition[2] == 0) {
                    jewellerycondition.get(0).setBackgroundColor(Color.parseColor("#be4d66"));
                    jewellerycondition.get(1).setBackgroundColor(Color.parseColor("#be4d66"));

                    condition[2] = 1;
                    conditionlist[0] = "used";
                    conditionlist[1] = "11";

                } else {
                    jewellerycondition.get(0).setBackgroundColor(Color.parseColor("#1d1f21"));
                    jewellerycondition.get(1).setBackgroundColor(Color.parseColor("#1d1f21"));

                    condition[2] = 0;
                    conditionlist[0] = "";
                    conditionlist[1] = "";

                }
                break;
        }

    }

    private void collapse() {
        int finalHeight = ringsizelayout.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                ringsizelayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }


    private void expand() {


        ringsizelayout.setVisibility(View.VISIBLE);

        // Remove and used in preDrawListener
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        ringsizelayout.measure(widthSpec, heightSpec);

        mAnimator = slideAnimator(0, ringsizelayout.getMeasuredHeight());


        mAnimator.start();

    }

    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = ringsizelayout.getLayoutParams();
                layoutParams.height = value;
                ringsizelayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}
