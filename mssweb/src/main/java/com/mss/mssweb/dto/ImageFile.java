package com.mss.mssweb.dto;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;

import com.vaadin.flow.server.InputStreamFactory;

import org.tzuchi.syslib.metadata.*;

public class ImageFile implements InputStreamFactory  {
	
	private String fileName = "";
	private String fileTitle = "";
	private String fileSubject = "";
	private String eventName = "";
	private String fileCountry = "";
	private String filePlace = "";
	private String fileExt = "";
	private String filePath = "";
	private String imagePath = "";
	private String defaultWidth = "200px";
	private String defaultHeight = "200px";
	private String remarks = "";
	private String commOriginal = "";
	private String commEdited = "";
	private String keyword = "";
	private Date dateFrom = new Date();
	private Date dateTo = new Date();
	private String photographerCode = "";
	private String photographerName = "";
	private Integer fileSizeType = 0;	// 0=Thumbnail(default); 1=Original Size.
	
	private String missionCode = "";
	private String missionDescription = "";
	
	//private String rootPath = "D:\\Development\\eclipse-workspace\\";
	private String rootPath = "D:\\Development\\eclipse-workspace\\files";
	private String childPath = "files\\P001";
	
	
	private ArrayList<Category> alCategory = new ArrayList<>();
	

	public ImageFile (String path) {
		imagePath = path;
	}
	
	public ImageFile() {
		
	}
	
	public String getWidth() {
		return this.defaultWidth;
	}
	
	public String getHeight() {
		return defaultHeight;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileTitle() {
		return fileTitle;
	}
	
	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}
	
	public String getFileSubject() {
		return fileSubject;
	}
	
	public void setFileSubject(String fileSubject) {
		this.fileSubject = fileSubject;
	}
	
	public String getFileCountry() {
		return fileCountry;
	}
	
	public void setFileCountry(String fileCountry) {
		this.fileCountry = fileCountry;
	}
	
	public String getFilePlace() {
		return filePlace;
	}
	
	public void setFilePlace(String filePlace) {
		this.filePlace = filePlace;
	}
	
	public String getFileExt() {
		return fileExt;
	}
	
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getFileNameWithExt() {
		return this.fileName + "." + this.fileExt; 
	}
	
	public String getEventName() {
		return eventName;
	}
	
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	public String getOriginalComment() {
		return commOriginal;
	}
	
	public void setOriginalComment(String comment) {
		this.commOriginal = comment;
	}
	
	public String getKeyword() {
		return this.keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public String getRemarks() {
		return remarks;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getEditedComment() {
		return commEdited;
	}
	
	public void setEditedComment(String comment) {
		this.commEdited = comment;
	}
	
	public Date getDateTo() {
		return dateTo;
	}
	
	public void setDateTo(Date date) {
		this.dateTo = date;
	}
	
	public Date getDateFrom() {
		return dateFrom;
	}
	
	public void setDateFrom(Date date) {
		this.dateFrom = date;
	}
	
	public String getDateFromToString() {
		String dateFromToString = "";
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
		
		if (this.dateFrom != null) {
			dateFromToString = dateFormat.format(this.dateFrom);
		}
		
		if (this.dateTo != null) {
			dateFromToString = dateFromToString + " - ";
			dateFromToString = dateFromToString + dateFormat.format(this.dateTo);
		}
		
		return dateFromToString;
	}
	
	public String getPhotographerCode() {
		return photographerCode;
	}
	
	public void setPhotographerCode(String code) {
		this.photographerCode = code;
	}
	
	public String getPhotographerName() {
		return photographerName;
	}
	
	public void setPhotographerName(String photographerName) {
		this.photographerName = photographerName;
	}
	
	public ArrayList<Category> getCategory() {
		return this.alCategory;
	}
	
	public void setCategory(ArrayList<Category> alCategory) {
		this.alCategory = alCategory;
	}
	
	public void addCategory(Category category) {
		this.alCategory.add(category);
	}
	
	public void addCategory(String code, String description, String parentCode) {
		Category cat = new Category(code, description, parentCode);
		this.alCategory.add(cat);
	}
	
	public Integer getFileSizeType() {
		return this.fileSizeType;
	}
	
	public void setFileSizeType(Integer fileSizeType) {
		this.fileSizeType = fileSizeType;
	}
	
	public String getMissionCode() {
		return missionCode;
	}
	
	public void setMissionCode(String missionCode) {
		if (missionCode != null) {
			this.missionCode = missionCode.toUpperCase();
			
			if (missionCode.equals("CS")) {
				this.missionDescription = "Charity/慈善";
			}
			else if(missionCode.equals("YL")) {
				this.missionDescription = "Medicine/醫療";
			}
			else if(missionCode.equals("JY")) {
				this.missionDescription = "Education/教育";
			}
			else if(missionCode.equals("RW")) {
				this.missionDescription = "Humanistic Culture/人文";
			}
			else {
				this.missionDescription = "";
			}
		}
		else {
			this.missionDescription = "";
		}
	}
	
	public String getMissionDescription() {
		return this.missionDescription;
	}
	
	public void fetchMetadata() {
		// Fetch the file at full path and assign some values to variables not in database.
		
		try {
			
			String fullPath = rootPath + File.separator + this.filePath + File.separator + this.fileName;
			uo_tjx_metadata_tz meta = new uo_tjx_metadata_tz();
			meta.tzRead(new File(fullPath));

			this.fileTitle = meta.TZ_WIN_TITLE;
			this.fileSubject = meta.TZ_WIN_SUBJECT;
			this.eventName = meta.TZ_EVENT;
			setMissionCode(meta.TZ_MISSION);
			this.filePlace = meta.TZ_PLACE_DESC;
			this.fileCountry = meta.TZ_COUNTRY;
			this.photographerName = meta.TZ_PNAME_1;
			this.photographerCode = meta.TZ_PCODE;
			System.out.println("photographerCode");
			this.dateFrom = meta.TZ_DATE_FM;
			System.out.println("this.dateFrom");
			this.dateTo = meta.TZ_DATE_TO;
			System.out.println("this.dateTo");
			this.keyword = meta.TZ_KEYWORDS;
			this.remarks = meta.TZ_REMARKS;
			
		} catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "|Trace: " + e.getStackTrace());
        }
	}

