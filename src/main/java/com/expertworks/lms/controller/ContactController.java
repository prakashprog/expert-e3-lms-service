package com.expertworks.lms.controller;

import java.util.List;


import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.expertworks.lms.model.Contact;
import com.expertworks.lms.repository.ContactRepository;
import com.expertworks.lms.service.EmailService;

//branch code

@RestController
@Component
public class ContactController {

	public static final String SUCCESS = "success";

	@Autowired
	private EmailService emailService;

	@Autowired
	private ContactRepository contactRepository;

	@CrossOrigin
	@PostMapping("public/contactus")
	public ApiResponse save(@RequestBody Contact contact) {

		System.out.println("Request Recieved in contactus");
		String[] to = { contact.getEmail() };
		JSONObject obj = new JSONObject();
		String name = contact.getFirstname()!=null?contact.getFirstname():"";
		String lastname = contact.getLastname()!=null?contact.getLastname(): "";	
		
		System.out.println("lastname: " + lastname);
		obj.put("name", name +" "+ lastname);
		obj.put("ending", "Thanks");
		//String templateDataJson = "{ \"name\":\"Jack\", \"eom\": \"Tiger\"}";
		emailService.sendEmail(to, obj.toJSONString());

		return new ApiResponse(HttpStatus.OK, SUCCESS, contactRepository.save(contact));
	}

	@CrossOrigin
	@GetMapping("/contactus")
	public ApiResponse getAll() {

		List<Contact> list = contactRepository.getAll();
		return new ApiResponse(HttpStatus.OK, SUCCESS, list);
	}

	@CrossOrigin
	@GetMapping("/contactus/{contactId}")
	public ApiResponse getCourses(@PathVariable("contactId") String contactId) {

		List<Contact> list = contactRepository.get(contactId);
		// List list = coursesRepository.getmy1Courses(courseId,"S#1");
		return new ApiResponse(HttpStatus.OK, SUCCESS, list);
	}

	@CrossOrigin
	@DeleteMapping("/contactus/{contactId}")
	public String deleteCourse(@PathVariable("contactId") String contactId) {
		return contactRepository.delete(contactId);
	}

	@CrossOrigin
	@PutMapping("/contactus/{contactId}")
	public String updateCourse(@PathVariable("courseId") String contactId, @RequestBody Contact contact) {
		return contactRepository.update(contactId, contact);
	}

}
