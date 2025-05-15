package com.demo.arq.domain.event;

import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import lombok.Value;

@Value
@Builder
@Jacksonized
public class DeletedPeceraEvent {
	@NotNull
    String id;
}
