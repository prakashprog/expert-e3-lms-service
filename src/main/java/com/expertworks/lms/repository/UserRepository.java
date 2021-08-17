package com.expertworks.lms.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.Select;
import com.expertworks.lms.model.User;

@Repository
public class UserRepository {

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	// full scan
	public List<User> getAll() {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withLimit(10);
		PaginatedScanList<User> paginatedScanList = dynamoDBMapper.scan(User.class, scanExpression);

		paginatedScanList.loadAllResults();

		List<User> list = new ArrayList<User>(paginatedScanList.size());

		Iterator<User> iterator = paginatedScanList.iterator();
		while (iterator.hasNext()) {
			User element = iterator.next();
			list.add(element);
		}

		return list;

	}

	public List<User> get(String UserId) {

		User User = new User();
		User.setUserId(UserId);

		DynamoDBQueryExpression<User> dynamoDBQueryExpression = new DynamoDBQueryExpression().withHashKeyValues(User);
		PaginatedQueryList<User> paginatedQueryList = dynamoDBMapper.query(User.class, dynamoDBQueryExpression);
		paginatedQueryList.loadAllResults();

		List<User> list = new ArrayList<User>(paginatedQueryList.size());

		Iterator<User> iterator = paginatedQueryList.iterator();
		while (iterator.hasNext()) {
			User element = iterator.next();
			list.add(element);
		}

		return list;
	}
	
	
	

	public List<User> queryOnGSI(String indexName, String attributeName, String attributeValue) {

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS(attributeValue));

		DynamoDBQueryExpression<User> queryExpression = new DynamoDBQueryExpression<User>()
				.withIndexName(indexName).withKeyConditionExpression(attributeName + "= :val1")
				.withExpressionAttributeValues(eav).withConsistentRead(false);//.withLimit(2);

		QueryResultPage<User> scanPage = dynamoDBMapper.queryPage(User.class, queryExpression);

		List<User> list = scanPage.getResults();

		System.out.println("Partner List Size===========" + list.size());

		return list;
	}

	

	public User save(User user) {
		
		
		dynamoDBMapper.save(user);
		return user;
	}

	public String delete(String userId) {
		User user = dynamoDBMapper.load(User.class, userId);
		System.out.println("loading user :"+ user );
		if(user!=null) {
			System.out.println("deleting user :"+ user.getUserId() );
			dynamoDBMapper.delete(user);
		}
		return "User  Deleted!";
	}
	
	public User load(String userId) {
		
		User existingUser = dynamoDBMapper.load(User.class, userId);
		return existingUser;
		
	}

	public User update(String userId, User user) {
		
		User existingUser = dynamoDBMapper.load(User.class, userId);
		if(user.getName()!=null)
		{
			existingUser.setName(user.getName());
		}
		if(user.getPassword()!=null)
		{
			existingUser.setPassword(user.getPassword());
		}
		if(user.getEmail()!=null)
		{
			existingUser.setEmail(user.getEmail());
		}
				
		dynamoDBMapper.save(existingUser, new DynamoDBSaveExpression().withExpectedEntry("userId",
				new ExpectedAttributeValue(new AttributeValue().withS(userId))));
	
		
		return user;
	}
	
	
//	UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("userId", userId)
//    .withUpdateExpression("set info.name = :p")
//    .withValueMap(new ValueMap().withString(":p", "Name Changd.")
//        .withList(":a", Arrays.asList("Larry", "Moe", "Curly")))
//    .withReturnValues(ReturnValue.UPDATED_NEW);
//
//try {
//    System.out.println("Updating the item...");
//    UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
//    System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());
//
//}
//catch (Exception e) {
//    System.err.println("Unable to update item: " + year + " " + title);
//    System.err.println(e.getMessage());
//}
}