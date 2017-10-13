package com.linguist;

import java.util.HashMap;
import java.util.Map;

import linguist.student.test.guide.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.linguist.customviews.ResultRow;
import com.linguist.data.ResultVO;
import com.linguist.data.TestDataVO;
import com.linguist.model.TestDataModel;
import com.linguist.util.ConfigurationManager;
import com.linguist.util.LinguistConstants;

public class ResultActivity extends Activity implements LinguistConstants{

	private TableLayout m_resultTableLayout = null;
	private TestDataModel m_testDataModel = null;
	private ConfigurationManager m_confMan = null;
	private Button m_reviewBtn = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		m_confMan = ConfigurationManager.getInstance();
		m_testDataModel = m_confMan.getTestDataModel();

		m_resultTableLayout = (TableLayout) findViewById(R.id.resultTableLayout);

		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		Map<String, Integer> l_resultMap = new HashMap<String, Integer>();
		Map<String, Integer> l_totalMap = new HashMap<String, Integer>();

		for (TestDataVO testData : m_testDataModel.getTestDataList()) 
		{
			if(testData.getAnsweredNumber() == testData.getCorrectAnswer())
			{
				if(l_resultMap.containsKey(testData.getTitle()))
				{
					int l_currentVal = l_resultMap.get(testData.getTitle());
					l_resultMap.put(testData.getTitle(), ++l_currentVal);
				}
				else
				{
					l_resultMap.put(testData.getTitle(), 1);
				}
			}
			
			if(l_totalMap.containsKey(testData.getTitle()))
			{
				int l_currentVal = l_totalMap.get(testData.getTitle());
				l_totalMap.put(testData.getTitle(), ++l_currentVal);
			}
			else
			{
				l_totalMap.put(testData.getTitle(), 1);
			}			

		}
		
		if(l_totalMap.size() > l_resultMap.size())
		{
			for (Map.Entry<String, Integer> entry : l_totalMap.entrySet())
			{
				if(!l_resultMap.containsKey(entry.getKey()))
				{
					l_resultMap.put(entry.getKey(), 0);
				}
			}
		}

		if(l_resultMap.entrySet().size() > 0)
		{
			for (Map.Entry<String, Integer> entry : l_resultMap.entrySet())
			{
				TableRow l_row = new TableRow(this);
				l_row.setLayoutParams(lp);

				ResultVO l_resultVO = new ResultVO();			
				l_resultVO.setSubject(entry.getKey());
				l_resultVO.setMarks(entry.getValue());
				l_resultVO.setTotal(l_totalMap.get(entry.getKey()));
//				+"/"+l_totalMap.get(entry.getKey()));
				ResultRow l_result = new ResultRow(this, l_row, l_resultVO);

				m_resultTableLayout.addView(l_row);
			}
		}
		else
		{
			for (Map.Entry<String, Integer> entry : l_totalMap.entrySet())
			{
				TableRow l_row = new TableRow(this);
				l_row.setLayoutParams(lp);

				ResultVO l_resultVO = new ResultVO();			
				l_resultVO.setSubject(entry.getKey());
				l_resultVO.setMarks(0);
				l_resultVO.setTotal(l_totalMap.get(entry.getKey()));
				ResultRow l_result = new ResultRow(this, l_row, l_resultVO);
				m_resultTableLayout.addView(l_row);
			}
		}
		
		m_reviewBtn = (Button) findViewById(R.id.resultReviewButton);
		m_reviewBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				m_testDataModel.resetCurrentIndex();
//				startActivity(new Intent(REVIEW_ACTIVITY));				
				startActivity(new Intent(getApplicationContext(), ReviewActivity.class));
			}
		});
		
		m_confMan.setResultActivity(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result, menu);
		return true;
	}

}
