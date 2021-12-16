package com.expertworks.lms.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

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
import com.expertworks.lms.http.EmailDTO;
import com.expertworks.lms.model.Contact;
import com.expertworks.lms.util.CloudTemplateLoader;

import freemarker.template.Configuration;
import freemarker.template.Template;

@org.springframework.stereotype.Service
public class EmailService {

	private final static Logger logger = LoggerFactory.getLogger(EmailService.class);

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

	@Value("${aws.ses.templateroot}")
	private String templateRootURL;

	@Value("${aws.ses.emailid.welcome}")
	private String welcomeEmailFrom;

	@Value("${aws.ses.emailid.sales}")
	private String salesEmailFrom;

	@Bean
	public AmazonSimpleEmailService getAmazonSimpleEmailService() {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		com.amazonaws.services.simpleemail.AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder
				.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();
		return client;
	}

//	 @Bean
//     public FreeMarkerConfigurationFactoryBean factoryBean() throws Exception {
//         FreeMarkerConfigurationFactoryBean bean=new FreeMarkerConfigurationFactoryBean();
//          bean.setTemplateLoaderPath("classpath:/templates");
//           return bean;
//     }
//
//	 @Autowired
//	 Configuration fmConfiguration;

	@Autowired
	AmazonSimpleEmailService client;

	@Bean
	public FreeMarkerConfigurationFactoryBean getFreeMarkerConfig() throws Exception {
		FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
		Properties properties = new Properties();
		properties.setProperty("localized_lookup", "false");
		bean.setFreemarkerSettings(properties);
		bean.setPreTemplateLoaders(new CloudTemplateLoader(new URL(templateRootURL)));
		bean.setDefaultEncoding("UTF-8");
		return bean;
		// Configuration fmConfiguration = bean.createConfiguration();

	}

	@Autowired
	FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean;

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

