package com.jpa.database.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

//NB see weak and strong section of handout - so linking two ids together.

@Embeddable
public class PermissionId implements Serializable {

	String idRole;
	String permission;
}
