
package com.mongo.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mongo.database.entity.MemberEntity;

public interface MemberRepository extends MongoRepository<MemberEntity, String> {
	
	List<MemberEntity> findByProjectIdAndTaskId(String projectId, String taskId);

}
