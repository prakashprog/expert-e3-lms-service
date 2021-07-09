package com.expertworks.lms.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.expertworks.lms.http.ApiResponse;
import com.expertworks.lms.http.CoursesDTO;
import com.expertworks.lms.http.CoursesDetailsDTO;
import com.expertworks.lms.http.VideoLinkDTO;
import com.expertworks.lms.model.Courses;
import com.expertworks.lms.model.UserDetail;
import com.expertworks.lms.repository.CoursesRepository;
import com.expertworks.lms.repository.UserDetailsRepository;
import com.expertworks.lms.util.AuthTokenDetails;
import com.expertworks.lms.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

//https://github.com/dailycodebuffer/Spring-MVC-Tutorials/tree/master/DynanoDb-SpringBoot-Demo/src/main/java/com/dailycodebuffer
@CrossOrigin(origins = "*")
@RestController

public class CoursesController {

	public static final String SUCCESS = "success";
	
	
	@Value("${aws.cloudfront.distributiondomain}")
	private String distributiondomain;
	
	@Value("${aws.cloudfront.protocol}")
	private String distributionprotocol;
	

	@Autowired
	private CoursesRepository coursesRepository;

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private JwtUtil jwtUtil;

	
	@PostMapping("/courses")
	@CrossOrigin
	public Courses saveCourses(@RequestBody Courses courses) {
		return coursesRepository.save(courses);
	}

	// get all courses
	
	@GetMapping("/courses")
	@CrossOrigin
	public ApiResponse getCourses() {
		
		System.out.println("Request Recievd in getCourses");

		List<Courses> courseList = coursesRepository.getAllCourses();
		return new ApiResponse(HttpStatus.OK, SUCCESS, courseList);
	}

	// get all courses
	
	@GetMapping("/public/courses")
	@CrossOrigin
	public ApiResponse getallCourses() {

		List<Courses> courseList = coursesRepository.getAllCourses();
		return new ApiResponse(HttpStatus.OK, SUCCESS, courseList);
	}

	
	@GetMapping("/courses/{courseId}")
	@CrossOrigin
	public ApiResponse getCourses(@PathVariable("courseId") String courseId) {

		Courses courses = null;
		System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();

		System.out.println("credentials :" + credentials);

		String username = null;
		String teamId = null;
		String userId = null;

		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		if (credentials instanceof AuthTokenDetails) {
			teamId = ((AuthTokenDetails) credentials).getTeamId();
			userId = ((AuthTokenDetails) credentials).getSub();

		}

		System.out.println("UserName : " + username);
		System.out.println("courseId : " + courseId);
		System.out.println("teamId : " + teamId);
		System.out.println("sub : " + userId);

		List<Courses> list = coursesRepository.getCourses(courseId);
		List<CoursesDTO> courseDTOList = new ArrayList();

		for (Courses item : list) {
			CoursesDTO courseDTO = item.toCourseDTO();
			courseDTOList.add(courseDTO);
		}

		Collections.sort(courseDTOList);
		return new ApiResponse(HttpStatus.OK, SUCCESS, courseDTOList);
	}

	
	@GetMapping("/tmp/courses/{courseId}")
	@CrossOrigin
	public ApiResponse gettmpCourses(@PathVariable("courseId") String courseId) {
		
		System.out.println("Request recived in gettmpCourses");

		System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
		System.out.println("credentials :" + credentials);

		String username = null;
		String teamId = null;
		String userId = null;

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object details = authentication.getDetails();
		if (details instanceof OAuth2AuthenticationDetails) {
			OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) details;
			System.out.println(":::==" + oAuth2AuthenticationDetails.getTokenValue());
			Claims claims = jwtUtil.extractAllClaims(oAuth2AuthenticationDetails.getTokenValue());
			System.out.println("--" + claims.get("teamId"));
			teamId = claims.get("teamId", String.class);
			userId = claims.get("userId", String.class);
		}

		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		if (credentials instanceof AuthTokenDetails) {
			teamId = ((AuthTokenDetails) credentials).getTeamId();
			userId = ((AuthTokenDetails) credentials).getSub();

		}

		System.out.println("UserName : " + username);
		System.out.println("courseId : " + courseId);
		System.out.println("teamId : " + teamId);
		System.out.println("sub : " + userId);

		// get all rows start with S#
		List<Courses> list = coursesRepository.getCourses(courseId);
		List<CoursesDTO> courseDTOList = new ArrayList();

		for (Courses item : list) {
			CoursesDTO courseDTO = item.toCourseDTO();
			courseDTOList.add(courseDTO);
		}

		Collections.sort(courseDTOList);

		/**
		 * -----to get percentage of course completed---userId and start with
		 * C#courseId------
		 **/
		List<UserDetail> userDetailList = userDetailsRepository.get(userId, "C#" + courseId);
		System.out.println("userId " + userId + courseId);
		int videosCount = courseDTOList.size(); // as each section has one video
		int videoscompleted = userDetailList.size();

