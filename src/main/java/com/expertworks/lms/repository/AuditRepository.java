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
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.expertworks.lms.model.Audit;


@Repository
public class AuditRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    
    

    public List<Audit> getAll() {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withLimit(10);
		PaginatedScanList<Audit> paginatedScanList = dynamoDBMapper.scan(Audit.class, scanExpression);
		paginatedScanList.loadAllResults();
		List<Audit> list = new ArrayList<Audit>(paginatedScanList.size());
		Iterator<Audit> iterator = paginatedScanList.iterator();
		while (iterator.hasNext()) {
			Audit element = iterator.next();
			list.add(element);
		}
		return list;
	}
    
    
    public List<Audit> get(String auditId) {
    	
    	Audit audit = new Audit();
    	audit.setId(auditId);
    	
    	DynamoDBQueryExpression<Audit> dynamoDBQueryExpression = new DynamoDBQueryExpression().withHashKeyValues(audit);
    	PaginatedQueryList<Audit> paginatedQueryList  =  dynamoDBMapper.query(Audit.class, dynamoDBQueryExpression);
    	paginatedQueryList.loadAllResults();

		List<Audit> list = new ArrayList<Audit>(paginatedQueryList.size());
		Iterator<Audit> iterator = paginatedQueryList.iterator();
		while (iterator.hasNext()) {
			Audit element = iterator.next();
			list.add(element);
		}
		return list;

    }
    
    public Audit save(Audit audit) {
        dynamoDBMapper.save(audit);
        return audit;
    }

    public String delete(String contactId) {
    	Audit teamCourses = dynamoDBMapper.load(Audit.class, contactId);
        dynamoDBMapper.delete(teamCourses);
        return "Audit  Deleted!";
    }

    public String update(String id, Audit audit) {
    	
    	dynamoDBMapper.save(audit, new DynamoDBSaveExpression().withExpectedEntry("id",
				new ExpectedAttributeValue(new AttributeValue().withS(id))));
		return id;
    	
     
    }
} 