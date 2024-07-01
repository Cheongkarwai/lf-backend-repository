package com.lfhardware.customer.service;

import com.lfhardware.appointment.domain.AppointmentId;
import com.lfhardware.appointment.dto.AppointmentDTO;
import com.lfhardware.auth.domain.Address;
import com.lfhardware.auth.dto.Role;
import com.lfhardware.auth.service.IUserService;
import com.lfhardware.auth.service.RoleService;
import com.lfhardware.customer.dto.CustomerCountGroupByDayDTO;
import com.lfhardware.customer.dto.CustomerDTO;
import com.lfhardware.appointment.mapper.AppointmentMapper;
import com.lfhardware.appointment.repository.IAppointmentRepository;
import com.lfhardware.customer.domain.Customer;
import com.lfhardware.customer.cache.CustomerAppointmentCacheKey;
import com.lfhardware.customer.dto.CustomerInfoInput;
import com.lfhardware.customer.mapper.CustomerMapper;
import com.lfhardware.customer.repository.ICustomerRepository;
import com.lfhardware.configuration.CacheConfiguration;
import com.lfhardware.notification.service.INotificationService;
import com.lfhardware.shared.CacheService;
import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.Pageable;
import org.hibernate.reactive.stage.Stage;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Service
public class CustomerService implements ICustomerService {

    private final ICustomerRepository customerRepository;

    private final IAppointmentRepository appointmentRepository;

    private final Stage.SessionFactory sessionFactory;

    private final CustomerMapper customerMapper;

    private final AppointmentMapper appointmentMapper;

    private final IUserService userService;

    private final RoleService roleService;

    private final CacheManager cacheManager;

    private final CacheService<CustomerDTO> customerCacheService;

    private final CacheService<AppointmentDTO> appointmentCacheService;

    private final INotificationService notificationService;


    public CustomerService(ICustomerRepository customerRepository,
                           IAppointmentRepository appointmentRepository,
                           Stage.SessionFactory sessionFactory,
                           CustomerMapper customerMapper,
                           AppointmentMapper appointmentMapper,
                           CacheManager cacheManager,
                           IUserService userService,
                           RoleService roleService,
                           INotificationService notificationService,
                           CacheService<CustomerDTO> customerCacheService,
                           CacheService<AppointmentDTO> appointmentCacheService) {
        this.customerRepository = customerRepository;
        this.appointmentRepository = appointmentRepository;
        this.sessionFactory = sessionFactory;
        this.customerMapper = customerMapper;
        this.appointmentMapper = appointmentMapper;
        this.cacheManager = cacheManager;
        this.userService = userService;
        this.roleService = roleService;
        this.notificationService = notificationService;
        this.customerCacheService = customerCacheService;
        this.appointmentCacheService = appointmentCacheService;
    }

