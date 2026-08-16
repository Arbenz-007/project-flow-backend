package com.Tracker.projectFlow.dto;

import java.time.LocalDate;


import lombok.Data;

@Data
public class ProjectRequest {

	
	private String title;
	private String description;
	private LocalDate deadline;
	
}
