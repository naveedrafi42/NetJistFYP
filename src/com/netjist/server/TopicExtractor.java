package com.netjist.server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;





//import com.clearforest.CalaisLocator;

import com.netjist.models.Topic;
import com.netjist.models.TopicsInALink;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskOptions;

public class TopicExtractor extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Exception Exception = null;

	private String getUrlSource(String url) throws IOException {
		URL yahoo = new URL(url);
		URLConnection yc = yahoo.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				yc.getInputStream(), "UTF-8"));
		String inputLine;
		StringBuilder a = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
			a.append(inputLine);
		in.close();

		return a.toString();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		
		String licenseID = "p8e6vaur5pu2xbc7wkmc273u";
		//String paramsXML = "<c:params xmlns:c=\"http://s.opencalais.com/1/pred/\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"> <c:processingDirectives c:contentType=\"TEXT/HTML\" c:enableMetadataType=\"GenericRelations,SocialTags\" c:outputFormat=\"XML/RDF\"></c:processingDirectives><c:userDirectives c:allowDistribution=\"true\" c:allowSearch=\"true\" c:externalID=\"17cabs901\" c:submitter=\"ABC\"></c:userDirectives><c:externalMetadata></c:externalMetadata></c:params>";
		String content = new String();
		String link = req.getParameter("Link");
		Logger logs = Logger.getLogger("Default");
		logs.info(link);

	    content = getUrlSource(link);
		//resp.getWriter().println((content));
		//content="It has become fashionable in recent years to refer to a growing libertarian wing of the Republican Party, and Rand Paul, the Kentucky senator who announced his candidacy for the presidency on Tuesday, hopes to become the first serious candidate to make it part of a winning primary coalition. Perhaps in a decade or two, a representative of the libertarian wing of the party will have an easy time winning the nomination. It’s just unlikely to happen in 2016.";	
		
		@SuppressWarnings("deprecation")
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://api.opencalais.com/tag/rs/enrich");
		StringEntity input = new StringEntity(content);
		post.setEntity(input);
		post.setHeader("x-calais-licenseID", licenseID);
		post.setHeader("content-type", "text/html");
		post.setHeader("outputformat", "xml/rdf");
		post.setHeader("enableMetadataType","GenericRelations,SocialTags");
		HttpResponse response = client.execute(post);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String Result = new String();
		String line = "";
		StringBuilder everything = new StringBuilder();
		while ((line = rd.readLine()) != null) {
		    everything.append(line);		
			//logger.info(line);
		}
		Result=everything.toString();
		resp.getWriter().println(Result);

		try {
			processResult(Result, link);
		} catch (SAXException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void processResult(String Result, String link) throws SAXException,
			IOException, ParserConfigurationException {
		// TODO Auto-generated method stub
		Logger logs = Logger.getLogger("Default");
		logs.info(Result);
		int start = Result.indexOf("<rdf:RDF xmlns:rdf");
		
		Result = Result.substring(start);

		InputStream stream = new ByteArrayInputStream(Result.getBytes(StandardCharsets.UTF_8));
		Document dc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);

		NodeList Descriptions = dc.getElementsByTagName("rdf:Description");

		int i = 0;

		// make an object and fill it with those entities that are mentioned
		// with relevance

		ArrayList<Topic> Topics = new ArrayList<Topic>();
		String name = new String();
		for (i = 0; i < Descriptions.getLength(); i++) {
			Node it = Descriptions.item(i);
			if (it.getNodeType() == Node.ELEMENT_NODE) {
				Element item = (Element) it;
				NodeList insideItem = item.getChildNodes();
				// NewsItem temp = new NewsItem();

				for (int j = 0; j < insideItem.getLength(); j++) {
					Node f = insideItem.item(j);
					if (f.getNodeName() == "c:name") {
						// temp.setM_title(f.getTextContent());
						// System.out.println("Name: " + f.getTextContent());
						name = f.getTextContent();
					}
					if (f.getNodeName() == "c:relevance") {
						// temp.setM_link(f.getTextContent());
						// System.out.println("Relevance: " +
						// f.getTextContent());
						Double relevance = Double.parseDouble(f
								.getTextContent());

						Topic topic = new Topic(name, relevance);
						Topics.add(topic);
					}

				}
			}

		}
		
		logs.info(link);
		
		TopicsInALink ListOfTopics = new TopicsInALink(Topics, link);
		StoreTopics(ListOfTopics);

	}

	private void StoreTopics(TopicsInALink listOfTopics) {
		// TODO Auto-generated method stub
		Queue queue = QueueFactory.getDefaultQueue();

		String link = listOfTopics.getM_Link();
		ArrayList<Topic> myTopics = listOfTopics.getM_Topics();
		
		Logger logger = Logger.getLogger("Default");
		for(int i = 0; i < myTopics.size(); i++){
			logger.info(myTopics.get(i).getM_topic());
		}		
	    logger.info(link);

		for (int i = 0; i < myTopics.size(); i++) {
			queue.add(TaskOptions.Builder.withUrl("/storedata")
					.param("kind", "Topics")
					.param("Link", link)
					.param("Topic", myTopics.get(i).getM_topic())
					// .param("Description", temp.getM_description())
					.retryOptions(RetryOptions.Builder.withTaskRetryLimit(1)));
		}
	}
}
