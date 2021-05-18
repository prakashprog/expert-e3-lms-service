package com.expertworks.lms.http;

import java.util.List;


public class CoursesDetailsDTO  {
		
    private float percentage;
	private String type;
	private String level;
	
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

	


	
}
