package com.demo.arq.infrastructure.database.mapper;

import java.util.List;
import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.demo.arq.domain.model.Pecera;
import com.demo.arq.domain.model.ValueObject;
import com.demo.arq.infrastructure.database.entity.PeceraEntity;
import com.demo.arq.infrastructure.database.entity.ValueObjectEntity;

@Mapper(componentModel = "spring")
public interface PeceraToPeceraEntityMapper {

	@Mapping(target = "valueZ", source = "domain.value")
	@Mapping(target = "eliminado", ignore = true)
	PeceraEntity fromDomainToEntity(Pecera domain);

	@Mapping(target = "value", source = "entity.valueZ")
	Pecera fromEntityToDomain(PeceraEntity entity);

	List<Pecera> fromEntityToDomain(List<PeceraEntity> content);

	ValueObjectEntity fromDomainToEntity(ValueObject domain);

	default ValueObject fromEntityToDomain(ValueObjectEntity entity) {
		return ValueObject.builder().value(entity.getValue()).build();
	}

	default Page<Pecera> fromEntityToDomain(Page<PeceraEntity> pageEntity) {
		return pageEntity == null ? null
				: new PageImpl<>(
						fromEntityToDomain(pageEntity.getContent()), 
						pageEntity.getPageable(),
						pageEntity.getTotalElements());
	}

	default Optional<Pecera> fromEntityToDomain(Optional<PeceraEntity> entity) {
		return entity.isPresent() ? Optional.<Pecera>of(fromEntityToDomain(entity.get())) : Optional.<Pecera>empty();
	}
}
