package com.expertworks.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.expertworks.lms.http.ApiResponse;
import com.expertworks.lms.model.UserDetail;
import com.expertworks.lms.repository.UserDetailsRepository;

@RestController
public class UserController {

	public static final String SUCCESS = "success";
	public static final String status = "success";


	@Value("${aws.cloudfront.distributiondomain}")
	private String distributionDomain;


	
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	

	@CrossOrigin
	@PostMapping("/video/status")
	public ApiResponse setVideoStatus(@RequestBody UserDetail userDetail) throws Exception {
		
		String courseId = userDetail.getCourseId();
		String userId = userDetail.getUserId();
		String vid = userDetail.getVid();
			
		userDetail.setSk("C#"+courseId+"#"+vid);
		userDetailsRepository.save(userDetail);
		return new ApiResponse(HttpStatus.OK, SUCCESS, userDetail);

	}
	
	@CrossOrigin
	@GetMapping("/video/status/{userId}/{courseId}")
	public ApiResponse getVideoStatus(@PathVariable("userId") String userId,@PathVariable("courseId") String courseId) throws Exception {
		
		System.out.println("userId :"+  userId);
		System.out.println("courseId :"+courseId);
		List<UserDetail> userDetailsList =  userDetailsRepository.get(userId,"C#"+courseId);
		return new ApiResponse(HttpStatus.OK, SUCCESS, userDetailsList);

	}
	
//	@CrossOrigin
//	@GetMapping("/video/{vid}")
//	public ApiResponse getCourses(@PathVariable("vid") String contactId) {
//
//    	List<Contact> list  = contactRepository.get(contactId);
//		//List list = coursesRepository.getmy1Courses(courseId,"S#1"); 
//		return new ApiResponse(HttpStatus.OK, SUCCESS, list);
//	}
//	
	

}
