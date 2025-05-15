package com.class4.dto;

import java.time.LocalDateTime;

public class Appointment {

	private String doctorId;
	private LocalDateTime date;

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

}
