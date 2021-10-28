package com.expertworks.lms.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.StandardToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.math.NumberUtils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.expertworks.lms.http.CoursesDTO;
import com.expertworks.lms.http.CurrencyDTO;
import com.expertworks.lms.http.VideoLinkDTO;
import com.expertworks.lms.util.CurrencyUtil;

@DynamoDBTable(tableName = "CoursesMaster")

public class Courses implements Comparable<Courses> {

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
	
	@DynamoDBAttribute
	private String description;
	
	@DynamoDBAttribute
	private String leveldesc;
	
	@DynamoDBAttribute
	private List<String> includes;

	// @JsonIgnore
	@DynamoDBAttribute
	private List<VideoLink> videoLinks;

	@DynamoDBAttribute
	private int totalvidoes;

	@DynamoDBAttribute
	private String s3folder;

	@DynamoDBAttribute
	private String type;

	@DynamoDBAttribute
	private String level;
	
	@DynamoDBAttribute
	private String order;

	@DynamoDBAttribute
	private String price;
	
	
	private List<CurrencyDTO> currencies;
	
	@DynamoDBAttribute
	private String hours;

	@DynamoDBAttribute
	private String rating;
	
	@DynamoDBAttribute
	private String reviews;
	
	@DynamoDBAttribute
	private String actualPrice;
	
	
		
	
	public int compareTo(Courses courses) {
		return sk.compareTo(courses.sk);

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
		coursesDTO.setSk(this.sk);
		coursesDTO.setType(this.type);
		coursesDTO.setLevel(this.level);
		coursesDTO.setS3folder(this.s3folder);
		coursesDTO.setTotalvidoes(this.totalvidoes);
		coursesDTO.setTitle(this.title);
		coursesDTO.setSubtitle(this.subtitle);
		coursesDTO.setImg(this.img);
		coursesDTO.setCreateDate(this.createDate);
		coursesDTO.setPrice(this.price);
		coursesDTO.setActualPrice(this.actualPrice);
		coursesDTO.setHours(this.hours);
		coursesDTO.setReviews(this.reviews);
		coursesDTO.setRating(this.rating);
		
		if(NumberUtils.isCreatable(coursesDTO.getPrice()) && NumberUtils.isCreatable(coursesDTO.getActualPrice()))
			coursesDTO.setCurrencies(
					CurrencyUtil.getCurrencies(Double.parseDouble(coursesDTO.getPrice()),Double.parseDouble(coursesDTO.getActualPrice())));


		List<VideoLinkDTO> videoLinkDTOList = new ArrayList<VideoLinkDTO>();
		if (this.getVideoLinks() != null)
			for (VideoLink videoLink : this.getVideoLinks()) {
				videoLinkDTOList.add(videoLink.toVideoLinkDTO());
			}
		coursesDTO.setVideoLinks(videoLinkDTOList);
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getIncludes() {
		return includes;
	}

	public void setIncludes(List<String> includes) {
		this.includes = includes;
	}

	public String getLeveldesc() {
		return leveldesc;
	}

	public void setLeveldesc(String leveldesc) {
		this.leveldesc = leveldesc;
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
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

	public List<CurrencyDTO> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(List<CurrencyDTO> currencies) {
		this.currencies = currencies;
	}



}
