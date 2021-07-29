package com.expertworks.lms.model;

import java.util.Date;



import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerateStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedTimestamp;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.expertworks.lms.http.UserDTO;

@DynamoDBTable(tableName = "User")

public class User implements Comparable<User> {
	


	@DynamoDBHashKey
	//@DynamoDBAutoGeneratedKey
	private String userId;


	@DynamoDBAutoGeneratedTimestamp(strategy=DynamoDBAutoGenerateStrategy.CREATE)
	private Date createdDate;

	@DynamoDBAttribute
	private String name;

	@DynamoDBAttribute
	private String userName;
	
	@DynamoDBAttribute
	private String img;
	
	@DynamoDBAttribute
	private String teamId;
	
	@DynamoDBAttribute
	private String groupId;
	
	@DynamoDBAttribute
	private String partnerId;
	
	@DynamoDBAttribute
	private String password;
	
	@DynamoDBAttribute
	private String email;
	
	@DynamoDBAttribute
	private String userRole;
	
	@DynamoDBAttribute
	private String oldpassword;
	

	public int compareTo(User user) {
		return userId.compareTo(user.userId);
	}
	
	public UserDTO toUserDTO() {
		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(this.userId);
		userDTO.setName(this.name);	
		userDTO.setPartnerId(this.partnerId);	
		userDTO.setGroupId(this.groupId);
		userDTO.setTeamId(this.teamId);	
		userDTO.setEmail(this.email);	
		userDTO.setImg(this.img);	
		userDTO.setUserRole(this.userRole);	
		userDTO.setCreatedDate(this.createdDate);	
		
		return userDTO;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getOldpassword() {
		return oldpassword;
	}

	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}



}
