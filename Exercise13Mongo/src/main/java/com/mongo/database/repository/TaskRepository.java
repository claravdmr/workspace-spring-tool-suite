package com.mongo.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mongo.database.entity.TaskEntity;

@Repository
public interface TaskRepository extends MongoRepository<TaskEntity,String>{

	List<TaskEntity> findByProjectId(String projectId);
}
