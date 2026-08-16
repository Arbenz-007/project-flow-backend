package com.Tracker.projectFlow.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Tracker.projectFlow.model.Project;
import com.Tracker.projectFlow.model.Users;

public interface ProjectRepo extends JpaRepository<Project, Long>{

	List<Project> findByOwnerEmail(String email);

	Optional<Project> findByIdAndOwner(Long projectid, Users user);

}
