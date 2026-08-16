package com.Tracker.projectFlow.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Tracker.projectFlow.model.Project;
import com.Tracker.projectFlow.model.Task;

public interface TaskRepo extends JpaRepository<Task, Long> {

	List<Task> findByProject(Project project);
	void deleteByProject(Project project);

}
