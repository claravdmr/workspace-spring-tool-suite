package com.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.mongo.entity.Article;
import com.mongo.entity.Details;

public interface ArticlesRepository extends MongoRepository<Article,String>{

	//like with JPA, QUERIES can be auto generated using MongoRep for attributes of Article (need to be same names).
	
	List<Article> findByName(String name);
	
	// can access attributes within dimension by putting it joined and immediately after as with dimensions and height below.
	List<Article> findByDimensionsHeight(int height);

	
	//both the below are options but not both due to duplicate names.
	//List<Article> findByDetailsValue(String value);
	
	List<Details> findByDetailsValue(String value);

	@Query("{ 'name': ?0, 'dimensiones.height' : ?1, 'dimensiones.width' : ?2")
	List<Article> customQuery(String name, int height, int width);
	
}
