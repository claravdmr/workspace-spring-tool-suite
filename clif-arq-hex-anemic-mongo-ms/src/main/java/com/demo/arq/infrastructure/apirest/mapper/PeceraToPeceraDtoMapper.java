package com.demo.arq.infrastructure.apirest.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.demo.arq.domain.model.Pecera;
import com.demo.arq.infrastructure.apirest.dto.response.PeceraDto;

@Mapper(componentModel = "spring")
public interface PeceraToPeceraDtoMapper {

	PeceraDto fromDomainToDto(Pecera domain);

	List<PeceraDto> fromDomainToDto(List<Pecera> domain);

	default Page<PeceraDto> fromDomainToDto(Page<Pecera> pageDomain) {
		return pageDomain == null ? null
				: new PageImpl<>(
						fromDomainToDto(pageDomain.getContent()), 
						pageDomain.getPageable(),
						pageDomain.getTotalElements());
	}

}
