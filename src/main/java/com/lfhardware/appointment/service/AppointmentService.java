package com.lfhardware.appointment.service;

import com.lfhardware.appointment.cache.AppointmentCacheKey;
import com.lfhardware.appointment.domain.Appointment;
import com.lfhardware.appointment.dto.AppointmentCountGroupByDayDTO;
import com.lfhardware.appointment.domain.AppointmentId;
import com.lfhardware.appointment.domain.AppointmentStatus;
import com.lfhardware.appointment.dto.AppointmentDTO;
import com.lfhardware.appointment.dto.AppointmentFeesInput;
import com.lfhardware.appointment.dto.AppointmentInput;
import com.lfhardware.appointment.dto.AppointmentStatusInput;
import com.lfhardware.appointment.mapper.AppointmentMapper;
import com.lfhardware.appointment.repository.IAppointmentRepository;
import com.lfhardware.charges.service.IPaymentService;
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
import com.stripe.model.Charge;
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

    private final IPaymentService paymentService;


    public AppointmentService(IAppointmentRepository appointmentRepository,
                              CustomerRepository customerRepository,
                              IProviderRepository providerRepository,
                              IProviderBusinessRepository businessRepository,
                              Stage.SessionFactory sessionFactory,
                              AppointmentMapper appointmentMapper,
                              ICheckoutService checkoutService,
                              CacheService<AppointmentDTO> appointmentCacheService,
                              NotificationService notificationService,
                              IPaymentService paymentService) {
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
        this.paymentService = paymentService;
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
                                    //Transfer Group
                                    String transferGroup = UUID.randomUUID()
                                            .toString();

                                    AppointmentId appointmentId = new AppointmentId();
                                    appointmentId.setServiceProviderId(appointmentInput.getServiceProviderId());
                                    appointmentId.setCustomerId(authentication.getName());
                                    appointmentId.setServiceId(appointmentInput.getServiceId());
                                    appointmentId.setCreatedAt(currentDateTime);
                                    appointment.setStatus(appointmentInput.getStatus());
                                    appointment.setAppointmentId(appointmentId);
                                    appointment.setBookingDatetime(LocalDateTime.now());
                                    appointment.setId(UUID.randomUUID());

                                    return Mono.fromCompletionStage(sessionFactory.withSession(session -> customerRepository.findById(session, authentication.getName())
                                            .thenApply(customer -> {
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
                                            .thenCompose(finalAppointment -> sessionFactory.withTransaction((session1, transaction) -> {
                                                        //With transactions
                                                        return appointmentRepository.save(session1, appointment)
                                                                .thenAccept((v) -> appointmentCacheService.removeAll())
                                                                .thenCompose(e -> {
                                                                    return notificationService.sendBookingAppointmentNotification(authentication.getName(), "", appointment.getServiceProvider()
                                                                                            .getName(),
                                                                                    appointment.getService()
                                                                                            .getName())
                                                                            .toFuture()
                                                                            .toCompletableFuture();
                                                                })
                                                                .thenCompose(e -> {
                                                                    CheckoutInput checkoutInput = new CheckoutInput();
                                                                    checkoutInput.setServiceProviderId(appointmentInput.getServiceProviderId());
                                                                    checkoutInput.setCustomerId(authentication.getName());
                                                                    ServiceItemInput serviceItemInput = new ServiceItemInput();
                                                                    serviceItemInput.setPrice(appointmentInput.getEstimatedPrice()
                                                                            .movePointRight(2)
                                                                            .longValueExact());
                                                                    serviceItemInput.setServiceName("Hello");
                                                                    serviceItemInput.setCurrency(Currency.MYR);
                                                                    checkoutInput.setItems(List.of(serviceItemInput));
                                                                    checkoutInput.setProcessingFees(BigDecimal.valueOf(1000));
                                                                    return checkoutService.createCheckoutSession(transferGroup, checkoutInput)
                                                                            .toFuture()
                                                                            .toCompletableFuture();
                                                                })
                                                                .thenCompose(checkoutSession -> {
                                                                    return sessionFactory.withTransaction(updateSession -> {
                                                                                appointment.setCheckoutSessionId(checkoutSession.getId());
                                                                                appointment.setTransferGroup(transferGroup);
                                                                                return appointmentRepository.save(updateSession, appointment);
                                                                            })
                                                                            .thenApply(v -> checkoutSession.getUrl());
                                                                });

                                                    }
                                            ))));
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
                        .flatMap(appointment -> notificationService.sendAppointmentUpdateNotification(appointment.getCustomer()
                                .getId(), appointmentStatusInput.getStatus(), appointment.getId()))
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

    @Override
    public Mono<String> payAppointment(AppointmentFeesInput appointmentFeesInput, String serviceId, String serviceProviderId,
                                       String customerId, LocalDateTime createdAt) {

        AppointmentId appointmentId = new AppointmentId();
        appointmentId.setServiceId(Long.valueOf(serviceId));
        appointmentId.setServiceProviderId(serviceProviderId);
        appointmentId.setCustomerId(customerId);
        appointmentId.setCreatedAt(createdAt);
        String transferGroup = UUID.randomUUID()
                .toString();
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> Mono.fromCompletionStage(sessionFactory.withSession(session -> appointmentRepository.findById(session, appointmentId)
                        .thenCompose(appointment -> {
                            return sessionFactory.withTransaction((session1, transaction) -> {
                                        CheckoutInput checkoutInput = new CheckoutInput();
                                        checkoutInput.setServiceProviderId(serviceProviderId);
                                        checkoutInput.setCustomerId(authentication.getName());
                                        ServiceItemInput serviceItemInput = new ServiceItemInput();
                                        serviceItemInput.setPrice(appointment.getEstimatedPrice()
                                                .movePointRight(2)
                                                .longValueExact());
                                        serviceItemInput.setServiceName("Hello");
                                        serviceItemInput.setCurrency(Currency.MYR);
                                        checkoutInput.setItems(List.of(serviceItemInput));
                                        checkoutInput.setProcessingFees(BigDecimal.valueOf(1000));
                                        return checkoutService.createCheckoutSession(transferGroup, checkoutInput)
                                                .toFuture()
                                                .toCompletableFuture();
                                    })
                                    .thenCompose(checkoutSession -> {
                                        return sessionFactory.withTransaction(updateSession -> {
                                                    appointment.setCheckoutSessionId(checkoutSession.getId());
                                                    appointment.setTransferGroup(transferGroup);
                                                    return appointmentRepository.save(updateSession, appointment);
                                                })
                                                .thenApply(v -> checkoutSession.getUrl());
                                    });
                        }))));
    }

    @Override
    public Mono<Void> transferAppointmentFunds(Long serviceId, String serviceProviderId, String customerId, LocalDateTime createdAt) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    return Mono.fromCompletionStage(sessionFactory.withSession(session -> {
                                AppointmentId appointmentId = new AppointmentId();
                                appointmentId.setServiceProviderId(serviceProviderId);
                                appointmentId.setCustomerId(customerId);
                                appointmentId.setServiceId(serviceId);
                                appointmentId.setCreatedAt(createdAt);
                                return providerRepository.findDetailsById(session, serviceProviderId)
                                        .thenCompose(serviceProvider -> {
                                            return appointmentRepository.findById(session, appointmentId)
                                                    .thenCompose(appointment -> {
                                                        if (appointment.isPaid()) {
                                                            return sessionFactory.withTransaction((session1, transaction) -> {
                                                                        appointment.setStatus(AppointmentStatus.COMPLETED);
                                                                        appointment.setCompletionDateTime(LocalDateTime.now());
                                                                        return appointmentRepository.save(session1, appointment);
                                                                    })
                                                                    .thenCompose(v -> {
                                                                        return paymentService.transferFunds(Currency.MYR, appointment.getEstimatedPrice()
                                                                                        .longValue(), appointment.getTransferGroup(), serviceProvider.getStripeAccountId())
                                                                                .toFuture()
                                                                                .toCompletableFuture();
                                                                    })
                                                                    .thenCompose(v -> {
                                                                        return notificationService.sendAppointmentUpdateNotification(customerId, AppointmentStatus.COMPLETED, appointment.getId())
                                                                                .toFuture()
                                                                                .toCompletableFuture();
                                                                    })
                                                                    .thenCompose(v -> {
                                                                        return notificationService.sendReceiveFundsNotification(serviceProviderId, appointment.getEstimatedPrice(), appointment.getId())
                                                                                .toFuture()
                                                                                .newIncompleteFuture()
                                                                                .toCompletableFuture();
                                                                    })
                                                                    .thenCompose(v -> {
                                                                        return notificationService.sendTransferFundCompletedNotification(authentication.getName(), appointment.getEstimatedPrice(), appointment.getId())
                                                                                .toFuture()
                                                                                .toCompletableFuture();
                                                                    });
                                                        }
                                                        //Do nothing if appointment is not paid
                                                        return null;
                                                    });
                                        });
                            }))
                            .then();
                });
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

    @Override
    public Mono<Void> fulfillAppointment(String checkoutSessionId, String paymentIntentId) {
        return Mono.fromCompletionStage(sessionFactory.withTransaction(session -> {
            return appointmentRepository.findByCheckoutSessionId(session, checkoutSessionId)
                    .thenCompose(appointment -> {
                        appointment.setPaymentIntentId(paymentIntentId);
                        appointment.setPaid(true);
                        return appointmentRepository.save(session, appointment)
                                .thenCompose(e -> {
                                    return notificationService.sendPaymentCompletedNotification(appointment.getCustomer()
                                                    .getId(), appointment.getService()
                                                    .getName(), appointment.getServiceProvider()
                                                    .getName(), appointment.getEstimatedPrice())
                                            .toFuture()
                                            .toCompletableFuture();
                                });
                    })
                    .thenAccept(e -> appointmentCacheService.removeAll());
        }));
    }

    @Override
    public Mono<String> findReceiptById(AppointmentId appointmentId) {
        return Mono.fromCompletionStage(sessionFactory.withSession(session ->
                appointmentRepository.findById(session, appointmentId)
                        .thenCompose(appointment -> paymentService.findChargeByPaymentIntent(appointment.getPaymentIntentId())
                                .toFuture()
                                .toCompletableFuture())
                        .thenApply(Charge::getReceiptUrl)));
    }
}
