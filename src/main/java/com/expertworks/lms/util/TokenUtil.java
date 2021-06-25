package com.expertworks.lms.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenUtil {
	
	
	public String getPartner() {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();

		String username = null;
		String teamId = null;
		String userId = null;
		String partnerId=null;

		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		if (credentials instanceof AuthTokenDetails) {
			teamId = ((AuthTokenDetails) credentials).getTeamId();
			userId = ((AuthTokenDetails) credentials).getSub();
			partnerId = ((AuthTokenDetails) credentials).getPartnerId();
			

		}

		System.out.println("UserName : " + username);
		System.out.println("teamId : " + teamId);
		System.out.println("sub : " + userId);
		System.out.println("partnerId : " + partnerId);
		

		return partnerId;
	}

}
