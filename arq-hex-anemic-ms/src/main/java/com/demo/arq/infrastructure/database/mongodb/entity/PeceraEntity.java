package com.demo.arq.infrastructure.database.mongodb.entity;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
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
@Document("PECERAS")
public class PeceraEntity {
	@Id
    String id;
    String value;
    ValueObjectEntity valueObject;
    BigDecimal idJpa;
    boolean eliminado;
}
