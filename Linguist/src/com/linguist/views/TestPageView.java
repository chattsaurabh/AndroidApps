package com.linguist.views;

import java.util.Map;
import java.util.Set;

import linguist.student.test.guide.R;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.linguist.ResultActivity;
import com.linguist.abs.AbsPannerView;
import com.linguist.data.TestDataVO;
import com.linguist.model.TestDataModel;
import com.linguist.util.ConfigurationManager;

public class TestPageView extends AbsPannerView {


	private int m_currentAns = 0;
	private TestDataVO m_currentDataVO;
	private TestDataModel m_testDataModel;
	private Button m_submitBtn;
	
	public TestPageView(Context context, ViewGroup a_root) {
		super(context, a_root);
		// TODO Auto-generated constructor stub
	}
	
	public TestPageView(Context context, AttributeSet attrs,
			ViewGroup a_root) {
		super(context, attrs, a_root);
		// TODO Auto-generated constructor stub
	}
	
	public TestPageView(Context context, AttributeSet attrs, int defStyle,
			ViewGroup a_root) {
		super(context, attrs, defStyle, a_root);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initView(Context a_cont, ViewGroup a_root) {
		LayoutInflater infl = (LayoutInflater)a_cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_view = (ViewGroup) infl.inflate(R.layout.test_page_view, a_root, true);
		m_confMan = ConfigurationManager.getInstance();
		m_testDataModel = m_confMan.getTestDataModel();
		m_currentDataVO = m_testDataModel.getCurrentData();
		m_confMan.setCircularModel(false);
		
		configure(m_testDataModel.getTestDataList().size(), m_testDataModel.getCurrentIndex(), R.id.testPageDotsLL, R.id.testPageTitlePrevBtn, R.id.testPageTitleNextBtn);
		
		
		m_submitBtn = (Button) m_view.findViewById(R.id.testPageSubmitButton);
		m_submitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				m_cont.startActivity(new Intent(RESULT_ACTIVITY));
				m_cont.startActivity(new Intent(m_cont,ResultActivity.class));
			}
		});
		
		
		TextView l_titleTV = (TextView) m_view.findViewById(R.id.testPageTitleTextView);
		l_titleTV.setText(m_currentDataVO.getTitle());
		
		
		TextView l_quesTV = (TextView) m_view.findViewById(R.id.testPageQuestionTextView);
		String l_ques = m_cont.getResources().getString(R.string.test_page_ques_format)+
						(m_testDataModel.getCurrentIndex()+1)+
						" : "+
						m_currentDataVO.getQuestion();
		final SpannableStringBuilder l_spannable_ques = new SpannableStringBuilder(l_ques);
		l_spannable_ques.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		l_spannable_ques.setSpan(new RelativeSizeSpan(1.2f), 0, 8, 0);
		l_quesTV.setText(l_spannable_ques);
		
		Map<String, Boolean> l_ansMap = m_currentDataVO.getAnswer();
		Set<String> l_ans = l_ansMap.keySet();
		int l_anseweredVal = m_currentDataVO.getAnsweredNumber();
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, (int) m_cont.getResources().getDimension(R.dimen.test_page_radio_btn_ht));
		
		RadioGroup l_ansRadioGrp = (RadioGroup) m_view.findViewById(R.id.testPageRadioGrpTop);
		for (String ans : l_ans) {
			RadioButton l_ansBtn = new RadioButton(m_cont);
			l_ansBtn.setText(ans);
			l_ansBtn.setId(m_currentAns);
			l_ansBtn.setTextSize(m_cont.getResources().getDimension(R.dimen.answer_text_size));
			l_ansBtn.setLayoutParams(lp);
			
			if((l_anseweredVal != -1) && (l_anseweredVal == m_currentAns))
			{
				l_ansBtn.setChecked(true);
			}
			
			l_ansRadioGrp.addView(l_ansBtn);
			
			l_ansBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(m_currentDataVO.getAnsweredNumber() == -1)
					{
						m_testDataModel.incrementResultCount();
					}
					setCurrentAnswer(m_currentDataVO, v.getId());
					enableSubmitButton();
				}
			});
			m_currentAns++;
		}
		enableSubmitButton();
	}

	private void enableSubmitButton() 
	{
		if(m_testDataModel.getTestDataList().size() == m_testDataModel.getResultCount())
		{
			m_submitBtn.setEnabled(true);
		}
	}
	
	private void setCurrentAnswer(TestDataVO a_testDataVO, int a_ans)
	{
		a_testDataVO.setAnsweredNumber(a_ans);
	}
	
	@Override
	protected void clean() {
		m_currentAns = 0;
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
