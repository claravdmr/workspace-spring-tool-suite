
package com.mongo.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mongo.database.entity.BasketEntity;
import com.mongo.database.entity.BasketState;

@Repository
public interface BasketRepository extends MongoRepository<BasketEntity, String> {

	List<BasketEntity> findByState(BasketState state); //as state is an attribute, the repository will auto generate findBy method.

}
