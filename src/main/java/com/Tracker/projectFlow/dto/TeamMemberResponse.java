package com.Tracker.projectFlow.dto;

import com.Tracker.projectFlow.model.MembershipStatus;
import com.Tracker.projectFlow.model.TeamRole;

import lombok.Data;

@Data
public class TeamMemberResponse {

	private Long id;
	 private String username;
	    private TeamRole role;
	    private MembershipStatus status;
}
