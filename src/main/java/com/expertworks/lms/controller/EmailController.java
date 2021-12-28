package com.expertworks.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.expertworks.lms.http.ApiResponse;
import com.expertworks.lms.http.EmailDTO;
import com.expertworks.lms.http.SendReminderDTO;
import com.expertworks.lms.service.EmailService;

//https://stackoverflow.com/questions/16232833/how-to-respond-with-http-400-error-in-a-spring-mvc-responsebody-method-returnin

@RestController
@Component
public class EmailController {

	public static final String SUCCESS = "success";

	@Autowired
	private EmailService emailService;

	@CrossOrigin
	@GetMapping("/testmail")
	public ApiResponse testMail() throws Exception {

		EmailDTO email = new EmailDTO();
		email.setTo("prakash.s@expert-works.com");
		email.setUsername("mahesh");
		email.setLoginId("1234");
		email.setPassword("testpwd");

		emailService.sendResetCredentailsEMailv1(email);
		return new ApiResponse(HttpStatus.OK, SUCCESS, "email response");



	}

	@CrossOrigin
	@PostMapping("/sendReminder")
	public ApiResponse sendReminder(@RequestBody SendReminderDTO sendReminderDTO) {
		EmailDTO email = new EmailDTO();
		email.from = sendReminderDTO.getFromEmail();
		email.to = sendReminderDTO.getUserEmail();
		//write code to send email with appropriate email template.
		emailService.sendRemainderEmail(sendReminderDTO);
		return new ApiResponse(HttpStatus.OK, SUCCESS, sendReminderDTO);

	}


}
