package com.lfhardware.city.api;

import com.lfhardware.city.domain.City;
import com.lfhardware.city.dto.CityDTO;
import com.lfhardware.city.service.CityService;
import com.lfhardware.core.exception.InputValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.hibernate.validator.spi.messageinterpolation.LocaleResolver;
import org.hibernate.validator.spi.messageinterpolation.LocaleResolverContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.server.i18n.LocaleContextResolver;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class CityApi {

    private final CityService cityService;

    private final org.springframework.validation.Validator validator;

    public CityApi(CityService cityService,
                   org.springframework.validation.Validator validator) {
        this.cityService = cityService;
        this.validator = validator;
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
                .doOnNext(cityDTO -> {
                    Errors errors = new BeanPropertyBindingResult(cityDTO, "cityDTO");
                    validator.validate(cityDTO, errors);
                    if(errors.hasErrors()){
                       throw new InputValidationException(errors, serverRequest.headers().acceptLanguage());
                    }
                })
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
                .then(Mono.defer(() -> ServerResponse.noContent().build()));
    }
}
