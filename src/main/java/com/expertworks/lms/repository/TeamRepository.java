package com.expertworks.lms.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.expertworks.lms.model.Team;
import com.expertworks.lms.model.User;

@Repository
public class TeamRepository {

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	// full scan
	public List<Team> getAll() {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withLimit(10);
		PaginatedScanList<Team> paginatedScanList = dynamoDBMapper.scan(Team.class, scanExpression);

		paginatedScanList.loadAllResults();

		List<Team> list = new ArrayList<Team>(paginatedScanList.size());

		Iterator<Team> iterator = paginatedScanList.iterator();
		while (iterator.hasNext()) {
			Team element = iterator.next();
			list.add(element);
		}

		return list;

	}

	public List<Team> get(String TeamId) {

		Team Team = new Team();
		Team.setTeamId(TeamId);

		DynamoDBQueryExpression<Team> dynamoDBQueryExpression = new DynamoDBQueryExpression().withHashKeyValues(Team);
		PaginatedQueryList<Team> paginatedQueryList = dynamoDBMapper.query(Team.class, dynamoDBQueryExpression);
		paginatedQueryList.loadAllResults();

		List<Team> list = new ArrayList<Team>(paginatedQueryList.size());

		Iterator<Team> iterator = paginatedQueryList.iterator();
		while (iterator.hasNext()) {
			Team element = iterator.next();
			list.add(element);
		}

		return list;

	}

	public Team addUserToTeam(String teamId, User user) {

		Team team = new Team();
		team.setTeamId(teamId);
		team.setSk("U#" + user.getUserId());
		team.setUserId(user.getUserId());
		team.setName(user.getName());
		Team savedRow = this.save(team);
		return savedRow;
	}

	public Team save(Team Team) {
		dynamoDBMapper.save(Team);
		return Team;
	}

	public String delete(String TeamId) {
		Team teamCourses = dynamoDBMapper.load(Team.class, TeamId);
		dynamoDBMapper.delete(teamCourses);
		return "Team  Deleted!";
	}

	public String update(String TeamId, Team Team) {
		dynamoDBMapper.save(TeamId, new DynamoDBSaveExpression().withExpectedEntry("teamId",
				new ExpectedAttributeValue(new AttributeValue().withS(TeamId))));
		return TeamId;
	}
}