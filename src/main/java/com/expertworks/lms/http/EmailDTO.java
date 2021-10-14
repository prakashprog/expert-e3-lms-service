package com.expertworks.lms.http;

import java.util.Map;

public class EmailDTO {
	
	 String to;
	 String from;
	 String subject;
	 String content;
	 String password;
	 String loginId;
	 String username;
       
	 
	public EmailDTO(String to,String username,String loginId,String password)
	 {
		 this.to = to;
		 this.username = username;
		 this.loginId = loginId;
		 this.password = password;
		
	 }
   
	public EmailDTO()
	 {
		
	 }
    
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}
