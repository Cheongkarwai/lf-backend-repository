package com.lfhardware.provider.service;

import com.lfhardware.account.service.IAccountService;
import com.lfhardware.appointment.domain.AppointmentStatus;
import com.lfhardware.appointment.dto.AppointmentDTO;
import com.lfhardware.appointment.mapper.AppointmentMapper;
import com.lfhardware.appointment.repository.IAppointmentRepository;
import com.lfhardware.auth.domain.Address;
import com.lfhardware.auth.dto.AccountDTO;
import com.lfhardware.auth.dto.Role;
import com.lfhardware.auth.service.RoleService;
import com.lfhardware.customer.domain.Customer;
import com.lfhardware.customer.dto.CustomerDTO;
import com.lfhardware.provider.cache.ServiceProviderCacheKey;
import com.lfhardware.provider.repository.IServiceDetailsRepository;
import com.lfhardware.shared.CacheService;
import com.lfhardware.email.service.IEmailService;
import com.lfhardware.form.domain.FormId;
import com.lfhardware.form.dto.FormDTO;
import com.lfhardware.form.mapper.FormMapper;
import com.lfhardware.form.repository.IFormRepository;
import com.lfhardware.provider.cache.ServiceProviderReviewCacheKey;
import com.lfhardware.provider.domain.*;
import com.lfhardware.provider.dto.*;
import com.lfhardware.provider.mapper.ServiceProviderMapper;
import com.lfhardware.provider.mapper.ServiceProviderReviewMapper;
import com.lfhardware.provider.repository.IProviderRepository;
import com.lfhardware.provider.repository.IServiceProviderReviewRepository;
import com.lfhardware.provider.repository.ServiceDetailsRepository;
import com.lfhardware.provider_business.dto.ServiceDTO;
import com.lfhardware.provider_business.mapper.ServiceDetailsMapper;
import com.lfhardware.provider_business.repository.IProviderBusinessRepository;
import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.Pageable;
import com.lfhardware.state.domain.State;
import com.lfhardware.state.repository.IStateRepository;
import com.stripe.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.stage.Stage;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProviderService implements IProviderService {

    private final Stage.SessionFactory sessionFactory;

    private final IProviderRepository providerRepository;

    private final IProviderBusinessRepository providerBusinessRepository;

    private final IServiceDetailsRepository serviceDetailsRepository;
    private final IEmailService emailService;

    private final IStateRepository stateRepository;

    private final IFormRepository formRepository;

    private final IAppointmentRepository appointmentRepository;

    private final IServiceProviderReviewRepository serviceProviderReviewRepository;

    private final ServiceDetailsMapper serviceDetailsMapper;
    private final ServiceProviderMapper serviceProviderMapper;

    private final ServiceProviderReviewMapper serviceProviderReviewMapper;

    private final FormMapper formMapper;

    private final AppointmentMapper appointmentMapper;

    private final IAccountService accountService;

    private final RoleService roleService;

    private final CacheService<ServiceProviderDTO> serviceProviderCacheService;

    private final CacheService<ServiceDTO> serviceDTOCacheService;

    private final CacheService<AppointmentDTO> appointmentCacheService;

    private final CacheService<ServiceProviderDetailsDTO> serviceProviderDetailsCacheService;

    private final CacheService<ServiceProviderReviewDTO> serviceProviderReviewCacheService;


    public ProviderService(Stage.SessionFactory sessionFactory,
                           IProviderRepository providerRepository,
                           IProviderBusinessRepository providerBusinessRepository,
                           IEmailService emailService,
                           IStateRepository stateRepository,
                           IFormRepository formRepository,
                           IServiceProviderReviewRepository serviceProviderReviewRepository,
                           IServiceDetailsRepository serviceDetailsRepository,
                           IAppointmentRepository appointmentRepository,
                           ServiceProviderMapper serviceProviderMapper,
                           ServiceDetailsMapper serviceDetailsMapper,
                           ServiceProviderReviewMapper serviceProviderReviewMapper,
                           FormMapper formMapper,
                           AppointmentMapper appointmentMapper,
                           IAccountService accountService,
                           RoleService roleService,
                           CacheService<ServiceProviderDTO> serviceProviderCacheService,
                           CacheService<ServiceDTO> serviceDTOCacheService,
                           CacheService<AppointmentDTO> appointmentCacheService,
                           CacheService<ServiceProviderDetailsDTO> serviceProviderDetailsCacheService,
                           CacheService<ServiceProviderReviewDTO> serviceProviderReviewCacheService) {
        //Session
        this.sessionFactory = sessionFactory;

        //Service
        this.emailService = emailService;

        //Repository
        this.providerRepository = providerRepository;
        this.providerBusinessRepository = providerBusinessRepository;
        this.stateRepository = stateRepository;
        this.formRepository = formRepository;
        this.appointmentRepository = appointmentRepository;
        this.serviceDetailsRepository = serviceDetailsRepository;
        this.serviceProviderReviewRepository = serviceProviderReviewRepository;
        //Mapper
        this.serviceProviderMapper = serviceProviderMapper;
        this.serviceDetailsMapper = serviceDetailsMapper;
        this.serviceProviderReviewMapper = serviceProviderReviewMapper;
        this.formMapper = formMapper;
        this.appointmentMapper = appointmentMapper;

        this.accountService = accountService;
        this.roleService = roleService;

        //Cache Service
        this.serviceProviderCacheService = serviceProviderCacheService;
        this.serviceDTOCacheService = serviceDTOCacheService;
        this.appointmentCacheService = appointmentCacheService;
        this.serviceProviderDetailsCacheService = serviceProviderDetailsCacheService;
        this.serviceProviderReviewCacheService = serviceProviderReviewCacheService;
    }

    @Override
    public Mono<Pageable<ServiceProviderDTO>> findAll(PageInfo pageRequest, List<String> status, List<String> states, Double rating,
                                                      String serviceName) {

        ServiceProviderCacheKey serviceProviderCacheKey = new ServiceProviderCacheKey(pageRequest, status, states, rating, serviceName);

        return serviceProviderCacheService.getCachedPageable(serviceProviderCacheKey)
                .switchIfEmpty(Mono.defer(() -> Mono.fromCompletionStage(sessionFactory.withSession(session -> providerRepository.findAll(session, pageRequest, status,
                                                states, rating, serviceName)
                                        .thenApply(serviceProviders -> serviceProviders.stream()
                                                .map(serviceProviderMapper::mapToServiceProviderDTO)
                                                .collect(Collectors.toList())))
                                .thenCombine(sessionFactory.withSession(session ->
                                        providerRepository.count(session, pageRequest, status,
                                                states, rating, serviceName)), ((serviceProviders, totalElements) ->
                                        new Pageable<>(serviceProviders, pageRequest.getPageSize(), pageRequest.getPage(), totalElements.intValue()))))
                        .flatMap(serviceProviderDTOPageable -> serviceProviderCacheService.updateCachedPageable(serviceProviderCacheKey, serviceProviderDTOPageable))));
    }

    @Override
    public Mono<Pageable<ServiceProviderDetailsDTO>> findAllDetails(ServiceProviderPageRequest serviceProviderRequest) {
        return Mono.fromCompletionStage(sessionFactory.withSession(session ->
                        providerRepository.findAll(session, serviceProviderRequest, List.of(), List.of(), null, null))
                .thenCombine(sessionFactory.withSession(session -> providerRepository.count(session, serviceProviderRequest, List.of(), List.of(),
                        null, null)), ((serviceProviders, totalElements) -> new Pageable<>(serviceProviders.stream()
                        .map(serviceProviderMapper::mapToServiceProviderDetailsDTO)
                        .collect(Collectors.toList()), serviceProviderRequest.getPageSize(), serviceProviderRequest.getPage(), totalElements.intValue()))));
    }

    @Override
    public Mono<ServiceProviderDetailsDTO> findDetailsById(String id) {

        Mono<ServiceProviderDetailsDTO> serviceProviderDetailsDTOMono = Mono.fromCompletionStage(sessionFactory.withSession(session -> providerRepository.findDetailsById(session, id)
                        .thenApply(serviceProviderMapper::mapToServiceProviderDetailsDTO)))
                .flatMap(serviceProviderDetailsDTO -> serviceProviderDetailsCacheService.updateCachedObject(id, serviceProviderDetailsDTO));

        return serviceProviderDetailsCacheService.getCachedObject(id)
                .switchIfEmpty(Mono.defer(() -> serviceProviderDetailsDTOMono));
    }

    @Override
    public Mono<Void> patch(String id, Map<String, Object> data) {

        return Mono.empty();
//        return Mono.fromCompletionStage(sessionFactory.withTransaction((session, transaction) -> providerRepository.findById(session, id).thenCompose(serviceProvider -> {
//            for (Map.Entry<String, Object> entry : data.entrySet()) {
//                String key = entry.getKey();
//                Object value = entry.getValue();
//
//                switch (key) {
//                    case "business_name" -> serviceProvider.setBusinessName((String) value);
//                    case "business_email_address" -> serviceProvider.setBusinessEmailAddress((String) value);
//                    case "business_address" -> serviceProvider.setBusinessAddress((String) value);
//                    case "business_description" -> serviceProvider.setBusinessDescription((String) value);
//                    case "social_media_link" -> serviceProvider.setSocialMediaLink((SocialMediaLink) value);
//                    case "rating" -> serviceProvider.setRating((double) value);
//                    case "overview" -> serviceProvider.setOverview((String) value);
//                    case "status" -> serviceProvider.setStatus(Status.valueOf((String) value));
//                }
//            }
//            return providerRepository.save(session, serviceProvider);
//        })));
    }

    public Mono<List<ServiceDTO>> findAllCurrentProviderServices() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication ->
                        Mono.fromCompletionStage(sessionFactory.withSession(session ->
                                serviceDetailsRepository.findAllByServiceProviderId(authentication.getName(), session)
                                        .thenApply(services -> services.stream()
                                                .map(serviceDetailsMapper::mapToServiceDTO)
                                                .collect(Collectors.toList())))));
    }

    public Mono<FormDTO> findCurrentProviderForm(Long serviceId) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication ->
                        Mono.fromCompletionStage(sessionFactory.withSession(session -> {
                            FormId formId = new FormId();
                            formId.setServiceId(serviceId);
                            formId.setServiceProviderId(authentication.getName());
                            return formRepository.findById(session, formId)
                                    .thenApply(formMapper::mapToFormDTO);
                        })));
    }

    public Mono<Pageable<AppointmentDTO>> findAllCurrentProviderAppointments(PageInfo pageRequest, List<String> status) {

        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    ServiceProviderAppointmentCacheKey cacheKey = new ServiceProviderAppointmentCacheKey(authentication.getName(), status, pageRequest);

                    return appointmentCacheService.getCachedPageable(cacheKey)
                            .switchIfEmpty(Mono.defer(() -> {
                                return Mono.fromCompletionStage(sessionFactory.withSession(session ->
                                                        appointmentRepository.findAllByServiceProviderId(session, pageRequest, authentication.getName(), status)
                                                                .thenApply(appointments -> appointments.stream()
                                                                        .map(appointmentMapper::mapToAppointmentDTO)
                                                                        .collect(Collectors.toList())))
                                                .thenCombine(sessionFactory.withSession(appointmentSession ->
                                                                appointmentRepository.countByServiceProviderId(appointmentSession, pageRequest, authentication.getName(), status)),
                                                        ((appointments, totalElements) -> new Pageable<>(
                                                                appointments,
                                                                pageRequest.getPageSize(),
                                                                pageRequest.getPage(), totalElements.intValue()))))
                                        .flatMap(appointmentDTOPageable -> appointmentCacheService.updateCachedPageable(cacheKey, appointmentDTOPageable));
                            }));
                });
    }

    @Override
    public Mono<ServiceProviderDTO> findCurrentServiceProviderByUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication ->
                        Mono.fromCompletionStage(sessionFactory.withTransaction(session ->
                                providerRepository.findDetailsById(session, authentication.getName())
                                        .thenApply(serviceProviderMapper::mapToServiceProviderDTO))));
    }

    @Override
    public Mono<Pageable<ServiceProviderReviewDTO>> findAllServiceProviderReviewsById(String id, PageInfo pageInfo, Double rating) {

        ServiceProviderReviewCacheKey cacheKey = new ServiceProviderReviewCacheKey();
        cacheKey.setServiceProviderId(id);
        cacheKey.setPageInfo(pageInfo);
        cacheKey.setRating(rating);

        return serviceProviderReviewCacheService.getCachedPageable(cacheKey)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.fromCompletionStage(sessionFactory.withSession(session -> serviceProviderReviewRepository.findAllReviewByServiceProviderId(session, pageInfo, id, rating)
                                                .thenApply(reviews -> reviews.stream()
                                                        .map(serviceProviderReviewMapper::mapToServiceProviderReviewDTO)
                                                        .collect(Collectors.toList())))
                                        .thenCombine(sessionFactory.withSession(session -> serviceProviderReviewRepository.countByServiceProviderId(session, id, rating)),
                                                (reviews, totalElements) -> new Pageable<>(reviews,
                                                        pageInfo.getPageSize(),
                                                        pageInfo.getPage(),
                                                        totalElements.intValue())))
                                .flatMap(serviceProviderReviewDTOPageable -> {
                                    return Mono.just(serviceProviderReviewDTOPageable)
                                            .flatMap(pageable -> {
                                                return Flux.fromIterable(pageable.getItems())
                                                        .flatMap(serviceProviderReviewDTO -> {
                                                            //Find customer details by service provider review customer
                                                            Mono<UserRepresentation> userRepresentationMono = accountService.findById(serviceProviderReviewDTO.getCustomer()
                                                                    .getId());

                                                            return userRepresentationMono.map(userRepresentation -> {
                                                                CustomerDTO customerDTO = serviceProviderReviewDTO.getCustomer();
                                                                if (customerDTO != null) {
                                                                    customerDTO.setFirstName(userRepresentation.getFirstName());
                                                                    customerDTO.setLastName(userRepresentation.getLastName());
                                                                }
                                                                return serviceProviderReviewDTO;
                                                            });
                                                        })
                                                        .collectList();
                                            })
                                            .map(serviceProviderReviewDTOS -> {
                                                serviceProviderReviewDTOPageable.setItems(serviceProviderReviewDTOS);
                                                return serviceProviderReviewDTOPageable;
                                            });
                                })
                                .flatMap(serviceProviderReviewDTOPageable -> serviceProviderReviewCacheService.updateCachedPageable(cacheKey, serviceProviderReviewDTOPageable))));
    }

    @Override
    public Mono<Long> countServiceProviderReviewsById(String id) {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> serviceProviderReviewRepository.countByServiceProviderId(session, id, null)));
    }

    @Override
    public Mono<List<ServiceProviderReviewCountGroupByRatingDTO>> countCurrentServiceProviderReviews() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication ->
                        Mono.fromCompletionStage(sessionFactory.withSession(session -> {
                            return serviceProviderReviewRepository.countReviewsByServiceProviderIdGroupByRating(session, authentication.getName());
                        })));
    }

    @Override
    public Mono<List<ServiceProviderAppointmentCountGroupByDayDTO>> countCurrentServiceProviderAppointments(AppointmentStatus status, Integer day){
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> Mono.fromCompletionStage(sessionFactory.withSession(session -> {
                    return appointmentRepository.countAppointmentsByServiceProviderIdGroupByDay(session, authentication.getName(), status, day);
                })));
    }

    @Override
    public  Mono<List<ServiceProviderCountGroupByDayDTO>> countServiceProviders(Integer day){
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> providerRepository.countServiceProvidersGroupByDay(session, day)));

    }

    @Override
    public Mono<List<ServiceDTO>> findAllProviderServicesById(String serviceProviderId) {
        Mono<List<ServiceDTO>> providerServicesByIdMono = Mono.fromCompletionStage(sessionFactory.withSession(session ->
                        serviceDetailsRepository.findAllByServiceProviderId(serviceProviderId, session)
                                .thenApply(services -> services.stream()
                                        .map(serviceDetailsMapper::mapToServiceDTO)
                                        .collect(Collectors.toList()))))
                .flatMap(services -> serviceDTOCacheService.updateCachedList(serviceProviderId, services));

        return serviceDTOCacheService.getCachedList(serviceProviderId)
                .switchIfEmpty(Mono.defer(() -> providerServicesByIdMono));
    }

    @Override
    public Mono<Void> save(ServiceProviderOnboardInput serviceProviderInput) {

        return Mono.fromCallable(() -> serviceProviderMapper.mapToServiceProviderEntity(serviceProviderInput))
                .flatMap(serviceProvider -> ReactiveSecurityContextHolder.getContext()
                        .map(SecurityContext::getAuthentication)
                        .flatMap(authentication -> {
                            serviceProvider.setId(authentication.getName());
                            return Mono.fromCompletionStage(sessionFactory.withTransaction((session, transaction) -> {
                                        return providerRepository.save(session, serviceProvider)
                                                .thenCompose(v -> providerBusinessRepository.findAllByIds(session, serviceProviderInput.getBasicInformation()
                                                        .getServiceDetails()
                                                        .getServices()
                                                        .stream()
                                                        .map(ServiceInput::getId)
                                                        .collect(Collectors.toList())))
                                                .thenCompose(services -> {
                                                    //Set services
                                                    Set<ServiceDetails> serviceDetailsList = new HashSet<>();
                                                    for (com.lfhardware.provider_business.domain.Service service : services) {
                                                        ServiceDetails serviceDetails = new ServiceDetails();
                                                        serviceDetails.setService(service);
                                                        serviceDetailsList.add(serviceDetails);
                                                    }
                                                    serviceProvider.addServiceDetailsList(serviceDetailsList);
                                                    return providerRepository.merge(session, serviceProvider);
                                                })
                                                .thenCompose(v -> stateRepository.findAllByIds(session, serviceProviderInput.getBasicInformation()
                                                        .getServiceDetails()
                                                        .getCoverage()
                                                        .getStates()
                                                        .stream()
                                                        .map(StateInput::getId)
                                                        .collect(Collectors.toList())))
                                                .thenCompose(states -> {
                                                    //Set State coverages
                                                    Set<StateCoverage> stateCoverages = new HashSet<>();
                                                    for (State state : states) {
                                                        StateCoverage stateCoverage = new StateCoverage();
                                                        stateCoverage.setState(state);
                                                        stateCoverages.add(stateCoverage);
                                                    }
                                                    serviceProvider.addStateCoverages(stateCoverages);
                                                    return providerRepository.merge(session, serviceProvider);
                                                });
                                    }))
                                    .flatMap(v -> roleService.findByName(Role.service_provider.name())
                                            .flatMap(roleRepresentations -> accountService.updateAccountRoleById(authentication.getName(), roleRepresentations)))
                                    .then();
                        }));
    }

    @Override
    public Mono<Void> updateById(String id, ServiceProviderDetailsInput serviceProviderDetailsInput) {
        return Mono.fromCompletionStage(sessionFactory.withTransaction((session, transaction) -> providerRepository.findById(session, id)
                        .thenCompose(provider -> {
                            provider.setName(serviceProviderDetailsInput.getName());
                            provider.setVerified(serviceProviderDetailsInput.isVerified());
                            provider.setPhoneNumber(serviceProviderDetailsInput.getContactInfo()
                                    .phoneNumber());
                            provider.setEmailAddress(serviceProviderDetailsInput.getContactInfo()
                                    .emailAddress());
                            provider.setOverview(serviceProviderDetailsInput.getOverview());
                            Address address = new Address();
                            address.setAddressLine1(serviceProviderDetailsInput.getAddress()
                                    .getAddressLine1());
                            address.setAddressLine2(serviceProviderDetailsInput.getAddress()
                                    .getAddressLine1());
                            address.setZipcode(serviceProviderDetailsInput.getAddress()
                                    .getZipcode());
                            address.setState(serviceProviderDetailsInput.getAddress()
                                    .getState());
                            address.setCity(serviceProviderDetailsInput.getAddress()
                                    .getCity());
                            provider.setAddress(address);
                            //provider.getStateCoverages().clear();
                            provider.getServiceDetails()
                                    .clear();
                            return providerRepository.save(session, provider)
                                    .thenApply(e -> session.flush())
//                                    .thenCompose(e->{
//                                        return stateRepository.findAllByIds(session, serviceProviderDetailsInput.getCoverage()
//                                                        .getStates().stream()
//                                                        .map(StateInput::getId).collect(Collectors.toList()))
//                                                .thenCompose(states->{
//                                                    Set<StateCoverage> stateCoverages = new HashSet<>();
//                                                    for (State state : states) {
//                                                        StateCoverageId stateCoverageId = new StateCoverageId();
//                                                        stateCoverageId.setStateId(state.getId());
//                                                        stateCoverageId.setServiceProviderId(provider.getId());
//                                                        StateCoverage stateCoverage = new StateCoverage();
////                                            stateCoverage.setStateCoverageId(stateCoverageId);
//                                                        stateCoverage.setServiceProvider(provider);
//                                                        stateCoverage.setState(state);
//                                                        stateCoverages.add(stateCoverage);
//                                                    }
//                                                    provider.addStateCoverages(stateCoverages);
//                                                    return providerRepository.save(session, provider);
//                                                });
//                                    })
                                    .thenCompose(e -> {
                                        return providerBusinessRepository.findAllByIds(session, serviceProviderDetailsInput.getServices()
                                                        .stream()
                                                        .map(ServiceInput::getId)
                                                        .collect(Collectors.toList()))
                                                .thenCompose(services -> {
                                                    Set<ServiceDetails> serviceDetailsList = new HashSet<>();
                                                    for (com.lfhardware.provider_business.domain.Service service : services) {
                                                        ServiceDetails serviceDetails = new ServiceDetails();
                                                        serviceDetails.setService(service);
                                                        serviceDetails.setServiceProvider(provider);
                                                        serviceDetailsList.add(serviceDetails);
                                                    }
                                                    provider.addServiceDetailsList(serviceDetailsList);
                                                    return providerRepository.save(session, provider);
                                                });
                                    })
                                    .thenAccept(e -> serviceProviderCacheService.removeAll());
                        })))
                .then();
    }

    @Override
    public Mono<Void> updateStatus(String id, Status status) {
        return Mono.fromCompletionStage(sessionFactory.withTransaction((session, transaction) -> providerRepository.updateStatus(session, id, status)))
                .flatMap(result -> {
                    this.serviceProviderCacheService.removeAll();
//                    if (result == 1) {
//                        return emailService.sendServiceProviderConfirmation("cheongkarwai10@gmail.com", status);
//                    }
                    return Mono.empty();
                })
                .then();
    }


    @Override
    public Mono<String> createPaymentDetails() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> Mono.fromCompletionStage(sessionFactory.withSession(session -> providerRepository.findDetailsById(session, authentication.getName())
                                .thenApply(ServiceProvider::getStripeAccountId)))
                        .flatMap(accountService::createStripeAccountOnboardingLink)
                        .switchIfEmpty(Mono.defer(() -> accountService.createStripeAccount()
                                .flatMap(account -> Mono.fromCompletionStage(sessionFactory.withTransaction(session -> providerRepository.findDetailsById(session, authentication.getName())
                                                .thenCompose(provider -> {
                                                    provider.setStripeAccountId(account.getId());
                                                    return providerRepository.save(session, provider);
                                                })
                                                .thenAccept((e) -> serviceProviderCacheService.removeAll())))
                                        .then(Mono.defer(() -> accountService.createStripeAccountOnboardingLink(account.getId())))))));
    }

    @Override
    public Mono<Boolean> findPaymentAccountStatus() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> Mono.fromCompletionStage(sessionFactory.withSession(session -> providerRepository.findDetailsById(session, authentication.getName())
                                .thenApply(ServiceProvider::getStripeAccountId)))
                        .flatMap(accountService::findStripeAccount)
                        .map(Account::getDetailsSubmitted)
                )
                .switchIfEmpty(Mono.defer(() -> Mono.just(false)));

    }
}
