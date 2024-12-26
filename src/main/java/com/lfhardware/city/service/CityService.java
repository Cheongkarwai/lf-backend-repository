package com.lfhardware.city.service;

import com.lfhardware.city.domain.City;
import com.lfhardware.city.dto.CityDTO;
import com.lfhardware.city.mapper.CityMapper;
import com.lfhardware.city.repository.ICityRepository;
import com.lfhardware.configuration.CacheConfiguration;
import org.hibernate.reactive.stage.Stage;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CityService {

    private final ICityRepository cityRepository;

    private final CityMapper cityMapper;

    private final Stage.SessionFactory sessionFactory;

    public CityService(ICityRepository cityRepository,
                       CityMapper cityMapper,
                       Stage.SessionFactory sessionFactory) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
        this.sessionFactory = sessionFactory;
    }

    @Cacheable(value = CacheConfiguration.citiesCache, key = "#root.methodName")
    public Flux<CityDTO> findAll() {
        return Mono.fromCompletionStage(sessionFactory.withSession(cityRepository::findAll))
                .flatMapMany(Flux::fromIterable)
                .switchIfEmpty(Flux.empty())
                .map(cityMapper::mapToCityDTO);
    }

    @Cacheable(value = CacheConfiguration.citiesCache, key = "#id", unless = "#result == null")
    public Mono<CityDTO> find(Long id) {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> cityRepository.findById(session, id)))
                .map(cityMapper::mapToCityDTO);
    }

    @Caching(evict = {
            @CacheEvict(value = CacheConfiguration.citiesCache, key = "'findAll'")
    }, put = {
//            @CachePut(value = CacheConfiguration.citiesCache, key = "#result.id", unless = "#result == null")
    })
    public Mono<CityDTO> save(CityDTO cityDTO) {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> {
            City city = cityMapper.mapToCity(cityDTO);
            return cityRepository.save(session, city)
                    .thenApply(v-> cityMapper.mapToCityDTO(city));
        }));
    }

    @Caching(
            put = {
                    @CachePut(value = CacheConfiguration.citiesCache, key = "#id"),
            },
            evict = {
                    @CacheEvict(value = CacheConfiguration.citiesCache, key = "'findAll'")
            })
    public Mono<CityDTO> update(Long id, CityDTO cityDTO) {
        return Mono.fromCompletionStage(sessionFactory.withTransaction(session -> cityRepository.findById(session, id)
                .thenApply(city -> {
                    city.setName(cityDTO.getName());
                    return city;
                })
                .thenCompose(city -> cityRepository.save(session, city)
                        .thenApply(v -> cityMapper.mapToCityDTO(city)))));
    }

    @Caching(evict = {
            @CacheEvict(value = CacheConfiguration.citiesCache, key = "#id"),
            @CacheEvict(value = CacheConfiguration.citiesCache, key = "'findAll'")
    })
    public Mono<Void> delete(Long id) {
        return Mono.fromCompletionStage(sessionFactory.withTransaction(session -> cityRepository.findById(session, id)
                        .thenCompose(city -> cityRepository.delete(session, city))))
                .then();
    }
}
