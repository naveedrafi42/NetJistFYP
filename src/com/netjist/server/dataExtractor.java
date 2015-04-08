package com.netjist.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.netjist.datastore.GetData;

public class dataExtractor {
	
	private List<String> Sources;
	//private List newsItems;	

	public void extractAndSort() {
		// TODO Auto-generated method stub
		//getsources
		List<String> mySources = GetData.getSources();	
		Sources=mySources;
		
		//store links in sources
		StoreLinksInSources();
		
		//get List of links and send it to opencalais servlet module
		//first get list of links
		List<String> myLinks = getLinks();
		//so far so good test it tomo
		Logger logger = Logger.getLogger("Default");
		for(int i=0; i<myLinks.size(); i++){
			logger.info(myLinks.get(i).toString());
		}
		//send these links to opencalais servlet and store results
		extractTopics(myLinks);
	    //get topics from datastore, reorder and store them back
		//dont forget to make a servlet that handles client requests
		
	}
	
	private void extractTopics(List<String> myLinks) {
		// TODO Auto-generated method stub
		Queue queue = QueueFactory.getDefaultQueue();
		for(int i=0;i<myLinks.size();i++)
	    {
	    	queue.add(TaskOptions.Builder
	    			.withUrl("/opencalais")
	    			.param("Link", myLinks.get(i).toString())
	    			.retryOptions(RetryOptions.Builder.withTaskRetryLimit(1)));
	    }
	}

	private List<String> getLinks() {
		// TODO Auto-generated method stub
		return GetData.getLinks();
	}

	public void StoreLinksInSources(){
		Queue queue = QueueFactory.getDefaultQueue();
		for(int i=0;i<Sources.size();i++)
	    {
	    	queue.add(TaskOptions.Builder
	    			.withUrl("/extract")
	    			.param("Link", Sources.get(i).toString())
	    			.retryOptions(RetryOptions.Builder.withTaskRetryLimit(1)));
	    }
		
		
	}
	
	public dataExtractor(){
		Sources = new ArrayList<String>();
	}

}
