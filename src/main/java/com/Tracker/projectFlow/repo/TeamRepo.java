package com.Tracker.projectFlow.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Tracker.projectFlow.model.Teams;


public interface TeamRepo extends JpaRepository<Teams, Long> {

	boolean existsByCode(String code);
	Optional<Teams> findByCode(String code);

}
