package com.hexarq.infrastructure.database.mapper;

import java.util.List;
import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.hexarq.domain.model.FishTank;
import com.hexarq.domain.model.ValueObject;
import com.hexarq.infrastructure.database.entity.FishTankEntity;
import com.hexarq.infrastructure.database.entity.ValueObjectEntity;

// see hand out for how to deal with variables with different names, as it knows what to do based on the names.
//e.g. @Mapping(target = "value2", source = "domain.value") for domain to entity
//e.g. @Mapping(target = "value", source = "entity.value2") for entity to domain
// target is what it is converting to, source what it is converting from.

@Mapper(componentModel = "spring")
public interface FishTankToFishTankEntityMapper {
	
	@Mapping(target = "deleted", ignore = true) // this it to remove the warning that 'deleted' field isn't mapped as entity has deleted boolean variable but domain does not.
	FishTankEntity fromDomainToEntity(FishTank domain);
	
	FishTank fromEntityToDomain(FishTankEntity entity);
	
	ValueObjectEntity fromDomainToEntity(ValueObject domain);
	
	List<FishTank> fromEntityToDomain(List<FishTankEntity> content);
	
	//default + further code replaces the method that mapstruct generates so as to manually create a mapping for a certain method.
	default ValueObject fromEntityToDomain(ValueObjectEntity entity) {
		return ValueObject.builder().value(entity.getValue()).build();
	}

	//the pageable is the page and the number of elements and is one of the 3 things below which the page is comprised of.
	//only the content is mapped but the pageable and total elements are needed for the constructor of the PageImpl.
	default Page<FishTank> fromEntityToDomain(Page<FishTankEntity> pageEntity){
		return pageEntity == null ? null : 
			new PageImpl<>(
				fromEntityToDomain(pageEntity.getContent()), 
				pageEntity.getPageable(),
				pageEntity.getTotalElements());
		//  //
	}

	default Optional<FishTank> fromEntityToDomain(Optional<FishTankEntity> entity){
		return entity.isPresent() 
				?
				//the "Optional.<T>of" is a method of the Optional class and here returns a FishTank which has been mapped in the brackets.
				Optional.<FishTank>of(fromEntityToDomain(entity.get())) 
				: 
				Optional.<FishTank>empty();
		//	//	
	}

	

}
