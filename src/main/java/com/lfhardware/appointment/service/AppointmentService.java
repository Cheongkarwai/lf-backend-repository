package com.lfhardware.appointment.service;

import com.lfhardware.appointment.cache.AppointmentCacheKey;
import com.lfhardware.appointment.domain.Appointment;
import com.lfhardware.appointment.domain.AppointmentCountGroupByDayDTO;
import com.lfhardware.appointment.domain.AppointmentId;
import com.lfhardware.appointment.domain.AppointmentStatus;
import com.lfhardware.appointment.dto.AppointmentDTO;
import com.lfhardware.appointment.dto.AppointmentFeesInput;
import com.lfhardware.appointment.dto.AppointmentInput;
import com.lfhardware.appointment.dto.AppointmentStatusInput;
import com.lfhardware.appointment.mapper.AppointmentMapper;
import com.lfhardware.appointment.repository.IAppointmentRepository;
import com.lfhardware.checkout.dto.CheckoutInput;
import com.lfhardware.checkout.dto.ServiceItemInput;
import com.lfhardware.checkout.service.ICheckoutService;
import com.lfhardware.customer.repository.CustomerRepository;
import com.lfhardware.customer.repository.ICustomerRepository;
import com.lfhardware.notification.service.INotificationService;
import com.lfhardware.notification.service.NotificationService;
import com.lfhardware.provider.repository.IProviderRepository;
import com.lfhardware.provider_business.repository.IProviderBusinessRepository;
import com.lfhardware.shared.CacheService;
import com.lfhardware.shared.Currency;
import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.stage.Stage;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AppointmentService implements IAppointmentService {

    private final IAppointmentRepository appointmentRepository;

    private final ICustomerRepository customerRepository;

    private final IProviderRepository providerRepository;

    private final IProviderBusinessRepository businessRepository;

    private final Stage.SessionFactory sessionFactory;

    private final AppointmentMapper appointmentMapper;

    private final ICheckoutService checkoutService;

    private final CacheService<AppointmentDTO> appointmentCacheService;

    private final INotificationService notificationService;


    public AppointmentService(IAppointmentRepository appointmentRepository,
                              CustomerRepository customerRepository,
                              IProviderRepository providerRepository,
                              IProviderBusinessRepository businessRepository,
                              Stage.SessionFactory sessionFactory,
                              AppointmentMapper appointmentMapper,
                              ICheckoutService checkoutService,
                              CacheService<AppointmentDTO> appointmentCacheService,
                              NotificationService notificationService) {
        //Repository
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.providerRepository = providerRepository;
        this.businessRepository = businessRepository;
        this.sessionFactory = sessionFactory;
        //Mapper
        this.appointmentMapper = appointmentMapper;
        //Service
        this.checkoutService = checkoutService;
        this.appointmentCacheService = appointmentCacheService;
        this.notificationService = notificationService;
    }

    @Override
    public Mono<Pageable<AppointmentDTO>> findAll(PageInfo pageRequest, List<String> status) {
        AppointmentCacheKey appointmentCacheKey = new AppointmentCacheKey(pageRequest, status);
        return appointmentCacheService.getCachedPageable(appointmentCacheKey)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.fromCompletionStage(sessionFactory.withSession(session ->
                                                appointmentRepository.findAll(session, pageRequest, status)
                                                        .thenApply(appointments -> appointments.stream()
                                                                .map(appointmentMapper::mapToAppointmentDTO)
                                                                .collect(Collectors.toList())))
                                        .thenCombine(sessionFactory.withSession(session -> appointmentRepository.count(session, pageRequest, status)),
                                                (appointments, totalElements) -> new Pageable<>(appointments, pageRequest.getPageSize(), pageRequest.getPage(), totalElements.intValue())))
                                .flatMap(appointmentDTOPageable -> appointmentCacheService.updateCachedPageable(appointmentCacheKey, appointmentDTOPageable))));
    }

    @Override
    public Mono<String> create(AppointmentInput appointmentInput) {

        LocalDateTime currentDateTime = LocalDateTime.now();

        return Mono.fromCallable(() -> appointmentMapper.mapToAppointmentEntity(appointmentInput))
                .flatMap(appointment -> ReactiveSecurityContextHolder.getContext()
                        .map(SecurityContext::getAuthentication)
                        .flatMap(authentication -> {
                                    AppointmentId appointmentId = new AppointmentId();
                                    appointmentId.setServiceProviderId(appointmentInput.getServiceProviderId());
                                    appointmentId.setCustomerId(authentication.getName());
                                    appointmentId.setServiceId(appointmentInput.getServiceId());
                                    appointmentId.setCreatedAt(currentDateTime);
                                    appointment.setStatus(appointmentInput.getStatus());
                                    appointment.setAppointmentId(appointmentId);


                                    return Mono.fromCompletionStage(sessionFactory.withSession(session -> customerRepository.findById(session, authentication.getName())
                                                    .thenApply(customer -> {
                                                        System.out.println(customer);
                                                        appointment.setCustomer(customer);
                                                        return appointment;
                                                    })
                                                    .thenCompose(customerWithAppointment -> providerRepository.findDetailsById(session, appointmentInput.getServiceProviderId()))
                                                    .thenApply(serviceProvider -> {
                                                        appointment.setServiceProvider(serviceProvider);
                                                        return appointment;
                                                    })
                                                    .thenCompose(service -> businessRepository.findById(session, appointmentInput.getServiceId()))
                                                    .thenApply(service -> {
                                                        appointment.setService(service);
                                                        return appointment;
                                                    })
                                                    .thenCompose(finalAppointment -> sessionFactory.withTransaction((session1, transaction) -> appointmentRepository.save(session1, appointment)))))
//                                        sessionFactory.withTransaction(session -> {
//                            System.out.println(authentication.getName());
//                                    Appointment appointment1 = new Appointment();
//                                            AppointmentId appointmentId = new AppointmentId();
//                                            appointmentId.setServiceProviderId(appointmentInput.getServiceProviderId());
//                                            appointmentId.setCustomerId(authentication.getName());
//                                            appointmentId.setServiceId(appointmentInput.getServiceId());
//                                            appointmentId.setCreatedAt(currentDateTime);
//                                            appointment1.setAppointmentId(appointmentId);
//                                            Customer customer = new Customer();
//                                            customer.setId(authentication.getName());
//                                            appointment1.setCustomer(customer);
//                                            return appointmentRepository.save(session, appointment1);
//                                        }))
                                            .then(Mono.defer(() -> notificationService.send(authentication.getName(), "You have booked an appointment for " + appointment.getService()
                                                    .getName() + " from " + appointment.getServiceProvider()
                                                    .getName())))
                                            .then(Mono.defer(() -> {
//                                                return Mono.fromCompletionStage(sessionFactory.withSession(session -> {
//                                                            return businessRepository.findById(session, appointmentInput.getServiceId());
//                                                        }))
//                                                        .flatMap(service -> {
                                                            CheckoutInput checkoutInput = new CheckoutInput();
                                                            checkoutInput.setServiceProviderId(appointmentInput.getServiceProviderId());
                                                            ServiceItemInput serviceItemInput = new ServiceItemInput();
                                                            serviceItemInput.setPrice(appointmentInput.getEstimatedPrice());
                                                            serviceItemInput.setServiceName("Hello");
                                                            serviceItemInput.setCurrency(Currency.MYR);
                                                            checkoutInput.setItems(List.of(serviceItemInput));
                                                            checkoutInput.setProcessingFees(BigDecimal.valueOf(1000));

                                                            return checkoutService.createCheckoutSession(checkoutInput);
//                                                        });
                                            }));
                                }
                        ));
    }

    @Override
    public Mono<Void> updateStatus(AppointmentStatusInput appointmentStatusInput) {
        return Mono.fromCompletionStage(sessionFactory.withTransaction(session -> appointmentRepository.findAllByIds(session,
                                appointmentStatusInput.getIds()
                                        .stream()
                                        .map(id -> new AppointmentId(id.getServiceId(), id.getServiceProviderId(), id.getCustomerId(), id.getCreatedAt()))
                                        .collect(Collectors.toList()))
                        .thenApply(appointments -> {
                            appointments.forEach(appointment -> {
                                appointment.setStatus(appointmentStatusInput.getStatus());
                                updateAppointmentDateBasedOnStatus(appointmentStatusInput.getStatus(), appointment);
                            });
                            return appointments;
                        })
                        .thenCompose(appointments -> appointmentRepository.saveAll(session, appointments)
                                .thenApply((e) -> {
                                    appointmentCacheService.removeAll();
                                    return appointments;
                                }))))
                .flatMap(appointments -> Flux.just(appointments)
                        .flatMapIterable(appointments1 -> appointments1)
                        .flatMap(appointment -> notificationService.send(appointment.getCustomer()
                                .getId(), getUpdateAppointmentStatusNotificationMessage(appointmentStatusInput.getStatus(), appointment.getId())))
                        .then());
    }

    private void updateAppointmentDateBasedOnStatus(AppointmentStatus appointmentStatus, Appointment appointment) {
        switch (appointmentStatus) {
            case REFUNDED -> {

            }
            case REJECTED -> {

            }
            case CANCELLED -> {
//                appointment.set
            }
            case COMPLETED -> {
                appointment.setCompletionDateTime(LocalDateTime.now());
            }
            case CONFIRMED -> {
                appointment.setConfirmationDatetime(LocalDateTime.now());
            }
            case WORK_IN_PROGRESS -> {
                appointment.setJobStartedDatetime(LocalDateTime.now());
            }
            case REVIEW -> {
                appointment.setReviewDatetime(LocalDateTime.now());
            }
        }

        appointment.setStatusLastUpdate(LocalDateTime.now());
    }

    private String getUpdateAppointmentStatusNotificationMessage(AppointmentStatus appointmentStatus, UUID appointmentId) {
        return switch (appointmentStatus) {
            case WORK_COMPLETED -> "Appointment #" + appointmentId + " work has been completed";
            case REVIEW -> "Appointment #" + appointmentId + " will be reviewed by Admin";
            case COMPLETED -> "Appointment #" + appointmentId + " has been completed";
            case PENDING -> "Appointment #" + appointmentId + " has been created";
            case CANCELLED -> "Appointment #" + appointmentId + " has been cancelled";
            case REFUNDED -> "Appointment #" + appointmentId + " has been refunded";
            case WORK_IN_PROGRESS -> "Appointment #" + appointmentId + " work is in progress";
            case REJECTED -> "Appointment #" + appointmentId + " has been rejected";
            case CONFIRMED -> "Appointment #" + appointmentId + " has been confirmed";
        };
    }

    @Override
    public Mono<String> payAppointment(AppointmentFeesInput appointmentFeesInput, String serviceId, String serviceProviderId,
                                       String customerId, LocalDateTime createdAt) {


        return Mono.empty();

//        return businessRepository.findById()checkoutService.createCheckoutSession(Map.of("created_at", createdAt.toString(),
//                "service_id", serviceId, "service_provider_id", serviceProviderId, "customer_id", customerId));
    }

    @Override
    public Mono<AppointmentDTO> findById(AppointmentId appointmentId) {

        Mono<AppointmentDTO> appointmentDTOMono = Mono.fromCompletionStage(sessionFactory.withSession(session -> appointmentRepository.findById(session, appointmentId)
                        .thenApply(appointmentMapper::mapToAppointmentDTO)))
                .flatMap(appointmentDTO -> appointmentCacheService.updateCachedObject(appointmentId, appointmentDTO));

        return appointmentCacheService.getCachedObject(appointmentId)
                .switchIfEmpty(Mono.defer(() -> appointmentDTOMono));
    }

    @Override
    public Mono<List<AppointmentCountGroupByDayDTO>> countAppointments(AppointmentStatus appointmentStatus, Integer day) {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> appointmentRepository.countAppointmentsGroupByDay(session, appointmentStatus, day)));
    }
}
