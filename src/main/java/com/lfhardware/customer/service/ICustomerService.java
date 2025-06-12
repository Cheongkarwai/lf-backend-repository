package com.lfhardware.customer.service;

import com.lfhardware.appointment.dto.AppointmentDTO;
import com.lfhardware.customer.dto.CustomerCountGroupByDayDTO;
import com.lfhardware.customer.dto.CustomerDTO;
import com.lfhardware.customer.dto.CustomerInfoInput;
import com.lfhardware.core.dto.PageRequest;
import com.lfhardware.core.dto.Page;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

public interface ICustomerService {

    Mono<CustomerDTO> findById(String id);

    Mono<Void> save(CustomerInfoInput customerInfo);

    Mono<CustomerDTO> findCurrentlyLoggedInCustomer();

    Mono<Page<AppointmentDTO>> findAllCurrentCustomerAppointments(PageRequest pageRequest, LocalDateTime dateTime, List<String> status);

    Mono<Page<CustomerDTO>> findAll(PageRequest pageRequest);

    Mono<Page<AppointmentDTO>> findAllAppointmentsByCustomerId(PageRequest pageRequest, String customerId);

   Mono<AppointmentDTO> findCurrentCustomerAppointmentById(Long serviceId, String serviceProviderId, LocalDateTime createdAt);

    Mono<Void> update(String id,CustomerInfoInput customerInfoInput);

    Mono<List<CustomerCountGroupByDayDTO>> countCustomers(Integer day);
}
