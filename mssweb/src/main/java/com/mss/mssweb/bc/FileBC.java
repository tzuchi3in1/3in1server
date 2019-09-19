package com.mss.mssweb.bc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import com.mss.mssweb.helper.dbservice.*;
import com.icafe4j.date.DateTime;
import com.mss.mssweb.dto.*;

import org.tzuchi.syslib.metadata.*;

public class FileBC {
	
	public static ArrayList<ImageFile> searchImageMin(int pageNo, int pageSize, 
			   									      String searchText, Date eventDateFrom, Date eventDateTo) {
		String sqlScript = "";
		int limitOffset = 0;
		int limitCount = 10;
		
		if (pageNo > 0 && pageSize > 0) {
			limitOffset = (pageNo - 1) * pageSize;
			limitCount = pageSize;
		}
		
		//sqlScript = "SELECT * FROM ImageFile WHERE FileName = '" + fileName + "'";
		sqlScript = "SELECT pf.*, pm.description FROM prop_file pf " + 
					"LEFT JOIN link_file_mission lfm ON lfm.file_name = pf.file_name " + 
					"LEFT JOIN prop_mission pm ON pm.code = lfm.mission_code " + 
					"WHERE 1=1 ";
		
		if (searchText != "") {
			sqlScript = sqlScript + " AND (";
			sqlScript = sqlScript + " pf.file_name LIKE '%" + searchText + "%' ";
			sqlScript = sqlScript + " OR pf.event_name LIKE '%" + searchText + "%' ";
			sqlScript = sqlScript + " OR pf.place_name LIKE '%" + searchText + "%' ";
			sqlScript = sqlScript + " OR pf.comm_original LIKE '%" + searchText + "%' ";
			sqlScript = sqlScript + " OR pf.photographer_code LIKE '%" + searchText + "%' ";
			sqlScript = sqlScript + " OR pf.photographer_name LIKE '%" + searchText + "%' ";
			sqlScript = sqlScript + " OR pf.keyword LIKE '%" + searchText + "%' ";
			sqlScript = sqlScript + " OR UPPER(pm.description) LIKE '%" + searchText.toUpperCase() + "%' ";
			sqlScript = sqlScript + " ) ";
		}
		
		if (eventDateFrom != null && eventDateTo != null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strEventDateFrom = dateFormat.format(eventDateFrom) + " 00:00:00";
			String strEventDateTo = dateFormat.format(eventDateTo) + " 23:59:59";
			
			//System.out.println("strEventDateFrom: " + strEventDateFrom);
			//System.out.println("strEventDateTo: " + strEventDateTo);
			
			sqlScript = sqlScript + " AND date_from BETWEEN '" + strEventDateFrom + "' AND '" + strEventDateTo + "' ";
		}
		
		sqlScript = sqlScript + " LIMIT " + limitOffset + ", " + limitCount + ";";

		//sqlScript = "SELECT * FROM prop_file LIMIT " + limitOffset + ", " + limitCount + ";";
		//System.out.println(sqlScript);
		return SearchImage(sqlScript);
	}
	
	public static int getTotalRecordMin(String searchText, Date eventDateFrom, Date eventDateTo) {
		String sqlScript = "";
		
		sqlScript = "SELECT COUNT(*) AS TotalRecord FROM prop_file pf " + 
					"LEFT JOIN link_file_mission lfm ON lfm.file_name = pf.file_name " + 
					"LEFT JOIN prop_mission pm ON pm.code = lfm.mission_code " + 
					"WHERE 1=1 ";
		
		if (searchText != "") {
			sqlScript = sqlScript + " AND (";
			sqlScript = sqlScript + " pf.file_name LIKE '%" + searchText + "%' ";
			sqlScript = sqlScript + " OR pf.event_name LIKE '%" + searchText + "%' ";
			sqlScript = sqlScript + " OR pf.place_name LIKE '%" + searchText + "%' ";
			sqlScript = sqlScript + " OR pf.comm_original LIKE '%" + searchText + "%' ";
			sqlScript = sqlScript + " OR pf.photographer_code LIKE '%" + searchText + "%' ";
			sqlScript = sqlScript + " OR pf.photographer_name LIKE '%" + searchText + "%' ";
			sqlScript = sqlScript + " OR pf.keyword LIKE '%" + searchText + "%' ";
			sqlScript = sqlScript + " OR UPPER(pm.description) LIKE '%" + searchText.toUpperCase() + "%' ";
			sqlScript = sqlScript + " ) ";
		}
		
		if (eventDateFrom != null && eventDateTo != null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strEventDateFrom = dateFormat.format(eventDateFrom) + " 00:00:00";
			String strEventDateTo = dateFormat.format(eventDateTo) + " 23:59:59";
			
			//System.out.println("strEventDateFrom: " + strEventDateFrom);
			//System.out.println("strEventDateTo: " + strEventDateTo);
			
			sqlScript = sqlScript + " AND date_from BETWEEN '" + strEventDateFrom + "' AND '" + strEventDateTo + "' ";
		}
		
		return GetRecordCount(sqlScript);
	}

