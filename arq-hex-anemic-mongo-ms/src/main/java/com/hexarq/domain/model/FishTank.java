package com.hexarq.domain.model;

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
public class FishTank {
	
	String id;
	String value;
	//value object comes from DDD (see hand out) and is internal data stored within the fish tank.
	ValueObject valueObject;

}
