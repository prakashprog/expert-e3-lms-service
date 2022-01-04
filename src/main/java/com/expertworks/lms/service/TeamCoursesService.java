package com.expertworks.lms.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expertworks.lms.http.SelectedCourseDTO;
import com.expertworks.lms.model.Courses;
import com.expertworks.lms.model.TeamCourses;
import com.expertworks.lms.repository.CoursesRepository;
import com.expertworks.lms.repository.TeamCoursesRepository;

@Service
public class TeamCoursesService {

	private final static Logger logger = LoggerFactory.getLogger(TeamCoursesService.class);

	@Autowired
	private TeamCoursesRepository teamCoursesRepository;

	@Autowired
	private CoursesRepository coursesRepository;

	public String deleteCourses(String teamId, String rangeKey) {

		return teamCoursesRepository.delete(teamId, rangeKey);

	}

	public List<TeamCourses> deleteCourses(String teamId) {

		List<TeamCourses> teamCourses = teamCoursesRepository.getTeamCourses(teamId);
		if (teamCourses != null && teamCourses.size() > 0)
			for (TeamCourses teamCourse : teamCourses) {
				teamCoursesRepository.delete(teamCourse.getTeamId(), teamCourse.getCourseId());
			}
		return teamCourses;

	}

	public List<TeamCourses> deactivateCourses(String teamId) {

		List<TeamCourses> teamCourses = teamCoursesRepository.getTeamCourses(teamId);
		if (teamCourses != null && teamCourses.size() > 0)
			for (TeamCourses teamCourse : teamCourses) {
				teamCoursesRepository.delete(teamCourse.getTeamId(), teamCourse.getCourseId());
			}
		return teamCourses;

	}

	public void addTeamCourses(String teamId,String groupId,String partnerId , List<SelectedCourseDTO> selectedCourseDTOList) {
		// Adding the Courses to The TeamCourses
		if (selectedCourseDTOList != null) {
			for (SelectedCourseDTO selectedCourseDTO : selectedCourseDTOList) {
				String courseId = selectedCourseDTO.getCourseId();
				if (!courseId.equalsIgnoreCase("ALL")) {
					logger.info("TeamCourses adding TeamId '" + teamId + "': CourseId : " + courseId);
					teamCoursesRepository.save(new TeamCourses(teamId, courseId,groupId,partnerId));
				} else {
					this.addAllCourses(teamId,groupId,partnerId);
				}

			}
		}
	}

	public void deleteTeamCourses(String teamId, List<SelectedCourseDTO> selectedCourseDTOList) {
		if (selectedCourseDTOList != null) {
			for (SelectedCourseDTO selectedCourseDTO : selectedCourseDTOList) {
				String courseId = selectedCourseDTO.getCourseId();
				teamCoursesRepository.delete(teamId, courseId);
				logger.info("TeamCourses Deleting TeamId '" + teamId + "': CourseId : " + courseId);

			}

		}
	}

	public void addAllCourses(String teamId,String groupId,String partnerId) {

		List<Courses> allCourseList = coursesRepository.getAllCourses();
		for (Courses courses : allCourseList) {
			String courseId = courses.getCourseId();
						logger.info("TeamCourses adding All TeamId '" + teamId + "', CourseId : " + courseId+
					", groupId : " + groupId + ", partnerId : " + partnerId);
			teamCoursesRepository.save(new TeamCourses(teamId, courseId,groupId,partnerId));
		}

	}

	public static void main(String[] args) {

		List<String> teamCourses = Arrays.asList();

		teamCourses.forEach((temp) -> {
			System.out.println(temp);
		});

	}

}
