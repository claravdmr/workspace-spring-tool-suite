package com.demo.arq.domain.query;

import jakarta.validation.constraints.NotNull;

import org.springframework.data.domain.Pageable;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import lombok.Value;

@Value
@Builder
@Jacksonized
public class GetPecerasQuery {
    @NotNull
    Pageable pageable;
}
