package com.expertworks.lms.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


import com.expertworks.lms.http.BaseResponse;
import com.expertworks.lms.http.CoursesResponse;
import com.expertworks.lms.model.Courses;
import com.expertworks.lms.model.VideoLink;
import com.expertworks.lms.repository.CoursesRepository;
import com.expertworks.lms.util.AuthTokenDetails;
import com.expertworks.lms.util.JwtUtil;

//https://github.com/dailycodebuffer/Spring-MVC-Tutorials/tree/master/DynanoDb-SpringBoot-Demo/src/main/java/com/dailycodebuffer

@RestController
@Component
public class CoursesController {

	@Autowired
	private CoursesRepository coursesRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@GetMapping("/samplecourse/{teamId}")
	public ResponseEntity<Courses> saveCourses(@PathVariable String teamId) {

		Courses courses = new Courses();

		courses.setTeamId("teamId5");
		courses.setCourseId("courseId5");
		courses.setS3folder("s3folder");

		VideoLink videoLink = new VideoLink();
		VideoLink videoLink1 = new VideoLink();

		videoLink.setUrl("https://d3s24np0er9fug.cloudfront.net/sample-mp4-file.mp4");
		videoLink.setType("mp4");

		videoLink1.setUrl("https://d3s24np0er9fug.cloudfront.net/sample-mp4-file.mp4");
		videoLink1.setType("mp4");

		List<VideoLink> videoLinks = new ArrayList();
		videoLinks.add(videoLink);
		videoLinks.add(videoLink1);

		courses.setVideoLinks(videoLinks);

		courses = coursesRepository.save(courses);

		HttpHeaders responseHeaders = new HttpHeaders();
		return new ResponseEntity<>(courses, responseHeaders, HttpStatus.OK);

	}

	@PostMapping("/courses")
	public Courses saveCourses(@RequestBody Courses courses) {
		return coursesRepository.save(courses);
	}

	@GetMapping("/courses/{teamId}")
	public CoursesResponse getCourses(@PathVariable("teamId") String teamId) {

		CoursesResponse coursesResponse = null;
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
			teamId = ((AuthTokenDetails) credentials).getTeamId();

		}

		System.out.println("UserName : " + username);
		System.out.println("teamId : " + teamId);

		coursesResponse = coursesRepository.getTeamCourses(teamId).toCourseResponse();

		if (coursesResponse != null) {
			coursesResponse.setResponseCode(BaseResponse.ResponseCode.SUCCESS);
		} else {
			coursesResponse = new CoursesResponse();
			coursesResponse.setResponseCode(BaseResponse.ResponseCode.FAIL);
		}

		return coursesResponse;
	}

	@GetMapping("/courses/{teamId}/{courseId}")
	public Courses getCourses(@PathVariable("teamId") String teamId, @PathVariable("courseId") String courseId) {

		System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();

		String username = null;
		String partnerId = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		if (credentials instanceof AuthTokenDetails) {
			partnerId = ((AuthTokenDetails) credentials).getPartnerId();

		} else {
			username = credentials.toString();
		}

		System.out.println("PartnerId : " + partnerId);
		return coursesRepository.getTeamCourses(teamId, courseId);
	}

	@DeleteMapping("/courses/{id}")
	public String deleteEmployee(@PathVariable("id") String teamId) {
		return coursesRepository.delete(teamId);
	}

	@PutMapping("/courses/{id}")
	public String updateEmployee(@PathVariable("id") String teamId, @RequestBody Courses courses) {
		return coursesRepository.update(teamId, courses);
	}

}
