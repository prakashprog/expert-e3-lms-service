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
import com.expertworks.lms.model.Contact;
import com.expertworks.lms.model.Pack;

@Repository
public class PackageRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public List<Pack> getAll() {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withLimit(10);
		PaginatedScanList<Pack> paginatedScanList = dynamoDBMapper.scan(Pack.class, scanExpression);

		paginatedScanList.loadAllResults();
		List<Pack> list = new ArrayList<Pack>(paginatedScanList.size());
	       Iterator<Pack> iterator = paginatedScanList.iterator();
		while (iterator.hasNext()) {
			Pack element = iterator.next();
			list.add(element);
		}

		return list;

	}
    
    
    public List<Pack> get(String contactId) {
    	
    	Contact contact = new Contact();
    	contact.setContactId(contactId);
    	
    	DynamoDBQueryExpression<Pack> dynamoDBQueryExpression = new DynamoDBQueryExpression().withHashKeyValues(contact);
    	PaginatedQueryList<Pack> paginatedQueryList  =  dynamoDBMapper.query(Pack.class, dynamoDBQueryExpression);
    	paginatedQueryList.loadAllResults();

		List<Pack> list = new ArrayList<Pack>(paginatedQueryList.size());

		Iterator<Pack> iterator = paginatedQueryList.iterator();
		while (iterator.hasNext()) {
			Pack element = iterator.next();
			list.add(element);
		}

		return list;

    }
    

    public Pack save(Pack pack) {
        dynamoDBMapper.save(pack);
        return pack;
    }

    public String delete(String PackId) {
    	Pack teamCourses = dynamoDBMapper.load(Pack.class, PackId);
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