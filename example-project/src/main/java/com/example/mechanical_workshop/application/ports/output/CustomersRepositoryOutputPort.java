package com.example.mechanical_workshop.application.ports.output;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.domain.model.Customer;

import jakarta.validation.Valid;

public interface CustomersRepositoryOutputPort {

	public Page<Customer> listCustomers(@Valid Pageable pageable) throws BusinessException;

	public Optional<Customer> getCustomer(@Valid String idCustomer) throws BusinessException;

	public String saveCustomer(@Valid Customer customer) throws BusinessException;

	public void deleteCustomer(@Valid String idCustomer) throws BusinessException;

	public void modifyCustomer(@Valid Customer customer) throws BusinessException;

	public Page<Customer> searchCustomerBy(@Valid String typeSearch, @Valid String value, @Valid Pageable pageable)
			throws BusinessException;
}
