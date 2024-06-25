package com.lfhardware.provider_business.service;

import com.lfhardware.provider_business.cache.BusinessServiceCacheService;
import com.lfhardware.provider_business.dto.ServiceDTO;
import com.lfhardware.provider_business.dto.ServiceDetailsDTO;
import com.lfhardware.provider_business.dto.ServiceGroupByCategoryDTO;
import com.lfhardware.provider_business.mapper.ServiceCategoryMapper;
import com.lfhardware.provider_business.mapper.ServiceDetailsMapper;
import com.lfhardware.provider_business.repository.IProviderBusinessCategoryRepository;
import com.lfhardware.provider_business.repository.IProviderBusinessRepository;
import com.lfhardware.shared.CacheService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.stage.Stage;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Service
@Slf4j
public class ProviderBusinessService implements IProviderBusinessService{

    private final IProviderBusinessCategoryRepository providerBusinessCategoryRepository;

    private final IProviderBusinessRepository providerBusinessRepository;

    private final ServiceCategoryMapper serviceCategoryMapper;

    private final ServiceDetailsMapper serviceDetailsMapper;

    private final Stage.SessionFactory sessionFactory;
    private final CacheService<ServiceDTO> businessServiceCacheService;

    public ProviderBusinessService(IProviderBusinessCategoryRepository providerBusinessCategoryRepository,
                                   IProviderBusinessRepository providerBusinessRepository,
                                   ServiceCategoryMapper serviceCategoryMapper,
                                   ServiceDetailsMapper serviceDetailsMapper,
                                   CacheService<ServiceDTO> businessServiceCacheService,
                                   Stage.SessionFactory sessionFactory){
        //Repository
        this.providerBusinessCategoryRepository = providerBusinessCategoryRepository;
        this.providerBusinessRepository = providerBusinessRepository;

        //Mapper
        this.serviceCategoryMapper = serviceCategoryMapper;
        this.serviceDetailsMapper = serviceDetailsMapper;

        //Cache
        this.businessServiceCacheService = businessServiceCacheService;

        this.sessionFactory = sessionFactory;
    }

    public Mono<List<ServiceGroupByCategoryDTO>> findAllServiceDetails(){

        BusinessServiceCacheService cacheService = (BusinessServiceCacheService) businessServiceCacheService;

        return cacheService.getCachedServiceGroupByCategory("serviceDetails")
                                .switchIfEmpty(Mono.defer(()-> Mono.fromCompletionStage(sessionFactory.withSession(session -> providerBusinessRepository.findAllGroupByCategory(session)
                                        .thenApply(serviceCategoryMapper::mapToServiceGroupByCategoryDTOs)))
                                        .flatMap(serviceGroupByCategoryDTOList -> {
                                            log.info("Saving service details cache");
                                            return cacheService.updateCachedServiceGroupByCategoryDTO("serviceDetails",serviceGroupByCategoryDTOList);
                                        })));
    }

    public Mono<ServiceDetailsDTO> findDetailsById(Long id){
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> providerBusinessRepository.findById(session, id)
                .thenApply(serviceDetailsMapper::mapToServiceDetailsDTO)));
    }

}
