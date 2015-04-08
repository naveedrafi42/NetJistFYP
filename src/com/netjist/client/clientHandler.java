package com.netjist.client;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class clientHandler extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("So youve finally found netjist on the internet han Saad?");
		// have a controller for the domain
		// tell controller to invoke a source getting function of a particular
		// class
		//List mySources = new ArrayList();
		//NJC.extractAndSortData();
		// display mySources
		
	}
}
