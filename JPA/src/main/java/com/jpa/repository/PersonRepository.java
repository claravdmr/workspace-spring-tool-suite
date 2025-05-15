package com.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jpa.entity.Person;

@Transactional
@Repository
public interface PersonRepository extends JpaRepository<Person,Long>{
	
	List<Person> findByName(String name);
	
	List<Person> findByAge(int age);
	
	List<Person> findByNameAndAge (String name, int age);
	
	//The below is a manual version of the first method above, meaning we can define things such as the case sensitivity.
	@Query(value = "SELECT * FROM PERSON WHERE LOWER(NAME) = LOWER(:name)", nativeQuery = true)
	Person findByNameNoCase(@Param("name") String name);
	
    Long deleteAllByName(String name);  // Returns the count of deleted rows
    
    List<Person> findByNameIgnoreCase(String name);
}


