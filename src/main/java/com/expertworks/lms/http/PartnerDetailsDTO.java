package com.expertworks.lms.http;

import java.util.ArrayList;
import java.util.List;

import com.expertworks.lms.model.Group;
import com.expertworks.lms.model.Partner;
import com.expertworks.lms.model.Team;
import com.expertworks.lms.model.TeamCourses;

public class PartnerDetailsDTO {


	private String partnerId;

	private Partner partner;

	private List<Group> groupList;

	private List<Team> teamList;

	private List<TeamCourses> deletedteamCourses = new ArrayList<TeamCourses>();

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

	public List<TeamCourses> getDeletedteamCourses() {
		return deletedteamCourses;
	}

	public void setDeletedteamCourses(List<TeamCourses> deletedteamCourses) {
		this.deletedteamCourses = deletedteamCourses;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}










}
