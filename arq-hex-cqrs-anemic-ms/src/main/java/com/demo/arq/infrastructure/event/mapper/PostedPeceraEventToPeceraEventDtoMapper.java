package com.demo.arq.infrastructure.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.demo.arq.domain.event.PostedPeceraEvent;
import com.demo.arq.domain.mapper.BaseMapper;
import com.demo.arq.infrastructure.event.dto.PeceraEventDto;

@Mapper(componentModel = "spring", uses = ValueObjectToValueObjectEventDtoMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostedPeceraEventToPeceraEventDtoMapper extends BaseMapper<PostedPeceraEvent, PeceraEventDto> {

}
