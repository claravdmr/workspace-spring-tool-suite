package com.example.mechanical_workshop.infrastructure.apirest.mapper.customer;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.Customer;
import com.example.mechanical_workshop.infrastructure.apirest.dto.customer.RequestPatchCustomerDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerToPatchCustomerDtoMapper extends BaseMapper<Customer, RequestPatchCustomerDto> {

}
