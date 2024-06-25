package com.lfhardware.shared;

import com.lfhardware.shared.Pageable;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CacheService<T> {

    Mono<T> getCachedObject(Object key);

    Mono<List<T>> getCachedList(Object key);

    Mono<Pageable<T>> getCachedPageable(Object key);

    Mono<T> updateCachedObject(Object key, T obj);

    Mono<List<T>> updateCachedList(Object key, List<T> objs);

    Mono<Pageable<T>> updateCachedPageable(Object key, Pageable<T> pageable);

    Mono<Boolean> remove(Object key);
    boolean removeAll();
}
