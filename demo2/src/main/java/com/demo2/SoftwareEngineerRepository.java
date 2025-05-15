package com.demo2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
	
@Transactional													//The first is T generic, in this case SE, and the second is the key type, in this case Integer for id.
@Repository
public interface SoftwareEngineerRepository extends JpaRepository<SoftwareEngineer, Integer>{ 
	
	
	
	
}
