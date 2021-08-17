package com.expertworks.lms.http;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.expertworks.lms.model.User;



public class TeamDTO implements Comparable<TeamDTO> {
	
	private String teamId;
	private String sk;
	private Date createdDate;
	private String name;
	private String userLimit;
	private String description;

	private String img;
	private List<User> users = new ArrayList();
	
	
 	public int compareTo(TeamDTO teamDTO) {
		return sk.compareTo(teamDTO.sk);

	}

	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
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
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getUserLimit() {
		return userLimit;
	}

	public void setUserLimit(String userLimit) {
		this.userLimit = userLimit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	
	
	

}
