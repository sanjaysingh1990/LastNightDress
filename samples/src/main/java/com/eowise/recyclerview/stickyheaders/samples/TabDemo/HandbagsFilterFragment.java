package com.eowise.recyclerview.stickyheaders.samples.TabDemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
 * 
 */
public class HandbagsFilterFragment extends Fragment implements View.OnClickListener {

	@Bind({R.id.size1, R.id.size2, R.id.size3, R.id.size4})
	List<TextView> handbagsize;

	@Bind({R.id.purses, R.id.clutches, R.id.pursetypeall})
	List<TextView> handbagtype;

	@Bind({R.id.conditionused, R.id.conditionnew, R.id.conditionall})
	List<TextView> handbagcondition;

	@Bind({R.id.color1,R.id.color2,R.id.color3,R.id.color4,R.id.color5,R.id.color6,R.id.color7,R.id.color8,R.id.color9,R.id.color10,R.id.color11,R.id.color12,R.id.color13,R.id.color14,R.id.color15,R.id.color16,})
	List<TextView> color;
	@Bind(R.id.rangebar1)RangeBar rangebar;
	@Bind(R.id.price)TextView price;

	@Bind(R.id.reset)
	ImageButton reset;

	static String[] conditionlist=new String[]{"",""};
	static String[] sizelist = new String[]{"", "", ""};
	static String[] handbagtypelist=new String[]{"",""};
	static String[] colorlist = new String[] {"", "","","","","","","","", "","","","","",""};

	int col[] = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	int condi[]=new int[] {0,0,0};
	int dresstype[]=new int[] {0,0,0};
	int bagsize[] = new int[]{0,0,0,0};

	static int price1=0;
	static int price2=1000;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		LndShopActivity.filterselected = 2;
		// Inflate the layout for this fragment
		View view = inflater
				.inflate(R.layout.handbags_filter_page, container, false);
		ButterKnife.bind(this,view);
			TextView btn = (TextView) view.findViewById(R.id.choosecate);


		//handbag type listener
		for (int i = 0; i < handbagtype.size(); i++) {
			handbagtype.get(i).setOnClickListener(this);
		}

		//handbag condition listener
		for (int i = 0; i < handbagcondition.size(); i++) {
			handbagcondition.get(i).setOnClickListener(this);
		}
		//size listener
		for (int i = 0; i < handbagsize.size(); i++) {
			handbagsize.get(i).setOnClickListener(this);
		}

		//color listener
		for (int i = 0; i < color.size(); i++) {
			color.get(i).setOnClickListener(this);
		}

		//condtion  events

		//business type events



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

				price1=value2;
				price2=value1;
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
		//handbag size
		for (int i = 0; i <handbagsize.size() - 1;i++) {
			handbagsize.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
			sizelist[i] = "";
			bagsize[i] = 0;
		}
//handbag type
		for (int i = 0; i < handbagtype.size() - 1; i++) {
			handbagtype.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
			handbagtypelist[i] = "";
			dresstype[i] = 0;
		}

