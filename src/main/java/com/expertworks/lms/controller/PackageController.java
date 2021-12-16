package com.expertworks.lms.controller;

import java.util.List;

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
import com.expertworks.lms.model.Pack;
import com.expertworks.lms.repository.PackageRepository;
import com.expertworks.lms.service.EmailService;



@RestController
@Component

public class PackageController {

	public static final String SUCCESS = "success";

	@Autowired
	private EmailService emailService;

	@Autowired
	private PackageRepository packageRepository;

	@CrossOrigin
	@PostMapping("/packages")
	public ApiResponse save(@RequestBody Pack pack) {
           	return new ApiResponse(HttpStatus.OK, SUCCESS, packageRepository.save(pack));
	}

	@CrossOrigin
	@GetMapping("/packages")
	public ApiResponse getAll() {

		List<Pack> list = packageRepository.getAll();
		return new ApiResponse(HttpStatus.OK, SUCCESS, list);
	}

	@CrossOrigin
	@GetMapping("/packages/{packageId}")
	public ApiResponse getPackage(@PathVariable("packageId") String packageId) {
	    Pack pack = packageRepository.getPack(packageId);
		return new ApiResponse(HttpStatus.OK, SUCCESS, pack);
	}

	@CrossOrigin
	@DeleteMapping("/packages/{packageId}")
	public String deletePackage(@PathVariable("contactId") String contactId) {
		return packageRepository.delete(contactId);
	}

	@CrossOrigin
	@PutMapping("/packages/{packageId}")
	public String updateCourse(@PathVariable("courseId") String contactId, @RequestBody Contact contact) {
		return packageRepository.update(contactId, contact);
	}






}
