package com.expertworks.lms.http;

import java.util.List;


public class CoursesDetailsDTO  {
		
	private String courseId;
	private float percentage;
	private String type;
	private String level;
	private String img;
	private String title;
	private String subtitle;
	
	List<CoursesDTO> sections;

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

	public List<CoursesDTO> getSections() {
		return sections;
	}

	public void setSections(List<CoursesDTO> sections) {
		this.sections = sections;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	


	
}
