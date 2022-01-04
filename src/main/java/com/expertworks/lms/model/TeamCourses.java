package com.expertworks.lms.model;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnore;

@DynamoDBTable(tableName = "TeamCourses")

public class TeamCourses {

	@DynamoDBHashKey
	private String teamId;

	@DynamoDBRangeKey
	@DynamoDBIndexHashKey(globalSecondaryIndexName = "courseId-index")
	private String courseId;

	@DynamoDBAttribute
	private String createDate;

	@DynamoDBAttribute
	private String title;

	@DynamoDBAttribute
	private String subtitle;

	@DynamoDBAttribute
	private String img;

	@JsonIgnore
	@DynamoDBAttribute
	private List<VideoLink> videoLinks;

	@DynamoDBAttribute
	private int totalvidoes;

	@DynamoDBAttribute
	private String s3folder;

	@DynamoDBAttribute
	private String preferences;

	@DynamoDBAttribute
	private String status;

	@DynamoDBAttribute
	private String groupId;

	@DynamoDBAttribute
	private String partnerId;

	public TeamCourses() {
	}


	public TeamCourses(String teamId,String courseId) {

		this.teamId = teamId;
		this.courseId=courseId;
	}

	public TeamCourses(String teamId,String courseId,String groupId,String partnerId) {

		this.teamId = teamId;
		this.courseId=courseId;
		this.groupId = groupId;
		this.partnerId= partnerId;
	}
	/**
	 * @return the teamId
	 */

	public String getTeamId() {
		return teamId;
	}

	/**
	 * @param teamId the teamId to set
	 */

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	/**
	 * @return the courseId
	 */
	public String getCourseId() {
		return courseId;
	}

	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the s3folder
	 */
	public String getS3folder() {
		return s3folder;
	}

	/**
	 * @param s3folder the s3folder to set
	 */
	public void setS3folder(String s3folder) {
		this.s3folder = s3folder;
	}

	/**
	 * @return the preferences
	 */
	public String getPreferences() {
		return preferences;
	}

	/**
	 * @param preferences the preferences to set
	 */
	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}

	/**
	 * @return the totalvidoes
	 */
	public int getTotalvidoes() {
		return totalvidoes;
	}

	/**
	 * @param totalvidoes the totalvidoes to set
	 */
	public void setTotalvidoes(int totalvidoes) {
		this.totalvidoes = totalvidoes;
	}

	/**
	 * @return the videoLinks
	 */
	public List<VideoLink> getVideoLinks() {
		return videoLinks;
	}

	/**
	 * @param videoLinks the videoLinks to set
	 */
	public void setVideoLinks(List<VideoLink> videoLinks) {
		this.videoLinks = videoLinks;
	}

	/**
	 * @return the subtitle
	 */
	public String getSubtitle() {
		return subtitle;
	}

	/**
	 * @param subtitle the subtitle to set
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	/**
	 * @return the img
	 */
	public String getImg() {
		return img;
	}

	/**
	 * @param img the img to set
	 */
	public void setImg(String img) {
		this.img = img;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
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

}
