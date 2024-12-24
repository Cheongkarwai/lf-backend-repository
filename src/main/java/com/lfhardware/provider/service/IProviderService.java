package com.lfhardware.provider.service;

import com.lfhardware.appointment.domain.AppointmentId;
import com.lfhardware.appointment.domain.AppointmentStatus;
import com.lfhardware.appointment.dto.AppointmentDTO;
import com.lfhardware.form.dto.FormDTO;
import com.lfhardware.provider.domain.Status;
import com.lfhardware.provider.dto.*;
import com.lfhardware.provider_business.dto.ServiceDTO;
import com.lfhardware.core.dto.PageInfo;
import com.lfhardware.core.dto.Pageable;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface IProviderService {

    Mono<Void> save(ServiceProviderOnboardInput serviceProviderInput);

    Mono<Pageable<ServiceProviderDTO>> findAll(PageInfo pageRequest, List<String> status, List<String> states, Double rating,
                                               String serviceName);

    Mono<Pageable<ServiceProviderDetailsDTO>> findAllDetails(ServiceProviderPageRequest serviceProviderRequest);

    Mono<ServiceProviderDetailsDTO> findDetailsById(String id);

    Mono<Void> patch(String id, Map<String, Object> data);

    Mono<Void> updateStatus(String id, Status status);

    Mono<List<ServiceDTO>> findAllCurrentProviderServices();

    Mono<FormDTO> findCurrentProviderForm(Long serviceId);

    Mono<Pageable<AppointmentDTO>> findAllCurrentProviderAppointments(PageInfo pageInfo, List<String> status);

    Mono<ServiceProviderDTO> findCurrentServiceProviderByUserId();

    Mono<Pageable<ServiceProviderReviewDTO>> findAllServiceProviderReviewsById(String id, PageInfo pageInfo, Double rating);

    Mono<Long> countServiceProviderReviewsById(String id);

    Mono<List<ServiceDTO>> findAllProviderServicesById(String serviceProviderId);

    Mono<Void> updateById(String id, ServiceProviderDetailsInput serviceProviderDetailsInput);

    Mono<String> createPaymentDetails();

    Mono<Boolean> findPaymentAccountStatus();

    Mono<List<ServiceProviderReviewCountGroupByRatingDTO>> countCurrentServiceProviderReviews();

    Mono<List<ServiceProviderAppointmentCountGroupByDayDTO>> countCurrentServiceProviderAppointments(AppointmentStatus status,
                                                                                                     Integer day);

    Mono<List<ServiceProviderCountGroupByDayDTO>> countServiceProviders(Integer day);

    Mono<Void> saveServiceProviderReview(String id, AppointmentId appointmentId, ServiceProviderReviewInput serviceProviderReviewInput);
}
