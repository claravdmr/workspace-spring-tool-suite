package com.example.mechanical_workshop.infrastructure.repository.mongodb.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import com.example.mechanical_workshop.infrastructure.repository.mongodb.entity.AppointmentEntity;

import jakarta.validation.Valid;

@Repository
@EnableMongoRepositories
public interface AppointmentRepository extends MongoRepository<AppointmentEntity, String> {

	Page<AppointmentEntity> findByEliminado(boolean eliminado, Pageable pageable);

	Optional<AppointmentEntity> findByEliminadoAndId(boolean eliminado, String id);

	Page<AppointmentEntity> findByEliminadoAndDate(boolean eliminado, @Valid String value, @Valid Pageable pageable);

	Page<AppointmentEntity> findByEliminadoAndCustomerEmailIgnoreCase(boolean eliminado, @Valid String value,
			@Valid Pageable pageable);

	Page<AppointmentEntity> findByEliminadoAndCustomerPhoneNumberIgnoreCase(boolean eliminado, @Valid String value,
			@Valid Pageable pageable);
}
