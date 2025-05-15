package com.demo.arq.infrastructure.event.dto.output;

import com.demo.arq.infrastructure.event.dto.common.ValueObjectEventDto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class PeceraEventDto {
	@NotNull
    String id;
	String value;
	ValueObjectEventDto valueObject;
}
