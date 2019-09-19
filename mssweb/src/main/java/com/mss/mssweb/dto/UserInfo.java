package com.mss.mssweb.dto;

public class UserInfo {
	
	private String userID;
	private String fullName;
	private int userType;
	private boolean isActive;
	
	public String getUserID() {
		return userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public int getUserType() {
		return userType;
	}
	
	public void setUserType(int userType) {
		this.userType = userType;
	}
	
	public boolean getIsActive() {
		return isActive;
	}
	
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public UserInfo() {
		userID = "";
		fullName = "";
		userType = 0;
		isActive = false;
	}
}
