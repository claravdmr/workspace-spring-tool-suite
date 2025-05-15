package com.example.mechanical_workshop.infrastructure.repository.mongodb.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.mechanical_workshop.application.ports.output.CustomersRepositoryOutputPort;
import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.domain.model.Customer;
import com.example.mechanical_workshop.infrastructure.repository.mongodb.entity.CustomerEntity;
import com.example.mechanical_workshop.infrastructure.repository.mongodb.mapper.CustomerToCustomerEntityMapper;

import jakarta.validation.Valid;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para gestionar datos de clientes en el
 * repositorio MongoDB.
 */
@Slf4j
@Component
public class CustomerRepositoryService implements CustomersRepositoryOutputPort {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	CustomerToCustomerEntityMapper customerMapper;

	/**
	 * Obtiene una lista paginada de clientes.
	 *
	 * @param pageable Información de paginación.
	 * @return Página de objetos Customer.
	 * @throws BusinessException
	 */
	@Override
	@Cacheable(value = "customers", key = "#pageable")
	public Page<Customer> listCustomers(@Valid Pageable pageable) throws BusinessException {
		log.debug("listCustomers");
		try {
			Page<CustomerEntity> customers = customerRepository.findByEliminado(false, pageable);
			return customerMapper.fromOutputToInput(customers);
		} catch (Exception e) {
			log.error("Error al obtener los clientes", e);
			throw new BusinessException("Error al obtener los clientes");
		}
	}

	/**
	 * Obtiene un cliente por su ID.
	 *
	 * @param id ID del cliente.
	 * @return Optional que contiene al Customer si se encuentra, de lo contrario
	 *         vacío.
	 * @throws BusinessException
	 */
	@Override
	@Cacheable(value = "customers", key = "#id")
	public Optional<Customer> getCustomer(@Valid String id) throws BusinessException {
		log.debug("getCustomer");
		try {
			Optional<CustomerEntity> customerEntityOpt = customerRepository.findByEliminadoAndId(false, id);
			if (customerEntityOpt.isEmpty()) {
				return Optional.empty();
			}
			return customerMapper.fromOutputToInput(customerEntityOpt);
		} catch (Exception e) {
			log.error("Error al obtener el cliente", e);
			throw new BusinessException("Error al obtener el cliente");
		}
	}

	/**
	 * Busca clientes basándose en un criterio específico.
	 *
	 * @param typeSearch Tipo de criterio de búsqueda ("documentNumber", "email",
	 *                   "lastName", "customerNumber").
	 * @param value      Valor a buscar.
	 * @param pageable   Información de paginación.
	 * @return Página de objetos Customer que coinciden con el criterio de búsqueda.
	 * @throws BusinessException
	 * @throws IllegalArgumentException si el formato del número de cliente es
	 *                                  inválido.
	 */
	@Override
	@Cacheable(value = "customers", key = "#typeSearch")
	public Page<Customer> searchCustomerBy(@Valid String typeSearch, @Valid String value, Pageable pageable)
			throws BusinessException {
		log.debug("searchCustomerBy");
		Page<CustomerEntity> customerEntityOpt;
		try {
			switch (typeSearch) {
			case "documentNumber":
				customerEntityOpt = customerRepository.findByEliminadoAndDocumentNumberIgnoreCase(false, value,
						pageable);
				break;
			case "email":
				customerEntityOpt = customerRepository.findByEliminadoAndEmailIgnoreCase(false, value, pageable);
				break;
			case "lastName":
				customerEntityOpt = customerRepository.findByEliminadoAndLastNameIgnoreCase(false, value, pageable);
				break;
			case "phoneNumber":
				customerEntityOpt = customerRepository.findByEliminadoAndPhoneNumber(false, value, pageable);
				break;
			default:
				try {
					Long valueLong = Long.parseLong(value);
					customerEntityOpt = customerRepository.findByEliminadoAndCustomerNumber(false, valueLong, pageable);
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("Formato de tipo de búsqueda no válido");
				}
				break;
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Formato de tipo de búsqueda no válido");
		} catch (Exception e) {
			log.error("Error al buscar el cliente", e);
			throw new BusinessException("Error al buscar el cliente");
		}
		return customerMapper.fromOutputToInput(customerEntityOpt);
	}

	/**
	 * Guarda un nuevo cliente.
	 *
	 * @param customer Cliente a guardar.
	 * @return ID del cliente guardado.
	 * @throws BusinessException si ya existe un cliente con el mismo número de
	 *                           documento.
	 */
	@Override
	@CacheEvict(value = "customers", allEntries = true)
	public String saveCustomer(@Valid Customer customer) throws BusinessException {
		log.debug("saveCustomer");
		try {
			// Compruebo si ya existe el cliente
			Optional<CustomerEntity> existingCustomer = customerRepository
					.findByEliminadoAndDocumentNumberIgnoreCase(false, customer.getDocumentNumber());

			if (existingCustomer.isPresent()) {
				throw new BusinessException("Ya existe un cliente con este número de identidad.");
			}
			long newCustomerNumber = 10;

			// Obtengo el customerNumber para el nuevo customer, sumándole 10 al
			// customerNumber del último cutomer creado
			Optional<CustomerEntity> lastCustomerOpt = customerRepository.findTopByOrderByCustomerNumberDesc();
			if (lastCustomerOpt.isPresent()) {
				CustomerEntity lastCustomer = lastCustomerOpt.get();
				newCustomerNumber = lastCustomer.getCustomerNumber() + 10;
				log.debug("Numero ultimo cliente: {}", lastCustomer.getCustomerNumber());
			} else {
				log.debug("No existen clientes: {}", newCustomerNumber);
			}
			customer.setCustomerNumber(newCustomerNumber);
			log.debug("Numero de cliente asignado: {}", newCustomerNumber);

			CustomerEntity customerSaved = customerMapper.fromInputToOutput(customer);
			return customerRepository.save(customerSaved).getId();

		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error al guardar el cliente", e);
			throw new BusinessException("Error al guardar el cliente");
		}
	}

	/**
	 * Modifica un cliente existente.
	 *
	 * @param customer Cliente a modificar.
	 */
	@SneakyThrows
	@Override
	@CacheEvict(value = "customers", allEntries = true)
	public void modifyCustomer(@Valid Customer customer) {
		log.debug("modifyCustomer");
		try {
			CustomerEntity customerSaved = customerMapper.fromInputToOutput(customer);
			customerRepository.save(customerSaved);
		} catch (Exception e) {
			log.error("Error al modificar el cliente", e);
			throw new BusinessException("Error al modificar el cliente");
		}
	}

	/**
	 * Elimina lógicamente un cliente por su ID.
	 *
	 * @param id ID del cliente a eliminar.
	 * @throws BusinessException
	 */
	@Override
	@Transactional
	@CacheEvict(value = "customers", allEntries = true)
	public void deleteCustomer(@Valid String id) throws BusinessException {
		log.debug("deleteCustomer");
		try {
			Optional<CustomerEntity> customerOpt = customerRepository.findByEliminadoAndId(false, id);
			if (customerOpt.isPresent()) {
				customerOpt.get().setEliminado(true);
				customerRepository.save(customerOpt.get());
			}
		} catch (Exception e) {
			log.error("Error al eliminar el cliente", e);
			throw new BusinessException("Error al eliminar el cliente");
		}

	}

}
