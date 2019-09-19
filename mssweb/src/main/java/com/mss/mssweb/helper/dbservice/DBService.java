package com.mss.mssweb.helper.dbservice;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBService {
	
	private static DBService databaseService;
	
	/* Constant variables. */
	//public static String baseDBConnUrl = "jdbc:mysql://localhost:3306/sakila?characterEncoding=latin1&useConfigs=maxPerformance"; 
	//public static String baseDBConnUrl = "jdbc:mysql://localhost:3306/tzuchi?characterEncoding=latin1&useConfigs=maxPerformance";
	public static String baseDBConnUrl = "jdbc:mysql://localhost:3306/tzuchi?characterEncoding=UTF-8&useConfigs=maxPerformance";
	public static String baseDBUserID = "juneboon";
	public static String baseDBPassword = "pass@word1";
	
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	
	public DBService() {
		
	}
	
	
	private static Connection getConnection2() {
		Connection objConn = null;
		
		try {
			
			objConn = DriverManager.getConnection(baseDBConnUrl, baseDBUserID, baseDBPassword);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if (objConn != null) {
				try {
					objConn.close();	
				}
				catch (SQLException e) { 
					e.printStackTrace(); 
				}
			}
			
		}
		
		return objConn;
	}
	
	public static ResultSet RunSQLReturnResultSet(String sqlScript) {
		ResultSet rsResult = null;
		Connection objConn = null;
		Statement objStmt = null;
		
		try {
			
			if (sqlScript != null && sqlScript != "") {
				objConn = DriverManager.getConnection(baseDBConnUrl, baseDBUserID, baseDBPassword);
				
				if (objConn != null) {
					objStmt = objConn.createStatement();
					
					rsResult = objStmt.executeQuery(sqlScript);
					/*
					while (rsResult.next()) {
						rsResult.getbyte
						
						String id = rs.getString("actor_id");
						String firstName = rs.getString("first_name"); 
						String lastName = rs.getString("last_name");
					}
					*/
				}
			}
			
			
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if (rsResult != null) {
				try {
					rsResult.close();	
				}
				catch (SQLException e) { 
					e.printStackTrace(); 
				}
			}
			
		}
		
		return rsResult;
	}
	
	
	public String getData() {
		String strResult = "";
		
		try {
//			new com.mysql.jdbc.Driver();
			Class.forName("com.mysql.jdbc.Driver").newInstance();
	//conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdatabase?user=testuser&password=testpassword");
			String connectionUrl = "jdbc:mysql://localhost:3306/sakila?characterEncoding=latin1&useConfigs=maxPerformance";
			String connectionUser = "juneboon";
			String connectionPassword = "pass@word1";
			conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from actor");
			while (rs.next()) {
				String id = rs.getString("actor_id");
				String firstName = rs.getString("first_name"); 
				String lastName = rs.getString("last_name");
				strResult = strResult + "ID: " + id + ", First Name: " + firstName
						+ ", Last Name: " + lastName + "<br>";
				//System.out.println("ID: " + id + ", First Name: " + firstName
				//		+ ", Last Name: " + lastName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		
		return strResult;
	}
}
