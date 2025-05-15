package com.demo.arq.domain.query;

import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import lombok.Value;

@Value
@Builder
@Jacksonized
public class GetPeceraQuery {
    @NotNull
	String id;
}
