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
import com.expertworks.lms.model.Group;
import com.expertworks.lms.model.Team;
import com.expertworks.lms.model.User;

@Repository
public class GroupRepository {

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	// full scan
	public List<Group> getAll() {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withLimit(100);
		PaginatedScanList<Group> paginatedScanList = dynamoDBMapper.scan(Group.class, scanExpression);

		paginatedScanList.loadAllResults();

		List<Group> list = new ArrayList<Group>(paginatedScanList.size());

		Iterator<Group> iterator = paginatedScanList.iterator();
		while (iterator.hasNext()) {
			Group element = iterator.next();
			list.add(element);
		}

		return list;
	}
	
	

	public List<Group> queryOnGSI(String indexName, String attributeName, String attributeValue) {

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS(attributeValue));

		DynamoDBQueryExpression<Group> queryExpression = new DynamoDBQueryExpression<Group>()
				.withIndexName(indexName).withKeyConditionExpression(attributeName + "= :val1")
				.withExpressionAttributeValues(eav).withConsistentRead(false);//.withLimit(2);

		QueryResultPage<Group> scanPage = dynamoDBMapper.queryPage(Group.class, queryExpression);
		List<Group> list = scanPage.getResults();
		System.out.println("Group List Size===========" + list.size());

		return list;
	}
	
	

	public List<Group> get(String groupId) {

		Group Group = new Group();
		Group.setGroupId(groupId);

		DynamoDBQueryExpression<Group> dynamoDBQueryExpression = new DynamoDBQueryExpression().withHashKeyValues(Group);
		PaginatedQueryList<Group> paginatedQueryList = dynamoDBMapper.query(Group.class, dynamoDBQueryExpression);
		paginatedQueryList.loadAllResults();

		List<Group> list = new ArrayList<Group>(paginatedQueryList.size());

		Iterator<Group> iterator = paginatedQueryList.iterator();
		while (iterator.hasNext()) {
			Group element = iterator.next();
			list.add(element);
		}

		return list;

	}

	public List<Group> queryOnsk(String teamId) {

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS("T#"+teamId));

		DynamoDBQueryExpression<Group> queryExpression = new DynamoDBQueryExpression<Group>()
				.withIndexName("sk-index")
				.withKeyConditionExpression("sk= :val1").withExpressionAttributeValues(eav)
				.withConsistentRead(false).withLimit(2);

		QueryResultPage<Group> scanPage = dynamoDBMapper.queryPage(Group.class, queryExpression);
		
		List<Group> list=  scanPage.getResults();
		
		System.out.println("===========" + list.get(0).getGroupId());
		
		return list;
	}

	public Group addTeamToGroup(String groupId, Team team) {

		Group newRow = new Group();
		newRow.setGroupId(groupId);
		newRow.setSk("T#" + team.getTeamId());
		newRow.setTeamId(team.getTeamId());
		newRow.setName(team.getName());
	
		Group savedRow = this.save(newRow);
		return savedRow;
	}

	public Group save(Group Group) {
		dynamoDBMapper.save(Group);
		return Group;
	}
	
	public Group load(String groupId,String sk) {
		Group group = dynamoDBMapper.load(Group.class,groupId,sk);
		return group;
	}


	public String delete(String GroupId) {
		Group teamCourses = dynamoDBMapper.load(Group.class, GroupId);
		dynamoDBMapper.delete(teamCourses);
		return "Group  Deleted!";
	}

	public String update(String GroupId, Group Group) {
		dynamoDBMapper.save(GroupId, new DynamoDBSaveExpression().withExpectedEntry("teamId",
				new ExpectedAttributeValue(new AttributeValue().withS(GroupId))));
		return GroupId;
	}
}