	@Override
	public InputStream createInputStream() {
		// TODO Auto-generated method stub
		//return null;
		
		try {
			//String dirName = "D:\\Development\\eclipse-workspace\\Hybrid-Menu-vaadin10\\Hybrid-Menu-vaadin10\\HM-Demo\\src\\main\\webapp\\theme\\";
			//ByteArrayOutputStream baos = new ByteArrayOutputStream(5000);
			//BufferedImage img = ImageIO.read(new File(dirName,"ajax-loader.gif"));
			//String fullPath = rootPath + childPath + "\\" + this.fileName;
			String fullPath = rootPath + File.separator + this.filePath + File.separator + this.fileName;
			uo_tjx_metadata_tz meta = new uo_tjx_metadata_tz();
			meta.tzReadAll(new File(fullPath));
			
			if (fileSizeType == 0 && meta.TZ_IMG_THUMBNAIL.length > 0) {
				
				// Get thumbnail only.
				return new ByteArrayInputStream(meta.TZ_IMG_THUMBNAIL);
			}
			else {
				/*
				// cannot work cos the image is smaller and metadata is missing.
				ByteArrayOutputStream baos = new ByteArrayOutputStream(10000);
				BufferedImage img = ImageIO.read(new File(fullPath));
				ImageIO.write(img, this.fileExt, baos);
				baos.flush();
				
				return new ByteArrayInputStream(baos.toByteArray());
				*/
				
				// Get the full size of image.
				File initialFile = new File(fullPath);
			    InputStream targetStream = new FileInputStream(initialFile);
			    return targetStream;
			}
			
			
			
		} catch (Exception e) {
            return null;
        }
	}
	
	/*
	@Override
	public InputStream createInputStream() {
		// TODO Auto-generated method stub
		//return null;
		
		try {
			//String dirName = "D:\\Development\\eclipse-workspace\\Hybrid-Menu-vaadin10\\Hybrid-Menu-vaadin10\\HM-Demo\\src\\main\\webapp\\theme\\";
			ByteArrayOutputStream baos = new ByteArrayOutputStream(5000);
			//BufferedImage img = ImageIO.read(new File(dirName,"ajax-loader.gif"));
			//String fullPath = rootPath + childPath + "\\" + this.fileName;
			String fullPath = rootPath + File.separator + this.filePath + File.separator + this.fileName;
			BufferedImage img = ImageIO.read(new File(fullPath));
			ImageIO.write(img, this.fileExt, baos);
			baos.flush();
			
			return new ByteArrayInputStream(baos.toByteArray());
			
		} catch (IOException e) {
            return null;
        }
	}
	*/
}
