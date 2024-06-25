package com.lfhardware.appointment.cache;

import com.lfhardware.appointment.dto.AppointmentDTO;
import com.lfhardware.configuration.CacheConfiguration;
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
public class AppointmentCacheService implements CacheService<AppointmentDTO> {

    public static final String ID = CacheConfiguration.appointmentCache;

    private final CacheManager cacheManager;
    public AppointmentCacheService(CacheManager cacheManager){
        this.cacheManager = cacheManager;
    }
    @Override
    public Mono<AppointmentDTO> getCachedObject(Object key) {
        return Mono.fromCallable(()-> Objects.requireNonNull(cacheManager.getCache(ID))
                .get(key, AppointmentDTO.class));
    }

    @Override
    public Mono<List<AppointmentDTO>> getCachedList(Object key) {
        return Mono.fromCallable(()-> Objects.requireNonNull(cacheManager.getCache(ID))
                        .get(key, (Callable<List<AppointmentDTO>>) ArrayList::new))
                .flatMap(cache -> CollectionUtils.isNotEmpty(cache) ?
                        Mono.just(cache) : Mono.empty());
    }

    @Override
    public Mono<Pageable<AppointmentDTO>> getCachedPageable(Object key) {
        return Mono.fromCallable(()-> Objects.requireNonNull(cacheManager.getCache(ID))
                .get(key, (Callable<Pageable<AppointmentDTO>>) Pageable::new))
                .flatMap(cache -> CollectionUtils.isNotEmpty(cache.getItems()) ?
                        Mono.just(cache) : Mono.empty());
    }

    @Override
    public Mono<AppointmentDTO> updateCachedObject(Object key, AppointmentDTO obj) {
        return Mono.fromCallable(()->{
            Objects.requireNonNull(cacheManager.getCache(ID))
                    .put(key, obj);

            return obj;
        });
    }

    @Override
    public Mono<List<AppointmentDTO>> updateCachedList(Object key, List<AppointmentDTO> objs) {
        return Mono.fromCallable(()->{
            Objects.requireNonNull(cacheManager.getCache(ID))
                    .put(key, objs);

            return objs;
        });
    }

    @Override
    public Mono<Pageable<AppointmentDTO>> updateCachedPageable(Object key, Pageable<AppointmentDTO> pageable) {
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
