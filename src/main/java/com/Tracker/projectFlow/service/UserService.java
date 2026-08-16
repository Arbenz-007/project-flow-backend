package com.Tracker.projectFlow.service;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Tracker.projectFlow.dto.LoginRequest;
import com.Tracker.projectFlow.dto.LoginResponse;
import com.Tracker.projectFlow.dto.RegisterRequest;
import com.Tracker.projectFlow.dto.UserResponse;
import com.Tracker.projectFlow.model.Users;
import com.Tracker.projectFlow.repo.UserRepo;
import com.Tracker.projectFlow.utils.JwtUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
	
	private AuthenticationManager authManager;
	private PasswordEncoder passwordEncoder;
	private JwtUtils jwtUtils;
	private UserRepo userRepo;

	public Users registerUser(Users user) {
		
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		return userRepo.save(user);
		
	}

	public LoginResponse authenticateUser(LoginRequest loginRequest) {
		Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		
		String jwt = jwtUtils.generateToken(userDetails);
		
		String username = userDetails.getUser().getUsername();
		
		return new LoginResponse(jwt, username);
		
		
	}

	public UserResponse getUser(String name) {
		
		Users user = userRepo.findByEmail(name);
		
		UserResponse res= new UserResponse();
		res.setEmail(name);
		res.setUsername(user.getUsername());
		
		return res;
	}

}
