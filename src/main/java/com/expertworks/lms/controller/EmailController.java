package com.expertworks.lms.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import com.expertworks.lms.http.ApiResponse;
import com.expertworks.lms.http.EmailDTO;
import com.expertworks.lms.http.TransferDTO;
import com.expertworks.lms.http.UserDTO;
import com.expertworks.lms.model.Group;
import com.expertworks.lms.model.Partner;
import com.expertworks.lms.model.Team;
import com.expertworks.lms.model.User;
import com.expertworks.lms.repository.GroupRepository;
import com.expertworks.lms.repository.PartnerRepository;
import com.expertworks.lms.repository.TeamRepository;
import com.expertworks.lms.repository.UserRepository;
import com.expertworks.lms.service.EmailService;
import com.expertworks.lms.util.TokenUtil;

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


}
