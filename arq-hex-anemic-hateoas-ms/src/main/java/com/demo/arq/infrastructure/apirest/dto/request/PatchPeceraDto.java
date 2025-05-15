package com.demo.arq.infrastructure.apirest.dto.request;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.demo.arq.infrastructure.apirest.dto.common.ValueObjectDto;
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
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "peceras")
public class PatchPeceraDto extends RepresentationModel<PatchPeceraDto> {
	String value;
	ValueObjectDto valueObject;
}
