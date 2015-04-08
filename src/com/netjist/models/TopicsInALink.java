package com.netjist.models;

import java.util.ArrayList;

public class TopicsInALink {
	public ArrayList<Topic> getM_Topics() {
		return m_Topics;
	}

	public String getM_Link() {
		return m_Link;
	}

	private ArrayList<Topic> m_Topics;
	private String m_Link;
	
	public TopicsInALink(ArrayList<Topic> m_Topics, String m_Link) {
		this.m_Topics = m_Topics;
		this.m_Link = m_Link;
	}

	public void print() {
		// TODO Auto-generated method stub
		System.out.println("Link: "+m_Link);
		System.out.println("Topics Included:");
		for(int i=0; i<m_Topics.size(); i++){
			Topic t = (Topic) m_Topics.get(i);
			System.out.println("Topic: " +t.getM_topic()+"     Relevance: "+t.getM_Relevance());
		}
	}

	public void filter(double d) {
		// TODO Auto-generated method stub
		int i = 0;
		for(i=0; i<m_Topics.size(); i++){
			Topic t = (Topic) m_Topics.get(i);
			if(t.getM_Relevance()<d){
				m_Topics.remove(i);
				//i++;
			}
		}
		System.out.println(i);
		
	}

	public int size() {
		// TODO Auto-generated method stub
		return m_Topics.size();
	}
	
	
}
