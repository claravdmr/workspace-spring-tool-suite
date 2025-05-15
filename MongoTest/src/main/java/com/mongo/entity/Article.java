package com.mongo.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("ARTICLES")
public class Article {
	
	@Id
	private String id;
	
	private String name;
	
	private Dimensions dimensions;
	
	private List<Details> details;

	
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

	
	public Dimensions getDimensions() {
		return dimensions;
	}

	
	public void setDimensions(Dimensions dimensions) {
		this.dimensions = dimensions;
	}

	
	public List<Details> getDetails() {
		return details;
	}

	
	public void setDetails(List<Details> details) {
		this.details = details;
	}


	@Override
	public String toString() {
		return "Article [id=" + id + ", name=" + name + ", dimensions=" + dimensions + ", details=" + details + "]";
	}
	
	
	
	

}
