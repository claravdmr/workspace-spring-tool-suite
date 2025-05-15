package com.demo.arq.infrastructure.database.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.demo.arq.infrastructure.database.entity.PeceraEntity;

@Repository
@EnableJpaRepositories
public interface PeceraRepository extends JpaRepository<PeceraEntity, BigDecimal> {
	
	Page<PeceraEntity> findByEliminado(boolean eliminado, Pageable pageable);
	
	Optional<PeceraEntity> findByIdAndEliminado(BigDecimal id, boolean eliminado);
	
	// Ejemplo de sacar el id de una Secuencia Oracle
	@Query(value = "SELECT SCHEMA_PECERA.SQ_PECERAS.nextval FROM dual", nativeQuery = true)
	public BigDecimal getNextValSequence();
}
