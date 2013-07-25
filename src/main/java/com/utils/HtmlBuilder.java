//package com.inmobi.psocommons;
package com.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class HtmlBuilder {

	// ##########################################################################################
	public static String build (String title) {
		String htmlFileContents = "";

		htmlFileContents = "<html>";
		htmlFileContents += htmlHead(title);	// building html head
		htmlFileContents += htmlScriptData();
		htmlFileContents += htmlStyleData();
		//htmlFileContents += htmlBody();
		htmlFileContents += "\n" + "</html>";

		return htmlFileContents;

	} // end build()


	// ##########################################################################################	
	public static String htmlHead(String resultsTitle) {

		String htmlHeadData = "";
		htmlHeadData += "\n" + "<head>";
		htmlHeadData += "\n" + "<TITLE>" + resultsTitle + "</TITLE>";

		return htmlHeadData;

	}	// end htmlHead


	// ##########################################################################################
	public static String htmlScriptData() {

		String htmlScriptData = "";
		htmlScriptData += "\n" + "<script type=\"text/javascript\">" 
				+ "\n" + "\t" + "function toggle_visibility(tbid, lnkid) {"
				+ "\n" + "\t\t" + "var obj = document.getElementsByTagName(\"table\");"
				+ "\n" + "\n\t\t" + "for (i = 0; i < obj.length; i++) {"
				+ "\n" + "\t\t\t" + "if (obj[i].id && obj[i].id != tbid) {"
				+ "\n" + "\t\t\t\t" + "x = obj[i].id.substring(3);"
				+ "\n" + "\t\t\t\t" + "document.getElementById(\"lnk\" + x).value = \"[+] Expand\";"
				+ "\n" + "\t\t\t" + "}"
				+ "\n" + "\t\t" + "}"
				+ "\n" + "\t\t" + "if (document.all) {"
				+ "\n" + "\t\t" + "document.getElementById(tbid).style.display = document"
				+ "\n" + "\t\t\t" + ".getElementById(tbid).style.display == \"block\" ? \"none\""
				+ "\n" + "\t\t\t" + ": \"block\";"

							+ "\n" + "\t\t" + "} else {"
							+ "\n" + "\t\t" + "document.getElementById(tbid).style.display = document"
							+ "\n" + "\t\t\t" + ".getElementById(tbid).style.display == \"table\" ? \"none\""
							+ "\n" + "\t\t\t" + ": \"table\";"
							+ "\n" + "\t\t" + "}"


							+ "\n" + "\t\t" + "document.getElementById(lnkid).value" +
							" = " +
							"document.getElementById(lnkid).value == \"[-] Collapse\" ? \"[+] Expand\"" +
							"\n" + " : \"[-] Collapse\";"
							+ "\n" + "\t" + "}"
							+ "\n" + "</script>";

		return htmlScriptData;

	}	// end htmlScript

	// ##########################################################################################
	public static String htmlStyleData() {

		String htmlStyleContents = "";
		htmlStyleContents += "\n" + "<style type=\"text/css\">"
				+ "\n" +  ".tbl {"
				+ "\n" + "\t" + "display: none;"
				+ "\n" +  "}"
				+ "\n"
				+ "\n" + ".lnk {"
				+ "\n" + "\t" + "border: none;"
				+ "\n" + "\t" + "background: none;"
				+ "\n" + "\t" + "width: 85px;"
				+ "\n" + "}"
				+ "\n"
				+ "\n" + "td {"
				+ "\n" + "\t" + "FONT-SIZE: 75%;"
				+ "\n" + "\t" + "MARGIN: 0px;"
				+ "\n" + "\t" + "COLOR: #000000;"
				+ "\n" + "}"
				+ "\n"
				+ "\n" + "td {"
				+ "\n" + "\t" + "FONT-FAMILY: verdana, helvetica, arial, sans-serif"
				+ "\n" + "}"
				+ "\n"
				+ "\n" + "a {"
				+ "\n" + "\t" + "TEXT-DECORATION: none;"
				+ "\n" + "}"
				+ "\n" + "</style>"
				+ "\n" + "</head>";

		return htmlStyleContents;

	}


	// ##############################################################################################################
	// Generating html-body
	public static String htmlBody(HashMap<String, ArrayList<String>> htmlBodyData) {

		String htmlFileBody = "";
		htmlFileBody += "\n" + "<body>";
		
		int htmlTableCount = 1;
		for (String title : htmlBodyData.keySet()) {
			
			if (!title.equalsIgnoreCase("headers")) {
				
				htmlFileBody += "\n" + "<!--  TABLE # 1 -->"
						+ "\n" + "\t" + "<table width=\"800px\" border=\"0\" align=\"left\" cellpadding=\"4\""
						+ "\n" + "\t\t" + "cellspacing=\"0\">"
						+ "\n" + "\t\t" + "<tr height=\"1\">"
						+ "\n" + "\t\t\t" + "<td bgcolor=\"#727272\" colspan=\"10\"></td>"
						+ "\n" + "\t\t" + "</tr>";

				// Table title:
				htmlFileBody += "\n" + "\t\t" + "<tr bgcolor=\"#CCCCCC\" height=\"15\">"
						+ "\n" + "\t\t\t" + "<td><strong>Slot Size: " + title + "</strong></td>"
						+ "\n" + "\t\t\t" + "<td></td>"
						+ "\n" + "\t\t\t" + "<td bgcolor=\"#CCCCCC\" align=\"right\"><input class=\"lnk\" id=\"lnk" + htmlTableCount + "\""
						+ "\n" + "\t\t\t\t" + "type=\"button\" value=\"[+] Expand\""
						+ "\n" + "\t\t\t\t" + "onclick=\"toggle_visibility('tbl" + htmlTableCount + "','lnk" + htmlTableCount + "');\"></td>"
						+ "\n" + "\t\t" + "</tr>";


				// Table description:
				htmlFileBody += "\n" + "\t\t" + "<tr>"
						+ "\n" + "\t\t\t" + "<td colspan=\"10\">"
						+ "\n" + "\t\t\t" + "<table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"0\""
						+ "\n" + "\t\t\t" + "id=\"tbl" + htmlTableCount + "\" class=\"tbl\">"
						+ "\n" + "\t\t" + "<tr>"
						+ "\n" + "\t\t\t" + "<td colspan=\"10\">Ad results for SlotSize: " + title + "</td>"
						+ "\n" + "\t\t" + "</tr>";


				// Building column headers
				ArrayList<String> headersList = htmlBodyData.get("headers");
				int totalColumns = headersList.size();
				int widthAdjustment = 100/totalColumns;

				htmlFileBody += "\n" + "\t\t" + "<tr bgcolor=\"#CCCCCC\">";
				for (String header : headersList) {
					htmlFileBody += "\n" + "\t\t\t" + "<td width=\"" + widthAdjustment + "%\"><strong>" + header + "</strong></td>";				
				}
				htmlFileBody += "\n" + "\t\t" + "</tr>";


				// Build rows
				ArrayList<String> keyBindedData = htmlBodyData.get(title);
				for (String row : keyBindedData) {
					String[] cellValues = row.split(",");

					// Row data entered:
					htmlFileBody += "\n" + "\t\t" + "<!--  row data	 -->"
							+ "\n" + "\t\t" + "<tr>";
					for (String value : cellValues) {
						htmlFileBody += "\n" + "\t\t\t" + "<td>" + value + "</td>"; 
					}

					htmlFileBody += "\n" + "\t\t" + "</tr>"
							+ "\n" + "\t\t" + "<tr height=\"1\">"
							+ "\n" + "\t\t\t" + "<td colspan=\"10\" bgcolor=\"#EEEEEE\"></td>"
							+ "\n" + "\t\t" + "</tr>";

				}
				
				htmlTableCount++;
				
			}	// end if() - not a header

		}	// end for() - titles

		htmlFileBody += "\n" + "</body>";

		return htmlFileBody;

	}
	// ##########################################################################################
	// for testing purpose
	public static void main(String[] args) {
		System.out.println(build("Test Results") );
	}

}	// end class - HtmlBuilder()
