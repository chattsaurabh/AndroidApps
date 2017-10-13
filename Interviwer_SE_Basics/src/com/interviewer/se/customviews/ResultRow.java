package com.interviewer.se.customviews;

import interviewer.se.basics.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.interviewer.se.data.ResultVO;

public class ResultRow extends ViewGroup {
	
	protected ViewGroup m_view = null;	
	protected Context m_cont = null;

	public ResultRow(Context context, ViewGroup a_root, ResultVO a_resultVO) {
		super(context);
		m_cont = context;
		init(a_root,a_resultVO);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub

	}
	
	private void init(ViewGroup a_root, ResultVO a_resultVO) {
		LayoutInflater infl = (LayoutInflater)m_cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_view = (ViewGroup) infl.inflate(R.layout.result_row, a_root, true);
		
		TextView l_subject = (TextView) m_view.findViewById(R.id.resultRowSubject);
//		l_subject.setTextSize(m_cont.getResources().getDimension(R.dimen.result_score_text_size));
		l_subject.setText(a_resultVO.getSubject());
		
		TextView l_marks = (TextView) m_view.findViewById(R.id.resultRowMarks);
//		l_marks.setTextSize(m_cont.getResources().getDimension(R.dimen.result_score_text_size));
		int l_score = a_resultVO.getMarks();
		int l_total = a_resultVO.getTotal();
		if((float)l_score/l_total < 0.3)
		{
			l_marks.setTextColor(Color.RED);
		}
		else
		{
			l_marks.setTextColor(Color.GREEN);
		}
		l_marks.setText(l_score+"/"+l_total);
	}

}
