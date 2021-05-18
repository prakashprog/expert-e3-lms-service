package com.expertworks.lms.repository;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.expertworks.lms.model.Contact;
import com.expertworks.lms.model.UserDetail;

@Repository
public class UserDetailsRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public List<UserDetail> getAll() {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withLimit(10);
		PaginatedScanList<UserDetail> paginatedScanList = dynamoDBMapper.scan(UserDetail.class, scanExpression);

		paginatedScanList.loadAllResults();

		List<UserDetail> list = new ArrayList<UserDetail>(paginatedScanList.size());

		Iterator<UserDetail> iterator = paginatedScanList.iterator();
		while (iterator.hasNext()) {
			UserDetail element = iterator.next();
			list.add(element);
		}

		return list;

	}
    
    
    public List<UserDetail> get(String userId,String sk) {
    	
    	UserDetail userDetail = new UserDetail();
    	userDetail.setUserId(userId);
    
    	Condition rangeKeyCondition = new Condition()
		        .withComparisonOperator(ComparisonOperator.BEGINS_WITH.toString())
		        .withAttributeValueList(new AttributeValue().withS(sk));
		
		DynamoDBQueryExpression<UserDetail> queryExpression = new DynamoDBQueryExpression<UserDetail>()
		        .withHashKeyValues(userDetail)
		        .withRangeKeyCondition("sk", rangeKeyCondition);
		
		List<UserDetail> list = dynamoDBMapper.query(UserDetail.class, queryExpression);
		
		return list;
		

    }
    

    public UserDetail save(UserDetail userDetail) {
        dynamoDBMapper.save(userDetail);
        return userDetail;
    }

    public String delete(String contactId) {
    	Contact teamCourses = dynamoDBMapper.load(Contact.class, contactId);
        dynamoDBMapper.delete(teamCourses);
        return "Contact  Deleted!";
    }

    public String update(String contactId, Contact contact) {
        dynamoDBMapper.save(contactId,
                new DynamoDBSaveExpression()
        .withExpectedEntry("teamId",
                new ExpectedAttributeValue(
                        new AttributeValue().withS(contactId)
                )));
        return contactId;
    }
} 