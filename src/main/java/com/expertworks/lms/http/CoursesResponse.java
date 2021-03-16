package com.expertworks.lms.http;

import java.util.List;

import com.expertworks.lms.model.VideoLink;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


public class CoursesResponse extends BaseResponse {
   
	
	private static final long serialVersionUID = -8091879091924046844L;
   


	private String teamId;

	private String courseId;

	private String createDate;
	
	private String title;

	private List<VideoLink> videoLinks;
	
	private int totalvidoes;
	
	private String s3folder;
	
	private String preferences;

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
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	
	
	
}
