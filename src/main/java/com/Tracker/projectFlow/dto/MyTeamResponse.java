package com.Tracker.projectFlow.dto;
import java.time.LocalDateTime;
import java.util.List;

import com.Tracker.projectFlow.model.TeamRole;

import lombok.Data;

@Data
public class MyTeamResponse {

	private Long id;
	
	private String name;
	
	private String code;
	
	private TeamRole myrole;
	
	private LocalDateTime createdAt;
	
	private List<TeamMemberResponse> members;
	
	private List<ProjectResponse> projects;
	
	
	
}
