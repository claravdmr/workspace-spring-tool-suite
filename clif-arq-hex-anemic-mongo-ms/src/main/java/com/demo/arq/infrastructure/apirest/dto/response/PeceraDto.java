package com.demo.arq.infrastructure.apirest.dto.response;

import com.demo.arq.infrastructure.apirest.dto.common.ValueObjectDto;

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
public class PeceraDto {
	String id;
	String value;
	ValueObjectDto valueObject;
}
