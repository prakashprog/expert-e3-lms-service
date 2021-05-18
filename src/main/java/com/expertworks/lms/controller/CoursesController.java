package com.expertworks.lms.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

//https://github.com/dailycodebuffer/Spring-MVC-Tutorials/tree/master/DynanoDb-SpringBoot-Demo/src/main/java/com/dailycodebuffer

@RestController
@Component
public class CoursesController {

	public static final String SUCCESS = "success";

	@Autowired
	private CoursesRepository coursesRepository;

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@CrossOrigin
	@PostMapping("/courses")
	public Courses saveCourses(@RequestBody Courses courses) {
		return coursesRepository.save(courses);
	}

	// get all courses
	@CrossOrigin
	@GetMapping("/courses")
	public ApiResponse getCourses() {

		List<Courses> courseList = coursesRepository.getAllCourses();
		return new ApiResponse(HttpStatus.OK, SUCCESS, courseList);
	}

	@CrossOrigin
	@GetMapping("/courses/{courseId}")
	public ApiResponse getCourses(@PathVariable("courseId") String courseId) {

		Courses courses = null;
		System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();

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
	
	
	
	@CrossOrigin
	@GetMapping("/tmp/courses/{courseId}")
	public ApiResponse gettmpCourses(@PathVariable("courseId") String courseId) {

		
		System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();

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
        
		//get all rows start with S#
		List<Courses> list = coursesRepository.getCourses(courseId);
		List<CoursesDTO> courseDTOList = new ArrayList();

		for (Courses item : list) {
			CoursesDTO courseDTO = item.toCourseDTO();
			courseDTOList.add(courseDTO);
		}

		Collections.sort(courseDTOList);

		/** ------------to get percentage of course completed---------------------/ **/
		List<UserDetail> userDetailList = userDetailsRepository.get(userId, "C#" + courseId);
		int videosCount = courseDTOList.size(); // as each section has one video
		int videoscompleted = userDetailList.size();
		System.out.println("videosCount :" + videosCount);
		System.out.println("videoscompleted :" + videoscompleted);
		float percentage=0;
		if (videoscompleted > 0) {
          	percentage = (float) videoscompleted / videosCount;	
		}
		/** ------------to get percentage of course completed---------------------/ **/
		
		List<Courses> coursesMetaList = coursesRepository.getMetaDetailsCourses(courseId);
		Courses courselevel = coursesMetaList.get(0);
		CoursesDetailsDTO coursesDetailsDTO  = new CoursesDetailsDTO();
		coursesDetailsDTO.setSections(courseDTOList);
		coursesDetailsDTO.setPercentage(percentage);
		coursesDetailsDTO.setLevel(courselevel.getLevel());
		coursesDetailsDTO.setType(courselevel.getType());
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

		for (CoursesDTO section : courseDTOList) {

			List<VideoLinkDTO> videolinkList = section.getVideoLinks();

			if (section == courseDTOList.get(0)) {

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

		return new ApiResponse(HttpStatus.OK, SUCCESS, courseDTOList);
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
