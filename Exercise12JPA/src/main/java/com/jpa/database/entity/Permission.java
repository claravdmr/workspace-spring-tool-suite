package com.jpa.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "PERMISSIONS")
public class Permission { //this is an attribute of a role but sql cannot have a list within a table so creating table for this.
	
	@Id
	private PermissionId id;

}
