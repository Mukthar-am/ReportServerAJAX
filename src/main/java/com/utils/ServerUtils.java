package com.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inmobi.psocommons.RegExpCollection;
import com.inmobi.psocommons.TouchstoneParsers;


public class ServerUtils {

	String LOGTAG = "+ [ServerUtils]: ";
	RegExpCollection regExp = new RegExpCollection();
	SearchUtils searchUtils = new SearchUtils();
	UrlParserUtils urlParser = new UrlParserUtils();
	String psoResourcePkg = "/com/inmobi/pso";

	public String hostIP, testConfigFile, DB_URL, JDBC_DRIVER, USERID, PASSWORD, CLICKS_TABLE, BEACON_TABLE;

	// ########################################################################################################
	public StringBuilder searchAndReplaceInResponses(HashMap<String, String> reqParams, String urlConfigFile, String responseFile) {
		System.out.println(LOGTAG + "+++ searchAndReplaceInResponses() : Search and replace URL string...");

		TouchstoneParsers parsers = new TouchstoneParsers();
		HashMap<String, HashMap<String, String>> configPropsMap = parsers.readPropsFile(new File(urlConfigFile));
		//HashMap<String, String> reqParams = getReqParams(reqParams);	// hash map holding all the url params parsed

		// get all click, beacon and click-redirect urls built
		HashMap<String, String> allUrlPaths = buildAllUrls(reqParams, configPropsMap);	// Get all urls
		//System.out.println(LOGTAG + " URLs Hash: " + allUrlPaths.toString());

		StringBuilder finalResponseContent = replaceInIMAI(responseFile, configPropsMap, allUrlPaths, reqParams);	// TO REPLACE THE CONTENT OF RESPONSE FILES

		return finalResponseContent;

	}


	// ################################################################################################################
	// Fetch hostname
	public String getHostIp() {

		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.out.println(e);
		}

