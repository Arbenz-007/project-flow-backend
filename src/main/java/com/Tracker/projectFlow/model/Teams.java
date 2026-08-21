package com.Tracker.projectFlow.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="teams")
public class Teams {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@ManyToOne
	private Users owner;
	
	@Column(unique = true, nullable = false)
	private String code;
	
	@OneToMany(mappedBy = "team")
	private List<Project> projects=new ArrayList<>();
	
	@OneToMany(mappedBy = "team")
    private List<TeamMembers> members = new ArrayList<>();
	
	
}
