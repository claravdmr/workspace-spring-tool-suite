package com.example.mechanical_workshop.infrastructure.repository.mongodb.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import com.example.mechanical_workshop.infrastructure.repository.mongodb.entity.CustomerEntity;

import jakarta.validation.Valid;

@Repository
@EnableMongoRepositories
public interface CustomerRepository extends MongoRepository<CustomerEntity, String> {

	/*
	 * @Query(value = "{}", sort = "{ 'customerNumber': -1 }", fields =
	 * "{ 'customerNumber': 1 }") CustomerEntity
	 * findTopByOrderByCustomerNumberDesc();
	 */

	Page<CustomerEntity> findByEliminado(boolean eliminado, Pageable pageable);

	Optional<CustomerEntity> findByEliminadoAndId(boolean eliminado, String id);

	@Query(sort = "{ 'customerNumber': -1 }")
	Optional<CustomerEntity> findTopByOrderByCustomerNumberDesc();

	Page<CustomerEntity> findByEliminadoAndDocumentNumberIgnoreCase(boolean eliminado, String documentNumber,
			Pageable pageable);

	Page<CustomerEntity> findByEliminadoAndCustomerNumber(boolean eliminado, Long customerNumber, Pageable pageable);

	Page<CustomerEntity> findByEliminadoAndEmailIgnoreCase(boolean eliminado, String email, Pageable pageable);

	Page<CustomerEntity> findByEliminadoAndLastNameIgnoreCase(boolean eliminado, @Valid String value,
			Pageable pageable);

	Optional<CustomerEntity> findByEliminadoAndDocumentNumberIgnoreCase(boolean eliminado, String documentNumber);

	Page<CustomerEntity> findByEliminadoAndPhoneNumber(boolean eliminado, @Valid String value, Pageable pageable);

}
