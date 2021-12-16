package com.expertworks.lms.http;

import java.util.Date;

import com.expertworks.lms.enums.ExpertRole;
import com.fasterxml.jackson.annotation.JsonIgnore;



public class UserDTO implements Comparable<UserDTO> {

	private String userId;
	@JsonIgnore
	private String sk;

	private Date createdDate;
	private String name;

	private String img;

	private String teamId;
	private String groupId;
	private String partnerId;

	private String email;
	private ExpertRole userRole;



	@Override
	public int compareTo(UserDTO partner) {
		return sk.compareTo(partner.sk);

	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
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


	public String getTeamId() {
		return teamId;
	}


	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}


	public String getGroupId() {
		return groupId;
	}


	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}


	public String getPartnerId() {
		return partnerId;
	}


	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public ExpertRole getUserRole() {
		return userRole;
	}


	public void setUserRole(ExpertRole userRole) {
		this.userRole = userRole;
	}








}
