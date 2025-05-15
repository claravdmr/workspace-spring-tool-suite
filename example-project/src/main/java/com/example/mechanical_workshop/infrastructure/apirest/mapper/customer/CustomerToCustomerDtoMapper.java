package com.example.mechanical_workshop.infrastructure.apirest.mapper.customer;

import org.mapstruct.Mapper;

import com.example.mechanical_workshop.domain.mapper.BaseMapper;
import com.example.mechanical_workshop.domain.model.Customer;
import com.example.mechanical_workshop.infrastructure.apirest.dto.customer.ResponseCustomerDto;

@Mapper(componentModel = "spring")
public interface CustomerToCustomerDtoMapper extends BaseMapper<Customer, ResponseCustomerDto> {

}
