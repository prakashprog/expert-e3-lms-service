package com.expertworks.lms.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expertworks.lms.model.Team;
import com.expertworks.lms.repository.TeamRepository;

@Service
public class TeamService {

	public static final String TEAM_ADMIN = "TEAM_ADMIN";

	@Autowired
	private TeamRepository teamRepository;

	public Team getTeambyName(String teamName) throws Exception {
		List<Team> list = null;
		list = teamRepository.queryOnGSI("name-index", "name", teamName);

		if (list != null && list.size() > 1)
			throw new Exception("Multiple Team Exits with same Name");
		else

			return list.get(0);
	}

	public Team getTeambyType(String type) throws Exception {
		List<Team> list = null;
		list = teamRepository.queryOnGSI("teamType-index", "teamType", type);

		if (list != null && list.size() ==1)
		{	//throw new Exception("No Team exists");
			System.out.println("One Team present TeamId: "+list.get(0).getTeamId());

			return list.get(0);
		}
		else

			return null;
	}

	public Team getAdminTeambyGroupId(String groupId) throws Exception {
		Team adminTeam = null;
		List<Team> teamList = null;
		teamList = teamRepository.queryOnGSI("groupId-index", "groupId", groupId);

		teamList =  teamList.stream()
        .filter(t -> t.getTeamType()!=null && t.getTeamType().equalsIgnoreCase(TEAM_ADMIN)).collect(Collectors.toList());

		if(teamList.size()==1)
			adminTeam= teamList.get(0);

		return adminTeam;
	}

	public Team createOrGetAdminTeam(String groupId) throws Exception {

		Team team = null;
		//team = getTeambyType(TEAM_ADMIN);
		team = getAdminTeambyGroupId(groupId);
		if (team == null) {
			//create Admin Team if doesnt
			team = new Team();
			team.setSk("details");
			team.setGroupId(groupId);
			team.setName(TEAM_ADMIN);
			team.setTeamType(TEAM_ADMIN);
			team = teamRepository.save(team);
			System.out.println("createOrGetAdminTeam New Admin under (GroupId : +" +groupId+", teamId: "+team.getTeamId()+")");
		}

		System.out.println("createOrGetAdminTeam TeamId:"+team.getTeamId());
		return team;
	}

}
