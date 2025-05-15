package com.example.mechanical_workshop.infrastructure.apirest.mapper.customer;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.Customer;
import com.example.mechanical_workshop.infrastructure.apirest.dto.customer.RequestPostPutCustomerDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerToPostPutCustomerDtoMapper extends BaseMapper<Customer, RequestPostPutCustomerDto> {

}
