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
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.expertworks.lms.model.Group;
import com.expertworks.lms.model.Partner;
import com.expertworks.lms.model.Team;
import com.expertworks.lms.model.Partner;
import com.expertworks.lms.model.TeamCourses;
import com.expertworks.lms.model.UserDetail;

@Repository
public class PartnerRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    //full scan
    public List<Partner> getAll() {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withLimit(100);
		PaginatedScanList<Partner> paginatedScanList = dynamoDBMapper.scan(Partner.class, scanExpression);

		paginatedScanList.loadAllResults();

		List<Partner> list = new ArrayList<Partner>(paginatedScanList.size());

		Iterator<Partner> iterator = paginatedScanList.iterator();
		while (iterator.hasNext()) {
			Partner element = iterator.next();
			list.add(element);
		}

		return list;

	}
    
    
    public List<Partner> queryOnsk(String groupId) {

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS("G#"+groupId));

		DynamoDBQueryExpression<Partner> queryExpression = new DynamoDBQueryExpression<Partner>()
				.withIndexName("sk-index")
				.withKeyConditionExpression("sk= :val1").withExpressionAttributeValues(eav)
				.withConsistentRead(false);

		QueryResultPage<Partner> scanPage = dynamoDBMapper.queryPage(Partner.class, queryExpression);
		List<Partner> list=  scanPage.getResults();
		System.out.println("Partner List Size===========" + list.size());
		return list;
	}
    

	public List<Partner> queryOnGSI(String indexName, String attributeName, String attributeValue) {

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS(attributeValue));

		DynamoDBQueryExpression<Partner> queryExpression = new DynamoDBQueryExpression<Partner>().withIndexName(indexName)
				.withKeyConditionExpression(attributeName + "= :val1").withExpressionAttributeValues(eav)
				.withConsistentRead(false);// .withLimit(2);

		QueryResultPage<Partner> scanPage = dynamoDBMapper.queryPage(Partner.class, queryExpression);
		List<Partner> list = scanPage.getResults();
		return list;
	}
    
 
    
    
    public List<Partner> get(String partnerId) {
    	
    	Partner partner = new Partner();
    	partner.setPartnerId(partnerId);
    	
    	DynamoDBQueryExpression<Partner> dynamoDBQueryExpression = new DynamoDBQueryExpression().withHashKeyValues(partner);
    	PaginatedQueryList<Partner> paginatedQueryList  =  dynamoDBMapper.query(Partner.class, dynamoDBQueryExpression);
    	paginatedQueryList.loadAllResults();

		List<Partner> list = new ArrayList<Partner>(paginatedQueryList.size());

		Iterator<Partner> iterator = paginatedQueryList.iterator();
		while (iterator.hasNext()) {
			Partner element = iterator.next();
			list.add(element);
		}

		return list;

    }
    
    public Partner addGroup(String partnerId, Group group) {

		Partner newRow = new Partner();
		newRow.setPartnerId(partnerId);
		newRow.setSk("G#" + group.getGroupId());
		newRow.setGroupId(group.getGroupId());
		newRow.setName(group.getName());
		Partner savedRow = this.save(newRow);
		return savedRow;
	}
    

    public Partner save(Partner partner) {
        dynamoDBMapper.save(partner);
        return partner;
    }

    public String delete(String PartnerId) {
    	Partner teamCourses = dynamoDBMapper.load(Partner.class, PartnerId);
        dynamoDBMapper.delete(teamCourses);
        return "Partner  Deleted!";
    }

    public String update(String partnerId, Partner Partner) {
        dynamoDBMapper.save(partnerId,
                new DynamoDBSaveExpression()
        .withExpectedEntry("teamId",
                new ExpectedAttributeValue(
                        new AttributeValue().withS(partnerId)
                )));
        return partnerId;
    }
} 