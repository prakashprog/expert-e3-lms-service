package com.expertworks.lms.http;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.expertworks.lms.model.Team;



public class TransferDTO 
{
	
	private String toTeam;
	private String fromTeam;
	
	
	public String getToTeam() {
		return toTeam;
	}
	public void setToTeam(String toTeam) {
		this.toTeam = toTeam;
	}
	public String getFromTeam() {
		return fromTeam;
	}
	public void setFromTeam(String fromTeam) {
		this.fromTeam = fromTeam;
	}

	

}
