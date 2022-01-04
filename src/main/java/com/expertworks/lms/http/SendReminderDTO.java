package com.expertworks.lms.http;

public class SendReminderDTO {

	private String userEmail;

	private String fromEmail;

	private CourseStatus status;

	private String courseName;

	private String courseDetail;

	private String percentageCompleted;

	private String adminEmail;

	private String adminUserId;

	private String userName;

	public static enum CourseStatus {
		COMPLETED("COMPLETED"), IN_PROGRESS("IN_PROGRESS"), NOT_STARTED("NOT_STARTED");

		private String status;

		CourseStatus(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public CourseStatus getStatus() {
		return status;
	}

	public void setStatus(CourseStatus status) {
		this.status = status;
	}

}
