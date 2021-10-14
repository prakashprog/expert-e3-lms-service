package com.expertworks.lms.model;

import java.util.Date;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerateStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedTimestamp;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.expertworks.lms.http.PartnerDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

@DynamoDBTable(tableName = "Packages")

public class Pack implements Comparable<Pack> {
	
	@DynamoDBHashKey
	@DynamoDBAutoGeneratedKey
	private String id;

	@DynamoDBAttribute
	private String name;
	
	@DynamoDBAttribute
	private String subscription;
	
	@DynamoDBAttribute
	private String price;
	
	@DynamoDBAttribute
	private List<String> features;
	
	@DynamoDBAutoGeneratedTimestamp(strategy=DynamoDBAutoGenerateStrategy.CREATE)
	private Date createdDate;

	
	public int compareTo(Pack partner) {
		return price.compareTo(partner.price);
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPrice() {
		return price;
	}


	public void setPrice(String price) {
		this.price = price;
	}


	public List<String> getFeatures() {
		return features;
	}


	public void setFeatures(List<String> features) {
		this.features = features;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public String getSubscription() {
		return subscription;
	}


	public void setSubscription(String subscription) {
		this.subscription = subscription;
	}



}
