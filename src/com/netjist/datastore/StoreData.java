package com.netjist.datastore;

import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

public class StoreData extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp){
	    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	    Map<String, String[]> myParams = req.getParameterMap();
	     
	    Logger los = Logger.getLogger("Default");
	    los.info(req.getParameter("Topic")+" "+req.getParameter("Link"));
	  //Entity toPut = new Entity(req.getParameter("kind"), myParams.get("Link")[0]);
	    
	    //make case for newsitems
	    Entity toPut = new Entity(req.getParameter("kind"));
	      
	    for(String name : myParams.keySet()){
	    	if (name.equalsIgnoreCase("kind"))
	    		continue;
	    	toPut.setProperty(name, myParams.get(name)[0]);	
	    }
	    ds.put(toPut);
	    
	}
}
