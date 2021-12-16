package com.expertworks.lms.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.expertworks.lms.model.Courses;
import com.expertworks.lms.model.ResourceLink;

@Repository
public class CoursesRepository {

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	public Courses save(Courses courses) {
		dynamoDBMapper.save(courses);
		return courses;
	}

	public void getmyCourses() {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();

		ScanRequest scanRequest = new ScanRequest().withTableName("CoursesMaster");

		ScanResult result = client.scan(scanRequest);
		for (Map<String, AttributeValue> item : result.getItems()) {
			System.out.println("=============================");
			System.out.println(item);
		}

	}

	/** get all records with starting with C# **/

	public List<Courses> getAllCourses() {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withLimit(100);
		PaginatedScanList<Courses> paginatedScanList = dynamoDBMapper.scan(Courses.class, scanExpression);

		paginatedScanList.loadAllResults();

		List<Courses> list = new ArrayList<Courses>(paginatedScanList.size());

		Iterator<Courses> iterator = paginatedScanList.iterator();
		while (iterator.hasNext()) {
			Courses element = iterator.next();
			if (element.getSk().startsWith("C"))
				list.add(element);
		}

		return list;

	}

	public List<Courses> getCourseSections(String courseId) {

		Courses courses = new Courses();
		courses.setCourseId(courseId);
		Condition rangeKeyCondition = new Condition().withComparisonOperator(ComparisonOperator.BEGINS_WITH.toString())
				.withAttributeValueList(new AttributeValue().withS("S"));

		DynamoDBQueryExpression<Courses> queryExpression = new DynamoDBQueryExpression<Courses>()
				.withHashKeyValues(courses).withRangeKeyCondition("sk", rangeKeyCondition);

		List<Courses> list = dynamoDBMapper.query(Courses.class, queryExpression);

		// return dynamoDBMapper.load(Courses.class, courseId,rangeKey);

		return list;
	}

	// pk = courseId & sortkey = C#cid
	public Courses getCoursesMetaDetails(String courseId) {
		Courses Courses = dynamoDBMapper.load(Courses.class, courseId, "C#" + courseId);
		return Courses;
	}

	// pk = courseId & sortkey = Beginswith C#
	public List<Courses> getMetaDetailsCourses(String courseId) {

		Courses courses = new Courses();
		courses.setCourseId(courseId);
		Condition rangeKeyCondition = new Condition().withComparisonOperator(ComparisonOperator.BEGINS_WITH.toString())
				.withAttributeValueList(new AttributeValue().withS("C"));

		DynamoDBQueryExpression<Courses> queryExpression = new DynamoDBQueryExpression<Courses>()
				.withHashKeyValues(courses).withRangeKeyCondition("sk", rangeKeyCondition);

		List<Courses> list = dynamoDBMapper.query(Courses.class, queryExpression);

		// return dynamoDBMapper.load(Courses.class, courseId,rangeKey);

		return list;
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

	public Courses getRow(String courseId, String rangeKey) {
		System.out.println("rangekey:" + rangeKey);
		return dynamoDBMapper.load(Courses.class, courseId, rangeKey);

	}

	public Courses getRowAndAddResourceAndUpdate(String courseId, String rangeKey) {
		System.out.println("rangekey:" + rangeKey);
		Courses courses = dynamoDBMapper.load(Courses.class, courseId, rangeKey);
		if (courses != null) {
			List<ResourceLink> resourceLinks = new ArrayList();
			ResourceLink resourceLink = new ResourceLink("content.pdf", "pdf", "Course Content");
			resourceLinks.add(resourceLink);
			courses.getVideoLinks().get(0).setResourceLinks(resourceLinks);
			dynamoDBMapper.save(courses);
		}
		return courses;
	}

	public static void main(String[] args) throws Exception {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
						"http://dynamodb.ap-south-1.amazonaws.com/", "ap-south-1"))
				.withCredentials(new AWSStaticCredentialsProvider(
						new BasicAWSCredentials("AKIAZTGOLXTICK323HL7", "06BBllO6zZvjWklG2o4qQIZHQAEjYXugPim6nNTg")))
				.build();

		CoursesRepository coursesRepository = new CoursesRepository();
		coursesRepository.dynamoDBMapper = new DynamoDBMapper(client);

		File file = ResourceUtils.getFile("classpath:data/CoursesMaster-Scan-sec2");
		String line = "";
		int c=0;
		BufferedReader br = new BufferedReader(new FileReader(file));
		Courses courses= null;
		while ((line = br.readLine()) != null) {
			String[] stringArray = line.split(",");
			System.out.println("stringArray[0,1] ; " + stringArray[0] + "," + stringArray[1]);
			 courses = coursesRepository.getRowAndAddResourceAndUpdate(stringArray[0], stringArray[1]);
			c++;
            if(c==40)
            	break;
		}

		System.out.println("Last courseId :"+courses.getCourseId());
       	courses.getVideoLinks().forEach(video -> {
			for (ResourceLink resourceLink : video.getResourceLinks()) {
				System.out.println("getLink :" + resourceLink.getLink());
				System.out.println("getTitle :" + resourceLink.getTitle());
				System.out.println("getType :" + resourceLink.getType());
			}

		});

		// coursesRepository.getmyCourses();

	}
}