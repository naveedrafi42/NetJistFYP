package com.netjist.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.netjist.models.NewsItem;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskOptions;

public class LinkExtractor extends HttpServlet {

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		public void doPost(HttpServletRequest req, HttpServletResponse resp)
				throws IOException {
			resp.setContentType("text/plain");
			
			try {
				
				//Map<String, String[]> myParams = req.getParameterMap();			
				
				//connect to data source
			    URL wiki = new URL(req.getParameter("Link"));
			    URLConnection myURLConnection = wiki.openConnection();
			    
			    //create document and put the source xml in it
			    Document dc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(myURLConnection.getInputStream());	    
			    
			    //create node list that contains all <item> in rss channel
			    NodeList newsItems = dc.getElementsByTagName("item");
			    
			    DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
			    
			    Queue queue = QueueFactory.getDefaultQueue();
			    
			    Logger logger = Logger.getLogger("Default");
			    logger.info("Hello Logs");
			    
			    for(int i=0; i<newsItems.getLength(); i++){
			    	Node it = newsItems.item(i);
			    	if(it.getNodeType()==Node.ELEMENT_NODE){
			    		Element item = (Element) it;
			    		NodeList insideItem = item.getChildNodes();
			    		NewsItem temp = new NewsItem();
			    		for(int j=0; j<insideItem.getLength(); j++){
			    			Node f = insideItem.item(j);
			    			if(f.getNodeName()=="title"){
			    				temp.setM_title(f.getTextContent());
			    				resp.getWriter().println("Title: " + temp.getM_title());
			    			}
			    			if(f.getNodeName()=="link"){
			    				temp.setM_link(f.getTextContent());
			    				resp.getWriter().println("Link: " + temp.getM_link());
			    			}
			    			if(f.getNodeName()=="pubDate"){	    				
			    				temp.setM_pubDate(formatter.parse(f.getTextContent()));
			    				resp.getWriter().println("Date: " + temp.getM_pubDate().toString());
			    			}
			    			/*if(f.getNodeName()=="description"){	    				
			    				temp.setM_description(f.getTextContent());
			    				resp.getWriter().println("Description: " + temp.getM_description());
			    			}*/
			    		}
			    		//Check if temp.link already exists else keep going
			    		
			    		if(getEntity(temp.getM_link(), "NewsItems")!=null)
			    			continue;
			    		
			    		queue.add(TaskOptions.Builder
				        		.withUrl("/storedata")
				        		.param("kind", "NewsItems")
				        		.param("Title", temp.getM_title())
				        		.param("Link", temp.getM_link())
				        		.param("Date", temp.getM_pubDate().toString())
				        		//.param("Description", temp.getM_description())
				        		.retryOptions(RetryOptions.Builder.withTaskRetryLimit(1)));
			    	}
			    }
			    //display the arraylist
			    //store the newsList on dataStore!!! - Done!
		        
			   
			} 
			catch (MalformedURLException e) { 
			    // new URL() failed
			    // ...
			} 
			catch (IOException e) {   
			    // openConnection() failed
			    // ...
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			resp.getWriter().println("Hello, from naveed");
		}
		
		//Checks if links already exists in "Newsitems" in datastore 
		private Entity getEntity(String key, String kind)
		{
			DatastoreService datastore =
		    DatastoreServiceFactory.getDatastoreService();

		    // Get a key that matches this entity:
		    Key toCheck = KeyFactory.createKey(kind, key);

		    try {
		        return datastore.get(toCheck);
		    } catch (EntityNotFoundException e) {
		        // Entity does not exist in DB:
		        return null;
		    }
		}

}
