package com.demo.arq.infrastructure.database.entity;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
@EqualsAndHashCode
@Document(collection = "PECERAS")
public class PeceraEntity {
	@Id
    String id;
    @NotNull
	String value;
    ValueObjectEntity valueObject;
}
