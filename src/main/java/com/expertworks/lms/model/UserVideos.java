package com.expertworks.lms.model;

import java.util.List;

public class UserVideos {
	
	
	private int status;
	
	private String message;
	
	private String user;
		
	private List <VideoLink>videos;


	


	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}


	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}


	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}


	/**
	 * @return the videos
	 */
	public List<VideoLink> getVideos() {
		return videos;
	}


	/**
	 * @param videos the videos to set
	 */
	public void setVideos(List<VideoLink> videos) {
		this.videos = videos;
	}


	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

}
