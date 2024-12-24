package com.lfhardware.city.service;

import com.lfhardware.city.dto.CityDTO;
import com.lfhardware.city.mapper.CityMapper;
import com.lfhardware.city.repository.ICityRepository;
import com.lfhardware.configuration.CacheConfiguration;
import org.hibernate.reactive.stage.Stage;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(CacheConfiguration.citiesCache)
    public Flux<CityDTO> findAll() {
        return Mono.fromCompletionStage(sessionFactory.withSession(cityRepository::findAll))
                .flatMapMany(Flux::fromIterable)
                .switchIfEmpty(Flux.empty())
                .map(cityMapper::mapToCityDTO);
    }
}
