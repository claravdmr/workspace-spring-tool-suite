package com.mongo.database.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mongo.database.entity.ProjectEntity;

@Repository
public interface ProjectRepository extends MongoRepository<ProjectEntity, String>{

}
