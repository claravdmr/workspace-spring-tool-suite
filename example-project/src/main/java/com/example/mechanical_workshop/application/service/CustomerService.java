package com.example.mechanical_workshop.application.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mechanical_workshop.application.ports.input.CustomersServiceInputPort;
import com.example.mechanical_workshop.application.ports.output.CustomersMessageOutputPort;
import com.example.mechanical_workshop.application.ports.output.CustomersRepositoryOutputPort;
import com.example.mechanical_workshop.application.ports.output.VehiclesMessageOutputPort;
import com.example.mechanical_workshop.application.ports.output.VehiclesRepositoryOutputPort;
import com.example.mechanical_workshop.application.util.Constants;
import com.example.mechanical_workshop.application.util.Errors;
import com.example.mechanical_workshop.domain.exception.BusinessException;
import com.example.mechanical_workshop.domain.mapper.CustomerPatchMapper;
import com.example.mechanical_workshop.domain.model.Customer;
import com.example.mechanical_workshop.domain.model.Vehicle;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para gestionar clientes en la aplicación.
 */
@Slf4j
@Service
public class CustomerService implements CustomersServiceInputPort {

	@Autowired
	CustomersRepositoryOutputPort customersRepository;

	@Autowired
	CustomersMessageOutputPort kafkaProducer;

	@Autowired
	CustomerPatchMapper customerPatchMapper;

	@Autowired
	VehiclesRepositoryOutputPort vehiclesRepositoryOutputPort;

	@Autowired
	VehiclesMessageOutputPort vehiclesMessageOutputPort;

	/**
	 * Crea un nuevo cliente en el sistema.
	 *
	 * @param customer Cliente a crear.
	 * @return ID del cliente creado.
	 * @throws BusinessException Si ocurre algún error de negocio.
	 */
	@Override
	public String createCustomer(@Valid Customer customer) throws BusinessException {
		log.debug("Init createCustomer");
		String customerId = "";
		try {
			customerId = customersRepository.saveCustomer(customer);
			customer.setId(customerId);
			kafkaProducer.eventCreateCustomer(customer);
			log.debug("End createCustomer");
			return customerId;
		} catch (BusinessException e) {
			log.error("Error al guardar el cliente: " + customer.getCustomerNumber(), e);
			throw e;
		} catch (Exception e) {
			log.error("Error inesperado al guardar el cliente: " + customer.getCustomerNumber(), e);
			throw new BusinessException("Error inesperado al guardar el cliente");
		}
	}

	/**
	 * Obtiene una página de clientes del sistema.
	 *
	 * @param pageable Objeto Pageable que especifica la paginación de resultados.
	 * @return Página de clientes recuperada del repositorio.
	 * @throws BusinessException Si ocurre algún error de negocio.
	 */
	@Override
	public Page<Customer> getCustomers(@Valid Pageable pageable) throws BusinessException {
		log.debug("Init getCustomers");
		try {
			if (pageable.getPageSize() > Constants.MAXIMUM_PAGINATION) {
				throw new BusinessException(Errors.MAXIMUM_PAGINATION_EXCEEDED);
			}
			return customersRepository.listCustomers(pageable);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error al obtener la lista de clientes", e);
			throw new BusinessException("Error al obtener la lista de clientes");
		}
	}

	/**
	 * Obtiene un cliente específico por su ID.
	 *
	 * @param idCustomer ID del cliente a obtener.
	 * @return Cliente encontrado, o vacío si no existe.
	 * @throws BusinessException
	 */
	@Override
	public Optional<Customer> getCustomer(@Valid String idCustomer) throws BusinessException {
		log.debug("Init getCustomer");
		try {
			return customersRepository.getCustomer(idCustomer);
		} catch (Exception e) {
			log.error("Error al obtener el cliente con id" + idCustomer, e);
			throw new BusinessException("Error al obtener el cliente con id" + idCustomer);
		}
	}

