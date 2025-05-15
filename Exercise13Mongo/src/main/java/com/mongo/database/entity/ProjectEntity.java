
package com.mongo.database.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("PROJECTS")
public class ProjectEntity {

	@Id
	private String id;
	private String name;
	private String description;
	private LocalDate startDate;
	private LocalDate endDate;

	private LocalDateTime creation;
	private List<String> tasks;
	private List<String> members;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}


	public LocalDateTime getCreation() {
		return creation;
	}


	public void setCreation(LocalDateTime creation) {
		this.creation = creation;
	}


	public List<String> getTasks() {
		return tasks;
	}


	public void setTasks(List<String> tasks) {
		this.tasks = tasks;
	}


	public List<String> getMembers() {
		return members;
	}


	public void setMembers(List<String> members) {
		this.members = members;
	}


}
