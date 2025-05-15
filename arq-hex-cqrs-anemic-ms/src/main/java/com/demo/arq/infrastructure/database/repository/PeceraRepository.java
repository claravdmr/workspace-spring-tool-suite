package com.demo.arq.infrastructure.database.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import com.demo.arq.infrastructure.database.entity.PeceraEntity;

@Repository
@EnableMongoRepositories
public interface PeceraRepository extends MongoRepository<PeceraEntity, String> {

}
