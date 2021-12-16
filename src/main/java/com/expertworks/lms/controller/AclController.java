package com.expertworks.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.expertworks.lms.enums.ExpertRole;
import com.expertworks.lms.enums.PackageType;
import com.expertworks.lms.enums.SubscriptionStatus;
import com.expertworks.lms.http.ApiResponse;
import com.expertworks.lms.model.Acl;
import com.expertworks.lms.model.Subscriptions;
import com.expertworks.lms.repository.AclRepository;
import com.expertworks.lms.service.PackageService;
import com.expertworks.lms.service.SubscriptionsService;

@RestController
@Component

public class AclController {

	public static final String SUCCESS = "success";



	@Autowired
	private PackageService packageService;

	@Autowired
	private AclRepository aclRepository;

	@Autowired
	private SubscriptionsService subscriptionsService;

	@CrossOrigin
	@GetMapping("/user/{role}/pages")

	public ApiResponse pagesAcessable(@PathVariable("role") String role,
			@RequestParam(required = false, name = "groupId") String groupId) {

		Acl acl = aclRepository.load(role);
		Subscriptions subscriptions = subscriptionsService.get("B2B_"+groupId);
		System.out.println("subscriptions : "  +subscriptions);
		if(subscriptions!=null && subscriptions.getSubscriptionStatus()==SubscriptionStatus.SUBSCRIBED)
		{
			System.out.println("subscriptions Status : "+subscriptions.getSubscriptionStatus());
			System.out.println("subscriptions Package : "+subscriptions.getSubscriptionPackage());
			//Pack pack = packageService.getPack(subscriptions.getCourseOrPackageId());
			if(subscriptions.getSubscriptionPackage()==PackageType.PREMIUM)
				acl = aclRepository.load(ExpertRole.ROLE_PREUSER.toString());
		}
		if (acl != null)
			return new ApiResponse(HttpStatus.OK, SUCCESS, acl);
		else
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "role not present/empty");

	}


//	if(pack.getSubscriptionPackage()==PackageType.PREMIUM)
//	{
//		System.out.println("in ------------------");
//		acl = aclRepository.load(ExpertRole.ROLE_PREUSER.toString());
//	}



}
