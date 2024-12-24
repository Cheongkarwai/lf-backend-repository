package com.lfhardware.country.service;

import com.lfhardware.configuration.CacheConfiguration;
import com.lfhardware.country.dto.CountryDTO;
import com.lfhardware.country.mapper.CountryMapper;
import com.lfhardware.country.repository.ICountryRepository;
import org.hibernate.reactive.stage.Stage;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CountryService {

    private final ICountryRepository countryRepository;

    private final CountryMapper countryMapper;

    private final Stage.SessionFactory sessionFactory;


    public CountryService(ICountryRepository countryRepository,
                          CountryMapper countryMapper,
                          Stage.SessionFactory sessionFactory) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
        this.sessionFactory = sessionFactory;
    }

    @Cacheable(CacheConfiguration.countriesCache)
    public Flux<CountryDTO> findAll() {
        return Mono.fromCompletionStage(sessionFactory.withSession(countryRepository::findAll))
                .flatMapMany(Flux::fromIterable)
                .switchIfEmpty(Flux.empty())
                .map(countryMapper::mapToCountryDTO);
    }
}