	public static ArrayList<ImageFile> searchImage(int pageNo, int pageSize, 
												   String fileName, String eventName, 
												   String placeName, String commOriginal, 
												   String photographerCode, String photographerName, 
												   String keyword, String missionCode, 
												   Date eventDateFrom, Date eventDateTo) {
		String sqlScript = "";
		int limitOffset = 0;
		int limitCount = 10;
		
		if (pageNo > 0 && pageSize > 0) {
			limitOffset = (pageNo - 1) * pageSize;
			limitCount = pageSize;
		}
		
		//sqlScript = "SELECT * FROM ImageFile WHERE FileName = '" + fileName + "'";
		sqlScript = "SELECT * FROM prop_file WHERE 1=1 ";
		
		if (fileName != "") {
			sqlScript = sqlScript + " AND file_name LIKE '%" + fileName + "%' ";
		}
		
		if (eventName != "") {
			sqlScript = sqlScript + " AND event_name LIKE '%" + eventName + "%' ";
		}
		
		if (placeName != "") {
			sqlScript = sqlScript + " AND place_name LIKE '%" + placeName + "%' ";
		}
		
		if (commOriginal != "") {
			sqlScript = sqlScript + " AND comm_original LIKE '%" + commOriginal + "%' ";
		}
		
		if (photographerCode != "") {
			sqlScript = sqlScript + " AND photographer_code = '" + photographerCode + "' ";
		}
		
		if (photographerName != "") {
			sqlScript = sqlScript + " AND photographer_name LIKE '%" + photographerName + "%' ";
		}
		
		if (keyword != "") {
			sqlScript = sqlScript + " AND keyword LIKE '%" + keyword + "%' ";
		}
		
		if (missionCode != "") {
			sqlScript = sqlScript + " AND mission_code = '" + missionCode + "' ";
		}
		
		if (eventDateFrom != null && eventDateTo != null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strEventDateFrom = dateFormat.format(eventDateFrom) + " 00:00:00";
			String strEventDateTo = dateFormat.format(eventDateTo) + " 23:59:59";
			
			//System.out.println("strEventDateFrom: " + strEventDateFrom);
			//System.out.println("strEventDateTo: " + strEventDateTo);
			
			sqlScript = sqlScript + " AND date_from BETWEEN '" + strEventDateFrom + "' AND '" + strEventDateTo + "' ";
		}
		
		sqlScript = sqlScript + " LIMIT " + limitOffset + ", " + limitCount + ";";

		//sqlScript = "SELECT * FROM prop_file LIMIT " + limitOffset + ", " + limitCount + ";";
		//System.out.println(sqlScript);
		return SearchImage(sqlScript);
	}
	
	public static ArrayList<ImageFile> getImageDetails(String fileName) {
		String sqlScript = "";
		
		//sqlScript = "SELECT * FROM ImageFile WHERE FileName = '" + fileName + "'";
		sqlScript = "SELECT * FROM prop_file WHERE file_name = '" + fileName + "';";
		System.out.println(sqlScript);
		
		ArrayList<ImageFile> alImage = SearchImage(sqlScript);
		System.out.println("Count1: " + alImage.size());
		if (alImage.size() > 0) {
			// Get the selected category for this image.
			String fileName2 = alImage.get(0).getFileName();
			//String sqlFileCategory = "SELECT * FROM link_file_category WHERE file_name = '" + fileName2 + "';";
			// stop here
			ArrayList<Category> alFileCategory = GetFileCategory(fileName2);
			System.out.println("Count: " + alFileCategory.size());
			alImage.get(0).setCategory(alFileCategory);
		}
		
		return alImage;
	}
	