    @Override
    public Mono<CustomerDTO> findById(String id) {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> customerRepository.findById(session, id)
                        .thenApply(customerMapper::mapToCustomerDTO)))
                .flatMap(customerDTO -> {
                    return userService.findById(id)
                            .map(userRepresentation -> {
                                customerDTO.setFirstName(userRepresentation.getFirstName());
                                customerDTO.setLastName(userRepresentation.getLastName());
                                customerDTO.setEmailAddress(userRepresentation.getEmail());
                                customerDTO.setEmailVerified(userRepresentation.isEmailVerified());
                                customerDTO.setEnabled(userRepresentation.isEnabled());
                                return customerDTO;
                            });
                });
    }


    @Override
    public Mono<Void> save(CustomerInfoInput customerInfoInput) {

        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> Mono.fromCallable(() -> {
                            Customer customer = customerMapper.mapToCustomer(customerInfoInput);
                            customer.setId(authentication.getName());
                            return customer;
                        })
                        .flatMap(customer -> Mono.fromCompletionStage(sessionFactory.withTransaction(session ->
                                customerRepository.save(session, customer))))
                        .then(roleService.findByName(Role.customer.name())
                                .flatMap(roleRepresentations -> userService.updateUserRoleById(authentication.getName(), roleRepresentations))
                                .then(notificationService.sendProfileUpdatedNotification(authentication.getName()))));
    }

    @Override
    public Mono<CustomerDTO> findCurrentlyLoggedInCustomer() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> Mono.fromCompletionStage(sessionFactory.withSession(session -> customerRepository.findById(session, authentication.getName())
                        .thenApply(customerMapper::mapToCustomerDTO))));
    }

    @Override
    public Mono<Pageable<AppointmentDTO>> findAllCurrentCustomerAppointments(PageInfo pageRequest, LocalDateTime bookingDateTime, List<String> status) {

        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    CustomerAppointmentCacheKey customerAppointmentCacheKey = new CustomerAppointmentCacheKey(pageRequest, authentication.getName(), bookingDateTime, status);
                    return appointmentCacheService.getCachedPageable(customerAppointmentCacheKey)
                            .switchIfEmpty(Mono.defer(() -> Mono.fromCompletionStage(sessionFactory.withSession(session ->
                                                    appointmentRepository.findAllByCustomerIdAndBookingDateAndStatus(session, pageRequest, authentication.getName(), bookingDateTime, status)
                                            )
                                            .thenCombine(sessionFactory.withSession(appointmentSession -> appointmentRepository.countByCustomerIdAndBookingDateAndStatus(appointmentSession,
                                                            pageRequest, authentication.getName(), bookingDateTime, status)),
                                                    ((appointments, totalElements) -> new Pageable<>(
                                                            appointments.stream()
                                                                    .map(appointmentMapper::mapToAppointmentDTO)
                                                                    .toList(),
                                                            pageRequest.getPageSize(),
                                                            pageRequest.getPage(), totalElements.intValue()))))
                                    .flatMap(appointmentDTOPageable -> appointmentCacheService.updateCachedPageable(customerAppointmentCacheKey, appointmentDTOPageable))));
                });

    }

    @Override
    public Mono<Pageable<CustomerDTO>> findAll(PageInfo pageRequest) {
        return customerCacheService.getCachedPageable(pageRequest)
                .switchIfEmpty(Mono.defer(() -> Mono.fromCompletionStage(sessionFactory.withSession(session -> customerRepository.findAll(session, pageRequest)
                                .thenApply(customers -> {
                                    return customers.stream()
                                            .map(customerMapper::mapToCustomerDTO)
                                            .collect(Collectors.toList());
                                })))
                        .flatMapIterable(customerDTOS -> customerDTOS)
                        .flatMap(customerDTO -> {
                            return userService.findById(customerDTO.getId())
                                    .map(user -> {
                                        customerDTO.setFirstName(user.getFirstName());
                                        customerDTO.setLastName(user.getLastName());
                                        return customerDTO;
                                    });
                        })
                        .collectList()
                        .flatMap(customerDTOS -> {
                            return Mono.fromCompletionStage(sessionFactory.withSession(session -> {
                                return customerRepository.count(session, pageRequest)
                                        .thenApply(totalElements -> {
                                            return new Pageable<>(customerDTOS,
                                                    pageRequest.getPageSize(),
                                                    pageRequest.getPage(),
                                                    totalElements.intValue());
                                        });
                            }));
                        })
                        .flatMap(customerDTOPageable -> customerCacheService.updateCachedPageable(pageRequest, customerDTOPageable))));
    }

    @Override
    public Mono<Pageable<AppointmentDTO>> findAllAppointmentsByCustomerId(PageInfo pageRequest, String customerId) {

        CustomerAppointmentCacheKey customerAppointmentCacheKey = new CustomerAppointmentCacheKey(pageRequest, customerId, null, null);

        return Mono.justOrEmpty(Objects.requireNonNull(cacheManager.getCache(CacheConfiguration.customerAppointmentCache))
                        .get(customerAppointmentCacheKey, (Callable<List<AppointmentDTO>>) ArrayList::new))
                .flatMap(cacheAppointments -> !cacheAppointments.isEmpty() ?
                        Mono.just(cacheAppointments)
                                .flatMap(appointments -> Mono.fromCompletionStage(sessionFactory.withSession(session -> appointmentRepository.countByCustomerId(session, pageRequest, customerId)
                                        .thenApply(totalElements -> new Pageable<>(
                                                appointments,
                                                pageRequest.getPageSize(),
                                                pageRequest.getPage(), totalElements.intValue()))))) : Mono.empty())

                .switchIfEmpty(Mono.defer(() -> Mono.fromCompletionStage(sessionFactory.withSession(session ->
                                appointmentRepository.findAllByCustomerId(session, pageRequest, customerId))
                        .thenCombine(sessionFactory.withSession(appointmentSession -> appointmentRepository.countByCustomerId(appointmentSession, pageRequest, customerId)),
                                ((appointments, totalElements) -> {
                                    System.out.println(totalElements);
                                    List<AppointmentDTO> appointmentDTOS = appointments.stream()
                                            .map(appointmentMapper::mapToAppointmentDTO)
                                            .toList();
                                    Objects.requireNonNull(cacheManager.getCache(CacheConfiguration.customerAppointmentCache))
                                            .putIfAbsent(customerAppointmentCacheKey, appointmentDTOS);
                                    return new Pageable<>(
                                            appointmentDTOS,
                                            pageRequest.getPageSize(),
                                            pageRequest.getPage(), totalElements.intValue());
                                })))));
    }

    @Override
    public Mono<AppointmentDTO> findCurrentCustomerAppointmentById(Long serviceId, String serviceProviderId, LocalDateTime createdAt) {
        AppointmentId appointmentId = new AppointmentId();
        appointmentId.setServiceId(serviceId);
        appointmentId.setServiceProviderId(serviceProviderId);
        appointmentId.setCreatedAt(createdAt);
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    appointmentId.setCustomerId(authentication.getName());
                    return appointmentCacheService.getCachedObject(appointmentId)
                            .switchIfEmpty(Mono.defer(() ->
                                    Mono.fromCompletionStage(sessionFactory.withSession(session ->
                                                    appointmentRepository.findById(session, appointmentId)
                                                            .thenApply(appointmentMapper::mapToAppointmentDTO)))
                                            .flatMap(appointmentDTO -> appointmentCacheService.updateCachedObject(appointmentId, appointmentDTO))));
                });
    }

    public Mono<Void> update(String id, CustomerInfoInput customerInfoInput) {
        return Mono.fromCompletionStage(sessionFactory.withTransaction((session, transaction) -> customerRepository.findById(session, id)
                        .thenCompose(customer -> {
                            Address customerAddress = customer.getAddress();
                            customerAddress.setAddressLine1(customerInfoInput.getAddress()
                                    .getAddressLine1());
                            customerAddress.setAddressLine2(customerInfoInput.getAddress()
                                    .getAddressLine2());
                            customerAddress.setCity(customerInfoInput.getAddress()
                                    .getCity());
                            customerAddress.setState(customerInfoInput.getAddress()
                                    .getState());
                            customerAddress.setZipcode(customerInfoInput.getAddress()
                                    .getZipcode());
                            customer.setPhoneNumber(customerInfoInput.getPhoneNumber());
                            customer.setAddress(customerAddress);
                            return customerRepository.save(session, customer);
                        })
                        .thenAccept((e) -> customerCacheService.removeAll())))
                .then(Mono.defer(() -> userService.findById(id)
                        .flatMap(userRepresentation -> {
                            userRepresentation.setFirstName(customerInfoInput.getFirstName());
                            userRepresentation.setLastName(customerInfoInput.getLastName());
                            userRepresentation.setEmail(customerInfoInput.getEmailAddress());
                            userRepresentation.setEmailVerified(customerInfoInput.isEmailVerified());
                            userRepresentation.setEnabled(customerInfoInput.isEnabled());
                            return userService.update(id, userRepresentation);
                        })));
    }

    @Override
    public Mono<List<CustomerCountGroupByDayDTO>> countCustomers(Integer day) {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> customerRepository.countCustomerGroupByDay(session, day)));
    }
}
