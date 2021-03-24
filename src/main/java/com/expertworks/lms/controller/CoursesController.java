package com.expertworks.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.expertworks.lms.http.ApiResponse;
import com.expertworks.lms.model.Courses;
import com.expertworks.lms.repository.CoursesRepository;
import com.expertworks.lms.util.AuthTokenDetails;

//https://github.com/dailycodebuffer/Spring-MVC-Tutorials/tree/master/DynanoDb-SpringBoot-Demo/src/main/java/com/dailycodebuffer

@RestController
@Component
public class CoursesController {
	
	public static final String SUCCESS = "success";

	@Autowired
	private CoursesRepository coursesRepository;




	@PostMapping("/courses")
	public Courses saveCourses(@RequestBody Courses courses) {
		return coursesRepository.save(courses);
	}
	
	// get all courses
	@GetMapping("/courses/")
	public ApiResponse getCourses() {

		List<Courses>  courseList = coursesRepository.getCourses();
		return new ApiResponse(HttpStatus.OK, SUCCESS, courseList);
	}
		

	@GetMapping("/courses/{courseId}")
	public ApiResponse getCourses(@PathVariable("courseId") String courseId) {

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

		courses = coursesRepository.getCourses(courseId);

	

		return new ApiResponse(HttpStatus.OK, SUCCESS, courses);
	}

	

	@DeleteMapping("/courses/{courseId}")
	public String deleteCourse(@PathVariable("courseId") String courseId) {
		return coursesRepository.delete(courseId);
	}

	@PutMapping("/courses/{courseId}")
	public String updateCourse(@PathVariable("courseId") String courseId, @RequestBody Courses courses) {
		return coursesRepository.update(courseId, courses);
	}

}
