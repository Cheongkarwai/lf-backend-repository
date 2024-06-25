package com.lfhardware.city.service;

import com.lfhardware.city.dto.CityDTO;
import com.lfhardware.city.mapper.CityMapper;
import com.lfhardware.city.repository.ICityRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Persistence;
import org.hibernate.reactive.stage.Stage;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Service
public class CityService {

    private final ICityRepository cityRepository;

    private final CityMapper cityMapper;

    private final Stage.SessionFactory sessionFactory;

    private final CacheManager cacheManager;

    public CityService(ICityRepository cityRepository, CityMapper cityMapper, CacheManager cacheManager, Stage.SessionFactory sessionFactory) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
        this.cacheManager = cacheManager;
        this.sessionFactory = sessionFactory;
    }

    public Mono<List<CityDTO>> findAll() {
        return Mono.justOrEmpty(cacheManager.getCache("cityCache").get("cities", (Callable<List<CityDTO>>) ArrayList::new))
                .flatMap(cities->!cities.isEmpty() ? Mono.just(cities) : Mono.empty())
                .switchIfEmpty(Mono.defer(()->Mono.fromCompletionStage(
                        sessionFactory.withSession(session -> cityRepository.findAll(session).thenApply(dbCities -> {
                            List<CityDTO> cityDTOS = dbCities.stream().map(cityMapper::mapToCityDTO).collect(Collectors.toList());
                            cacheManager.getCache("cityCache").put("cities", cityDTOS);
                            return cityDTOS;
                        })))));
    }
}
