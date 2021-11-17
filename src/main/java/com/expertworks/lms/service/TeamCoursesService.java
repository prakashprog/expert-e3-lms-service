package com.expertworks.lms.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expertworks.lms.model.TeamCourses;
import com.expertworks.lms.repository.TeamCoursesRepository;

@Service
public class TeamCoursesService {

	@Autowired
	private TeamCoursesRepository teamCoursesRepository;

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

	public static void main(String[] args) {

		List<String> teamCourses = Arrays.asList();

		teamCourses.forEach((temp) -> {
			System.out.println(temp);
		});

	}

}
