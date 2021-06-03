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
import com.expertworks.lms.http.GroupDTO;
import com.expertworks.lms.model.Group;
import com.expertworks.lms.model.Partner;
import com.expertworks.lms.model.Team;
import com.expertworks.lms.repository.GroupRepository;
import com.expertworks.lms.repository.PartnerRepository;

@RestController
@Component
public class GroupController {

	public static final String SUCCESS = "success";


	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private PartnerRepository partnerRepository;

	@CrossOrigin
	@PostMapping("/group/{partnerId}")
	public ApiResponse save(@PathVariable("partnerId") String partnerId, @RequestBody Group group) {
		
		group.setSk("details");
		group.setPartnerId(partnerId);
		Group savedGroup = groupRepository.save(group);
		System.out.println("Group Saved " + savedGroup.getGroupId());
		Partner partner = partnerRepository.get(partnerId).get(0);
		partnerRepository.addGroup(partner.getPartnerId(), savedGroup);
		return new ApiResponse(HttpStatus.OK, SUCCESS, savedGroup);
	}

		
	
	@CrossOrigin
	@GetMapping("/group")
	public ApiResponse getAll() {

		List<Group> list = groupRepository.getAll();
		return new ApiResponse(HttpStatus.OK, SUCCESS, list);
	}

	@CrossOrigin
	@GetMapping("/group/{groupId}")
	public ApiResponse get(@PathVariable("groupId") String groupId) {

	   GroupDTO groupDTO = new GroupDTO();
		
		List<Group> list = groupRepository.get(groupId);
		for (Group group : list) {
			if (group.getSk().equalsIgnoreCase("details")) {
				groupDTO.setGroupId(groupId);
				groupDTO.setName(group.getName());
				groupDTO.setCreatedDate(group.getCreatedDate());
			} else {
				Team team = new Team();
				team.setTeamId(group.getTeamId());
				team.setName(group.getName());
				groupDTO.getTeams().add(team);

			}
		}
	    return new ApiResponse(HttpStatus.OK, SUCCESS, groupDTO);
	}
	
	
	

	@CrossOrigin
	@DeleteMapping("/group/{groupId}")
	public String delete(@PathVariable("contactId") String contactId) {
		return groupRepository.delete(contactId);
	}
	
	
	@CrossOrigin
	@PostMapping("/Group/addGroups/{GroupId}")
	public ApiResponse addGroups(@PathVariable("GroupId") String GroupId, @RequestBody Group group) {
		
		Group newRow= new Group();	
		newRow.setGroupId(GroupId);
		newRow.setSk("G#"+group.getGroupId());
		newRow.setGroupId(group.getGroupId());
		Group savedRow=  groupRepository.save(newRow);
		return new ApiResponse(HttpStatus.OK, SUCCESS, savedRow);
	}

	@CrossOrigin
	@PutMapping("/Group/{GroupId}")
	public String update(@PathVariable("GroupId") String GroupId, @RequestBody Group Group) {
		return groupRepository.update(GroupId, Group);
	}

}
