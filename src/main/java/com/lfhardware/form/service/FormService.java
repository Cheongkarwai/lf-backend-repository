package com.lfhardware.form.service;

import com.lfhardware.authorization.dto.Scope;
import com.lfhardware.authorization.manager.PermissionManager;
import com.lfhardware.form.domain.Form;
import com.lfhardware.form.domain.FormId;
import com.lfhardware.form.dto.FormDTO;
import com.lfhardware.form.dto.FormInput;
import com.lfhardware.form.dto.FormPageRequest;
import com.lfhardware.form.mapper.FormMapper;
import com.lfhardware.form.repository.IFormRepository;
import com.lfhardware.provider.repository.IProviderRepository;
import com.lfhardware.provider_business.repository.IProviderBusinessRepository;
import com.lfhardware.shared.Pageable;
import org.casbin.jcasbin.main.Enforcer;
import org.hibernate.reactive.stage.Stage;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class FormService implements IFormService {

    private final IFormRepository formRepository;

    private final IProviderRepository providerRepository;

    private final IProviderBusinessRepository providerBusinessRepository;

    private final Stage.SessionFactory sessionFactory;

    private final FormMapper formMapper;

    private final Enforcer enforcer;

    private final CacheManager cacheManager;

    private final PermissionManager permissionManager;


    public FormService(IFormRepository formRepository,
                       IProviderRepository providerRepository,
                       IProviderBusinessRepository providerBusinessRepository,
                       Stage.SessionFactory sessionFactory,
                       FormMapper formMapper,
                       Enforcer enforcer,
                       CacheManager cacheManager,
                       PermissionManager permissionManager) {

        this.formRepository = formRepository;
        this.sessionFactory = sessionFactory;
        this.providerRepository = providerRepository;
        this.providerBusinessRepository = providerBusinessRepository;
        this.formMapper = formMapper;
        this.enforcer = enforcer;
        this.cacheManager = cacheManager;
        this.permissionManager = permissionManager;
    }

    private Mono<List<Long>> findPermission(String userId, String action) {
        return Mono.fromCallable(() -> enforcer.getFilteredNamedPolicy("p", 0, userId, "", action)
                .stream()
                .map(policies -> {
                    String[] ids = policies.get(1)
                            .split(",");
                    return Long.valueOf(ids[1]);
                })
                .toList());
    }

    @Override
    public Flux<Pageable<FormDTO>> findAll(FormPageRequest pageRequest) {

        List<FormDTO> accumulator = new ArrayList<>();
        AtomicLong countTotalElements = new AtomicLong();

        return permissionManager.isAuthenticated()
                .flatMap(isAuthenticated -> permissionManager.getName())
                .flatMap(userId -> Mono.zip(Mono.just(userId), findPermission(userId, Scope.FORM_READ.getScope())))
                .flatMap(tuple -> Mono.zip(Mono.just(tuple.getT1()), Mono.just(tuple.getT2()), Mono.fromCompletionStage(sessionFactory.withSession(session -> formRepository.countInIds(session, tuple.getT2())))))
                .flatMapMany(tuple -> {
                    countTotalElements.set(tuple.getT3());
                    return Flux.defer(() -> Mono.fromCompletionStage(sessionFactory.withSession(session -> formRepository.findAll(session, pageRequest, tuple.getT2())
                            .thenApply(forms -> forms.stream()
                                    .filter(form -> enforcer.enforce(tuple.getT1(), form.getService(), Scope.FORM_READ.getScope()))
                                    .map(formMapper::mapToFormDTO)
                                    .collect(Collectors.toList())))));
                })
                .map(formDTOList -> {
                    accumulator.addAll(formDTOList);
                    return new Pageable<>(accumulator, pageRequest.getPageSize(), pageRequest.getPage(), countTotalElements.intValue());
                })
                .doOnNext(e -> pageRequest.incrementPage())
                .repeat()
                .takeUntil(e -> accumulator.size() >= pageRequest.getPageSize() || (pageRequest.getPage() * pageRequest.getPageSize()) > countTotalElements.get());
    }


    //    @PreAuthorize("@formAuthorization.authorizeGetForm(#formId)")
    @Override
    public Mono<FormDTO> findById(Long id) {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> formRepository.findById(session, id)
                .thenApply(formMapper::mapToFormDTO)));
    }

    @Override
    public Mono<Void> save(Long serviceId, FormInput formInput) {

        return Mono.fromCompletionStage(sessionFactory.withTransaction((session, transaction) -> providerBusinessRepository.findById(session, serviceId)
                .thenCompose(business -> {
                    Form form = new Form();
                    //form.setService(business);
                    form.setId(serviceId);
                    form.setService(business);
                    form.setConfiguration(formInput.getForm());
                    return formRepository.merge(session, form);
                }))).then();
    }
//                    Form form = new Form();
//                    FormId formId = new FormId();
//                    formId.setServiceId(formInput.getServiceId());
//                    formId.setUserId(formInput.getServiceProviderId());
//                    form.setService(providerBusiness);
//                    form.setServiceProvider(provider);
//                    form.setFormId(formId);
//                    form.setConfiguration(formInput.getForm());
//                    return formRepository.merge(session, form);
//                })));

//        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
//                .map(Principal::getName).flatMap(name -> Mono.fromCallable(() -> {
//                    try {
//                        enforcer.addNamedPolicy("p", name, "\"" + formInput.getServiceProviderId() + "," + formInput.getServiceId() + "\"", Scope.FORM_READ.getScope());
//                        enforcer.addNamedPolicy("p", name, "\"" + formInput.getServiceProviderId() + "," + formInput.getServiceId() + "\"", Scope.FORM_WRITE.getScope());
//                    } catch (Exception e) {
//                        return Mono.error(e);
//                    }
//                    return Mono.empty();
//                }).flatMap(e -> saveFormMono)).then();

//        Mono<CompletionStage<Form>> saveFormMono = Mono.fromCompletionStage(sessionFactory.withSession((session) -> providerRepository.findById(session, formInput.getServiceProviderId()))
//                .thenCombine(sessionFactory.withSession(session -> providerBusinessRepository.findById(session, formInput.getServiceId())), (provider, providerBusiness) -> sessionFactory.withTransaction((session, transaction) -> {
//                    Form form = new Form();
//                    FormId formId = new FormId();
//                    formId.setServiceId(formInput.getServiceId());
//                    formId.setServiceProviderId(formInput.getServiceProviderId());
//                    form.setService(providerBusiness);
//                    form.setServiceProvider(provider);
//                    form.setFormId(formId);
//                    form.setConfiguration(formInput.getForm());
//                    return formRepository.merge(session, form);
//                })));
//
//        return ReactiveSecurityContextHolder.getContext().map(securityContext -> (Jwt) securityContext.getAuthentication().getPrincipal())
//                .map(jwt ->  Mono.fromCallable(() -> {
//                    System.out.println("Jwt Subject:" + jwt.getSubject());
//                    //enforcer.getImplicitUsersForRole();
//                    return enforcer.addPolicy(jwt.getSubject(), Domain.FORM.name(), formInput.getServiceProviderId() + "," +
//                            formInput.getServiceId(), "read");
//                }).subscribeOn(Schedulers.boundedElastic()).flatMap(isPolicySaved-> {
//                    if(isPolicySaved){
//                        return saveFormMono;
//                    }
//                    return Mono.empty();
//                })).then();

}
