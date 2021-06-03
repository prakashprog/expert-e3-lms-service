package com.expertworks.lms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;

@org.springframework.stereotype.Service
public class EmailService {
	
	//D:\aws email>aws ses delete-template --template-name WelcomeTemplate
	//D:\aws email>aws ses create-template --cli-input-json file://WelcomeUserTemplate.json

	@Value("${aws.ses.accesskey}")
	private String accessKey;

	@Value("${aws.ses.secretkey}")
	private String secretKey;

	@Value("${aws.ses.region}")
	private String region;
	
	@Value("${aws.ses.from}")
	private String from;

	@Value("${aws.ses.templatename}")
	private String templateName;
		


	public String sendEmail(String[] to, String templateDataJson) {

		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		com.amazonaws.services.simpleemail.AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder
				.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();

		Destination destination = new Destination();
		List<String> toAddresses = new ArrayList<String>();
		String[] Emails = to;

		for (String email : Emails) {
			toAddresses.add(email);
		}

		destination.setToAddresses(toAddresses);
		SendTemplatedEmailRequest templatedEmailRequest = new SendTemplatedEmailRequest();
		templatedEmailRequest.withDestination(destination);
		templatedEmailRequest.withTemplate(templateName);
		templatedEmailRequest.withTemplateData(templateDataJson);
		templatedEmailRequest.withSource(from);
		client.sendTemplatedEmail(templatedEmailRequest);
		return "email sent";
	}

}
