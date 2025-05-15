package com.MockitoTest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.MockitoTest.entity.CarcelEntity;

@Repository
public interface CarcelRepository extends MongoRepository<CarcelEntity, Long> {
}

