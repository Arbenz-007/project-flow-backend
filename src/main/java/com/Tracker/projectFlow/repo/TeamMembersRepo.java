package com.Tracker.projectFlow.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Tracker.projectFlow.model.MembershipStatus;
import com.Tracker.projectFlow.model.TeamMembers;
import com.Tracker.projectFlow.model.Teams;
import com.Tracker.projectFlow.model.Users;

public interface TeamMembersRepo extends JpaRepository<TeamMembers, Long> {

	boolean existsByTeamAndUser(Teams team, Users user);

	List<TeamMembers> findByTeamAndStatus(Teams team, MembershipStatus pending);

	List<TeamMembers> findByUserAndStatus(Users user, MembershipStatus accepted);

	void deleteByTeam(Teams team);

}
