package com.demo.arq.domain.command;

import com.demo.arq.domain.model.ValueObject;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class PatchPeceraCommand {
	@NotNull
    String id;
	String value;
    ValueObject valueObject;
}
