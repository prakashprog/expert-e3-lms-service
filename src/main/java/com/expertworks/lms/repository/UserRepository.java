package com.expertworks.lms.repository;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.expertworks.lms.model.User;


@Repository
public class UserRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    //full scan
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
    	PaginatedQueryList<User> paginatedQueryList  =  dynamoDBMapper.query(User.class, dynamoDBQueryExpression);
    	paginatedQueryList.loadAllResults();

		List<User> list = new ArrayList<User>(paginatedQueryList.size());

		Iterator<User> iterator = paginatedQueryList.iterator();
		while (iterator.hasNext()) {
			User element = iterator.next();
			list.add(element);
		}

		return list;

    }
    

    public User save(User User) {
        dynamoDBMapper.save(User);
        return User;
    }

    public String delete(String UserId) {
    	User UserCourses = dynamoDBMapper.load(User.class, UserId);
        dynamoDBMapper.delete(UserCourses);
        return "User  Deleted!";
    }

    public String update(String UserId, User User) {
        dynamoDBMapper.save(UserId,
                new DynamoDBSaveExpression()
        .withExpectedEntry("UserId",
                new ExpectedAttributeValue(
                        new AttributeValue().withS(UserId)
                )));
        return UserId;
    }
} 