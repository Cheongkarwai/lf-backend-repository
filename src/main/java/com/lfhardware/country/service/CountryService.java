package com.lfhardware.country.service;

import com.lfhardware.country.dto.CountryDTO;
import com.lfhardware.country.mapper.CountryMapper;
import com.lfhardware.country.repository.ICountryRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Persistence;
import org.hibernate.SessionFactory;
import org.hibernate.reactive.stage.Stage;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Service
public class CountryService {

    private final ICountryRepository countryRepository;

    private final CountryMapper countryMapper;

    private final Stage.SessionFactory sessionFactory;

    private final CacheManager cacheManager;


    public CountryService(ICountryRepository countryRepository, CountryMapper countryMapper, Stage.SessionFactory sessionFactory, CacheManager cacheManager) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
        this.sessionFactory = sessionFactory;
        this.cacheManager = cacheManager;
    }

    public Mono<List<CountryDTO>> findAll() {
        return Mono.justOrEmpty(cacheManager.getCache("countryCache").get("countries", (Callable<List<CountryDTO>>) ArrayList::new))
                .flatMap(cities->!cities.isEmpty() ? Mono.just(cities) : Mono.empty()).switchIfEmpty(Mono.defer(()->Mono.fromCompletionStage(
                        sessionFactory.withSession(session -> countryRepository.findAll(session).thenApply(dbCities -> {
                            List<CountryDTO> cityDTOS = dbCities.stream().map(countryMapper::mapToCountryDTO).collect(Collectors.toList());
                            cacheManager.getCache("countryCache").put("countries", cityDTOS);
                            return cityDTOS;
                        })))));
    }
}
