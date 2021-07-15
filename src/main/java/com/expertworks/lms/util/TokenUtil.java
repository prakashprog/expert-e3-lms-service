package com.expertworks.lms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;

@Service
public class TokenUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(TokenUtil.class);
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public String getPartner() {

//		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
//
//		String username = null;
//		String teamId = null;
//		String userId = null;
//		String partnerId=null;
//
//		if (principal instanceof UserDetails) {
//			username = ((UserDetails) principal).getUsername();
//		} else {
//			username = principal.toString();
//		}
//
//		if (credentials instanceof AuthTokenDetails) {
//			teamId = ((AuthTokenDetails) credentials).getTeamId();
//			userId = ((AuthTokenDetails) credentials).getSub();
//			partnerId = ((AuthTokenDetails) credentials).getPartnerId();
//			
//
//		}
		
		String username = null;
		String teamId = null;
		String userId = null;
		String partnerId=null;
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object details = authentication.getDetails();
		if (details instanceof OAuth2AuthenticationDetails) {
			OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) details;
			System.out.println(":::==" + oAuth2AuthenticationDetails.getTokenValue());
			Claims claims = jwtUtil.extractAllClaims(oAuth2AuthenticationDetails.getTokenValue());
			System.out.println("--" + claims.get("teamId"));
			teamId = claims.get("teamId", String.class);
			userId = claims.get("userId", String.class);
			partnerId = claims.get("partnerId", String.class);
		}

		System.out.println("UserName : " + username);
		System.out.println("teamId : " + teamId);
		System.out.println("sub : " + userId);
		System.out.println("partnerId : " + partnerId);
		

		return partnerId;
	}
	
	
	
	public String getUserId()
	{
		String userId= null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object details = authentication.getDetails();
		if (details instanceof OAuth2AuthenticationDetails) {
			OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) details;
			System.out.println(":::==" + oAuth2AuthenticationDetails.getTokenValue());
			Claims claims = jwtUtil.extractAllClaims(oAuth2AuthenticationDetails.getTokenValue());
			userId = claims.get("userId", String.class);
			
		}
		 return userId;
		
	}
	
	

}
