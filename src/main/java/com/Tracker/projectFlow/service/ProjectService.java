package com.Tracker.projectFlow.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Tracker.projectFlow.dto.ProjectRequest;
import com.Tracker.projectFlow.model.Project;
import com.Tracker.projectFlow.model.ProjectStatus;
import com.Tracker.projectFlow.model.Users;
import com.Tracker.projectFlow.repo.ProjectRepo;
import com.Tracker.projectFlow.repo.TaskRepo;
import com.Tracker.projectFlow.repo.UserRepo;

import jakarta.transaction.Transactional;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepo projectRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private TaskRepo taskRepo;

	public Project createProject(ProjectRequest request, String email) {
	
		Users user=userRepo.findByEmail(email);
		
		if(user==null) {
			throw new RuntimeException("User not found");
		}
		
		Project project= new Project();
		project.setDeadline(request.getDeadline());
		project.setDescription(request.getDescription());
		project.setOwner(user);
		project.setTitle(request.getTitle());
		project.setStatus(ProjectStatus.NOT_STARTED);
		
		return projectRepo.save(project);
		
	}

	public List<Project> getMyProjects(String email) {
		// TODO Auto-generated method stub
		List<Project> projects =
		        projectRepo.findByOwnerEmail(email);
		
		return projects;
	}

	public Project getProject(Long id) {

	    return projectRepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Project not found"));
	}

	public Project updateProjectDetails(Long id, ProjectRequest request, String email) {
		
		Project project=projectRepo.findById(id).orElseThrow(()-> new RuntimeException("Project not found"));
		
		if(!project.getOwner().getEmail().equals(email)) {
			throw new RuntimeException("You are not allowed to update this project");
		}
		
		if (request.getTitle() != null) {
		    project.setTitle(request.getTitle());
		}

		if (request.getDescription() != null) {
		    project.setDescription(request.getDescription());
		}

		if (request.getDeadline() != null) {
		    project.setDeadline(request.getDeadline());
		}
		
		return projectRepo.save(project);
	}

	public Project updateStatus(Long id, ProjectStatus status, String name) {
Project project=projectRepo.findById(id).orElseThrow(()-> new RuntimeException("Project not found"));
		
		if(!project.getOwner().getEmail().equals(name)) {
			throw new RuntimeException("You are not allowed to update this project");
		}
		
		project.setStatus(status);
		
		return projectRepo.save(project);
	}

	@Transactional
	public void deleteProject(Long id, String name) {
		// TODO Auto-generated method stub
		Project project = projectRepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Project not found"));

	    if (!project.getOwner().getEmail().equals(name)) {
	        throw new RuntimeException("You are not allowed to delete this project");
	    }
	    
	    taskRepo.deleteByProject(project);


	    projectRepo.deleteById(id);
	}
	
}
