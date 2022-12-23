package com.expertworks.lms.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.expertworks.lms.enums.CustomerType;
import com.expertworks.lms.enums.PackageType;
import com.expertworks.lms.enums.SubscriptionStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamoDBTable(tableName = "Subscriptions")
public class Subscriptions {

	/**
	 * subsciptionIdendtifier = CustomerType_customerId
	 */
	@DynamoDBHashKey(attributeName = "subscriptionIdentifier")
	private String subscriptionIdentifier;

	@DynamoDBRangeKey(attributeName ="courseOrPackageId")
	private String courseOrPackageId;

	@DynamoDBAttribute(attributeName = "bundleId")
	private String bundleId;

	@DynamoDBTypeConvertedEnum
	@DynamoDBAttribute(attributeName = "subscriptionStatus")
	private SubscriptionStatus subscriptionStatus;

	@DynamoDBTypeConvertedEnum
	@DynamoDBAttribute(attributeName = "subscriptionPackage")
	private PackageType subscriptionPackage;

	@DynamoDBTypeConvertedEnum
	@DynamoDBAttribute(attributeName = "customerType")
	private CustomerType customerType;

	private String groupId;

}
