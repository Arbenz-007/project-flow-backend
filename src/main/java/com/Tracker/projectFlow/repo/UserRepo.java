package com.Tracker.projectFlow.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Tracker.projectFlow.model.Users;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {

	Users findByEmail(String email);

}
