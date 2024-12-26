package com.lfhardware.city.api;

import com.lfhardware.city.domain.City;
import com.lfhardware.city.dto.CityDTO;
import com.lfhardware.city.service.CityService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CityApi {

    private final CityService cityService;

    public CityApi(CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     */
    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ServerResponse.ok().body(cityService.findAll(), City.class);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        return cityService.find(Long.valueOf(serverRequest.pathVariable("id")))
                .flatMap(city -> ServerResponse.ok().body(Mono.just(city), City.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CityDTO.class)
                .flatMap(cityService::save)
                .flatMap(city -> ServerResponse.ok().body(Mono.just(city), CityDTO.class));
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CityDTO.class)
                .flatMap(cityDTO -> cityService.update(Long.valueOf(serverRequest.pathVariable("id")), cityDTO))
                .flatMap(city -> ServerResponse.ok().body(Mono.just(city), CityDTO.class));
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return cityService.delete(Long.valueOf(serverRequest.pathVariable("id")))
                .flatMap(cityDTO -> ServerResponse.noContent().build());
    }
}
