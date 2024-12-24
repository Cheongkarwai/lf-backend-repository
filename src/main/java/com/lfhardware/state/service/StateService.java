package com.lfhardware.state.service;

import com.lfhardware.city.dto.CityDTO;
import com.lfhardware.configuration.CacheConfiguration;
import com.lfhardware.state.dto.StateDTO;
import com.lfhardware.state.mapper.StateMapper;
import com.lfhardware.state.repository.IStateRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Persistence;
import org.hibernate.Cache;
import org.hibernate.reactive.stage.Stage;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Service
public class StateService {

    private final IStateRepository stateRepository;

    private final StateMapper stateMapper;

    private final Stage.SessionFactory sessionFactory;

    public StateService(IStateRepository stateRepository, StateMapper stateMapper, Stage.SessionFactory sessionFactory) {
        this.stateRepository = stateRepository;
        this.stateMapper = stateMapper;
        this.sessionFactory = sessionFactory;
    }

    @Cacheable(CacheConfiguration.statesCache)
    public Flux<StateDTO> findAll() {
       return Mono.fromCompletionStage(sessionFactory.withSession(stateRepository::findAll))
               .flatMapMany(Flux::fromIterable)
               .switchIfEmpty(Flux.empty())
               .map(stateMapper::mapToStateDTO);
    }
}
