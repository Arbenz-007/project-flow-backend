package com.Tracker.projectFlow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Tracker.projectFlow.dto.LoginRequest;
import com.Tracker.projectFlow.dto.LoginResponse;
import com.Tracker.projectFlow.dto.RegisterRequest;
import com.Tracker.projectFlow.dto.UserResponse;
import com.Tracker.projectFlow.model.Role;
import com.Tracker.projectFlow.model.Users;
import com.Tracker.projectFlow.repo.UserRepo;
import com.Tracker.projectFlow.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
	
	private UserService userService;
	private UserRepo userRepo;
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody RegisterRequest register) throws Exception{
		
		Users isEmailExist=userRepo.findByEmail(register.getEmail());
		if(isEmailExist!=null) {
			throw new Exception("User with this email already exists");
		}
		
		Users user=new Users();
		
		user.setEmail(register.getEmail());
		user.setPassword(register.getPassword());
		user.setUsername(register.getUsername());
		user.setRole(Role.USER);
		
		Users savedUSer= userService.registerUser(user);
		
		return ResponseEntity.ok("User registered Succesfully");
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
		
		LoginResponse res= userService.authenticateUser(loginRequest);
		
		return ResponseEntity.ok(res);
	}
	


}
