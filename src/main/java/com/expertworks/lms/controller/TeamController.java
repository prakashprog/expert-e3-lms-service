package com.expertworks.lms.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.expertworks.lms.http.ApiResponse;
import com.expertworks.lms.http.SelectedCourseDTO;
import com.expertworks.lms.http.TeamDTO;
import com.expertworks.lms.model.Group;
import com.expertworks.lms.model.Team;
import com.expertworks.lms.model.TeamCourses;
import com.expertworks.lms.model.User;
import com.expertworks.lms.repository.GroupRepository;
import com.expertworks.lms.repository.TeamCoursesRepository;
import com.expertworks.lms.repository.TeamRepository;
import com.expertworks.lms.service.TeamCoursesService;

@RestController
@Component
public class TeamController {

	private final static Logger logger = LoggerFactory.getLogger(TeamController.class);

	public static final String SUCCESS = "success";
	public static final String TEAM_ADMIN = "TEAM_ADMIN";

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private TeamCoursesRepository teamCoursesRepository;

	@Autowired
	private TeamCoursesService teamCoursesService;

	@Autowired
	private GroupRepository groupRepository;

	@CrossOrigin
	@PostMapping("/team/{groupId}")
	// createTeam under the Group
	public ApiResponse create(@PathVariable("groupId") String groupId, @RequestBody Team team) {

		logger.info("Creating Team Under groupId :" + groupId);
		team.setSk("details");
		team.setGroupId(groupId);
		String partnerId = null;
		List<SelectedCourseDTO> selectedCourseDTOList = team.getSelectedCourses();
		logger.info("coursesDTO size : " + selectedCourseDTOList);
		// Every Team should be unique Name
		checkIfTeamNameExists(groupId, team.getName());
		// start fix
		Group group = groupRepository.load(groupId, "details");
		partnerId = group.getPartnerId();
		team.setPartnerId(partnerId);
		if (partnerId == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Partners found for groupId : " + groupId);
		// group.setPartnerId(partnerId);
		// end
		Team savedTeam = teamRepository.save(team);

		// Adding the Courses to The TeamCourses
		if (selectedCourseDTOList != null) {
			for (SelectedCourseDTO selectedCourseDTO : selectedCourseDTOList) {
				String courseId = selectedCourseDTO.getCourseId();
				if (!courseId.equalsIgnoreCase("ALL")) {
					logger.info("TeamCourses adding TeamId '" + savedTeam.getTeamId() + "', CourseId : " + courseId
							+ ", groupId : " + groupId + ", partnerId : " + partnerId);
					teamCoursesRepository.save(new TeamCourses(savedTeam.getTeamId(), courseId, groupId, partnerId));
				} else {
					teamCoursesService.addAllCourses(savedTeam.getTeamId(), groupId, partnerId);
				}

			}
		}

		/*
		 * if (team.getAllCourses() != null &&
		 * team.getAllCourses().equalsIgnoreCase("ALL")) { List<Courses> allCourseList =
		 * coursesRepository.getAllCourses(); for (Courses courses : allCourseList) {
		 * String courseId = courses.getCourseId();
		 * logger.info("TeamCourses adding TeamId '" + savedTeam.getTeamId() +
		 * "': CourseId : " + courseId); teamCoursesRepository.save(new
		 * TeamCourses(savedTeam.getTeamId(), courseId)); } }
		 */

//		Group group = groupRepository.get(groupId).get(0);
//		groupRepository.addTeamToGroup(group.getGroupId(), savedTeam);
		return new ApiResponse(HttpStatus.OK, SUCCESS, savedTeam);
	}

	@CrossOrigin
	@GetMapping("/team")
	public ApiResponse getAll(@RequestParam(required = false, name = "groupId") String groupId) {

		List<Team> teams = null;
		if (groupId == null || groupId.equalsIgnoreCase(""))
			teams = teamRepository.getAll();
		else
			teams = teamRepository.queryOnGSI("groupId-index", "groupId", groupId);

		teams = teams.stream().filter(p -> {
			if (p.getTeamType() != null && p.getTeamType().equals(TEAM_ADMIN)) {
				return false;//dont collect admins Teams
			} else
				return true;
		}).collect(Collectors.toList());

		Collections.sort(teams, new Comparator<Team>() {
			@Override
			public int compare(Team t1, Team t2) {
				// notice the cast to (Integer) to invoke compareTo
				return (t2.getCreatedDate()).compareTo(t1.getCreatedDate());
			}
		});

		return new ApiResponse(HttpStatus.OK, SUCCESS, teams);
	}

	@CrossOrigin
	@GetMapping("/teambyName")
	public ApiResponse getTeamByName(@RequestParam(required = false, name = "name") String name) {

		List<Team> list = null;
		list = teamRepository.queryOnGSI("name-index", "name", name);
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
		return teamRepository.delete(teamId, "details");
	}

	@CrossOrigin
	@PutMapping("/team/{teamId}")
	public void update(@PathVariable("TeamId") String TeamId, @RequestBody Team Team) {
		teamRepository.update(TeamId, Team);
	}

	@CrossOrigin
	@PostMapping("/team/delete")
	public ApiResponse delete(@RequestBody Team team) throws Exception {

		String teamId = team.getTeamId();

		logger.info("Deleting Team  :" + teamId);
		if (team.getTeamId() == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team with teamId : " + teamId + ", not found");
		Team teaminDB = teamRepository.load(teamId, "details");
		if (teaminDB == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Team with teamId : " + teamId + ", not found in DB");

		return new ApiResponse(HttpStatus.OK, SUCCESS, teamRepository.delete(teamId, "details"));
	}

	@CrossOrigin
	@PostMapping("/team/update")
	public ApiResponse update(@RequestBody Team team) {

		String teamId = team.getTeamId();
		String partnerId;
		String groupId;
		if (team.getTeamId() == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team with teamId : " + teamId + ", not found");

		Team teaminDB = teamRepository.load(team.getTeamId(), "details");

		// --------------------------------------------
		Group group = groupRepository.load(teaminDB.getGroupId(), "details");
		partnerId = group.getPartnerId();
		groupId = teaminDB.getGroupId();
		// ------------------------------------------------------------

		if (teaminDB == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Team with teamId : " + team.getTeamId() + ", not found");
		logger.info("Updating Team  :" + team.getTeamId());
		team.setSk("details");
		teamRepository.update(team.getTeamId(), team);

		teamCoursesService.deleteTeamCourses(team.getTeamId(), teaminDB.getSelectedCourses());

		teamCoursesService.addTeamCourses(team.getTeamId(), groupId, partnerId, team.getSelectedCourses());

		return new ApiResponse(HttpStatus.OK, SUCCESS, teamRepository.load(team.getTeamId(), "details"));

	}

	// Team should be unique Name in an Group
	public void checkIfTeamNameExists(String groupId, String teamName) {

		List<Team> teams = teamRepository.queryOnGSI("groupId-index", "groupId", groupId);

		for (Team team : teams) {
			if (team.getName().endsWith(teamName))
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, team.getName() + " already exists");

		}

	}
}
