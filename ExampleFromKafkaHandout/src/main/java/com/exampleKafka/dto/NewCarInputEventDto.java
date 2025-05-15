package com.exampleKafka.dto;

public class NewCarInputEventDto {

	private String id;
	private String infoNewCar;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInfoNewCar() {
		return infoNewCar;
	}

	public void setInfoNewCar(String infoNewCar) {
		this.infoNewCar = infoNewCar;
	}

}
