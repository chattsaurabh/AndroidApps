package com.customwidgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

public class CustomEditText extends EditText {

	public CustomEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		InputConnection connection = super.onCreateInputConnection(outAttrs);
	    int imeActions = outAttrs.imeOptions&EditorInfo.IME_MASK_ACTION;
	    if ((imeActions&EditorInfo.IME_ACTION_DONE) != 0) {
	        // clear the existing action
	        outAttrs.imeOptions ^= imeActions;
	        // set the DONE action
	        outAttrs.imeOptions |= EditorInfo.IME_ACTION_DONE;
	    }
	    if ((outAttrs.imeOptions&EditorInfo.IME_FLAG_NO_ENTER_ACTION) != 0) {
	        outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
	    }
	    return connection;
	}

}
