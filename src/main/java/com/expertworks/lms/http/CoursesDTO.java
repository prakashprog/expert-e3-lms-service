package com.expertworks.lms.http;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class CoursesDTO  implements Comparable<CoursesDTO> {
   
	
	private static final long serialVersionUID = -8091879091924046844L;
   
	
	private String courseId;
	private String sk;
	private String createDate;
	private String title;
	private String subtitle;
	private String img;
    private List<VideoLinkDTO> videoLinks;
    private float  percentage;
	private int totalvidoes;
	private String s3folder;
	@JsonIgnore
	private String type;
	@JsonIgnore
	private String level;

	 public int compareTo(CoursesDTO coursesDTO) {  
		    return Integer.parseInt(sk.substring(2))- Integer.parseInt(coursesDTO.sk.substring(2));  
		    
		 
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
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	/**
	 * @return the videoLinks
	 */
	public List<VideoLinkDTO> getVideoLinks() {
		return videoLinks;
	}


	/**
	 * @param videoLinks the videoLinks to set
	 */
	public void setVideoLinks(List<VideoLinkDTO> videoLinks) {
		this.videoLinks = videoLinks;
	}


	public float getPercentage() {
		return percentage;
	}


	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getLevel() {
		return level;
	}


	public void setLevel(String level) {
		this.level = level;
	}


	


	
}
