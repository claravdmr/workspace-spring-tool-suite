package com.hexarq.infrastructure.apirest.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.hexarq.domain.model.FishTank;
import com.hexarq.infrastructure.apirest.dto.response.FishTankDto;

//spring is just because we are using spring, and unmapped properties will be ignored
@Mapper(componentModel = "spring")
public interface FishTankToFishTankDtoMapper {

	FishTankDto fromDomainToDto(FishTank domain);
	
	List<FishTankDto> fromDomainToDto(List<FishTank> domain);
	
	
	//the pageable is the page and the number of elements and is one of the 3 things below which the page is comprised of.
	//only the content is mapped but the pageable and total elements are needed for the constructor of the PageImpl.
	default Page<FishTankDto> fromDomainToDto(Page<FishTank> pageDomain){
		return pageDomain == null ? null : 
			new PageImpl<>(
				fromDomainToDto(pageDomain.getContent()), 
				pageDomain.getPageable(),
				pageDomain.getTotalElements());
		//  //
	}
	
}
