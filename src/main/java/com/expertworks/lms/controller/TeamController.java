package com.expertworks.lms.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import com.expertworks.lms.model.Courses;
import com.expertworks.lms.model.Team;
import com.expertworks.lms.model.TeamCourses;
import com.expertworks.lms.model.User;
import com.expertworks.lms.repository.CoursesRepository;
import com.expertworks.lms.repository.GroupRepository;
import com.expertworks.lms.repository.TeamCoursesRepository;
import com.expertworks.lms.repository.TeamRepository;

@RestController
@Component
public class TeamController {

	private final static Logger logger = LoggerFactory.getLogger(TeamController.class);

	public static final String SUCCESS = "success";

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private TeamCoursesRepository teamCoursesRepository;

	@Autowired
	private CoursesRepository coursesRepository;

	@CrossOrigin
	@PostMapping("/team/{groupId}")
	// createTeam under the Group
	public ApiResponse create(@PathVariable("groupId") String groupId, @RequestBody Team team) {

		logger.info("Creating Team Under groupId :" +  groupId);
		team.setSk("details");
		team.setGroupId(groupId);
		List<SelectedCourseDTO> selectedCourseDTOList = team.getSelectedCourses();
		logger.info("coursesDTO size : " + selectedCourseDTOList);
		Team savedTeam = teamRepository.save(team);

		// Adding the Courses to The TeamCourses
		if (selectedCourseDTOList != null) {
			for (SelectedCourseDTO selectedCourseDTO : selectedCourseDTOList) {
				String courseId = selectedCourseDTO.getCourseId();
				if (!courseId.equalsIgnoreCase("ALL")) {
					logger.info("TeamCourses adding TeamId '" + savedTeam.getTeamId() + "': CourseId : " + courseId);
					teamCoursesRepository.save(new TeamCourses(savedTeam.getTeamId(), courseId));
				} else {
					this.addAllCourses(savedTeam.getTeamId());
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
	
	
	
	
	private void addAllCourses(String teamId) {
		
		List<Courses> allCourseList = coursesRepository.getAllCourses();
		for (Courses courses : allCourseList) {
			String courseId = courses.getCourseId();
			logger.info("TeamCourses adding TeamId '" + teamId + "': ,CourseId : " + courseId);
			teamCoursesRepository.save(new TeamCourses(teamId, courseId));
		}
		
	}

	@CrossOrigin
	@GetMapping("/team")
	public ApiResponse getAll(@RequestParam(required = false, name = "groupId") String groupId) {

		List<Team> list = null;
		if (groupId == null || groupId.equalsIgnoreCase(""))
			list = teamRepository.getAll();
		else
			list = teamRepository.queryOnGSI("groupId-index", "groupId", groupId);

		Collections.sort(list, new Comparator<Team>() {
			public int compare(Team t1, Team t2) {
				// notice the cast to (Integer) to invoke compareTo
				return (t2.getCreatedDate()).compareTo(t1.getCreatedDate());
			}
		});

		return new ApiResponse(HttpStatus.OK, SUCCESS, list);
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
		return teamRepository.delete(teamId,"details");
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
	
		logger.info("Deleting Team  :" +  teamId);
		if(team.getTeamId()==null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team with teamId : "+ teamId + ", not found");
		Team teaminDB = teamRepository.load(teamId,"details");
		if(teaminDB==null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team with teamId : "+ teamId + ", not found in DB");
	
		return new ApiResponse(HttpStatus.OK, SUCCESS, teamRepository.delete(teamId,"details"));
	}
	
	@CrossOrigin
	@PostMapping("/team/update")
	public ApiResponse update(@RequestBody Team team) {
		
		String teamId = team.getTeamId();
		if(team.getTeamId()==null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team with teamId : "+ teamId + ", not found");
			
		Team teaminDB = teamRepository.load(team.getTeamId(), "details");
		if(teaminDB==null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team with teamId : "+team.getTeamId()  + ", not found");
		logger.info("Updating Team  :" +  team.getTeamId());
		team.setSk("details");
		teamRepository.update(team.getTeamId(), team);
		
		this.deleteTeamCourses(team.getTeamId(),teaminDB.getSelectedCourses());
		
		this.addTeamCourses(team.getTeamId(),team.getSelectedCourses());
		
		return new ApiResponse(HttpStatus.OK, SUCCESS, teamRepository.load(team.getTeamId(), "details"));
		
	}
	
	
	private void addTeamCourses(String teamId, List<SelectedCourseDTO> selectedCourseDTOList)
	{
	// Adding the Courses to The TeamCourses
			if (selectedCourseDTOList != null) {
				for (SelectedCourseDTO selectedCourseDTO : selectedCourseDTOList) {
					String courseId = selectedCourseDTO.getCourseId();
					if (!courseId.equalsIgnoreCase("ALL")) {
						logger.info("TeamCourses adding TeamId '" + teamId + "': CourseId : " + courseId);
						teamCoursesRepository.save(new TeamCourses(teamId, courseId));
					} else {
						this.addAllCourses(teamId);
					}

				}
			}
	}
	
	private void deleteTeamCourses(String teamId, List<SelectedCourseDTO> selectedCourseDTOList)
	{
		if (selectedCourseDTOList != null) {
			for (SelectedCourseDTO selectedCourseDTO : selectedCourseDTOList) {
				String courseId = selectedCourseDTO.getCourseId();
					teamCoursesRepository.delete(teamId,courseId);
					logger.info("TeamCourses Deleting TeamId '" + teamId + "': CourseId : " + courseId);
					
				}

			}
		}

	}
