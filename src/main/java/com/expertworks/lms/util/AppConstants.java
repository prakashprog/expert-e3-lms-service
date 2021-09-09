package com.expertworks.lms.util;

public interface AppConstants {
	
	String DEFAULT_PAGE_NUMBER = "0";
	String DEFAULT_PAGE_SIZE = "30";

	int MAX_PAGE_SIZE = 50;
	String SUCCESS_SMALL = "success";
	String SUCCESS_RESP_CODE = "00";
	String ERROR_RESP_CODE = "90";
	long ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60;
	String SIGNING_KEY = "devglan123r";
	String TOKEN_PREFIX = "Bearer ";
	String HEADER_STRING = "Authorization";
	String FAILURE_SMALL = "failure";
	String FAILURE_RESP_CODE = "01";
}