package com.Tracker.projectFlow.service;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Tracker.projectFlow.dto.MyTeamResponse;
import com.Tracker.projectFlow.dto.ProjectResponse;
import com.Tracker.projectFlow.dto.TeamMemberResponse;
import com.Tracker.projectFlow.dto.TeamResponse;
import com.Tracker.projectFlow.model.MembershipStatus;
import com.Tracker.projectFlow.model.Project;
import com.Tracker.projectFlow.model.TeamMembers;
import com.Tracker.projectFlow.model.TeamRole;
import com.Tracker.projectFlow.model.Teams;
import com.Tracker.projectFlow.model.Users;
import com.Tracker.projectFlow.repo.ProjectRepo;
import com.Tracker.projectFlow.repo.TeamMembersRepo;
import com.Tracker.projectFlow.repo.TeamRepo;
import com.Tracker.projectFlow.repo.UserRepo;

import jakarta.transaction.Transactional;

@Service
public class TeamService {

	@Autowired
	private TeamRepo teamRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ProjectRepo projectRepo;
	
	@Autowired
	private TeamMembersRepo memberRepo;

	public TeamResponse createTeam(String name, String email) {

		Users owner = userRepo.findByEmail(email);
		if (owner == null) {
			throw new RuntimeException("User not found");
		}

		Teams team = new Teams();

		team.setName(name);
		team.setOwner(owner);

		String code;
		do {
			code = generateCode();
		} while (teamRepo.existsByCode(code));

		team.setCode(code);

		Teams savedTeam = teamRepo.save(team);

		TeamMembers admin = new TeamMembers();

		admin.setTeam(savedTeam);
		admin.setUser(owner);
		admin.setStatus(MembershipStatus.ACCEPTED);
		admin.setRole(TeamRole.ADMIN);

		memberRepo.save(admin);

		TeamResponse res = new TeamResponse();
		res.setCode(code);
		res.setCreatedAt(savedTeam.getCreatedAt());
		res.setName(name);

		return res;

	}

	private String generateCode() {
		return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
	}

	public void joinTeam(String joinCode, String name) {

		Users user = userRepo.findByEmail(name);
		if (user == null) {
			throw new RuntimeException("User not found");
		}

		Teams team = teamRepo.findByCode(joinCode).orElseThrow(() -> new RuntimeException("Invalid team code"));

		if (team.getOwner().getEmail().equals(name)) {
			throw new RuntimeException("You are already the owner of the team");
		}

		if (memberRepo.existsByTeamAndUser(team, user)) {
			throw new RuntimeException("Already a member or request already exist");
		}

		TeamMembers member = new TeamMembers();
		member.setRole(TeamRole.MEMBER);
		member.setStatus(MembershipStatus.PENDING);
		member.setUser(user);
		member.setTeam(team);

		memberRepo.save(member);

	}

	public List<TeamMemberResponse> getJoinRequests(Long teamid, String name) {
		Teams team = teamRepo.findById(teamid).orElseThrow(() -> new RuntimeException("Team not found"));

		if (team.getOwner().getEmail().equals(name)) {
			return memberRepo.findByTeamAndStatus(team, MembershipStatus.PENDING).stream()
			        .map(member -> {

			            TeamMemberResponse response = new TeamMemberResponse();

			            response.setId(member.getId());
			            response.setUsername(member.getUser().getUsername());
			            response.setRole(member.getRole());
			            response.setStatus(member.getStatus());

			            return response;

			        })
			        .toList();
			
			
		} else {
			throw new RuntimeException("you are not owner");
		}

	}

	public void updateRequest(Long requestId, String action, String email) {
		TeamMembers request = memberRepo.findById(requestId)
				.orElseThrow(() -> new RuntimeException("Request not found"));

		Teams team = request.getTeam();

		Users owner = team.getOwner();

		// Only team owner can accept/reject
		if (!owner.getEmail().equals(email)) {
			throw new RuntimeException("Only the team owner can manage join requests");
		}

		// Request must still be pending
		if (request.getStatus() != MembershipStatus.PENDING) {
			throw new RuntimeException("This request is no longer pending");
		}

		if (action.equalsIgnoreCase("ACCEPT")) {

			// User becomes an actual team member
			request.setStatus(MembershipStatus.ACCEPTED);

			request.setRole(TeamRole.MEMBER);

			memberRepo.save(request);

		} else if (action.equalsIgnoreCase("REJECT")) {
			memberRepo.delete(request);

		} else {

			throw new RuntimeException("Invalid action. Use ACCEPT or REJECT");
		}
	}

	public List<MyTeamResponse> getMyTeams(String email) {

		Users user = userRepo.findByEmail(email);

		if (user == null) {
			throw new RuntimeException("User not found");
		}

		List<TeamMembers> memberships = memberRepo.findByUserAndStatus(user, MembershipStatus.ACCEPTED);

		return memberships.stream().map(member -> {
			Teams team = member.getTeam();
			MyTeamResponse response = new MyTeamResponse();

			response.setId(team.getId());
			response.setName(team.getName());
			response.setCode(team.getCode());
			response.setCreatedAt(team.getCreatedAt());
			
			response.setMyrole(member.getRole());

			List<TeamMemberResponse> members = team.getMembers().stream()
					.filter(m -> m.getStatus() == MembershipStatus.ACCEPTED).map(m -> {

						TeamMemberResponse memberResponse = new TeamMemberResponse();

						memberResponse.setUsername(m.getUser().getUsername());

						memberResponse.setRole(m.getRole());

						memberResponse.setStatus(m.getStatus());

						return memberResponse;
					}).toList();
			response.setMembers(members);
			
			List<ProjectResponse> projects= team.getProjects().stream()
					.map(project->{
						ProjectResponse proResponse= new ProjectResponse();
						
						proResponse.setId(project.getId());
						proResponse.setName(project.getTitle());
						
						return proResponse;
					}).toList();
			response.setProjects(projects);
			
			return response;
		}).toList();

	}
	@Transactional
	public void deleteTeam(Long teamid, String name) {
		
		Teams team= teamRepo.findById(teamid).orElseThrow(()-> new RuntimeException("Team not found"));
		
		if(!(team.getOwner().getEmail().equals(name))) {
			throw new RuntimeException("Not Allowed to delete");
		}
		 // Remove team association from projects
	    for (Project project : team.getProjects()) {
	        project.setTeam(null);
	        projectRepo.save(project);
	    }

	    // Remove all members from the team
	    memberRepo.deleteByTeam(team);

	    // Delete the team itself
	    teamRepo.delete(team);
		
		
	}
}
