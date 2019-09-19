package com.mss.mssweb.bc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mss.mssweb.dto.UserInfo;
import com.mss.mssweb.helper.dbservice.DBService;

public class AuthenticationBC {
	
	public static boolean authenticate(String userName, String password) {
		boolean isValid = false;

		ResultSet rsResult = null;
		Connection objConn = null;
		Statement objStmt = null;
		String sqlScript = "";
		
		sqlScript = "SELECT COUNT(*) AS TotalRecord FROM user WHERE LOWER(user_id)=LOWER('" + replaceSQL(userName) + "') AND password='" + replaceSQL(password) + "';";
		
		try {
			
			if (sqlScript != null && sqlScript != "") {
				objConn = DriverManager.getConnection(DBService.baseDBConnUrl, DBService.baseDBUserID, DBService.baseDBPassword);
				
				if (objConn != null) {
					objStmt = objConn.createStatement();
					
					rsResult = objStmt.executeQuery(sqlScript);

					while (rsResult.next()) {
						
						if (rsResult.getInt("TotalRecord") > 0) {
							isValid = true;
						}
						
					}
					
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
			
			if (objStmt != null) {
				try {
					objStmt.close();	
				}
				catch (SQLException e) { 
					e.printStackTrace(); 
				}
			}
			
			if (objConn != null) {
				try {
					objConn.close();	
				}
				catch (SQLException e) { 
					e.printStackTrace(); 
				}
			}
			
		}
		
		return isValid;
	}

	public static UserInfo getUserInfo(String userName) {
		ResultSet rsResult = null;
		Connection objConn = null;
		Statement objStmt = null;
		UserInfo userInfo = new UserInfo();
		String sqlScript = "";
		
		sqlScript = "SELECT * FROM user WHERE LOWER(user_id)=LOWER('" + replaceSQL(userName) + "');";
		
		try {
			
			if (sqlScript != null && sqlScript != "") {
				objConn = DriverManager.getConnection(DBService.baseDBConnUrl, DBService.baseDBUserID, DBService.baseDBPassword);
				
				if (objConn != null) {
					objStmt = objConn.createStatement();
					
					rsResult = objStmt.executeQuery(sqlScript);

					while (rsResult.next()) {
						
						userInfo.setUserID(rsResult.getString("user_id"));
						userInfo.setFullName(rsResult.getString("user_name"));
						
						if (rsResult.getInt("is_active") == 1) {
							userInfo.setIsActive(true);
						}
						else {
							userInfo.setIsActive(false);
						}
						
						userInfo.setUserType(rsResult.getInt("user_type"));
						
					}
					
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
			
			if (objStmt != null) {
				try {
					objStmt.close();	
				}
				catch (SQLException e) { 
					e.printStackTrace(); 
				}
			}
			
			if (objConn != null) {
				try {
					objConn.close();	
				}
				catch (SQLException e) { 
					e.printStackTrace(); 
				}
			}
			
		}
		
		return userInfo;
	}
	
	private static String replaceSQL(String strSQL) {
		
		strSQL = strSQL.replace("'", "''");
		
		return strSQL;
	}
}
