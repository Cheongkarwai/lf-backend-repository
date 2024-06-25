package com.lfhardware.state.service;

import com.lfhardware.city.dto.CityDTO;
import com.lfhardware.state.dto.StateDTO;
import com.lfhardware.state.mapper.StateMapper;
import com.lfhardware.state.repository.IStateRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Persistence;
import org.hibernate.Cache;
import org.hibernate.reactive.stage.Stage;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Service
public class StateService {

    private final IStateRepository stateRepository;

    private final StateMapper stateMapper;

    private Stage.SessionFactory sessionFactory;

    private final CacheManager cacheManager;

    @PostConstruct
    public void init() {
        this.sessionFactory = Persistence.createEntityManagerFactory("postgres").unwrap(Stage.SessionFactory.class);
    }

    public StateService(IStateRepository stateRepository, StateMapper stateMapper, CacheManager cacheManager) {
        this.stateRepository = stateRepository;
        this.stateMapper = stateMapper;
        this.cacheManager = cacheManager;
    }

    public Mono<List<StateDTO>> findAll() {
        return Mono.justOrEmpty(cacheManager.getCache("stateCache").get("states", (Callable<List<StateDTO>>) ArrayList::new))
                .flatMap(states -> states.isEmpty() ? Mono.empty() :Mono.just(states)).switchIfEmpty(Mono.defer(()->Mono.fromCompletionStage(sessionFactory.withSession(session -> stateRepository.findAll(session)
                        .thenApply(states -> {
                            List<StateDTO> stateDTOS = states.stream().map(stateMapper::mapToStateDTO).collect(Collectors.toList());
                            cacheManager.getCache("stateCache").put("states", stateDTOS);
                            return stateDTOS;
                        })))));
    }
}
