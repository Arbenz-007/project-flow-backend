package com.Tracker.projectFlow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Tracker.projectFlow.dto.ProjectRequest;
import com.Tracker.projectFlow.model.Project;
import com.Tracker.projectFlow.model.ProjectStatus;
import com.Tracker.projectFlow.service.ProjectService;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

	@Autowired
	private ProjectService projectService;
	
	@PostMapping("/create")
	public ResponseEntity<Project> createProject(@RequestBody ProjectRequest request, Authentication auth){
		
		String email=auth.getName();
		Project project=projectService.createProject(request,email);
		
		return ResponseEntity.ok(project);
	}
	
	@GetMapping("/my-projects")
	public ResponseEntity<List<Project>> getAllProjects(Authentication authentication) {

	    String email = authentication.getName();

	    return ResponseEntity.ok(
	            projectService.getMyProjects(email)
	    );
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Project> getProject(@PathVariable Long id){
		 System.out.println("Controller reached");
		return ResponseEntity.ok(projectService.getProject(id));
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Project> updateProjectDetails(@PathVariable Long id, @RequestBody ProjectRequest request, Authentication auth){
		String email=auth.getName();
		return ResponseEntity.ok(projectService.updateProjectDetails(id,request,email));
	}
	
	@PatchMapping("/{id}/status")
	public ResponseEntity<Project> updateStatus(
	        @PathVariable Long id,
	        @RequestBody ProjectStatus status,
	        Authentication auth) {

	    return ResponseEntity.ok(
	            projectService.updateStatus(id, status, auth.getName())
	    );
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProject(@PathVariable Long id, Authentication auth){
		projectService.deleteProject(id,auth.getName());
		return ResponseEntity.ok("Deleted");
	}
}
