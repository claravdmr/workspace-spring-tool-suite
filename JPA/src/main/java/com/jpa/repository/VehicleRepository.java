package com.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jpa.entity.Vehicle;

@Transactional
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Long>{
	

}


