package com.linguist.views;

import java.util.Map;
import java.util.Set;

import linguist.student.test.guide.R;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linguist.abs.AbsPannerView;
import com.linguist.data.TestDataVO;
import com.linguist.model.TestDataModel;
import com.linguist.util.ConfigurationManager;

public class ReviewPageView extends AbsPannerView {

	private TestDataVO m_currentDataVO;
	private TestDataModel m_testDataModel;
	
	public ReviewPageView(Context context, ViewGroup a_root) {
		super(context, a_root);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initView(Context a_cont, ViewGroup a_root) {
		LayoutInflater infl = (LayoutInflater)a_cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_view = (ViewGroup) infl.inflate(R.layout.review_page_view, a_root, true);
		m_confMan = ConfigurationManager.getInstance();
		m_testDataModel = m_confMan.getTestDataModel();
		m_currentDataVO = m_testDataModel.getCurrentData();
		m_confMan.setCircularModel(false);
		configure(m_testDataModel.getTestDataList().size(), m_testDataModel.getCurrentIndex(), R.id.reviewPageDotsLL, R.id.reviewPageTitlePrevBtn, R.id.reviewPageTitleNextBtn);
		
		Button l_homeBtn = (Button) m_view.findViewById(R.id.reviewPageHomeButton);
		l_homeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				m_confMan.resetToHome();				
			}
		});
		
		TextView l_titleTV = (TextView) m_view.findViewById(R.id.reviewPageTitleTextView);
		l_titleTV.setText(m_currentDataVO.getTitle());
		
		TextView l_quesTV = (TextView) m_view.findViewById(R.id.reviewPageQuestionTextView);
		String l_ques = m_cont.getResources().getString(R.string.test_page_ques_format)+
						(m_testDataModel.getCurrentIndex()+1)+
						" : "+
						m_currentDataVO.getQuestion();
		final SpannableStringBuilder l_spannable_ques = new SpannableStringBuilder(l_ques);
		l_spannable_ques.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		l_spannable_ques.setSpan(new RelativeSizeSpan(1.2f), 0, 8, 0);
		l_quesTV.setText(l_spannable_ques);
		
//		l_quesTV.setText(l_ques);

		Map<String, Boolean> l_ansMap = m_currentDataVO.getAnswer();
		Set<String> l_ans = l_ansMap.keySet();
		
		LinearLayout l_ansLL = (LinearLayout) m_view.findViewById(R.id.reviewPageAnsLL);
		int l_ansCount = 0;
		for (String ans : l_ans) 
		{
			TextView l_ansTV = new TextView(m_cont);
			l_ansTV.setTextSize(m_cont.getResources().getDimension(R.dimen.review_ans_text_size));
			l_ansTV.setText(Html.fromHtml("&#x2022; "+ans));
			int l_anseweredNum = m_currentDataVO.getAnsweredNumber();
			int l_correctAns = m_currentDataVO.getCorrectAnswer();
			if(l_anseweredNum == l_correctAns)
			{
				if(l_ansCount == l_anseweredNum)
				{
					l_ansTV.setTextColor(Color.GREEN);
				}
			}
			else if(l_ansCount == l_anseweredNum)
			{
				l_ansTV.setTextColor(Color.RED);
			}
			else if(l_ansCount == l_correctAns)
			{
				l_ansTV.setTextColor(Color.GREEN);
			}
			
			l_ansCount++;
			l_ansLL.addView(l_ansTV);
		}
	}

	@Override
	protected void clean() 
	{
		m_currentDataVO = null;		
		m_view.removeAllViews();
		m_view = null;
		m_confMan.setCircularModel(true);
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub

	}

}