	/**
	 * Busca clientes por un tipo específico de búsqueda.
	 *
	 * @param typeSearch Tipo de búsqueda (ej. firstName, lastName, email).
	 * @param value      Valor a buscar.
	 * @param pageable   Objeto Pageable que especifica la paginación de resultados.
	 * @return Página de clientes que coinciden con los criterios de búsqueda.
	 * @throws BusinessException
	 */
	@Override
	public Page<Customer> searchCustomerBy(@Valid String typeSearch, @Valid String value, @Valid Pageable pageable)
			throws BusinessException {
		log.debug("Init searchCustomerBy");
		try {
			return customersRepository.searchCustomerBy(typeSearch, value, pageable);
		} catch (IllegalArgumentException e) {
			log.error("Error en el formato de búsqueda de cliente", e);
			throw new BusinessException("Error en el formato de búsqueda de cliente");
		} catch (Exception e) {
			log.error("Error inesperado al buscar cliente", e);
			throw new BusinessException("Error inesperado al buscar cliente");
		}
	}

	/**
	 * Realiza una modificación parcial de un cliente existente.
	 *
	 * @param customer Cliente con los datos actualizados.
	 * @throws BusinessException Si ocurre algún error de negocio.
	 */
	@Override
	public void partialModificationCustomer(@Valid Customer customer) throws BusinessException {
		log.debug("Init partialModificationCustomer");
		try {
			Optional<Customer> opt = customersRepository.getCustomer(customer.getId());
			if (!opt.isPresent()) {
				throw new BusinessException(Errors.CUSTOMER_NOT_FOUND);
			}
			Customer customerUpdated = opt.get();
			customerPatchMapper.update(customerUpdated, customer);
			customersRepository.modifyCustomer(customerUpdated);
			kafkaProducer.eventModifyCustomer(customerUpdated);
			log.debug("End partialModificationCustomer");
		} catch (Exception e) {
			log.error("Error al realizar modificación parcial del cliente", e);
			throw new BusinessException("Error al realizar modificación parcial del cliente");
		}
	}

	/**
	 * Actualiza completamente un cliente existente.
	 *
	 * @param customer Cliente con los datos actualizados.
	 * @throws BusinessException Si ocurre algún error de negocio.
	 */
	@Override
	public void updateCustomer(@Valid Customer customer) throws BusinessException {
		log.debug("Init updateCustomer");
		try {
			Optional<Customer> opt = customersRepository.getCustomer(customer.getId());
			if (!opt.isPresent()) {
				throw new BusinessException(Errors.CUSTOMER_NOT_FOUND);
			}
			customersRepository.modifyCustomer(customer);
			kafkaProducer.eventModifyCustomer(customer);
			log.debug("End updateCustomer");
		} catch (Exception e) {
			log.error("Error al actualizar el cliente", e);
			throw new BusinessException("Error al actualizar el cliente");
		}
	}

	/**
	 * Elimina un cliente y sus vehículos asociados.
	 *
	 * @param idCustomer ID del cliente a eliminar.
	 * @throws BusinessException Si ocurre algún error de negocio.
	 */
	@Override
	@Transactional
	public void deleteCustomer(@Valid String idCustomer) throws BusinessException {
		log.debug("Init deleteCustomer");
		try {
			Optional<Customer> customerSavedOpt = customersRepository.getCustomer(idCustomer);
			if (!customerSavedOpt.isPresent()) {
				throw new BusinessException(Errors.CUSTOMER_NOT_FOUND);
			}

			// Elimino todos los vehículos de la lista idVehicles del cliente
			Customer customer = customerSavedOpt.get();
			customer.getIdVehicles().stream().forEach(idVehicle -> {
				Optional<Vehicle> vehicle;
				try {
					vehicle = vehiclesRepositoryOutputPort.getVehicle(idVehicle);
					if (vehicle.isPresent()) {
						/*
						 * vehicle.get().getIdWorkOrders().stream().forEach(idWorkOrder -> {
						 * workOrderRepositoryOutputPort.deleteWorkOrder(idWorkOrder); });
						 */
						vehiclesRepositoryOutputPort.deleteVehicle(idVehicle);
						vehiclesMessageOutputPort.eventDeleteVehicle(vehicle.get());
					}
				} catch (BusinessException e) {
					e.printStackTrace();
				}
			});
			customersRepository.deleteCustomer(idCustomer);
			kafkaProducer.eventDeleteCustomer(customerSavedOpt.get());
		} catch (Exception e) {
			log.error("Error al eliminar el cliente", e);
			throw new BusinessException("Error al eliminar el cliente");
		}
	}

}
