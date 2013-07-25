package com.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchUtils {

	String LOGTAG = "+ [SearchUtils]: ";

	// ####################################################################################
	public String searchImageSource(String inputString) {
		Pattern nullClickUrl = Pattern.compile("IMAGE-SOURCE");
		String mPattern = null;

		Matcher imageSourceStr = nullClickUrl.matcher(inputString);
		if (imageSourceStr.find()) {
			//System.out.println(LOGTAG + "PATTERN MATCHED: " + imageSourceStr.group());
			mPattern = imageSourceStr.group();
		}

		return mPattern;
	}


	// ####################################################################################
	public String searchLandingUrl(String inputString) {
		Pattern nullClickUrl = Pattern.compile("LANDING-PAGE");
		String mPattern = null;

		Matcher landingPageStr = nullClickUrl.matcher(inputString);
		if (landingPageStr.find()) {
			//System.out.println(LOGTAG + "PATTERN MATCHED: " + landingPageStr.group());
			mPattern = landingPageStr.group();
		}

		return mPattern;
	}

	
	// ####################################################################################
	public String searchBeaconStringM101(String inputString) {
		Pattern beaconM101 = Pattern.compile("BEACON-URL-101");
		String mPattern = null;

		Matcher beaconM101Str = beaconM101.matcher(inputString);
		if (beaconM101Str.find()) {
			//System.out.println(LOGTAG + "PATTERN MATCHED: " + beaconM101Str.group());
			mPattern = beaconM101Str.group();
		}

		return mPattern;
	}

	// ####################################################################################
	public String searchBeaconStringM18(String inputString) {
		Pattern beaconM18 = Pattern.compile("BEACON-URL-18");
		String mPattern = null;

		Matcher beaconM18Str = beaconM18.matcher(inputString);
		if (beaconM18Str.find()) {
			//System.out.println(LOGTAG + "PATTERN MATCHED: " + beaconM18Str.group());
			mPattern = beaconM18Str.group();
		}

		return mPattern;
	}

	
	// ####################################################################################
	public String searchClickUrl(String inputString) {
		Pattern nullClickUrl = Pattern.compile("CLICK-URL");
		String mPattern = null;
		
		Matcher clickUrlStr = nullClickUrl.matcher(inputString);
		if (clickUrlStr.find()) {
			//System.out.println(LOGTAG + "PATTERN MATCHED: " + clickUrlStr.group());
			mPattern = clickUrlStr.group();
		}

		return mPattern;
	}

	// ####################################################################################
	public String searchClickXmlUrl(String inputString) {
		Pattern clickXmlUrl = Pattern.compile("CLICK-XML-URL");
		String mPattern = null;

		Matcher clickXmlUrlStr = clickXmlUrl.matcher(inputString);
		if (clickXmlUrlStr.find()) {
			//System.out.println(LOGTAG + "PATTERN MATCHED: " + clickUrlStr.group());
			mPattern = clickXmlUrlStr.group();
		}

		return mPattern;
	}
	
	// ####################################################################################
	public String searchNullClickUrl(String inputString) {
		Pattern nullClickUrl = Pattern.compile("'CLICK-URL'");
		String mPattern = null;

		Matcher clickUrlStr = nullClickUrl.matcher(inputString);
		if (clickUrlStr.find()) {
			//System.out.println(LOGTAG + "PATTERN MATCHED: " + clickUrlStr.group());
			mPattern = clickUrlStr.group();
		}

		return mPattern;
	}
	
	
	// ####################################################################################
//	public String searchClickUrlFromXhtml(String inputString) {
//		Pattern nullClickUrlXhtml = Pattern.compile("CLICK-URL");
//		String mPattern = null;
//
//		Matcher clickUrlStrXhtml = nullClickUrlXhtml.matcher(inputString);
//		if (clickUrlStrXhtml.find()) {
//			//System.out.println(LOGTAG + "PATTERN MATCHED: " + clickUrlStrXhtml.group());
//			mPattern = clickUrlStrXhtml.group();
//		}
//
//		return mPattern;
//	}

	
}
