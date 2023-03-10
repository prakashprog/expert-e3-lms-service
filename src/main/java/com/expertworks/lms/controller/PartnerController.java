package com.expertworks.lms.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.expertworks.lms.http.ApiResponse;
import com.expertworks.lms.http.DeletedCoursesDTO;
import com.expertworks.lms.http.PartnerDetailsDTO;
import com.expertworks.lms.model.Group;
import com.expertworks.lms.model.Partner;
import com.expertworks.lms.model.Team;
import com.expertworks.lms.model.TeamCourses;
import com.expertworks.lms.repository.GroupRepository;
import com.expertworks.lms.repository.PartnerRepository;
import com.expertworks.lms.repository.TeamRepository;
import com.expertworks.lms.service.TeamCoursesService;

@RestController
@Component
public class PartnerController {

	public static final String SUCCESS = "success";

	@Autowired
	private PartnerRepository partnerRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private TeamCoursesService teamCoursesService;

	@CrossOrigin
	@PostMapping("/partner")
	public ApiResponse save(@RequestBody Partner partner) {
		partner.setSk("details");
		Partner savedPartner = partnerRepository.save(partner);
		/*
		 * if (partner.getGroupIds() != null && partner.getGroupIds().size() > 0) { for
		 * (String groupId : partner.getGroupIds()) { // Group Id are present Partner
		 * newRow = new Partner(); newRow.setPartnerId(savedPartner.getPartnerId());
		 * newRow.setSk("G#" + groupId); partnerRepository.save(newRow); } }
		 */

		return new ApiResponse(HttpStatus.OK, SUCCESS, savedPartner);
	}



	@CrossOrigin
	@PostMapping("/partner/update")
	public ApiResponse update(@RequestBody Partner partner) {

		partnerRepository.update(partner);

		return new ApiResponse(HttpStatus.OK, SUCCESS, teamRepository.load(partner.getPartnerId(), "details"));
	}

	/*
	 * @CrossOrigin
	 *
	 * @GetMapping("/partner") public ApiResponse getAll() {
	 *
	 * List<Partner> list = partnerRepository.getAll(); return new
	 * ApiResponse(HttpStatus.OK, SUCCESS, list); }
	 */

	@CrossOrigin
	@GetMapping("/partner")
	public ApiResponse getAll() {

		List<Partner> list = partnerRepository.queryOnGSI("sk-index", "sk", "details");

		Collections.sort(list, new Comparator<Partner>() {
			@Override
			public int compare(Partner p1, Partner p2) {
				// notice the cast to (Integer) to invoke compareTo
				return (p2.getCreatedDate()).compareTo(p1.getCreatedDate());
			}
		});

		return new ApiResponse(HttpStatus.OK, SUCCESS, list);
	}

//	@CrossOrigin
//	@GetMapping("/partner/{partnerId}")
//	public ApiResponse get(@PathVariable("partnerId") String partnerId) {
//
//		PartnerDTO partnerDTO = new PartnerDTO();
//
//		List<Partner> list = partnerRepository.get(partnerId);
//		for (Partner partner : list) {
//			if (partner.getSk().equalsIgnoreCase("details")) {
//				partnerDTO.setPartnerId(partnerId);
//				partnerDTO.setName(partner.getName());
//				partnerDTO.setCreatedDate(partner.getCreatedDate());
//			} else {
//				Group group = new Group();
//				group.setGroupId(partner.getGroupId());
//				group.setName(partner.getName());
//				partnerDTO.getGroups().add(group);
//
//			}
//		}
//		// List list = coursesRepository.getmy1Courses(courseId,"S#1");
//		return new ApiResponse(HttpStatus.OK, SUCCESS, partnerDTO);
//	}


	@CrossOrigin
	@GetMapping("/partner/{partnerId}")
	public ApiResponse get(@PathVariable("partnerId") String partnerId) {

		return new ApiResponse(HttpStatus.OK, SUCCESS, partnerRepository.load(partnerId, "details"));
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

	/*
	 * @CrossOrigin
	 *
	 * @PutMapping("/partner/{partnerId}") public String
	 * update(@PathVariable("partnerId") String partnerId, @RequestBody Partner
	 * partner) { return partnerRepository.update(partnerId); }
	 */

	@CrossOrigin
	@PostMapping("/partner/{partnerId}/deletecourses")
	public DeletedCoursesDTO revokePartner(@PathVariable("partnerId") String partnerId, @RequestBody Partner partner) {

		List<Group> groupList = null;
		List<Team> teamList = null;
		List<TeamCourses> teamCoursesList = null;
		DeletedCoursesDTO deletedCoursesDTO = new DeletedCoursesDTO();
		deletedCoursesDTO.setPartnerId(partnerId);

		partner = partnerRepository.load(partnerId, "details");
		if(partner==null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Partner with partnerId : "+ partnerId + " is missing");

		deletedCoursesDTO.setPartner(partner);
		groupList = groupRepository.queryOnGSI("partnerId-index", "partnerId", partnerId);

		if (groupList != null && groupList.size() > 0)
			for (Group group : groupList) {
				teamList = teamRepository.queryOnGSI("groupId-index", "groupId", group.getGroupId());
				if (teamList != null && teamList.size() > 0)
					for (Team team : teamList) {
						teamCoursesList = teamCoursesService.deleteCourses(team.getTeamId());
						deletedCoursesDTO.getDeletedteamCourses().addAll(teamCoursesList);
					}

			}

		deletedCoursesDTO.setGroupList(groupList);
		deletedCoursesDTO.setTeamList(teamList);

		return deletedCoursesDTO;
	}

	@CrossOrigin
	@PostMapping("/partner/{partnerId}/details")
	public PartnerDetailsDTO partnerDetails(@PathVariable("partnerId") String partnerId, @RequestBody Partner partner) {

		List<Group> groupList = null;
		List<Team> teamList = null;
		List<TeamCourses> teamCoursesList = null;
		PartnerDetailsDTO partnerDetailsDTO = new PartnerDetailsDTO();
		partnerDetailsDTO.setPartnerId(partnerId);

		partner = partnerRepository.load(partnerId, "details");
		if(partner==null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Partner with partnerId : "+ partnerId + " is missing");

		partnerDetailsDTO.setPartner(partner);
		groupList = groupRepository.queryOnGSI("partnerId-index", "partnerId", partnerId);

		if (groupList != null && groupList.size() > 0)
			for (Group group : groupList) {
				teamList = teamRepository.queryOnGSI("groupId-index", "groupId", group.getGroupId());
				if (teamList != null && teamList.size() > 0)
					for (Team team : teamList) {
						partnerDetailsDTO.getDeletedteamCourses().addAll(teamCoursesList);
					}

			}

		partnerDetailsDTO.setGroupList(groupList);
		partnerDetailsDTO.setTeamList(teamList);

		return partnerDetailsDTO;
	}


}
