package com.Tracker.projectFlow.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TeamResponse {

	
	private String name;
	
	private LocalDateTime createdAt;
	
	private String code;
	
}
