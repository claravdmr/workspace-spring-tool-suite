package com.example.mechanical_workshop.application.ports.output;

import com.example.mechanical_workshop.domain.model.Customer;

import jakarta.validation.Valid;

public interface CustomersMessageOutputPort {

	void eventCreateCustomer(@Valid Customer input);

	void eventModifyCustomer(@Valid Customer input);

	void eventDeleteCustomer(@Valid Customer customer);

}
