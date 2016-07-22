package com.eowise.recyclerview.stickyheaders.samples.TabDemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;
import com.eowise.recyclerview.stickyheaders.samples.R;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.shape.RectangleShape;

/**
 * Example about replacing fragments inside a ViewPager. I'm using
 * android-support-v7 to maximize the compatibility.
 *
 * @author Dani Lao (@dani_lao)
 */
public class DressFilterFragment extends Fragment implements OnClickListener {

    @Bind({R.id.size1, R.id.size2, R.id.size3, R.id.size4, R.id.size5, R.id.size6, R.id.size7, R.id.sizealltext})
    List<TextView> dresssize1;
    @Bind({R.id.numsize1, R.id.numsize2, R.id.numsize3, R.id.numsize4, R.id.numsize5, R.id.numsize6, R.id.numsize7, R.id.numsize8, R.id.numsize9, R.id.numsize10, R.id.numsize11, R.id.numsize12, R.id.sizeallnum})
    List<TextView> dresssize2;

    @Bind({R.id.dresstype1, R.id.dresstype2, R.id.dresstype3, R.id.dresstype4, R.id.dressall})
    List<TextView> dresstype;
    @Bind({R.id.lastnightdress, R.id.conditionused, R.id.conditionnew, R.id.conditionall})
    List<TextView> dresscondition;


    @Bind(R.id.rangebar1)
    RangeBar rangebar;

    @Bind(R.id.reset)
    ImageButton reset;
    @Bind(R.id.price)
    TextView price;
    @Bind({R.id.color1, R.id.color2, R.id.color3, R.id.color4, R.id.color5, R.id.color6, R.id.color7, R.id.color8, R.id.color9, R.id.color10, R.id.color11, R.id.color12, R.id.color13, R.id.color14, R.id.color15, R.id.color16})
    List<TextView> color;

    static String[] sizetypelist1 = new String[]{"", "", "", "", "", "", ""};
    static String[] sizetypelist2 = new String[]{"", "", "", "", "", "", "", "", "", "", "", ""};
    static String[] dresstypelist = new String[]{"", "", "", ""};
    static String[] conditionlist = new String[]{"", "", ""};
    static String[] colorlist = new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

    int col[] = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    int sizetype1[] = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
    int sizetype2[] = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int dresssize[] = new int[]{0, 0, 0, 0, 0};

    static int price1=0;
    static int price2=1000;
    int condition[] = {0, 0, 0, 0};
//for tutorial page
    @Bind(R.id.swipe_instruction) View swiperight;
    @Bind(R.id.dress_tutorial_heading) View turorialheaidng;
    @Bind(R.id.dress_tutorial_subheading) View turorialsubheaidng;
    @Bind(R.id.choosecate)TextView btn;
    Animation anim1,anim2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LndShopActivity.filterselected = 1;
        View view = inflater.inflate(R.layout.dress_filter_page, container, false);
        ButterKnife.bind(this, view);

        //initialize animations
        anim1= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_out);


        //size1 listener
        for (int i = 0; i < dresssize1.size(); i++) {
            dresssize1.get(i).setOnClickListener(this);
        }

        //size2 listener
        for (int i = 0; i < dresssize2.size(); i++) {
            dresssize2.get(i).setOnClickListener(this);
        }
//dress type listener
        for (int i = 0; i < dresstype.size(); i++) {
            dresstype.get(i).setOnClickListener(this);
        }


//color listener
        for (int i = 0; i < color.size(); i++) {
            color.get(i).setOnClickListener(this);
        }
