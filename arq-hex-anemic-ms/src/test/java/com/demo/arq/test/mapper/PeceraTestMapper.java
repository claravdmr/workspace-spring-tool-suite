package com.demo.arq.test.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.demo.arq.infrastructure.apirest.dto.request.PatchPeceraDto;
import com.demo.arq.infrastructure.apirest.dto.response.PeceraDto;
import com.demo.arq.infrastructure.database.mongodb.entity.PeceraEntity;

@Mapper(componentModel = "spring")
public interface PeceraTestMapper {

	PatchPeceraDto fromEntityToPatchDto(PeceraEntity entity);

	PeceraDto fromEntityToDto(PeceraEntity entity);

	List<PeceraDto> fromEntityToDto(List<PeceraEntity> entity);

	default Page<PeceraDto> fromEntityToDto(Page<PeceraEntity> pageEntity) {
		return pageEntity == null ? null
				: new PageImpl<>(fromEntityToDto(pageEntity.getContent()), pageEntity.getPageable(),
						pageEntity.getTotalElements());
	}

}