		return addr.getHostAddress();
	}

	// ################################################################################################################
	// Initialize server environment settings
	public void intServerEnv() {
		ServerUtils serverUtils = new ServerUtils();
		TouchstoneParsers parsers = new TouchstoneParsers();

		String CONFIGFILE = psoResourcePkg + "/configs/server.properties";
		String testConfigFile = ServerUtils.class.getResource(CONFIGFILE).getFile();

		HashMap<String, HashMap<String, String>> configPropsMap = parsers.readPropsFile(new File(testConfigFile));
		hostIP = (configPropsMap.get("URL_PARAMS").get("LOCAL_ENV").equalsIgnoreCase("yes") ? "localhost" : serverUtils.getHostIp());

		// JDBC driver name and database URL
		JDBC_DRIVER="com.mysql.jdbc.Driver";
		DB_URL="jdbc:mysql://" + hostIP + "/psotest";

		// Database credentials
		USERID = configPropsMap.get("SERVER_ENV").get("USERID");
		PASSWORD = configPropsMap.get("SERVER_ENV").get("PASSWORD");

		// Database tables:
		CLICKS_TABLE = configPropsMap.get("SERVER_ENV").get("CLICKS_TABLE");
		BEACON_TABLE = configPropsMap.get("SERVER_ENV").get("BEACON_TABLE");

	}


	// ################################################################################################################
	public StringBuilder buildResponse(HashMap<String, String> reqParams, String urlConfigFile, String inResponseFile, PrintWriter out) {

		// In-memory replacement of the contents of the response files 
		StringBuilder modifiedResponseContent = searchAndReplaceInResponses(reqParams, urlConfigFile, inResponseFile);
		out.println(modifiedResponseContent);

		out.flush();
		out.close();

		return modifiedResponseContent;

	} // buildResponse()


	// ################################################################################################################
	public HashMap<String, String> buildAllUrls(HashMap<String, String> reqParams, HashMap<String, HashMap<String, String>> configMap) {

		HashMap<String, String> urlPaths = new HashMap<String, String>();

		String hostName = (configMap.get("URL_PARAMS").get("LOCAL_ENV").equalsIgnoreCase("yes") ? "localhost" : getHostIp());		
		String bannerImgPath = configMap.get("URL_PARAMS").get("IMAGE_SOURCE");
		String serverPort = configMap.get("URL_PARAMS").get("PORT");
		String adReqType = reqParams.get("adtype");
		String creative = reqParams.get("creative");

		// Building URLs
		String imgUrl = "http://" + hostName + ":" + serverPort + bannerImgPath + adReqType + "_" + creative.toLowerCase() + ".png";
		urlPaths.put("imageUrl", imgUrl);

		// landing-url
		String landingUrl = configMap.get("URL_PARAMS").get("LANDING_PAGE");
		urlPaths.put("landingUrl", landingUrl);

		String clickServerURL =	"http://" + hostName + ":" + serverPort + configMap.get("URL_PARAMS").get("CLICK_URL") 
				+ "?rqparam=" 
				+ "/test=" + reqParams.get("test")
				+ "/release=" + reqParams.get("release")
				+ "/platform=" + reqParams.get("platform") + "_" + reqParams.get("platformVersion") + "_" + reqParams.get("integration")
				+ "/slotid=" + reqParams.get("slotid") 
				+ "/creative=" + reqParams.get("creative");
		urlPaths.put("clickServerURL", clickServerURL);

		String clickServerEscapedURL = "http:\\\\/\\\\/" + hostName + ":" + serverPort + configMap.get("URL_PARAMS").get("CLICK_URL_ESCAPED")
				+ "?rqparam="
				+ "\\\\/test=" + reqParams.get("test")
				+ "\\\\/release=" + reqParams.get("release")
				+ "\\\\/platform=" + reqParams.get("platform") + "_" + reqParams.get("platformVersion") + "_" + reqParams.get("integration")
				+ "\\\\/slotid=" + reqParams.get("slotid") 
				+ "\\\\/creative=" + reqParams.get("creative");
		urlPaths.put("clickServerEscapedURL", clickServerEscapedURL);

		//?test=testAndroidTemplate1&release=imai_async&platform=android_4.2_sdk370&m=101
		String beaconUrlM18 = "http://" + hostName + ":" + serverPort + configMap.get("URL_PARAMS").get("BEACON_URL")
				+ "?rqparam=" 
				+ "/test=" + reqParams.get("test")
				+ "/release=" + reqParams.get("release")
				+ "/platform=" + reqParams.get("platform") + "_" + reqParams.get("platformVersion") + "_" + reqParams.get("integration")
				+ "/slotid=" + reqParams.get("slotid") 
				+ "/creative=" + reqParams.get("creative")
				+ "/m=18";
		urlPaths.put("beaconUrlM18", beaconUrlM18);

		String beaconUrlM101 = "http://" + hostName + ":" + serverPort + configMap.get("URL_PARAMS").get("BEACON_URL")
				+ "?rqparam=" 
				+ "/test=" + reqParams.get("test")
				+ "/release=" + reqParams.get("release")
				+ "/platform=" + reqParams.get("platform") + "_" + reqParams.get("platformVersion") + "_" + reqParams.get("integration")
				+ "/slotid=" + reqParams.get("slotid") 
				+ "/creative=" + reqParams.get("creative")
				+ "/m=101";
		urlPaths.put("beaconUrlM101", beaconUrlM101);

		String beaconUrlM18Escaped = "http:\\/\\/" + hostName + ":" + serverPort + configMap.get("URL_PARAMS").get("BEACON_URL_ESCAPED")
				+ "?rqparam=" 
				+ "\\/test=" + reqParams.get("test")
				+ "\\/release=" + reqParams.get("release")
				+ "\\/platform=" + reqParams.get("platform") + "_" + reqParams.get("platformVersion") + "_" + reqParams.get("integration")
				+ "\\/slotid=" + reqParams.get("slotid") 
				+ "\\/creative=" + reqParams.get("creative")
				+ "\\/m=18";
		urlPaths.put("beaconUrlM18Escaped", beaconUrlM18Escaped);

		String beaconUrlM101Escaped = "http:\\/\\/" + hostName + ":" + serverPort + configMap.get("URL_PARAMS").get("BEACON_URL_ESCAPED")
				+ "?rqparam=" 
				+ "\\/test=" + reqParams.get("test")
				+ "\\/release=" + reqParams.get("release")
				+ "\\/platform=" + reqParams.get("platform") + "_" + reqParams.get("platformVersion") + "_" + reqParams.get("integration")
				+ "\\/slotid=" + reqParams.get("slotid") 
				+ "\\/creative=" + reqParams.get("creative")
				+ "\\/m=101";
		urlPaths.put("beaconUrlM101Escaped", beaconUrlM101Escaped);

		String asyncLandingPage = "http://" + hostName + ":" + serverPort + configMap.get("URL_PARAMS").get("ASYNC_LANDING_PAGE");
		urlPaths.put("asyncLandingPage", asyncLandingPage);

		// Note: usage of 4-back slashes when replaceAll and 2 when just replace
		String asyncLandingPageEscaped = "http:\\/\\/" + hostName + ":" + serverPort + configMap.get("URL_PARAMS").get("ASYNC_LANDING_PAGE_ESCAPED");
		urlPaths.put("asyncLandingPageEscaped", asyncLandingPageEscaped);

		String iOsLandingPageEscaped = "http:\\/\\/" + hostName + ":" + serverPort + configMap.get("URL_PARAMS").get("iOS_LANDING_PAGE_ESCAPED");
		urlPaths.put("iOsLandingPageEscaped", iOsLandingPageEscaped);

		return urlPaths;

	}

	// ################################################################################################################
	//	public StringBuilder replaceInIMAI (String inResponseFile,
	//			HashMap<String, HashMap<String, String>> configMap,
	//			HashMap<String, String> urls,
	//			boolean sync,
	//			String responseFormat) {
	//	
	public StringBuilder replaceInIMAI (String inResponseFile,
			HashMap<String, HashMap<String, String>> configMap,
			HashMap<String, String> urls,
			HashMap<String, String> reqParams) {

		String clickActionType = getResponseFormat(reqParams, "");
		String responseFormat = getResponseFormat(reqParams, "format");
		String adFormat = reqParams.get("adtype");
		String platform = reqParams.get("platform");
		boolean sync = (clickActionType.equalsIgnoreCase("sync") ? true : false);

		System.out.println(LOGTAG + " Replacing in the reponse data...");
		System.out.println(LOGTAG + "INFO: Response format: " + responseFormat + " Sync Type: " + sync);

		StringBuilder fileContent = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(inResponseFile));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {

				line.trim();	// trimming trailing characters
				if (!line.matches("^\\s*$") ) {	// skip blank lines

					// Changing image source location
					String imgSrcMatch = searchUtils.searchImageSource(line);
					if (imgSrcMatch != null) {
						line = line.replace(imgSrcMatch, urls.get("imageUrl"));
					}

					// for async tests, click-url = null and landing-page=click-redirect
					if (sync) {
						//System.out.println(LOGTAG + " Running into sync test response type... ");

						String landingUrlMatch = searchUtils.searchLandingUrl(line);
						String clickUrlMatch = searchUtils.searchClickUrl(line);

						if (clickUrlMatch != null && responseFormat.equalsIgnoreCase("xhtml")) {
							System.out.println(LOGTAG + "clickUrlMatch: " + clickUrlMatch);
							line = line.replaceAll( clickUrlMatch, urls.get("clickServerURL"));

						} else if (landingUrlMatch != null && responseFormat.equalsIgnoreCase("imai")) {
							line = line.replaceAll(landingUrlMatch, urls.get("clickServerEscapedURL") );	// for special cases like back slash should be replace rather replaceAll()

						}

						// to handle specific case of click-url with single quotes
						String clickUrlWithQuotes = searchUtils.searchNullClickUrl(line);
						if (clickUrlWithQuotes != null) {
							line = line.replaceAll( clickUrlWithQuotes, "null");
						}

					} else {
						/*System.out.println(LOGTAG + " Building  respnose for: " + responseFormat + " - " + "async");						 
						 	Async/bolt work flow: 
									click = click db entry url
									landing page = final landing page
						 */
						String clickUrlMatch = searchUtils.searchClickUrl(line);
						if (clickUrlMatch != null) {
							line = line.replaceAll( clickUrlMatch, urls.get("clickServerEscapedURL") );
						}

						// Specific click/landing page handlers for iOS (return type: adtest://) and android (return type: nil )
						if ( platform.equalsIgnoreCase("ios")) {
							String landingUrlMatch = searchUtils.searchLandingUrl(line);							
							if ( landingUrlMatch != null ) {
								line = line.replace(landingUrlMatch, urls.get("iOsLandingPageEscaped") );	// for special cases like back slash should be replace rather replaceAll()	
							}
						} else {							
							String landingUrlMatch = searchUtils.searchLandingUrl(line);							
							if ( landingUrlMatch != null ) {
								line = line.replace(landingUrlMatch, urls.get("asyncLandingPageEscaped") );	// for special cases like back slash should be replace rather replaceAll()	
							}
						}

					}

					// Beacon URLs:
					String beaconUrlMatch18 = searchUtils.searchBeaconStringM18(line);
					if (beaconUrlMatch18 != null) {
						line = line.replace(beaconUrlMatch18, urls.get("beaconUrlM18"));
					}

					String beaconUrlMatch101 = searchUtils.searchBeaconStringM101(line);
					if (beaconUrlMatch101 != null) {
						line = line.replace(beaconUrlMatch101, urls.get("beaconUrlM101"));
					}						

				}	// end if-else (!line.matches("^\\s*$")

				// Skip blank lines from the input response files
				if (line != null) {
					fileContent.append("\n");
					fileContent.append(line);
				}

			} // end while(line = reader.readLine())

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileContent;
	}


	// ################################################################################################################
	public String responseFileFetch (HashMap<String, String> reqParams) {

		String platform = null;
		String testCaseId = null;

		platform = reqParams.get("platform");
		testCaseId = reqParams.get("test");	// test case id from url param

		String adtype = reqParams.get("adtype");
		String creative = reqParams.get("creative");
		String slot = reqParams.get("slotid");
		String[] releaseParams = reqParams.get("release").split("_");

		String responseFormat = releaseParams[0];
		boolean sync = (releaseParams[1].equalsIgnoreCase("sync") ? true : false);

		/* get the path of the response file based on
		 * 1. release=imai_sync or imai_async, then picks banner_slot-id_creative.new
		 * 2. release=xhtml_sync - picks banner_slot-id_creative.new
		 * 3. release=xhtml_async - picks banner_bolt_slot-id_creative.new
		 */

		String[] platformParams = reqParams.get("platform").split("_");
		if (platformParams.length > 1) {
			platform = (platformParams[0] != null) ? platformParams[0] : "-";
		} else {
			platform = platformParams[0];
		}

		// #####################
		// CHANGE /responseNew TO /response AT LATER STAGES
		String psoResourcePkg = "/com/inmobi/pso";
		String inResponseFile = null;

		if ( adtype.equalsIgnoreCase("banner") ) {
			if (sync) {
				inResponseFile = psoResourcePkg + "/responsesNew/" + responseFormat.toLowerCase() + "/" + platform.toLowerCase() + "/" +
						adtype + "_" + slot.toUpperCase() + "_" + creative.toUpperCase() + ".new";	
			} else {
				inResponseFile = psoResourcePkg + "/responsesNew/" + responseFormat.toLowerCase() + "/" + platform.toLowerCase() + "/" +
						adtype + "_" + "bolt" + "_" + slot.toUpperCase() + "_" + creative.toUpperCase() + ".new";
			}			

		} else if (adtype.equalsIgnoreCase("interstitial")) {
			inResponseFile = psoResourcePkg + "/responsesNew/" + "interstitial/" + responseFormat.toLowerCase() + "/" + platform.toLowerCase() + "/" +
					adtype + "_" + slot.toUpperCase() + "_" + creative.toUpperCase() + ".new";			
		}

		System.out.println(LOGTAG + " [testCaseId : response file]: [" + testCaseId + " : " + inResponseFile + "]");

		// TO IMPLEMENT: Fails here when the file is not found... move class.getResource() to some other class
		String responseFile = ServerUtils.class.getResource(inResponseFile).getFile();

		return responseFile;

	}


	// ################################################################################################################
	// get request paramerts
	public HashMap<String, String> getReqParams(HttpServletRequest request) {

		HashMap<String, String> reqParams = new HashMap<String, String>();

		String rqParam = request.getParameter("rqparam");
		System.out.println(LOGTAG + " req-params: " + rqParam);

		if (rqParam != null) {
			String[] paramsList = rqParam.split("/");
			boolean skipFlag = true;

			for (String param : paramsList) {
				if (!skipFlag) {
					String[] paramsPair = param.split("=");
					String[] beaconPair = null;
					
					
					// get platform param from platform=? found in req-url
					if (paramsPair[0].equalsIgnoreCase("platform")) {
						String[] platformParams = paramsPair[1].split("_");

						if (platformParams.length > 1) {
							String platform = (platformParams[0] != null) ? platformParams[0] : "-";
							String platformVersion = (platformParams[1] != null) ? platformParams[1] : "-";
							String integration = (platformParams[2] != null) ? platformParams[2] : "-";

							reqParams.put("platform", platform);
							reqParams.put("platformVersion", platformVersion);
							reqParams.put("integration", integration);

						} else {
							reqParams.put("platform", platformParams[0]);

						}	// if-else()

						continue;	// do not run in the else part since platform variable will be duplicated.

					}	// if(platform)-else

					// Get all normal url-params
					if(paramsPair[1].contains("?"))
					{
						beaconPair = paramsPair[1].split("\\?");
						reqParams.put(paramsPair[0], beaconPair[0]);
					}else{
						reqParams.put(paramsPair[0], paramsPair[1]);
					}


				}

				skipFlag = false;
			}

		} else {
			System.out.println(LOGTAG + " +++ Oops! No \"rqparam\" found in the url, please check the click url...");

		}	// if-else

		System.out.println(LOGTAG + "Request Parameters: " + reqParams.toString());
		return reqParams;
	}

	// ##############################################################################################################
	// if the 2nd arg is "format", it will return the respone-format type from release url param
	// if releaes=xhtml_async, it returns xhtml or release=imai_async, returns imai
	public String getResponseFormat(HashMap<String, String> reqParameters, String returnInfo) {
		//System.out.println(LOGTAG + " Req parameters: " + reqParameters.toString());

		String[] releaseParams = reqParameters.get("release").split("_");

		if (returnInfo.equalsIgnoreCase("format")) {
			return releaseParams[0];
		} else if (releaseParams.length > 1) {
			return releaseParams[1];	// to return sync or async type
		} else {
			System.out.println(LOGTAG + " ERROR: Parsing release/response-format params, please check the request URL...");
			return "null";
		}

	}	// getResponseFormat()


	// ##############################################################################################################
	public void dbEntry (HashMap<String, String> reqParams, HttpServletResponse response, String dbTable) {
		System.out.println("\n" + LOGTAG + "Click info is being entered into the db @ " + hostIP + "...");
		System.out.println(LOGTAG +  " Req-Parameters Hash: " + reqParams.toString());

		intServerEnv();	// init server env
		HashMap<String, String> platformInfo = urlParser.getPlatformInfo(reqParams);
		StringBuilder responseOut = new StringBuilder();	// response is to be build.		
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());		

		// Set response content type
		String clickRespType = getResponseFormat(reqParams, "isSync");		// used to set the title of the click action window
		response.setContentType("text/html");
		String title = clickRespType.toUpperCase() + " - " + dbTable + " Database Entry";

		String docType =
				"<!doctype html public \"-//w3c//dtd html 4.0 " +
						"transitional//en\">\n";
		responseOut.append(docType +
				"<html>\n" +
				"<head><title>" + title + "</title></head>\n" +
				"<body bgcolor=\"#f0f0f0\">\n" +
				"<h1 align=\"center\">" + title + "</h1>\n");

		Connection conn = null;
		Statement stmt = null;

		try{
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection
			conn = DriverManager.getConnection(DB_URL, USERID, PASSWORD);

			// Execute SQL query
			stmt = conn.createStatement();
			String selectQuery = null;
			String tableToOperate = null;
			Integer beaconPing = null;
			String insertQuery = null;

			if (dbTable.equalsIgnoreCase("click")) {
				selectQuery = "SELECT * FROM " + CLICKS_TABLE 
						+ " WHERE testid='" +  reqParams.get("test").toString() + "'" 
						+ " and " + "slot" + "=" + "'" + reqParams.get("slotid").toString() + "'"
						+ " and " + "creative" + "=" + "'" + reqParams.get("creative").toString() + "'"
						+ " and " + "releasetype" + "=" + "'" + reqParams.get("release").toString() + "'"
						+ " and " + "platform" + "=" + "'" + reqParams.get("platform").toString() + "'"
						+ " and " + "version" + "=" + "'" + reqParams.get("platformVersion").toString() + "'"
						+ " and " + "integration" + "=" + "'" + reqParams.get("integration").toString() + "'"
						+ ";";

				tableToOperate = CLICKS_TABLE;

			} else {				
				beaconPing = Integer.parseInt(reqParams.get("m"));
				selectQuery = "SELECT * FROM " + BEACON_TABLE 
						+ " WHERE testid='" +  reqParams.get("test").toString() + "'" 
						+ " and " + "slot" + "=" + "'" + reqParams.get("slotid").toString() + "'"
						+ " and " + "creative" + "=" + "'" + reqParams.get("creative").toString() + "'"
						+ " and " + "releasetype" + "=" + "'" + reqParams.get("release").toString() + "'"
						+ " and " + "platform" + "=" + "'" + reqParams.get("platform").toString() + "'"
						+ " and " + "version" + "=" + "'" + reqParams.get("platformVersion").toString() + "'"
						+ " and " + "integration" + "=" + "'" + reqParams.get("integration").toString() + "'"
						+ " and " + "beacon=" + beaconPing
						+ ";";

				tableToOperate = BEACON_TABLE;

			}	// end try()

			//System.out.println(LOGTAG + " SELECT QUERY: " + selectQuery);
			ResultSet selectRes = stmt.executeQuery(selectQuery);

			// Insert into the Db if the record is not found, else update the same record with the timestmap and other details
			if (!selectRes.next()) {
				PreparedStatement pst = null;

				if ( dbTable.equalsIgnoreCase("click") ) {
					System.out.println(LOGTAG + " Inserting into table: " + tableToOperate);

					insertQuery = "INSERT INTO " + CLICKS_TABLE + " VALUES (?,?,?,?,?,?,?,?,?)";
					pst = conn.prepareStatement(insertQuery);

					pst.setString(1, reqParams.get("test"));
					pst.setString(2, timeStamp);
					pst.setString(3, reqParams.get("slotid").toString());
					pst.setString(4, reqParams.get("creative").toString());
					pst.setString(5, reqParams.get("release").toString());
					pst.setString(6, reqParams.get("platform").toString());
					pst.setString(7, reqParams.get("platformVersion").toString());
					pst.setString(8, reqParams.get("integration").toString());
					pst.setString(9, "PASS");	

				} else {
					System.out.println(LOGTAG + " Inserting into table: " + tableToOperate);

					insertQuery = "INSERT INTO " + BEACON_TABLE + " VALUES (?,?,?,?,?,?,?,?,?,?)";
					pst = conn.prepareStatement(insertQuery);

					pst.setString(1, reqParams.get("test"));
					pst.setString(2, timeStamp);
					pst.setString(3, reqParams.get("slotid").toString());
					pst.setString(4, reqParams.get("creative").toString());
					pst.setString(5, reqParams.get("release").toString());
					pst.setString(6, reqParams.get("platform").toString());
					pst.setString(7, reqParams.get("platformVersion").toString());
					pst.setString(8, reqParams.get("integration").toString());
					pst.setInt(9, Integer.parseInt(reqParams.get("m").toString()) );
					pst.setString(10, "PASS");	
				}


				int numRowsChanged = pst.executeUpdate();
				System.out.println("\n" + LOGTAG + " Insert successful: Inserted total number of rows - " + numRowsChanged);

				pst.close();
				selectRes.close();

			} else {
				
				String updateQuery = "UPDATE " + tableToOperate + " SET " + "datetime='" + timeStamp + "'"
						+ " WHERE testid='" +  reqParams.get("test").toString() + "'" 
						+ " and " + "slot" + "=" + "'" + reqParams.get("slotid").toString() + "'"
						+ " and " + "creative" + "=" + "'" + reqParams.get("creative").toString() + "'"
						+ " and " + "releasetype" + "=" + "'" + reqParams.get("release").toString() + "'"
						+ ";";

				System.out.println(LOGTAG + " Updating table: " + tableToOperate );
				System.out.println(LOGTAG + " +++ UPDATE QUERY: " + updateQuery);
				stmt.executeUpdate(updateQuery);

				//conn.commit();
				responseOut.append(LOGTAG + "\"" + tableToOperate + "\" table update successful!");

			}

			System.out.println(LOGTAG + " [DB-Entry]: " + reqParams.get("test").toString() + ", " + timeStamp + ", " + reqParams.get("slotid").toString() 
					+ ", " + reqParams.get("creative").toString() 
					+ ", " + reqParams.get("release").toString() 
					+ ", " + platformInfo.get("platform") 
					+ ", " + platformInfo.get("platformVersion") 
					+ ", " + platformInfo.get("integration") 
					+ ", PASS" );

		} catch(SQLException se) {
			se.printStackTrace();

		} catch(Exception e) {
			e.printStackTrace();

		} finally {

			try{
				if(stmt!=null)
					stmt.close();

			} catch(SQLException se2){

			}// nothing we can do
			try {
				if(conn!=null)
					conn.close();

			} catch(SQLException se){
				se.printStackTrace();

			}//end finally try
		} //end try


		// Write the output statement at the response
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println(responseOut);
		out.close();

	}

	
	public void getReqParametersFromSdk(HttpServletRequest request) {
		
		System.out.println("To out-put All the request parameters received from SDK-Request - ");

		Enumeration enParams = request.getParameterNames(); 
		while(enParams.hasMoreElements()){
			String paramName = (String)enParams.nextElement();
			System.out.println(LOGTAG + " " + paramName + "=" + request.getParameter(paramName));
		}
	}

	

	// ####################################
	// main()
	public static void main(String[] args) {


	}

}