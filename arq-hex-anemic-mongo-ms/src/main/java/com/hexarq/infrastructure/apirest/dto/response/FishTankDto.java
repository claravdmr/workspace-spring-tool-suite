package com.hexarq.infrastructure.apirest.dto.response;

import com.hexarq.infrastructure.apirest.dto.common.ValueObjectDto;

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
public class FishTankDto {
	
	String id;
	String value;
	ValueObjectDto valueObject;

}
