package com.hexarq.infrastructure.database.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import com.hexarq.infrastructure.database.entity.FishTankEntity;

@Repository
@EnableMongoRepositories
public interface FishTankRepository extends MongoRepository<FishTankEntity,String>{
	
	Page<FishTankEntity> findByDeleted(boolean deleted, Pageable pageable);
	
	Optional<FishTankEntity> findByIdAndDeleted(String id, boolean deleted);

}
