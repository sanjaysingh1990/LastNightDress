package com.eowise.recyclerview.stickyheaders.samples.TabDemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class ShoesFilterFragment extends Fragment implements View.OnClickListener {

	@Bind({R.id.size1, R.id.size2, R.id.size3, R.id.size4, R.id.size5, R.id.size6, R.id.size7,R.id.size8, R.id.size9, R.id.size10, R.id.size11, R.id.size12, R.id.size13, R.id.size14, R.id.size15, R.id.sizeall})
	List<TextView> shoesize;


	@Bind(R.id.conditionused)TextView conditionused;
	@Bind(R.id.conditionnew)TextView conditionnew;
	@Bind(R.id.conditionall)TextView conditionall;

	@Bind(R.id.rangebar1)RangeBar rangebar;
	@Bind(R.id.price)TextView price;

	@Bind({R.id.flats,R.id.pumps,R.id.platforms,R.id.boots,R.id.wedges,R.id.bridal,R.id.sandals,R.id.typeall})
	List<TextView> shoestype;

	String[] businesslist=new String[]{"","",""};
	String[] conditionlist=new String[]{"","",""};

	String[] sizelist = new String[]{"", "", "", "", "", "", "","", "", "", "", "", "", "","", ""};
	String[] colorlist = new String[] {"", "","","","","","","","", "","","","","",""};
	String[] shoetypelist=new String[]{"","","","","","","",""};
	int col[] = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	int shoe[] =new int[]{0,0,0,0,0,0,0,0};
	int size[] = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	@Bind({R.id.color1,R.id.color2,R.id.color3,R.id.color4,R.id.color5,R.id.color6,R.id.color7,R.id.color8,R.id.color9,R.id.color10,R.id.color11,R.id.color12,R.id.color13,R.id.color14,R.id.color15,R.id.color16,})
	List<TextView> color;


	int condinew=0,condiused=0,condiall=0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater
				.inflate(R.layout.shoes_filter_page, container, false);
		ButterKnife.bind(this, view);
		TextView btn = (TextView) view.findViewById(R.id.choosecate);

		//size listener
		for (int i = 0; i < shoesize.size(); i++) {
			shoesize.get(i).setOnClickListener(this);
		}

		//shoe type listener
		for (int i = 0; i < shoestype.size(); i++) {
			shoestype.get(i).setOnClickListener(this);
		}

		//color listener
		for (int i = 0; i < color.size(); i++) {
			color.get(i).setOnClickListener(this);
		}

		//condtion  events
		conditionnew.setOnClickListener(this);
		conditionused.setOnClickListener(this);
		conditionall.setOnClickListener(this);



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
			}
		});

		return view;
	}

	@Override
	public void onClick(View v) {


		switch (v.getId()){

			//events for size
			case R.id.size1:
				if (size[0] == 0) {
					((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
					size[0] = 1;
					sizelist[0] = "5";

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
					sizelist[1] = "5.5";

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
					sizelist[2] = "6";

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
					sizelist[3] = "6.5";

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
					sizelist[4] = "7";

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
					sizelist[5] = "7.5";

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
					sizelist[6] = "8";

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
					sizelist[7] = "8.5";

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
					sizelist[8] = "9";

				}
				else
				{
					((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
					size[8] = 0;
					sizelist[8] = "";

				}
				break;
			case R.id.size10:


				if (size[9] == 0) {
					((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
					size[9] = 1;
					sizelist[9] = "9.5";

				}
				else
				{
					((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
					size[9] = 0;
					sizelist[9] = "";

				}
				break;
			case R.id.size11:


				if (size[10] == 0) {
					((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
					size[10] = 1;
					sizelist[10] = "10";

				}
				else
				{
					((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
					size[10] = 0;
					sizelist[10] = "";

				}
				break;
			case R.id.size12:


				if (size[11] == 0) {
					((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
					size[11] = 1;
					sizelist[11] = "10.5";

				}
				else
				{
					((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
					size[11] = 0;
					sizelist[11] = "";

				}
				break;case R.id.size13:


				if (size[12] == 0) {
					((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
					size[12] = 1;
					sizelist[12] = "11";

				}
				else
				{
					((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
					size[12] = 0;
					sizelist[12] = "";

				}
				break;case R.id.size14:


				if (size[13] == 0) {
					((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
					size[13] = 1;
					sizelist[13] = "11.5";

				}
				else
				{
					((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
					size[13] = 0;
					sizelist[13] = "";

				}
				break;case R.id.size15:


				if (size[14] == 0) {
					((TextView) v).setBackgroundColor(Color.parseColor("#be4d66"));
					size[14] = 1;
					sizelist[14] = "12";

				}
				else
				{
					((TextView) v).setBackgroundColor(Color.parseColor("#1d1f21"));
					size[14] = 0;
					sizelist[14] = "";

				}
				break;
			case R.id.sizeall:
				if(size[15]==0)
				{
					for(int i=0;i<shoesize.size()-1;i++)
						shoesize.get(i).setBackgroundColor(Color.parseColor("#be4d66"));
					size[15]=1;
					sizelist[0]="5";
					sizelist[1]="5.5";
					sizelist[2]="6";
					sizelist[3]="6.5";
					sizelist[4]="7";
					sizelist[5]="7.5";
					sizelist[6]="8";
					sizelist[7]="8.5";
					sizelist[8]="9";
					sizelist[9]="9.5";
					sizelist[10]="10";
					sizelist[11]="10.5";
					sizelist[12]="11";
					sizelist[13]="11.5";
					sizelist[14]="12";
					sizelist[15]="All";

				}
				else {
					for (int i = 0; i < shoesize.size() - 1; i++)
					{
						shoesize.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
						sizelist[i] = "";
					}
					size[15] = 0;


				}
				break;




			//for shoetype events
			case R.id.flats:
				if(shoe[0]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					shoe[0]=1;
					shoetypelist[0]="flats";
				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					shoe[0]=0;
					shoetypelist[0]="";
				}
				break;
			case R.id.pumps:
				if(shoe[1]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					shoe[1]=1;
					shoetypelist[1]="pumps";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					shoe[1]=0;
					shoetypelist[1]="";

				}
				break;
			case R.id.platforms:
				if(shoe[2]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					shoe[2]=1;
					shoetypelist[2]="platforms";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));

					shoe[2]=0;
					shoetypelist[2]="";

				}
				break;
			case R.id.boots:
				if(shoe[3]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					shoe[3]=1;
					shoetypelist[3]="boots";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));

					shoe[3]=0;
					shoetypelist[3]="";

				}
				break;
			case R.id.wedges:
				if(shoe[4]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					shoe[4]=1;
					shoetypelist[4]="wedges";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));

					shoe[4]=0;
					shoetypelist[4]="";

				}
				break;
			case R.id.bridal:
				if(shoe[5]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					shoe[5]=1;
					shoetypelist[5]="bridal";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));

					shoe[5]=0;
					shoetypelist[5]="";

				}
				break;
			case R.id.sandals:
				if(shoe[6]==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					shoe[6]=1;
					shoetypelist[6]="sandals";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));

					shoe[6]=0;
					shoetypelist[6]="";

				}
				break;
			case R.id.typeall:
				if(shoe[7]==0)
				{
					for(int i=0;i<shoestype.size()-1;i++)
						shoestype.get(i).setBackgroundColor(Color.parseColor("#be4d66"));
					shoe[7]=1;
					shoetypelist[0]="flats";
					shoetypelist[1]="pumps";
					shoetypelist[2]="platforms";
					shoetypelist[3]="boots";
					shoetypelist[4]="wedges";
					shoetypelist[5]="bridal";
					shoetypelist[6]="sandals";
					shoetypelist[7]="all";

				}
				else {
					for (int i = 0; i < shoestype.size() - 1; i++)
					{
						shoestype.get(i).setBackgroundColor(Color.parseColor("#1d1f21"));
						shoetypelist[i] = "";
					}
					shoe[7] = 0;


				}
				break;

//condition events

			case R.id.conditionnew:
				if(condinew==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					condinew=1;
					conditionlist[0]="11";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					condinew=0;
					conditionlist[0]="";

				}
				break;
			case R.id.conditionused:
				if(condiused==0)
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#be4d66"));
					condiused=1;
					conditionlist[1]="used";

				}
				else
				{
					((TextView)v).setBackgroundColor(Color.parseColor("#1d1f21"));
					condiused=0;
					conditionlist[1]="";

				}
				break;
			case R.id.conditionall:
				if(condiall==0)
				{
					conditionnew.setBackgroundColor(Color.parseColor("#be4d66"));
					conditionused.setBackgroundColor(Color.parseColor("#be4d66"));

					condiall=1;
					conditionlist[0]="used";
					conditionlist[1]="11";

				}
				else
				{
					conditionused.setBackgroundColor(Color.parseColor("#1d1f21"));
					conditionnew.setBackgroundColor(Color.parseColor("#1d1f21"));

					condiall=1;
					conditionlist[0]="0";
					conditionlist[1]="";
					condiall=0;


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
