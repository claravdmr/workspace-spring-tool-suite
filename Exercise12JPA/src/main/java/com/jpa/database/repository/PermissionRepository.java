package com.jpa.database.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jpa.database.entity.Permission;
import com.jpa.database.entity.PermissionId;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,PermissionId>{

	
}
