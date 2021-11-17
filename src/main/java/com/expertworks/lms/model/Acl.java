package com.expertworks.lms.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Acl")

public class Acl {

	@DynamoDBHashKey
	private String userRole;
	
	@DynamoDBAttribute
	private String desc;
	
	@DynamoDBAttribute
	private List<MenuItem> pages;

	
	public String getUserRole() {
		return userRole;
	}


	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}


	public List<MenuItem> getPages() {
		return pages;
	}


	public void setPages(List<MenuItem> pages) {
		this.pages = pages;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}






}
