package com.netjist.models;

import java.util.Date;

public class NewsItem {
	private String m_title;
	private String m_link;
	private Date m_pubDate;
	//private String m_description;
	
	/*public String getM_description() {
		return m_description;
	}
	public void setM_description(String m_description) {
		this.m_description = m_description;
	}*/
	public String getM_title() {
		return m_title;
	}
	public void setM_title(String m_title) {
		this.m_title = m_title;
	}
	public String getM_link() {
		return m_link;
	}
	public void setM_link(String m_link) {
		this.m_link = m_link;
	}
	public Date getM_pubDate() {
		return m_pubDate;
	}
	public void setM_pubDate(Date m_pubDate) {
		this.m_pubDate = m_pubDate;
	}
	
	
}