		for (UserDetail userDetail : userDetailList) {
			for (CoursesDTO section : courseDTOList) {
				if (section.getSk().equalsIgnoreCase(userDetail.getVid())) {
					section.setCompleted(true);
				}
			}
		}

		System.out.println("videosCount :" + videosCount);
		System.out.println("videoscompleted :" + videoscompleted);
		float percentage = 0;
		if (videoscompleted > 0) {
			percentage = (float) videoscompleted / videosCount;
		}
		/** ------------to get percentage of course completed---------------------/ **/

		List<Courses> coursesMetaList = coursesRepository.getMetaDetailsCourses(courseId);
		Courses courselevel = coursesMetaList.get(0);
		CoursesDetailsDTO coursesDetailsDTO = new CoursesDetailsDTO();
		coursesDetailsDTO.setSections(courseDTOList);
		coursesDetailsDTO.setPercentage(percentage);
		coursesDetailsDTO.setLevel(courselevel.getLevel());
		coursesDetailsDTO.setType(courselevel.getType());
		coursesDetailsDTO.setCourseId(courseId);
		coursesDetailsDTO.setDescription(courselevel.getDescription());
		coursesDetailsDTO.setLeveldesc(courselevel.getLeveldesc());
		coursesDetailsDTO.setIncludes(courselevel.getIncludes());

		return new ApiResponse(HttpStatus.OK, SUCCESS, coursesDetailsDTO);
	}

	@CrossOrigin
	@GetMapping("/public/courses/{courseId}")
	public ApiResponse getCoursesNoAuthorization(@PathVariable("courseId") String courseId) {

		Courses courses = null;
		System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();

		String username = null;
		String teamId = null;

		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		if (credentials instanceof AuthTokenDetails) {
			teamId = ((AuthTokenDetails) credentials).getTeamId();

		}

		System.out.println("UserName : " + username);
		System.out.println("courseId : " + courseId);
		System.out.println("teamId : " + teamId);

		List<Courses> sectionList = coursesRepository.getCourses(courseId);
		List<CoursesDTO> courseDTOList = new ArrayList();

		for (Courses item : sectionList) {
			CoursesDTO courseDTO = item.toCourseDTO();
			courseDTOList.add(courseDTO);
		}

		Collections.sort(courseDTOList);
		List<Courses> coursesMetaList = coursesRepository.getMetaDetailsCourses(courseId);
		String s3folder = coursesMetaList.get(0).getS3folder();
		System.out.println("s3folder : "  + s3folder);
		
		for (CoursesDTO section : courseDTOList) {

			List<VideoLinkDTO> videolinkList = section.getVideoLinks();

			videolinkList = videolinkList.stream().map(v -> {				
				String url = v.getUrl();
				System.out.println("url : " + url);
				String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
				System.out.println("fileName : " + fileName);
				v.setUrl(distributionprotocol+"://"+distributiondomain+"/"+s3folder+"/"+fileName);
				return v;
			}).collect(Collectors.toList());

			if (section == courseDTOList.get(0) || section == courseDTOList.get(1) || section == courseDTOList.get(2)) {

				for (int i = 0; i < videolinkList.size(); i++) {
					if (i != 0) {
						VideoLinkDTO videoLink = videolinkList.get(i);
						videoLink.setUrl(null);
					}
				}

			} else
				for (VideoLinkDTO videoLink : videolinkList)
					videoLink.setUrl(null);

		}

		
		Courses courselevel = coursesMetaList.get(0);
		System.out.println("getS3folder  : " + courselevel.getS3folder());
		CoursesDetailsDTO coursesDetailsDTO = new CoursesDetailsDTO();
		coursesDetailsDTO.setSections(courseDTOList);
		coursesDetailsDTO.setLevel(courselevel.getLevel());
		coursesDetailsDTO.setType(courselevel.getType());
		coursesDetailsDTO.setCourseId(courseId);
		coursesDetailsDTO.setDescription(courselevel.getDescription());
		coursesDetailsDTO.setLeveldesc(courselevel.getLeveldesc());
		coursesDetailsDTO.setIncludes(courselevel.getIncludes());

		return new ApiResponse(HttpStatus.OK, SUCCESS, coursesDetailsDTO);
	}

	@CrossOrigin
	@GetMapping("/courses/meta/{courseId}")
	public ApiResponse getMetaDeatils(@PathVariable("courseId") String courseId) {

		List<Courses> list = coursesRepository.getMetaDetailsCourses(courseId);
		return new ApiResponse(HttpStatus.OK, SUCCESS, list);
	}

	@CrossOrigin
	@DeleteMapping("/courses/{courseId}")
	public String deleteCourse(@PathVariable("courseId") String courseId) {
		return coursesRepository.delete(courseId);
	}

	@CrossOrigin
	@PutMapping("/courses/{courseId}")
	public String updateCourse(@PathVariable("courseId") String courseId, @RequestBody Courses courses) {
		return coursesRepository.update(courseId, courses);
	}

}
