package com.mss.mssweb.dto;

public class Category {
	
	private String code = "";
	private String description = "";
	private String parentCode = "";
	private Category parentCategory = null; //new Category();
	
	public Category() {
		this.code = "";
		this.description = "";
		this.parentCode = "";
	}
	
	public Category(String code, String description, String parentCode) {
		this.code = code;
		this.description = description;
		this.parentCode = parentCode;
	}

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getParentCode() {
		return parentCode;
	}
	
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	
	public Category getParentCategory() {
		return parentCategory;
	}
	
	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}
}
