package com.mss.mssweb.dto;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.imageio.ImageIO;

import com.vaadin.flow.server.InputStreamFactory;

public class ImageFile implements InputStreamFactory  {
	
	private String fileName = "";
	private String eventName = "";
	private String fileExt = "";
	private String filePath = "";
	private String imagePath = "";
	private String defaultWidth = "200px";
	private String defaultHeight = "200px";
	private String commOriginal = "";
	private String commEdited = "";
	private Date dateFrom = new Date();
	private Date dateTo = new Date();
	private String photographerCode = "";
	
	private String rootPath = "D:\\Development\\eclipse-workspace\\";
	private String childPath = "files\\P001";
	

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
	
	public String getPhotographerCode() {
		return photographerCode;
	}
	
	public void setPhotographerCode(String code) {
		this.photographerCode = code;
	}

	@Override
	public InputStream createInputStream() {
		// TODO Auto-generated method stub
		//return null;
		
		try {
			//String dirName = "D:\\Development\\eclipse-workspace\\Hybrid-Menu-vaadin10\\Hybrid-Menu-vaadin10\\HM-Demo\\src\\main\\webapp\\theme\\";
			ByteArrayOutputStream baos = new ByteArrayOutputStream(5000);
			//BufferedImage img = ImageIO.read(new File(dirName,"ajax-loader.gif"));
			String fullPath = rootPath + childPath + "\\" + this.fileName;
			BufferedImage img = ImageIO.read(new File(fullPath));
			ImageIO.write(img, this.fileExt, baos);
			baos.flush();
			
			return new ByteArrayInputStream(baos.toByteArray());
			
		} catch (IOException e) {
            return null;
        }
	}
	
}
