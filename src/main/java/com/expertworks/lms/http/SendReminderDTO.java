package com.expertworks.lms.http;

public class SendReminderDTO {
	
	private String userEmail;
	
	private String fromEmail;
	
	private Status status; 
	
	private String courseName;
	
	private String courseDetail;
	
	private String percentageCompleted;
	
	private String adminEmail;
	
	private String adminUserId;
	
	
	public static enum Status {
		COMPLETED, IN_PROGRESS, NOT_STARTED;
	}


	public String getUserEmail() {
		return userEmail;
	}


	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}


	public String getFromEmail() {
		return fromEmail;
	}


	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public String getCourseName() {
		return courseName;
	}


	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}


	public String getCourseDetail() {
		return courseDetail;
	}


	public void setCourseDetail(String courseDetail) {
		this.courseDetail = courseDetail;
	}


	public String getPercentageCompleted() {
		return percentageCompleted;
	}


	public void setPercentageCompleted(String percentageCompleted) {
		this.percentageCompleted = percentageCompleted;
	}


	public String getAdminEmail() {
		return adminEmail;
	}


	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}


	public String getAdminUserId() {
		return adminUserId;
	}


	public void setAdminUserId(String adminUserId) {
		this.adminUserId = adminUserId;
	}
	
	
}
