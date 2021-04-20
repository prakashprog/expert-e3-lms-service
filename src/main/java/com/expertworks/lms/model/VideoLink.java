package com.expertworks.lms.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.expertworks.lms.http.VideoLinkDTO;

@DynamoDBDocument
public class VideoLink {
	
    @DynamoDBAttribute
	private String url;
    
    @DynamoDBAttribute
	private String type;
	
    @DynamoDBAttribute
 	private String title;
    
	@DynamoDBAttribute
	private String subtitle;
    
    @DynamoDBAttribute
   	private String img;
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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

	public VideoLinkDTO toVideoLinkDTO()
	{
		VideoLinkDTO videoLinkDTO = new VideoLinkDTO();
		
		videoLinkDTO.setUrl(this.url);
		videoLinkDTO.setType(this.type);
		videoLinkDTO.setTitle(this.title);
		videoLinkDTO.setSubtitle(this.subtitle);
		videoLinkDTO.setImg(this.img);
		
		return videoLinkDTO;
	}
	
	
}
