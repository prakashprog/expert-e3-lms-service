package com.expertworks.lms.http;

import java.util.List;



public class VideoLinkDTO {
	

	    private String vid ;
	
		private String url;
	      
		private String type;
		   
	 	private String title;
	    
		private String subtitle;
	    	
	   	private String img;
	   	
	   	private String status;
	   	
		private String courseId;
	   	
	    private List<ResourceLinkDTO> resourceLinks;


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


		public List<ResourceLinkDTO> getResourceLinks() {
			return resourceLinks;
		}


		public void setResourceLinks(List<ResourceLinkDTO> resourceLinks) {
			this.resourceLinks = resourceLinks;
		}


		public String getVid() {
			return vid;
		}


		public void setVid(String vid) {
			this.vid = vid;
		}


		public String getStatus() {
			return status;
		}


		public void setStatus(String status) {
			this.status = status;
		}


		public String getCourseId() {
			return courseId;
		}


		public void setCourseId(String courseId) {
			this.courseId = courseId;
		}

}
