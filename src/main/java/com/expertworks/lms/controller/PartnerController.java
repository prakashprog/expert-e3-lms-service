package com.expertworks.lms.controller;

import java.util.ArrayList;
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
import com.expertworks.lms.http.PartnerDTO;
import com.expertworks.lms.model.Group;
import com.expertworks.lms.model.Partner;
import com.expertworks.lms.repository.PartnerRepository;

@RestController
@Component
public class PartnerController {

	public static final String SUCCESS = "success";

	@Autowired
	private PartnerRepository partnerRepository;

	@CrossOrigin
	@PostMapping("/partner")
	public ApiResponse save(@RequestBody Partner partner) {
		partner.setSk("details");
		Partner savedPartner = partnerRepository.save(partner);

		if (partner.getGroupIds() != null && partner.getGroupIds().size() > 0) {
			for (String groupId : partner.getGroupIds()) {
				// Group Id are present
				Partner newRow = new Partner();
				newRow.setPartnerId(savedPartner.getPartnerId());
				newRow.setSk("G#" + groupId);
				partnerRepository.save(newRow);
			}
		}

		return new ApiResponse(HttpStatus.OK, SUCCESS, savedPartner);
	}

	@CrossOrigin
	@GetMapping("/partner")
	public ApiResponse getAll() {

		List<Partner> list = partnerRepository.getAll();
		return new ApiResponse(HttpStatus.OK, SUCCESS, list);
	}

	@CrossOrigin
	@GetMapping("/partner/{partnerId}")
	public ApiResponse get(@PathVariable("partnerId") String partnerId) {

		PartnerDTO partnerDTO = new PartnerDTO();
		
		List<Partner> list = partnerRepository.get(partnerId);
		for (Partner partner : list) {
			if (partner.getSk().equalsIgnoreCase("details")) {
				partnerDTO.setPartnerId(partnerId);
				partnerDTO.setName(partner.getName());
				partnerDTO.setCreatedDate(partner.getCreatedDate());
			} else {
				Group group = new Group();
				group.setGroupId(partner.getGroupId());
				group.setName(partner.getName());
				partnerDTO.getGroups().add(group);

			}
		}
		// List list = coursesRepository.getmy1Courses(courseId,"S#1");
		return new ApiResponse(HttpStatus.OK, SUCCESS, partnerDTO);
	}

	@CrossOrigin
	@DeleteMapping("/partner/{partnerId}")
	public String delete(@PathVariable("contactId") String contactId) {
		return partnerRepository.delete(contactId);
	}

	@CrossOrigin
	@PostMapping("/partner/addGroups/{partnerId}")
	public ApiResponse addGroups(@PathVariable("partnerId") String partnerId, @RequestBody Group group) {

		Partner savedRow = partnerRepository.addGroup(partnerId, group);
		return new ApiResponse(HttpStatus.OK, SUCCESS, savedRow);
	}

	@CrossOrigin
	@PutMapping("/partner/{partnerId}")
	public String update(@PathVariable("partnerId") String partnerId, @RequestBody Partner partner) {
		return partnerRepository.update(partnerId, partner);
	}

}
