package com.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jpa.entity.Access;
import com.jpa.entity.Action;

@Transactional
@Repository
public interface AccessRepository extends JpaRepository<Access,Long>{
	
	List<Access> findByRegAndActionOrderByDateDesc(String reg, Action action);
	
}


// this is not the table itself but rather gives us access to the table via various methods included in the JPArepository