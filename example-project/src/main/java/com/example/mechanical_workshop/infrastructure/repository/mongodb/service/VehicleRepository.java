package com.example.mechanical_workshop.infrastructure.repository.mongodb.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import com.example.mechanical_workshop.infrastructure.repository.mongodb.entity.VehicleEntity;

@Repository
@EnableMongoRepositories
public interface VehicleRepository extends MongoRepository<VehicleEntity, String> {

	Page<VehicleEntity> findByEliminado(boolean eliminado, Pageable pageable);

	Optional<VehicleEntity> findByEliminadoAndId(boolean eliminado, String id);

	Optional<VehicleEntity> findByLicensePlateIgnoreCase(String licensePlate);

	Optional<VehicleEntity> findByVinIgnoreCase(String licensePlate);

	Page<VehicleEntity> findByEliminadoAndLicensePlateIgnoreCase(boolean eliminado, String licensePlate,
			Pageable pageable);

	Page<VehicleEntity> findByEliminadoAndVinIgnoreCase(boolean eliminado, String vin, Pageable pageable);
}