//condtion  events
        for (int i = 0; i < dresscondition.size(); i++) {
            dresscondition.get(i).setOnClickListener(this);
        }


        btn.setOnClickListener(new OnClickListener() {

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
               price1=value2;
               price2=value1;
            }
        });


        //reset button
        reset.setOnClickListener(new OnClickListener() {
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
        for (int i = 0; i < dresssize1.size() - 1
                ; i++) {
            dresssize1.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
            sizetypelist1[i] = "";
            sizetype1[i] = 0;
        }
//dress size2
        for (int i = 0; i < dresssize2.size() - 1; i++) {
            dresssize2.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
            sizetypelist2[i] = "";
            sizetype2[i] = 0;
        }

        //dress rest
        for (int i = 0; i < dresstype.size() - 1; i++) {
            dresstype.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
            dresstypelist[i] = "";
            dresssize[i] = 0;
        }
        //condition reset
        for (int i = 0; i < dresscondition.size() - 1; i++) {
            dresscondition.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
            conditionlist[i] = "";
            condition[i] = 0;
        }
        //color reset
        for (int i = 0; i < color.size() - 1; i++) {
            color.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
            colorlist[i] = "";
            col[i] = 0;
        }

        //range bar
        rangebar.setRangePinsByValue(0, 1000);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.size1 || v.getId() == R.id.size2 || v.getId() == R.id.size3 || v.getId() == R.id.size4 || v.getId() == R.id.size5 || v.getId() == R.id.size6 || v.getId() == R.id.size7)
            clearSizetype2();
        else if (v.getId() == R.id.numsize1 || v.getId() == R.id.numsize2 || v.getId() == R.id.numsize3 || v.getId() == R.id.numsize4 || v.getId() == R.id.numsize5 || v.getId() == R.id.numsize6 || v.getId() == R.id.numsize7 || v.getId() == R.id.numsize8 || v.getId() == R.id.numsize9 || v.getId() == R.id.numsize10 || v.getId() == R.id.numsize11 || v.getId() == R.id.numsize12)
            clearSizetype1();


        switch (v.getId()) {

            //events for size
            case R.id.size1:
                if (sizetype1[0] == 0) {

                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype1[0] = 1;
                    sizetypelist1[0] = "2xs";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype1[0] = 0;
                    sizetypelist1[0] = "";
                }
                break;
            case R.id.size2:
                if (sizetype1[1] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype1[1] = 1;
                    sizetypelist1[1] = "xs";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype1[1] = 0;
                    sizetypelist1[1] = "";
                }
                break;
            case R.id.size3:
                if (sizetype1[2] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype1[2] = 1;
                    sizetypelist1[2] = "s";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype1[2] = 0;
                    sizetypelist1[2] = "";
                }
                break;
            case R.id.size4:
                if (sizetype1[3] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype1[3] = 1;
                    sizetypelist1[3] = "m";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype1[3] = 0;
                    sizetypelist1[3] = "";

                }
                break;
            case R.id.size5:
                if (sizetype1[4] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype1[4] = 1;
                    sizetypelist1[4] = "l";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype1[4] = 0;
                    sizetypelist1[4] = "";

                }
                break;
            case R.id.size6:
                if (sizetype1[5] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype1[5] = 1;
                    sizetypelist1[5] = "xl";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype1[5] = 0;
                    sizetypelist1[5] = "";
                }
                break;
            case R.id.size7:
                if (sizetype1[6] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype1[6] = 1;
                    sizetypelist1[6] = "2xl";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype1[6] = 0;
                    sizetypelist1[6] = "";

                }
                break;
            case R.id.sizealltext:


                if (sizetype1[7] == 0) {
                    for (int i = 0; i < dresssize1.size() - 1; i++)
                        dresssize1.get(i).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype1[7] = 1;
                    sizetypelist1[0] = "2xs";
                    sizetypelist1[1] = "xs";
                    sizetypelist1[2] = "s";
                    sizetypelist1[3] = "m";
                    sizetypelist1[4] = "l";
                    sizetypelist1[5] = "xl";
                    sizetypelist1[6] = "2xl";


                } else {
                    for (int i = 0; i < dresssize1.size() - 1; i++) {
                        dresssize1.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
                        sizetypelist1[i] = "";
                    }
                    sizetype1[7] = 0;


                }
                break;

            case R.id.numsize1:
                if (sizetype2[0] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[0] = 1;
                    sizetypelist2[0] = "00";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[0] = 0;
                    sizetypelist2[0] = "";

                }
                break;
            case R.id.numsize2:


                if (sizetype2[1] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[1] = 1;
                    sizetypelist2[1] = "0";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[1] = 0;
                    sizetypelist2[1] = "";

                }
                break;
            case R.id.numsize3:


                if (sizetype2[2] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[2] = 1;
                    sizetypelist2[2] = "2";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[2] = 0;
                    sizetypelist2[2] = "";

                }
                break;
            case R.id.numsize4:


                if (sizetype2[3] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[3] = 1;
                    sizetypelist2[3] = "4";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[3] = 0;
                    sizetypelist2[3] = "";

                }
                break;
            case R.id.numsize5:


                if (sizetype2[4] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[4] = 1;
                    sizetypelist2[4] = "6";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[4] = 0;
                    sizetypelist2[4] = "";

                }
                break;
            case R.id.numsize6:


                if (sizetype2[5] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[5] = 1;
                    sizetypelist2[5] = "8";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[5] = 0;
                    sizetypelist2[5] = "";

                }
                break;
            case R.id.numsize7:


                if (sizetype2[6] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[6] = 1;
                    sizetypelist2[6] = "10";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[6] = 0;
                    sizetypelist2[7] = "";

                }
                break;
            case R.id.numsize8:


                if (sizetype2[7] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[7] = 1;
                    sizetypelist2[7] = "12";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[7] = 0;
                    sizetypelist2[7] = "";

                }
                break;
            case R.id.numsize9:


                if (sizetype2[8] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[8] = 1;
                    sizetypelist2[8] = "14";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[8] = 0;
                    sizetypelist2[8] = "";

                }
                break;
            case R.id.numsize10:


                if (sizetype2[9] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[9] = 1;
                    sizetypelist2[9] = "16";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[9] = 0;
                    sizetypelist2[9] = "";

                }
                break;
            case R.id.numsize11:


                if (sizetype2[10] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[10] = 1;
                    sizetypelist2[10] = "18";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[10] = 0;
                    sizetypelist2[10] = "";

                }
                break;
            case R.id.numsize12:


                if (sizetype2[11] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[11] = 1;
                    sizetypelist2[11] = "19";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    sizetype2[11] = 0;
                    sizetypelist2[11] = "";

                }
                break;
            case R.id.sizeallnum:


                if (sizetype2[12] == 0) {
                    for (int i = 0; i < dresssize2.size() - 1; i++)
                        dresssize2.get(i).setBackgroundColor(Color.parseColor("#be4d66"));
                    sizetype2[12] = 1;

                    sizetypelist2[0] = "00";
                    sizetypelist2[1] = "0";
                    sizetypelist2[2] = "2";
                    sizetypelist2[3] = "4";
                    sizetypelist2[4] = "6";
                    sizetypelist2[5] = "8";
                    sizetypelist2[6] = "10";
                    sizetypelist2[7] = "12";
                    sizetypelist2[8] = "14";
                    sizetypelist2[9] = "16";
                    sizetypelist2[10] = "18";
                    sizetypelist2[11] = "20+";


                } else {
                    for (int i = 0; i < dresssize2.size() - 1; i++) {
                        dresssize2.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
                        sizetypelist2[i] = "";
                    }
                    sizetype2[12] = 0;


                }
                break;

            //for length events
            case R.id.dresstype1:
                if (dresssize[0] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    dresssize[0] = 1;
                    dresstypelist[0] = "1";
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    dresssize[0] = 0;
                    dresstypelist[0] = "";
                }
                break;
            case R.id.dresstype2:
                if (dresssize[1] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    dresssize[1] = 1;
                    dresstypelist[1] = "2";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    dresssize[1] = 0;
                    dresstypelist[1] = "";

                }
                break;
            case R.id.dresstype3:
                if (dresssize[2] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    dresssize[2] = 1;
                    dresstypelist[2] = "3";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));

                    dresssize[2] = 0;
                    dresstypelist[2] = "";

                }
                break;
            case R.id.dresstype4:
                if (dresssize[3] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    dresssize[3] = 1;
                    dresstypelist[3] = "4";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));

                    dresssize[3] = 0;
                    dresstypelist[3] = "";

                }
                break;
            case R.id.dressall:
                if (dresssize[4] == 0) {
                    dresssize[4] = 1;
                    for (int i = 0; i < dresstype.size() - 1; i++) {
                        dresstype.get(i).setBackgroundColor(Color.parseColor("#be4d66"));
                        dresstypelist[i] =(i+1)+"";

                    }


                } else {

                    dresssize[4] = 0;
                    for (int i = 0; i < dresstype.size() - 1; i++) {
                        dresstype.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
                        dresstypelist[i] ="";

                    }

                }
                break;


