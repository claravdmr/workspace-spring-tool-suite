package com.jpa.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jpa.database.entity.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role,Long>{
	List<Role> findByUserId(Long userId);
}
