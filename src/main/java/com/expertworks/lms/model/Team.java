package com.expertworks.lms.model;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerateStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedTimestamp;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.expertworks.lms.http.TeamDTO;

@DynamoDBTable(tableName = "Team")

public class Team implements Comparable<Team> {
	


	@DynamoDBHashKey
	@DynamoDBAutoGeneratedKey
	private String teamId;

	@DynamoDBRangeKey // CustomerBookmark Table referred.
	private String sk;

	@DynamoDBAutoGeneratedTimestamp(strategy=DynamoDBAutoGenerateStrategy.CREATE)
	private Date createdDate;
	
	
	@DynamoDBAttribute
	private String email;

	@DynamoDBAttribute
	private String name;

	@DynamoDBAttribute
	private String userId;

	@DynamoDBAttribute
	private String img;
	
	@DynamoDBAttribute
	private String description;
	
	@DynamoDBAttribute
	private String userLimit;
	
	@DynamoDBAttribute
	private String groupId;
	
	@DynamoDBAttribute
	private String teamType;
	
	
	public Team(String groupId,String name,String sk)
	{
		this.name = name;
		this.groupId = groupId;
		this.sk = sk;
	}
	
	public Team()
	{
		
	}

	public int compareTo(Team partner) {
		return sk.compareTo(partner.sk);
	}
	
	public TeamDTO toGroupDTO() {
		TeamDTO teamDTO = new TeamDTO();
		teamDTO.setTeamId(this.teamId);
		teamDTO.setSk(this.sk);
		teamDTO.setName(this.name);
		teamDTO.setUserLimit(this.userLimit);
		teamDTO.setDescription(this.description);
		
		return teamDTO;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
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

	public String getTeamType() {
		return teamType;
	}

	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}



	




}
