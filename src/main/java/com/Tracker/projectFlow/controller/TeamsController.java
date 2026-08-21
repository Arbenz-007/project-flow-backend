package com.Tracker.projectFlow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Tracker.projectFlow.dto.MyTeamResponse;
import com.Tracker.projectFlow.dto.TeamMemberResponse;
import com.Tracker.projectFlow.dto.TeamResponse;
import com.Tracker.projectFlow.model.TeamMembers;
import com.Tracker.projectFlow.model.Teams;
import com.Tracker.projectFlow.service.TeamService;

@RestController
@RequestMapping("/api/teams")
public class TeamsController {

	@Autowired
	private TeamService teamService;
	
	@PostMapping("/create")
	public ResponseEntity<TeamResponse> createTeam(@RequestParam("name") String name ,Authentication auth){
		
			return ResponseEntity.ok(teamService.createTeam(name,auth.getName()));
	}
	
	@GetMapping("/my-teams")
		public ResponseEntity<List<MyTeamResponse>> getMyTeams(Authentication auth){

			return ResponseEntity.ok(teamService.getMyTeams(auth.getName())); 
		}
	
	@PostMapping("/join")
	public ResponseEntity<String> joinTeam(@RequestParam("joincode") String joinCode, Authentication auth){
		teamService.joinTeam(joinCode,auth.getName());
		return ResponseEntity.ok("Request Sent");
	}
	
	@GetMapping("/{teamid}/requests")
	public ResponseEntity<List<TeamMemberResponse>> getJoinRequests(@PathVariable("teamid") Long teamid, Authentication auth){

		return ResponseEntity.ok(teamService.getJoinRequests(teamid,auth.getName()));
	}
	
	@PutMapping("/requests/{requestId}")
	public ResponseEntity<?> updateRequest(
	        @PathVariable Long requestId,
	        @RequestParam String action,
	        Authentication auth) {

	    teamService.updateRequest(requestId, action, auth.getName());

	    return ResponseEntity.ok("Request updated successfully");
	}
	
	@DeleteMapping("/{teamid}/delete")
	public ResponseEntity<String> deleteTeam(@PathVariable Long teamid, Authentication auth){
		teamService.deleteTeam(teamid,auth.getName()); 
		return ResponseEntity.ok("Team Deleted Successfully");
	}
}
