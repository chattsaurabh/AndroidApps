package com.linguist.customviews;

import linguist.student.test.guide.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

public class CustomViewFlipper extends ViewFlipper {

	private Context m_context = null;
	private int m_maxRange = -1;
	
	public CustomViewFlipper(Context context) {
		super(context);
		m_context = context;
		initView();
	}

	public CustomViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
		m_context = context;
		initView();
	}
	
	
	@Override
	protected int computeHorizontalScrollRange() {		
		return m_maxRange;
	}
	
	public void setMaxScrollRange(int a_maxRange) {
		m_maxRange = a_maxRange;
	}
	
	private void initView() {
		setWillNotDraw(false);
		setHorizontalScrollBarEnabled(true);
		setVerticalScrollBarEnabled(true);

		TypedArray a = m_context.obtainStyledAttributes(R.styleable.CustomScrollerView);
		initializeScrollbars(a);
		a.recycle();
	}

}
