package com.expertworks.lms.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

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
import com.expertworks.lms.http.EmailDTO;
import com.expertworks.lms.model.Contact;
import com.expertworks.lms.repository.ContactRepository;
import com.expertworks.lms.service.EmailService;

//branch code
//code added in main branch (dev1 code)

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
	public ApiResponse save(@RequestBody @Valid Contact contact) throws Exception {

		System.out.println("Request Recieved in contactus...");
		String[] to = { contact.getEmail() };
		JSONObject obj = new JSONObject();
		String name = contact.getFirstname() != null ? contact.getFirstname() : "";
		String lastname = contact.getLastname() != null ? contact.getLastname() : "";
		// Assert.notNull(contact.getFirstname(), "Name is required"); gives 500 error

		obj.put("name", name + " " + lastname);
		obj.put("ending", "Thanks");
		// String templateDataJson = "{ \"name\":\"Jack\", \"eom\": \"Tiger\"}";
		// emailService.sendEmail(to, obj.toJSONString());

		Contact saved = contactRepository.save(contact);

		EmailDTO email = new EmailDTO();
		email.to = contact.getEmail();
		email.username = name;

		emailService.sendContactUsEMailv1(email);
		System.out.println("saved : " + saved.toString());

		// Send email to sales Team as well

		if (contact.getLoggedinUser() != null)
			emailService.sendSupportEmail(email, saved);
		else
			emailService.sendSalesEmail(email, saved);

		return new ApiResponse(HttpStatus.OK, SUCCESS, saved);
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
		Date date = list.get(0).getCreatedDate();
		SimpleDateFormat mdyFormat = new SimpleDateFormat("MMM-dd-yyyy");
		String mdy = mdyFormat.format(date);
		System.out.println("DateFormat : " + mdy);
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

	@CrossOrigin(origins = "*")
	@PutMapping("/putcall/{contactId}")
	public ApiResponse putCallTest(@PathVariable("contactId") String contactId, @RequestBody Contact contact) {
		return new ApiResponse(HttpStatus.OK, SUCCESS, "Put working...");
	}

}
