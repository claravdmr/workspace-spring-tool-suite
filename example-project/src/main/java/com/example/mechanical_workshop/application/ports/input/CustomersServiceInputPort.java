package com.example.mechanical_workshop.application.ports.input;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.domain.model.Customer;

import jakarta.validation.Valid;

public interface CustomersServiceInputPort {

	public String createCustomer(@Valid Customer customer) throws BusinessException;

	public Page<Customer> getCustomers(@Valid Pageable pageable) throws BusinessException;

	public Optional<Customer> getCustomer(@Valid String idCustomer) throws BusinessException;

	public void updateCustomer(@Valid Customer customer) throws BusinessException;

	public void deleteCustomer(@Valid String idCustomer) throws BusinessException;

	public void partialModificationCustomer(@Valid Customer customer) throws BusinessException;

	public Page<Customer> searchCustomerBy(@Valid String typeSerach, @Valid String value, @Valid Pageable pageable)
			throws BusinessException;

}
