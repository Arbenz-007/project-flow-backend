package com.Tracker.projectFlow.dto;

import com.Tracker.projectFlow.model.Users;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

	private String token;
	private String username;
}
