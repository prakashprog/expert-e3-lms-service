package com.expertworks.lms.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.expertworks.lms.model.Acl;

@Repository
public class AclRepository {

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	public List<Acl> getAll() {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();// .withLimit(10);
		PaginatedScanList<Acl> paginatedScanList = dynamoDBMapper.scan(Acl.class, scanExpression);

		paginatedScanList.loadAllResults();

		List<Acl> list = new ArrayList<Acl>(paginatedScanList.size());

		Iterator<Acl> iterator = paginatedScanList.iterator();
		while (iterator.hasNext()) {
			Acl element = iterator.next();
			list.add(element);
		}

		return list;

	}

	public Acl load(String role) {
		Acl acl = dynamoDBMapper.load(Acl.class, role);
		return acl;
	}

	public List<Acl> get(String role) {

		Acl Acl = new Acl();
		Acl.setUserRole(role);

		DynamoDBQueryExpression<Acl> dynamoDBQueryExpression = new DynamoDBQueryExpression().withHashKeyValues(Acl);
		PaginatedQueryList<Acl> paginatedQueryList = dynamoDBMapper.query(Acl.class, dynamoDBQueryExpression);
		paginatedQueryList.loadAllResults();

		List<Acl> list = new ArrayList<Acl>(paginatedQueryList.size());

		Iterator<Acl> iterator = paginatedQueryList.iterator();
		while (iterator.hasNext()) {
			Acl element = iterator.next();
			list.add(element);
		}

		return list;

	}

	public Acl save(Acl Acl) {
		dynamoDBMapper.save(Acl);
		return Acl;
	}

	public String delete(String AclId) {
		Acl teamCourses = dynamoDBMapper.load(Acl.class, AclId);
		dynamoDBMapper.delete(teamCourses);
		return "Acl  Deleted!";
	}

	public String update(String AclId, Acl Acl) {
		dynamoDBMapper.save(AclId, new DynamoDBSaveExpression().withExpectedEntry("teamId",
				new ExpectedAttributeValue(new AttributeValue().withS(AclId))));
		return AclId;
	}

	public String migrate() {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		PaginatedScanList<Acl> paginatedScanList = dynamoDBMapper.scan(Acl.class, scanExpression);

		paginatedScanList.loadAllResults();

		List<Acl> list = new ArrayList<Acl>(paginatedScanList.size());

		Iterator<Acl> iterator = paginatedScanList.iterator();
		while (iterator.hasNext()) {
			Acl acl = iterator.next();
			list.add(acl);
			dynamoDBMapper.save(acl);
		}

		return "";
	}

	public static void main(String[] args) throws Exception {

		// createTable("Moviesone");
		//scanTableAndCopy("CoursesMaster", "CoursesMaster");
		scanTableAndCopy("Packages", "Packages");
		//listTables();

	}

//	CreateTableRequest createTableRequest = new CreateTableRequest();
//	new CreateTableRequest()
//    .withTableName(table.getTableName())
//    .withKeySchema(table.describe().getKeySchema())
//    .withAttributeDefinitions(table.describe().getAttributeDefinitions())
//    .withGlobalSecondaryIndexes(table.describe().getGlobalSecondaryIndexes())
//    .withProvisionedThroughput(new ProvisionedThroughput(10L, 10L));

