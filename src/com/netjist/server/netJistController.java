package com.netjist.server;

public class netJistController {
	
	private dataExtractor m_extractor;

	public void extractAndSortData() {
		// TODO Auto-generated method stub
		m_extractor.extractAndSort();
	}

	public netJistController() {
		m_extractor = new dataExtractor();
	}
	
}
