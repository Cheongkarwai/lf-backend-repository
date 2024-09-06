package com.lfhardware.country.api;

import com.lfhardware.country.service.CountryService;
import com.lfhardware.country.domain.Country;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CountryHandler {

    private final CountryService countryService;
    public CountryHandler(CountryService countryService){
        this.countryService = countryService;
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findAll(ServerRequest serverRequest){
        return ServerResponse.ok().body(countryService.findAll(),Country.class).log();
    }
}
