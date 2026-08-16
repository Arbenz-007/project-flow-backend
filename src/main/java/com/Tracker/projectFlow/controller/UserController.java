package com.Tracker.projectFlow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Tracker.projectFlow.dto.UserResponse;
import com.Tracker.projectFlow.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	
	@Autowired
	private UserService userService;
	
	@GetMapping("/me")
	ResponseEntity<UserResponse> getUser(Authentication auth){
		 System.out.println("AUTHENTICATION: " + auth);
		return ResponseEntity.ok(userService.getUser(auth.getName()));
	}
	
}
