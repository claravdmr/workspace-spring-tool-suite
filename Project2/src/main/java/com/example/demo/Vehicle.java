package com.example.demo;

public class Vehicle {

	private String registration;
	private Models model;
	private int kms;
	private boolean secretService;

	// constructor
	public Vehicle(String registration, Models model, int kms, boolean secretService) {
		this.registration = registration;
		this.model = model;
		this.kms = kms;
		this.secretService = secretService;
	}

	// getters and setters
	public String getRegistration() {
		return registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
	}

	public Models getModel() {
		return model;
	}

	public void setModel(Models model) {
		this.model = model;
	}

	public int getKms() {
		return kms;
	}

	public void setKms(int kms) {
		this.kms = kms;
	}

	public boolean isSecretService() {
		return secretService;
	}

	public void setSecretService(boolean secretService) {
		this.secretService = secretService;
	}

	@Override
	public String toString() {
		return "Vehicle [registration=" + registration + ", model=" + model + ", kms=" + kms + ", secretService="
				+ secretService + "]";
	}

}
