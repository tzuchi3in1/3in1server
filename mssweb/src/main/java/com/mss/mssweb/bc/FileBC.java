package com.mss.mssweb.bc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.mss.mssweb.helper.dbservice.*;
import com.mss.mssweb.dto.*;

public class FileBC {

	public static ArrayList<ImageFile> searchImage(int pageNo, int pageSize, String fileName, String eventName) {
		String sqlScript = "";
		int limitOffset = 0;
		int limitCount = 10;
		
		if (pageNo > 0 && pageSize > 0) {
			limitOffset = (pageNo - 1) * pageSize;
			limitCount = pageSize;
		}
		
		//sqlScript = "SELECT * FROM ImageFile WHERE FileName = '" + fileName + "'";
		sqlScript = "SELECT * FROM prop_file LIMIT " + limitOffset + ", " + limitCount + ";";
		System.out.println(sqlScript);
		return SearchImage(sqlScript);
	}
	
	public static int getTotalRecord(String fileName, String eventName) {
		String sqlScript = "";
		
		//sqlScript = "SELECT * FROM ImageFile WHERE FileName = '" + fileName + "'";
		sqlScript = "SELECT COUNT(*) AS TotalRecord FROM prop_file";
		
		return GetRecordCount(sqlScript);
	}
	
	public static HashMap<String,String> getCategory(String code) {
		String sqlScript = "";
		
		sqlScript = "SELECT * FROM prop_category ";
		
		if (code != "") {
			sqlScript = sqlScript + " WHERE code = '" + code + "' ";
		}
		
		sqlScript = sqlScript + " ORDER BY description";
		
		return GetCategory(sqlScript);
	}
	
	private static ArrayList<ImageFile> SearchImage(String sqlScript) {
		ResultSet rsResult = null;
		Connection objConn = null;
		Statement objStmt = null;
		ArrayList<ImageFile> alImageFiles = new ArrayList<ImageFile>();
		
		try {
			
			if (sqlScript != null && sqlScript != "") {
				objConn = DriverManager.getConnection(DBService.baseDBConnUrl, DBService.baseDBUserID, DBService.baseDBPassword);
				
				if (objConn != null) {
					objStmt = objConn.createStatement();
					
					rsResult = objStmt.executeQuery(sqlScript);
					
					while (rsResult.next()) {
						
						ImageFile objImage = new ImageFile();
						objImage.setFileName(rsResult.getString("file_name"));
						objImage.setFilePath(rsResult.getString("file_path"));
						objImage.setFileExt(rsResult.getString("file_ext"));
						objImage.setOriginalComment(rsResult.getString("comm_original"));
						objImage.setEditedComment(rsResult.getString("comm_edited"));
						objImage.setPhotographerCode(rsResult.getString("photographer_code"));
						objImage.setDateFrom(rsResult.getDate("date_from"));
						objImage.setDateTo(rsResult.getDate("date_to"));
						
						alImageFiles.add(objImage);
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
		
		return alImageFiles;
	}
	
	public static boolean saveFile(String fileName, InputStream fileContent) {
		boolean savedSuccess = false;
		boolean insertDBSuccess = false;
		String rootPath = "D:\\Development\\eclipse-workspace\\MSS_1\\mssweb\\src\\main\\resources\\";
		String childPath = "files\\P001";
		String fileExt = getFileExtension(fileName);
		OutputStream outStream = null;
		
		/* Save to physical folder first. */
		try 
		{
			if (fileContent != null) {
				
				byte[] buffer = new byte[fileContent.available()];
				fileContent.read(buffer);
			    
				File targetFile = new File(rootPath + childPath + "\\" + fileName);
			    outStream = new FileOutputStream(targetFile);
			    outStream.write(buffer);
			    
			    savedSuccess = true;
			}
		}
		catch (IOException e) { 
			e.printStackTrace(); 
		}
		finally {
			try {
				outStream.close();
			}
			catch (IOException e) { 
				e.printStackTrace(); 
			}
		}
		
		/* Save to database. */
		ResultSet rsResult = null;
		Connection objConn = null;
		Statement objStmt = null;
		String sqlScript = ""; 
		
		
		try {
			
			if (savedSuccess) {
				objConn = DriverManager.getConnection(DBService.baseDBConnUrl, DBService.baseDBUserID, DBService.baseDBPassword);
				
				if (objConn != null) {
					/* Validation. */
					sqlScript = "SELECT * FROM prop_file WHERE file_name = '" + fileName + "';";
					
					objStmt = objConn.createStatement();
					rsResult = objStmt.executeQuery(sqlScript);
					rsResult.last();
				    int count = rsResult.getRow();
				    rsResult.close();
				    objStmt.close();
				    
				    /* Insert to table. */
					if (count <= 0) {
					
						sqlScript = "INSERT INTO prop_file (" + 
									"file_name, file_path, file_ext, " + 
									"comm_original, comm_edited, date_from, date_to, " + 
									"photographer_code) VALUES (" + 
									"'" + fileName + "', '" + childPath + "', '" + fileExt + "', " + 
									"'Photographer comment.', 'Editor comment.', NOW(), NOW(), " +
									"'P001'" + 
									");";
						
						objStmt = objConn.createStatement();
						count = objStmt.executeUpdate(sqlScript);
						
						insertDBSuccess = true;
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
		
		return savedSuccess;
	}
	
	private static int GetRecordCount(String sqlScript) {
		ResultSet rsResult = null;
		Connection objConn = null;
		Statement objStmt = null;
		int totalRecord = 0;
		
		try {
			
			if (sqlScript != null && sqlScript != "") {
				objConn = DriverManager.getConnection(DBService.baseDBConnUrl, DBService.baseDBUserID, DBService.baseDBPassword);
				
				if (objConn != null) {
					objStmt = objConn.createStatement();
					
					rsResult = objStmt.executeQuery(sqlScript);
					
					while (rsResult.next()) {
						totalRecord = rsResult.getInt("TotalRecord");
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
		
		return totalRecord;
	}
	
	public static String getFileExtension(String fileName) {
		String fileExt = "";
		
		if (fileName != null && fileName != "") {
			fileExt = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		}
		
		return fileExt;
	}
	
	private static HashMap<String,String> GetCategory(String sqlScript) {
		ResultSet rsResult = null;
		Connection objConn = null;
		Statement objStmt = null;
		HashMap<String,String> hmResult = new HashMap<String,String>();
		
		try {
			
			if (sqlScript != null && sqlScript != "") {
				objConn = DriverManager.getConnection(DBService.baseDBConnUrl, DBService.baseDBUserID, DBService.baseDBPassword);
				
				if (objConn != null) {
					objStmt = objConn.createStatement();
					
					rsResult = objStmt.executeQuery(sqlScript);
					
					while (rsResult.next()) {
						if (!hmResult.containsKey(rsResult.getString("code"))) {
							hmResult.put(rsResult.getString("code"), rsResult.getString("description"));
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
		
		return hmResult;
	}
}