//color events
            case R.id.color1:
                if (col[0] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[0] = 1;
                    colorlist[0] = "black";
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[0] = 0;
                    colorlist[0] = "";
                }
                break;
            case R.id.color2:
                if (col[1] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[1] = 1;
                    colorlist[1] = "silver";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[1] = 0;
                    colorlist[1] = "";
                }
                break;
            case R.id.color3:
                if (col[2] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[2] = 1;
                    colorlist[2] = "orange";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[2] = 0;
                    colorlist[2] = "";
                }
                break;
            case R.id.color4:
                if (col[3] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[3] = 1;
                    colorlist[3] = "white";
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[3] = 0;
                    colorlist[3] = "";
                }
            case R.id.color5:
                if (col[4] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[4] = 1;
                    colorlist[4] = "gold";
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[4] = 0;
                    colorlist[4] = "";

                }
                break;
            case R.id.color6:
                if (col[5] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[5] = 1;
                    colorlist[5] = "brown";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[5] = 0;
                    colorlist[5] = "";

                }
                break;
            case R.id.color7:
                if (col[6] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[6] = 1;
                    colorlist[6] = "red";
                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[6] = 0;
                    colorlist[6] = "";

                }
                break;
            case R.id.color8:
                if (col[7] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[7] = 1;
                    colorlist[7] = "purple";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[7] = 0;
                    colorlist[7] = "";

                }
                break;
            case R.id.color9:
                if (col[8] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[8] = 1;
                    colorlist[8] = "nude";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[8] = 0;
                    colorlist[8] = "";


                }
                break;
            case R.id.color10:
                if (col[9] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[9] = 1;
                    colorlist[9] = "blue";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[9] = 0;
                    colorlist[9] = "";

                }
                break;
            case R.id.color11:
                if (col[10] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[10] = 1;
                    colorlist[10] = "yellow";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[10] = 0;
                    colorlist[10] = "";

                }
                break;
            case R.id.color12:
                if (col[11] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[11] = 1;
                    colorlist[11] = "gray";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[11] = 0;
                    colorlist[11] = "";

                }
                break;
            case R.id.color13:
                if (col[12] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[12] = 1;
                    colorlist[12] = "green";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[12] = 0;
                    colorlist[12] = "";

                }
                break;
            case R.id.color14:
                if (col[13] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[13] = 1;
                    colorlist[13] = "pink";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[13] = 0;
                    colorlist[13] = "";

                }
                break;
            case R.id.color15:
                if (col[14] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[14] = 1;
                    colorlist[14] = "pattern";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    col[14] = 0;
                    colorlist[14] = "";

                }
                break;

            case R.id.color16:
                if (col[15] == 0) {
                    for (int i = 0; i < color.size() - 1; i++)
                        color.get(i).setBackgroundColor(Color.parseColor("#be4d66"));
                    col[15] = 1;
                    colorlist[0] = "black";
                    colorlist[1] = "silver";
                    colorlist[2] = "orange";
                    colorlist[3] = "white";
                    colorlist[4] = "gold";
                    colorlist[5] = "brown";
                    colorlist[6] = "red";
                    colorlist[7] = "purple";
                    colorlist[8] = "nude";
                    colorlist[9] = "blue";
                    colorlist[10] = "yellow";
                    colorlist[11] = "gray";
                    colorlist[12] = "green";
                    colorlist[13] = "pink";
                    colorlist[14] = "pattern";

                } else {
                    for (int i = 0; i < color.size() - 1; i++) {
                        color.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
                        colorlist[i] = "";
                    }
                    col[15] = 0;


                }
                break;
            //condition events
            case R.id.conditionnew:
                if (condition[0] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    condition[0] = 1;
                    conditionlist[0] = "12";

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
            case R.id.lastnightdress:
                if (condition[2] == 0) {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
                    condition[2] = 1;
                    conditionlist[2] = "11";

                } else {
                    ((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
                    condition[2] = 0;
                    conditionlist[2] = "";

                }
                break;
            case R.id.conditionall:
                if (condition[3] == 0) {

                    condition[3] = 1;
                    for (int i = 0; i < dresscondition.size() - 1; i++) {
                        dresscondition.get(i).setBackgroundColor(Color.parseColor("#be4d66"));
                    }
                    conditionlist[0] = "12";
                    conditionlist[1] = "used";
                    conditionlist[2] = "11";


                } else {

                    condition[3] = 0;
                    for (int i = 0; i < dresscondition.size() - 1; i++) {
                        dresscondition.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
                        conditionlist[i] = "";
                    }


                }
                break;

        }
    }

    private void clearSizetype1() {
        for (int i = 0; i < dresssize1.size() - 1; i++) {
            dresssize1.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
            sizetypelist1[i] = "";
        }
    }

    private void clearSizetype2() {
        for (int i = 0; i < dresssize2.size() - 1; i++) {
            dresssize2.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));

            sizetypelist2[i] = "";
        }
    }

}