		//handbag condition
		for (int i = 0; i <handbagcondition.size() - 1; i++) {
			handbagcondition.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
			conditionlist[i] = "";
			condi[i] = 0;
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

		switch(v.getId()){
			//for length events
			case R.id.purses:
				if(dresstype[0]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					dresstype[0]=1;
					handbagtypelist[0]="1";
				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					dresstype[0]=0;
					handbagtypelist[0]="";
				}
				break;
			case R.id.clutches:
				if(dresstype[1]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					dresstype[1]=1;
					handbagtypelist[1]="2";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					dresstype[1]=0;
					handbagtypelist[1]="";

				}
				break;
			case R.id.pursetypeall:
				if(dresstype[2]==0)
				{
					for(int i=0;i<handbagtype.size()-1;i++) {
						handbagtype.get(i).setBackgroundColor(Color.parseColor("#be4d66"));
						handbagtypelist[i]=(i+1)+"";
						  }
						dresstype[2]=1;

				}
				else
				{

					for(int i=0;i<handbagtype.size()-1;i++) {
						handbagtype.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
						handbagtypelist[i]="";
					}
					dresstype[2]=0;

				}
				break;

				//events for size
			case R.id.size1:
				if (bagsize[0] == 0) {
					((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
					bagsize[0] = 1;
					sizelist[0] = "1";

				} else {
					((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
					bagsize[0] = 0;
					sizelist[0] = "";
				}
				break;
			case R.id.size2:
				if (bagsize[1] == 0) {
					((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
					bagsize[1] = 1;
					sizelist[1] = "2";

				} else {
					((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
					bagsize[1] = 0;
					sizelist[1] = "";
				}
				break;
			case R.id.size3:
				if (bagsize[2] == 0) {
					((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
					bagsize[2] = 1;
					sizelist[2] = "3";

				} else {
					((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
					bagsize[2] = 0;
					sizelist[2] = "";
				}
				break;
			case R.id.size4:
				if(bagsize[3]==0)
				{
					for(int i=0;i<handbagsize.size()-1;i++)
						handbagsize.get(i).setBackgroundColor(Color.parseColor("#be4d66"));
					bagsize[3]=1;
					sizelist[0]="1";
					sizelist[1]="2";
					sizelist[2]="3";

				}
				else {
					for (int i = 0; i < handbagsize.size() - 1; i++)
					{
						handbagsize.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
						sizelist[i] = "";
					}
					bagsize[3] = 0;


				}
				break;


			//condition events
			case R.id.conditionnew:
				if(condi[0]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					condi[0]=1;
					conditionlist[0]="11";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					condi[0]=0;
					conditionlist[0]="";

				}
				break;
			case R.id.conditionused:
				if(condi[1]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					condi[1]=1;
					conditionlist[1]="used";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					condi[1]=1;
					conditionlist[1]="";

				}
				break;

			case R.id.conditionall:
				if(condi[2]==0)
				{
					for (int i = 0; i <handbagcondition.size() - 1; i++)
					{
						handbagcondition.get(i).setBackgroundColor(Color.parseColor("#be4d66"));
					}
					conditionlist[0]="used";
					conditionlist[1]="11";
					condi[2]=1;


				}
				else
				{
					for (int i = 0; i <handbagcondition.size() - 1; i++)
					{
						handbagcondition.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
						conditionlist[i] = "";
					}

					condi[2]=0;


				}
				break;

			//color events
			case R.id.color1:
				if(col[0]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					col[0]=1;
					colorlist[0]="black";
				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					col[0]=0;
					colorlist[0]="";
				}
				break;
			case R.id.color2:
				if(col[1]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					col[1]=1;
					colorlist[1]="silver";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					col[1]=0;
					colorlist[1]="";
				}
				break;
			case R.id.color3:
				if(col[2]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					col[2]=1;
					colorlist[2]="orange";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					col[2]=0;
					colorlist[2]="";
				}
				break;
			case R.id.color4:
				if(col[3]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					col[3]=1;
					colorlist[3]="white";
				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					col[3]=0;
					colorlist[3]="";
				}
			case R.id.color5:
				if(col[4]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					col[4]=1;
					colorlist[4]="gold";
				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					col[4]=0;
					colorlist[4]="";

				}
				break;
			case R.id.color6:
				if(col[5]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					col[5]=1;
					colorlist[5]="brown";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					col[5]=0;
					colorlist[5]="";

				}
				break;
			case R.id.color7:
				if(col[6]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					col[6]=1;
					colorlist[6]="red";
				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					col[6]=0;
					colorlist[6]="";

				}
				break;
			case R.id.color8:
				if(col[7]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					col[7]=1;
					colorlist[7]="purple";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					col[7]=0;
					colorlist[7]="";

				}
				break;
			case R.id.color9:
				if(col[8]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					col[8]=1;
					colorlist[8]="nude";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					col[8]=0;
					colorlist[8]="";


				}
				break;
			case R.id.color10:
				if(col[9]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					col[9]=1;
					colorlist[9]="blue";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					col[9]=0;
					colorlist[9]="";

				}
				break;
			case R.id.color11:
				if(col[10]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					col[10]=1;
					colorlist[10]="yellow";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					col[10]=0;
					colorlist[10]="";

				}
				break;
			case R.id.color12:
				if(col[11]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					col[11]=1;
					colorlist[11]="gray";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					col[11]=0;
					colorlist[11]="";

				}
				break;
			case R.id.color13:
				if(col[12]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					col[12]=1;
					colorlist[12]="green";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					col[12]=0;
					colorlist[12]="";

				}
				break;
			case R.id.color14:
				if(col[13]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					col[13]=1;
					colorlist[13]="pink";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					col[13]=0;
					colorlist[13]="";

				}
				break;
			case R.id.color15:
				if(col[14]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					col[14]=1;
					colorlist[14]="pattern";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					col[14]=0;
					colorlist[14]="";

				}
				break;

			case R.id.color16:
				if(col[15]==0)
				{
					for(int i=0;i<color.size()-1;i++)
						color.get(i).setBackgroundColor(Color.parseColor("#be4d66"));
					col[15]=1;
					colorlist[0]="black";
					colorlist[1]="silver";
					colorlist[2]="orange";
					colorlist[3]="white";
					colorlist[4]="gold";
					colorlist[5]="brown";
					colorlist[6]="red";
					colorlist[7]="purple";
					colorlist[8]="nude";
					colorlist[9]="blue";
					colorlist[10]="yellow";
					colorlist[11]="gray";
					colorlist[12]="green";
					colorlist[13]="pink";
					colorlist[14]="pattern";

				}
				else
				{
					for(int i=0;i<color.size()-1;i++) {
						color.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
						colorlist[i]="";
					}
					col[15]=0;


				}
				break;

		}

	}
}
