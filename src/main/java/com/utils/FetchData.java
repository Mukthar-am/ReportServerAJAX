package com.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;


public class FetchData {

	private static Connection connection = null;
	public static String LOGTAG = "+ [FetchData]: ";
	static String psoResourcePkg = "/com/inmobi/pso";


	public static Connection getConnection() {
		String DBCONFIG = psoResourcePkg + "/configs/db.properties";
		if (connection != null)
			return connection;
		else {
			try {
				Properties prop = new Properties();
				InputStream inputStream = FetchData.class.getClassLoader().getResourceAsStream(DBCONFIG);

//				prop.load(inputStream);
//				String driver = prop.getProperty("driver");
//				String url = prop.getProperty("url");
//				String user = prop.getProperty("user");
//				String password = prop.getProperty("password");
				
				//Use this for local runs
				String driver="com.mysql.jdbc.Driver";
				String url="jdbc:mysql://localhost:3306/psotest";
				String user="psotest";
				String password="psotest123";
						
				Class.forName(driver);
				connection = DriverManager.getConnection(url, user, password);

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
//			} 
//			catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
			}
			return connection;
		}

	}


	public ArrayList<String> getValuesToQuery( String key ) {
		connection = FetchData.getConnection();
		ArrayList<String> queryValues = new ArrayList<String>();
		try {
			Statement statement = connection.createStatement();
			String getAllSlotsQuery = "SELECT" +
										" DISTINCT(" + key + ") "
										+ "FROM clicks"
										+ " order by " + key + " asc";
			System.out.println(" Groupby query: " + getAllSlotsQuery);
			ResultSet rs = statement.executeQuery(getAllSlotsQuery);

			// Get all slot sizes:
			while(rs.next()) { 
				queryValues.add(rs.getString(key));
				System.out.println(rs.getString(key));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return queryValues;
	}


	public ArrayList<String> testResults (String slotSize) throws SQLException {
		connection = FetchData.getConnection();
		ArrayList<String> inputQueryResults = new ArrayList<String>();
		
		Statement statement = null;
		try {
			statement = connection.createStatement();
			
			String resultsPopulatorQuery = "select " +
										"c.testid, c.datetime, c.slot, c.creative, c.releasetype, c.platform, c.version, c.integration, " +
										"b.beacon as beacon_resp, " +
										"c.status " +
										"from " +
										"clicks c left outer join beacon b " +
										"on " +
										"c.testid = b.testid where c.slot = '" + slotSize + "'";
			
			
			System.out.println(LOGTAG + "Query: " + resultsPopulatorQuery);
			ResultSet resultsSet = statement.executeQuery(resultsPopulatorQuery);

			while ( resultsSet.next() ) {				
				int columnCount = resultsSet.getMetaData().getColumnCount();
				String rowData = "";
				
				for (int i = 1; i <= columnCount; i++) {
					rowData += resultsSet.getString(i);
					if (i != columnCount) {
						rowData += ",";
					}
				}
				
				inputQueryResults.add(rowData);
				
			}	// while()
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
 
		return inputQueryResults;
		
	}	// end testResults()

	
	
	public static void main(String[] args) {
		
		
		FetchData fetcher = new FetchData();
		
		
		try {
			fetcher.getValuesToQuery( "slot" );
			fetcher.testResults("320x480");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}	// end FetchData

