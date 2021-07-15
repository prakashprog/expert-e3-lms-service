package com.expertworks.lms.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.CommonsRequestLoggingFilter;

public class CustomizedRequestLoggingFilter extends CommonsRequestLoggingFilter {
	
	

	@Override
	protected void beforeRequest(HttpServletRequest httpServletRequest, String message) {

		super.beforeRequest(httpServletRequest, message);
		System.out.println("beforeRequest");

	}

	@Override
	protected void afterRequest(HttpServletRequest httpServletRequest, String message) {
		System.out.println("afterRequest");
	}
}
