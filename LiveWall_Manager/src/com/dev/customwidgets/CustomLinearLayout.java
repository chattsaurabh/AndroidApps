package com.dev.customwidgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.dev.constants.LiveWallManagerConstants;

public class CustomLinearLayout extends LinearLayout implements LiveWallManagerConstants{

	public CustomLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustomLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		Log.d(TAG,"CustomLinearLayout.onLayout. == "+l+" , "+t+" , "+r+" , "+b);
		super.onLayout(changed, l, t, r, b);
	}
}
