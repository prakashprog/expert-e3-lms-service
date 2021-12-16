package com.expertworks.lms.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.expertworks.lms.enums.SubscriptionStatus;
import com.expertworks.lms.model.Subscriptions;

@Repository
public class SubscriptionsRepository {

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	public Subscriptions load(String subscriptionIdentifier, String courseOrPackageId) {
		return dynamoDBMapper.load(Subscriptions.class, subscriptionIdentifier, courseOrPackageId);
	}

	public List<Subscriptions> get(String subscriptionIdentifier) {

		Subscriptions subscriptions = new Subscriptions();
		subscriptions.setSubscriptionIdentifier(subscriptionIdentifier);

		DynamoDBQueryExpression<Subscriptions> dynamoDBQueryExpression = new DynamoDBQueryExpression().withHashKeyValues(subscriptions);
		PaginatedQueryList<Subscriptions> paginatedQueryList = dynamoDBMapper.query(Subscriptions.class, dynamoDBQueryExpression);
		paginatedQueryList.loadAllResults();

		List<Subscriptions> list = new ArrayList<Subscriptions>(paginatedQueryList.size());

		Iterator<Subscriptions> iterator = paginatedQueryList.iterator();
		while (iterator.hasNext()) {
			Subscriptions element = iterator.next();
			list.add(element);
		}

		return list;

	}


	public static void main(String[] args) {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
						"http://dynamodb.ap-south-1.amazonaws.com/", "ap-south-1"))
				.withCredentials(new AWSStaticCredentialsProvider(
						new BasicAWSCredentials("AKIAZTGOLXTICK323HL7", "06BBllO6zZvjWklG2o4qQIZHQAEjYXugPim6nNTg")))
				.build();
		DynamoDB dynamoDB = new DynamoDB(client);
		Table table = dynamoDB.getTable("Subscriptions");
		//Item item = table.getItem("subscriptionIdentifier", "B2C_test02B2CCustomer");
		ScanSpec ScanSpec = new ScanSpec();
		ItemCollection<ScanOutcome> items = table.scan(ScanSpec);

		for (Item item : items) {
			//System.out.println(item.toJSONPretty());

		}

		Subscriptions subscriptions= new DynamoDBMapper(client)
				.load(Subscriptions.class, "B2B_testCustomer", "package-1");
		System.out.println(subscriptions.toString());

		Subscriptions subscriptions1= new Subscriptions();
		subscriptions1.setSubscriptionIdentifier("B2B_testCustomer111");
		subscriptions1.setCourseOrPackageId("test");
		subscriptions1.setSubscriptionStatus(SubscriptionStatus.SUBSCRIBED);
		new DynamoDBMapper(client)
				.save(subscriptions1);
	}


}
