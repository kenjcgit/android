package com.bloo.kenjc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bloo.kenjc.R;

public class Header extends LinearLayout {
	LinearLayout headerLinearLayout;
	ImageView backbtn;
	public static final int HEADER_BACKBTN = 100;

	public Header(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.header, null);
		headerLinearLayout = (LinearLayout) v.findViewById(R.id.header_ll);
		backbtn = (ImageView) findViewById(R.id.headerbackbtn);
		backbtn.setId(HEADER_BACKBTN);
		if (com.fipl.kenjc.Utils.Constants.intDrawerSelection == 1) {

			backbtn.setVisibility(View.VISIBLE);

		}

	}

}
