package com.lfhardware.state.api;

import com.lfhardware.city.domain.City;
import com.lfhardware.state.service.StateService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class StateHandler {

    private final StateService stateService;
    public StateHandler(StateService stateService){
        this.stateService = stateService;
    }
    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findAll(ServerRequest serverRequest){
        return ServerResponse.ok().body(stateService.findAll(),City.class).log();
    }
}
