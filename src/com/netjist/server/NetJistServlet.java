package com.netjist.server;

import java.io.IOException;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class NetJistServlet extends HttpServlet {

	private netJistController NJC = new netJistController();

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
		// have a controller for the domain
		// tell controller to invoke a source getting function of a particular
		// class
		//List mySources = new ArrayList();
		NJC.extractAndSortData();
		// display mySources
		
	}

	
}
