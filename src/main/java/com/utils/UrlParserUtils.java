package com.utils;

import java.util.HashMap;

public class UrlParserUtils {
	String LOGTAG = "+ [URL-PARSERS-UTILS]: ";

	public HashMap<String, String> getPlatformInfo(HashMap<String, String> reqParams) {

		HashMap<String, String> platformInfo = new HashMap<String, String>();
		
		/*
		 *  two ways to get platform version, and integration
		 *  Either from testcase-id = Android_4.2_sdk370 || from platform = android_4.2_sdk370 or platform = android
		 */

		String testCaseId = reqParams.get("test").toString();
		String[] testIdParams = testCaseId.split("_");
		String platform = null, platformVersion = null, integration = null;
		
		if (testIdParams.length > 1) {
			platformInfo.put("platformVersion", testIdParams[1]);
			platformInfo.put("integration", testIdParams[2]);
		}

		String[] platformParams = reqParams.get("platform").split("_");
		if (platformParams.length > 1) {
			platform = (platformParams[0] != null) ? platformParams[0] : "-";
			platformVersion = (platformParams[1] != null) ? platformParams[1] : "-";
			integration = (platformParams[2] != null) ? platformParams[2] : "-";
			
			platformInfo.put("platform", platform);
			platformInfo.put("platformVersion", platformVersion);
			platformInfo.put("integration", integration);
			
		} else {
			platformInfo.put("platform", platformParams[0]);
			
		}

		return platformInfo;
		
	}	// getPlatformInfo()



}	// END CLASS
