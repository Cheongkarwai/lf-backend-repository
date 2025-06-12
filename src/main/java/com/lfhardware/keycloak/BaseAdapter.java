package com.lfhardware.keycloak;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@Slf4j
public abstract class BaseAdapter {

    protected WebClient webClient;

    public BaseAdapter(WebClient webClient) {
        this.webClient = webClient;
    }

    protected <T> Mono<T> get(String path,
                              MultiValueMap<String, String> queryParams,
                              Consumer<HttpHeaders> httpHeadersConsumer,
                              Class<T> responseType) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(path).queryParams(queryParams).build())
                .headers(httpHeadersConsumer)
                .exchangeToMono(response -> response.bodyToMono(responseType));
    }

    protected <T> Flux<T> getAll(String path,
                                 MultiValueMap<String, String> queryParams,
                                 Consumer<HttpHeaders> httpHeadersConsumer,
                                 Class<T> responseType) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(path).queryParams(queryParams).build())
                .headers(httpHeadersConsumer)
                .exchangeToFlux(response -> response.bodyToFlux(responseType));
    }

    protected <T> Mono<T> post(String path,
                               BodyInserters.FormInserter<String> bodyInserter,
                               MultiValueMap<String, String> headers,
                               Class<T> responseType) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(path).build())
                .headers(httpHeadersConsumer -> httpHeadersConsumer.addAll(headers))
                .body(bodyInserter)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(responseType));
    }

    protected <T> Mono<T> post(String path,
                               Object body,
                               MultiValueMap<String, String> headers,
                               Class<T> responseType) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(path).build())
                .headers(httpHeadersConsumer -> httpHeadersConsumer.addAll(headers))
                .bodyValue(body)
                .exchangeToMono(clientResponse -> {
                    if(clientResponse.statusCode().is2xxSuccessful()) {
                        return clientResponse.bodyToMono(responseType);
                    }
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(err-> Mono.error(new RuntimeException(err)));
                })
                .doOnError(err-> log.error("Error occurred when calling {}. Error={}", path, err.getMessage(), err));
    }

    protected <T> Mono<T> put(String path,
                              Object body,
                              MultiValueMap<String, String> headers,
                              Class<T> responseType
    ){
        return webClient.put()
                .uri(uriBuilder -> uriBuilder.path(path).build())
                .headers(httpHeadersConsumer -> httpHeadersConsumer.addAll(headers))
                .bodyValue(body)
                .exchangeToMono(response -> response.bodyToMono(responseType));
    }

    protected <T> Mono<T> delete(String path,
                                 MultiValueMap<String, String> headers,
                                 Class<T> responseType) {
        return webClient.delete()
                .uri(uriBuilder -> uriBuilder.path(path).build())
                .headers(httpHeadersConsumer -> httpHeadersConsumer.addAll(headers))
                .exchangeToMono(response -> response.bodyToMono(responseType));
    }

}
