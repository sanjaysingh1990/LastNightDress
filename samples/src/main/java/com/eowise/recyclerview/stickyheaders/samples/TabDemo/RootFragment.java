package com.eowise.recyclerview.stickyheaders.samples.TabDemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.R;

/**
 * Example about replacing fragments inside a ViewPager. I'm using
 * android-support-v7 to maximize the compatibility.
 * 
 * @author Dani Lao (@dani_lao)
 * 
 */
public class RootFragment extends Fragment {

	private static final String TAG = "RootFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
		View view = inflater.inflate(R.layout.root_fragment, container, false);

		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		/*
		 * When this container fragment is created, we fill it with our first
		 * "real" fragment
		 */
		String value=LndShopActivity.currentcategory;
		if (value.compareToIgnoreCase("dress") == 0)
			transaction.replace(R.id.root_frame, new DressFilterFragment());
		else if (value.compareToIgnoreCase("handbags") == 0)
			transaction.replace(R.id.root_frame, new HangbagsFilterFragment());
		else if (value.compareToIgnoreCase("shoes") == 0)
			transaction.replace(R.id.root_frame, new ShoesFilterFragment());
		else if (value.compareToIgnoreCase("jewellery") == 0)
			transaction.replace(R.id.root_frame, new JwelleryFilterFragment());
		else
		transaction.replace(R.id.root_frame, new CategoryFragment());

		transaction.commit();

		return view;
	}

}
