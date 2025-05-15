package com.hexarq.infrastructure.apirest.dto.request;

import com.hexarq.infrastructure.apirest.dto.common.ValueObjectDto;

import jakarta.validation.constraints.NotNull;
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
public class PostPutFishTankDto {
	
	@NotNull
	String value;
	ValueObjectDto valueObject;

}
