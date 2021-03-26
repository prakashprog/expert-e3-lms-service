package com.expertworks.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expertworks.lms.http.ApiResponse;
import com.expertworks.lms.model.Courses;
import com.expertworks.lms.model.TeamCourses;
import com.expertworks.lms.repository.TeamCoursesRepository;
import com.expertworks.lms.util.AuthTokenDetails;

//https://github.com/dailycodebuffer/Spring-MVC-Tutorials/tree/master/DynanoDb-SpringBoot-Demo/src/main/java/com/dailycodebuffer

@RestController
@Component
@RequestMapping("team-courses")
public class TeamController {
	
	public static final String SUCCESS = "success";

	@Autowired
	private TeamCoursesRepository teamCoursesRepository;



	@PostMapping("/")
	public TeamCourses saveCourses(@RequestBody TeamCourses teamCourses) {
		
		System.out.println("Request recieved for saveCourses");
		return teamCoursesRepository.save(teamCourses);
	}
	
	/**
	 * Get all courses for a Team_courses Table
	 * @return
	 */
	@GetMapping("/all")
	public ApiResponse getTeamCourses() {
		
         System.out.println("Request received");
		List<TeamCourses>  courseList = teamCoursesRepository.getTeamCourses();
		return new ApiResponse(HttpStatus.OK, SUCCESS, courseList);
	}
	
	
	@GetMapping("/")
	public ApiResponse getCourses() {

		List<TeamCourses> teamCourses = null;
		String teamIdinToken = null;
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
			System.out.println("teamIdinToken : " + teamIdinToken);

		}
		teamCourses = teamCoursesRepository.getTeamCourses(teamIdinToken);
    	return new ApiResponse(HttpStatus.OK, SUCCESS, teamCourses);
	}
	
	
	
		

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
