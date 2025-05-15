
package com.mongo.apirest.dto;

import java.time.LocalDate;

public class ProjectDto {

	private String id;
	private String name;
	private String description;
	private LocalDate startDate;
	private LocalDate endDate;

	
	
	public ProjectDto(String id, String name, String description, LocalDate startDate, LocalDate endDate) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public ProjectDto() {
		
	}

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


}
