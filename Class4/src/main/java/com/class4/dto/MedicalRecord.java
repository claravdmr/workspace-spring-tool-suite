package com.class4.dto;

import java.time.LocalDateTime;

public class MedicalRecord {

	private String doctorId;
	private LocalDateTime lastAccessed;

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public LocalDateTime getLastAccessed() {
		return lastAccessed;
	}

	public void setLastAccessed(LocalDateTime lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

}
