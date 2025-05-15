package com.mongo.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("TASKS")
public class TaskEntity {
	
	@Id
	private String id;
	private String name;
	private String state; // sometimes in the database itself, ie from entity class, enums are saved as strings. 
	private String projectId; // we are relating task to project but mongo itself is not.
	
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
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}

	public String getProjectId() {

		return projectId;

	}

	public void setProjectId(String projectId) {

		this.projectId = projectId;

	}
	
	
	

}
