package com.expertworks.lms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;

@org.springframework.stereotype.Service
public class EmailService {

	// D:\aws email>aws ses delete-template --template-name WelcomeTemplate
	// D:\aws email>aws ses create-template --cli-input-json
	// file://WelcomeUserTemplate.json

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

	@Bean
	public AmazonSimpleEmailService getAmazonSimpleEmailService() {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		com.amazonaws.services.simpleemail.AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder
				.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();
		return client;
	}

	@Autowired
	AmazonSimpleEmailService client;

	public String sendEmail(String[] to, String templateDataJson) {

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
		System.out.println("From MailId : " + from);
		templatedEmailRequest.withSource("Sales@expert-works.com");
		client.sendTemplatedEmail(templatedEmailRequest);

		return "email sent";
	}

	
	public void sendSimpleSESMessage() {

		final String FROM = "Sales@expert-works.com";
		final String TO = "sprakasham@gmail.com";
		final String SUBJECT = "Amazon SES test (AWS SDK for Java)";
		final String HTMLBODY = "<h1>Amazon SES test (AWS SDK for Java)</h1>"
				+ "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
				+ "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>" + "AWS SDK for Java</a>";
		final String TEXTBODY = "This email was sent through Amazon SES " + "using the AWS SDK for Java.";

		SendEmailRequest request = new SendEmailRequest().withDestination(new Destination().withToAddresses(TO))
				.withMessage(new com.amazonaws.services.simpleemail.model.Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY))
								.withText(new Content().withCharset("UTF-8").withData(TEXTBODY)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);
		client.sendEmail(request);

	}
	
	public void sendCredentailsMessage(String to, String username , String userId,String password) {

		final String FROM = "Sales@expert-works.com";
		//final String TO = "sskprakash@gmail.com";
		final String SUBJECT = "Welcome to Expert Works.4";
		final String HTMLBODY = "Hi " + username +",<br><br>"+"Welcome to Expert Works,"
				+"<br>"+"Please login with below credentials"+"<br><br>"
		        + "<b>Username</b> : " +"<i>" +userId+"</i><br>"
		        + "<b>Password</b> : " + "<i>"+password+"</i><br><hr>"
				+ "<p>This email was sent from <a href='https://www.expert-works.com'>"
				+ "expert-works.com</a> "
		        + "<br><br><br> Thanks,<br> Expert Works Team.</a> ";
		final String TEXTBODY = "This email was sent from <a href='https://www.expert-works.com'>\"";

		SendEmailRequest request = new SendEmailRequest().withDestination(new Destination().withToAddresses(to))
				.withMessage(new com.amazonaws.services.simpleemail.model.Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY))
								.withText(new Content().withCharset("UTF-8").withData(TEXTBODY)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);
		client.sendEmail(request);

	}

}
