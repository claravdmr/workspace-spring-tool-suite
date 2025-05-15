package com.mongo.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("MEMBERS")
public class MemberEntity {
	
	@Id
	private String id;
	private String name;
	private String projectId;
	private String taskId;
	
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

	
	public String getProjectId() {
		return projectId;
	}

	
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	
	public String getTaskId() {
		return taskId;
	}

	
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	

}
