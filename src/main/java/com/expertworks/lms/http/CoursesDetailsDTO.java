package com.expertworks.lms.http;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.StandardToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.expertworks.lms.model.Currency;


public class CoursesDetailsDTO  {
		
	private String courseId;
	private float percentage;
	private String type;
	private String level;
	private String order;
	private String img;
	private String title;
	private String subtitle;
	private String description;
	private String leveldesc;

	private String hours;
	private String rating;
	private String reviews;
	
	private List<String> includes;
	private List<CurrencyDTO> currencies;
	
	private String actualPrice;
	private String price;
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLeveldesc() {
		return leveldesc;
	}

	public void setLeveldesc(String leveldesc) {
		this.leveldesc = leveldesc;
	}

	public List<String> getIncludes() {
		return includes;
	}

	public void setIncludes(List<String> includes) {
		this.includes = includes;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	 @Override
		public String toString() {
		     StandardToStringStyle style = new StandardToStringStyle();
		     style.setUseClassName(false);
		     style.setUseIdentityHashCode(false);
		    ToStringBuilder builder = new ReflectionToStringBuilder(this, style);
		    return builder.toString();
		}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getReviews() {
		return reviews;
	}

	public void setReviews(String reviews) {
		this.reviews = reviews;
	}

	public String getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(String actualPrice) {
		this.actualPrice = actualPrice;
	}



	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public List<CurrencyDTO> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(List<CurrencyDTO> currencies) {
		this.currencies = currencies;
	}





	
}
