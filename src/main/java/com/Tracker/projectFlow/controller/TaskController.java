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

import com.Tracker.projectFlow.dto.TaskRequest;
import com.Tracker.projectFlow.model.Task;
import com.Tracker.projectFlow.model.TaskStatus;
import com.Tracker.projectFlow.service.TaskService;

@RestController
@RequestMapping("/api/project/task")
public class TaskController {

	@Autowired
	private TaskService taskService;
	
	@PostMapping("/{projectid}/create")
	public ResponseEntity<Task> createTask(@RequestBody TaskRequest request, @PathVariable Long projectid, Authentication auth ){
		
		String email=auth.getName();
		
		return ResponseEntity.ok(taskService.createTask(request,projectid,email));
	}
	
	@GetMapping("/{projectid}/my-tasks")
	public ResponseEntity<List<Task>> getAllTasks(@PathVariable Long projectid, Authentication auth){
		
		return ResponseEntity.ok(taskService.getAllTasks(projectid,auth.getName()));
	}
	
	@GetMapping("/all-task")
	public ResponseEntity<List<Task>> getUserTask(Authentication auth){
		return ResponseEntity.ok(taskService.getUserTask(auth.getName()));
	}
	
	@PatchMapping("/{projectid}/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @RequestBody TaskRequest request,
            Authentication authentication) {

        Task task = taskService.updateTaskDetails(
                id,
                request,
                authentication.getName()
        );

        return ResponseEntity.ok(task);
    }


    // Update status
    @PatchMapping("/{projectid}/{id}/status")
    public ResponseEntity<Task> updateStatus(
            @PathVariable Long id,
            @RequestBody TaskStatus status,
            Authentication authentication) {

        Task task = taskService.updateStatus(
                id,
                status,
                authentication.getName()
        );

        return ResponseEntity.ok(task);
    }


    // Delete task
    @DeleteMapping("/{projectid}/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            Authentication authentication) {

        taskService.deleteTask(
                id,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}
