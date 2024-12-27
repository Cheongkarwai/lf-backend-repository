package com.lfhardware.city.api;

import com.lfhardware.city.domain.City;
import com.lfhardware.city.dto.CityDTO;
import com.lfhardware.city.service.CityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public Mono<ServerResponse> findAll() {
        return ServerResponse.ok().body(cityService.findAll(), City.class);
    }

    @GetMapping("/{id}")
    public Mono<ServerResponse> findById(@PathVariable Long id) {
        return cityService.find(id)
                .flatMap(city -> ServerResponse.ok().body(Mono.just(city), City.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PostMapping
    public Mono<ServerResponse> save(@Valid @RequestBody Mono<CityDTO> cityDTOMono) {
        return cityDTOMono
                .flatMap(cityService::save)
                .flatMap(city -> ServerResponse.ok().body(Mono.just(city), CityDTO.class));
    }

    @PutMapping("/{id}")
    public Mono<ServerResponse> update(@PathVariable Long id, @Valid @RequestBody Mono<CityDTO> cityDTOMono) {
        return cityDTOMono
                .flatMap(cityDTO -> cityService.update(id, cityDTO))
                .flatMap(city -> ServerResponse.ok().body(Mono.just(city), CityDTO.class));
    }

    @DeleteMapping("/{id}")
    public Mono<ServerResponse> delete(@PathVariable Long id) {
        return cityService.delete(id)
                .then(Mono.defer(() -> ServerResponse.noContent().build()));
    }
}
