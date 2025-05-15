package com.mongo.apirest.dto;


public enum TaskState {
	
	PENDING, IN_PROGRESS, COMPLETE;
	
	public static boolean validateValue(String value) {
		
		TaskState[] states = TaskState.values();
		
		for (TaskState state : states) {
			if(state.name().equals(value)) {     //this converts the value to a string and checks that is exists in the enum
				return true;
			}
		}
		
		return false;
				
	}

}
