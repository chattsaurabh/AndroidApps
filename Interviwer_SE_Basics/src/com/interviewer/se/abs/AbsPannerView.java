package com.interviewer.se.abs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.interviewer.se.util.ConfigurationManager;
import com.interviewer.se.util.Interviewer_SE_Basics_Constants;

public abstract class AbsPannerView  extends ViewGroup implements Interviewer_SE_Basics_Constants{

	protected ViewGroup m_view = null;	
	protected Context m_cont = null;
	protected ConfigurationManager m_confMan = null;
	protected LinearLayout m_dotsLinLayout = null;
	protected Button m_prevBtn = null;
	protected Button m_nextBtn = null;
	
	
	public AbsPannerView(Context context, ViewGroup a_root) {
		super(context);		
		m_cont = context;
		initView(context, a_root);
	}
	
	public AbsPannerView(Context context, AttributeSet attrs, ViewGroup a_root) {
		super(context, attrs);
		m_cont = context;
		initView(context, a_root);
	}
	
	public AbsPannerView(Context context, AttributeSet attrs, int defStyle, ViewGroup a_root) {
		super(context, attrs, defStyle);
		m_cont = context;
		initView(context, a_root);
	}
	
	protected void configure(int a_numPages, int a_currentPage, int a_llId, int a_prevBtnId, int a_nextBtnId) {
		m_dotsLinLayout = (LinearLayout) m_view.findViewById(a_llId);
		TextView l_index = new TextView(m_cont);
		l_index.setText((a_currentPage+1)+"/"+a_numPages);
		m_dotsLinLayout.addView(l_index);
		/*for (int i = 0; i < a_numPages; i++) {
			ImageView l_dot = new ImageView(m_cont);
			if(i == a_currentPage)
			{
				l_dot.setBackgroundResource(R.drawable.green_dot_20);
			}
			else
			{
				l_dot.setBackgroundResource(R.drawable.white_dot_20);
			}
			m_dotsLinLayout.addView(l_dot);
		}*/
		
		m_prevBtn = (Button) m_view.findViewById(a_prevBtnId);
		if(a_currentPage == 0)
		{
			m_prevBtn.setEnabled(false);
		}
		else
		{
			m_prevBtn.setEnabled(true);
		}
		m_prevBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				m_confMan.showPreviousPage();
				
			}
		});
		
		m_nextBtn = (Button) m_view.findViewById(a_nextBtnId);
		if(a_currentPage == (a_numPages-1))
		{
			m_nextBtn.setEnabled(false);
		}
		else
		{
			m_nextBtn.setEnabled(true);
		}
		m_nextBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				m_confMan.showNextPage();
				
			}
		});
	}
	
	protected abstract void initView(Context a_cont, ViewGroup a_root);
	protected abstract void clean();
}
