package com.netjist.datastore;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class GetData {

	public static List<String> getSources() {
		// TODO Auto-generated method stub
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query gaeQuery = new Query("DataSource");
		PreparedQuery pq = ds.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		
		List<String> Sources = new ArrayList<String>();
		
		for(int i=0; i<list.size(); i++)
		{
			Sources.add(list.get(i).getProperty("Link").toString());
		}
		
		
		return Sources;
	}

	public static List<String> getLinks() {
		// TODO Auto-generated method stub
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		Query gaeQuery = new Query("NewsItems");
		PreparedQuery pq = ds.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		
		List<String> toReturn = new ArrayList<String>();
		
		for(int i=0; i<list.size(); i++)
		{
			toReturn.add(list.get(i).getProperty("Link").toString());
		}
		return toReturn;
	}

}