	public void sendCredentailsMessage(String to, String username, String userId, String password) {

		final String FROM = "Sales@expert-works.com";
		// final String TO = "sskprakash@gmail.com";
		final String SUBJECT = "Welcome to Expert Works.4";
		final String HTMLBODY = "Hi " + username + ",<br><br>" + "Welcome to Expert Works," + "<br>"
				+ "Please login with below credentials" + "<br><br>" + "<b>Login Id</b> : " + "<i>" + userId
				+ "</i><br>" + "<b>Password</b> : " + "<i>" + password + "</i><br><hr>"
				+ "<p>This email was sent from <a href='https://www.expert-works.com'>" + "expert-works.com</a> "
				+ "<br><br><br> Thanks,<br> Expert Works Team.</a> ";
		final String TEXTBODY = "This email was sent from <a href='https://www.expert-works.com'>\"";
		logger.info("Sending mail to :" + to);
		SendEmailRequest request = new SendEmailRequest().withDestination(new Destination().withToAddresses(to))
				.withMessage(new com.amazonaws.services.simpleemail.model.Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY))
								.withText(new Content().withCharset("UTF-8").withData(TEXTBODY)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);
		client.sendEmail(request);

	}

	public void sendEmailVerification(String to, String username, String userId, String verificationKey) {

		final String FROM = "Sales@expert-works.com";
		// final String TO = "sskprakash@gmail.com";
		final String SUBJECT = "Welcome to Expert Works , please verify your email!";

		String verificationUrl = "<a href=https://api.expert-works.com/public/signup/" + userId + "/" + verificationKey
				+ ">Verify Email</a>";
		// String verificationUrl= "<a
		// href=http://localhost:9091/public/signup/"+userId+"/"+verificationKey+">Verify
		// Email</a>";
		// String verificationUrl= "<a
		// href=https://www.expert-works.com/public/signup/"+userId+"/"+verificationKey+">Verify
		// Email</a>";
		final String HTMLBODY = "Hi " + username + ",<br><br>" + "Welcome to Expert Works," + "<br>"
				+ "We recently received a request to create an account. To verify that you made this request, we're sending this confirmation email."
				+ "<br><br>" + "<b>" + verificationUrl + "<br><hr>"

				+ "<p>This email was sent from <a href='https://www.expert-works.com'>" + "expert-works.com</a> "
				+ "<br><br><br> Thanks,<br> Expert Works Team.</a> ";
		final String TEXTBODY = "This email was sent from <a href='https://www.expert-works.com'>\"";
		logger.info("Sending mail to :" + to);
		SendEmailRequest request = new SendEmailRequest().withDestination(new Destination().withToAddresses(to))
				.withMessage(new com.amazonaws.services.simpleemail.model.Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY))
								.withText(new Content().withCharset("UTF-8").withData(TEXTBODY)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);
		client.sendEmail(request);

	}

	// public void sendEmailVerificationv1(String to, String username,String
	// userId,String verificationKey) {
	public void sendEmailVerificationv1(EmailDTO email) throws Exception {

		// final String FROM = "Sales@expert-works.com";
		final String FROM = welcomeEmailFrom;
		final String SUBJECT = "Welcome to Expert Works , please verify your email!";
		Map<String, Object> model = new HashMap<>();
		String verificationKey = email.getVerificationKey();
		String userId = email.getLoginId();
		String verificationUrl = "https://api.expert-works.com/public/signup/" + userId + "/" + verificationKey;

		model.put("user", email.getUsername());
		model.put("url", verificationUrl);

		Configuration fmConfiguration = freeMarkerConfigurationFactoryBean.createConfiguration();
		Template template = fmConfiguration.getTemplate("email_template/verify_email.html");
		String HTMLBODY = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

		final String TEXTBODY = "This email was sent from <a href='https://www.expert-works.com'>\"";
		SendEmailRequest request = new SendEmailRequest()
				.withDestination(new Destination().withToAddresses(email.getTo()))
				.withMessage(new com.amazonaws.services.simpleemail.model.Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY))
								.withText(new Content().withCharset("UTF-8").withData(TEXTBODY)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);
		client.sendEmail(request);

	}

	public void sendResetCredentailsMail(String to, String username, String userId, String password) {

		final String FROM = "Sales@expert-works.com";
		// final String TO = "sskprakash@gmail.com";
		final String SUBJECT = "Password Reset for Expert-works";
		final String HTMLBODY = "Hi " + username + ",<br><br>" + "Welcome to Expert Works," + "<br>"
				+ "Please login with below default credentials" + "<br><br>" + "<b>Login Id</b> : " + "<i>" + userId
				+ "</i><br>" + "<b>Password</b> : " + "<i>" + password + "</i><br><hr>"
				+ "<p>This email was sent from <a href='https://www.expert-works.com'>" + "expert-works.com</a> "
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

	public void sendResetCredentailsEMailv1(EmailDTO email) throws Exception {

		final String FROM = "Sales@expert-works.com";
		// final String TO = "sskprakash@gmail.com";
		final String SUBJECT = "Password Reset for Expert-works";

		Map<String, Object> model = new HashMap<>();
		model.put("user", email.getUsername());
		model.put("userid", email.getLoginId());
		model.put("password", email.getPassword());

		Configuration fmConfiguration = freeMarkerConfigurationFactoryBean.createConfiguration();
		Template template = fmConfiguration.getTemplate("email_template/password_reset.html");
		String HTMLBODY = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

		System.out.println("======================html===========================");
		// System.out.println(HTMLBODY);

		final String TEXTBODY = "This email was sent from <a href='https://www.expert-works.com'>\"";

		SendEmailRequest request = new SendEmailRequest()
				.withDestination(new Destination().withToAddresses(email.getTo()))
				.withMessage(new com.amazonaws.services.simpleemail.model.Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY))
								.withText(new Content().withCharset("UTF-8").withData(TEXTBODY)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);
		client.sendEmail(request);

	}

	public void sendCredentailsMessagev1(EmailDTO email) throws Exception {

		// final String FROM = "Sales@expert-works.com";
		final String FROM = welcomeEmailFrom;
		final String SUBJECT = "Welcome to Expert Works";

		Map<String, Object> model = new HashMap<>();
		model.put("user", email.getUsername());
		model.put("userid", email.getLoginId());
		model.put("password", email.getPassword());
		Configuration fmConfiguration = freeMarkerConfigurationFactoryBean.createConfiguration();
		Template template = fmConfiguration.getTemplate("email_template/admin_email.html");
		String HTMLBODY = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

		final String TEXTBODY = "This email was sent from <a href='https://www.expert-works.com'>\"";
		logger.info("Sending mail to :" + email.getTo());
		SendEmailRequest request = new SendEmailRequest()
				.withDestination(new Destination().withToAddresses(email.getTo()))
				.withMessage(new com.amazonaws.services.simpleemail.model.Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY))
								.withText(new Content().withCharset("UTF-8").withData(TEXTBODY)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);
		client.sendEmail(request);

	}

	public void sendContactUsEMailv1(EmailDTO email) throws Exception {

		// final String FROM = "Sales@expert-works.com";
		// final String FROM = "Sales Team <Sales@expert-works.com>";
		final String FROM = salesEmailFrom;
		final String SUBJECT = "Thanks for contacting Expert-Works ";

		Map<String, Object> model = new HashMap<>();
		model.put("user", email.getUsername());

		Configuration fmConfiguration = freeMarkerConfigurationFactoryBean.createConfiguration();
		Template template = fmConfiguration.getTemplate("email_template/more_details_email.html");
		String HTMLBODY = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

		System.out.println("======================html===========================");
		// System.out.println(HTMLBODY);

		final String TEXTBODY = "This email was sent from <a href='https://www.expert-works.com'>\"";

		SendEmailRequest request = new SendEmailRequest()
				.withDestination(new Destination().withToAddresses(email.getTo()))
				.withMessage(new com.amazonaws.services.simpleemail.model.Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY))
								.withText(new Content().withCharset("UTF-8").withData(TEXTBODY)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);
		client.sendEmail(request);

	}

	public void sendSalesEMail(EmailDTO email, Contact contact) {

		final String FROM = salesEmailFrom;
		//final String to = email.getTo();
		final String to = salesEmailFrom;
		final String SUBJECT = "New Contact";
		final String HTMLBODY = "Hi Team, <br>" + "<br>" + "Please find below new contact."
				+ "<b> </b> : " + "<i>" + this.converttoHTML(contact) + "</i><br>"
				+ "<p>This email was sent from <a href='https://www.expert-works.com'>" + "expert-works.com</a> "
				+ "<br><br><br> Thanks,<br> Expert Works Team.</a> ";
		final String TEXTBODY = "This email was sent from <a href='https://www.expert-works.com'>\"";

		this.converttoHTML(contact);
		SendEmailRequest request = new SendEmailRequest().withDestination(new Destination().withToAddresses(to))
				.withMessage(new com.amazonaws.services.simpleemail.model.Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY))
								.withText(new Content().withCharset("UTF-8").withData(TEXTBODY)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);
		client.sendEmail(request);

	}

	public String converttoHTML(Contact p) {

		StringBuilder sb = new StringBuilder();

		sb.append("<table border='1' style='border-collapse:collapse'>");
		sb.append("<th> ContactId </th>");
		sb.append("<th> Firstname </th>");
		sb.append("<th> Lastname </th>");
		sb.append("<th> Mobile </th>");
		sb.append("<th> Company </th>");
		sb.append("<th> Email </th>");
		sb.append("<th> Message </th>");

		sb.append("<tr>");
		sb.append("<td> " + p.getContactId() + " </td>");
		sb.append("<td> " + p.getFirstname() + " </td>");
		sb.append("<td> " + p.getLastname() + " </td>");
		sb.append("<td> " + p.getMobile() + " </td>");
		sb.append("<td> " + p.getCompany() + " </td>");
		sb.append("<td> " + p.getEmail() + " </td>");
		sb.append("<td> " + p.getMessage() + " </td>");

		sb.append("</tr>");

		sb.append("</tr>");
		sb.append("</table>");

		System.out.println("html : " + sb.toString());
		return sb.toString();
	}

}
