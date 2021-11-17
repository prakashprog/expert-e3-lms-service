package com.expertworks.lms.http;

import java.util.ArrayList;
import java.util.List;

import com.expertworks.lms.model.Group;
import com.expertworks.lms.model.Team;
import com.expertworks.lms.model.TeamCourses;

public class DeletedCoursesDTO {


	private List<Group> groupList;

	private List<Team> teamList;

	private List<TeamCourses> teamCoursesList = new ArrayList<TeamCourses>();

	public List<Group> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}

	public List<Team> getTeamList() {
		return teamList;
	}

	public void setTeamList(List<Team> teamList) {
		this.teamList = teamList;
	}

	public List<TeamCourses> getTeamCoursesList() {
		return teamCoursesList;

	}

	public void setTeamCoursesList(List<TeamCourses> teamCoursesList) {
		this.teamCoursesList = teamCoursesList;

	}









}
