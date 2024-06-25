package com.lfhardware.provider.cache;

import com.lfhardware.shared.CacheService;
import com.lfhardware.configuration.CacheConfiguration;
import com.lfhardware.provider.dto.ServiceProviderDetailsDTO;
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
public class ServiceProviderDetailsCacheService implements CacheService<ServiceProviderDetailsDTO> {

    public static final String ID = CacheConfiguration.serviceProviderDetailsCache;

    private final CacheManager cacheManager;
    public ServiceProviderDetailsCacheService(CacheManager cacheManager){
        this.cacheManager = cacheManager;
    }

    @Override
    public Mono<ServiceProviderDetailsDTO> getCachedObject(Object key) {
        return Mono.fromCallable(()-> Objects.requireNonNull(cacheManager.getCache(ID))
                .get(key, ServiceProviderDetailsDTO.class));
    }

    @Override
    public Mono<List<ServiceProviderDetailsDTO>> getCachedList(Object key) {
        return Mono.fromCallable(()-> Objects.requireNonNull(cacheManager.getCache(ID))
                        .get(key, (Callable<List<ServiceProviderDetailsDTO>>) ArrayList::new))
                .flatMap(cache -> CollectionUtils.isNotEmpty(cache) ?
                        Mono.just(cache) : Mono.empty());
    }

    @Override
    public Mono<Pageable<ServiceProviderDetailsDTO>> getCachedPageable(Object key) {
        return Mono.fromCallable(()-> Objects.requireNonNull(cacheManager.getCache(ID))
                .get(key, (Callable<Pageable<ServiceProviderDetailsDTO>>) Pageable::new))
                .flatMap(cache -> CollectionUtils.isNotEmpty(cache.getItems()) ?
                        Mono.just(cache) : Mono.empty());
    }

    @Override
    public Mono<ServiceProviderDetailsDTO> updateCachedObject(Object key, ServiceProviderDetailsDTO obj) {
        return Mono.fromCallable(()->{
            Objects.requireNonNull(cacheManager.getCache(ID))
                    .put(key, obj);

            return obj;
        });
    }

    @Override
    public Mono<List<ServiceProviderDetailsDTO>> updateCachedList(Object key, List<ServiceProviderDetailsDTO> objs) {
        return Mono.fromCallable(()->{
            Objects.requireNonNull(cacheManager.getCache(ID))
                    .put(key, objs);

            return objs;
        });
    }

    @Override
    public Mono<Pageable<ServiceProviderDetailsDTO>> updateCachedPageable(Object key, Pageable<ServiceProviderDetailsDTO> pageable) {
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
       return Objects.requireNonNull(cacheManager.getCache(ID))
               .invalidate();
    }
}
