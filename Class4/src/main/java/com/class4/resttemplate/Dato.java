package com.class4.resttemplate;

import java.io.Serializable;

public class Dato implements Serializable { // serializable class will prepare it into plain text for the web
	// this was imported when Dato class was underlined yellow, generated
	private static final long serialVersionUID = 7451583615745669769L;

	private String dato1;
	private String dato2;

	public String getDato1() {
		return dato1;
	}

	public void setDato1(String dato1) {
		this.dato1 = dato1;
	}

	public String getDato2() {
		return dato2;
	}

	public void setDato2(String dato2) {
		this.dato2 = dato2;
	}

}
