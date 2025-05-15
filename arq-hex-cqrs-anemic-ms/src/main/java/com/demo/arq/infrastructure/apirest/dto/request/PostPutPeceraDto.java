package com.demo.arq.infrastructure.apirest.dto.request;

import com.demo.arq.infrastructure.apirest.dto.common.ValueObjectDto;

import jakarta.validation.constraints.NotNull;
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
public class PostPutPeceraDto {
	@NotNull
	String value;
	ValueObjectDto valueObject;
}
