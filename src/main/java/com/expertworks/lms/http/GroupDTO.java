package com.expertworks.lms.http;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.expertworks.lms.model.Team;



public class GroupDTO implements Comparable<GroupDTO> {
	
	private String groupId;
	private String sk;
	private Date createdDate;
	private String name;
	private String teamId;
	private String img;
	private String userLimit;

	private String region;
	private List<Team> teams = new ArrayList();

	public int compareTo(GroupDTO partner) {
		return sk.compareTo(partner.sk);

	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
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

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}



	public List<Team> getTeams() {
		return teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getUserLimit() {
		return userLimit;
	}

	public void setUserLimit(String userLimit) {
		this.userLimit = userLimit;
	}

	
	

}
