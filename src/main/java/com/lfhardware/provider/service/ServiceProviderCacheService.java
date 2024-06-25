package com.lfhardware.provider.service;

import com.lfhardware.shared.CacheService;
import com.lfhardware.configuration.CacheConfiguration;
import com.lfhardware.provider.dto.ServiceProviderDTO;
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
public class ServiceProviderCacheService implements CacheService<ServiceProviderDTO> {

    public static final String ID = CacheConfiguration.serviceProviderCache;

    private final CacheManager cacheManager;

    public ServiceProviderCacheService(CacheManager cacheManager){
        this.cacheManager = cacheManager;
    }

    @Override
    public Mono<ServiceProviderDTO> getCachedObject(Object key) {
        return Mono.fromCallable(()-> Objects.requireNonNull(cacheManager.getCache(ID))
                .get(key, ServiceProviderDTO.class));
    }

    @Override
    public Mono<List<ServiceProviderDTO>> getCachedList(Object key) {
        return Mono.fromCallable(()-> Objects.requireNonNull(cacheManager.getCache(ID))
                        .get(key, (Callable<List<ServiceProviderDTO>>) ArrayList::new))
                .flatMap(providerServicesCache -> CollectionUtils.isNotEmpty(providerServicesCache) ?
                        Mono.just(providerServicesCache) : Mono.empty());
    }

    @Override
    public Mono<Pageable<ServiceProviderDTO>> getCachedPageable(Object key) {
        return Mono.fromCallable(()-> Objects.requireNonNull(cacheManager.getCache(ID))
                .get(key, (Callable<Pageable<ServiceProviderDTO>>) Pageable::new))
                .flatMap(cache -> CollectionUtils.isNotEmpty(cache.getItems()) ?
                        Mono.just(cache) : Mono.empty());
    }

    @Override
    public Mono<ServiceProviderDTO> updateCachedObject(Object key, ServiceProviderDTO obj) {
        return Mono.fromCallable(()->{
            Objects.requireNonNull(cacheManager.getCache(ID))
                    .putIfAbsent(key, obj);

            return obj;
        });
    }

    @Override
    public Mono<List<ServiceProviderDTO>> updateCachedList(Object key, List<ServiceProviderDTO> objs) {
        return Mono.fromCallable(()->{
            Objects.requireNonNull(cacheManager.getCache(ID))
                    .putIfAbsent(key, objs);

            return objs;
        });
    }

    @Override
    public Mono<Pageable<ServiceProviderDTO>> updateCachedPageable(Object key, Pageable<ServiceProviderDTO> pageable) {
        return Mono.fromCallable(()->{
            Objects.requireNonNull(cacheManager.getCache(ID))
                    .putIfAbsent(key, pageable);

            return pageable;
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
