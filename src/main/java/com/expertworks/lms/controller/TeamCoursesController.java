package com.expertworks.lms.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.expertworks.lms.enums.CustomerType;
import com.expertworks.lms.enums.SubscriptionStatus;
import com.expertworks.lms.http.ApiResponse;
import com.expertworks.lms.http.CoursesDTO;
import com.expertworks.lms.model.Courses;
import com.expertworks.lms.model.Subscriptions;
import com.expertworks.lms.model.TeamCourses;
import com.expertworks.lms.repository.CoursesRepository;
import com.expertworks.lms.repository.TeamCoursesRepository;
import com.expertworks.lms.service.SubscriptionsService;
import com.expertworks.lms.util.AuthTokenDetails;
import com.expertworks.lms.util.JwtUtil;

import io.jsonwebtoken.Claims;

//https://github.com/dailycodebuffer/Spring-MVC-Tutorials/tree/master/DynanoDb-SpringBoot-Demo/src/main/java/com/dailycodebuffer

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("team-courses")
public class TeamCoursesController {

	public static final String SUCCESS = "success";
	public static final String ALL_COURSES = "ALL";

	@Autowired
	private TeamCoursesRepository teamCoursesRepository;

	@Autowired
	private CoursesRepository coursesRepository;

	@Autowired
	private SubscriptionsService subscriptionsService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/")
	@CrossOrigin
	public TeamCourses saveCourses(@RequestBody TeamCourses teamCourses) {

		System.out.println("Request recieved for saveCourses");
		return teamCoursesRepository.save(teamCourses);
	}

	/**
	 * Get all courses for a Team_courses Table
	 *
	 * @return
	 */
	@CrossOrigin
	@GetMapping("/all")
	public ApiResponse getTeamCourses() {

		System.out.println("Request received");
		List<TeamCourses> courseList = teamCoursesRepository.getTeamCourses();
		return new ApiResponse(HttpStatus.OK, SUCCESS, courseList);
	}

	@CrossOrigin(origins = "*")
	@GetMapping
	@RequestMapping(value = "/")
	public ApiResponse getCourses() {

		List<TeamCourses> teamCourses = null;
		List<CoursesDTO> coursesDTOList = new ArrayList();
		String teamIdinToken = null;
		String groupId = null;
		String userId = null;
		System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();

		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		if (credentials instanceof AuthTokenDetails) {
			teamIdinToken = ((AuthTokenDetails) credentials).getTeamId();
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object details = authentication.getDetails();

		if (details instanceof OAuth2AuthenticationDetails) {
			OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) details;
			System.out.println(":::==" + oAuth2AuthenticationDetails.getTokenValue());
			Claims claims = jwtUtil.extractAllClaims(oAuth2AuthenticationDetails.getTokenValue());
			System.out.println("--" + claims.get("teamId"));
			teamIdinToken = claims.get("teamId", String.class);
			userId = claims.get("userId", String.class);
			groupId = claims.get("groupId", String.class);
		}
		//========================================

		Subscriptions dbsubscriptions = subscriptionsService.query(CustomerType.B2B +"_" + groupId);
		System.out.println("dbsubscriptions : "+ dbsubscriptions);

        if(dbsubscriptions==null)
        	throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No Rows/Subscription found for groupId:"+groupId);
    	if (dbsubscriptions != null && dbsubscriptions.getSubscriptionStatus() != SubscriptionStatus.SUBSCRIBED)
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Subscription Satus for groupId:"+groupId+"["+dbsubscriptions.getSubscriptionStatus()+"]");

		//============================================

		System.out.println("teamIdinToken : " + teamIdinToken +"; groupId : "+groupId);
		teamCourses = teamCoursesRepository.getTeamCourses(teamIdinToken);


		/*
		 * if (teamCourses != null && teamCourses.size() == 1 &&
		 * teamCourses.get(0).getCourseId().equalsIgnoreCase(ALL_COURSES)) {
		 * List<Courses> allCourses = coursesRepository.getAllCourses(); return new
		 * ApiResponse(HttpStatus.OK, SUCCESS, allCourses); }
		 */


		for (TeamCourses teamCourse : teamCourses) {

//			System.out.println("TeamId : " + teamIdinToken + " , CourseId : " + teamCourse.getCourseId() + ",total:"
//					+ teamCourses.size());

			// List<Courses> list =
			// coursesRepository.getMetaDetailsCourses(teamCourse.getCourseId());
			Courses course = coursesRepository.getCoursesMetaDetails(teamCourse.getCourseId());

			// if (list != null && list.size() > 0) {
			if (course != null) {
				CoursesDTO coursesDTO = course.toCourseDTO();
				coursesDTOList.add(coursesDTO);
			}
		}

		List<String> imageList = coursesDTOList.stream().map(p -> p.getImg()).collect(Collectors.toList());
		//System.out.println(imageList);
		coursesDTOList.stream().map(p -> p.getImg()).forEach(p -> System.out.println(p));
		return new ApiResponse(HttpStatus.OK, SUCCESS, coursesDTOList);
	}

	@CrossOrigin
	@GetMapping("/{teamId}")
	public ApiResponse getCourses(@PathVariable("teamId") String teamId) {

		List<TeamCourses> teamCourses = null;
		System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();

		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		if (credentials instanceof AuthTokenDetails) {
			String teamIdinToken = ((AuthTokenDetails) credentials).getTeamId();
			System.out.println("teamIdinToken : " + teamIdinToken);

		}
		teamCourses = teamCoursesRepository.getTeamCourses(teamId);
		return new ApiResponse(HttpStatus.OK, SUCCESS, teamCourses);
	}

	@CrossOrigin
	@GetMapping("/teams/{courseId}")
	public ApiResponse getTeams(@PathVariable("courseId") String courseId) {

		List<TeamCourses> teamCourses = null;
		System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();

		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		if (credentials instanceof AuthTokenDetails) {
			String teamIdinToken = ((AuthTokenDetails) credentials).getTeamId();
			System.out.println("teamIdinToken : " + teamIdinToken);

		}
		teamCourses = teamCoursesRepository.getTeamCoursesonGSI(courseId);
		return new ApiResponse(HttpStatus.OK, SUCCESS, teamCourses);
	}

}
