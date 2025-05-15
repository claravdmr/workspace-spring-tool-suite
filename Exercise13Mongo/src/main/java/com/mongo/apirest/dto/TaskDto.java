package com.mongo.apirest.dto;


public class TaskDto {
	
	private String id;
	private String name;
	private TaskState state;
	
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
	
	public TaskState getState() {
		return state;
	}
	
	public void setState(TaskState state) {
		this.state = state;
	}
	
	
	

}
