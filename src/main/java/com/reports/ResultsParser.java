

package com.reports;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inmobi.psocommons.TouchstoneParsers;
import com.utils.FetchData;
import com.utils.HtmlBuilder;

public class ResultsParser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String LOGTAG = "+ [PSOWEBAPP - AdFormatsResults]: ";

	TouchstoneParsers parsers = new TouchstoneParsers();
	
	HashMap<String, String> reqParams = new HashMap<String, String>();

	public ResultsParser() {
		super();
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String groupByKey = request.getParameter("groupby");
		
		StringBuilder responseOut = new StringBuilder();	// response is to be build.
		String title = "Ad Fromats Test Results";
		response.setContentType("text/html");
		String docType =
				"<!doctype html public \"-//w3c//dtd html 4.0 " +
						"transitional//en\">\n";
		responseOut.append(docType +
				"<html>\n" +
				"<head><title>" + title + "</title></head>\n" +
				"<body bgcolor=\"#f0f0f0\">\n" +
				"<h1 align=\"center\">" + title + "</h1>\n");

		// Variables:
		FetchData dbFetcher = new FetchData();
		ArrayList<String> slots = dbFetcher.getValuesToQuery( groupByKey.toLowerCase()) ;
		HashMap<String, ArrayList<String>> resFromDb = new HashMap<String, ArrayList<String>>();

		// Todo: to be moved to the db.properties file
		ArrayList<String> headersInfo = new ArrayList<String>();
		String headers = "testid,datetime,slot,creative,releasetype,platform,version,integration,beacon_resp,status";
		String[] headersList = headers.split(",");
		for (String header : headersList) {
			headersInfo.add(header);
		}
		resFromDb.put("headers", headersInfo);
		
		// Add data to the data-hash
		for (String inSlot: slots) {
			System.out.println(LOGTAG + " Slot-Size: " + inSlot);
			
			resFromDb.put(inSlot, new ArrayList<String>());
			
			try {
				ArrayList<String> inputQueryResults = dbFetcher.testResults(inSlot);
				resFromDb.put(inSlot, inputQueryResults);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		System.out.println(LOGTAG + "\n+++++++++++++++++" + "\n" + "Data fetched from Database...... ");
		System.out.println(resFromDb.toString());

		String htmlContent = ""; //HtmlBuilder.build("Test Results");
		htmlContent = "<html>";
		htmlContent += HtmlBuilder.htmlHead("Test results - Ad Formats");
		htmlContent += HtmlBuilder.htmlScriptData();
		htmlContent += HtmlBuilder.htmlStyleData();
		htmlContent += HtmlBuilder.htmlBody(resFromDb);
		htmlContent += "\n" + "</html>";
		
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println(htmlContent);
		out.close();
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
