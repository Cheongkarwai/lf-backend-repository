package com.lfhardware.provider_business.cache;


import com.lfhardware.configuration.CacheConfiguration;
import com.lfhardware.provider_business.dto.ServiceDTO;
import com.lfhardware.provider_business.dto.ServiceGroupByCategoryDTO;
import com.lfhardware.shared.CacheService;
import com.lfhardware.shared.Pageable;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

@Service
public class BusinessServiceCacheService implements CacheService<ServiceDTO> {

    public static final String ID = CacheConfiguration.serviceCache;

    private final CacheManager cacheManager;

    public BusinessServiceCacheService(CacheManager cacheManager){
        this.cacheManager = cacheManager;
    }


    @Override
    public Mono<ServiceDTO> getCachedObject(Object key) {
        return Mono.fromCallable(()-> Objects.requireNonNull(cacheManager.getCache(ID))
                .get(key, ServiceDTO.class));
    }

    @Override
    public Mono<List<ServiceDTO>> getCachedList(Object key) {
        return Mono.fromCallable(()-> Objects.requireNonNull(cacheManager.getCache(ID))
                        .get(key, (Callable<List<ServiceDTO>>) ArrayList::new))
                .flatMap(cache -> CollectionUtils.isNotEmpty(cache) ?
                        Mono.just(cache) : Mono.empty());
    }

    @Override
    public Mono<Pageable<ServiceDTO>> getCachedPageable(Object key) {
        return Mono.fromCallable(()-> Objects.requireNonNull(cacheManager.getCache(ID))
                .get(key, (Callable<Pageable<ServiceDTO>>) Pageable::new))
                .flatMap(cache -> CollectionUtils.isNotEmpty(cache.getItems()) ?
                        Mono.just(cache) : Mono.empty());
    }

    @Override
    public Mono<ServiceDTO> updateCachedObject(Object key, ServiceDTO obj) {
        return Mono.fromCallable(()->{
            Objects.requireNonNull(cacheManager.getCache(ID))
                    .put(key, obj);

            return obj;
        });
    }

    @Override
    public Mono<List<ServiceDTO>> updateCachedList(Object key, List<ServiceDTO> objs) {
        return Mono.fromCallable(()->{
            Objects.requireNonNull(cacheManager.getCache(ID))
                    .put(key, objs);

            return objs;
        });
    }

    @Override
    public Mono<Pageable<ServiceDTO>> updateCachedPageable(Object key, Pageable<ServiceDTO> pageable) {
        return Mono.fromCallable(()->{
            Objects.requireNonNull(cacheManager.getCache(ID))
                    .put(key, pageable);

            return pageable;
        });
    }

    public Mono<List<ServiceGroupByCategoryDTO>> getCachedServiceGroupByCategory(String key){
        return Mono.fromCallable(()-> Objects.requireNonNull(cacheManager.getCache(ID))
                        .get(key, (Callable<List<ServiceGroupByCategoryDTO>>) ArrayList::new))
                .flatMap(cache -> CollectionUtils.isNotEmpty(cache) ?
                        Mono.just(cache) : Mono.empty());
    }
    public Mono<List<ServiceGroupByCategoryDTO>> updateCachedServiceGroupByCategoryDTO(String key, List<ServiceGroupByCategoryDTO> serviceGroupByCategoryDTOList){
        return Mono.fromCallable(()->{
            Objects.requireNonNull(cacheManager.getCache(ID))
                    .put(key,serviceGroupByCategoryDTOList);
            return serviceGroupByCategoryDTOList;
        });
    }

    public Mono<Boolean> remove(Object key){
        return Mono.fromCallable(()-> Objects.requireNonNull(this.cacheManager.getCache(ID))
                .evictIfPresent(key));
    }
    public boolean removeAll(){
        return Objects.requireNonNull(cacheManager.getCache(ID))
                .invalidate();
    }
}
