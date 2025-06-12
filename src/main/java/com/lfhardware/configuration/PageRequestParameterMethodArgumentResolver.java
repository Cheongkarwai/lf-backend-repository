package com.lfhardware.configuration;

import com.lfhardware.core.dto.PageRequest;
import com.lfhardware.core.repository.Sort;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;


public class PageRequestParameterMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(PageRequest.class);
    }

    @NotNull
    @Override
    public Mono<Object> resolveArgument(@NotNull MethodParameter parameter, @NotNull BindingContext bindingContext, ServerWebExchange exchange) {
       String page = Optional.ofNullable(exchange.getRequest().getQueryParams().getFirst("pageNo")).orElse("0");
       String pageSize = Optional.ofNullable(exchange.getRequest().getQueryParams().getFirst("pageSize")).orElse("10");
       String sort = exchange.getRequest().getQueryParams().getFirst("sort");

       PageRequest pageRequest = new PageRequest(Integer.parseInt(pageSize), Integer.parseInt(page),  new Sort(sort),null);

        return Mono.just(pageRequest);
    }

}
