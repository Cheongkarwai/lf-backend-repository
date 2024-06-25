package com.lfhardware.customer.service;

import com.lfhardware.appointment.dto.AppointmentDTO;
import com.lfhardware.customer.dto.CustomerCountGroupByDayDTO;
import com.lfhardware.customer.dto.CustomerDTO;
import com.lfhardware.customer.dto.CustomerInfoInput;
import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.Pageable;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.BodyInserter;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

public interface ICustomerService {

    Mono<CustomerDTO> findById(String id);

    Mono<Void> save(CustomerInfoInput customerInfo);

    Mono<CustomerDTO> findCurrentlyLoggedInCustomer();

    Mono<Pageable<AppointmentDTO>> findAllCurrentCustomerAppointments(PageInfo pageInfo, LocalDateTime dateTime, List<String> status);

    Mono<Pageable<CustomerDTO>> findAll(PageInfo pageRequest);

    Mono<Pageable<AppointmentDTO>> findAllAppointmentsByCustomerId(PageInfo pageRequest, String customerId);

   Mono<AppointmentDTO> findCurrentCustomerAppointmentById(Long serviceId, String serviceProviderId, LocalDateTime createdAt);

    Mono<Void> update(String id,CustomerInfoInput customerInfoInput);

    Mono<List<CustomerCountGroupByDayDTO>> countCustomers(Integer day);
}
