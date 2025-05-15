package com.example.demo;

public class Property {

	private int rooms;
	private String address;

	// constructor
	public Property(int rooms, String address) {
		this.rooms = rooms;
		this.address = address;
	}

	// getters and setters
	public int getRooms() {
		return rooms;
	}

	public void setRooms(int rooms) {
		this.rooms = rooms;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
