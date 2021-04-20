package com.expertworks.lms.model;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.expertworks.lms.http.CoursesDTO;
import com.expertworks.lms.http.VideoLinkDTO;

@DynamoDBTable(tableName = "CoursesMaster1")

public class Courses {

	@DynamoDBHashKey
	private String courseId;

	@DynamoDBRangeKey // CustomerBookmark Table referred.
	private String sk;


	@DynamoDBAttribute
	private String createDate;

	@DynamoDBAttribute
	private String title;

	@DynamoDBAttribute
	private String subtitle;

	@DynamoDBAttribute
	private String img;

	// @JsonIgnore
	@DynamoDBAttribute
	private List<VideoLink> videoLinks;

	@DynamoDBAttribute
	private int totalvidoes;

	@DynamoDBAttribute
	private String s3folder;

	@DynamoDBAttribute
	private String preferences;

	
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

	public CoursesDTO toCourseDTO() {

		CoursesDTO coursesDTO = new CoursesDTO();
		coursesDTO.setCourseId(this.courseId);
		coursesDTO.setCreateDate(this.createDate);
		coursesDTO.setPreferences(this.preferences);
		coursesDTO.setS3folder(this.s3folder);
		coursesDTO.setTitle(this.title);
		List<VideoLinkDTO> videoLinkDTOList = new ArrayList<VideoLinkDTO>();

		for (VideoLink videoLink : getVideoLinks()) {
			
			videoLinkDTOList.add(videoLink.toVideoLinkDTO());
		}
		coursesDTO.setVideoLinkDTO(videoLinkDTOList);
		return coursesDTO;

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

	/**
	 * @return the sk
	 */
	public String getSk() {
		return sk;
	}

	/**
	 * @param sk the sk to set
	 */
	public void setSk(String sk) {
		this.sk = sk;
	}

	
}
