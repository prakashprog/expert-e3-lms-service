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
import com.expertworks.lms.model.TeamCourses;

@Repository
public class ContactRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public List<Contact> getAll() {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withLimit(10);
		PaginatedScanList<Contact> paginatedScanList = dynamoDBMapper.scan(Contact.class, scanExpression);

		paginatedScanList.loadAllResults();

		List<Contact> list = new ArrayList<Contact>(paginatedScanList.size());

		Iterator<Contact> iterator = paginatedScanList.iterator();
		while (iterator.hasNext()) {
			Contact element = iterator.next();
			list.add(element);
		}

		return list;

	}
    
    
    public List<Contact> get(String contactId) {
    	
    	Contact contact = new Contact();
    	contact.setContactId(contactId);
    	
    	DynamoDBQueryExpression<Contact> dynamoDBQueryExpression = new DynamoDBQueryExpression().withHashKeyValues(contact);
    	PaginatedQueryList<Contact> paginatedQueryList  =  dynamoDBMapper.query(Contact.class, dynamoDBQueryExpression);
    	paginatedQueryList.loadAllResults();

		List<Contact> list = new ArrayList<Contact>(paginatedQueryList.size());

		Iterator<Contact> iterator = paginatedQueryList.iterator();
		while (iterator.hasNext()) {
			Contact element = iterator.next();
			list.add(element);
		}

		return list;

    }
    

    public Contact save(Contact contact) {
        dynamoDBMapper.save(contact);
        return contact;
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