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
import com.expertworks.lms.http.TeamDTO;
import com.expertworks.lms.model.Group;
import com.expertworks.lms.model.Team;
import com.expertworks.lms.model.User;
import com.expertworks.lms.repository.GroupRepository;
import com.expertworks.lms.repository.TeamRepository;

@RestController
@Component
public class TeamController {

	public static final String SUCCESS = "success";


	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private GroupRepository groupRepository;

	@CrossOrigin
	@PostMapping("/team/{groupId}")
	public ApiResponse create(@PathVariable("groupId") String groupId, @RequestBody Team Team) {
		
		Team.setSk("details");
		Team.setGroupId(groupId);
		
		
		Team savedTeam = teamRepository.save(Team);
		Group group = groupRepository.get(groupId).get(0);
		groupRepository.addTeamToGroup(group.getGroupId(), savedTeam);
		return new ApiResponse(HttpStatus.OK, SUCCESS, savedTeam);
	}

		
	
	@CrossOrigin
	@GetMapping("/team")
	public ApiResponse getAll() {

		List<Team> list = teamRepository.getAll();
		return new ApiResponse(HttpStatus.OK, SUCCESS, list);
	}

	@CrossOrigin
	@GetMapping("/team/{teamId}")
	public ApiResponse get(@PathVariable("teamId") String teamId) {

    	 TeamDTO teamDTO = new TeamDTO();
	     	List<Team> list = teamRepository.get(teamId);
			for (Team team : list) {
				if (team.getSk().equalsIgnoreCase("details")) {
					teamDTO.setTeamId(teamId);
					teamDTO.setName(team.getName());
					teamDTO.setCreatedDate(team.getCreatedDate());
				} else {
					User user = new User();
					user.setUserId(team.getUserId());
					user.setName(team.getName());
					teamDTO.getUsers().add(user);

				}
			}
		return new ApiResponse(HttpStatus.OK, SUCCESS, teamDTO);
	}
	
	
	

	@CrossOrigin
	@DeleteMapping("/team/{teamId}")
	public String delete(@PathVariable("teamId") String teamId) {
		return teamRepository.delete(teamId);
	}
	
	

	@CrossOrigin
	@PutMapping("/team/{teamId}")
	public String update(@PathVariable("TeamId") String TeamId, @RequestBody Team Team) {
		return teamRepository.update(TeamId, Team);
	}

}
