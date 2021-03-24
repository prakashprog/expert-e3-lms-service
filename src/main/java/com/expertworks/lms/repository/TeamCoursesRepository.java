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
import com.expertworks.lms.model.TeamCourses;

@Repository
public class TeamCoursesRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public List<TeamCourses> getTeamCourses() {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withLimit(10);
		PaginatedScanList<TeamCourses> paginatedScanList = dynamoDBMapper.scan(TeamCourses.class, scanExpression);

		paginatedScanList.loadAllResults();

		List<TeamCourses> list = new ArrayList<TeamCourses>(paginatedScanList.size());

		Iterator<TeamCourses> iterator = paginatedScanList.iterator();
		while (iterator.hasNext()) {
			TeamCourses element = iterator.next();
			list.add(element);
		}

		return list;

	}

    public TeamCourses save(TeamCourses courses) {
        dynamoDBMapper.save(courses);
        return courses;
    }

    public List<TeamCourses> getTeamCourses(String teamId) {
    	
    	TeamCourses teamCourses = new TeamCourses();
    	teamCourses.setTeamId(teamId);
    	
    	DynamoDBQueryExpression<TeamCourses> dynamoDBQueryExpression = new DynamoDBQueryExpression().withHashKeyValues(teamCourses);
    	PaginatedQueryList<TeamCourses> paginatedQueryList  =  dynamoDBMapper.query(TeamCourses.class, dynamoDBQueryExpression);
    	paginatedQueryList.loadAllResults();

		List<TeamCourses> list = new ArrayList<TeamCourses>(paginatedQueryList.size());

		Iterator<TeamCourses> iterator = paginatedQueryList.iterator();
		while (iterator.hasNext()) {
			TeamCourses element = iterator.next();
			list.add(element);
		}

		return list;

    }
    
    
 public List<TeamCourses> getTeamCoursesonGSI(String courseId) {
    	
    	TeamCourses teamCourses = new TeamCourses();
    	teamCourses.setCourseId(courseId);
    	
    	DynamoDBQueryExpression<TeamCourses> dynamoDBQueryExpression = new DynamoDBQueryExpression().withHashKeyValues(teamCourses);
    	
    	dynamoDBQueryExpression.withIndexName("courseId-index");
    	dynamoDBQueryExpression.setConsistentRead(false);
    	
    	PaginatedQueryList<TeamCourses> paginatedQueryList  =  dynamoDBMapper.query(TeamCourses.class, dynamoDBQueryExpression);
    	paginatedQueryList.loadAllResults();

    	   	
		List<TeamCourses> list = new ArrayList<TeamCourses>(paginatedQueryList.size());

		Iterator<TeamCourses> iterator = paginatedQueryList.iterator();
		while (iterator.hasNext()) {
			TeamCourses element = iterator.next();
			list.add(element);
		}

		return list;

    }
    
    
    
//    public TeamCourses getTeamCourses(String teamId) {
//        return dynamoDBMapper.load(TeamCourses.class, teamId);
//    }

    public String delete(String teamId) {
    	TeamCourses teamCourses = dynamoDBMapper.load(TeamCourses.class, teamId);
        dynamoDBMapper.delete(teamCourses);
        return "Team Course Deleted!";
    }

    public String update(String teamId, TeamCourses teamCourses) {
        dynamoDBMapper.save(teamId,
                new DynamoDBSaveExpression()
        .withExpectedEntry("teamId",
                new ExpectedAttributeValue(
                        new AttributeValue().withS(teamId)
                )));
        return teamId;
    }
} 