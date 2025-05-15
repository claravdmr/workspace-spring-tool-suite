package com.MockitoTest.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("PENAS")
public class PenaEntity {
@Id
	private Long id;
	private TipoPena tipo;
	private Integer dias;
}