	// https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStarted.Java.03.html
	public static void listTables() throws Exception {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
						"http://dynamodb.ap-south-1.amazonaws.com/", "ap-south-1"))
				.withCredentials(new AWSStaticCredentialsProvider(
						new BasicAWSCredentials("AKIAZTGOLXTICK323HL7", "06BBllO6zZvjWklG2o4qQIZHQAEjYXugPim6nNTg")))
				.build();
		DynamoDB dynamoDB = new DynamoDB(client);

		AmazonDynamoDB clientDev = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
						"http://dynamodb.ap-south-1.amazonaws.com/", "ap-south-1"))
				.withCredentials(new AWSStaticCredentialsProvider(
						new BasicAWSCredentials("AKIA2QKQ27EQTHXINMVF", "hWOzb6QEsd0GzMTZzm4CVvjpOoQhd+jP4Ntmbpi+")))
				.build();
		DynamoDB dynamoDBDev = new DynamoDB(clientDev);

		TableCollection<ListTablesResult> tables = dynamoDB.listTables();
		Iterator<Table> iterator = tables.iterator();

		while (iterator.hasNext()) {
			Table table = iterator.next();

			System.out.println(table.getTableName() + "=============================");
			System.out.println("=KeyShema=" + table.describe().getKeySchema());
			System.out.println("=TableName=" + table.describe().getTableName());
			System.out.println("=AAA==" + table.describe().getAttributeDefinitions());
			System.out.println("=GSI=" + table.describe().getGlobalSecondaryIndexes());
			System.out.println("==PTP=" + table.describe().getProvisionedThroughput());

			if (table.getTableName().equalsIgnoreCase("User")) {

				System.out.println("getKeySchema: " + table.describe().getKeySchema());
				System.out.println("getKeySchema: " + table.describe().getAttributeDefinitions());
				Table devTable = dynamoDBDev.createTable(table.getTableName(), table.describe().getKeySchema(),
						table.describe().getAttributeDefinitions(), new ProvisionedThroughput(10L, 10L));
				table.waitForActive();
				System.out.println("Success.Table status: " + devTable.getTableName() + "; "
						+ devTable.getDescription().getTableStatus());
				break;

			}

		}

	}

	// https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStarted.Java.03.html
	public static void createTable(String tableNameinput) {

		AmazonDynamoDB clientDev = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
						"http://dynamodb.ap-south-1.amazonaws.com/", "ap-south-1"))
				.withCredentials(new AWSStaticCredentialsProvider(
						new BasicAWSCredentials("AKIA2QKQ27EQTHXINMVF", "hWOzb6QEsd0GzMTZzm4CVvjpOoQhd+jP4Ntmbpi+")))
				.build();

		DynamoDB dynamoDB = new DynamoDB(clientDev);

		String tableName = tableNameinput;
		;

		try {
			System.out.println("Attempting to create table; please wait...");
			Table table = dynamoDB.createTable(tableName, Arrays.asList(new KeySchemaElement("year", KeyType.HASH), // Partition
																													// key
					new KeySchemaElement("title", KeyType.RANGE)), // Sort key
					Arrays.asList(new AttributeDefinition("year", ScalarAttributeType.N),
							new AttributeDefinition("title", ScalarAttributeType.S)),
					new ProvisionedThroughput(10L, 10L));
			table.waitForActive();
			System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());

		} catch (Exception e) {
			System.err.println("Unable to create table: ");
			System.err.println(e.getMessage());
		}

	}

	// https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStarted.Java.03.html
	public static void scanTableAndCopy(String tableNameSource, String tableNamedestination) {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
						"http://dynamodb.ap-south-1.amazonaws.com/", "ap-south-1"))
				.withCredentials(new AWSStaticCredentialsProvider(
						new BasicAWSCredentials("AKIAZTGOLXTICK323HL7", "06BBllO6zZvjWklG2o4qQIZHQAEjYXugPim6nNTg")))
				.build();
		DynamoDB dynamoDB = new DynamoDB(client);

		AmazonDynamoDB clientDev = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
						"http://dynamodb.ap-south-1.amazonaws.com/", "ap-south-1"))
				.withCredentials(new AWSStaticCredentialsProvider(
						new BasicAWSCredentials("AKIA2QKQ27EQTHXINMVF", "hWOzb6QEsd0GzMTZzm4CVvjpOoQhd+jP4Ntmbpi+")))
				.build();
		DynamoDB dynamoDBDev = new DynamoDB(clientDev);




		try {
			System.out.println("Scanning  to  table; please wait...");
			Table table = dynamoDB.getTable(tableNameSource);// Partition
			Table destinationtable = dynamoDBDev.getTable(tableNamedestination);// Partition
			ScanRequest scanRequest = new ScanRequest().withTableName(tableNameSource);
			// key
			ScanResult result = client.scan(scanRequest);
			for (Map<String, AttributeValue> item : result.getItems()) {
				System.out.println(item);
				// destinationtable.putItem((Item) item);
			}
			// ------------------------------------------------------
			// https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/ScanJavaDocumentAPI.html
			ScanSpec ScanSpec = new ScanSpec();
			ItemCollection<ScanOutcome> items = table.scan(ScanSpec);

			for (Item item : items) {
				System.out.println(item.toJSONPretty());
				destinationtable.putItem(item);
			}

			System.out.println("==========================================");
			System.out.println("size : " + result.getItems().size());
			System.out.println("table.scan items size : " + items.getTotalCount());
			System.out.println("==========================================");
		} catch (Exception e) {
			System.err.println("Unable to create table: ");
			System.err.println(e.getMessage());
		}

	}
}
