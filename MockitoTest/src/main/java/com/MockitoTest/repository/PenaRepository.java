package com.MockitoTest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.MockitoTest.entity.PenaEntity;

@Repository
public interface PenaRepository extends MongoRepository<PenaEntity, Long> {
}

