package com.netjist.models;

public class Topic {
	private String m_topic;
	private Double m_Relevance;
	
	public Topic(String m_topic, Double m_Relevance) {
		this.m_topic = m_topic;
		this.m_Relevance = m_Relevance;
	}

	public String getM_topic() {
		return m_topic;
	}

	public Double getM_Relevance() {
		return m_Relevance;
	}
	
}
