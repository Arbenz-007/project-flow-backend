package com.Tracker.projectFlow.dto;

import java.time.LocalDate;

import com.Tracker.projectFlow.model.TaskStatus;

import lombok.Data;

@Data
public class TaskRequest {

	
	private String title;
	
	private String description;
	
	private LocalDate dueDate;
}
