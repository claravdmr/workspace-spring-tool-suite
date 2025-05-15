package com.demo.arq.infrastructure.database.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PECERAS", schema = "SCHEMA_PECERA")
public class PeceraEntity {
	@Id
	@Column(name = "ID", columnDefinition = "NUMBER(11) NOT NULL")
    BigDecimal id;
	@Column(name = "VALUE_DATA", columnDefinition = "VARCHAR2(255 BYTE) NULL")
	String value;
	@Column(name = "VALUE_OBJECT_VALUE", columnDefinition = "VARCHAR2(255 BYTE) NULL")
	String valueObjectValue;
	@Column(name = "ELIMINADO", columnDefinition="NUMBER(0,1) NOT NULL")
	boolean eliminado;
}
