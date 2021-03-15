package com.expertworks.lms.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.expertworks.lms.model.Courses;

@Repository
public class CoursesRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;


    public Courses save(Courses courses) {
        dynamoDBMapper.save(courses);
        return courses;
    }

    public Courses getTeamCourses(String teamId, String rangeKey) {
        return dynamoDBMapper.load(Courses.class, teamId,rangeKey);
    }
    
    public Courses getTeamCourses(String teamId) {
        return dynamoDBMapper.load(Courses.class, teamId);
    }

    public String delete(String employeeId) {
    	Courses emp = dynamoDBMapper.load(Courses.class, employeeId);
        dynamoDBMapper.delete(emp);
        return "Course Deleted!";
    }

    public String update(String employeeId, Courses employee) {
        dynamoDBMapper.save(employee,
                new DynamoDBSaveExpression()
        .withExpectedEntry("employeeId",
                new ExpectedAttributeValue(
                        new AttributeValue().withS(employeeId)
                )));
        return employeeId;
    }
} 