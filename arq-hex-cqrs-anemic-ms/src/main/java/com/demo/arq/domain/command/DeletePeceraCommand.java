package com.demo.arq.domain.command;

import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import lombok.Value;

@Value
@Builder
@Jacksonized
public class DeletePeceraCommand {
	@NotNull
    String id;
}
