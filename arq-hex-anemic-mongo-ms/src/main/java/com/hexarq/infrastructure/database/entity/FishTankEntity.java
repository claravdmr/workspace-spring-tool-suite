package com.hexarq.infrastructure.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document("FISHTANKS")
public class FishTankEntity {
	
	@Id
	String id;
	String value;
	//value object comes from DDD (see hand out) and is internal data stored within the fish tank.
	ValueObjectEntity valueObject;
	boolean deleted;

}


