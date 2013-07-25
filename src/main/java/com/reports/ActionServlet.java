package com.reports;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;


public class ActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String LOGTAG = "+ [ActionServlet]: ";

	static String psoResourcePkg = "/com/reports";
	String INPUT_CONFIGS = psoResourcePkg + "/config.properties";


	public ActionServlet() {
		super();
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String reqProperty = request.getParameter("dimension");

		Enumeration enAttr = request.getAttributeNames(); 
		while(enAttr.hasMoreElements()){
			String attributeName = (String)enAttr.nextElement();
			System.out.println("Attribute Name - "+attributeName+", Value - "+(request.getAttribute(attributeName)).toString());
		}
		
		// Read data from config-props file
		Properties prop = new Properties();
		InputStream inputStream = ActionServlet.class.getClassLoader().getResourceAsStream(INPUT_CONFIGS);
		prop.load(inputStream);
		
		String dimensionProps = prop.getProperty( reqProperty.toLowerCase() );
		if (dimensionProps == null) {
			System.out.println(LOGTAG + "ERROR: Could not found dimension property!");
		}
		
		String[] dimensions = dimensionProps.split(",");

		Map<Integer, String> ind = new LinkedHashMap<Integer, String>();
		Integer inputIndex = 1;
		for (String slot : dimensions) {
			ind.put(inputIndex, slot);
			inputIndex++;
		}
		
		String json= new Gson().toJson(ind);		
		System.out.println("Json: " + json.toString());
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
