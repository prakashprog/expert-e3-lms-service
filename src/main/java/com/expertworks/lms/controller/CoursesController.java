package com.expertworks.lms.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.expertworks.lms.aspect.LogExecutionTime;
import com.expertworks.lms.http.ApiResponse;
import com.expertworks.lms.http.CoursesDTO;
import com.expertworks.lms.http.CoursesDetailsDTO;
import com.expertworks.lms.http.ResourceLinkDTO;
import com.expertworks.lms.http.VideoLinkDTO;
import com.expertworks.lms.model.Courses;
import com.expertworks.lms.model.ResourceLink;
import com.expertworks.lms.model.UserDetail;
import com.expertworks.lms.repository.CoursesRepository;
import com.expertworks.lms.repository.UserDetailsRepository;
import com.expertworks.lms.util.AuthTokenDetails;
import com.expertworks.lms.util.CurrencyUtil;
import com.expertworks.lms.util.JwtUtil;

import io.jsonwebtoken.Claims;

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
	
	@Autowired
	private HttpServletRequest request;

	@LogExecutionTime
	@PostMapping("/courses")
	@CrossOrigin
	public Courses saveCourses(@RequestBody Courses courses) {
		//return coursesRepository.save(courses);
		return courses;
	}

	// get all courses
	
	@GetMapping("/courses")
	@CrossOrigin
	public ApiResponse getCourses() {
		
		System.out.println("Request Recievd in getCourses");
		List<Courses> courseList = coursesRepository.getAllCourses();
		
		for (Courses courses :courseList)
		{	if(NumberUtils.isCreatable(courses.getPrice()) && NumberUtils.isCreatable(courses.getActualPrice()))
			courses.setCurrencies(
					CurrencyUtil.getCurrencies(Double.parseDouble(courses.getPrice()),Double.parseDouble(courses.getActualPrice())));

		}
		
		return new ApiResponse(HttpStatus.OK, SUCCESS, courseList);
	}

	// get all courses
	//@LogExecutionTime
	@GetMapping("/public/courses")
	@CrossOrigin
	public ApiResponse getallCourses() {

		List<Courses> courseList = coursesRepository.getAllCourses();
		System.out.println("request.getRemoteAddr() :  "+ request.getRemoteAddr());
		for (Courses courses :courseList)
		{	if(NumberUtils.isCreatable(courses.getPrice()))
			if(NumberUtils.isCreatable(courses.getPrice()) && NumberUtils.isCreatable(courses.getActualPrice()))
				courses.setCurrencies(
						CurrencyUtil.getCurrencies(Double.parseDouble(courses.getPrice()),Double.parseDouble(courses.getActualPrice())));

		}
		
		return new ApiResponse(HttpStatus.OK, SUCCESS, courseList);
	}

	
	//@LogExecutionTime
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

		List<Courses> list = coursesRepository.getCourseSections(courseId);
		List<CoursesDTO> courseDTOList = new ArrayList();

		for (Courses item : list) {
			CoursesDTO courseDTO = item.toCourseDTO();
			courseDTOList.add(courseDTO);
		}

		Collections.sort(courseDTOList);
		return new ApiResponse(HttpStatus.OK, SUCCESS, courseDTOList);
	}

	//@LogExecutionTime
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

		//List<Courses> coursesMetaList = coursesRepository.getCoursesMetaDetails(courseId);
		//Courses courselevelData = coursesMetaList.get(0);
		Courses courselevelData = coursesRepository.getCoursesMetaDetails(courseId);
		
		String s3folder = courselevelData.getS3folder();
		System.out.println("s3folder : "  + s3folder);
		
		// get all rows start with S#
		List<Courses> secList = coursesRepository.getCourseSections(courseId);
		List<CoursesDTO> secDTOList = new ArrayList();
		
		for (Courses section : secList) {
			CoursesDTO courseDTO = section.toCourseDTO();
			List videosList = courseDTO.getVideoLinks().stream().map(v-> {
				v.getResourceLinks();
				for(ResourceLinkDTO resourceLinkDTO : v.getResourceLinks()) {
					String link = resourceLinkDTO.getLink();
					String basepath = s3folder.substring(0,s3folder.indexOf("/videos"));
					resourceLinkDTO.setLink(distributionprotocol+"://"+distributiondomain+"/"+basepath+"/docs/"+link);
				}	
				
				return v;
			}).collect(Collectors.toList());
			courseDTO.setVideoLinks(videosList);
			secDTOList.add(courseDTO);
		}

		Collections.sort(secDTOList);
		
		
		//888

	
		/**
		 * -----to get percentage of course completed---userId and start with
		 * C#courseId------
		 **/
		List<UserDetail> userDetailList = userDetailsRepository.get(userId, "C#" + courseId);
		System.out.println("userId " + userId + courseId);
		int videosCount = secDTOList.size(); // as each section has one video
		int videoscompleted = userDetailList.size();

		for (UserDetail userDetail : userDetailList) {
			for (CoursesDTO section : secDTOList) {
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

		
		CoursesDetailsDTO coursesDetailsDTO = new CoursesDetailsDTO();
		coursesDetailsDTO.setSections(secDTOList);
		coursesDetailsDTO.setPercentage(percentage);
		coursesDetailsDTO.setLevel(courselevelData.getLevel());
		coursesDetailsDTO.setType(courselevelData.getType());
		coursesDetailsDTO.setCourseId(courseId);
		coursesDetailsDTO.setDescription(courselevelData.getDescription());
		coursesDetailsDTO.setLeveldesc(courselevelData.getLeveldesc());  
		coursesDetailsDTO.setIncludes(courselevelData.getIncludes());
		coursesDetailsDTO.setOrder(courselevelData.getOrder());
		coursesDetailsDTO.setHours(courselevelData.getHours());
		coursesDetailsDTO.setRating(courselevelData.getRating());
		coursesDetailsDTO.setReviews(courselevelData.getReviews());
		coursesDetailsDTO.setActualPrice(courselevelData.getActualPrice());
		coursesDetailsDTO.setPrice(courselevelData.getPrice());  
		
		System.out.println("NumberUtils.isCreatable ; "+NumberUtils.isCreatable(courselevelData.getPrice()));
		if(NumberUtils.isCreatable(courselevelData.getPrice()))
		{
			coursesDetailsDTO.setCurrencies(CurrencyUtil.getCurrencies(Double.parseDouble(courselevelData.getPrice()),Double.parseDouble(courselevelData.getActualPrice())));
		}

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

		List<Courses> sectionList = coursesRepository.getCourseSections(courseId);
		List<CoursesDTO> courseDTOList = new ArrayList();

		for (Courses section : sectionList) {
			CoursesDTO courseDTO = section.toCourseDTO();
			courseDTOList.add(courseDTO);
		}

		Collections.sort(courseDTOList);
		//List<Courses> coursesMetaList = coursesRepository.getCoursesMetaDetails(courseId);
		Courses courselevel = coursesRepository.getCoursesMetaDetails(courseId);
		String s3folder = courselevel.getS3folder();
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
	
		System.out.println("getS3folder  : " + courselevel.getS3folder());
		CoursesDetailsDTO coursesDetailsDTO = new CoursesDetailsDTO();
		coursesDetailsDTO.setSections(courseDTOList);
		coursesDetailsDTO.setLevel(courselevel.getLevel());
		coursesDetailsDTO.setType(courselevel.getType());
		coursesDetailsDTO.setCourseId(courseId);
		coursesDetailsDTO.setDescription(courselevel.getDescription());
		coursesDetailsDTO.setLeveldesc(courselevel.getLeveldesc());
		coursesDetailsDTO.setIncludes(courselevel.getIncludes());
		coursesDetailsDTO.setOrder(courselevel.getOrder());
		
		coursesDetailsDTO.setHours(courselevel.getHours());
		coursesDetailsDTO.setRating(courselevel.getRating());
		coursesDetailsDTO.setReviews(courselevel.getReviews());
		
		
		coursesDetailsDTO.setActualPrice(courselevel.getActualPrice());
		coursesDetailsDTO.setPrice(courselevel.getPrice());
		
		//coursesDetailsDTO.setCurrencies(courselevel.getCurrencies());
		if(NumberUtils.isCreatable(courselevel.getPrice()) && NumberUtils.isCreatable(courselevel.getActualPrice()))
		coursesDetailsDTO.setCurrencies(
				CurrencyUtil.getCurrencies(Double.parseDouble(courselevel.getPrice()),Double.parseDouble(courselevel.getActualPrice())));

		return new ApiResponse(HttpStatus.OK, SUCCESS, coursesDetailsDTO);
	}

	@CrossOrigin
	@GetMapping("/courses/meta/{courseId}")
	public ApiResponse getMetaDeatils(@PathVariable("courseId") String courseId) {

		Courses course = coursesRepository.getCoursesMetaDetails(courseId);
		return new ApiResponse(HttpStatus.OK, SUCCESS, course);
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
	
	
	@CrossOrigin
	@GetMapping("/courses/meta/{courseId}/{sk}")
	public ApiResponse getRow(@PathVariable("courseId") String courseId,@PathVariable("sk") String sk) {
		System.out.println("row : ");
		Courses row = coursesRepository.getRow(courseId,"S#"+sk);
		System.out.println("row : "+ row);
		
		ResourceLink resourceLink = new ResourceLink();
		resourceLink.setLink("content.pdf");
		resourceLink.setTitle("Course Content");
		resourceLink.setType("pdf");
		
		List<ResourceLink> list = new ArrayList<ResourceLink>();
		list.add(resourceLink);
		
		row.getVideoLinks().get(0).setResourceLinks(list);
		
		 coursesRepository.update(row.getCourseId(),row);
		
		return new ApiResponse(HttpStatus.OK, SUCCESS, row);
	}
	
	
	@CrossOrigin
	@GetMapping("/getremoteip")
	public ApiResponse getRemoteAddr() {
			
		Map map = new HashMap();
		map.put("request.getRemoteAddr()", request.getRemoteAddr());
		map.put("request.getRemoteHost()", request.getRemoteHost());
		return new ApiResponse(HttpStatus.OK, SUCCESS, map);
	}
	
	

}
