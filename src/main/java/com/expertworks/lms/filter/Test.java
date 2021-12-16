package com.expertworks.lms.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.util.ResourceUtils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;

public class Test {

	public static void main(String[] args) throws Exception {

		File file = ResourceUtils.getFile("classpath:data/CoursesMaster-Scan-sec2");
		int c = 1;
		String line = "";
		BufferedReader br = new BufferedReader(new FileReader(file));
		while ((line = br.readLine()) != null) {
			System.out.println(c + ":======================================");
			String[] stringArray = line.split(",");
			for (int i = 0; i < stringArray.length; i++) {
				System.out.println(i + "=" + stringArray[i]);
			}
			c++;
			GetItemRequest getItemRequest = createGetItemRequest("CoursesMaster", stringArray[0], stringArray[1]);
			System.out.println("stringArray[0,1] ; " + stringArray[0] + "," + stringArray[1]);
			GetItemResult getItemResult = dynamoDB.getItem(getItemRequest);

			Map<String, AttributeValue> item = getItemResult.getItem();
			if (item != null) {
				if (item.get("videoLinks") != null)
					System.out.println("Video Link ::: " + item.get("videoLinks"));
			}

		}

//		File file = ResourceUtils.getFile("classpath:data/coursehours.csv");
//		int c = 0;
//		String line = "";
//		BufferedReader br = new BufferedReader(new FileReader(file));
//		while ((line = br.readLine()) != null)
//		{
//			String[] stringArray = line.split(",");
//			for (int i = 0; i < stringArray.length; i++) {
//				System.out.println(i + "=" + stringArray[i]);
//				// 1=recor,2=assignent,3=projrct ,7=courseId,6=totalhours
//			}
//			updateTableRow(stringArray[7], stringArray[1], stringArray[2], stringArray[3], stringArray[6]);
//			c++;
//		}

	}

	private static QueryRequest createQueryRequest(String table, String attributName, String attributValue) {
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setTableName(table);
		String keyConditionExpression = "#A1 = :A1";
		queryRequest.setKeyConditionExpression(keyConditionExpression);
		queryRequest.setConsistentRead(false);
		queryRequest.setScanIndexForward(true);

		Map<String, String> expressionAttributeNames = new HashMap<String, String>();
		expressionAttributeNames.put("#A1", attributName);

		Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
		expressionAttributeValues.put(":A1", new AttributeValue(attributValue));
		queryRequest.setExpressionAttributeNames(expressionAttributeNames);
		queryRequest.setExpressionAttributeValues(expressionAttributeValues);
		return queryRequest;
	}

	private static GetItemRequest createGetItemRequest(String table, String partionkey, String rangeKey) {
		GetItemRequest getItemRequest = new GetItemRequest();
		getItemRequest.setTableName(table);
		HashMap<String, AttributeValue> searchKeys = new HashMap<String, AttributeValue>();
		searchKeys.put("courseId", new AttributeValue(partionkey));
		searchKeys.put("sk", new AttributeValue(rangeKey));
		getItemRequest.setKey(searchKeys);
		return getItemRequest;
	}

	public static File loadFileWithSpringInternalClass(String fileName) throws FileNotFoundException {
		// return ResourceUtils.getFile("classpath:data/coursehours.csv");
		return ResourceUtils.getFile("classpath:data/coursehours.csv");
	}

	public static AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard()
			.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
					"http://dynamodb.ap-south-1.amazonaws.com/", "ap-south-1"))
			.withCredentials(new AWSStaticCredentialsProvider(
					new BasicAWSCredentials("AKIAZTGOLXTICK323HL7", "06BBllO6zZvjWklG2o4qQIZHQAEjYXugPim6nNTg")))
			.build();

	public static void updateTableRow(String courseId, String rechours, String assignments, String mini,
			String totalhours) throws Exception {
		// Create the DynamoDB Client with the region you want

		// Create UpdateItemRequest
		HashMap<String, AttributeValue> searchKeys = new HashMap<String, AttributeValue>();
		searchKeys.put("courseId", new AttributeValue(courseId));
		searchKeys.put("sk", new AttributeValue("C#" + courseId));
		// ---------------------------------------------------------------------------------------------
		String tableName = "CoursesMaster";
		String updateExpression = "SET #A1 = :A1, #A2 = :A2";
		Map<String, String> expressionAttributeNames = new HashMap<String, String>();
		expressionAttributeNames.put("#A1", "includes");
		expressionAttributeNames.put("#A2", "hours");

		Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
		expressionAttributeValues.put(":A1",
				new AttributeValue().withL(getAttributeValueMethod1(rechours, assignments, mini)));
		expressionAttributeValues.put(":A2", new AttributeValue(totalhours));
		// -----------------------------------------------------------------------------------------------------
		UpdateItemRequest updateItemRequest = createUpdateItemRequest(tableName, updateExpression, searchKeys,
				expressionAttributeNames, expressionAttributeValues);
		UpdateItemResult updateItemResult = dynamoDB.updateItem(updateItemRequest);
		System.out.println("Successfully updated item.");
		// Handle updateItemResult

	}

	private static UpdateItemRequest createUpdateItemRequest(String tableName, String updateExpression,
			HashMap<String, AttributeValue> searchKeys, Map<String, String> expressionAttributeNames,
			Map<String, AttributeValue> expressionAttributeValues) {
		UpdateItemRequest updateItemRequest = new UpdateItemRequest();
		updateItemRequest.setTableName(tableName);
		updateItemRequest.setKey(searchKeys);

		updateItemRequest.setUpdateExpression(updateExpression);
		updateItemRequest.setExpressionAttributeNames(expressionAttributeNames);
		updateItemRequest.setExpressionAttributeValues(expressionAttributeValues);
		return updateItemRequest;
	}

	private static List<AttributeValue> getAttributeValueMethod1(String rechours, String assignments, String mini) {
		List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();
		if (rechours != null && !rechours.equalsIgnoreCase(""))
			attributeValues.add(new AttributeValue(rechours + " hours of recorded sessions"));
		if (assignments != null && !assignments.equalsIgnoreCase(""))
			attributeValues.add(new AttributeValue(assignments + " assignments"));
		if (mini != null && !mini.equalsIgnoreCase(""))
			attributeValues.add(new AttributeValue(mini + " mini-project"));
		attributeValues.add(new AttributeValue("Real-time contents"));
		attributeValues.add(new AttributeValue("Questions & Answers"));
		return attributeValues;
	}

	private static List<AttributeValue> getAttributeValueMethod1() {
		List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();
		attributeValues.add(new AttributeValue(" hours of recorded sessions"));
		attributeValues.add(new AttributeValue(" assignments"));
		attributeValues.add(new AttributeValue(" mini-project"));
		attributeValues.add(new AttributeValue("Real-time contents"));
		return attributeValues;
	}

	public static void readFile(File file) throws Exception {
		// parsing a CSV file into Scanner class constructor
		Scanner sc = new Scanner(file);
		sc.useDelimiter(","); // sets the delimiter pattern
		while (sc.hasNext()) // returns a boolean value
		{
			System.out.print(sc.next());

		}
		sc.close(); // closes the scanner
	}

	public static void readFileBuffered(File file) throws Exception {

		String line = "";
		String splitBy = ",";
		BufferedReader br = new BufferedReader(new FileReader(file));
		while ((line = br.readLine()) != null) // returns a Boolean value
		{
			String[] employee = line.split(splitBy); // use comma as separator
			System.out.println(employee);
		}
	}
}
