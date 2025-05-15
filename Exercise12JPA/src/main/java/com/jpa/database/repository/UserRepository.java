package com.jpa.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jpa.database.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

}
