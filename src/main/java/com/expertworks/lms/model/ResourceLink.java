package com.expertworks.lms.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.expertworks.lms.http.ResourceLinkDTO;
import com.expertworks.lms.http.VideoLinkDTO;

@DynamoDBDocument
public class ResourceLink {
	
    @DynamoDBAttribute
	private String link;
    
    @DynamoDBAttribute
	private String type;
	
    @DynamoDBAttribute
 	private String title;
	
	
	public ResourceLinkDTO toResourceLinkDTO()
	{
		ResourceLinkDTO resourceLinkDTO = new ResourceLinkDTO();
		resourceLinkDTO.setLink(this.link);
		resourceLinkDTO.setType(this.type);
		resourceLinkDTO.setTitle(this.title);
		return resourceLinkDTO;
	}



	public String getLink() {
		return link;
	}



	public void setLink(String link) {
		this.link = link;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
	
	
}
