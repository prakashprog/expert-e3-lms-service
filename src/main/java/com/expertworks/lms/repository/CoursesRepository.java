package com.expertworks.lms.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
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

	public List<Courses> getCourses() {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withLimit(10);
		PaginatedScanList<Courses> paginatedScanList = dynamoDBMapper.scan(Courses.class, scanExpression);

		paginatedScanList.loadAllResults();

		List<Courses> list = new ArrayList<Courses>(paginatedScanList.size());

		Iterator<Courses> iterator = paginatedScanList.iterator();
		while (iterator.hasNext()) {
			Courses element = iterator.next();
			list.add(element);
		}

		return list;

	}

	public Courses getCourses(String teamId, String rangeKey) {
		return dynamoDBMapper.load(Courses.class, teamId, rangeKey);
	}

	public Courses getCourses(String courseId) {
		return dynamoDBMapper.load(Courses.class, courseId);
	}

	public String delete(String courseId) {
		Courses course = dynamoDBMapper.load(Courses.class, courseId);
		dynamoDBMapper.delete(course);
		return "Course Deleted!";
	}

	public String update(String courseId, Courses Courses) {
		dynamoDBMapper.save(Courses, new DynamoDBSaveExpression().withExpectedEntry("courseId",
				new ExpectedAttributeValue(new AttributeValue().withS(courseId))));
		return courseId;
	}
}