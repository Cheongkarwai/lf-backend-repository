package com.lfhardware.provider.cache;

import com.lfhardware.shared.CacheService;
import com.lfhardware.configuration.CacheConfiguration;
import com.lfhardware.provider.dto.ServiceProviderReviewDTO;
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
public class ServiceProviderReviewCacheService implements CacheService<ServiceProviderReviewDTO> {

    public static final String ID = CacheConfiguration.serviceProviderReviewCache;

    private final CacheManager cacheManager;

    public ServiceProviderReviewCacheService(CacheManager cacheManager){
        this.cacheManager = cacheManager;
    }

    @Override
    public Mono<ServiceProviderReviewDTO> getCachedObject(Object key) {
        return Mono.fromCallable(()-> Objects.requireNonNull(cacheManager.getCache(ID))
                .get(key, ServiceProviderReviewDTO.class));
    }

    @Override
    public Mono<List<ServiceProviderReviewDTO>> getCachedList(Object key) {
        return Mono.fromCallable(()-> Objects.requireNonNull(cacheManager.getCache(ID))
                        .get(key, (Callable<List<ServiceProviderReviewDTO>>) ArrayList::new))
                .flatMap(cache -> CollectionUtils.isNotEmpty(cache) ?
                        Mono.just(cache) : Mono.empty());
    }

    @Override
    public Mono<Pageable<ServiceProviderReviewDTO>> getCachedPageable(Object key) {
        return Mono.fromCallable(()-> Objects.requireNonNull(cacheManager.getCache(ID))
                        .get(key, (Callable<Pageable<ServiceProviderReviewDTO>>) Pageable::new))
                .flatMap(cache -> CollectionUtils.isNotEmpty(cache.getItems()) ?
                        Mono.just(cache) : Mono.empty());
    }

    @Override
    public Mono<ServiceProviderReviewDTO> updateCachedObject(Object key, ServiceProviderReviewDTO obj) {
        return Mono.fromCallable(()->{
            Objects.requireNonNull(cacheManager.getCache(ID))
                    .put(key, obj);

            return obj;
        });
    }

    @Override
    public Mono<List<ServiceProviderReviewDTO>> updateCachedList(Object key, List<ServiceProviderReviewDTO> objs) {
        return Mono.fromCallable(()->{
            Objects.requireNonNull(cacheManager.getCache(ID))
                    .put(key, objs);

            return objs;
        });
    }

    @Override
    public Mono<Pageable<ServiceProviderReviewDTO>> updateCachedPageable(Object key, Pageable<ServiceProviderReviewDTO> pageable) {
        return Mono.fromCallable(()->{
            Objects.requireNonNull(cacheManager.getCache(ID))
                    .put(key, pageable);

            return pageable;
        });
    }

    public Mono<Boolean> remove(Object key){
        return Mono.fromCallable(()-> Objects.requireNonNull(this.cacheManager.getCache(ID))
                .evictIfPresent(key));
    }

    public boolean removeAll(){
        return Objects.requireNonNull(this.cacheManager.getCache(ID))
                .invalidate();
    }
}
