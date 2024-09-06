package com.lfhardware.city.api;

import com.lfhardware.city.service.CityService;
import com.lfhardware.city.domain.City;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CityHandler {

    private final CityService cityService;
    public CityHandler(CityService cityService){
        this.cityService = cityService;
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findAll(ServerRequest serverRequest){
        return ServerResponse.ok().body(cityService.findAll(),City.class).log();
    }
}
