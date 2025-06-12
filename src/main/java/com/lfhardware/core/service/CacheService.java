package com.lfhardware.core.service;

import com.lfhardware.core.dto.Page;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CacheService<T> {

    Mono<T> getCachedObject(Object key);

    Mono<List<T>> getCachedList(Object key);

    Mono<Page<T>> getCachedPageable(Object key);

    Mono<T> updateCachedObject(Object key, T obj);

    Mono<List<T>> updateCachedList(Object key, List<T> objs);

    Mono<Page<T>> updateCachedPageable(Object key, Page<T> pageable);

    Mono<Boolean> remove(Object key);
    boolean removeAll();
}
