package com.jpa.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ACCESS")
public class Access {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String reg;
	
	
	@Column(nullable = false)
	private LocalDateTime date;
	
	@Column(nullable = false)
	private Action action;

	
	public Long getId() {
		return id;
	}

	
	public void setId(Long id) {
		this.id = id;
	}

	
	public String getReg() {
		return reg;
	}

	
	public void setReg(String reg) {
		this.reg = reg;
	}

	
	public LocalDateTime getDate() {
		return date;
	}

	
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	
	public Action getActions() {
		return action;
	}

	
	public void setActions(Action actions) {
		this.action = actions;
	}
	
	
}
