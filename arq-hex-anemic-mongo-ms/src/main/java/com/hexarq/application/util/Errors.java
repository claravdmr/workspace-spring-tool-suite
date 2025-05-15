package com.hexarq.application.util;

public class Errors {
	
	//this is to ensure no one can create a new / add new variables and will overwrite the default public constructor.
	private Errors() {
		//private constructor
	}
	
	public static final String MAXIMUM_PAGINATION_EXCEEDED = "MAXIMUM_PAGINATION_EXCEEDED";
	public static final String FISHTANK_NOT_FOUND = "FISHTANK_NOT_FOUND";

}
