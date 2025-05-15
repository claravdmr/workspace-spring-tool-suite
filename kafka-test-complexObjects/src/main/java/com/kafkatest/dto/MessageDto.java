
package com.kafkatest.dto;

import java.util.List;

public class MessageDto {

	private String user;
	private String message;
	private List<DetailDto> details;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<DetailDto> getDetails() {
		return details;
	}

	public void setDetails(List<DetailDto> details) {
		this.details = details;
	}


}
