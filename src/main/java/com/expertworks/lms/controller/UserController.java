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
import com.expertworks.lms.http.UserDTO;
import com.expertworks.lms.model.Group;
import com.expertworks.lms.model.Partner;
import com.expertworks.lms.model.Team;
import com.expertworks.lms.model.User;
import com.expertworks.lms.repository.GroupRepository;
import com.expertworks.lms.repository.PartnerRepository;
import com.expertworks.lms.repository.TeamRepository;
import com.expertworks.lms.repository.UserRepository;

@RestController
@Component
public class UserController {

	public static final String SUCCESS = "success";

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private PartnerRepository partnerRepository;

	@CrossOrigin
	@PostMapping("/user/{teamId}")
	public ApiResponse create(@PathVariable("teamId") String teamId, @RequestBody User user) {

		user.setUserName(user.getName());
		user.setTeamId(teamId);
		Group group = groupRepository.queryOnsk(teamId).get(0);
		System.out.println("groupId : " + group.getGroupId());
		user.setGroupId(group.getGroupId());
		Partner partner = partnerRepository.queryOnsk(group.getGroupId()).get(0);
		user.setPartnerId(partner.getPartnerId());
		User savedUser = userRepository.save(user);
		Team team = teamRepository.get(teamId).get(0); // check for existance
		teamRepository.addUserToTeam(team.getTeamId(), savedUser);
		return new ApiResponse(HttpStatus.OK, SUCCESS, savedUser);
	}

	@CrossOrigin
	@GetMapping("/user")
	public ApiResponse getAll() {

		List<Team> list = teamRepository.getAll();
		return new ApiResponse(HttpStatus.OK, SUCCESS, list);
	}

	@CrossOrigin
	@GetMapping("/user/{userId}")
	public ApiResponse get(@PathVariable("userId") String userId) {

		UserDTO userDTO = new UserDTO();
		List<User> list = userRepository.get(userId);
		for (User user : list) {
			userDTO.setName(user.getName());
			userDTO.setUserId(user.getUserId());
			userDTO.setGroupId(user.getGroupId());
			userDTO.setTeamId(user.getTeamId());
			userDTO.setPartnerId(user.getPartnerId());
		}

		return new ApiResponse(HttpStatus.OK, SUCCESS, userDTO);

	}

	@CrossOrigin
	@DeleteMapping("/user/{userId}")
	public String delete(@PathVariable("userId") String teamId) {
		return userRepository.delete(teamId);
	}

	@CrossOrigin
	@PutMapping("/team/{userId}")
	public String update(@PathVariable("userId") String userId, @RequestBody User user) {
		return userRepository.update(userId, user);
	}

}
