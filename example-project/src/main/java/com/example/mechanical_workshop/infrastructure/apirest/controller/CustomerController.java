package com.example.mechanical_workshop.infrastructure.apirest.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.mechanical_workshop.application.ports.input.CustomersServiceInputPort;
import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.domain.model.Customer;
import com.example.mechanical_workshop.infrastructure.apirest.dto.customer.RequestPatchCustomerDto;
import com.example.mechanical_workshop.infrastructure.apirest.dto.customer.RequestPostPutCustomerDto;
import com.example.mechanical_workshop.infrastructure.apirest.dto.customer.ResponseCustomerDto;
import com.example.mechanical_workshop.infrastructure.apirest.mapper.customer.CustomerToCustomerDtoMapper;
import com.example.mechanical_workshop.infrastructure.apirest.mapper.customer.CustomerToPatchCustomerDtoMapper;
import com.example.mechanical_workshop.infrastructure.apirest.mapper.customer.CustomerToPostPutCustomerDtoMapper;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para gestionar clientes en un taller mecánico.
 */
@Slf4j
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	CustomersServiceInputPort customerService;

	@Autowired
	CustomerToCustomerDtoMapper customerToCustomerDtoMapper;

	@Autowired
	CustomerToPatchCustomerDtoMapper customerToPatchCustomerDtoMapper;

	@Autowired
	CustomerToPostPutCustomerDtoMapper customerToPostPutCustomerDtoMapper;

	/**
	 * Maneja las peticiones POST para crear un nuevo cliente.
	 *
	 * @param customer Datos del cliente en formato RequestPostPutCustomerDto.
	 * @return ResponseEntity con estado 201 (Creado) y URI del recurso creado. En
	 *         caso de conflicto, retorna ResponseEntity con estado 409 (Conflict) y
	 *         mensaje de error.
	 * @throws BusinessException Si hay un error al crear el cliente.
	 */
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping
	public ResponseEntity postCustomer(@Valid @RequestBody RequestPostPutCustomerDto customer)
			throws BusinessException {
		log.debug("postCustomer");
		try {
			Customer customerToSave = customerToPostPutCustomerDtoMapper.fromOutputToInput(customer);
			String idCustomer = customerService.createCustomer(customerToSave);
			URI locationHeader = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(idCustomer).toUri();
			/*
			 * Map<String, String> responseBody = new HashMap<>(); responseBody.put("id",
			 * idCustomer);
			 */

			return ResponseEntity.created(locationHeader).build();
		} catch (BusinessException e) {
			log.error("Error creando el cliente", e);
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}

	/**
	 * Maneja las peticiones GET para obtener todos los clientes.
	 *
	 * @param pageable Objeto Pageable para paginación.
	 * @return ResponseEntity con estado 200 (OK) y una página de
	 *         ResponseCustomerDto. En caso de error, retorna ResponseEntity con
	 *         estado 400 (Bad Request) y mensaje de error.
	 */
	@GetMapping
	public ResponseEntity getCustomers(@Valid Pageable pageable) {
		log.debug("getCustomers");
		Page<Customer> customersDomain;
		try {
			customersDomain = customerService.getCustomers(pageable);
			// return
			// ResponseEntity.ok(customerToCustomerDtoMapper.fromInputToOutput(customersDomain));
			List<ResponseCustomerDto> listDto = new ArrayList<>();
			customersDomain.getContent().forEach(customer -> {
				ResponseCustomerDto customerDto = customerToCustomerDtoMapper.fromInputToOutput(customer);
				listDto.add(customerDto);
			});
			return ResponseEntity.ok(new PageImpl<>(listDto, pageable, listDto.size()));
		} catch (BusinessException e) {
			log.error("Error obteniendo clientes", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/**
	 * Maneja las peticiones GET para obtener un cliente específico por su ID.
	 *
	 * @param idCustomer ID del cliente a buscar.
	 * @return ResponseEntity con estado 200 (OK) y el cliente encontrado, o
	 *         ResponseEntity con estado 204 (No Content) si no se encuentra.
	 */
	@GetMapping("/{id-customer}")
	public ResponseEntity getCustomer(@Valid @PathVariable("id-customer") final String idCustomer) {
		log.debug("getCustomer");
		try {
			Optional<Customer> customerOpt = customerService.getCustomer(idCustomer);

			if (customerOpt.isPresent()) {
				return ResponseEntity.ok(customerToCustomerDtoMapper.fromInputToOutput(customerOpt.get()));
			} else {
				return ResponseEntity.noContent().build();
			}
		} catch (BusinessException e) {
			log.error("Error al obtener el cliente", e);
			return ResponseEntity.badRequest().body("Error al obtener el cliente: " + e.getMessage());
		}
	}

	/**
	 * Maneja las peticiones GET para buscar clientes según un criterio específico.
	 *
	 * @param typeSearch Tipo de búsqueda (por ejemplo, "documentNumber",
	 *                   "lastName", "email", "customerNumber").
	 * @param value      Valor a buscar.
	 * @param pageable   Objeto Pageable para paginación.
	 * @return ResponseEntity con estado 200 (OK) y una página de clientes que
	 *         coinciden con el criterio de búsqueda, o ResponseEntity con estado
	 *         400 (Bad Request) si el tipo de búsqueda es inválido, o
	 *         ResponseEntity con estado 500 (Internal Server Error) si ocurre un
	 *         error durante la búsqueda.
	 */
	@GetMapping("/search/{type-search}")
	public ResponseEntity searchCustomerBy(@Valid @PathVariable("type-search") final String typeSearch,
			@Valid @RequestParam("value") final String value, @Valid Pageable pageable) {
		log.debug("searchCustomerBy");
		try {
			Page<Customer> customer = customerService.searchCustomerBy(typeSearch, value, pageable);
			/*
			 * if (customer.isPresent()) { return ResponseEntity.ok(customer); } else {
			 * return ResponseEntity.noContent().build(); }
			 */
			return ResponseEntity.ok(customerToCustomerDtoMapper.fromInputToOutput(customer));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body("Invalid search type");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("An error occurred while searching for customers");
		}
	}

	/**
	 * Maneja las peticiones DELETE para eliminar un cliente por su ID.
	 *
	 * @param idCustomer ID del cliente a eliminar.
	 * @return ResponseEntity con estado 204 (No Content) si la eliminación es
	 *         exitosa, o ResponseEntity con estado 400 (Bad Request) si hay un
	 *         error.
	 */
	@DeleteMapping("/{id-customer}")
	public ResponseEntity deleteCustomer(@Valid @PathVariable("id-customer") final String idCustomer) {
		log.debug("deleteCustomer");
		try {
			customerService.deleteCustomer(idCustomer);
			return ResponseEntity.noContent().build();
		} catch (BusinessException e) {
			log.error("Error eliminando cliente", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/**
	 * Maneja las peticiones PATCH para modificar parcialmente un cliente existente.
	 *
	 * @param idCustomer ID del cliente a modificar.
	 * @param customer   Datos actualizados del cliente en formato
	 *                   RequestPatchCustomerDto.
	 * @return ResponseEntity con estado 200 (OK) y el cliente modificado, o
	 *         ResponseEntity con estado 400 (Bad Request) si hay un error.
	 */
	@PatchMapping("/{id-customer}")
	public ResponseEntity patchCustomer(@Valid @PathVariable("id-customer") final String idCustomer,
			@Valid @RequestBody RequestPatchCustomerDto customer) {
		log.debug("patchCustomer");

		Customer customerDomain = customerToPatchCustomerDtoMapper.fromOutputToInput(customer);
		customerDomain.setId(idCustomer);

		try {
			customerService.partialModificationCustomer(customerDomain);
			// URI locationHeader =
			// ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand().toUri();
			return ResponseEntity.ok(customerDomain);
		} catch (BusinessException e) {
			log.debug("Error modificando cita", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	/**
	 * Maneja las peticiones PUT para actualizar completamente un cliente existente.
	 *
	 * @param idCustomer ID del cliente a actualizar.
	 * @param customer   Datos actualizados del cliente en formato
	 *                   RequestPostPutCustomerDto.
	 * @return ResponseEntity con estado 204 (No Content) si la actualización es
	 *         exitosa, o ResponseEntity con estado 400 (Bad Request) si hay un
	 *         error.
	 */
	@PutMapping("/{id-customer}")
	public ResponseEntity putCustomer(@Valid @PathVariable("id-customer") final String idCustomer,
			@Valid @RequestBody RequestPostPutCustomerDto customer) {
		log.debug("putCustomer");

		Customer domain = customerToPostPutCustomerDtoMapper.fromOutputToInput(customer);
		domain.setId(idCustomer);

		try {
			customerService.updateCustomer(domain);
			// URI locationHeader =
			// ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand().toUri();
			return ResponseEntity.noContent().build();
		} catch (BusinessException e) {
			log.debug("Error modificando cliente", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
