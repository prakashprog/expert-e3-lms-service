package com.expertworks.lms.http;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.expertworks.lms.model.Group;
import com.fasterxml.jackson.annotation.JsonIgnore;



public class PartnerDTO implements Comparable<PartnerDTO> {


	private String partnerId;
	@JsonIgnore
	private String sk;
    private Date createdDate;
 	private String name;
 	private String img;

	private String mobile;
	private String address;
	
	private List<Group> groups = new ArrayList();
	
 	
 	
 	public int compareTo(PartnerDTO partnerDTO) {
		return sk.compareTo(partnerDTO.sk);

	}

	
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getSk() {
		return sk;
	}
	public void setSk(String sk) {
		this.sk = sk;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}


	public List<Group> getGroups() {
		return groups;
	}


	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}



	



	
	

}
