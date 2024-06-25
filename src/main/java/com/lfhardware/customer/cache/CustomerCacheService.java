package com.lfhardware.customer.cache;

import com.lfhardware.configuration.CacheConfiguration;
import com.lfhardware.customer.dto.CustomerDTO;
import com.lfhardware.provider.dto.ServiceProviderDetailsDTO;
import com.lfhardware.shared.CacheService;
import com.lfhardware.shared.Pageable;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

@Service
public class CustomerCacheService implements CacheService<CustomerDTO> {

    public static final String ID = CacheConfiguration.customerCache;
    private final CacheManager cacheManager;

    public CustomerCacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public Mono<CustomerDTO> getCachedObject(Object key) {
        return null;
    }

    @Override
    public Mono<List<CustomerDTO>> getCachedList(Object key) {
        return null;
    }

    @Override
    public Mono<Pageable<CustomerDTO>> getCachedPageable(Object key) {
        return Mono.fromCallable(()-> Objects.requireNonNull(cacheManager.getCache(ID))
                        .get(key, (Callable<Pageable<CustomerDTO>>) Pageable::new))
                .flatMap(cache -> {
                    System.out.println(cache.getItems());
                    return CollectionUtils.isNotEmpty(cache.getItems()) ?
                            Mono.just(cache) : Mono.empty();
                });
    }

    @Override
    public Mono<CustomerDTO> updateCachedObject(Object key, CustomerDTO obj) {
        return null;
    }

    @Override
    public Mono<List<CustomerDTO>> updateCachedList(Object key, List<CustomerDTO> objs) {
        return null;
    }

    @Override
    public Mono<Pageable<CustomerDTO>> updateCachedPageable(Object key, Pageable<CustomerDTO> pageable) {
        return Mono.fromCallable(() -> {
            Objects.requireNonNull(cacheManager.getCache(ID))
                    .put(key, pageable);

            return pageable;
        });
    }

    @Override
    public Mono<Boolean> remove(Object key) {
        return Mono.fromCallable(() -> Objects.requireNonNull(cacheManager.getCache(ID))
                .evictIfPresent(key));
    }

    @Override
    public boolean removeAll() {
        return Objects.requireNonNull(cacheManager.getCache(ID))
                .invalidate();
    }
}
