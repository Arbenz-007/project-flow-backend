package com.Tracker.projectFlow.service;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Tracker.projectFlow.dto.TaskRequest;
import com.Tracker.projectFlow.model.Project;
import com.Tracker.projectFlow.model.Task;
import com.Tracker.projectFlow.model.TaskStatus;
import com.Tracker.projectFlow.model.Users;
import com.Tracker.projectFlow.repo.ProjectRepo;
import com.Tracker.projectFlow.repo.TaskRepo;
import com.Tracker.projectFlow.repo.UserRepo;

@Service
public class TaskService {
	
	@Autowired
	private TaskRepo taskRepo;

	@Autowired
	private ProjectRepo projectRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	public Task createTask(TaskRequest request, Long projectid, String email) {
		// TODO Auto-generated method stub
		Users user = userRepo.findByEmail(email);

		if (user == null) {
	        throw new RuntimeException("User not found");
	    }

		 Project project = projectRepo.findByIdAndOwner(projectid, user)
		            .orElseThrow(() ->
		                    new RuntimeException("Project not found or you don't have access"));
		 
		 
		Task task= new Task();
		task.setDescription(request.getDescription());
		task.setTitle(request.getTitle());
		task.setDueDate(request.getDueDate());
		task.setStatus(TaskStatus.TODO);
		task.setProject(project);
		
		
		return taskRepo.save(task);
	}

	public List<Task> getAllTasks(Long projectid, String email) {
		Users user = userRepo.findByEmail(email);
		if (user == null) {
		    throw new RuntimeException("User not found");
		}
		Project project = projectRepo.findByIdAndOwner(projectid, user)
	            .orElseThrow(() ->
	                    new RuntimeException("Project not found or you don't have access"));
		
		return taskRepo.findByProject(project);
		
	}
	
	public Task updateTaskDetails(Long id, TaskRequest request, String email) {

        Task task = taskRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Task not found"));

        if (!task.getProject().getOwner().getEmail().equals(email)) {
            throw new RuntimeException(
                    "You are not allowed to update this task");
        }

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }

        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }

        return taskRepo.save(task);
    }


    // Update task status
    public Task updateStatus(Long id, TaskStatus status, String email) {

        Task task = taskRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Task not found"));

        if (!task.getProject().getOwner().getEmail().equals(email)) {
            throw new RuntimeException(
                    "You are not allowed to update this task");
        }

        task.setStatus(status);

        return taskRepo.save(task);
    }


    // Delete task
    public void deleteTask(Long id, String email) {

        Task task = taskRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Task not found"));

        if (!task.getProject().getOwner().getEmail().equals(email)) {
            throw new RuntimeException(
                    "You are not allowed to delete this task");
        }

        taskRepo.deleteById(id);
    }

	public List<Task> getUserTask(String name) {
		return taskRepo.findByProjectOwnerEmail(name);
	}
	
	

	
	
}
