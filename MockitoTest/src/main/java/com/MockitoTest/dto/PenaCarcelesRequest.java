package com.MockitoTest.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PenaCarcelesRequest {
private Integer edad;
private Boolean esMujer;
private List<Long> penas;
}
