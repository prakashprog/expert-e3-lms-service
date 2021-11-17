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
import com.expertworks.lms.model.Acl;
import com.expertworks.lms.model.User;


@Repository
public class AclRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public List<Acl> getAll() {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withLimit(10);
		PaginatedScanList<Acl> paginatedScanList = dynamoDBMapper.scan(Acl.class, scanExpression);

		paginatedScanList.loadAllResults();

		List<Acl> list = new ArrayList<Acl>(paginatedScanList.size());

		Iterator<Acl> iterator = paginatedScanList.iterator();
		while (iterator.hasNext()) {
			Acl element = iterator.next();
			list.add(element);
		}

		return list;

	}
    
    
	public Acl load(String role) {
		Acl acl = dynamoDBMapper.load(Acl.class, role);
		return acl;
	}
    
    public List<Acl> get(String role) {
    	
    	Acl Acl = new Acl();
    	Acl.setUserRole(role);
    	
    	DynamoDBQueryExpression<Acl> dynamoDBQueryExpression = new DynamoDBQueryExpression().withHashKeyValues(Acl);
    	PaginatedQueryList<Acl> paginatedQueryList  =  dynamoDBMapper.query(Acl.class, dynamoDBQueryExpression);
    	paginatedQueryList.loadAllResults();

		List<Acl> list = new ArrayList<Acl>(paginatedQueryList.size());

		Iterator<Acl> iterator = paginatedQueryList.iterator();
		while (iterator.hasNext()) {
			Acl element = iterator.next();
			list.add(element);
		}

		return list;

    }
    

    public Acl save(Acl Acl) {
        dynamoDBMapper.save(Acl);
        return Acl;
    }

    public String delete(String AclId) {
    	Acl teamCourses = dynamoDBMapper.load(Acl.class, AclId);
        dynamoDBMapper.delete(teamCourses);
        return "Acl  Deleted!";
    }

    public String update(String AclId, Acl Acl) {
        dynamoDBMapper.save(AclId,
                new DynamoDBSaveExpression()
        .withExpectedEntry("teamId",
                new ExpectedAttributeValue(
                        new AttributeValue().withS(AclId)
                )));
        return AclId;
    }
} 