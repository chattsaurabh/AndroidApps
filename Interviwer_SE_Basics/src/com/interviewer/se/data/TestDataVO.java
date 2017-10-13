package com.interviewer.se.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TestDataVO {
	
	private String m_title;
	private String m_question;
	private Map<String, Boolean> m_answerMap;
	private int m_answeredNumber = -1;
	
	public TestDataVO() {
		m_answerMap = new HashMap<String, Boolean>();
	}

	public void setQuestion(String a_question) {
		this.m_question = a_question;
	}

	public String getQuestion() {
		return m_question;
	}

	public void setAnswer(String a_answer, Boolean a_correct) {
		m_answerMap.put(a_answer, a_correct);
	}

	public Map<String, Boolean> getAnswer() {
		return m_answerMap;
	}

	public void setTitle(String m_title) {
		this.m_title = m_title;
	}

	public String getTitle() {
		return m_title;
	}

	public void setAnsweredNumber(int m_answeredNumber) {
		this.m_answeredNumber = m_answeredNumber;
	}

	public int getAnsweredNumber() {
		return m_answeredNumber;
	}
	
	public int getCorrectAnswer() 
	{
//		ArrayList<Integer> l_correctAnsList = new ArrayList<Integer>();
		int l_correctAns = 0;
		for (Boolean l_ans : m_answerMap.values()) 
		{	
			if(l_ans)
			{
//				l_correctAnsList.add(l_correctAns);
				break;
			}
			l_correctAns++;
		}
		return l_correctAns;
	}

}
