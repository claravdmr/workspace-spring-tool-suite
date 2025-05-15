package case_management_system.domain.model;

public enum Complexity {
	
	SIMPLE(1), MODERATE(2), COMPLEX(3);
	
	public final int value;

	Complexity(int value) {
		this.value = value;
	}

}
