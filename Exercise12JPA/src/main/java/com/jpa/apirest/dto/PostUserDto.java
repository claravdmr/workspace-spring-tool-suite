package com.jpa.apirest.dto;


public class PostUserDto {
	
	//what does a user need to be created? name and email which go here.
	//the other attributes of a user are limited as we want to assign these ourselves
	//DTO is data transfer object and refers to objects to be dealt with by the controller.
	//this object is also used in the put method as these are the 2 attributes we want to allow users to modify.
	//this is what is sent in the postman body.
	
	private String name;
	private String email;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
