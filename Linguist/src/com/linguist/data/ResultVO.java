package com.linguist.data;

public class ResultVO {
	
	private String m_subject;
	private int m_marks;
	private int m_total;
	
	public void setSubject(String m_subject) {
		this.m_subject = m_subject;
	}
	public String getSubject() {
		return m_subject;
	}
	public void setMarks(int m_marks) {
		this.m_marks = m_marks;
	}
	public int getMarks() {
		return m_marks;
	}
	public void setTotal(int m_total) {
		this.m_total = m_total;
	}
	public int getTotal() {
		return m_total;
	}

}
