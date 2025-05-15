package com.example.mechanical_workshop.infrastructure.repository.mongodb.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import com.example.mechanical_workshop.domain.model.StatusRepair;
import com.example.mechanical_workshop.infrastructure.repository.mongodb.entity.WorkOrderEntity;

@Repository
@EnableMongoRepositories
public interface WorkOrderRepository extends MongoRepository<WorkOrderEntity, String> {

	Page<WorkOrderEntity> findByEliminado(boolean eliminado, Pageable pageable);

	Optional<WorkOrderEntity> findByEliminadoAndId(boolean eliminado, String id);

	@Query(sort = "{ 'workOrderNumber': -1 }")
	Optional<WorkOrderEntity> findTopByOrderByWorkOrderNumberDesc();

	Page<WorkOrderEntity> findByEliminadoAndWorkOrderNumber(boolean eliminado, Long valueLong, Pageable pageable);

	@Query("{ 'eliminado': ?0, 'vehicle.licensePlate': ?1 }")
	Page<WorkOrderEntity> findByEliminadoAndLicensePlate(boolean eliminado, String licensePlate, Pageable pageable);

	Page<WorkOrderEntity> findByEliminadoAndStatusNot(boolean eliminado, StatusRepair terminada, Pageable pageable);
}