	//public static int getTotalRecord(String fileName, String eventName) {
	public static int getTotalRecord(String fileName, String eventName, 
									 String placeName, String commOriginal, 
									 String photographerCode, String photographerName, 
									 String keyword, String missionCode, 
									 Date eventDateFrom, Date eventDateTo) {
		String sqlScript = "";
		
		//sqlScript = "SELECT * FROM ImageFile WHERE FileName = '" + fileName + "'";
		sqlScript = "SELECT COUNT(*) AS TotalRecord FROM prop_file WHERE 1=1 ";
		
		if (fileName != "") {
			sqlScript = sqlScript + " AND file_name LIKE '%" + fileName + "%' ";
		}
		
		if (eventName != "") {
			sqlScript = sqlScript + " AND event_name LIKE '%" + eventName + "%' ";
		}
		
		if (placeName != "") {
			sqlScript = sqlScript + " AND place_name LIKE '%" + placeName + "%' ";
		}
		
		if (commOriginal != "") {
			sqlScript = sqlScript + " AND comm_original LIKE '%" + commOriginal + "%' ";
		}
		
		if (photographerCode != "") {
			sqlScript = sqlScript + " AND photographer_code = '" + photographerCode + "' ";
		}
		
		if (photographerName != "") {
			sqlScript = sqlScript + " AND photographer_name LIKE '%" + photographerName + "%' ";
		}
		
		if (keyword != "") {
			sqlScript = sqlScript + " AND keyword LIKE '%" + keyword + "%' ";
		}
		
		if (missionCode != "") {
			sqlScript = sqlScript + " AND mission_code = '" + missionCode + "' ";
		}
		
		if (eventDateFrom != null && eventDateTo != null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strEventDateFrom = dateFormat.format(eventDateFrom) + " 00:00:00";
			String strEventDateTo = dateFormat.format(eventDateTo) + " 23:59:59";
			
			//System.out.println("strEventDateFrom: " + strEventDateFrom);
			//System.out.println("strEventDateTo: " + strEventDateTo);
			
			sqlScript = sqlScript + " AND date_from BETWEEN '" + strEventDateFrom + "' AND '" + strEventDateTo + "' ";
		}
		
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
	
	public static ArrayList<Category> getAllCategory() {
		String sqlScript = "";
		
		sqlScript = "SELECT * FROM prop_category ";
		sqlScript = sqlScript + " ORDER BY code";
		
		ArrayList<Category> alCategory = GetAllCategory(sqlScript);
		
		if (alCategory.size() > 0) {
			for(int i = 0; i < alCategory.size(); i++) {
				System.out.println("Code: " + alCategory.get(i).getCode() + ", Parent Code: " + alCategory.get(i).getParentCode());
				if (alCategory.get(i).getParentCode() != null && alCategory.get(i).getParentCode() != "") {
					alCategory.get(i).setParentCategory(getParentCategory(alCategory, alCategory.get(i).getParentCode()));
				}
			}
		}
		
		return alCategory;
	}
	
	private static Category getParentCategory(ArrayList<Category> alCategory, String parentCode) {
		Category parentCategory = new Category();
		System.out.println("Parent Code: " + parentCode);
		for(int i = 0; i < alCategory.size(); i++) {
			System.out.println("Code2: " + alCategory.get(i).getCode().toLowerCase());
			System.out.println("Boolean: " + (Integer.parseInt(alCategory.get(i).getCode().toString()) == Integer.parseInt(parentCode.toLowerCase())));
			if(Integer.parseInt(alCategory.get(i).getCode().toString()) == Integer.parseInt(parentCode.toLowerCase())) {
				System.out.println("Parent Category Code: " + alCategory.get(i).getCode());
				parentCategory = alCategory.get(i);
				break;
			}
		}
		
		return parentCategory;
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
						objImage.setEventName(rsResult.getString("event_name"));
						objImage.setFilePlace(rsResult.getString("place_name"));
						objImage.setMissionCode(rsResult.getString("mission_code"));
						objImage.setRemarks(rsResult.getString("remarks"));
						objImage.setKeyword(rsResult.getString("keyword"));
						
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
	
	// JB(20190605): To be removed.
	public static boolean saveFile(String fileName, InputStream fileContent) {
		boolean savedSuccess = false;
		boolean insertDBSuccess = false;
		String rootPath = "D:\\Development\\eclipse-workspace\\MSS_1\\mssweb\\src\\main\\resources\\";
		//String rootPath = "D:\\Development\\eclipse-workspace\\files\\";
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
	
	public static boolean saveFile2(String fileName, InputStream fileContent) {
		/*
		Root path to store files:  D:\\Development\\eclipse-workspace\\files\\
		Full path format: <root_path>\<upload_date_in_yyyy-MM-dd_format>\<file_name>
		Example: D:\Development\eclipse-workspace\files\hyx02\2019-06-04\empty.jpg
		*/
		
		boolean folderSuccess = false;
		boolean savedSuccess = false;
		boolean readSuccess = false;
		boolean insertDBSuccess = false;
		
		String rootPath = "D:\\Development\\eclipse-workspace\\files";
		//String photographerCodeFolder = photographerCode;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String dateFolder = dateFormat.format(date);
		
		String fullPathNoFileName = rootPath + File.separator + dateFolder;
		String fullPathWithFileName = rootPath + File.separator + dateFolder + File.separator + fileName;
		
		String fileExt = getFileExtension(fileName);
		OutputStream outStream = null;
		
		
		
		/* Save to physical folder first. */
		try 
		{
			if (fileContent != null) {
				
				// Create photographer code and date folder if not existed.
				File dateFolderPath = new File(fullPathNoFileName);
				if (!dateFolderPath.exists()) {
		            if (dateFolderPath.mkdirs()) {
		            	folderSuccess = true;
		                System.out.println("Multiple directories are created!");
		            } else {
		                System.out.println("Failed to create multiple directories!");
		            }
		        }
				else
				{
					folderSuccess = true;
				}
				
				// Save file to folder.
				if (folderSuccess) {
					
					byte[] buffer = new byte[fileContent.available()];
					fileContent.read(buffer);

					File targetFile = new File(fullPathWithFileName);
				    outStream = new FileOutputStream(targetFile);
				    outStream.write(buffer);
				    
				    savedSuccess = true;
				}
				
				
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
		
		String dbFileName = fileName;
		String dbFilePath = dateFolder;
		String dbFileExt = getFileExtension(fileName);
		String dbCommOri = "";
		String dbCommEdited = "";
		Date dbDateFromTmp = new Date();
		Date dbDateToTmp = new Date();
		DateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dbDateFrom = "";
		String dbDateTo = "";
		String dbPhotographerCode = "";
		String dbPhotographerName = "";
		String dbEventName = "";
		String dbPlaceName = "";
		String dbRemarks = "";
		String dbMissionCode = "";
		String dbKeyword = "";
		
		
		// Read the file from path.
		try {
			if (savedSuccess) {
				
				uo_tjx_metadata_tz meta = new uo_tjx_metadata_tz();
				meta.tzRead(new File(fullPathWithFileName));
				
				dbCommOri = meta.TZ_COMMENTS;
				dbDateFromTmp = meta.TZ_DATE_FM;
				dbDateToTmp = meta.TZ_DATE_TO;
				dbPhotographerCode = meta.TZ_PCODE;
				dbPhotographerName = meta.TZ_PNAME_1;
				dbEventName = meta.TZ_EVENT;
				dbPlaceName = meta.TZ_PLACE_DESC;
				dbKeyword = meta.TZ_KEYWORDS;
				dbMissionCode = meta.TZ_MISSION;
				dbRemarks = meta.TZ_REMARKS;
				
				if (dbDateFromTmp != null) {
					dbDateFrom = dbDateFormat.format(dbDateFromTmp);
				}
				if (dbDateToTmp != null) {
					dbDateTo = dbDateFormat.format(dbDateToTmp);
				}
				
				readSuccess = true;
			}
		}
		catch (Exception e) { 
			e.printStackTrace(); 
		}
		finally {
			try {
				outStream.close();
			}
			catch (Exception e) { 
				e.printStackTrace(); 
			}
		}
		
		
		
		/* Save to database. */
		ResultSet rsResult = null;
		Connection objConn = null;
		Statement objStmt = null;
		String sqlScript = ""; 
		
		try {
			
			if (readSuccess) {

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
									"photographer_code, photographer_name, " + 
									"event_name, place_name, " + 
									"mission_code, remarks, " +
									"keyword" + 
									") VALUES (" + 
									"'" + dbFileName + "', '" + dbFilePath + "', '" + dbFileExt + "', " + 
									"'" + dbCommOri + "', '" + dbCommEdited + "', ";
						
						if (!dbDateFrom.equals("")) {
							sqlScript = sqlScript + "'" + dbDateFrom + "', ";
						}
						else {
							sqlScript = sqlScript + " null, ";
						}
						
						if (!dbDateTo.equals("")) {
							sqlScript = sqlScript + "'" + dbDateTo + "', ";
						}
						else {
							sqlScript = sqlScript + " null, ";
						}
						//sqlScript = sqlScript + "'" + dbDateFrom + "', '" + dbDateTo + "', ";
								
						sqlScript = sqlScript + "'" + dbPhotographerCode + "', '" + dbPhotographerName + "', " + 
									"'" + dbEventName + "', '" + dbPlaceName + "', " +
									"'" + dbMissionCode + "', '" + dbRemarks + "', " +
									"'" + dbKeyword + "'" +
									");";
						
						objStmt = objConn.createStatement();
						count = objStmt.executeUpdate(sqlScript);
						
						if (dbMissionCode != "") {
							sqlScript = "INSERT INTO link_file_mission (file_name, mission_code) VALUES (";
							sqlScript = sqlScript + "'" + dbFileName + "', '" + dbMissionCode + "');";
							
							objStmt = objConn.createStatement();
							count = objStmt.executeUpdate(sqlScript);
						}
						
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
	
	public static HashMap<String,String> GetMissionCode() {
		HashMap<String,String> hmResult = new HashMap<String,String>();
		
		hmResult.put("CS","Charity/慈善");
		hmResult.put("YL","Medicine/醫療");
		hmResult.put("JY","Education/教育");
		hmResult.put("RW","Humanistic Culture/人文");
		
		return hmResult;
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

	private static ArrayList<Category> GetAllCategory(String sqlScript) {
		ResultSet rsResult = null;
		Connection objConn = null;
		Statement objStmt = null;
		ArrayList<Category> alResult = new ArrayList<Category>();
		
		try {
			
			if (sqlScript != null && sqlScript != "") {
				objConn = DriverManager.getConnection(DBService.baseDBConnUrl, DBService.baseDBUserID, DBService.baseDBPassword);
				
				if (objConn != null) {
					objStmt = objConn.createStatement();
					
					rsResult = objStmt.executeQuery(sqlScript);
					
					while (rsResult.next()) {
						String code = rsResult.getString("code");
						//System.out.println("Code: " + code);
						String description = rsResult.getString("description");
						//System.out.println("description: " + description);
						String parentCode = rsResult.getString("parent_code");
						//System.out.println("parent_code: " + parentCode);
						
						//Category category = new Category(rsResult.getString("code"), rsResult.getString("description"), rsResult.getString("parent_code"));
						Category category = new Category(code, description, parentCode);
						//System.out.println("Category: " + code);
						alResult.add(category);
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
		
		return alResult;
	}

	private static ArrayList<Category> GetFileCategory(String fileName) {
		ResultSet rsResult = null;
		Connection objConn = null;
		Statement objStmt = null;
		ArrayList<Category> alResult = new ArrayList<Category>();
		
		try {
			
			String sqlScript = "SELECT pc.*, fc.* " + 
							   "FROM link_file_category fc " + 
							   "INNER JOIN prop_category pc ON pc.code = fc.prop_category_code " + 
							   "WHERE fc.file_name = '" + fileName + "';";
			
			objConn = DriverManager.getConnection(DBService.baseDBConnUrl, DBService.baseDBUserID, DBService.baseDBPassword);
			
			if (objConn != null) {
				objStmt = objConn.createStatement();
				
				rsResult = objStmt.executeQuery(sqlScript);
				
				while (rsResult.next()) {
					String code = rsResult.getString("code");
					//System.out.println("Code: " + code);
					String description = rsResult.getString("description");
					//System.out.println("description: " + description);
					String parentCode = rsResult.getString("parent_code");
					//System.out.println("parent_code: " + parentCode);
					
					//Category category = new Category(rsResult.getString("code"), rsResult.getString("description"), rsResult.getString("parent_code"));
					Category category = new Category(code, description, parentCode);
					//System.out.println("Category: " + code);
					alResult.add(category);
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
		
		return alResult;
	}

	public static boolean saveImageDetails(ImageFile objFile) {
		boolean savedSuccess = false;
		
		/* Save to database. */
		ResultSet rsResult = null;
		Connection objConn = null;
		Statement objStmt = null;
		String sqlScript = ""; 
		
		
		try {
			
			objConn = DriverManager.getConnection(DBService.baseDBConnUrl, DBService.baseDBUserID, DBService.baseDBPassword);
			
			if (objConn != null) {
				/* Validation. */
				sqlScript = "SELECT * FROM prop_file WHERE file_name = '" + objFile.getFileName() + "';";
				
				objStmt = objConn.createStatement();
				rsResult = objStmt.executeQuery(sqlScript);
				rsResult.last();
			    int count = rsResult.getRow();
			    rsResult.close();
			    objStmt.close();
			    
			    /* Insert to table. */
				if (count > 0) {
				
					// Update prop_file table.
					sqlScript = "UPDATE prop_file SET " + 
								"comm_edited = '" + objFile.getEditedComment() + "' " + 
								"WHERE file_name = '" + objFile.getFileName() + "';";
					
					objStmt = objConn.createStatement();
					count = objStmt.executeUpdate(sqlScript);
					sqlScript = "";
					
					// Delete link_file_category.
					sqlScript = "DELETE FROM link_file_category WHERE file_name = '" + objFile.getFileName() + "';";
					objStmt = objConn.createStatement();
					count = objStmt.executeUpdate(sqlScript);
					sqlScript = "";
					
					// Insert to link_file_category.
					if (objFile.getCategory().size() > 0) {
						String insertScript = "INSERT INTO link_file_category (file_name, category_code, prop_file_file_name, prop_category_code) " + 
											  "VALUES (?, ?, ?, ?);";
						PreparedStatement preparedStatement = objConn.prepareStatement(insertScript);
						objConn.setAutoCommit(false);
						
						ArrayList<Category> alCategory = objFile.getCategory();
						for (int catIndex = 0; catIndex < alCategory.size(); catIndex++) {
							
							preparedStatement.setString(1, objFile.getFileName());
							preparedStatement.setString(2, alCategory.get(catIndex).getCode());
							preparedStatement.setString(3, objFile.getFileName());
							preparedStatement.setString(4, alCategory.get(catIndex).getCode());
							preparedStatement.addBatch();
							
						}
						
						int[] updateCounts = preparedStatement.executeBatch();
			            System.out.println("updateCounts: " + Arrays.toString(updateCounts));
			            objConn.commit();
			            objConn.setAutoCommit(true);

					}
					
					/*
					if (objFile.getCategory().size() > 0) {
						
						ArrayList<Category> alCategory = objFile.getCategory();
						for (int catIndex = 0; catIndex < alCategory.size(); catIndex++) {
							
							sqlScript = sqlScript + "INSERT INTO link_file_category (file_name, category_code, prop_file_file_name, prop_category_code) " + 
													"VALUES ('" + objFile.getFileName() + "', '" + alCategory.get(catIndex).getCode() + "', '" + objFile.getFileName() + "', '" + alCategory.get(catIndex).getCode() + "');";
						}
						System.out.println("sqlScript: " + sqlScript);
						objStmt = objConn.createStatement();
						count = objStmt.executeUpdate(sqlScript);
						sqlScript = "";
					}
					*/
					
					savedSuccess = true;
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
}

