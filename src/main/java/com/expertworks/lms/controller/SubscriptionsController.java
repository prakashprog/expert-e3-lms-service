package com.expertworks.lms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.expertworks.lms.http.ApiResponse;
import com.expertworks.lms.model.Subscriptions;
import com.expertworks.lms.service.SubscriptionsService;

@RestController
@Component

public class SubscriptionsController {

	private final static Logger logger = LoggerFactory.getLogger(SubscriptionsController.class);

	public static final String SUCCESS = "success";

	@Autowired
	private SubscriptionsService subscriptionsService;

	@CrossOrigin
	@PostMapping("/subscriptions/update")
	public ApiResponse update(@RequestBody Subscriptions subscriptions) {

		// Hash Key and Range key
		subscriptions.setSubscriptionIdentifier(subscriptions.getCustomerType() + "_" + subscriptions.getGroupId());

		List<Subscriptions> dbsubscriptions = subscriptionsService
				.queryAll(subscriptions.getCustomerType() + "_" + subscriptions.getGroupId());
		if (dbsubscriptions== null)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No rows in DB");

		for(Subscriptions dbsubscription : dbsubscriptions)
		{
			System.out.println("dbsubscriptions status before updating : " + dbsubscription.getSubscriptionStatus()+";GroupId : "+dbsubscription.getGroupId());

			dbsubscription.setSubscriptionStatus(subscriptions.getSubscriptionStatus());
		    subscriptionsService.update(dbsubscription);
		}
		return new ApiResponse(HttpStatus.OK, SUCCESS, "success");

	}
}
