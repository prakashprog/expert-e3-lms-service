package com.expertworks.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expertworks.lms.model.Subscriptions;
import com.expertworks.lms.repository.SubscriptionsRepository;

@Service
public class SubscriptionsService {

	@Autowired
	private SubscriptionsRepository subscriptionsRepository;


	public Subscriptions load(String subscriptionIdentifier, String courseOrPackageId) {
		return subscriptionsRepository.load(subscriptionIdentifier, courseOrPackageId);
	}


	public Subscriptions get(String subscriptionIdentifier) {
		System.out.println("subscriptionIdentifier: " +  subscriptionIdentifier);
		List<Subscriptions> subscriptions = null;
		subscriptions =  subscriptionsRepository.get(subscriptionIdentifier);
		System.out.println("Fetched Subsricption subscriptions : "+ subscriptions);

		if(subscriptions!=null && !subscriptions.isEmpty()) {
			System.out.println("Fetched Subsricption subscriptions size: "+ subscriptions.size());
		   return subscriptions.get(0);
		}
		return null;
	}





}
