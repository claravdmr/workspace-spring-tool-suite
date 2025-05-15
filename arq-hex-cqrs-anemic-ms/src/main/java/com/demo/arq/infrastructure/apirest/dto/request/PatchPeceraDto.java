package com.demo.arq.infrastructure.apirest.dto.request;

import com.demo.arq.infrastructure.apirest.dto.common.ValueObjectDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
public class PatchPeceraDto {
	String value;
	ValueObjectDto valueObject;
}
